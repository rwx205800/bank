package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TBankCardBase;
import com.yj.gyl.bank.model.TBankCardBaseExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TBankCardBaseMapper {
    long countByExample(TBankCardBaseExample example);

    int deleteByExample(TBankCardBaseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TBankCardBase record);

    int insertSelective(TBankCardBase record);

    List<TBankCardBase> selectByExample(TBankCardBaseExample example);

    TBankCardBase selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TBankCardBase record, @Param("example") TBankCardBaseExample example);

    int updateByExample(@Param("record") TBankCardBase record, @Param("example") TBankCardBaseExample example);

    int updateByPrimaryKeySelective(TBankCardBase record);

    int updateByPrimaryKey(TBankCardBase record);
}