package report.enums;

import channel.bean.ChannelPo;
import org.springframework.util.CollectionUtils;
import utils.MySQLHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelIdNameEnums {
    private static Map<String, String> channelMap = new HashMap();

    public static void init() {
        channelMap.clear();
        List<ChannelPo> channelList = MySQLHelper.INSTANCE.getChannelManageDao().listActiveChannel();
        if (!CollectionUtils.isEmpty(channelList)) {
            channelList.forEach(e -> channelMap.put(e.getChannelId(), e.getChannelName()));
        }
    }

    public static String getChannelNameById(String id) {
        if (CollectionUtils.isEmpty(channelMap)) {
            init();
        }
        return channelMap.get(id);
    }
}
