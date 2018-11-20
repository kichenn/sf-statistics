package report;

public class StaticRecordDto {
    private String dateBegin;
    private String dateEnd;
    private String channelId;
    private String bQuestion;
    private String answerId;
    private String visitCnt;
    private Long solvedCnt;
    private Long unSolvedCnt;

    private String reason;
    private Long reasonCnt;


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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getReasonCnt() {
        return reasonCnt;
    }

    public void setReasonCnt(Long reasonCnt) {
        this.reasonCnt = reasonCnt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getbQuestion() {
        return bQuestion;
    }

    public void setbQuestion(String bQuestion) {
        this.bQuestion = bQuestion;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getVisitCnt() {
        return visitCnt;
    }

    public void setVisitCnt(String visitCnt) {
        this.visitCnt = visitCnt;
    }

    public Long getSolvedCnt() {
        return solvedCnt;
    }

    public void setSolvedCnt(Long solvedCnt) {
        this.solvedCnt = solvedCnt;
    }

    public Long getUnSolvedCnt() {
        return unSolvedCnt;
    }

    public void setUnSolvedCnt(Long unSolvedCnt) {
        this.unSolvedCnt = unSolvedCnt;
    }
}
