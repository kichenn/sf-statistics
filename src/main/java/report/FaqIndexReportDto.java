package report;


import java.math.BigDecimal;

public class FaqIndexReportDto {
    private String dateBeginStr;
    private String dateEndStr;
    private String channelId;

    private Long totalRound = 0L;

    private Long faqTotalNum = 0L;
    private Long directAnswerNum = 0L;
    private BigDecimal directAnswerRate;

    private Long recommendAnswerNum = 0L;
    private BigDecimal recommendAnswerRate;

    private Long faqMissNum = 0L;
    private BigDecimal faqMissRate;


    public String getDateBeginStr() {
        return dateBeginStr;
    }

    public void setDateBeginStr(String dateBeginStr) {
        this.dateBeginStr = dateBeginStr;
    }

    public String getDateEndStr() {
        return dateEndStr;
    }

    public void setDateEndStr(String dateEndStr) {
        this.dateEndStr = dateEndStr;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


    public Long getTotalRound() {
        return totalRound;
    }

    public void setTotalRound(Long totalRound) {
        this.totalRound = totalRound;
    }

    public Long getFaqTotalNum() {
        return faqTotalNum;
    }

    public void setFaqTotalNum(Long faqTotalNum) {
        this.faqTotalNum = faqTotalNum;
    }

    public Long getDirectAnswerNum() {
        return directAnswerNum;
    }

    public void setDirectAnswerNum(Long directAnswerNum) {
        this.directAnswerNum = directAnswerNum;
    }

    public BigDecimal getDirectAnswerRate() {
        return directAnswerRate;
    }

    public void setDirectAnswerRate(BigDecimal directAnswerRate) {
        this.directAnswerRate = directAnswerRate;
    }

    public Long getRecommendAnswerNum() {
        return recommendAnswerNum;
    }

    public void setRecommendAnswerNum(Long recommendAnswerNum) {
        this.recommendAnswerNum = recommendAnswerNum;
    }

    public BigDecimal getRecommendAnswerRate() {
        return recommendAnswerRate;
    }

    public void setRecommendAnswerRate(BigDecimal recommendAnswerRate) {
        this.recommendAnswerRate = recommendAnswerRate;
    }

    public Long getFaqMissNum() {
        return faqMissNum;
    }

    public void setFaqMissNum(Long faqMissNum) {
        this.faqMissNum = faqMissNum;
    }

    public BigDecimal getFaqMissRate() {
        return faqMissRate;
    }

    public void setFaqMissRate(BigDecimal faqMissRate) {
        this.faqMissRate = faqMissRate;
    }
}
