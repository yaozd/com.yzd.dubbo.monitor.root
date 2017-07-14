package com.yzd.dubbo.monitor.service.service.impl;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.RegistryService;
import com.yzd.dubbo.monitor.service.service.inf.RegistryServiceInf;
import com.yzd.dubbo.monitor.service.utils.tool.MonitorConstants;
import com.yzd.dubbo.monitor.service.utils.tool.RegistryUtil;
import com.yzd.dubbo.monitor.service.utils.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dubbo 注册中心
 * Created by zd.yao on 2017/7/13.
 */
@Service
public class RegistryServiceImpl implements RegistryServiceInf {
    /**
     * map-layer-1:类型：生产者；消费者;配置信息
     * map-layer-2:服务的名称(接口的名称)：具体的URL配置信息*/
    private final Map<String, Map<String, Set<URL>>> registryCache = new ConcurrentHashMap<>();
    /**判断是否开始监控数据变化
     * time:执行时间
     * startMonitor:是否开始执行*/
    private final Map<String, Object> finalDataMap = new ConcurrentHashMap<>();
    //当前监测服务的dubbo注册名
    //String monitorDubboName ="com.yzd.dubbo.monitor.provider";
    private final String monitorDubboName = MonitorConstants.DUBBO_APPLICATION_NAME;
    @Autowired
    private RegistryService registry;
    @Override
    public void subscribe() {
        //获得需要订阅的URL
        URL subscribeUrl = RegistryUtil.getSubscribeUrl();
        // 订阅符合条件的已注册数据，当有注册数据变更时自动推送.
        // dubbo服务变化更新通知机制-当服务节点发生变化后会把当前节点最新的数据合发送给客户端;
        // 相当于重新获得服务最新所有数据
        // 特别注意：只有当一个服务的所有程序都不存在时才会发url以empty开头的url处理，移除此服务的所有数据:
        registry.subscribe(subscribeUrl, new NotifyListener() {
            public void notify(List<URL> urls) {
                if (urls == null || urls.size() == 0) {
                    return;
                }
                // 组合新数据
                final Map<String, Map<String, Set<URL>>> categories = new ConcurrentHashMap<>();
                //实际逻辑
                for (URL url : urls) {
                    //过滤当前监控程序本身的信息与com.alibaba.dubbo.monitor.MonitorService的信息
                    if(RegistryUtil.filterUrl(monitorDubboName,url)){
                        continue;
                    }
                    // url以empty开头的url处理，移除此数据:涉及provider、consumer的减少
                    // 从registryCache集合中删除URL
                    // 移除此数据:涉及provider、consumer的减少／进行数据通知+禁用数据的更改
                    String protocol = url.getProtocol();
                    if (Constants.EMPTY_PROTOCOL.equals(protocol)) {
                        deleteUrlInRegistryCache(url);
                        continue;
                    }
                    //逻辑处理
                    //region-将数据加载到categories集合中
                    String category = url.getParameter(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY);
                    Map<String, Set<URL>> services = categories.get(category);
                    if (services == null) {
                        services = new ConcurrentHashMap<>();
                        categories.put(category, services);
                    }
                    String service = url.getServiceKey();
                    Set<URL> servicesUrlSet = services.get(service);
                    if (servicesUrlSet == null) {
                        servicesUrlSet = new ConcurrentHashSet<>();
                        services.put(service, servicesUrlSet);
                    }
                    servicesUrlSet.add(url);
                    //endregion
                }
                //将categories数据添加到registryCache集合
                addCategoriesInRegistryCache(categories);
                //数据的最后更新时间
                finalDataMap.put("now", new Date());
            }
        });
    }


    @Override
    public Map<String, Map<String, Set<URL>>> getRegistryCache() {
        if(!registryCache.containsKey(Constants.PROVIDERS_CATEGORY)){
            registryCache.put(Constants.PROVIDERS_CATEGORY,new ConcurrentHashMap<String, Set<URL>>());
        }
        if(!registryCache.containsKey(Constants.CONSUMERS_CATEGORY)){
            registryCache.put(Constants.CONSUMERS_CATEGORY,new ConcurrentHashMap<String, Set<URL>>());
        }
        if(!registryCache.containsKey(Constants.CONFIGURATORS_CATEGORY)){
            registryCache.put(Constants.CONFIGURATORS_CATEGORY,new ConcurrentHashMap<String, Set<URL>>());
        }
        return Collections.unmodifiableMap(registryCache);
    }

    @Override
    public Date getFinalUpdateTime() {
        Date now = (Date) finalDataMap.get("now");
        return now;
    }

    //将categories数据添加到registryCache集合
    private void addCategoriesInRegistryCache(Map<String, Map<String, Set<URL>>> categories) {
        for (Map.Entry<String, Map<String, Set<URL>>> categoryEntry : categories.entrySet()) {
            String category = categoryEntry.getKey();
            Map<String, Set<URL>> services = registryCache.get(category);
            if (services == null) {
                services = new ConcurrentHashMap<>();
                registryCache.put(category, services);
            }
            services.putAll(categoryEntry.getValue());
        }
    }
    // url以empty开头的url处理，移除此数据:涉及provider、consumer的减少
    // 从registryCache集合中删除URL
    private void deleteUrlInRegistryCache(URL url){
        String category = url.getParameter(Constants.CATEGORY_KEY, Constants.DEFAULT_CATEGORY);
        Map<String, Set<URL>> services = registryCache.get(category);
        if (services != null) {
            String group = url.getParameter(Constants.GROUP_KEY);
            String version = url.getParameter(Constants.VERSION_KEY);
            // 注意：empty协议的group和version为*
            if (!Constants.ANY_VALUE.equals(group) && !Constants.ANY_VALUE.equals(version)) {
                String service = url.getServiceKey();
                services.remove(service);
            } else {
                String urlService = url.getServiceInterface();
                for (Map.Entry<String, Set<URL>> serviceEntry : services.entrySet()) {
                    String service = serviceEntry.getKey();
                    if (Tool.getInterface(service).equals(urlService)
                            && (Constants.ANY_VALUE.equals(group) || StringUtils.isEquals(group, Tool.getGroup(service)))
                            && (Constants.ANY_VALUE.equals(version) || StringUtils.isEquals(version, Tool.getVersion(service)))) {
                        services.remove(service);
                    }
                }
            }
        }
    }
}
