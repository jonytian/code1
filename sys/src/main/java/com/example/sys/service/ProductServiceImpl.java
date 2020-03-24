package com.example.sys.service;

import com.example.sys.mapper.ProductMapper;
import com.example.sys.entity.Product;
import com.example.sys.entity.ProductSearch;
import com.example.sys.util.DateUtils;
import com.example.sys.util.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by tyj on 2018/08/15.
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;


	@Override
	public PageDataResult getProducts(ProductSearch productSearch, int page, int limit) {
		// 时间处理
		if (null != productSearch) {
			if (StringUtils.isNotEmpty(productSearch.getInsertTimeStart())
					&& StringUtils.isEmpty(productSearch.getInsertTimeEnd())) {
				productSearch.setInsertTimeEnd(DateUtils.format(new Date()));
			} else if (StringUtils.isEmpty(productSearch.getInsertTimeStart())
					&& StringUtils.isNotEmpty(productSearch.getInsertTimeEnd())) {
				productSearch.setInsertTimeStart(DateUtils.format(new Date()));
			}
			if (StringUtils.isNotEmpty(productSearch.getInsertTimeStart())
					&& StringUtils.isNotEmpty(productSearch.getInsertTimeEnd())) {
				if (productSearch.getInsertTimeEnd().compareTo(
						productSearch.getInsertTimeStart()) < 0) {
					String temp = productSearch.getInsertTimeStart();
					productSearch
							.setInsertTimeStart(productSearch.getInsertTimeEnd());
					productSearch.setInsertTimeEnd(temp);
				}
			}
		}
		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<Product> prList = productMapper.getProducts(productSearch);
		// 获取分页查询后的数据
		PageInfo<Product> pageInfo = new PageInfo<>(prList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());

		pdr.setList(prList);
		return pdr;
	}

	@Override
	public List<Product> getProductList(ProductSearch productSearch) {
		return productMapper.getProducts(productSearch);
	}


	@Override
    public int insertProducts(Product product) {
        return productMapper.insertProducts(product);
    }

	@Override
	public List<Product> getProductByImei(String imei) {
		return productMapper.findByIMEI(imei);
	}

    @Override
    public int deleteProduct(int productId) {
        return productMapper.deleteByPrimaryKey(productId);
    }


}
