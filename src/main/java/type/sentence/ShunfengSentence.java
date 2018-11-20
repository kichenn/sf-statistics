/*
 * Copyright (c) 2016 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: xiongyaohua@emotibot.com.cn
 * Secondary Owner:
 */
package type.sentence;

import log.LoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import type.immutable.Answer;
import type.immutable.FAQQuestion;
import type.immutable.FAQRes;
import type.immutable.ModuleName;
import type.response.IResultBean;
import type.response.ShunfengResultBean;
import utils.JSONUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * base implementation of sentence for concurrency.
 *
 * @ThreadSafe
 */
public class ShunfengSentence extends BaseSentence {

    private final String relatedQuestionPrefix = "{HUASHU}\r\n\r\n你可能想问以下问题:";
    private final String relatedQuestionSuffix = "\r\n亲，请直接回复数字进行了解哦";

    private final String relatedQuestionItemPrefix = "\r\n[link submit=\"";
    private final String relatedQuestionItemSuffix = "[/link]";

    private final String ACSMsg = "ACS";

    private final int maxRelatedQuestionLength = 3;



    public ShunfengSentence(String sentence) {
        super(sentence);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setResponse(IResultBean resultBean) {

        ShunfengResultBean shunfengResultBean = (ShunfengResultBean) resultBean;

        if (message == null || message.isEmpty()) {
            shunfengResultBean.setCode(ShunfengResultBean.StatusType.SUCCESS.value);
            shunfengResultBean.setErrormsg("success");


            if (responseFeatures.containsKey(Sentence.ResponseFeature.ANSWERS)) {
                ArrayList<Answer> answers = (ArrayList<Answer>) responseFeatures.get(Sentence.ResponseFeature.ANSWERS);
                LoggerFactory.getLogger().info(JSONUtils.toJSon(answers));
                if (answers != null && answers.size() >= 1) {
                    shunfengResultBean.setMessage(answers.get(0).value);
                }

                if (responseFeatures.containsKey(Sentence.ResponseFeature.FAQ_RES)){
                    FAQRes faqRes = (FAQRes) responseFeatures.get(Sentence.ResponseFeature.FAQ_RES);

                    if (faqRes.getMatchedAnswerRecordId() != null){
                        shunfengResultBean.setContext(String.valueOf(faqRes.getMatchedAnswerRecordId()));
                        shunfengResultBean.setNeedEvaluate(true);
                    }

                    List<Answer> answerList = faqRes.getAnswers();
                    for (Answer item : answerList){
                        if ("text".equals(item.type) && "relatelist".equals(item.subType)){
                            String relatedQuestionHuashu = item.value;
                            List<Object> relatedList = item.datas;
                            if (relatedList != null && relatedList.size() > maxRelatedQuestionLength) {
                                relatedList = relatedList.subList(0, maxRelatedQuestionLength);
                            }
                            String msgTmp = shunfengResultBean.getMessage();
                            StringBuilder stringBuilder = new StringBuilder(msgTmp);
                            stringBuilder.append("\r\n\r\n");
                            stringBuilder.append(relatedQuestionHuashu);
                            int i = 1;
                            for (Object objItem : relatedList){
                                stringBuilder.append(relatedQuestionItemPrefix);
                                stringBuilder.append(String.valueOf(objItem));
                                stringBuilder.append("\"]【");
                                stringBuilder.append(i++);
                                stringBuilder.append("】");
                                stringBuilder.append(String.valueOf(objItem));
                                stringBuilder.append(relatedQuestionItemSuffix);
                            }
                            shunfengResultBean.setMessage(stringBuilder.toString());
                            break;
                        }
                    }
                }


            }

            if (responseFeatures.containsKey(Sentence.ResponseFeature.MODULE)) {
                String moduleName = (String) responseFeatures.get(Sentence.ResponseFeature.MODULE);
                shunfengResultBean.setType(convertType(moduleName));
                if (ACSMsg.equals(shunfengResultBean.getMessage())) {
                    shunfengResultBean.setType(4);
                }
            }

            if (responseFeatures.containsKey(Sentence.ResponseFeature.RELATED_QUESTIONS)) {
                List<FAQQuestion> related = (ArrayList<FAQQuestion>) getResponseFeature(
                        Sentence.ResponseFeature.RELATED_QUESTIONS, List.class);
                if (related != null && related.size() > maxRelatedQuestionLength) {
                    related = related.subList(0, maxRelatedQuestionLength);
                }
                List<String> relatedStringList = new ArrayList<>();

                for (FAQQuestion item : related) {
                    relatedStringList.add(item.question);
                }
                String msg = shunfengResultBean.getMessage();
                if (StringUtils.isNotBlank(msg) && related.size() > 0) {
                    shunfengResultBean.setMessage(relatedQuestionStr(msg, related));
                    shunfengResultBean.setRelatedQuestions(relatedStringList);
                    shunfengResultBean.setType(2);
                }

            }

        } else {
            shunfengResultBean.setCode(ShunfengResultBean.StatusType.FAIL.value);
            shunfengResultBean.setErrormsg(message);
        }

    }

    public String relatedQuestionStr(String huashuMsg, List<FAQQuestion> related) {
        if (CollectionUtils.isEmpty(related)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(relatedQuestionPrefix.replace("{HUASHU}", huashuMsg));
        int cnt = 1;
        for (FAQQuestion item : related) {
            stringBuilder.append(relatedQuestionItemPrefix);
            stringBuilder.append(item.question);
            stringBuilder.append("\"]【");
            stringBuilder.append(cnt++);
            stringBuilder.append("】");
            stringBuilder.append(item.question);
            stringBuilder.append(relatedQuestionItemSuffix);
        }
        stringBuilder.append(relatedQuestionSuffix);
        return stringBuilder.toString();
    }

    private int convertType(String moduleName) {
        if (ModuleName.CHAT.equals(moduleName)) {
            return 3;
        } else if (ModuleName.TO_HUMAN.equals(moduleName)) {
            return 4;
        } else if (StringUtils.isNotBlank(moduleName)) {
            return 1;
        }
        return -1;
    }

    public static void main(String[] args) {
        List<FAQQuestion> related = new ArrayList<>();
        related.add(new FAQQuestion("无单号查件", 1D));
        related.add(new FAQQuestion("快件几点更新", 1D));
        related.add(new FAQQuestion("快件能不能自取", 1D));
        related.add(new FAQQuestion("怎么申请电子发票", 1D));
        related.add(new FAQQuestion("怎么更改快件联系方式", 1D));
        related.add(new FAQQuestion("已发出快件如何转寄、退回", 1D));


        System.out.println(new ShunfengSentence("").relatedQuestionStr("",related));
    }


}
