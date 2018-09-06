package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TTransferRecord;
import com.yj.gyl.bank.model.TTransferRecordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TTransferRecordMapper {
    long countByExample(TTransferRecordExample example);

    int deleteByExample(TTransferRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TTransferRecord record);

    int insertSelective(TTransferRecord record);

    List<TTransferRecord> selectByExample(TTransferRecordExample example);

    TTransferRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TTransferRecord record, @Param("example") TTransferRecordExample example);

    int updateByExample(@Param("record") TTransferRecord record, @Param("example") TTransferRecordExample example);

    int updateByPrimaryKeySelective(TTransferRecord record);

    int updateByPrimaryKey(TTransferRecord record);
}