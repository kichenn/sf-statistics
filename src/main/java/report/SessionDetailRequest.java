package report;

import log.LoggerFactory;
import net.jodah.expiringmap.ExpiringMap;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import report.bean.ChatRecordEntity;
import report.bean.ReportResult;
import report.bean.SessionDetailItemDto;
import report.bean.TaskEngineInfoDomain;
import report.enums.ReportResultEnums;
import utils.DateTools;
import utils.HandlerUtilities;
import utils.MySQLHelper;
import utils.StrUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SessionDetailRequest extends BaseReportRequest {


    private static ExpiringMap<String, String> scenarioIdNameMap = ExpiringMap.builder().maxSize(1000).build();

    protected String sessionId;


    public static final String listEntryPoint = "/v1/sf/sessionDetail";

    public static final String exportEntryPoint = "/v1/sf/sessionDetailExport";

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
            beginDate = new Date(dateBeginStr);
            endDate = new Date(dateEndStr);
        } catch (Exception e) {
            return new ReportResult(ReportResultEnums.DATE_PARSE_EXCEPTION);
        }
        if (DateTools.gapDayOfTwo(beginDate, endDate) > 7L) {
            return new ReportResult(ReportResultEnums.DATE_SPAN_TOO_LONG);
        }

        HashMap<String, Object> req = new HashMap<String, Object>();
        String dateBeginStr = "";
        String dateEndStr = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (beginDate != null) {
            dateBeginStr = formatter.format(beginDate);
            req.put("beginDate", dateBeginStr);
        }
        if (endDate != null) {
            dateEndStr = formatter.format(endDate);
            req.put("endDate", dateEndStr);
        }
        if (StringUtils.isNotBlank(sessionId)) {
            req.put("sessionId", sessionId);
        }
        LoggerFactory.getLogger().info(String.format("[%s] input: '%s,  %s',%s", this.getClass().getSimpleName(), beginDate, endDate,sessionId));

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


    public void process(HttpServletRequest request, HttpServletResponse response) {

        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = new Date(dateBeginStr);
            endDate = new Date(dateEndStr);
            if (DateTools.gapDayOfTwo(beginDate, endDate) > 7L) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        LoggerFactory.getLogger().info(String.format("[%s] output: '%s,  %s'", this.getClass().getSimpleName(), beginDate, endDate));

        String title = "sessionDetail";
        String fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate) + StrUtils.randomInt(100) + ".xls";
        String fileNamePath = tmpDir + "/" + fileName;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", formatter.format(beginDate));
        req.put("endDate", formatter.format(endDate));
        req.put("sessionId", this.sessionId);

        List<ChatRecordEntity> ret = MySQLHelper.getInstance().getChatRecordDao().exportSessionDetail(req);

        saveExcel(title, fileNamePath, ret);
        LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "time consume:" + (System.currentTimeMillis() - startTime)));
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        try {
            InputStream in = new FileInputStream(fileNamePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void saveExcel(String title, String path, List<ChatRecordEntity> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet(title);
        int line = 0;
        HSSFRow rowm = sheet.createRow(line++);

        writeHeaders(rowm);
        if (data != null && data.size() > 0) {

            for (ChatRecordEntity item : data) {
                int i = 0;
                HSSFRow lcell = sheet.createRow(line++);
                lcell.createCell(i++).setCellValue(item.getSessionId());
                lcell.createCell(i++).setCellValue(item.getUserId());
                lcell.createCell(i++).setCellValue(item.getUserQ());
                lcell.createCell(i++).setCellValue(item.getStdQ());
                lcell.createCell(i++).setCellValue("intent");
                lcell.createCell(i++).setCellValue(extractAnswerData(item.getAnswer()));
                lcell.createCell(i++).setCellValue(item.getScore());
                lcell.createCell(i++).setCellValue(item.getModule());
                lcell.createCell(i++).setCellValue(DateTools.date2Str(item.getCreatedTime(), DateTools.DateFormat.DATE_FORMAT_stand));
                lcell.createCell(i++).setCellValue(item.getIntent());
                lcell.createCell(i).setCellValue(item.getIntentScore() == null ? 0 : item.getIntentScore());
            }
        }
        try {
            out = new FileOutputStream(path);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public String extractScenarioNameInfo(String scenarioId) {

        if (scenarioIdNameMap.containsKey(scenarioId)) return scenarioIdNameMap.get(scenarioId);

        List<TaskEngineInfoDomain> scenarioInfoList = MySQLHelper.getInstance().getChatRecordDao().queryAllScenarioInfo();
        String sname = "";
        for (TaskEngineInfoDomain item : scenarioInfoList) {
            if (scenarioIdNameMap.containsKey(item.getScenarioID()))
                sname = scenarioIdNameMap.get(item.getScenarioID());
            if (StringUtils.isNotBlank(item.getContent())) {
                JSONObject json = JSONObject.fromObject(item.getContent());
                if (json == null) continue;
                JSONObject metadata = (JSONObject) json.get("metadata");
                if (metadata == null) continue;
                scenarioIdNameMap.put(item.getScenarioID(), metadata.getString("scenario_name"));
                return metadata.getString("scenario_name");
            }
        }
        return sname;
    }

    private void writeHeaders(HSSFRow rowm) {
        int i = 0;
        rowm.createCell(i++).setCellValue("会话id");
        rowm.createCell(i++).setCellValue("用户id");
        rowm.createCell(i++).setCellValue("用户问句");
        rowm.createCell(i++).setCellValue("标准问题");
        rowm.createCell(i++).setCellValue("场景名称");
        rowm.createCell(i++).setCellValue("场景触发方");
        rowm.createCell(i++).setCellValue("机器人回答");
        rowm.createCell(i++).setCellValue("匹配分数");
        rowm.createCell(i++).setCellValue("出话模组");
        rowm.createCell(i++).setCellValue("访问时间");
        rowm.createCell(i++).setCellValue("意图");
        rowm.createCell(i).setCellValue("意图分数");

    }
}

