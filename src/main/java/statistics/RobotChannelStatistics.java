package statistics;

import log.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import report.bean.RobotChannelStatisticsBean;
import utils.MySQLHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Author: ChangluWang
 * Date: 2020-03-06 6:03 PM
 * Description: 统计机器人（appid）被哪些渠道接入
 */
public class RobotChannelStatistics {
    public static final String entryPoint = "/v1/sf/robotChannelStatistics";
    public static final String listEntryPoint = "/v1/sf/robotChannelStatistics/list";

    private String appId;
    private String channelId;

    public static ConcurrentMap<String, List<String>> robotChannels = new ConcurrentHashMap<>();

    public RobotChannelStatistics(HttpServletRequest request) {
        String appId = request.getParameter("appId");
        String channelId = request.getParameter("channelId");
        this.appId = appId;
        this.channelId = channelId;
    }

    /**
     * 从请求数据中，会拿到一个appid和channelId，和robotChannels中的数据进行对比，如果是一个新的关系，则将这个关系
     * 写入到数据库中，并且更新robotChannels，否则不做任何操作
     */
    public void statistics() {
        if(!StringUtils.isBlank(appId) && !StringUtils.isBlank(appId.trim())
            && !StringUtils.isBlank(channelId) && !StringUtils.isBlank(channelId.trim())){
            if(robotChannels.size() == 0){
                synchronized (this){
                    if(robotChannels.size() == 0){
                        try {
                            //为空则先同步数据
                            List<RobotChannelStatisticsBean> robotChannelStatisticsList = MySQLHelper.getInstance().getRobotChannelStatisticsDao().queryRobotChannelStatistics();
                            for (RobotChannelStatisticsBean bean : robotChannelStatisticsList){
                                if(robotChannels.containsKey(bean.getAppId())) {
                                    List<String> channelList = robotChannels.get(bean.getAppId());
                                    if(!channelList.contains(bean.getChannelId())){
                                        channelList.add(bean.getChannelId());
                                    }
                                }else {
                                    List<String> channelList = new ArrayList<>();
                                    channelList.add(bean.getChannelId());
                                    robotChannels.put(bean.getAppId(), channelList);
                                }
                            }
                        }catch (Exception e){
                            LoggerFactory.getLogger().error(e.getMessage());
                        }
                    }
                }
            }
            if(robotChannels.containsKey(appId.trim())){
                List<String> channelIds = robotChannels.get(appId);
                if(!channelIds.contains(channelId)){
                    addRobotChannel(channelIds, appId, channelId);
                }
            } else {
                List<String> channelIds = new ArrayList<>();
                addRobotChannel(channelIds, appId, channelId);
            }
        }else {
            LoggerFactory.getLogger().error("appId and channelId cannot empty.");
        }
    }


    private void addRobotChannel(List<String> channelIds, String appId, String channelId){
        //1、先查询数据库中数据有同样appid-channelId的记录
        //2、如果没有则写入数据库; 如果有，将这条数据放入缓存
        // 表中已经做了appid_channelId的唯一性约束，所以这里直接插入即可
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("appId", appId);
            param.put("channelId", channelId);
            param.put("createdTime", new Date());
            param.put("updateTime", new Date());
            MySQLHelper.getInstance().getRobotChannelStatisticsDao().addRobotChannel(param);
        }catch (Exception e){
            LoggerFactory.getLogger().error(String.format("插入一条%s-%s记录时失败", appId, channelId));
            LoggerFactory.getLogger().error(e.getMessage());
        }
        channelIds.add(channelId);
        robotChannels.put(appId, channelIds);
    }

    public List<RobotChannelStatisticsBean> list(){
        if(!StringUtils.isBlank(appId) && !StringUtils.isBlank(appId.trim())){
            Map<String, Object> param = new HashMap<>();
            param.put("appId", appId);
            List<RobotChannelStatisticsBean> list = MySQLHelper.getInstance().getRobotChannelStatisticsDao().queryRobotChannelList(param);
            return list;
        }else {
            LoggerFactory.getLogger().error("appId cannot empty.");
            return null;
        }
    }

}
