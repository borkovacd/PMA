package com.android.exconvictslocator.synchronization;

public class ServerIPConfig {

    private static String ip_address = "192.168.137.1";
    //private static String ip_address = "192.168.43.226"; //MOBILE HOTSPOT
//     private static String ip_address = "192.168.0.16";  - Olga
    public static String getIp_address() {
        return ip_address;
    }
}
