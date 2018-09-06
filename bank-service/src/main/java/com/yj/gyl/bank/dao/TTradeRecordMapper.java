package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TTradeRecord;
import com.yj.gyl.bank.model.TTradeRecordExample;
import com.yj.gyl.bank.model.TTradeRecordWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TTradeRecordMapper {
    long countByExample(TTradeRecordExample example);

    int deleteByExample(TTradeRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TTradeRecordWithBLOBs record);

    int insertSelective(TTradeRecordWithBLOBs record);

    List<TTradeRecordWithBLOBs> selectByExampleWithBLOBs(TTradeRecordExample example);

    List<TTradeRecord> selectByExample(TTradeRecordExample example);

    TTradeRecordWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TTradeRecordWithBLOBs record, @Param("example") TTradeRecordExample example);

    int updateByExampleWithBLOBs(@Param("record") TTradeRecordWithBLOBs record, @Param("example") TTradeRecordExample example);

    int updateByExample(@Param("record") TTradeRecord record, @Param("example") TTradeRecordExample example);

    int updateByPrimaryKeySelective(TTradeRecordWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TTradeRecordWithBLOBs record);

    int updateByPrimaryKey(TTradeRecord record);
}