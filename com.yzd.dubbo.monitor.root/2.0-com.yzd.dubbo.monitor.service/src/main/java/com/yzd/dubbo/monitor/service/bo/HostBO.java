package com.yzd.dubbo.monitor.service.bo;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * Created by zxg on 15/11/17.
 */
@ToString
@Data
public class HostBO {
    private String host;
    private String port;
    //增加了服务的创建时间
    private String createTime;

    public HostBO(){}
    public HostBO(String host, String port) {
        this.host = host;
        this.port = port;
    }

    private String hostString;

    public String getHostString(){
        if(port == null){
            return host;
        }
        return host+":"+port;
    }


    //======host 页面展示所用====
    Set<String> providers;
    Set<String> consumers;

    //服务名--即dba定义的该ip地址的名称
    String hostName;
    //对应的另外一个ip
    String anotherIp;
    //todo 由于增加了应用的创建时间所以必须重写equals与hashcode才可以用于set时排重
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        HostBO other = (HostBO)obj;
        if(host == null){
            if (other.host != null)
                return false;
        }else if(!host.equals(other.host))
            return false;
        if (port == null){
            if (other.port != null)
                return false;
        }else if(!port.equals(other.port))
            return false;
        return true;
    }
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        return result;
    }
}
