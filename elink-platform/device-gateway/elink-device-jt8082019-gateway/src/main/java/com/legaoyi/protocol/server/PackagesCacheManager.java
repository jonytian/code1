package com.legaoyi.protocol.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.util.MessageBuilder;

@Component("packagesCacheManager")
public class PackagesCacheManager {

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    private Packages createPackageCache(String cacheKey, int totalPackages) {
        Packages packages = gatewayCacheManager.getPackageCache(cacheKey);
        boolean bool = false;
        if (packages == null) {
            bool = true;
        } else {
            if (packages.getPackageSize() != totalPackages) {
                bool = true;
            }
        }
        if (bool) {
            packages = new Packages(totalPackages);
            gatewayCacheManager.addPackageCache(cacheKey, packages);
        }
        return packages;
    }

    public Packages addPackageCache(String cacheKey, int totalPackages, int packageSeq, byte[] b) throws Exception {
        Packages packages = createPackageCache(cacheKey, totalPackages);
        packages.addPackage(packageSeq, b);
        gatewayCacheManager.addPackageCache(cacheKey, packages);
        return packages;
    }

    public void removePackageCache(String cacheKey) {
        gatewayCacheManager.removePackageCache(cacheKey);
    }
}


class Packages implements java.io.Serializable {

    private static final long serialVersionUID = 5596431848979336631L;

    private byte[][] packages;

    private int firstSeq = -1;

    private int total;

    private int maxSeq;

    private int currentSeq;

    public Packages(int totalPackages) {
        this.total = totalPackages;
        packages = new byte[totalPackages][];
    }

    public void addPackage(int packageSeq, byte[] b) {
        if (maxSeq < packageSeq) {
            maxSeq = packageSeq;
        }
        if (firstSeq == -1) {
            firstSeq = packageSeq;
        }
        currentSeq = packageSeq;
        packages[packageSeq - 1] = b;
    }

    public boolean isRetransmission() {
        return currentSeq < maxSeq;
    }

    public boolean isFull() {
        if (maxSeq < total) {
            return false;
        }
        for (int i = 0; i < total; i++) {
            if (null == packages[i]) {
                return false;
            }
        }
        return true;
    }

    public int getFirstSeq() {
        return this.firstSeq;
    }

    public int getPackageSize() {
        return this.total;
    }

    public List<Integer> getLostPackageSeqs() {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < maxSeq; i++) {
            if (null == packages[i]) {
                list.add(i + 1);
            }
        }
        return list.isEmpty() ? null : list;
    }

    public byte[] getAllPackages() throws Exception {
        MessageBuilder mb = new MessageBuilder();
        for (int i = 0; i < maxSeq; i++) {
            mb.append(packages[i]);
        }
        return mb.getBytes();
    }
}
