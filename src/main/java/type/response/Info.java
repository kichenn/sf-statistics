package type.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import type.immutable.FAQQuestion;

import java.util.ArrayList;

public class Info {
	
	@Expose
	@SerializedName("module")
	public String module;
	
	@Expose
	@SerializedName("textScore")
	public Integer textScore;
	
	@Expose
	@SerializedName("intent")
	public String intent;
	
	@Expose
	@SerializedName("intentScore")
	public Integer intentScore;
	
	@Expose
	@SerializedName("emotion")
	public String emotion;
	
	@Expose
	@SerializedName("emotionScore")
	public Integer emotionScore;
	
	@Expose
	@SerializedName("tokens")
	private ArrayList<String> tokens = null;
	
	@SerializedName("relatedQuestions")
	private ArrayList<FAQQuestion> relatedQuestions = null;

	@SerializedName("matchQuestion")
	private String matchQuestion = null;
	
	public Info() {}
	
	public void setModule(String module) {
		this.module = module;
	}
	public void setTextScore(Integer textScore) {
		this.textScore = textScore;
	}
	
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public void setIntentScore(Integer intentScore) {
		this.intentScore = intentScore;
	}
	
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public void setEmotionScore(Integer emotionScore) {
		this.emotionScore = emotionScore;
	}
	
	public void setTokens(ArrayList<String> tokens) {
		this.tokens = tokens;
	}
	
	public void setRelatedQuestions(ArrayList<FAQQuestion> relatedQuestions) {
		this.relatedQuestions = relatedQuestions;
	}
	
	public void setMatchQuestion(String matchQuestion) {
		this.matchQuestion = matchQuestion;
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
