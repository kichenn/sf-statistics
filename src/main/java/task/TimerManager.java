package task;

import config.ConfigManagedService;
import config.Constants;
import log.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;


public class TimerManager {

    private static int reportRunTime = ConfigManagedService.getConfig().getInteger(Constants.REPORT_RUN_TIME);
    private static int reportTimeInterval = ConfigManagedService.getConfig().getInteger(Constants.REPORT_TIME_INTERVAL);


    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;


    public static void init() {
        new TimerManager();
    }


    public TimerManager() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reportRunTime);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        Timer timer = new Timer();
        CoreReportTask task = new CoreReportTask();
        task.preInit(reportTimeInterval);
        LoggerFactory.getLogger().info("定时任务启动");
        timer.schedule(task, date, PERIOD_DAY);
    }

    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }
}
