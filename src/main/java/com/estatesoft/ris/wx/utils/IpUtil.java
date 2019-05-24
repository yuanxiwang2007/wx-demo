package com.estatesoft.ris.wx.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by wk on 2017/8/10.
 */
public class IpUtil {
    public static String getLocalIp() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
//            System.out.println(netInterface.getName());
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address&&!"127.0.0.1".equals(ip.getHostAddress())) {
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }
}
