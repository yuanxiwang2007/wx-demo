package com.estatesoft.ris.wx.utils;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class SHA1Util {
    public SHA1Util() {
    }

    public static String createSHA1Sign(SortedMap<String, String> signParams) throws Exception {
        StringBuffer sb = new StringBuffer();
        Set es = signParams.entrySet();
        Iterator it = es.iterator();

        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            sb.append(k + "=" + v + "&");
        }

        String params = sb.substring(0, sb.lastIndexOf("&"));
        return getSha1(params);
    }

    public static String getSha1(String str) {
        if (str != null && str.length() != 0) {
            char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

            try {
                MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
                mdTemp.update(str.getBytes("UTF-8"));
                byte[] md = mdTemp.digest();
                int j = md.length;
                char[] buf = new char[j * 2];
                int k = 0;

                for(int i = 0; i < j; ++i) {
                    byte byte0 = md[i];
                    buf[k++] = hexDigits[byte0 >>> 4 & 15];
                    buf[k++] = hexDigits[byte0 & 15];
                }

                return new String(buf);
            } catch (Exception var9) {
                return null;
            }
        } else {
            return null;
        }
    }
}
