package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roman.recommend.entity.ItemScore;

public interface ExcludeItemMapper {

	public List<String> selectByImei(String imei);

	public List<String> selectByUserId(String userId);

	public void batchInsert(@Param("imei") String imei, @Param("userId") String userId,
			@Param("recommendList") List<ItemScore> recommendList);

	public void deleteByImei(String imei);

}
