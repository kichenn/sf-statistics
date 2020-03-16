package dao;

import org.mybatis.spring.annotation.MapperScan;
import report.bean.RobotChannelStatisticsBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: ChangluWang
 * Date: 2020-03-06 11:37 PM
 * Description:
 */
@MapperScan
public interface IRobotChannelStatisticsDao {

    List<RobotChannelStatisticsBean> queryRobotChannelStatistics();

    void addRobotChannel(Map param);

    List<RobotChannelStatisticsBean> queryRobotChannelList(Map param);
}
