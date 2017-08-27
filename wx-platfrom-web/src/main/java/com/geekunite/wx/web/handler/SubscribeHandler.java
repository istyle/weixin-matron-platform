package com.geekunite.wx.web.handler;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.geekunite.wx.web.builder.TextBuilder;
import com.iwamama.member.facade.IWxUserServiceFacade;
import com.iwamama.member.pojo.WxUserPOJO;
import com.iwamama.platform.common.ApiResult;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

@Component
public class SubscribeHandler extends AbstractHandler {

	@Autowired
	private IWxUserServiceFacade wxUserServiceFacade;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService, WxSessionManager sessionManager) throws WxErrorException {

		String openid = wxMessage.getFromUser();
		this.logger.info("新关注用户 OPENID: {}", openid);

		// 获取微信用户基本信息
		// WxMpUser userWxInfo =
		// weixinService.getUserService().userInfo(wxMessage.getFromUser(),
		// null);
		try {
			ApiResult<WxUserPOJO> apiResult = wxUserServiceFacade.getWxUserByOpenid(openid);
			if (apiResult.getResult() == null) {
				WxUserPOJO wxUserPOJO = new WxUserPOJO();
				Date now = new Date();
				wxUserPOJO.setOpenid(openid);
				wxUserPOJO.setCreateDate(now);
				wxUserPOJO.setUpdateDate(now);
				wxUserServiceFacade.insert(wxUserPOJO);
			}
			WxMpXmlOutMessage responseResult = handleSpecial(wxMessage);
			if (responseResult != null) {
				return responseResult;
			}

			return new TextBuilder().build("感谢关注", wxMessage, weixinService);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
	 */
	private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage) throws Exception {
		// TODO
		return null;
	}
}
