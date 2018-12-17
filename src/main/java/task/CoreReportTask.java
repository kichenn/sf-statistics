package task;


import com.google.gson.Gson;
import report.bean.CoreReportBean;
import staticPart.RedisCache;
import type.sentence.ShunfengSentence;
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
        // 获取 昨天的 日报表
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        Date yestoday = calendar.getTime();
        doQueryDayReport(yestoday);

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
