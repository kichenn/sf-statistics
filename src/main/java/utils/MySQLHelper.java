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

    public IChatRecordDao getChatRecordDao() {
        return INSTANCE.iChatRecordDao;
    }
}

