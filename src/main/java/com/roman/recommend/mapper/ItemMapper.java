package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roman.recommend.entity.Item;

public interface ItemMapper {

	public void insert(Item item);

	public void update(Item item);

	public void delete(String itemId);

	public void batchInsert(@Param("items") List<Item> items);

	public void batchUpdate(@Param("ids") List<String> ids, @Param("field") String field);

	public void batchDelete(@Param("ids") List<String> ids);

	public void deleteAll();
}
