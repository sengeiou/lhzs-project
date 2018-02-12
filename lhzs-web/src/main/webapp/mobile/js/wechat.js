/**
 * Created by ZHX on 2017/12/21.
 */
wechat = {
    share: function () {
        var menuShareTimeline = new Object();
        var menuShareAppMessage = new Object();
        //TODO 获取分享数据
        var menuItems = ['menuItem:share:appMessage', 'menuItem:share:timeline'];
        wechat.showMenuItems(menuItems);

        wechat.shareFriendOrCircle(menuShareAppMessage, menuShareTimeline);
    },
    showMenuItems: function (menuList) {
        wx.ready(function () {
            wx.showMenuItems({
                menuList: menuList
            });
        });
    },
    shareFriendOrCircle: function (menuShareAppMessage, menuShareTimeline) {
        wx.ready(function () {
            wx.checkJsApi({
                jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline'],
                success: function (res) {

                }
            });
            wx.onMenuShareAppMessage({
                title: "1111",
                link: "http://www.baidu.com",
                desc: "分享测试",
                imgUrl: "http://www.baidu.com"
            });

            wx.onMenuShareTimeline({
                title: "1111",
                link: "http://www.baidu.com",
                desc: "分享测试",
                imgUrl: "http://www.baidu.com"
            });
        });
    },
    isWechat: function () {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            return true;
        } else {
            return false;
        }
    },
    getConfig: function () {
        var params = {
            "url":{url:window.location.href}
        };
        param = JSON.stringify(params);
        $.ajax({
            type : "POST",
            url : "/app/weixin/config",
            dataType : "json",
            contentType : "application/json",
            data : param,
            async : false,
            success : function(result){
                var data = result.data;
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: data.appId,
                    timestamp: data.timestamp,
                    nonceStr: data.nonceStr,
                    signature: data.signature,
                    jsApiList: [
                        'checkJsApi',
                        'onMenuShareTimeline',
                        'onMenuShareAppMessage',
                        'hideMenuItems',
                        'showMenuItems',
                        'hideAllNonBaseMenuItem',
                        'showAllNonBaseMenuItem',
                        'getNetworkType',
                        'getLocation',
                        'hideOptionMenu',
                        'showOptionMenu',
                        'closeWindow',
                        'scanQRCode',
                        'chooseWXPay'
                    ]
                });
            }
        });
    }
}