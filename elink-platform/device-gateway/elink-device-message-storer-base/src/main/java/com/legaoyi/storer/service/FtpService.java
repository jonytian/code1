package com.legaoyi.storer.service;

import org.apache.commons.net.ftp.FTPClient;

public interface FtpService {

    public FTPClient getFtpClient() throws Exception;
}
