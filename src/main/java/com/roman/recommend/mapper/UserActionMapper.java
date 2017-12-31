package com.roman.recommend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.roman.recommend.entity.ItemScore;
import com.roman.recommend.entity.UserAction;

public interface UserActionMapper {

	public void insert(UserAction userAction);

	public void deleteAll();

	public void batchInsert(@Param("userActions") List<UserAction> userActions);

	public List<ItemScore> topItemScore(Integer size);

}
