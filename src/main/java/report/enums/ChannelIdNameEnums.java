package report.enums;

public enum ChannelIdNameEnums {
    channelId717("717", "费用明细渠道"),
    channelId163("163", "投诉结案页面"),
    channelId848("848", "微信-WP"),
    channelId376("376", "支付宝生活号2"),
    channelId594("594", "微信-在线客服"),
    channelId166("166", "手机QQ"),
    channelId659("659", "速运官网触屏版"),
    channelId503("503", "官网会员系统"),
    channelId469("469", "速运官网PC版"),
    channelId464("464", "支付宝生活好1"),
    channelId873("873", "H5自助链接"),
    channelId719("719", "短信服务厅"),
    channelId861("861", "速运APP"),
    channelId9999("9999", "未知"),
    channelId4test("qatest","qatest");

    private String channelId;
    private String channelName;

    ChannelIdNameEnums(String channelId, String channelName) {
        this.channelId = channelId;
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


    public static String getChannelNameById(String id) {
        for (ChannelIdNameEnums item : ChannelIdNameEnums.values()) {
            if (item.getChannelId().equals(id)) {
                return item.getChannelName();
            }
        }
        return id;
    }
}
