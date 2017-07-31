package com.yzd.dubbo.monitor.web.utils.toolbox;

import com.yzd.dubbo.monitor.service.utils.tool.RegistryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zd.yao on 2017/7/17.
 */
public class UrlHelperTool {
    private static final Map<String, String> urlMap = new ConcurrentHashMap<String, String>();

    public String href(String hrefKey) {
        String hrefVal = urlMap.get(hrefKey);
        if (hrefVal != null) return hrefVal;
        hrefVal = urlMap.get(hrefKey.toLowerCase());
        if (hrefVal != null) return hrefVal;
        String file = "/static" + hrefKey;//static是静态文件的根目录
        //
        InputStream is=null;
        try {
            is = getInputStream(file);
            if (is == null) {
                hrefVal = hrefKey + "?throwException-NoFoundFile";
                urlMap.put(hrefKey, hrefVal);
                return "\"" + hrefVal + "\"";
            }

            String md5 = getMD5(is);
            hrefVal = hrefKey + "?" + md5;
            urlMap.put(hrefKey, hrefVal);
            return "\"" + hrefVal + "\"";
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if(is!=null) try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private InputStream getInputStream(String fileName) {
        return getClass().getResourceAsStream(fileName);
    }

    private String getMD5(InputStream is) throws NoSuchAlgorithmException, IOException {
        StringBuffer md5 = new StringBuffer();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] dataBytes = new byte[1024];
        int nread = 0;
        while ((nread = is.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        ;
        byte[] mdbytes = md.digest();
        // convert the byte to hex format
        for (int i = 0; i < mdbytes.length; i++) {
            md5.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return md5.toString();
    }

    public String getRegistryAddressInfo() {
        return RegistryUtil.getAddressInfo();
    }

}
