package utils;

import config.ConfigManagedService;
import config.Constants;
import dao.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yuao on 3/11/17.
 */
public enum MySQLHelper {
    //
    INSTANCE();

    private final String applicationContextPath = ConfigManagedService.getConfig().getStr(Constants.APPLICATION_CONTEXT_PATH);
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(applicationContextPath);
    private final SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
    private final SqlSession session = sqlSessionFactory.openSession();


    private final IReportDao reportDao = (IReportDao) applicationContext.getBean("reportDao");

    private final IChatRecordDao iChatRecordDao = getChatRecordDaoInner();

    private final IRobotChannelStatisticsDao iRobotChannelStatisticsDao = getRobotChannelStatisticsDaoInner();

    private final ChannelManageDao ichannelManageDao = getChannelManageDaoInner();


    public static MySQLHelper getInstance() {
        return INSTANCE;
    }

    public IReportDao getReportDao() {
        return INSTANCE.reportDao;
    }

    private IChatRecordDao getChatRecordDaoInner() {
        return session.getMapper(IChatRecordDao.class);
    }

    private IRobotChannelStatisticsDao getRobotChannelStatisticsDaoInner() {
        return session.getMapper(IRobotChannelStatisticsDao.class);
    }


    public ChannelManageDao getChannelManageDaoInner() {
        return session.getMapper(ChannelManageDao.class);
    }

    public IChatRecordDao getChatRecordDao() {
        return INSTANCE.iChatRecordDao;
    }

    public IRobotChannelStatisticsDao getRobotChannelStatisticsDao() {
        return INSTANCE.iRobotChannelStatisticsDao;
    }


    public ChannelManageDao getChannelManageDao(){
        return INSTANCE.ichannelManageDao;
    }
}