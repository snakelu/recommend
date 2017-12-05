package com.roman.recommend.model;

import java.io.Serializable;
import java.util.List;

import com.roman.recommend.entity.Item;

/**
 * {@link}Item list
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
public class ItemList implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Item> items;

	private List<String> ids;

	private String field;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
