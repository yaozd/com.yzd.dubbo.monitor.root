package com.yzd.dubbo.monitor.service.service.inf;

import com.yzd.dubbo.monitor.service.bo.ApplicationBO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zd.yao on 2017/7/14.
 */
public interface ApplicationServiceInf {
    //热数据
    Set<String> getAllApplications();

    //缓存一个小时的数据，不关心即时性，可在里面取,无重复
    List<String> getAllApplicationsCache();

    // app名称，app对象
    Map<String,ApplicationBO> getApplicationsBOMap();
}
