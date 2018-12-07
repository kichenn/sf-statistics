package report;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import log.LoggerFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.CollectionUtils;
import report.bean.ChatRecordEntity;
import report.bean.CoreReportBean;
import report.bean.ReportResult;
import report.bean.SessionDetailItemDto;
import report.enums.ChannelIdNameEnums;
import report.enums.ReportResultEnums;
import utils.DateTools;
import utils.HandlerUtilities;
import utils.MySQLHelper;
import utils.StrUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SessionDetailRequest extends BaseReportRequest {
    protected String sessionId;


    public static final String listEntryPoint = "/v1/sf/sessionDetail";

    public SessionDetailRequest(HttpServletRequest request) {
        super(request);
        String sessionId = request.getParameter("sessionId");
        if (HandlerUtilities.isValidParameter(sessionId)) {
            this.sessionId = sessionId;
        }
    }


    public ReportResult list() {
        ReportResult result = new ReportResult(ReportResultEnums.SUCCESS);
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = DateTools.str2DateNormal(dateBegin, DateTools.DateFormat.DATE_FORMAT_4_get);
            endDate = DateTools.str2DateNormal(dateEnd, DateTools.DateFormat.DATE_FORMAT_4_get);
        } catch (Exception e) {
            return new ReportResult(ReportResultEnums.DATE_PARSE_EXCEPTION);
        }
        if (DateTools.gapDayOfTwo(beginDate, endDate) > 1L) {
            return new ReportResult(ReportResultEnums.DATE_SPAN_TOO_LONG);
        }

        HashMap<String, Object> req = new HashMap<String, Object>();
        String dateBeginStr = "";
        String dateEndStr = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dateBegin != null) {
            dateBeginStr = formatter.format(beginDate);
            req.put("beginDate", dateBeginStr);
        }
        if (dateEnd != null) {
            dateEndStr = formatter.format(endDate);
            req.put("endDate", dateEndStr);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            req.put("sessionId", sessionId);
        }

        List<SessionDetailItemDto> ret = MySQLHelper.getInstance().getChatRecordDao().querySessionDetail(req);
        for (SessionDetailItemDto item : ret) {
            item.setAnswer(extractAnswerData(item.getAnswer()));
        }
        result.setData(ret);
        return result;
    }

    private String extractAnswerData(String answerChunk) {
        if (StringUtils.isNotBlank(answerChunk)) {
            JSONArray json = JSONArray.fromObject(answerChunk);
            if (json.size() > 0) {
                JSONObject job = json.getJSONObject(0);
                return job.getString("value");
            }
        }
        return "";
    }


}

