package task;


import com.google.gson.Gson;
import log.LoggerFactory;
import org.eclipse.jetty.util.StringUtil;
import report.bean.CoreReportBean;
import staticPart.RedisCache;
import utils.DateTools;
import utils.MySQLHelper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class CoreReportTask extends TimerTask {
    private static final int PERIOD_DAY = 24 * 60 * 60 * 1000;

    private static final String coreReportKeyPre = "csbot:corereport:";

    public void run() {
        if (!RedisCache.INSTANCE.tryGetDistributedLock(getClass().getSimpleName(), "", 60 * 60)) {
            LoggerFactory.getLogger().info("获取定时任务锁失败");
            return;
        }
        // 获取 昨天的 日报表
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        Date yestoday = calendar.getTime();

        doQueryDayReport(yestoday);

        RedisCache.INSTANCE.releaseDistributedLock(getClass().getSimpleName(), "");

    }

    /***
     *
     */
    public void preInit(int reportTimeInterval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for (int i = 1; i <= reportTimeInterval; i++) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
            Date target = calendar.getTime();
            System.out.println(target.toString());
            String data = RedisCache.INSTANCE.get(CoreReportTask.generateKey(target));
            if (StringUtil.isBlank(data))
                doQueryDayReport(target);


        }

    }

    public static String generateKey(Date yestoday) {
        return coreReportKeyPre + DateTools.date2Str(yestoday, DateTools.DateFormat.DATE_FORMAT_request_day);
    }

    public static List<CoreReportBean> doQueryDayReport(Date date) {
        String dateStr = DateTools.date2Str(date, DateTools.DateFormat.DATE_FORMAT_request_day);
        List<CoreReportBean> ret = null;
        try {
            ret = MySQLHelper.getInstance().getReportDao().queryReport(DateTools.str2Date(dateStr, DateTools.DateFormat.DATE_FORMAT_request_day, true),
                    DateTools.str2Date(dateStr, DateTools.DateFormat.DATE_FORMAT_request_day, false),
                    null);
            RedisCache.INSTANCE.put(generateKey(date), new Gson().toJson(ret), 7 * PERIOD_DAY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
