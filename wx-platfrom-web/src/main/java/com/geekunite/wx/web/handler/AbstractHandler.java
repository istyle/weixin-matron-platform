package com.geekunite.wx.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;

/**
 * 
 * @author zhangjie
 *
 */
public abstract class AbstractHandler implements WxMpMessageHandler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
}