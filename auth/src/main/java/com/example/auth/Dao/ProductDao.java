package com.example.auth.Dao;

import com.example.auth.Entity.EsAttr;
import com.example.auth.Entity.EsProduct;
import com.example.auth.Entity.Product;
import com.example.auth.Entity.Spu;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProductDao {
    @Results( {
            @Result(property = "spuImg",column = "spu_img"),
            @Result(property = "spuId",column = "spu_id"),
            @Result(property = "spuPrice",column = "spu_price"),
            @Result(property = "spuTitle",column = "spu_name"),
            @Result(property = "decription",column = "spu_description"),
    })
    @Select("select spu_id, spu_name,spu_price,spu_img,spu_description from spu where spu_description like '%${decription}%' ")
    public List<Product> research(@Param("decription") String decription);


    @Results( {
            @Result(property = "spuImg",column = "spu_img"),
            @Result(property = "spuId",column = "spu_id"),
            @Result(property = "spuPrice",column = "spu_price"),
            @Result(property = "spuTitle",column = "spu_name"),
            @Result(property = "decription",column = "spu_description"),
            @Result(column = "spu_id",property = "attr",
                    //一对多关系映射many注解，这里查询用户的角色信息
                    many = @Many(select = "com.example.auth.Dao.ProductDao.findattr",fetchType = FetchType.LAZY))
    })
    @Select("select spu_id, spu_name,spu_price,spu_img,spu_description from spu ")
    public List<EsProduct> findEsProduct();


    @Results( {
            @Result(property = "attrname",column = "attr_name"),
            @Result(property = "attrvalue",column = "attr_value"),
            @Result(property = "skuid",column = "sku_id"),
    })
    @Select("select attr_name,attr_value,attr.sku_id from sku\n" +
            "INNER JOIN attr on attr.sku_id=sku.sku_id\n" +
            "where sku.spu_id=#{spuid}")
    public List<EsAttr> findattr(@Param("spuid") String spuid);

    @Results( {
            @Result(property = "spuImg",column = "spu_img"),
            @Result(property = "spuId",column = "spu_id"),
            @Result(property = "spuPrice",column = "spu_price"),
            @Result(property = "spuTitle",column = "spu_name"),
            @Result(property = "decription",column = "spu_description"),
    })
    @Select("select spu_id, spu_name,spu_price,spu_img,spu_description from spu")
    public List<Spu> findaAllSpu();

    @Results( {
            @Result(property = "spuImg",column = "spu_img"),
            @Result(property = "spuId",column = "spu_id"),
            @Result(property = "spuPrice",column = "spu_price"),
            @Result(property = "spuTitle",column = "spu_name"),
            @Result(property = "decription",column = "spu_description"),
    })
    @Select("select spu_id, spu_name,spu_price,spu_img,spu_description from spu where spu_id=#{spuid}")
    public List<Spu> findaSpubyspuid(@Param("spuid")String spuid);


    @Select("select spuid from spu")
   public List<String> findAllSpuid();

    @Results( {
            @Result(property = "attrname",column = "attr_name"),
            @Result(property = "attrvalue",column = "attr_value"),
    })
    @Select("select attr_name,attr_value from sku\n" +
            "INNER JOIN attr on attr.sku_id=sku.sku_id\n" +
            "where sku.spu_id=#{spuid}")
    public List<EsAttr> findAllsku(@Param("spuid") String spuid);
}
