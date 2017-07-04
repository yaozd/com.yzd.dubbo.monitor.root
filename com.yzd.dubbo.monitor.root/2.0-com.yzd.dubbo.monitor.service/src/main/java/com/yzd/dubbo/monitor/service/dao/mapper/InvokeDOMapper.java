package com.yzd.dubbo.monitor.service.dao.mapper;

import com.yzd.dubbo.monitor.service.dao.entity.InvokeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zd.yao on 2017/6/30.
 */
public interface InvokeDOMapper {
    int deleteByPrimaryKey(String uuId);

    int insertSelective(InvokeDO record);

    InvokeDO selectByPrimaryKey(String uuId);

    int updateByPrimaryKeySelective(InvokeDO record);

    List<InvokeDO> selectByInvokeDO(InvokeDO searchDO);

    int deleteByDate(@Param(value = "date") String minDate);

    void addRecordBatch (List<InvokeDO> recordList);

}
