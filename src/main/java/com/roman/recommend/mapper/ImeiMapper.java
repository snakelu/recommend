package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ImeiMapper {

	public Long select(String imei);

	public List<String> selectImeis(@Param("imeiList") List<String> imeiList);

	public void insert(String imei);

	public void batchInsert(@Param("imeiList") List<String> imeiList);

}
