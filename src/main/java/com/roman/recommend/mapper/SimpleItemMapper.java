package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roman.recommend.entity.SimpleItem;

public interface SimpleItemMapper {

	public void insert(SimpleItem simpleItem);

	public void batchInsert(@Param("simpleItems") List<SimpleItem> simpleItems);

	public void delete(String itemId);

	public void batchDelete(@Param("ids") List<String> ids);

	public void deleteAll();

	public List<String> select(@Param("itemTag") String itemTag, @Param("start") int start, @Param("size") int size);

	public List<String> selectByList(@Param("itemTags") List<String> itemTags);

	public List<String> selectRecentItem(int size);
}
