package report.bean;

import java.math.BigDecimal;

public class CoreReportBean {
    private String dateBegin;
    private String dateEnd;
    private String channelId;
//    总会话量
    private int totalSessionNum;
//    有效会话量
    private int validSessionNum;
//    有效业务会话量
    private int validBusinessSessionNum;
//    转人工会话量
    private int acsSessionNum;
//    独立服务量 =：未转人工会话量： 总会话量 - 转人工会话量
    private int nonAcsSessionNum;
//    有效独立（未转人工）服务量 =： 有效会话量 - 用户至少一次发言且转人工量
    private int validNoAcsSessionNum;


//    总转人工率 =： acsSessionNum / totalSessionNum
    private BigDecimal acsTotalRate;
//    有效转人工率 =： acsSessionNum / validSessionNum
    private BigDecimal validAcsRate;
//    独立服务率 =： nonAcsSessionNum / totalSessionNum
    private BigDecimal noAcsRate;
//    有效独立服务率 =： validNoAcsSessionNum / validSessionNum；
    private BigDecimal noAcsValidRate;

//    机器人分流率 =： 1 - acsTotalRate
    private BigDecimal machineRate;

//    交互轮数
    private BigDecimal interactRound = BigDecimal.ZERO;
//    平均对话时长
    private BigDecimal averageConversionTime = BigDecimal.ZERO;

    public int getValidBusinessSessionNum() {
        return validBusinessSessionNum;
    }

    public void setValidBusinessSessionNum(int validBusinessSessionNum) {
        this.validBusinessSessionNum = validBusinessSessionNum;
    }

    public String getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        this.dateBegin = dateBegin;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public int getTotalSessionNum() {
        return totalSessionNum;
    }

    public void setTotalSessionNum(int totalSessionNum) {
        this.totalSessionNum = totalSessionNum;
    }

    public int getValidSessionNum() {
        return validSessionNum;
    }

    public void setValidSessionNum(int validSessionNum) {
        this.validSessionNum = validSessionNum;
    }

    public int getAcsSessionNum() {
        return acsSessionNum;
    }

    public void setAcsSessionNum(int acsSessionNum) {
        this.acsSessionNum = acsSessionNum;
    }

    public int getNonAcsSessionNum() {
        return nonAcsSessionNum;
    }

    public void setNonAcsSessionNum(int nonAcsSessionNum) {
        this.nonAcsSessionNum = nonAcsSessionNum;
    }

    public int getValidNoAcsSessionNum() {
        return validNoAcsSessionNum;
    }

    public void setValidNoAcsSessionNum(int validNoAcsSessionNum) {
        this.validNoAcsSessionNum = validNoAcsSessionNum;
    }

    public BigDecimal getInteractRound() {
        return interactRound;
    }

    public void setInteractRound(BigDecimal interactRound) {
        this.interactRound = interactRound;
    }

    public BigDecimal getAverageConversionTime() {
        return averageConversionTime;
    }

    public void setAverageConversionTime(BigDecimal averageConversionTime) {
        this.averageConversionTime = averageConversionTime;
    }

    public BigDecimal getAcsTotalRate() {
        return acsTotalRate;
    }

    public void setAcsTotalRate(BigDecimal acsTotalRate) {
        this.acsTotalRate = acsTotalRate;
    }

    public BigDecimal getValidAcsRate() {
        return validAcsRate;
    }

    public void setValidAcsRate(BigDecimal validAcsRate) {
        this.validAcsRate = validAcsRate;
    }

    public BigDecimal getNoAcsRate() {
        return noAcsRate;
    }

    public void setNoAcsRate(BigDecimal noAcsRate) {
        this.noAcsRate = noAcsRate;
    }

    public BigDecimal getNoAcsValidRate() {
        return noAcsValidRate;
    }

    public void setNoAcsValidRate(BigDecimal noAcsValidRate) {
        this.noAcsValidRate = noAcsValidRate;
    }

    public BigDecimal getMachineRate() {
        return machineRate;
    }

    public void setMachineRate(BigDecimal machineRate) {
        this.machineRate = machineRate;
    }
}
