package report;

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

public class DialogueTimeReportApiRequest extends BaseReportRequest {
    public static final String entryPoint = "/v1/sf/dialogueTimeReport";

    public DialogueTimeReportApiRequest(HttpServletRequest request) {
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
        LoggerFactory.getLogger().info(String.format("[%s] output: '%s'", this.getClass().getSimpleName(), StrUtils.printObjectJson(req)));

        String title = "dialogueTimeReport";
        String fileName = title + "-" + DateTools.contructDaySpanStr(beginDate, endDate) +  ".xls";
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

            List<DialogueTimeReportPo> list = MySQLHelper.getInstance().getChatRecordDao().dialogueTimeReport(req);
            for (DialogueTimeReportPo dto : list) {
                dto.setDateBeginStr(dateBeginStr);
                dto.setDateEndStr(dateEndStr);
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

    public void saveExcel(String title, String path, List<DialogueTimeReportPo> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet(title);
        int line = 0;
        HSSFRow rowm = sheet.createRow(line++);
        writeHeaders(rowm);
        if (data != null && data.size() > 0) {

            for (DialogueTimeReportPo item : data) {
                int i = 0;
                HSSFRow lcell = sheet.createRow(line++);
                lcell.createCell(i++).setCellValue(item.getDateBeginStr());
                lcell.createCell(i++).setCellValue(item.getDateEndStr());
                lcell.createCell(i++).setCellValue(ChannelIdNameEnums.getChannelNameById(item.getChannelId()));
                lcell.createCell(i++).setCellValue(item.getAcsType());
                lcell.createCell(i++).setCellValue(String.valueOf(item.getSall()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS5()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS10()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS15()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS20()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS25()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS30()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS35()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS40()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS45()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS50()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS55()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getS60()));
                lcell.createCell(i).setCellValue(String.valueOf(item.getS61()));
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
        int j = 1;
        rowm.createCell(i++).setCellValue("开始日期");
        rowm.createCell(i++).setCellValue("结束日期");
        rowm.createCell(i++).setCellValue("渠道");
        rowm.createCell(i++).setCellValue("类型(是否转人工)");
        rowm.createCell(i++).setCellValue("总会话量");

        for (j = 1; j <= 12; j++)
            rowm.createCell(i++).setCellValue(j * 5 + "s");
        rowm.createCell(i).setCellValue("大于60s");
    }


}

