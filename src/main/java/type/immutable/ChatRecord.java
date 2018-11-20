package type.immutable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import type.sentence.IMutableSentence;
import type.sentence.Sentence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;

public class ChatRecord {
	
	@Expose
    @SerializedName("unique_id")
    private String uniqueId;

	@Expose
    @SerializedName("user_id")
    private String userId;

	@Expose
    @SerializedName("app_id")
    private String robot;

	@Expose
    @SerializedName("session_id")
    private String sessionId;

	@Expose
    @SerializedName("user_q")
    private String userQ;

	@Expose
    @SerializedName("std_q")
    private String stdQ;

	@Expose
    @SerializedName("answer")
    private ArrayList<Answer> answer;

	@Expose
    @SerializedName("module")
    private String module;

	@Expose
    @SerializedName("intent")
    private String intent;

	@Expose
    @SerializedName("intent_score")
    private int intentScore;

	@Expose
    @SerializedName("emotion")
    private String emotion;

	@Expose
    @SerializedName("emotion_score")
    private int emotionScore;

	@Expose
    @SerializedName("score")
    private int score;

	@Expose
    @SerializedName("host")
    private String host;

	@Expose
    @SerializedName("log_time")
    private String logTime;

	@Expose
    @SerializedName("custom_info")
    private Object customInfo;

	@Expose
    @SerializedName("note")
    private String note;

	@Expose
    @SerializedName("channelId")
    private String channelId;

    public ChatRecord(IMutableSentence sentence) {
        //common part 对话机器人基本资讯储存
        this.uniqueId = sentence.getUniqueId();
        this.userId = sentence.getUserId();
        this.robot = sentence.getRobotId();
        this.sessionId = sentence.getSessionId();
        this.userQ = sentence.getRawSentence();
        this.stdQ = sentence.getResponseFeature(Sentence.ResponseFeature.MATCHED_QUESTION, String.class);
        this.answer = sentence.getResponseFeature(Sentence.ResponseFeature.ANSWERS, ArrayList.class);
        this.module = sentence.getResponseFeature(Sentence.ResponseFeature.MODULE, String.class);
        this.intent = sentence.getResponseFeature(Sentence.ResponseFeature.INTENT, String.class);
        Double intentScoreD = sentence.getResponseFeature(Sentence.ResponseFeature.SCORE, Double.class);
        this.intentScore = intentScoreD != null ? intentScoreD.intValue() : 0;
        this.emotion = sentence.getResponseFeature(Sentence.ResponseFeature.EMOTION, String.class);
        Double emotionScoreD = sentence.getResponseFeature(Sentence.ResponseFeature.SCORE, Double.class);
        this.emotionScore = emotionScoreD != null ? emotionScoreD.intValue() : 0;
        Double scoreD = sentence.getResponseFeature(Sentence.ResponseFeature.SCORE, Double.class);
        this.score = scoreD != null ? scoreD.intValue() : 0;

        this.channelId = sentence.getChannelId();

        this.host = "";

        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.logTime = Instant.now().toString();
        // 此时间格式(RFC3339)末尾长度不一，统一尾数让SQL容易parse
        this.logTime = logTime.substring(0, 19) + ".000Z";

        try {
            //String customInfoString = sentence.getCustomInfo();
            this.customInfo = sentence.getCustomInfo();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        }

        this.note = sentence.getNote();
        this.sessionId = sessionId != null ? sessionId : "";
        this.robot = robot != null ? robot : "";
        this.stdQ = stdQ != null ? stdQ : "";
        this.module = module != null ? module : "";
        this.intent = intent != null ? intent : "";
        this.emotion = emotion != null ? emotion : "";
        this.host = host != null ? host : "";
        this.logTime = logTime != null ? logTime : "";
      
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getRobot() {
        return this.robot;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getUserQ() {
        return this.userQ;
    }

    public String getStdQ() {
        return this.stdQ;
    }

    public String getAnswer() {
        if (this.answer == null) {
            return "";
        }

        Gson gson = new Gson();
        return gson.toJson(this.answer);
    }

    public String getModule() {
        return this.module;
    }

    public String getIntent() {
        return this.intent;
    }

    public int getIntentScore() {
        return this.intentScore;
    }

    public String getEmotion() {
        return this.emotion;
    }

    public int getEmotionScore() {
        return this.emotionScore;
    }

    public int getScore() {
        return this.score;
    }

    public String getHost() {
        return this.host;
    }

    public String getLogTime() {
        return this.logTime;
    }

    public Object getCustomInfo() {
		return customInfo;
	}
  
    public String getNote() {
        return this.note;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}