package report;

import config.ConfigManagedService;
import config.Constants;
import log.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.util.CollectionUtils;
import report.bean.CoreReportBean;
import report.bean.ReportResult;
import report.enums.ChannelIdNameEnums;
import report.enums.ReportResultEnums;
import staticPart.RedisCache;
import task.CoreReportTask;
import utils.DateTools;
import utils.JSONUtils;
import utils.MySQLHelper;
import utils.StrUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


public class ReportApiRequest extends BaseReportRequest {
    public static final String entryPoint = "/v1/sf/coreReport";

    public static final String listEntryPoint = "/v1/sf/coreReportList";

    public static final String exportEntryPoint = "/v1/sf/exportCoreReport";

    private static int reportTimeInterval = ConfigManagedService.getConfig().getInteger(Constants.REPORT_TIME_INTERVAL);

    public ReportApiRequest(HttpServletRequest request) {
        super(request);
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
        if (DateTools.gapDayOfTwo(beginDate, endDate) > reportTimeInterval) {
            return new ReportResult(ReportResultEnums.DATE_SPAN_TOO_LONG);
        }
        LoggerFactory.getLogger().info(String.format("[%s] input: begin:'%s',end:'%s'", this.getClass().getSimpleName(), beginDate.toString(), endDate.toString()));
        Calendar c = Calendar.getInstance();
        Date iterator = beginDate;
        List<CoreReportBean> ret = new ArrayList<>();
        while (iterator.before(endDate)) {
            LoggerFactory.getLogger().debug("corereport:date:" + iterator.toString());
            String data = RedisCache.INSTANCE.get(CoreReportTask.generateKey(iterator));
            LoggerFactory.getLogger().debug("cache:" + data);
            if (StringUtil.isBlank(data)) {
                List<CoreReportBean> dayReport = CoreReportTask.doQueryDayReport(iterator);
                ret.addAll(dayReport);
            } else {
                List dayReport = JSONUtils.jsonToList(data, CoreReportBean.class);
                ret.addAll(dayReport);
            }
            c.setTime(iterator);
            c.add(Calendar.DATE, 1);
            iterator = c.getTime();
        }

        if (!CollectionUtils.isEmpty(channelIds) && channelIds.size() > 0) {
            ret = ret.stream().filter(a -> channelIds.contains(a.getChannelId())).collect(Collectors.toList());
        }
        for (CoreReportBean itm : ret) {
            itm.setChannelId(ChannelIdNameEnums.getChannelNameById(itm.getChannelId()));
        }

        result.setData(ret);
        LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "time consume:" + (System.currentTimeMillis() - startTime)));
        return result;
    }

    public void list4download(HttpServletResponse response) {
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = new Date(dateBeginStr);
            endDate = new Date(dateEndStr);
        } catch (Exception e) {
            return;
        }
        if (DateTools.gapDayOfTwo(beginDate, endDate) > reportTimeInterval) {
            return;
        }
        LoggerFactory.getLogger().info(String.format("[%s] input: begin:'%s',end:'%s'", this.getClass().getSimpleName(), beginDate.toString(), endDate.toString()));
        Calendar c = Calendar.getInstance();
        Date iterator = beginDate;
        List<CoreReportBean> ret = new ArrayList<>();
        while (iterator.before(endDate)) {
            String data = RedisCache.INSTANCE.get(CoreReportTask.generateKey(iterator));
            if (StringUtil.isBlank(data)) {
                List<CoreReportBean> dayReport = CoreReportTask.doQueryDayReport(iterator);
                ret.addAll(dayReport);
            } else {
                List dayReport = JSONUtils.jsonToList(data, CoreReportBean.class);
                ret.addAll(dayReport);
            }
            c.setTime(iterator);
            c.add(Calendar.DATE, 1);
            iterator = c.getTime();
        }

        if (!CollectionUtils.isEmpty(channelIds) && channelIds.size() > 0) {
            ret = ret.stream().filter(a -> channelIds.contains(a.getChannelId())).collect(Collectors.toList());
        }

        String title = "coreReport";
        Random random = new Random();
        String fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate) + "-" + random.nextInt(1000) + ".xls";
        String fileNamePath = tmpDir + "/" + fileName;
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


    public void process(HttpServletRequest request, HttpServletResponse response) {

        Date beginDate = null;
        Date endDate = null;
        try {
            // 兼容日报报表 和 查询列表接口
            if (dateBegin != null && dateBegin.length() == 10) {
                beginDate = DateTools.str2Date(dateBegin, DateTools.DateFormat.DATE_FORMAT_request_day, true);
            } else {
                beginDate = new Date(dateBeginStr);
            }

            if (dateEnd != null && dateEnd.length() == 10) {
                endDate = DateTools.str2Date(dateEnd, DateTools.DateFormat.DATE_FORMAT_request_day, false);
            } else {
                endDate = new Date(dateEndStr);
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

