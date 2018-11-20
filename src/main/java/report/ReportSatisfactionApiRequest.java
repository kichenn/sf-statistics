package report;

import com.google.gson.Gson;
import log.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.CollectionUtils;
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
import java.util.stream.Collectors;

public class ReportSatisfactionApiRequest extends BaseReportRequest {
    public static final String entryPoint = "/v1/sf/satisfyReport";


    private Gson gson = new Gson();

    public ReportSatisfactionApiRequest(HttpServletRequest request) {
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

        LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(),StrUtils.printObjectJson(req) ));


        String title = "satisfyReport";
        String fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate) +StrUtils.randomInt(1000)+ ".xls";
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


            List<StaticRecordDto> list = MySQLHelper.getInstance().getChatRecordDao().queryStaticRecordInfo(req);
            List<StaticRecordDto> reasonInfo = MySQLHelper.getInstance().getChatRecordDao().queryStaticRecordReasonInfo(req);

            for (StaticRecordDto item : list) {
                List<StaticRecordDto> tmp = reasonInfo.stream().filter((StaticRecordDto i) -> i.getChannelId().equals(item.getChannelId())
                        && i.getAnswerId().equals(item.getAnswerId()) && i.getbQuestion().equals(item.getbQuestion()))
                        .collect(Collectors.toList());
                StringBuilder sb = new StringBuilder();
                for (StaticRecordDto ite : tmp) {
                    sb.append(ite.getReason()).append(":").append(ite.getReasonCnt()).append("\r\n");
                }
                item.setReason(sb.toString());
                item.setReasonCnt(null);
            }
            for (StaticRecordDto item : list) {
                item.setDateBegin(dateBeginStr);
                item.setDateEnd(dateEndStr);
            }
            saveExcel(title, fileNamePath, list);
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


    public void saveExcel(String title, String path, List<StaticRecordDto> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet(title);
        int line = 0;
        HSSFRow rowm = sheet.createRow(line++);
        writeHeaders(rowm);
        if (data != null && data.size() > 0) {

            for (StaticRecordDto item : data) {
                int i = 0;
                HSSFRow lcell = sheet.createRow(line++);
                lcell.createCell(i++).setCellValue(item.getDateBegin());
                lcell.createCell(i++).setCellValue(item.getDateEnd());
                lcell.createCell(i++).setCellValue(ChannelIdNameEnums.getChannelNameById(item.getChannelId()));
                lcell.createCell(i++).setCellValue(item.getAnswerId());
                lcell.createCell(i++).setCellValue(item.getbQuestion());
                lcell.createCell(i++).setCellValue(item.getVisitCnt());
                lcell.createCell(i++).setCellValue(item.getSolvedCnt());
                lcell.createCell(i++).setCellValue(item.getUnSolvedCnt());
                lcell.createCell(i).setCellValue(item.getReason());
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
        rowm.createCell(i++).setCellValue("知识点ID");
        rowm.createCell(i++).setCellValue("标准问题");
        rowm.createCell(i++).setCellValue("访问量");
        rowm.createCell(i++).setCellValue("评价（有用）");
        rowm.createCell(i++).setCellValue("评价（无用）");
        rowm.createCell(i).setCellValue("无用原因");

    }

}

