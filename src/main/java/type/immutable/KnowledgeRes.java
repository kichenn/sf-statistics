package type.immutable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KnowledgeRes {
	@Expose
    @SerializedName("score")
	public Integer score;
	@Expose
    @SerializedName("answer")
	public String answer;
	@Override
	public String toString() {
		return String.format("score: %d, answer: %s", score, answer);
	}
}
