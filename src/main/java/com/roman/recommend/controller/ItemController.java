package com.roman.recommend.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.roman.recommend.entity.Item;
import com.roman.recommend.model.ItemList;
import com.roman.recommend.model.Response;
import com.roman.recommend.model.utils.ErrorCode;
import com.roman.recommend.service.ItemService;

/**
 * 活动rest接口
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;

	/**
	 * 单个活动上传接口
	 * 
	 * @param request
	 * @param item
	 *            活动model
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> upload(HttpServletRequest request, @RequestBody Item item) {
		if (item == null || StringUtils.isAnyBlank(item.getItemId(), item.getTitle())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return itemService.upload(item);
	}

	/**
	 * 批量活动上传接口
	 * 
	 * @param request
	 * @param itemList
	 *            活动model list
	 * @return
	 */
	@RequestMapping(value = "/batchupload", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> batchUpload(HttpServletRequest request, @RequestBody ItemList itemList) {
		if (itemList == null || CollectionUtils.isEmpty(itemList.getItems())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return itemService.batchUpload(itemList.getItems());
	}

	/**
	 * 单个活动更新接口
	 * 
	 * @param request
	 * @param item
	 *            活动model
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> update(HttpServletRequest request, @RequestBody Item item) {
		if (item == null || StringUtils.isBlank(item.getItemId())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return itemService.update(item);
	}

	/**
	 * 批量活动更新接口
	 * 
	 * @param request
	 * @param itemList
	 *            活动model list
	 * @return
	 */
	@RequestMapping(value = "/batchupdate", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> batchUpdate(HttpServletRequest request, @RequestBody ItemList itemList) {
		if (itemList == null || CollectionUtils.isEmpty(itemList.getIds())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return itemService.batchUpdate(itemList.getIds(), itemList.getField());
	}

	/**
	 * 单个活动删除接口
	 * 
	 * @param request
	 * @param itemId
	 *            活动id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public @ResponseBody Response<Boolean> delete(HttpServletRequest request,
			@RequestParam(value = "itemId", required = true) String itemId) {
		if (StringUtils.isBlank(itemId)) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return itemService.delete(itemId);
	}

	/**
	 * 批量活动删除接口
	 * 
	 * @param request
	 * @param itemList
	 *            活动id list
	 * @return
	 */
	@RequestMapping(value = "/batchdelete", method = RequestMethod.POST)
	public @ResponseBody Response<Boolean> batchDelete(HttpServletRequest request, @RequestBody ItemList itemList) {
		if (itemList == null || CollectionUtils.isEmpty(itemList.getIds())) {
			return new Response<>(ErrorCode.PARAM_ERROR);
		}
		return itemService.batchDelete(itemList.getIds());
	}

	/**
	 * 全部活动删除接口
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/clearall", method = RequestMethod.GET)
	public @ResponseBody Response<Boolean> clearAll(HttpServletRequest request) {
		return itemService.clearAll();
	}

}
