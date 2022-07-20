package com.example.auth.Dao;

import com.example.auth.Entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProductDao {
    @Results( {
            @Result(property = "spuId",column = "spu_id"),
            @Result(property = "spuImg",column = "spu_img"),
            @Result(property = "spuPrice",column = "spu_price"),
            @Result(property = "spuTitle",column = "spu_name"),
            @Result(property = "decription",column = "spu_description"),
    })
    @Insert("insert into spu ( spu_name,spu_price,spu_img,spu_description) values(#{spu.spuTitle},#{spu.spuPrice},#{spu.spuImg},#{spu.decription}) ")
    @Options(useGeneratedKeys=true, keyProperty="spu.spuId")
    public int insertuseGeneratedKeys(@Param("spu") Spu spu);



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

    @Results( {
            @Result(property = "sputitle",column = "spu_name"),
            @Result(property = "spuprice",column = "spu_price"),
            @Result(property = "spuid",column = "spu_id"),
            @Result(property = "spuimg",column = "spu_img"),
            @Result(property = "decription",column = "spu_description"),
    })
    @Select("select spu_id, spu_name,spu_price,spu_img,spu_description from spu where spu_id=#{spuid}")
    public Item findItembyspuid(@Param("spuid")String spuid);


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


    @Results( {
            @Result(property = "attrname",column = "attr_name"),
            @Result(property = "attrvalue",column = "attr_value"),
            @Result(property = "attrcode",column = "attr_code"),
            @Result(property = "skuid",column = "sku_id")
    })
    @Select("select sku.sku_id, sku.attr_name,attr.attr_value,attr.attr_code from sku\n" +
            "INNER JOIN attr on attr.sku_id=sku.sku_id\n" +
            "where sku.spu_id=#{spuid}")
    public List<StockAttr> findAllstockattr(@Param("spuid") String spuid);


    @Results( {
            @Result(property = "attrname",column = "attr_name"),
            @Result(property = "attrvalue",column = "attr_value"),
            @Result(property = "attrcode",column = "attr_code"),
            @Result(property = "skuid",column = "sku_id")
    })
    @Select("select sku.sku_id, sku.attr_name,attr.attr_value,attr.attr_code from sku\n" +
            "left JOIN attr on attr.sku_id=sku.sku_id\n" +
            "where sku.spu_id=#{spuid}")
    public List<StockAttr> findAllattr(@Param("spuid") String spuid);


    @Results( {
            @Result(property = "attrname",column = "attr_name"),
            @Result(property = "skuid",column = "sku_id"),
    })
    @Select("select attr_name,sku_id from sku where spu_id=#{spuid}")
    public List<SaleAttrVo> findAllskubyspuid(@Param("spuid") String spuid);

    @Results( {
            @Result(property = "attrname",column = "attr_name"),
            @Result(property = "spuid",column = "spu_id"),
    })
    @Insert("insert into sku (attr_name,spu_id) values (#{sku.attrname},#{sku.spuid})")
    @Options(useGeneratedKeys=true, keyProperty="sku.skuid")
    void uploadsku(@Param("sku") Sku sku);

    @Results( {
            @Result(property = "attrid",column = "attr_id"),
            @Result(property = "attrvalue",column = "attr_value"),
            @Result(property = "skuid",column = "sku_id"),
            @Result(property = "attrcode",column = "attr_code"),
            @Result(property = "isstock",column = "isstock")
    })
    @Select("select attr_id, attr_value,sku_id,attr_code,'true' isstock from attr where sku_id=#{skuid}")
    public List<ItemInAttrVo> findAllattrbyskuid(@Param("skuid") String skuid);

    @Results( {
            @Result(property = "attrid",column = "attr_id"),
            @Result(property = "attrvalue",column = "attr_value"),
            @Result(property = "skuid",column = "sku_id"),
            @Result(property = "attrcode",column = "attr_code"),
            @Result(property = "isstock",column = "isstock")
    })
    @Select("select attr_id, attr_value,sku_id,attr_code,'false' isstock from attr where sku_id=#{skuid}")
    public List<ItemInAttrVo> findAllattrbyskuidfalse(@Param("skuid") String skuid);

    @Results( {
            @Result(property = "attrvalue",column = "attr_value"),
            @Result(property = "skuid",column = "sku_id"),
            @Result(property = "attrcode",column = "attr_code")
    })
    @Insert("insert into attr (attr_value,sku_id,attr_code ) values (#{attr.attrvalue},#{attr.skuid},#{attr.attrcode})")
    void uploadattr(@Param("attr") Attr attr);

    //    @Results( {
//            @Result(property = "stockids",column = "stock_ids"),
//    })
    @Select("select stock_ids stockids from attr where attr_id=#{attrid}")
    public String findstockidsbyattrid(@Param("attrid") String attrid);

    @Select("select attr_list attrlist from stock where stock_id=#{stockid} and stock_num>0")
    public String findattrlistbystockid(@Param("stockid") String stockid);

    @Select("select sku_id  from attr where attr_id=#{attrid}")
    Integer findskuidbyattrid(String attrid);


    @Results( {
            @Result(property = "stockid",column = "stock_id"),
            @Result(property = "attrlist",column = "attr_List"),
            @Result(property = "spuid",column = "spu_id"),
            @Result(property = "stocknum",column = "stock_num"),
            @Result(property = "skuprice",column = "sku_price")
    })
    @Select("select stock_id, attr_List,spu_id,stock_num,sku_price from stock where spu_id=#{spuid} and stock_num>0")
    List<Stock> findstockbyspuid(String spuid);

    @Delete(
            " delete from attr where attr.sku_id in (select sku_id from sku where sku.attr_name=#{attrname} and sku.spu_id=#{spuId} );\n"
            )
    void deleteattrbyattrname(@Param("attrname") String attrname, @Param("spuId") String spuId);

@Delete(   "delete from sku where spu_id=#{spuId} and attr_name=#{attrname};")
    void deleteskubyattrname(@Param("attrname") String attrname, @Param("spuId") String spuId);

    void insersku(Sku sku);

    @Delete("delete FROM attr where attr.sku_id in (select sku.sku_id from sku where sku.attr_name=#{attrname} " +
            "and sku.spu_id=#{spuId} ) and attr.attr_value=#{attrvalue}"
    )
    void deleteattrvaluebyattrvalue(@Param("attrvalue") String attrvalue, @Param("spuId")String spuId, @Param("attrname") String attrname);

    @Results( {
            @Result(property = "spuId",column = "spu_id"),
            @Result(property = "spuImg",column = "spu_img"),
            @Result(property = "spuPrice",column = "spu_price"),
            @Result(property = "spuTitle",column = "spu_name"),
            @Result(property = "decription",column = "spu_description"),
    })
    @Update("update  spu set spu_name=#{spu.spuTitle},spu_price=#{spu.spuPrice},spu_img=#{spu.spuImg},spu_description=#{spu.decription} where spu_id=#{spu.spuId} ")
    void updatespu(@Param("spu") Spu spu);
}
