package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TBindCardRecord;
import com.yj.gyl.bank.model.TBindCardRecordExample;
import com.yj.gyl.bank.model.TBindCardRecordWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TBindCardRecordMapper {
    long countByExample(TBindCardRecordExample example);

    int deleteByExample(TBindCardRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TBindCardRecordWithBLOBs record);

    int insertSelective(TBindCardRecordWithBLOBs record);

    List<TBindCardRecordWithBLOBs> selectByExampleWithBLOBs(TBindCardRecordExample example);

    List<TBindCardRecord> selectByExample(TBindCardRecordExample example);

    TBindCardRecordWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TBindCardRecordWithBLOBs record, @Param("example") TBindCardRecordExample example);

    int updateByExampleWithBLOBs(@Param("record") TBindCardRecordWithBLOBs record, @Param("example") TBindCardRecordExample example);

    int updateByExample(@Param("record") TBindCardRecord record, @Param("example") TBindCardRecordExample example);

    int updateByPrimaryKeySelective(TBindCardRecordWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TBindCardRecordWithBLOBs record);

    int updateByPrimaryKey(TBindCardRecord record);
}