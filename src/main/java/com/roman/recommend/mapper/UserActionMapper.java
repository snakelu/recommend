package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roman.recommend.entity.ItemScore;
import com.roman.recommend.entity.UserAction;

public interface UserActionMapper {

	public List<UserAction> selectByImei(String imei);

	public void insert(UserAction userAction);

	public void deleteAll();

	public void batchInsert(@Param("userActions") List<UserAction> userActions);

	public List<ItemScore> topItemScore(@Param("from") int from, @Param("size") int size);
	
	public List<ItemScore> topItemScoreByCateid(@Param("cateid") String cateid, @Param("from") int from, @Param("size") int size);

}
