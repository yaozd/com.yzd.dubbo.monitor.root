package com.yzd.dubbo.monitor.service.utils.tool;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;

/**
 * Created by zd.yao on 2017/7/13.
 */
public class RegistryUtil {
    //获得需要订阅的URL
    public static URL getSubscribeUrl(){
        URL subscribeUrl = new URL(Constants.ADMIN_PROTOCOL, NetUtils.getLocalHost(), 0, "",
                Constants.INTERFACE_KEY, Constants.ANY_VALUE,
                Constants.GROUP_KEY, Constants.ANY_VALUE,
                Constants.VERSION_KEY, Constants.ANY_VALUE,
                Constants.CLASSIFIER_KEY, Constants.ANY_VALUE,
                Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY + ","
                + Constants.CONSUMERS_CATEGORY+ ","
                + Constants.CONFIGURATORS_CATEGORY,
                Constants.CHECK_KEY, String.valueOf(false));
        return subscribeUrl;
    }
    //对URL集合进行过滤
    public static boolean filterUrl(String monitorDubboName,URL url){
        String application = url.getParameter(Constants.APPLICATION_KEY);
        if (monitorDubboName.equals(application)) {
            return true;
        }
        String service = url.getServiceKey();
        if ("com.alibaba.dubbo.monitor.MonitorService".equals(service)) {
            return true;
        }
        return false;
    }
    public static String getAddressInfo() {
        return "address="+MonitorConstants.DUBBO_REGISTRY_ADDRESS+"|"+"group="+MonitorConstants.DUBBO_REGISTRY_GROPU;
    }
}
