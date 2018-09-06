package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.model.TBankCardExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TBankCardMapper {
    long countByExample(TBankCardExample example);

    int deleteByExample(TBankCardExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TBankCard record);

    int insertSelective(TBankCard record);

    List<TBankCard> selectByExample(TBankCardExample example);

    TBankCard selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TBankCard record, @Param("example") TBankCardExample example);

    int updateByExample(@Param("record") TBankCard record, @Param("example") TBankCardExample example);

    int updateByPrimaryKeySelective(TBankCard record);

    int updateByPrimaryKey(TBankCard record);
}