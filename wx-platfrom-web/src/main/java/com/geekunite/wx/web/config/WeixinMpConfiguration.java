package com.geekunite.wx.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.geekunite.wx.web.handler.AbstractHandler;
import com.geekunite.wx.web.handler.LocationHandler;
import com.geekunite.wx.web.handler.LogHandler;
import com.geekunite.wx.web.handler.MenuHandler;
import com.geekunite.wx.web.handler.MessageHandler;
import com.geekunite.wx.web.handler.NullHandler;
import com.geekunite.wx.web.handler.SessionHandler;
import com.geekunite.wx.web.handler.StoreCheckNotifyHandler;
import com.geekunite.wx.web.handler.SubscribeHandler;
import com.geekunite.wx.web.handler.UnsubscribeHandler;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;

@Configuration
@ConditionalOnClass(WxMpService.class)
@EnableConfigurationProperties(WechatMpProperties.class)
public class WeixinMpConfiguration {

	@Autowired
	protected LogHandler logHandler;
	@Autowired
	protected NullHandler nullHandler;
	@Autowired
	protected SessionHandler sessionHandler;
	@Autowired
	protected StoreCheckNotifyHandler storeCheckNotifyHandler;
	@Autowired
	private WechatMpProperties properties;
	@Autowired
	private LocationHandler locationHandler;
	@Autowired
	private MenuHandler menuHandler;
	@Autowired
	private MessageHandler messageHandler;
	@Autowired
	private UnsubscribeHandler unsubscribeHandler;
	@Autowired
	private SubscribeHandler subscribeHandler;

	@Bean
	@ConditionalOnMissingBean
	public WxMpConfigStorage configStorage() {
		WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
		configStorage.setAppId(this.properties.getAppId());
		configStorage.setSecret(this.properties.getSecret());
		configStorage.setToken(this.properties.getToken());
		configStorage.setAesKey(this.properties.getAesKey());
		return configStorage;
	}

	@Bean
	@ConditionalOnMissingBean
	public WxMpService wxMpService(WxMpConfigStorage configStorage) {
		// WxMpService wxMpService = new
		// me.chanjar.weixin.mp.api.impl.okhttp.WxMpServiceImpl();
		// WxMpService wxMpService = new
		// me.chanjar.weixin.mp.api.impl.jodd.WxMpServiceImpl();
		// WxMpService wxMpService = new
		// me.chanjar.weixin.mp.api.impl.apache.WxMpServiceImpl();
		WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(configStorage);
		return wxMpService;
	}

	@Bean
	public WxMpMessageRouter router(WxMpService wxMpService) {
		final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

		// 记录所有事件的日志 （异步执行）
		newRouter.rule().handler(this.logHandler).next();

		// 接收客服会话管理事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION).handler(this.sessionHandler).end();
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION).handler(this.sessionHandler).end();
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION).handler(this.sessionHandler).end();

		// 门店审核事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxMpEventConstants.POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

		// 自定义菜单事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).handler(this.getMenuHandler()).end();

		// 点击菜单连接事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_VIEW).handler(this.nullHandler).end();

		// 关注事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE).handler(this.getSubscribeHandler()).end();

		// 取消关注事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_UNSUBSCRIBE).handler(this.getUnsubscribeHandler()).end();

		// 上报地理位置事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_LOCATION).handler(this.getLocationHandler()).end();

		// 接收地理位置消息
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_LOCATION).handler(this.getLocationHandler()).end();

		// 扫码事件
		newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SCAN).handler(this.getScanHandler()).end();

		// 默认
		newRouter.rule().async(false).handler(this.getMessageHandler()).end();

		return newRouter;
	}

	protected MenuHandler getMenuHandler() {
		return this.menuHandler;
	}

	protected SubscribeHandler getSubscribeHandler() {
		return this.subscribeHandler;
	}

	protected UnsubscribeHandler getUnsubscribeHandler() {
		return this.unsubscribeHandler;
	}

	protected AbstractHandler getLocationHandler() {
		return this.locationHandler;
	}

	protected MessageHandler getMessageHandler() {
		return this.messageHandler;
	}

	protected AbstractHandler getScanHandler() {
		return null;
	}
}
