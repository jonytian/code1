package com.example.sys.mapper;

import com.example.sys.entity.Product;
import com.example.sys.entity.ProductSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {

    int deleteByPrimaryKey(Integer id);

    int insertProducts(Product product);
    /**
     * 分页查询用户数据
     * @return
     */
    List<Product> getProducts(@Param("productSearch") ProductSearch productSearch);

    List<Product> findByIMEI(@Param("imei") String imei);

    Product getOne(@Param("productSearch") ProductSearch productSearch);
}
