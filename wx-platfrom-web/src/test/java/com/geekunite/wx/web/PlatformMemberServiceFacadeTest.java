package com.geekunite.wx.web;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.iwamama.member.facade.IWxUserServiceFacade;
import com.iwamama.member.pojo.WxUserPOJO;
import com.iwamama.platform.common.ApiResult;

/**
 * 
 * @author zhangjie
 * 
 * @date 2017年8月28日 下午3:08:01
 */
public class PlatformMemberServiceFacadeTest extends BaseTest {

	@Autowired
	private IWxUserServiceFacade wxUserServiceFacade;

	/**
	 * 
	 * @author zhangjie
	 *
	 * @date 2017年8月28日 下午3:27:40
	 */
	@Test
	public void insertTest() {
		WxUserPOJO wxUserPOJO = new WxUserPOJO();
		Date now = new Date();
		wxUserPOJO.setNickname("nickname");
		wxUserPOJO.setOpenid("openid");
		wxUserPOJO.setCity("city");
		wxUserPOJO.setCountry("country");
		wxUserPOJO.setGroupid(1);
		wxUserPOJO.setHeadimgurl("headimgurl");
		wxUserPOJO.setLanguage("language");
		wxUserPOJO.setProvince("province");
		wxUserPOJO.setRemark("remark");
		wxUserPOJO.setSex(1);
		wxUserPOJO.setSubscribe("1");
		wxUserPOJO.setSubscribeTime("subscribeTime");
		wxUserPOJO.setTagidList("tagidList");
		wxUserPOJO.setUnionid("unionid");
		wxUserPOJO.setCreateDate(now);
		wxUserPOJO.setUpdateDate(now);
		ApiResult<WxUserPOJO> apiResult = wxUserServiceFacade.insert(wxUserPOJO);
		System.out.println(JSONObject.toJSONString(apiResult));
	}
}
