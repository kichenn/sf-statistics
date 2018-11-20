package type.immutable;

import java.util.ArrayList;

/**
 * 
 * 
 * Created by changmin on 2018/3/8.
 */
public class FAQRes {

	//确认是否faq有出答案 true:有出
    private FlagType finalFlag = FlagType.NO_ANSWER;
    //faq所出的答案(score>=faqThreshold0 && 最高分)
    private String matchQ = "";
    private ArrayList<Answer> answers = new ArrayList<Answer>();
    private String source = "";
    private Double score = 0.0;

    //answer关联的label关联的rule
    private Command cmd = null;
    
    //此轮对话推荐的问题，包含分数不够高的或指定相关问
    private ArrayList<String> recommandedQuesitons = new ArrayList<String>();
    //相关问题(score>=faqThreshold1)
    private ArrayList<FAQQuestion> relatedQuesitons = new ArrayList<FAQQuestion>();

    private Integer matchedAnswerRecordId;

    public Integer getMatchedAnswerRecordId() {
        return matchedAnswerRecordId;
    }

    public void setMatchedAnswerRecordId(Integer matchedAnswerRecordId) {
        this.matchedAnswerRecordId = matchedAnswerRecordId;
    }

    public void setFinalFlag(FlagType finalFlag) {
    		this.finalFlag = finalFlag;
    }
    public FlagType getFinalFlag() {
		return finalFlag;
    }
    
    public void setMatchQ(String matchQ) {
		this.matchQ = matchQ;
	}
    public String getMatchQ() {
    		return matchQ;
    }
    
    
    public void addAnswers(ArrayList<Answer> answers) {
		this.answers.addAll(answers);
    }
    public void addAnswer(Answer answer) {
    		this.answers.add(answer);
    }
    public void addTextAnswer(String answer) {
		this.answers.add(new Answer("text", "text", answer));
	}
    public ArrayList<Answer> getAnswers() {
    		return answers;
    }
    
    public void setSource(String source) {
		this.source = source;
	}
    public String getSource() {
		return source;
	}
    
    public void setScore(Double score) {
		this.score = score;
	}
    public Double getScore() {
		return score;
	}
    
    public void setRelatedQuesitons(ArrayList<FAQQuestion> relatedQuesitons) {
		this.relatedQuesitons = relatedQuesitons;
	}
    public ArrayList<FAQQuestion> getRelatedQuesiton() {
		return relatedQuesitons;
	}
    
    public void setRecommandedQuesitons(ArrayList<String> recommandedQuesitons) {
		this.recommandedQuesitons = recommandedQuesitons;
	}
    public ArrayList<String> getRecommandedQuesitons() {
		return recommandedQuesitons;
	}

    public Command getCmd() {
		return cmd;
	}
    public void setCmd(Command cmd) {
		this.cmd = cmd;
	}
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append("score: ").append(score)
                .append(", source: ").append(source)
                .append(", answer: ").append(answers.toString())
                .append(", match_q: ").append(matchQ)
                .append(", final_flag: ").append(finalFlag)
                .append(", relatedQuestion: ").append(relatedQuesitons)
                .toString();
    }
    
    /**
     * 
     * ONE_ANSWER: 有一個確定的答案
     * NO_ANSWER: 沒有找到答案
     * RECOMMEND: 使用推薦問的答案
     * 
     */
    public enum FlagType {
    		NO_ANSWER(-1),
		ONE_ANSWER(1),
		RECOMMEND(2); 
		
		public int value;
	    private FlagType(int value) {
	        this.value = value;
	    }
	}
}
