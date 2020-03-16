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
    INSTANCE();

    private final String applicationContextPath = ConfigManagedService.getConfig().getStr(Constants.APPLICATION_CONTEXT_PATH);
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(applicationContextPath);



    private final IReportDao reportDao = (IReportDao) applicationContext.getBean("reportDao");

    private final IChatRecordDao iChatRecordDao = getChatRecordDaoInner();

    private final IRobotChannelStatisticsDao iRobotChannelStatisticsDao = getRobotChannelStatisticsDaoInner();

    private final ChannelManageDao channelManageDao = initChannelManageDao();


    public static MySQLHelper getInstance() {
        return INSTANCE;
    }

    public IReportDao getReportDao() {
        return INSTANCE.reportDao;
    }

    private IChatRecordDao getChatRecordDaoInner() {
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession session = sqlSessionFactory.openSession();
        return session.getMapper(IChatRecordDao.class);
    }

    private IRobotChannelStatisticsDao getRobotChannelStatisticsDaoInner() {
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession session = sqlSessionFactory.openSession();
        return session.getMapper(IRobotChannelStatisticsDao.class);
    }

    public IChatRecordDao getChatRecordDao() {
        return INSTANCE.iChatRecordDao;
    }

    public IRobotChannelStatisticsDao getRobotChannelStatisticsDao() {
        return INSTANCE.iRobotChannelStatisticsDao;
    }

    public ChannelManageDao initChannelManageDao() {
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) applicationContext.getBean("sqlSessionFactory");
        SqlSession session = sqlSessionFactory.openSession();
        return session.getMapper(ChannelManageDao.class);
    }

    public ChannelManageDao getChannelManageDao(){
        return INSTANCE.channelManageDao;
    }
}