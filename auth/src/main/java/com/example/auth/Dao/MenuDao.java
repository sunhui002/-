package com.example.auth.Dao;

import com.example.auth.Entity.Menu;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MenuDao {

    @Results( {
            @Result(property = "Menuguid",column = "Menuguid"),
            @Result(property = "Icon",column = "Icon"),
            @Result(property = "IconActive",column = "IconActive"),
            @Result(property = "Caption",column = "Caption"),
            @Result(property = "Funcid",column = "Funcid"),
            @Result(property = "IconBlue",column = "IconBlue"),
            @Result(property = "MaxVisible",column = "MaxVisible"),
            @Result(property = "menu_id",column = "menu_id"),
            @Result(property = "MinVisible",column = "MinVisible"),
            @Result(property = "parentguid",column = "parentguid"),
            @Result(property = "type",column = "type"),
            @Result(property = "RunUrl",column = "RunUrl"),
    })
    @Select({"<script> ",
            "select Menuguid,Icon, IconActive, Caption,  Funcid, " ,
            " IconBlue ,MaxVisible, menu_id, MinVisible, parentguid," ,
            "type,RunUrl from  menu",
            "<where>",
            "menuguid in",
            "<foreach collection='menuguids' index='index' item='menuguid' open='(' separator=',' close=')'> ",
            "#{menuguid}",
            "</foreach>",
            "</where>",
            "</script>"
            })
    List<Menu> selectbymenuguid(@Param("menuguids")List<String> menuguids) ;
}
