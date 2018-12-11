package report;

import org.apache.commons.lang.StringUtils;
import utils.HandlerUtilities;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

abstract class BaseReportRequest {
    public static final String tmpDir = "/tmp";
    protected String dateBegin;
    protected String dateEnd;

    protected Long dateBeginStr;
    protected Long dateEndStr;

    protected String isForceNOCache;
    protected List<String> channelIds;

    protected long startTime;


    public BaseReportRequest(HttpServletRequest request){
        startTime  = System.currentTimeMillis();
        String reqDateBegin = request.getParameter("beginDate");
        String reqDateEnd = request.getParameter("endDate");
        String channelIdList = request.getParameter("channelIds");
        String force = request.getParameter("force");
        if (HandlerUtilities.isValidParameter(reqDateBegin)) {
            dateBegin = reqDateBegin;
        }
        if (HandlerUtilities.isValidParameter(reqDateEnd)) {
            dateEnd = reqDateEnd;
        }

        if (HandlerUtilities.isValidParameter(request.getParameter("dateBeginLong"))) {
            dateBeginStr = Long.valueOf(request.getParameter("dateBeginLong"));
        }

        if (HandlerUtilities.isValidParameter(request.getParameter("dateEndLong"))) {
            dateEndStr = Long.valueOf(request.getParameter("dateEndLong"));
        }

        if (HandlerUtilities.isValidParameter(force)) {
            isForceNOCache = force;
        }
        if (StringUtils.isNotBlank(channelIdList)) {
            channelIds = new ArrayList<>();
            String[] lst = channelIdList.split(",");
            for (String i : lst) {
                if (HandlerUtilities.isValidParameter(i)) {
                    channelIds.add(i);
                }
            }
        }
    }



}
