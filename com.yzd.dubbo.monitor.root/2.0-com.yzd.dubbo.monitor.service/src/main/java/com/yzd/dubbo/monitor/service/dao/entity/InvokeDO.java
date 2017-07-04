package com.yzd.dubbo.monitor.service.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zd.yao on 2017/6/28.
 */
@Data
@NoArgsConstructor
public class InvokeDO implements Serializable {

    private String uuId;

    private String application;

    private String appType;

    private String service;

    private String method;

    private String consumerHost;

    private String consumerPort;

    private String providerHost;

    private String providerPort;

    private Integer success;

    private Integer failure;

    private Integer elapsed;

    private Integer concurrent;

    private Integer maxElapsed;

    private Integer maxConcurrent;

    private String invokeDate;

    private String invokeHour;

    private Long invokeTime;

    private Date gmtCreate;
}
