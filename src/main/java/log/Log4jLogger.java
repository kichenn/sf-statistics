package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import type.sentence.IMutableSentence;

/**
 * Created by yuao on 4/9/17.
 */
public class Log4jLogger implements AbstractLogger{

    private Logger logger = null;
    private final static int depth = 3;
    	private final static String LOG_FORMAT = "- [ %-45s ] - %s";
    /**
     * construct abstractLogger
     */
    public Log4jLogger() {
        logger = LogManager.getLogger();
    }

    public final Logger getLogger() {
        return logger;
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#error(java.lang.String, java.lang.String)
     */
    public final int error(String msg) {
        logger.error(String.format(LOG_FORMAT, getStackTrace(depth), msg));
        return 1;
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#error(java.lang.String, java.lang.Exception)
     */
    public final int error(Exception ex) {
        logger.error(String.format(LOG_FORMAT, getStackTrace(depth), ex.getMessage()));
        return 1;
    }

    public final int warn(String msg) {
        logger.warn(String.format(LOG_FORMAT, getStackTrace(depth), msg));
        return 1;
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#debug(java.lang.String, java.lang.String)
     */
    public final int debug(String msg) {
        logger.debug(String.format(LOG_FORMAT, getStackTrace(depth), msg));
        return 1;
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#info(java.lang.String, java.lang.String)
     */
    public final int info(String msg) {
        logger.info(String.format(LOG_FORMAT, getStackTrace(depth), msg));
        return 1;
    }
    
    
    public final int error(String msg, IMutableSentence sentence) {
    		sentence.addNote(msg);
        return error(msg);
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#error(java.lang.String, java.lang.Exception)
     */
    public final int error(Exception ex, IMutableSentence sentence) {
    		sentence.addNote(ex.getMessage());
        return error(ex);
    }

    public final int warn(String msg, IMutableSentence sentence) {
    		sentence.addNote(msg);
        return warn(msg);
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#debug(java.lang.String, java.lang.String)
     */
    public final int debug(String msg, IMutableSentence sentence) {
    		sentence.addNote(msg);
        return debug(msg);
    }

    /* (non-Javadoc)
     * @see com.emotibot.memory.log.AbstractLogger#info(java.lang.String, java.lang.String)
     */
    public final int info(String msg, IMutableSentence sentence) {
    		sentence.addNote(msg);
        return info(msg);
    }


    protected final String getStackTrace(int depth) {
    		final String format = "%s.%s():%s";
        final Thread thread = Thread.currentThread();
        final StackTraceElement[] statckElements = thread.getStackTrace();
       
        if (statckElements.length > depth) {
            return String.format(format, statckElements[depth].getClassName(), statckElements[depth].getMethodName(), statckElements[depth].getLineNumber());
        }
        return null;
    }
}
