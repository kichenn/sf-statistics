package log;


import type.sentence.IMutableSentence;

/**
 * Created by yuao on 4/9/17.
 */
public interface AbstractLogger {

    /**
     * log error message at level error
     *
     * @param msg
     * @return
     */
    public int error(String msg);

    /**
     * log exception at level error
     *
     * @param ex
     * @return
     */
    public int error(Exception ex);

    /**
     * info log at level info
     *
     * @param msg
     * @return
     */
    public int info(String msg);

    /**
     * debug log at level debug
     *
     * @param msg
     * @return
     */
    public int debug(String msg);

    public int warn(String msg);


    public int error(String msg, IMutableSentence sentence);

    /**
     * log exception at level error
     *
     * @param ex
     * @return
     */
    public int error(Exception ex, IMutableSentence sentence);

    /**
     * info log at level info
     *
     * @param msg
     * @return
     */
    public int info(String msg, IMutableSentence sentence);

    /**
     * debug log at level debug
     *
     * @param msg
     * @return
     */
    public int debug(String msg, IMutableSentence sentence);

    public int warn(String msg, IMutableSentence sentence);
}

