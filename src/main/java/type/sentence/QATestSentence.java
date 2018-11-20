/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.sentence;

import type.immutable.FAQQuestion;
import type.response.IResultBean;
import type.response.Info;

import java.util.ArrayList;
import java.util.List;


/**
 * base implementation of sentence for concurrency.
 *
 * @ThreadSafe
 */
public class QATestSentence extends BaseSentence {

	public QATestSentence(String sentence) {
		super(sentence);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setResponse(IResultBean resultBean) {
		super.setResponse(resultBean);
		
		Info info = resultBean.getInformation();
		
		// set relatedQuestions
		if (responseFeatures.containsKey(ResponseFeature.RELATED_QUESTIONS)) {
			ArrayList<FAQQuestion> related = (ArrayList<FAQQuestion>) getResponseFeature(
					ResponseFeature.RELATED_QUESTIONS, List.class);
			info.setRelatedQuestions(related);
		}
		
	}
}
