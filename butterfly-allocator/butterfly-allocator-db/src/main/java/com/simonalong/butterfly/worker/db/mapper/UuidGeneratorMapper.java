package com.simonalong.butterfly.worker.db.mapper;

import com.simonalong.butterfly.worker.db.entity.UuidGeneratorDO;
import com.simonalong.neo.util.LocalDateTimeUtil;
import org.apache.ibatis.annotations.*;

import java.util.Date;

import static com.simonalong.butterfly.worker.db.DbConstant.UUID_TABLE;

/**
 * @author shizi
 * @since 2022-06-11 17:36:43
 */
public interface UuidGeneratorMapper {

    @Delete("delete from " + UUID_TABLE + " where `id` = #{id}")
    void delete(@Param("id") Long id);

    @Delete("update #{table} set name= #{name},last_expire_time =#{uuidDO.lastExpireTime} where id = #{uuidDO.id}")
    void update(@Param("tableName") String tableName, @Param("uuidDO") UuidGeneratorDO uuidGeneratorDO);

    @Select("select min(`id`) from " + UUID_TABLE + " where `namespace` =#{namespace} and `last_expire_time` < #{lastExpireTime}")
    Long selectMinId(@Param("namespace") String namespace, @Param("lastExpireTime") Date lastExpireTime);
}
