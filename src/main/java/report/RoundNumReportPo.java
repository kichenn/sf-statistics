package report;

public class RoundNumReportPo {

    private String channelId;
    private String acsType;
    private int roundNum;
    private Long roundNumCnt;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAcsType() {
        return acsType;
    }

    public void setAcsType(String acsType) {
        this.acsType = acsType;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public Long getRoundNumCnt() {
        return roundNumCnt;
    }

    public void setRoundNumCnt(Long roundNumCnt) {
        this.roundNumCnt = roundNumCnt;
    }
}
