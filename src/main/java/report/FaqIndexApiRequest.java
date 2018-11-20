package report;

import config.ConfigManagedService;
import config.Constants;
import log.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.CollectionUtils;
import utils.BigDecimalUtils;
import utils.DateTools;
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

public class FaqIndexApiRequest extends BaseReportRequest {
    public static final String entryPoint = "/v1/sf/faqindexReport";

    private static int faqThreshold0 = ConfigManagedService.getConfig().getInteger(Constants.FAQ_THRESHOLD0);
    private static int faqThreshold2 = ConfigManagedService.getConfig().getInteger(Constants.FAQ_THRESHOLD2);


    public FaqIndexApiRequest(HttpServletRequest request) {
        super(request);
    }


    public void process(HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Object> req = new HashMap<String, Object>();
        String dateBeginStr = "";
        String dateEndStr = "";
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = DateTools.str2Date(dateBegin, DateTools.DateFormat.DATE_FORMAT_request_day, true);
            endDate = DateTools.str2Date(dateEnd, DateTools.DateFormat.DATE_FORMAT_request_day, false);
            if (DateTools.gapDayOfTwo(beginDate, endDate) > 7L) {
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        if (dateBegin != null) {
            dateBeginStr = formatter.format(beginDate);
            req.put("beginDate", dateBeginStr);
        }
        if (dateEnd != null) {
            dateEndStr = formatter.format(endDate);
            req.put("endDate", dateEndStr);
        }
        if (!CollectionUtils.isEmpty(channelIds))
            req.put("channnelId", channelIds);
        req.put("faqThreshold0", faqThreshold0);
        req.put("faqThreshold2", faqThreshold2);

        LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(),StrUtils.printObjectJson(req) ));

        String title = "faqIndexReport";
        String fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate)+StrUtils.randomInt(1000) +  ".xls";
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

            List<FaqIndexReportDto> ret = MySQLHelper.getInstance().getChatRecordDao().faqIndexTotalRoundReport(req);
            List<FaqIndexReportDto> faqMissList = MySQLHelper.getInstance().getChatRecordDao().faqIndexMissReport(req);
            for (FaqIndexReportDto i : faqMissList) {
                FaqIndexReportDto target = getFaqIndexReportDtoByChannelId(i.getChannelId(), ret);
                if (target != null)
                    target.setFaqMissNum(i.getFaqMissNum());
            }
            List<FaqIndexReportDto> directList = MySQLHelper.getInstance().getChatRecordDao().faqIndexDirectReport(req);
            for (FaqIndexReportDto i : directList) {
                FaqIndexReportDto target = getFaqIndexReportDtoByChannelId(i.getChannelId(), ret);
                if (target != null)
                    target.setDirectAnswerNum(i.getDirectAnswerNum());
            }
            List<FaqIndexReportDto> recommendList = MySQLHelper.getInstance().getChatRecordDao().faqIndexRecommendReport(req);
            for (FaqIndexReportDto i : recommendList) {
                FaqIndexReportDto target = getFaqIndexReportDtoByChannelId(i.getChannelId(), ret);
                if (target != null)
                    target.setRecommendAnswerNum(i.getRecommendAnswerNum());
            }
            for (FaqIndexReportDto i : ret) {
                i.setDateBeginStr(dateBeginStr);
                i.setDateEndStr(dateEndStr);
                i.setFaqTotalNum(i.getDirectAnswerNum() + i.getRecommendAnswerNum() + i.getFaqMissNum());
                i.setDirectAnswerRate(BigDecimalUtils.divide4Long(i.getDirectAnswerNum(), i.getFaqTotalNum()));
                i.setRecommendAnswerRate(BigDecimalUtils.divide4Long(i.getRecommendAnswerNum(), i.getFaqTotalNum()));
                i.setFaqMissRate(BigDecimalUtils.divide4Long(i.getFaqMissNum(), i.getFaqTotalNum()));
            }
            saveExcel(title, fileNamePath, ret);
        }
        LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), "time consume:" + (System.currentTimeMillis() - startTime)));


        //2.获取要下载的文件名
        //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        //4.获取要下载的文件输入流
        try {
            InputStream in = new FileInputStream(fileNamePath);
            int len = 0;
            //5.创建数据缓冲区
            byte[] buffer = new byte[1024];
            //6.通过response对象获取OutputStream流
            OutputStream out = response.getOutputStream();
            //7.将FileInputStream流写入到buffer缓冲区
            while ((len = in.read(buffer)) > 0) {
                //8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static FaqIndexReportDto getFaqIndexReportDtoByChannelId(String channelId, List<FaqIndexReportDto> ret) {
        if (StringUtils.isBlank(channelId)){
            return null;
        }
        for (FaqIndexReportDto i : ret) {
            if (channelId.equals(i.getChannelId())) {
                return i;
            }
        }
        return null;
    }


    public void saveExcel(String title, String path, List<FaqIndexReportDto> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet(title);
        int line = 0;
        HSSFRow rowm = sheet.createRow(line++);
        writeHeaders(rowm);
        if (data != null && data.size() > 0) {

            for (FaqIndexReportDto item : data) {
                int i = 0;
                HSSFRow lcell = sheet.createRow(line++);
                lcell.createCell(i++).setCellValue(item.getDateBeginStr());
                lcell.createCell(i++).setCellValue(item.getDateEndStr());
                lcell.createCell(i++).setCellValue(ChannelIdNameEnums.getChannelNameById(item.getChannelId()));
                lcell.createCell(i++).setCellValue(item.getTotalRound());
                lcell.createCell(i++).setCellValue(item.getFaqTotalNum());
                lcell.createCell(i++).setCellValue(item.getDirectAnswerNum());
                lcell.createCell(i++).setCellValue(String.valueOf(item.getDirectAnswerRate()));
                lcell.createCell(i++).setCellValue(item.getRecommendAnswerNum());
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRecommendAnswerRate()));
                lcell.createCell(i++).setCellValue(item.getFaqMissNum());
                lcell.createCell(i).setCellValue(String.valueOf(item.getFaqMissRate()));
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
        rowm.createCell(i++).setCellValue("总问答交互轮数");
        rowm.createCell(i++).setCellValue("总FAQ类交互轮数");
        rowm.createCell(i++).setCellValue("直接回答数");
        rowm.createCell(i++).setCellValue("直接回答率");
        rowm.createCell(i++).setCellValue("推荐回答数");
        rowm.createCell(i++).setCellValue("推荐回答率");
        rowm.createCell(i++).setCellValue("未命中数");
        rowm.createCell(i).setCellValue("未命中率");

    }


}

