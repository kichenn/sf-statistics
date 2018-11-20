package log;

/**
 * Created by yuao on 4/9/17.
 */
public enum LoggerFactory {

    INSTANCE();

    private AbstractLogger logger;

    LoggerFactory() {
        logger = new Log4jLogger();
    }

    public static AbstractLogger getLogger() {
        return LoggerFactory.INSTANCE.logger;
    }

}
