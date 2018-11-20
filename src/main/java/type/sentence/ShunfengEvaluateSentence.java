/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.sentence;

import log.LoggerFactory;
import type.immutable.Answer;
import type.response.IResultBean;
import type.response.ShunfengEvaluateResultBean;
import utils.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * base implementation of sentence for concurrency.
 *
 * @ThreadSafe
 */
public class ShunfengEvaluateSentence extends BaseSentence {

    public ShunfengEvaluateSentence(String sentence) {
        super(sentence);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setResponse(IResultBean resultBean) {

        ShunfengEvaluateResultBean shunfengEvaluateResultBean = (ShunfengEvaluateResultBean) resultBean;

        if (message == null || message.isEmpty()) {
            shunfengEvaluateResultBean.setCode(ShunfengEvaluateResultBean.StatusType.SUCCESS.value);
            shunfengEvaluateResultBean.setErrormsg("success");

            if (responseFeatures.containsKey(Sentence.ResponseFeature.ANSWERS)) {
                ArrayList<Answer> answers = (ArrayList<Answer>) responseFeatures.get(Sentence.ResponseFeature.ANSWERS);
                LoggerFactory.getLogger().info(JSONUtils.toJSon(answers));

                if (customInfo != null && ((HashMap) customInfo).containsKey("uniqueId")) {
                    String uniqueId = (String) ((HashMap) customInfo).get("uniqueId");
                    if (uniqueId == null) {
                        shunfengEvaluateResultBean.setCode(ShunfengEvaluateResultBean.StatusType.FAIL.value);
                        shunfengEvaluateResultBean.setErrormsg("error");
                        LoggerFactory.getLogger().error("Evaluate api context param error");
                        return;
                    }
                }
                if (answers != null && answers.size() >= 1 && answers.get(0).value != "error") {
                    shunfengEvaluateResultBean.setType(0);
                    shunfengEvaluateResultBean.setMessage(answers.get(0).value);
                } else {
                    shunfengEvaluateResultBean.setCode(ShunfengEvaluateResultBean.StatusType.FAIL.value);
                    shunfengEvaluateResultBean.setErrormsg("error");
                    LoggerFactory.getLogger().error("Evaluate api reason number error");
                    return;
                }
                if (customInfo != null && ((HashMap) customInfo).containsKey("reasonList")) {
                    shunfengEvaluateResultBean.setType(1);
                    shunfengEvaluateResultBean.setReason((List<String>) ((HashMap) customInfo).get("reasonList"));
                }
            }

        } else {
            shunfengEvaluateResultBean.setCode(ShunfengEvaluateResultBean.StatusType.FAIL.value);
            shunfengEvaluateResultBean.setErrormsg(message);
        }

    }

}
