package type.immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by yuao on 20/9/17.
 */
public class FAQItem {

    private Double score;
    private String source;
    private String answer;
    private String matchedSQ;

    public static class Builder {
        private Double score;
        private String source;
        private String answer;
        private String matchedSQ;

        public Builder setScore(Double score) {
            this.score = score;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setAnswer(String answer) {
            this.answer = answer;
            return this;
        }

        public Builder setMatchedSQ(String matchedSQ) {
            this.matchedSQ = matchedSQ;
            return this;
        }

        public FAQItem build() {
            return new FAQItem(this);
        }
    }

    private FAQItem(Builder builder) {
        this.score = builder.score;
        this.source = builder.source;
        this.answer = builder.answer;
        this.matchedSQ = builder.matchedSQ;
    }

    public String getMatchedSQ() {
        return matchedSQ;
    }

    public Double getScore() {
        return score;
    }

    public String getSource() {
        return source;
    }

    public String getAnswer() {
        return answer;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("score", score)
                .append("source", source)
                .append("answer", answer)
                .append("matchedSQ", matchedSQ)
                .toString();
    }
}
