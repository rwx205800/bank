package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TMerchantSource;
import com.yj.gyl.bank.model.TMerchantSourceExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TMerchantSourceMapper {
    long countByExample(TMerchantSourceExample example);

    int deleteByExample(TMerchantSourceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TMerchantSource record);

    int insertSelective(TMerchantSource record);

    List<TMerchantSource> selectByExample(TMerchantSourceExample example);

    TMerchantSource selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TMerchantSource record, @Param("example") TMerchantSourceExample example);

    int updateByExample(@Param("record") TMerchantSource record, @Param("example") TMerchantSourceExample example);

    int updateByPrimaryKeySelective(TMerchantSource record);

    int updateByPrimaryKey(TMerchantSource record);
}