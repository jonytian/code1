package com.tyj.venus.service.impl;


import com.tyj.venus.entity.News;
import com.tyj.venus.service.NewsService;
import org.springframework.stereotype.Service;

@Service(value = "newsService")
public class NewsServiceImpl extends BaseServiceImpl <News, Integer> implements NewsService {
}
