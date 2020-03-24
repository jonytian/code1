package com.legaoyi.storer.tjsatl.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.legaoyi.common.util.Constants;
import com.legaoyi.storer.tjsatl.dao.FileServerDao;
import com.legaoyi.storer.tjsatl.service.FileServerService;

@Service("fileServerService")
public class FileServerServiceImpl implements FileServerService {

    @Autowired
    @Qualifier("fileServerDao")
    private FileServerDao fileServerDao;

    @Cacheable(value = Constants.CACHE_NAME_DICTIONARY_CACHE, key = "'tjsatl_file_server'", unless = "#result == null")
    public List<?> getFileServer() throws Exception {
        return fileServerDao.getFileServer();
    }

}
