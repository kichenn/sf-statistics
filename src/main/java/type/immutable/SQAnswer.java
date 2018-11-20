package type.immutable;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyman on 2018/1/17.
 */
public class SQAnswer {

    private DalAnswer.DalAnswerActualResult matchedAnswer;

    private String content = "";

    private Command command = null;

    private List relatedSq = new ArrayList();

    public DalAnswer.DalAnswerActualResult getMatchedAnswer() {
        return matchedAnswer;
    }

    public void setMatchedAnswer(DalAnswer.DalAnswerActualResult matchedAnswer) {
        this.matchedAnswer = matchedAnswer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

 
    public void setCommand(Command command) {
		this.command = command;
	}
    
    public Command getCommand() {
		return this.command;
	}
    
    
    public List GetRelatedSqContent() {
        return relatedSq;
    }

    public void SetRelatedSqContent(List sq) {
        relatedSq = sq;
    }

}
