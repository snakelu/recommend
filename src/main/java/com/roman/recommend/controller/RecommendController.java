package com.roman.recommend.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.roman.recommend.entity.ItemScore;
import com.roman.recommend.model.Response;
import com.roman.recommend.model.utils.ErrorCode;
import com.roman.recommend.service.RecommendService;

/**
 * 推荐rest接口
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/4
 */
@RestController
@RequestMapping("/recommend")
public class RecommendController {

	@Autowired
	private RecommendService recommendService;

	/**
	 * 获取推荐接口
	 * 
	 * @param request
	 * @param imei
	 *            设备id
	 * @param userId
	 *            用户id
	 * @param size
	 *            获取推荐数量
	 * @return 推荐活动列表
	 * @throws Exception
	 */
	@RequestMapping(value = "/byitem", method = RequestMethod.GET)
	public @ResponseBody Response<List<ItemScore>> getRecommendByItem(HttpServletRequest request,
			@RequestParam(value = "imei", required = true) String imei,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "size", required = false, defaultValue = "5") int size,
			@RequestParam(value = "exclude", required = false) String exclude,
			@RequestParam(value = "isExclude", required = true) Integer isExclude) throws Exception {
		if (StringUtils.isBlank(imei)) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return recommendService.getRecommendByItem(imei, userId, size, exclude, isExclude);
	}

	/**
	 * 获取推荐接口
	 * 
	 * @param request
	 * @param imei
	 *            设备id
	 * @param userId
	 *            用户id
	 * @param size
	 *            获取推荐数量
	 * @return 推荐活动列表
	 * @throws Exception
	 */
	@RequestMapping(value = "/byuser", method = RequestMethod.GET)
	public @ResponseBody Response<List<ItemScore>> getRecommendByUser(HttpServletRequest request,
			@RequestParam(value = "imei", required = true) String imei,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "size", required = false, defaultValue = "5") int size,
			@RequestParam(value = "exclude", required = false) String exclude,
			@RequestParam(value = "isExclude", required = true) Integer isExclude) throws Exception {
		if (StringUtils.isBlank(imei)) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return recommendService.getRecommendByUser(imei, userId, size, exclude, isExclude);
	}

	/**
	 * 获取推荐接口1(备用)
	 * 
	 * @param request
	 * @param imei
	 *            设备id
	 * @param userId
	 *            用户id
	 * @param size
	 *            获取推荐数量
	 * @return 推荐活动列表
	 */
	@RequestMapping(value = "/byuser1", method = RequestMethod.GET)
	public @ResponseBody Response<List<ItemScore>> getRecommendByUser1(HttpServletRequest request,
			@RequestParam(value = "imei", required = true) String imei,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "size", required = false, defaultValue = "5") int size,
			@RequestParam(value = "exclude", required = false) String exclude) {
		if (StringUtils.isBlank(imei)) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return recommendService.getRecommendByUser1(imei, userId, size, exclude);
	}

}
