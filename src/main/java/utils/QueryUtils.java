package utils;

/*
 * Copyright (c) 2017 by Emotibot Corporation. All rights reserved.
 * EMOTIBOT CORPORATION CONFIDENTIAL AND TRADE SECRET
 *
 * Primary Owner: zhipenghao@emotibot.com
 * Secondary Owner:
 */

import com.spreada.utils.chinese.ZHConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum QueryUtils {
    INSTANCE();

    private static ZHConverter converter = ZHConverter.getInstance(ZHConverter.SIMPLIFIED);

    QueryUtils() {}

    private static final String[] postPatterns = {
            "小影",
            "小精灵",
            "小宝",
            "小蜂"
    };

    private static final String inLineBreaker = "@E@";

    private static final String outLineBreaker = "<br/>";

    private static final String FUNCTION_JOKE_CONDITION_Q1 = "(^.*(再|继续)?[整讲来说看听出换要选挑找整给选取]?(一个|几个|个)?.*(动图|图片|文字)?(笑话|段子)(乐一乐|玩玩|玩一下|玩|逗乐一下|逗我一下|给我)?(听听)?.?)|(^(动图|图片|文字)?(笑话|段子)[整讲来说出]?[一几]?[个]?(?=$))|(.*[有|有没有].*(动图|图片|文字)?(笑话|段子|笑的).*)";
    private static final Pattern PATTERN_JOKE_CONDITION_Q1 = Pattern.compile(FUNCTION_JOKE_CONDITION_Q1);

    private static final String FUNCTION_JOKE_CONDITION_Q2 = "(^(我还想|还想再|再|继续|要|给我|我还要|我要|换|来|多发)[整讲来说看听出换要选挑找整给选取]?(一个|几个|个)?(动图|图片|文字)?类?(幽默|幽默点)?的?(笑话|段子)?(乐一乐|玩玩|玩一下|玩|逗乐一下|逗我一下|给我)?.?[吧啊哈呀呗嘛]*)|(^(动图|图片|文字)?类?的?(笑话|段子)?(晕)?(不好笑)?(再|继续|下)[整讲来说出发]?[一几]?[个]?(?=$))|((还|看)(有|(有没有))?(笑话|段子|笑的)?.?[吧啊哈呀]*)|((我)?还(想|想要)?(要|听)?(一个|几个|个)?(笑话|段子|笑的)?[吧啊哈呀]*)|(^还有[呢嘛]*)|(^继续[啊哈呀]*)";
    private static final Pattern PATTERN_JOKE_CONDITION_Q2 = Pattern.compile(FUNCTION_JOKE_CONDITION_Q2);

    public static boolean isJokeModule(String q) {
        Matcher m = PATTERN_JOKE_CONDITION_Q1.matcher(q);
        return m.matches();
    }

    public static boolean isJokeModuleContinuous(String q) {
        Matcher m = PATTERN_JOKE_CONDITION_Q2.matcher(q.replaceAll("[\\pP]", "").replace("~", ""));
        return m.matches();
    }

    public static String postProcess(String src) {
        if (src == null) {
            return null;
        }
      //  LoggerFactory.getLogger().debug("Before parse: " + src);
        src = src.replaceAll("\\\\n", "\n");
      //  LoggerFactory.getLogger().debug("After parse: " + src);

        src = src.replaceAll(inLineBreaker, outLineBreaker);

        return src;
    }

    public static String preProcess(String src) {
        if (src == null) {
            return null;
        }

        src = src.toLowerCase();

        src = converter.convert(src);

        return src;
    }
}
