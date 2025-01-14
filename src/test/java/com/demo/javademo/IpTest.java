package com.demo.javademo;

import com.demo.crawler.ip.KuaiDaiLiIP;

public class IpTest {
    public static void main(String[] args) {
        KuaiDaiLiIP kuaiDaiLiIP = new KuaiDaiLiIP();
        String ipList = kuaiDaiLiIP.getIPList();
        System.out.println(ipList);
    }
}
