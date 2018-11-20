package type.sentence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import utils.JSONUtils;

public class NBest implements Comparable<NBest> {
    @Expose
    @SerializedName("score")
    public int score;

    @Expose
    @SerializedName("text")
    public String text;

    @Override
    public int compareTo(NBest n2) {
        return this.score - n2.score;
    }

    public NBest() {
        super();
    }

    public NBest(int score, String text) {
        this.score = score;
        this.text = text;
    }

    @Override
    public String toString() {
        return JSONUtils.toJSon(this);
    }
}
