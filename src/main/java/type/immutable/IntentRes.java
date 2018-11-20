package type.immutable;

/*
 * Copyright (c) 2017 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: zhipenghao@emotibot.com
 * Secondary Owner:
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class IntentRes {
    @Expose
    @SerializedName("item")
    private String item;
    @Expose
    @SerializedName("score")
    private Double score;
    @Expose
    @SerializedName("other_info")
    private List<Object> other_info = new ArrayList<>();

    public IntentRes() {

    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<Object> getOther_info() {
        return other_info;
    }

    public void setOther_info(List<Object> other_info) {
        this.other_info = other_info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof IntentRes)) return false;

        IntentRes intentRes = (IntentRes) o;

        return new EqualsBuilder()
                .append(score, intentRes.score)
                .append(item, intentRes.item)
                .append(other_info, intentRes.other_info)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(item)
                .append(score)
                .append(other_info)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("item", item)
                .append("score", score)
                .append("other_info", other_info)
                .toString();
    }
}