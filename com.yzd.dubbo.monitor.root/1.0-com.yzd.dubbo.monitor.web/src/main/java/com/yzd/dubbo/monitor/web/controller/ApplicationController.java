package com.yzd.dubbo.monitor.web.controller;

import com.yzd.dubbo.monitor.service.bo.ApplicationBO;
import com.yzd.dubbo.monitor.service.bo.ServiceBO;
import com.yzd.dubbo.monitor.service.service.inf.ApplicationServiceInf;
import com.yzd.dubbo.monitor.web.model.response.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by zd.yao on 2017/7/17.
 */
@Controller
@RequestMapping("application")
public class ApplicationController {
    @Autowired
    private ApplicationServiceInf applicationService;

    @RequestMapping("index")
    public String index(){

        return "application/index";
    }

    //所有服务和其相关信息
    @RequestMapping(value = "/getAllAPPAndRelation", method = RequestMethod.GET)
    @ResponseBody
    public ResultVO getAllAPPAndRelation(){
        try {
            Map<String, Object> resultMap = new HashMap<>();

            Map<String, ApplicationBO> allApplicationsMap = applicationService.getApplicationsBOMap();
            List<ApplicationBO> appList = new ArrayList<>();

            Set<String> groupSet = new HashSet<>();
            Integer appSum = 0;
            for (Map.Entry<String, ApplicationBO> applicationBOEntry : allApplicationsMap.entrySet()) {
                appSum += 1;
                ApplicationBO applicationBO = applicationBOEntry.getValue();
                String organization = applicationBO.getOrganization();
                if (!organization.equals("")) {
                    groupSet.add(organization);
                }

                Set<String> serviceSet = new HashSet<>();
                Map<String, Set<ServiceBO>> serviceMap = applicationBO.getServiceMap();
                if (serviceMap != null) {
                    for (Map.Entry<String, Set<ServiceBO>> entry : serviceMap.entrySet()) {
                        Set<ServiceBO> serviceBOSet = entry.getValue();
                        for (ServiceBO serviceBO : serviceBOSet) {
                            serviceSet.add(serviceBO.getServiceName());
                        }
                    }
                }

                Set<String> providersSet = applicationBO.getProvidersSet();
                Set<String> consumersSet = applicationBO.getConsumersSet();
                if (!serviceSet.isEmpty()) applicationBO.setServiceSum(serviceSet.size());
                if (null != providersSet) applicationBO.setProviderSum(providersSet.size());
                if (null != consumersSet) applicationBO.setConsumerSum(consumersSet.size());
                appList.add(applicationBO);
            }

            //对appList 排序，按首字母
            Collections.sort(appList, new Comparator<ApplicationBO>() {
                @Override
                public int compare(ApplicationBO o1, ApplicationBO o2) {
                    Integer o1First = o1.getApplicationName().codePointAt(0);
                    Integer o2First = o2.getApplicationName().codePointAt(0);
                    return o1First.compareTo(o2First);
                }
            });

            resultMap.put("appSum", appSum);
            resultMap.put("groupSum", groupSet.size());
            resultMap.put("appList", appList);
            resultMap.put("allApp", allApplicationsMap);
            return ResultVO.wrapSuccessfulResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.wrapErrorResult(e.getMessage());
        }

    }
}
