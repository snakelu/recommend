package com.roman.recommend.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.roman.recommend.entity.Item;
import com.roman.recommend.entity.SimpleItem;
import com.roman.recommend.mapper.ItemMapper;
import com.roman.recommend.mapper.SimpleItemMapper;
import com.roman.recommend.model.Response;

/**
 * 活动service
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
@Service
public class ItemService {

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private SimpleItemMapper simpleItemMapper;

	@Transactional
	public Response<Boolean> batchUpload(List<Item> items) {
		itemMapper.batchInsert(items);
		List<SimpleItem> simpleItems = new ArrayList<SimpleItem>();
		for (Item item : items) {
			if (StringUtils.isNotBlank(item.getItemTags())) {
				String[] itemTags = item.getItemTags().split(",");
				for (String itemTag : itemTags) {
					SimpleItem simpleItem = new SimpleItem();
					simpleItem.setItemId(item.getItemId());
					simpleItem.setItemModifyTime(item.getItemModifyTime());
					simpleItem.setItemTag(itemTag);
					simpleItems.add(simpleItem);
				}
			}
		}
		simpleItemMapper.batchInsert(simpleItems);
		return new Response<Boolean>(true);
	}

	public Response<Boolean> batchUpdate(List<String> ids, String field) {
		itemMapper.batchUpdate(ids, field);
		return new Response<Boolean>(true);
	}

	@Transactional
	public Response<Boolean> batchDelete(List<String> ids) {
		itemMapper.batchDelete(ids);
		simpleItemMapper.batchDelete(ids);
		return new Response<Boolean>(true);
	}

	@Transactional
	public Response<Boolean> clearAll() {
		itemMapper.deleteAll();
		simpleItemMapper.deleteAll();
		return new Response<Boolean>(true);
	}

	@Transactional
	public Response<Boolean> upload(Item item) {
		Integer exsit = itemMapper.isExsit(item.getItemId());
		if (exsit != null && exsit > 0) {
			update(item);
		}
		itemMapper.insert(item);
		if (StringUtils.isNotBlank(item.getItemTags())) {
			String[] itemTags = item.getItemTags().split(",");
			List<SimpleItem> simpleItems = new ArrayList<SimpleItem>();
			for (String itemTag : itemTags) {
				SimpleItem simpleItem = new SimpleItem();
				simpleItem.setItemId(item.getItemId());
				simpleItem.setItemModifyTime(item.getItemModifyTime());
				simpleItem.setItemTag(itemTag);
				simpleItems.add(simpleItem);
			}
			simpleItemMapper.batchInsert(simpleItems);
		}
		return new Response<Boolean>(true);
	}

	@Transactional
	public Response<Boolean> update(Item item) {
		itemMapper.update(item);
		if (StringUtils.isNotBlank(item.getItemTags())) {
			String[] itemTags = item.getItemTags().split(",");
			List<SimpleItem> simpleItems = new ArrayList<SimpleItem>();
			for (String itemTag : itemTags) {
				SimpleItem simpleItem = new SimpleItem();
				simpleItem.setItemId(item.getItemId());
				simpleItem.setItemModifyTime(item.getItemModifyTime());
				simpleItem.setItemTag(itemTag);
				simpleItems.add(simpleItem);
			}
			simpleItemMapper.delete(item.getItemId());
			simpleItemMapper.batchInsert(simpleItems);
		}
		return new Response<Boolean>(true);
	}

	@Transactional
	public Response<Boolean> delete(String id) {
		itemMapper.delete(id);
		simpleItemMapper.delete(id);
		return new Response<Boolean>(true);
	}
}
