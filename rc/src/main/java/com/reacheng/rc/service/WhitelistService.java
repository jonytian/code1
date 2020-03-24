package com.reacheng.rc.service;


        import com.reacheng.rc.entity.Whitelist;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;

        import java.util.List;

public interface WhitelistService extends BaseService<Whitelist,Integer> {

    List<Whitelist> findByProjectIdAndAppId (Integer projectId ,Integer appId);

    Whitelist findByImeiAndAppId (String imei,Integer appId);

    List<Whitelist> findByImei(String imei);

    List<Whitelist> findByImeiAndPackageName(String imei,String packageName);



    //检测白名单应用
    List<Whitelist> findByImeiAndProjectId(String imei,Integer projectId);

    List<Whitelist> findByImeiAndPackageNameAndProjectId(String imei,String packageName,Integer projectId);


    Page<Whitelist> findWhiteByProId (Integer projectId ,Integer appId, Pageable pageable);

    List<Whitelist> findByProjectIdAndStatus (Integer projectId ,String status,Integer appId);
}
