package report.bean;

import java.util.Date;

public class SessionDetailItemDto {
    private String userq;
    private String answer;

    private String createdTimeStr;

    public String getCreatedTimeStr() {
        return createdTimeStr;
    }

    public void setCreatedTimeStr(String createdTimeStr) {
        this.createdTimeStr = createdTimeStr;
    }


    public String getUserq() {
        return userq;
    }

    public void setUserq(String userq) {
        this.userq = userq;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
