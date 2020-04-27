package com.test.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LockMapper {

    @Update("insert ignore into t_locks(lkey, lvalue, expire_at) values "
            + "(#{key}, #{value}, now() + interval #{expireTime} second) on duplicate key update "
            + " lvalue=if(expire_at < now(), values(lvalue), lvalue),"
            + " expire_at=if(lvalue=values(lvalue), values(expire_at), if(expire_at<now(),values(expire_at),expire_at))")
    @ResultType(Integer.class)
    int lock(@Param("key") String key, @Param("value") String value, @Param("expireTime") int seconds);

    @Delete("delete from t_locks where lkey=#{key} and lvalue=#{value}")
    int unlock(@Param("key") String key, @Param("value") String value);
}
