package com.simonalong.buffterfly.sample.mybatis;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @author shizi
 * @since 2022-06-11 17:00:24
 */
public interface DemoMapper {

    @Select("SELECT * FROM neo_table1 WHERE id = #{id}")
    @Results({
        @Result( id = true ,property = "id", column = "id"),
        @Result(property="userName" ,column="user_name"),
    })
    NeoTable one(long id);
}
