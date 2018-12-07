package report;

import log.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import report.bean.CoreReportBean;
import report.bean.ReportResult;
import report.enums.ChannelIdNameEnums;
import report.enums.ReportResultEnums;
import utils.DateTools;
import utils.MySQLHelper;
import utils.StrUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class ReportApiRequest extends BaseReportRequest {
    public static final String entryPoint = "/v1/sf/coreReport";

    public static final String listEntryPoint = "/v1/sf/coreReportList";

    public ReportApiRequest(HttpServletRequest request) {
        super(request);
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

        List<CoreReportBean> ret = MySQLHelper.getInstance().getReportDao().queryReport(beginDate, endDate, null);
        for (CoreReportBean itm : ret){
            itm.setChannelId(ChannelIdNameEnums.getChannelNameById(itm.getChannelId()));
        }
        result.setData(ret);
        return result;
    }

    public void process(HttpServletRequest request, HttpServletResponse response) {

        Date beginDate = null;
        Date endDate = null;
        try {
            // 兼容日报报表 和 查询列表接口
            if (dateBegin != null && dateBegin.length() == 10) {
                beginDate = DateTools.str2Date(dateBegin, DateTools.DateFormat.DATE_FORMAT_request_day, true);
            } else {
                beginDate = DateTools.str2DateNormal(dateBegin, DateTools.DateFormat.DATE_FORMAT_4_get);
            }

            if (dateEnd != null && dateEnd.length() == 10) {
                endDate = DateTools.str2Date(dateEnd, DateTools.DateFormat.DATE_FORMAT_request_day, false);
            } else {
                endDate = DateTools.str2DateNormal(dateEnd, DateTools.DateFormat.DATE_FORMAT_4_get);
            }
            if (DateTools.gapDayOfTwo(beginDate, endDate) > 7L) {
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        LoggerFactory.getLogger().info(String.format("[%s] output: '%s,  %s'", this.getClass().getSimpleName(), beginDate, endDate));

        String title = "coreReport";
        String fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate) + ".xls";
        String fileNamePath = tmpDir + "/" + fileName;
        File historyFile = new File(fileNamePath);
        if (!historyFile.exists() || "TRUE".equals(isForceNOCache)) {
            if (historyFile.exists()) {
                LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "file existed!"));
                if (!historyFile.delete()) {
                    fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate) + StrUtils.randomInt(100) + ".xls";
                    fileNamePath = tmpDir + "/" + fileName;
                    LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "delete file failed!"));
                }
            }
            List<CoreReportBean> ret = MySQLHelper.getInstance().getReportDao().queryReport(beginDate, endDate, null);
            saveExcel(title, fileNamePath, ret);
        }

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


    public void saveExcel(String title, String path, List<CoreReportBean> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet(title);
        int line = 0;
        HSSFRow rowm = sheet.createRow(line++);
        writeHeaders(rowm);
        if (data != null && data.size() > 0) {

            for (CoreReportBean item : data) {
                int i = 0;
                HSSFRow lcell = sheet.createRow(line++);
                lcell.createCell(i++).setCellValue(item.getDateBegin());
                lcell.createCell(i++).setCellValue(item.getDateEnd());
                lcell.createCell(i++).setCellValue(ChannelIdNameEnums.getChannelNameById(item.getChannelId()));
                lcell.createCell(i++).setCellValue(item.getTotalSessionNum());
                lcell.createCell(i++).setCellValue(item.getValidSessionNum());
                lcell.createCell(i++).setCellValue(item.getValidBusinessSessionNum());
                lcell.createCell(i++).setCellValue(item.getAcsSessionNum());
                lcell.createCell(i++).setCellValue(item.getNonAcsSessionNum());
                lcell.createCell(i++).setCellValue(item.getValidNoAcsSessionNum());
                lcell.createCell(i++).setCellValue(String.valueOf(item.getAcsTotalRate()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getValidAcsRate()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getNoAcsRate()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getNoAcsValidRate()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getMachineRate()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getInteractRound()));
                lcell.createCell(i).setCellValue(String.valueOf(item.getAverageConversionTime()));
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

    private void writeHeaders(HSSFRow rowm) {
        int i = 0;
        rowm.createCell(i++).setCellValue("开始日期");
        rowm.createCell(i++).setCellValue("结束日期");
        rowm.createCell(i++).setCellValue("渠道");
        rowm.createCell(i++).setCellValue("总会话量");
        rowm.createCell(i++).setCellValue("有效会话量");
        rowm.createCell(i++).setCellValue("有效业务会话量");
        rowm.createCell(i++).setCellValue("转人工量");
        rowm.createCell(i++).setCellValue("独立服务量");
        rowm.createCell(i++).setCellValue("有效独立服务量");
        rowm.createCell(i++).setCellValue("总转人工率");
        rowm.createCell(i++).setCellValue("有效转人工率");
        rowm.createCell(i++).setCellValue("独立服务率");
        rowm.createCell(i++).setCellValue("有效独立服务率");
        rowm.createCell(i++).setCellValue("机器人分流率");
        rowm.createCell(i++).setCellValue("交互轮数");
        rowm.createCell(i).setCellValue("平均对话时长");

    }


}

