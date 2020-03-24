package com.example.sys.service;


import com.example.sys.entity.Product;
import com.example.sys.entity.ProductSearch;
import com.example.sys.util.PageDataResult;

import java.util.List;

/**
 * Created by tyj on 2018/08/15.
 */
public interface ProductService {

	/**
	 * 分页查询用户列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageDataResult getProducts(ProductSearch productSearch, int page, int limit);

	List<Product> getProductList (ProductSearch productSearch);

    int insertProducts(Product product);

    List<Product> getProductByImei (String imei);

    int deleteProduct (int productId);
}
