package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roman.recommend.entity.ItemScore;

public interface ItemScoreMapper {

	public List<ItemScore> selectByImei(String imei);

	public Double sumScoreByImei(String imei);

	public List<ItemScore> selectByUserId(String userId);

	public Double sumScoreByUserId(String userId);

	public List<ItemScore> selectByItemId(String itemId);

	public List<ItemScore> selectInItemIds(@Param("itemIds") List<String> itemIds);

	public List<ItemScore> selectNotInItemIds(@Param("imei") String imei, @Param("itemIds") List<String> itemIds);

	public ItemScore select(@Param("imei") String imei, @Param("itemId") String itemId);

	public void insert(ItemScore itemScore);

	public void update(ItemScore itemScore);

	public void batchInsert(@Param("itemScores") List<ItemScore> itemScore);

	public List<String> selectTop3TagByImei(String imei);

}
