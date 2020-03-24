package com.example.logs.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.example.logs.entity.Device;
import org.springframework.stereotype.Service;


@Service("tokenService")
public class TokenService {
    public String getToken(Device device) {
        String token = JWT.create().withAudience(device.getImei())// 将 device id 保存到 token 里面
                .sign(Algorithm.HMAC256(device.getImei()+device.getProjectName()));// 以 imei+projectName 作为 token 的密钥
        return token;
    }
}
