package com.yzd.dubbo.monitor.service.utils.tool;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.yzd.dubbo.monitor.common.timeExt.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by zd.yao on 2017/7/13.
 */
public class Tool {

    public static String getInterface(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                service = service.substring(i + 1);
            }
            i = service.lastIndexOf(':');
            if (i >= 0) {
                service = service.substring(0, i);
            }
        }
        return service;
    }

    public static String getGroup(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                return service.substring(0, i);
            }
        }
        return null;
    }

    public static String getVersion(String service) {
        if (service != null && service.length() > 0) {
            int i = service.lastIndexOf(':');
            if (i >= 0) {
                return service.substring(i + 1);
            }
        }
        return null;
    }
    //判断是否是禁止的url
    public static Boolean compareIsOverride(URL url,Set<URL> forbitUrlSet){
        if(null == forbitUrlSet || forbitUrlSet.isEmpty()){
            return false;
        }
        String host = url.getHost();
        Integer port = url.getPort();
        String path = url.getPath();
        String version = url.getParameter(Constants.VERSION_KEY);

        Boolean result = false;
        for(URL compareUrl : forbitUrlSet){
            String compareHost = compareUrl.getHost();
            Integer comparePort = compareUrl.getPort();
            String comparePath = compareUrl.getPath();
            String compareVersion = compareUrl.getParameter(Constants.VERSION_KEY);
            if(host.equals(compareHost) && port.equals(comparePort) && path.equals(comparePath) && version.equals(compareVersion)){
                result = true;
                break;
            }
        }
        return result;
    }
    public static String getUrlTimeInfo(URL url) {
        String timestamp = url.getParameter(Constants.TIMESTAMP_KEY);
        Date now;
        if (timestamp == null || timestamp.length() == 0) {
           return "";
        } else if (timestamp.length() == "yyyyMMddHHmmss".length()) {
            try {
                now = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        } else {
            now = new Date(Long.parseLong(timestamp));
        }
        return TimeUtil.getFullTimeString(now);
    }
    public static String getUrlShortTimeInfo(URL url) {
        String timestamp = url.getParameter(Constants.TIMESTAMP_KEY);
        Date now;
        if (timestamp == null || timestamp.length() == 0) {
            return "";
        } else if (timestamp.length() == "yyyyMMddHHmmss".length()) {
            try {
                now = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        } else {
            now = new Date(Long.parseLong(timestamp));
        }
        return TimeUtil.getFullTimeString(now);
    }

    /**
     * 增加当前应用的版本号-application.version
     * @param url
     * @return
     */
    public static String getAppVersionInfo(URL url){
        String version=url.getParameter("application.version");
        return version==null?"":version;
    }

}
