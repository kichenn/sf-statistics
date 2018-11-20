package type.immutable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2018 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: frankchang@emotibot.com
 * Secondary Owner:
 */
public class Intent {

    public static class Prediction {
        @Expose
        @SerializedName("label")
        private String label;
    
        @Expose
        @SerializedName("score")
        private Double score;
    
        @Expose
        @SerializedName("other_info")
        private List<Object> otherInfo = new ArrayList<>();
    
        public Prediction() {}
    
        public String getLabel() {
            return label;
        }
    
        public void setLabel(String label) {
            this.label = label;
        }
    
        public Double getScore() {
            return score;
        }
    
        public void setScore(Double score) {
            this.score = score;
        }
    
        public List<Object> getOtherInfo() {
            return otherInfo;
        }
    
        public void setOtherInfo(List<Object> otherInfo) {
            this.otherInfo = otherInfo;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
    
            if (!(o instanceof Prediction)) return false;
    
            Prediction prediction = (Prediction) o;
    
            return new EqualsBuilder()
                    .append(label, prediction.label)
                    .append(score, prediction.score)
                    .append(otherInfo, prediction.otherInfo)
                    .isEquals();
        }
    
        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(label)
                    .append(score)
                    .append(otherInfo)
                    .toHashCode();
        }
    
        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("label", label)
                    .append("score", score)
                    .append("otherInfo", otherInfo)
                    .toString();
        }
    }

    @Expose
    @SerializedName("ver")
    private String version;

    @Expose
    @SerializedName("status")
    private String status;

    @Expose
    @SerializedName("predictions")
    private List<Prediction> predictions = new ArrayList<>();

    public Intent() {}

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }
    
    public ArrayList<IntentRes> getRes() {
		ArrayList<IntentRes> res = new ArrayList<IntentRes>();
		for(Prediction prediction: predictions) {
			IntentRes intent = new IntentRes();
			intent.setItem(prediction.getLabel());
			intent.setScore(prediction.getScore());
			intent.setOther_info(prediction.getOtherInfo());
			res.add(intent);
		}
		return res;
    }
    
    public CUIntent getCUintent() {
    		CUIntent cuIntent = new CUIntent();
    		List<IntentRes> intentReses = new ArrayList<IntentRes>();
    		for(Prediction prediction: predictions) {
    			IntentRes intentRes = new IntentRes();
    			intentRes.setItem(prediction.getLabel());
    			intentRes.setOther_info(prediction.getOtherInfo());
    			intentRes.setScore(prediction.getScore());
    			intentReses.add(intentRes);
    		}
    		cuIntent.setVersion(version);
    		cuIntent.setStatus(status);
    		cuIntent.setRes(intentReses);
    		return cuIntent;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Intent)) return false;

        Intent intent = (Intent) o;

        return new EqualsBuilder()
                .append(version, intent.version)
                .append(status, intent.status)
                .append(predictions, intent.predictions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(version)
                .append(status)
                .append(predictions)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("version", version)
                .append("status", status)
                .append("predictions", predictions)
                .toString();
    }
}
