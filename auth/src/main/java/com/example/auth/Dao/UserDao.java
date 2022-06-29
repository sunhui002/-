package com.example.auth.Dao;

import com.example.auth.Entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    @Insert("insert into user (username,password,salt) value (#{user.username},#{user.password},#{user.salt})")
   public int saveuser(@Param("user") User user );

    @Results( {
            @Result(property = "password",column = "password"),
            @Result(property = "username",column = "username"),
            @Result(property = "salt",column = "salt"),
    })
    @Select("select username,password,salt from user where username=#{username}")
    public User getuserbyid(@Param("username") String username );

}
