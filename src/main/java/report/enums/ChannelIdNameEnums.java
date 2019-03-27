package report.enums;

public enum ChannelIdNameEnums {
    channelId909("909", "第三方服务窗"),
    channelId254("254", "IVR引流-12"),
    channelId745("745", "IVR引流-11"),
    channelId823("823", "IVR引流-10"),
    channelId828("828", "IVR引流-09"),
    channelId234("234", "IVR引流-08"),
    channelId715("715", "IVR引流-07"),
    channelId139("139", "IVR引流-06"),
    channelId215("215", "IVR引流-05"),
    channelId788("788", "IVR引流-04"),
    channelId381("381", "IVR引流-03"),
    channelId498("498", "IVR引流-02"),
    channelId950("950", "IVR引流-01"),
    channelId362("362", "微信新版"),
    channelId610("610", "速运小程序"),
    channelId758("758", "自助理赔_微信"),
    channelId717("717", "费用明细渠道_丰发2"),
    channelId163("163", "投诉结案页面_丰发"),
    channelId848("848", "微信 WP-丰发"),
    channelId376("376", "支付宝生活号_丰发2"),
    channelId594("594", "微信--在线客服_丰发"),
    channelId166("166", "手机QQ_丰发"),
    channelId659("659", "速运官网触屏版_丰发"),
    channelId503("503", "官网会员系统_丰发"),
    channelId469("469", "速运官网PC端_丰发"),
    channelId464("464", "支付宝生活号_丰发"),
    channelId873("873", "H5自助链接_丰发"),
    channelId719("719", "短信服务厅_丰发"),
    channelId861("861", "速运APP_丰发"),
    channelId9999("9999", "未知"),
    channelId4test("qatest", "qatest");

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
