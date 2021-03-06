package handler;

import com.google.gson.Gson;
import org.junit.Test;
import report.bean.*;
import report.enums.ChannelIdNameEnums;
import utils.HandlerUtilities;
import utils.MySQLHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HandlerUtilitiesTest {

    @Test
    public void test() {
        assertTrue(HandlerUtilities.isValidParameter("123"));
        assertTrue(HandlerUtilities.isValidParameter("test account"));
        assertTrue(HandlerUtilities.isValidParameter("test 123"));
        assertTrue(HandlerUtilities.isValidParameter("_-"));
        assertTrue(HandlerUtilities.isValidParameter("_1-A"));
        assertFalse(HandlerUtilities.isValidParameter("_1+A"));
        assertFalse(HandlerUtilities.isValidParameter("AAAAA/AAA__"));
        assertFalse(HandlerUtilities.isValidParameter("'"));
        assertFalse(HandlerUtilities.isValidParameter("@"));
        assertFalse(HandlerUtilities.isValidParameter("~"));
        assertFalse(HandlerUtilities.isValidParameter("#"));
        assertFalse(HandlerUtilities.isValidParameter("("));
        assertFalse(HandlerUtilities.isValidParameter(")"));
        assertFalse(HandlerUtilities.isValidParameter("<"));
        assertFalse(HandlerUtilities.isValidParameter(">"));
    }

    @Test
    public void testsql(){
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", "2018-12-10");
        req.put("endDate", "2018-12-11");
        req.put("sessionId","444664472343377235352321");

//        List<ChatRecordEntity> ret = MySQLHelper.getInstance().getChatRecordDao().exportSessionDetail(req);

        List<TaskEngineInfoDomain> ret = MySQLHelper.getInstance().getChatRecordDao().queryAllScenarioInfo();


    }

    @Test
    public void testValidNoAcsSessionNum() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date b =  sdf.parse("2019-01-24 00:00:00");
        Date e = sdf.parse("2019-01-25 00:00:00");
        List<String> targetChannelIds = new ArrayList<>();
        targetChannelIds.add("719");
        List<CoreReportBean> ret =  MySQLHelper.getInstance().getReportDao().queryReport(b,e,targetChannelIds);
        System.out.println(new Gson().toJson(ret));
    }

    @Test
    public void reportTest() throws ParseException {
//        Date today = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//        String dateString = formatter.format(today);
//        DateFormatUtils.format(new Date(), "yyyy-MM-dd 00:00:00");
//        System.out.println(dateString);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date b =  sdf.parse("2019-01-23 00:00:00");
//        Date e = sdf.parse("2019-01-24 00:00:00");
//        List<String> targetChannelIds = new ArrayList<>();
//        targetChannelIds.add("376");
//        MySQLHelper.getInstance().getReportDao().queryReport(b,e,targetChannelIds);

//        ReportApiRequest reportApiRequest = new ReportApiRequest(null);
//        List<CoreReportBean> data = new ArrayList<>();
//       CoreReportBean i =  new CoreReportBean();
//       i.setChannelId("shuibiao");
//       i.setNoAcsValidRate(BigDecimal.ONE);
//        data.add(i);
//        reportApiRequest.saveExcel("/tmp/data.xls",data);

//        System.out.println(new Date().getTime());

//        List<ChatRecordEntity> list = null;
//
//        list = MySQLHelper.getInstance().getChatRecordDao().queryChatRecordAll();
//
//        System.out.println(new Gson().toJson(list));
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", "2018-12-07");
        req.put("endDate", "2018-12-08");
        req.put("sessionId","sessionid100000");
//        List<String> cls = new ArrayList<>();
//        cls.add("suibiao");
//        cls.add("suibian");
//        req.put("channnelId", cls);
//        req.put("faqThreshold0", 95);
//        req.put("faqThreshold3", 45);
        long startTIme = System.currentTimeMillis();
//        List<ChatRecordEntity> ret = MySQLHelper.getInstance().getChatRecordDao().queryChatRecordAll(req);

        List<SessionDetailItemDto> ret = MySQLHelper.getInstance().getChatRecordDao().querySessionDetail(req);
        System.out.println(ret.toArray());
//        List<FaqIndexReportDto> faqMissList = MySQLHelper.getInstance().getChatRecordDao().faqIndexMissReport(req);
//        for (FaqIndexReportDto i : faqMissList) {
//            FaqIndexReportDto target = getFaqIndexReportDtoByChannelId(i.getChannelId(), ret);
//            if (target != null)
//                target.setFaqMissNum(i.getFaqMissNum());
//        }
//
//        List<FaqIndexReportDto> directList = MySQLHelper.getInstance().getChatRecordDao().faqIndexDirectReport(req);
//        for (FaqIndexReportDto i : directList) {
//            FaqIndexReportDto target = getFaqIndexReportDtoByChannelId(i.getChannelId(), ret);
//            if (target != null)
//                target.setDirectAnswerNum(i.getDirectAnswerNum());
//        }
//
//        List<FaqIndexReportDto> recommendList = MySQLHelper.getInstance().getChatRecordDao().faqIndexRecommendReport(req);
//        for (FaqIndexReportDto i : recommendList) {
//            FaqIndexReportDto target = getFaqIndexReportDtoByChannelId(i.getChannelId(), ret);
//            if (target != null)
//                target.setRecommendAnswerNum(i.getRecommendAnswerNum());
//        }
//        for (FaqIndexReportDto i : ret) {
//            i.setFaqTotalNum(i.getDirectAnswerNum() + i.getRecommendAnswerNum() + i.getFaqMissNum());
//            i.setDirectAnswerRate(BigDecimalUtils.divide4Long(i.getDirectAnswerNum(), i.getFaqTotalNum()));
//            i.setRecommendAnswerRate(BigDecimalUtils.divide4Long(i.getRecommendAnswerNum(), i.getFaqTotalNum()));
//            i.setFaqMissRate(BigDecimalUtils.divide4Long(i.getFaqMissNum(), i.getFaqTotalNum()));
//        }
//
//        System.out.println(new Gson().toJson(ret));
//        List<RoundNumReportPo> list = MySQLHelper.getInstance().getChatRecordDao().calRoundNumReport(req);
//        List<RoundNumReportDto> ret = new ArrayList<>();
//        for (RoundNumReportPo p : list) {
//            try {
//                convertPo2Dto(p,ret);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }


//        List<CoreReportBean> list = MySQLHelper.getInstance().getReportDao().queryReport(null,null,null);
//        List<StaticRecordDto> reasonInfo = MySQLHelper.getInstance().getChatRecordDao().queryStaticRecordReasonInfo(req);
//
//        for (StaticRecordDto item : list){
//            List<StaticRecordDto> tmp =   reasonInfo.stream().filter((StaticRecordDto i) -> i.getChannelId().equals(item.getChannelId())
//                    && i.getAnswerId().equals(item.getAnswerId()) && i.getbQuestion().equals(item.getbQuestion()))
//                    .collect(Collectors.toList());
//            StringBuilder sb = new StringBuilder();
//            for (StaticRecordDto ite : tmp){
//                sb.append(ite.getReason()).append(":").append(ite.getReasonCnt());
//            }
//            item.setReason(sb.toString());
//            item.setReasonCnt(null);
//        }



    }

    private static FaqIndexReportDto getFaqIndexReportDtoByChannelId(String channelId, List<FaqIndexReportDto> ret) {
        for (FaqIndexReportDto i : ret) {
            if (i.getChannelId().equals(channelId)) {
                return i;
            }
        }
        return null;
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
        for (RoundNumReportDto i : ret) {
            if (i.getChannelId().equals(po.getChannelId()) && i.getAcsType().equals(po.getAcsType())) {
                return i;
            }
        }
        return null;
    }

    @Test
    public void switchTest() {


    }

    @Test
    public void testChannelEnum(){
//        String channelName = ChannelIdNameEnums.getChannelNameById("900");
//        System.out.println(channelName);
//        String channelName2 = ChannelIdNameEnums.getChannelNameById("901");
//        System.out.println(channelName2);
    }


}
