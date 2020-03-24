package com.reacheng.rc.dao;

import com.reacheng.rc.entity.Gadmin;
import com.reacheng.rc.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GadminRepository extends BaseRepository<Gadmin,Integer> {

    @Query(value = "FROM Gadmin a where a.gname =:gname")
    Gadmin findByName(@Param("gname") String gname);
}
