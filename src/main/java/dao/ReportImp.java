package dao;

import com.google.gson.Gson;
import log.LoggerFactory;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import report.CoreReportBean;
import utils.BigDecimalUtils;
import utils.MySQLHelper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by changmin on 2018/3/7.
 */
public class ReportImp extends JdbcDaoSupport implements IReportDao {
    private static final String DAY_BEGIN_POINT_FORMAT = "yyyy-MM-dd 00:00:00";
    private static final String DAY_END_POINT_FORMAT = "yyyy-MM-dd 59:59:59";
    private static final Gson GSON = new Gson();
//    private static final String TABLE_NAME = "chat_record";
//    private static final String welcomeTag = "@system:welcome_words@";
//    private static final String ACSTag = "%ACS%";
//    private String totalSessionSql =
//            String.format("select channelId, count(distinct session_id) as totalSessionNum from %s where created_time >= ? and created_time <= ? group by channelId", TABLE_NAME);
//    private String validSessionSql =
//            String.format("select channelId, count(distinct session_id) as validSessionNum from %s where user_q != '%s'  and  created_time >= ? and created_time <= ? group by channelId", TABLE_NAME, welcomeTag);
//    private String acsSessionSql =
//            String.format("select channelId, count(distinct session_id) as acsSessionNum from %s where answer like '%s' and created_time >= ? and created_time <= ? group by channelId", TABLE_NAME, ACSTag);
//
//    private String totalSessionMoreThanOneSql =
//            String.format("select channelId, count(distinct session_id) as totalSessionMoreThanOneNum from %s where user_q != '%s' and answer like '%s' and  created_time >= ? and created_time <= ? group by channelId",
//                    TABLE_NAME, welcomeTag, ACSTag);
//    private String interActRoundSql =
//            String.format("select channelId,count(distinct session_id)/count(id) as interactRound from chat_record where user_q != '%s' and  created_time >= ? and created_time <= ?  group by channelId", TABLE_NAME, welcomeTag);
//    private String averageConversionTimeSql =
//            String.format("select a.channelId, sum(a.conversionTime)/count(session_id) as averageTime from (select channelId,session_id , max(created_time)-min(created_time) as conversionTime from %s where user_q != '%s' and  created_time >= ? and created_time <= ? group by channelId,session_id) a group by a.channelId", TABLE_NAME, welcomeTag);

    /***
     * 计算平均会话时间
     * @param dateBegin
     * @param dateEnd
     * @param channelIdList
     * @return
     */
    private List<CoreReportBean> calculateAverageConversionTime(String dateBegin, String dateEnd, List<String> channelIdList) {
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryAverageConversionTime(req);
        return cnt;
    }

    /***
     * 计算交互轮数
     * @param dateBegin
     * @param dateEnd
     * @param channelIdList
     * @return
     */
    private List<CoreReportBean> calculateInteractRound(String dateBegin, String dateEnd, List<String> channelIdList) {
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryInterActRound(req);
        return cnt;
    }

    /***
     * 计算总会话量
     * @param dateBegin
     * @param dateEnd
     * @param channelIdList
     * @return
     */
    private List<CoreReportBean> calculateTotalSessionNum(String dateBegin, String dateEnd, List<String> channelIdList) {
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryTotalSession(req);
        return cnt;
    }

    private List<CoreReportBean> calculateValidBusinessSession(String dateBegin, String dateEnd, List<String> channelIdList) {
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryValidBusinessSession(req);
        return cnt;
    }

