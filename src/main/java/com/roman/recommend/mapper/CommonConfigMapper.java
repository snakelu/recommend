package com.roman.recommend.mapper;

import org.apache.ibatis.annotations.Param;

public interface CommonConfigMapper {

	public void update(@Param("field") String field, @Param("value") String value);

	public String select(String field);

}
