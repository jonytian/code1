package com.tyj.venus.dao;


import com.tyj.venus.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends BaseRepository<User,Integer> {

    @Query(value = "FROM User u where u.userName=:userName and u.password=:password")
    User adminLogin(@Param("userName") String userName, @Param("password") String password);
}
