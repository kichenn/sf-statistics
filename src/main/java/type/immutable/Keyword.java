/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.immutable;

/**
 * keyword in sentence.
 *
 */
public class Keyword {

	/* key word */
	private String keyword;
	
	/* word2vector */
	private float[] vector;
	
	/* keyword level */
	private int level;
	
	public Keyword(String keyword, float[] vector, int level){
		this.keyword = keyword;
		this.vector = vector;
		this.level = level;
	}
	
	
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @return the vector
	 */
	public float[] getVector() {
		return vector;
	}


	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
}
