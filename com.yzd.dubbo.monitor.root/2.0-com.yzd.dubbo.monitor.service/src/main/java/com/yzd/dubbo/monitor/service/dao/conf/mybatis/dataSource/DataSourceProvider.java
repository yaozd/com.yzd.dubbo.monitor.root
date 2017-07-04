package com.yzd.dubbo.monitor.service.dao.conf.mybatis.dataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 解决：The dependencies of some of the beans in the application context form a cycle
 * 使sqlDbMasterDataSource与sqlDbSlaveDataSource提前实例化
 * Created by zd.yao on 2017/7/3.
 */
@Configuration
public class DataSourceProvider {
    @Autowired
    @Qualifier("sqlDbMasterDataSource")
    private DataSource sqlDbMasterDataSource;
    @Autowired
    @Qualifier("sqlDbSlaveDataSource")
    private DataSource sqlDbUserSlaveDataSource;
}
