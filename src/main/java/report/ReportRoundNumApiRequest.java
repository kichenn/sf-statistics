package report;

import log.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.util.CollectionUtils;
import utils.DateTools;
import utils.MySQLHelper;
import utils.StrUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReportRoundNumApiRequest extends BaseReportRequest {
    public static final String entryPoint = "/v1/sf/roundNumReport";


    public ReportRoundNumApiRequest(HttpServletRequest request) {
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

        String title = "roundNumReport";
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
            List<RoundNumReportPo> list = MySQLHelper.getInstance().getChatRecordDao().calRoundNumReport(req);
            List<RoundNumReportDto> ret = new ArrayList<>();
            for (RoundNumReportPo p : list) {
                try {
                    convertPo2Dto(p, ret);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            for (RoundNumReportDto dto : ret) {
                dto.setDateBeginStr(dateBeginStr);
                dto.setDateEndStr(dateEndStr);
                long total = 0L;
                total += dto.getRound_1_Cnt();
                total += dto.getRound_2_Cnt();
                total += dto.getRound_3_Cnt();
                total += dto.getRound_4_Cnt();
                total += dto.getRound_5_Cnt();
                total += dto.getRound_6_Cnt();
                total += dto.getRound_7_Cnt();
                total += dto.getRound_8_Cnt();
                total += dto.getRound_9_Cnt();
                total += dto.getRound_10_Cnt();
                total += dto.getRound_11_Cnt();
                dto.setTotalSessionNum(total);
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

    private static void convertPo2Dto(RoundNumReportPo po, List<RoundNumReportDto> ret) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RoundNumReportDto findedDto = find(po, ret);
        if (findedDto == null) {
            RoundNumReportDto t = new RoundNumReportDto();
            t.setChannelId(po.getChannelId());
            t.setAcsType(po.getAcsType());
            findedDto = t;
            ret.add(t);
        }

        int rn = po.getRoundNum();
        String mothedName = "setRound_" + rn + "_Cnt";
        Method method = RoundNumReportDto.class.getMethod(mothedName, Long.class);
        method.invoke(findedDto, po.getRoundNumCnt());


    }

    private static RoundNumReportDto find(RoundNumReportPo po, List<RoundNumReportDto> ret) {
//        if (StringUtil.isBlank(po.getChannelId()) || StringUtil.isBlank(po.getAcsType())){
//            return null;
//        }
        for (RoundNumReportDto i : ret) {
            if (StringUtil.isBlank(po.getChannelId())
                    && StringUtil.isBlank(i.getChannelId())
                    && po.getAcsType().equals(i.getAcsType())
                    ) {
                return i;
            }
            if (StringUtil.isNotBlank(po.getChannelId()) && po.getChannelId().equals(i.getChannelId()) && po.getAcsType().equals(i.getAcsType())) {
                return i;
            }
        }
        return null;
    }


    public void saveExcel(String title, String path, List<RoundNumReportDto> data) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        OutputStream out = null;
        HSSFSheet sheet = workbook.createSheet(title);
        int line = 0;
        HSSFRow rowm = sheet.createRow(line++);
        writeHeaders(rowm);
        if (data != null && data.size() > 0) {

            for (RoundNumReportDto item : data) {
                int i = 0;
                HSSFRow lcell = sheet.createRow(line++);
                lcell.createCell(i++).setCellValue(item.getDateBeginStr());
                lcell.createCell(i++).setCellValue(item.getDateEndStr());
                lcell.createCell(i++).setCellValue(ChannelIdNameEnums.getChannelNameById(item.getChannelId()));
                lcell.createCell(i++).setCellValue(item.getAcsType());
                lcell.createCell(i++).setCellValue(String.valueOf(item.getTotalSessionNum()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_1_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_2_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_3_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_4_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_5_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_6_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_7_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_8_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_9_Cnt()));
                lcell.createCell(i++).setCellValue(String.valueOf(item.getRound_10_Cnt()));
                lcell.createCell(i).setCellValue(String.valueOf(item.getRound_11_Cnt()));

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
        rowm.createCell(i++).setCellValue("类型(是否转人工)");
        rowm.createCell(i++).setCellValue("总会话量");
        rowm.createCell(i++).setCellValue("1轮");
        rowm.createCell(i++).setCellValue("2轮");
        rowm.createCell(i++).setCellValue("3轮");
        rowm.createCell(i++).setCellValue("4轮");
        rowm.createCell(i++).setCellValue("5轮");
        rowm.createCell(i++).setCellValue("6轮");
        rowm.createCell(i++).setCellValue("7轮");
        rowm.createCell(i++).setCellValue("8轮");
        rowm.createCell(i++).setCellValue("9轮");
        rowm.createCell(i++).setCellValue("10轮");
        rowm.createCell(i).setCellValue("11轮");


    }


}

