package com.example.auth.Entity;


import java.util.List;


public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<EsProduct> product;




    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;

    private List<Integer> pageNavs;

    /**
     * 当前查询到的结果，所有涉及到的品牌
     */
    private List<BrandVo> brands;

    /**
     * 当前查询到的结果，所有涉及到的所有属性
     */
    private List<AttrVo> attrs;

    /**
     * 当前查询到的结果，所有涉及到的所有分类
     */
    private List<CatalogVo> catalogs;


    //===========================以上是返回给页面的所有信息============================//


    /* 面包屑导航数据 */
    private List<NavVo> navs;

    public List<EsProduct> getProduct() {
        return product;
    }

    public void setProduct(List<EsProduct> product) {
        this.product = product;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Integer> getPageNavs() {
        return pageNavs;
    }

    public void setPageNavs(List<Integer> pageNavs) {
        this.pageNavs = pageNavs;
    }

    public List<BrandVo> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandVo> brands) {
        this.brands = brands;
    }

    public List<AttrVo> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<AttrVo> attrs) {
        this.attrs = attrs;
    }

    public List<CatalogVo> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<CatalogVo> catalogs) {
        this.catalogs = catalogs;
    }

    public List<NavVo> getNavs() {
        return navs;
    }

    public void setNavs(List<NavVo> navs) {
        this.navs = navs;
    }

    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;


    }



    public static class BrandVo {

        private Long brandId;

        private String brandName;

        private String brandImg;

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }
    }



    public static class AttrVo {

        private Long attrId;

        private String attrName;

        private List<String> attrValue;

        public Long getAttrId() {
            return attrId;
        }

        public void setAttrId(Long attrId) {
            this.attrId = attrId;
        }

        public String getAttrName() {
            return attrName;
        }

        public void setAttrName(String attrName) {
            this.attrName = attrName;
        }

        public List<String> getAttrValue() {
            return attrValue;
        }

        public void setAttrValue(List<String> attrValue) {
            this.attrValue = attrValue;
        }
    }



    public static class CatalogVo {

        private Long catalogId;

        private String catalogName;
    }
}
