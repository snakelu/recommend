package com.roman.recommend.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.roman.recommend.entity.UserAction;
import com.roman.recommend.model.Response;
import com.roman.recommend.model.UserActionList;
import com.roman.recommend.model.utils.ErrorCode;
import com.roman.recommend.service.UserActionService;

/**
 * 用户行为rest接口
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
@RestController
@RequestMapping("/user/action")
public class UserActionController {

	@Autowired
	private UserActionService userActionService;

	/**
	 * 单个用户行为上传接口
	 * 
	 * @param request
	 * @param userAction
	 *            用户行为model
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> upload(HttpServletRequest request, @RequestBody UserAction userAction) {
		if (userAction == null || StringUtils.isBlank(userAction.getItemId())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return userActionService.upload(userAction);
	}

	/**
	 * 批量活动上传接口
	 * 
	 * @param request
	 * @param userActions
	 *            用户行为model list
	 * @return
	 */
	@RequestMapping(value = "/batchupload", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> batchUpload(HttpServletRequest request,
			@RequestBody UserActionList userActionList) {
		if (userActionList == null || CollectionUtils.isEmpty(userActionList.getUserActions())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return userActionService.batchUpload(userActionList.getUserActions());
	}

	/**
	 * 清空用户行为接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/clearall", method = RequestMethod.GET)
	public @ResponseBody Response<Boolean> clearAll(HttpServletRequest request) {
		return userActionService.clearAll();
	}

}
