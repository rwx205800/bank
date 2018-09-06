package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.model.TMerchantExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TMerchantMapper {
    long countByExample(TMerchantExample example);

    int deleteByExample(TMerchantExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TMerchant record);

    int insertSelective(TMerchant record);

    List<TMerchant> selectByExample(TMerchantExample example);

    TMerchant selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TMerchant record, @Param("example") TMerchantExample example);

    int updateByExample(@Param("record") TMerchant record, @Param("example") TMerchantExample example);

    int updateByPrimaryKeySelective(TMerchant record);

    int updateByPrimaryKey(TMerchant record);
}