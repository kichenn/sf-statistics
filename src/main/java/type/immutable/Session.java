package type.immutable;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import type.sentence.IMutableSentence;

import java.time.Instant;

public class Session {

    @Expose
    @SerializedName("session_id")
    public String sessionId;

    @Expose
    @SerializedName("start_time")
    public long start_time;

    @Expose
    @SerializedName("end_time")
    public long end_time;

    @Expose
    @SerializedName("app_id")
    public String app_id;

    @Expose
    @SerializedName("user_id")
    public String user_id;
    
    @Expose
    @SerializedName("custom_info")
    public Object customInfo;

    @Expose
    @SerializedName("channelId")
    public String channelId;

    public Session() {
        this.sessionId = "";
        this.start_time = 0;
        this.end_time = 0;
        this.app_id = "";
        this.user_id = "";
    }

    public Session(IMutableSentence sentence) {
        Long timestamp = Instant.now().getEpochSecond();
        this.user_id = sentence.getUserId();
        this.sessionId = sentence.getSessionId();

        /**
         * because we dont know if this session instance is used for create or update,
         * so we just use current timestamp and wait for
         **/
        this.start_time = timestamp;
        this.end_time = timestamp;
        this.app_id = sentence.getRobotId();
        this.channelId = sentence.getChannelId();
        try {
            this.customInfo = sentence.getCustomInfo();
        } catch (JsonSyntaxException e){
            e.printStackTrace();
        }
    }

    public String getSessionId() {
        return this.sessionId;
    }

}
