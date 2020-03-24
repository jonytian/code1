package com.tyj.venus.dao;


import com.tyj.venus.entity.User;
import com.tyj.venus.entity.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInfoRepository extends BaseRepository<UserInfo,Integer> {

    @Query(value = "FROM UserInfo u where u.username=:username and u.password=:password")
    UserInfo login(@Param("username") String username, @Param("password") String password);
}
