package com.example.auth.Dao;

import com.example.auth.Entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {
    @Insert("insert into user (user_name,user_password,user_salt) value (#{user.username},#{user.password},#{user.salt})")
   public int saveuser(@Param("user") User user );

}
