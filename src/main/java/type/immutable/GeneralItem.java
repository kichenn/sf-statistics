package type.immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by yuao on 21/9/17.
 */
public class GeneralItem {

    private Double score;
    private String label;

    public static class Builder {
        private Double score;
        private String label;

        public Builder setScore(Double score) {
            this.score = score;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public GeneralItem build() {
            return new GeneralItem(this);
        }
    }

    private GeneralItem(Builder builder) {
        this.score = builder.score;
        this.label = builder.label;
    }

    public Double getScore() {
        return score;
    }

    public String getLabel() {
        return label;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("label", label)
                .append("score", score)
                .toString();
    }
}

