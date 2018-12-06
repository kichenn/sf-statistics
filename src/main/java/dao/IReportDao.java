package dao;

import report.bean.CoreReportBean;

import java.util.Date;
import java.util.List;

/**
 * Created by changmin on 2018/3/7.
 */
public interface IReportDao {

	List<CoreReportBean> queryReport(Date beginDate, Date endDate, List<String> targetChannels);

}
