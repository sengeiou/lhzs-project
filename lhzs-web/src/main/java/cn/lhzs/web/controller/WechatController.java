package cn.lhzs.web.controller;

import cn.lhzs.common.constant.Constants;
import cn.lhzs.common.result.ResponseResult;
import cn.lhzs.common.util.StringUtil;
import cn.lhzs.common.util.WebUtil;
import cn.lhzs.common.util.WechatUtil;
import cn.lhzs.common.util.XMLUtil;
import cn.lhzs.common.vo.WechatAccount;
import cn.lhzs.common.vo.WechatAuthorize;
import cn.lhzs.common.vo.WechatConfig;
import cn.lhzs.common.vo.WechatToken;
import cn.lhzs.data.bean.WechatUser;
import cn.lhzs.service.intf.WechatService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static cn.lhzs.common.result.ResponseResultGenerator.generatorSuccessResult;

/**
 * Created by ZHX on 2018/2/8.
 */
@Controller
@RequestMapping("/app/weixin")
public class WechatController {

    @Autowired
    WechatService wechatService;

    @RequestMapping(value = "/checkSignature")
    public void checkSignature(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> paramFromRequest = WebUtil.getAllRequestParam(request, Constants.UTF8);
        try {
            Map<String, String> paramFromInputStream = XMLUtil.getMapFromInputStreamXML(request.getInputStream(), Constants.UTF8);
            String xmlMsg = wechatService.reply(paramFromRequest, paramFromInputStream);
            if (StringUtil.isNotEmpty(xmlMsg)) {
                response.getWriter().write(xmlMsg);
            } else {
                response.getWriter().write(" ");
            }
        } catch (Exception ex) {
            if (WechatUtil.checkSign(paramFromRequest)) {
                String echostr = request.getParameter("echostr");
                if (StringUtil.isNotEmpty(echostr)) {
                    response.getWriter().print(echostr);
                } else {
                    response.getWriter().print("");
                }
            }
        }
    }

    @RequestMapping(value = "/author/url")
    @ResponseBody
    public ResponseResult authorUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        WechatAccount account = wechatService.getAccount();
        String authorizeUrl = WechatUtil.generatorAuthorizeUrl(new WechatAuthorize() {{
            setAppId(account.getAppId());
            setRedirectUri("http://zhx.tunnel.qydev.com/app/weixin/author");
        }});
        WechatAuthorize wechatAuthorize = new WechatAuthorize();
        wechatAuthorize.setRedirectUri(authorizeUrl);
        return generatorSuccessResult(wechatAuthorize);
    }

    @RequestMapping(value = "/author")
    @ResponseBody
    public ResponseResult author(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String code = request.getParameter("code");
        if(StringUtil.isNotEmpty(code)){
            String authorizeUrl = wechatService.accessToken(code);
            WechatToken wechatToken = JSONObject.parseObject(authorizeUrl, WechatToken.class);

            String userInfo = wechatService.getWechatUserInfo(wechatToken);
            WechatUser wechatUser = JSONObject.parseObject(userInfo, WechatUser.class);
            wechatService.addWechatUser(wechatUser);
        }
        return generatorSuccessResult();
    }

    @RequestMapping(value = "/config")
    @ResponseBody
    public ResponseResult config(@RequestBody WechatConfig wechatConfig) throws Exception {
        return generatorSuccessResult(wechatService.getConfig(wechatConfig.getUrl()));
    }
}
