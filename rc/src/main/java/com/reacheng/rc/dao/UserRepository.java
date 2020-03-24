package com.reacheng.rc.dao;

import com.reacheng.rc.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends BaseRepository<User,Integer> {

    @Query(value = "FROM User u where u.userName =:userName and u.password=:password")
    User adminLogin(@Param("userName") String userName, @Param("password") String password);

    @Query(value = "FROM User u where u.userName =:userName ")
    User findByName(@Param("userName") String userName);

    @Query(value = "FROM User u where u.email =:email ")
    User findByEmail(@Param("email") String email);
}
