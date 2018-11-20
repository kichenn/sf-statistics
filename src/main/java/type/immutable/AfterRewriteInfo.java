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

public class AfterRewriteInfo {
    @Expose
    @SerializedName("intent_zoo")
    private CUIntent intentZoo;
    @Expose
    @SerializedName("CustomCU")
    protected List<CustomCU> customCU = new ArrayList<>();

    public AfterRewriteInfo() {

    }

    public void setCustomCU(List<CustomCU> customCU) {
        this.customCU = customCU;
    }

    public List<CustomCU> getCustomCU() {
        return customCU;
    }

    public CUIntent getIntentZoo() {
        return intentZoo;
    }

    public void setIntentZoo(CUIntent intentZoo) {
        this.intentZoo = intentZoo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AfterRewriteInfo)) return false;

        AfterRewriteInfo that = (AfterRewriteInfo) o;

        return new EqualsBuilder()
                .append(intentZoo, that.intentZoo)
                .append(customCU, that.customCU)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(intentZoo)
                .append(customCU)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("intentZoo", intentZoo)
                .append("customCU", customCU)
                .toString();
    }
}