    /***
     * 核心报表接口
     * @param beginDate
     * @param endDate
     * @param targetChannels
     * @return
     */
    public List<CoreReportBean> queryReport(Date beginDate, Date endDate, List<String> targetChannels) {
        String dateBeginStr = transformBeginDate(beginDate, true);
        String dateEndStr = transformBeginDate(endDate, false);

//        JdbcTemplate template = this.getJdbcTemplate();
//        if (template == null) {
//            LoggerFactory.getLogger().error("Null JdbcTemplate");
//            return null;
//        }
        List<CoreReportBean> totalSessionReportBeanList = null;
        try {
            totalSessionReportBeanList = calculateTotalSessionNum(dateBeginStr, dateEndStr, targetChannels);

            List<CoreReportBean> validSessionReportBeanList = calculateValidSession(dateBeginStr, dateEndStr, targetChannels);
            for (CoreReportBean item : totalSessionReportBeanList) {
                CoreReportBean tmp = getBeanByChannelId(validSessionReportBeanList, item.getChannelId());
                if (tmp != null)
                    item.setValidSessionNum(tmp.getValidSessionNum());
            }

            List<CoreReportBean> validBusinessReportBeanList = calculateValidBusinessSession(dateBeginStr, dateEndStr, targetChannels);
            for (CoreReportBean item : totalSessionReportBeanList) {
                CoreReportBean tmp = getBeanByChannelId(validBusinessReportBeanList, item.getChannelId());
                if (tmp != null)
                    item.setValidBusinessSessionNum(tmp.getValidBusinessSessionNum());
            }

            List<CoreReportBean> acsSessionBeanList = calulateAcsSession(dateBeginStr, dateEndStr, targetChannels);
            for (CoreReportBean item : totalSessionReportBeanList) {
                CoreReportBean tmp = getBeanByChannelId(acsSessionBeanList, item.getChannelId());
                if (tmp != null) {
                    item.setAcsSessionNum(tmp.getAcsSessionNum());
                }
                item.setNonAcsSessionNum(item.getTotalSessionNum() - item.getAcsSessionNum());
            }
            List<CoreReportBean> validNoAcsSessionBeanList = calculateValidNoAcsSession(dateBeginStr, dateEndStr, targetChannels);
            for (CoreReportBean item : totalSessionReportBeanList) {
                CoreReportBean tmp = getBeanByChannelId(validNoAcsSessionBeanList, item.getChannelId());
                int tmpValidNoAcsNum = 0;
                if (tmp != null)
                    tmpValidNoAcsNum = tmp.getValidNoAcsSessionNum();
                item.setValidNoAcsSessionNum(item.getValidSessionNum() - tmpValidNoAcsNum);
            }
            List<CoreReportBean> interactRoundBeanList = calculateInteractRound(dateBeginStr, dateEndStr, targetChannels);
            for (CoreReportBean item : totalSessionReportBeanList) {
                CoreReportBean tmp = getBeanByChannelId(interactRoundBeanList, item.getChannelId());
                if (tmp != null)
                    item.setInteractRound(tmp.getInteractRound());
            }

            List<CoreReportBean> averageBeanList = calculateAverageConversionTime(dateBeginStr, dateEndStr, targetChannels);
            for (CoreReportBean item : totalSessionReportBeanList) {
                CoreReportBean tmp = getBeanByChannelId(averageBeanList, item.getChannelId());
                if (tmp != null)
                    item.setAverageConversionTime(tmp.getAverageConversionTime());
            }

            //计算各种率
            for (CoreReportBean item : totalSessionReportBeanList) {
                item.setAcsTotalRate(BigDecimalUtils.divide4Int(item.getAcsSessionNum(), item.getTotalSessionNum()));
                item.setValidAcsRate(BigDecimalUtils.divide4Int(item.getAcsSessionNum(), item.getValidSessionNum()));
                item.setNoAcsRate(BigDecimalUtils.divide4Int(item.getNonAcsSessionNum(), item.getTotalSessionNum()));
                item.setNoAcsValidRate(BigDecimalUtils.divide4Int(item.getValidNoAcsSessionNum(), item.getValidSessionNum()));
                item.setMachineRate(BigDecimal.ONE.subtract(item.getAcsTotalRate()));
            }

            totalSessionReportBeanList.stream().forEach(item -> {
                item.setDateBegin(dateBeginStr);
                item.setDateEnd(dateEndStr);
            });


        } catch (Exception e) {
            e.printStackTrace();
            LoggerFactory.getLogger().error("Connection with MySQL failed.");
            LoggerFactory.getLogger().error(e);
        }
        return totalSessionReportBeanList;
    }

    /***
     * 获取指定bean
     * @param ret
     * @param channelId
     * @return
     */
    private CoreReportBean getBeanByChannelId(List<CoreReportBean> ret, String channelId) {

        if (ret != null && ret.size() > 0) {
            for (CoreReportBean item : ret) {
                if (channelId == null && item != null && item.getChannelId() == null) {
                    return item;
                }
                if (item != null && channelId.equals(item.getChannelId()))
                    return item;
            }
        }
        return null;
    }

    /***
     * 计算有效无人工会话量
     * @param
     * @return
     */
    private List<CoreReportBean> calculateValidNoAcsSession(String dateBegin, String dateEnd, List<String> channelIdList) {
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryTotalSessionMoreThanOne(req);
        return cnt;
    }

    private List<CoreReportBean> calulateAcsSession(String dateBegin, String dateEnd, List<String> channelIdList) {

        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryAcsSession(req);
        return cnt;
    }


    private List<CoreReportBean> calculateValidSession(String dateBegin, String dateEnd, List<String> channelIdList) {
        HashMap<String, Object> req = new HashMap<String, Object>();
        req.put("beginDate", dateBegin);
        req.put("endDate", dateEnd);
        req.put("channnelId", channelIdList);
        List<CoreReportBean> cnt = MySQLHelper.getInstance().getChatRecordDao().queryValidSession(req);
        return cnt;

    }

    /***
     * 日期转换，若null则转换为当天
     * @param date
     * @param beginOrEnd
     * @return
     */
    private String transformBeginDate(Date date, boolean beginOrEnd) {
        if (date == null) {
            String dateform = null;
            if (beginOrEnd) {
                dateform = DAY_BEGIN_POINT_FORMAT;
            } else {
                dateform = DAY_END_POINT_FORMAT;
            }
            return DateFormatUtils.format(new Date(), dateform);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }


}
