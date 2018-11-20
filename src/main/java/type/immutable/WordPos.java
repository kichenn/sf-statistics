/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.immutable;

/**
 * word POS in sentence.
 */
public class WordPos {

    /* word */
    private String word = null;

    /* word pos */
    private String pos = null;


    public WordPos(String word, String pos) {
        this.word = word;
        this.pos = pos;
    }

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @return the pos
     */
    public String getPos() {
        return pos;
    }

}
