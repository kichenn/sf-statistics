package dao;

import org.mybatis.spring.annotation.MapperScan;
import report.bean.*;

import java.util.List;
import java.util.Map;

/**
 * Created by changmin on 2018/3/7.
 */
@MapperScan
public interface IChatRecordDao {

    List<TaskEngineInfoDomain> queryAllScenarioInfo();

    List<ChatRecordEntity> exportSessionDetail(Map req);

    List<SessionDetailItemDto> querySessionDetail(Map req);

    List<ChatRecordEntity> queryChatRecordAll(Map req);

    List<StaticRecordDto> queryStaticRecordInfo(Map req);

    List<StaticRecordDto> queryStaticRecordReasonInfo(Map req);

    List<CoreReportBean> queryTotalSession(Map req);

    List<CoreReportBean> queryValidBusinessSession(Map req);

    List<CoreReportBean> queryValidSession(Map req);

    List<CoreReportBean> queryAcsSession(Map req);

    @Deprecated
    List<CoreReportBean> queryTotalSessionMoreThanOne(Map req);


    List<CoreReportBean> queryValidNoAcsBussinessNum(Map req);

    List<CoreReportBean> queryInterActRound(Map req);

    List<CoreReportBean> queryAverageConversionTime(Map req);

    List<RoundNumReportPo> calRoundNumReport(Map req);

    List<HourSessionReportPo> hourSessionReport(Map req);

    List<DialogueTimeReportPo> dialogueTimeReport(Map req);



    List<FaqIndexReportDto> faqIndexRecommendReport(Map req);
    List<FaqIndexReportDto> faqIndexDirectReport(Map req);
    List<FaqIndexReportDto> faqIndexMissReport(Map req);
    List<FaqIndexReportDto> faqIndexTotalRoundReport(Map req);
}


