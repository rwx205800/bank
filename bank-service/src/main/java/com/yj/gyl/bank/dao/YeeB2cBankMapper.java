package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.YeeB2cBank;
import com.yj.gyl.bank.model.YeeB2cBankExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YeeB2cBankMapper {
    long countByExample(YeeB2cBankExample example);

    int deleteByExample(YeeB2cBankExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(YeeB2cBank record);

    int insertSelective(YeeB2cBank record);

    List<YeeB2cBank> selectByExample(YeeB2cBankExample example);

    YeeB2cBank selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") YeeB2cBank record, @Param("example") YeeB2cBankExample example);

    int updateByExample(@Param("record") YeeB2cBank record, @Param("example") YeeB2cBankExample example);

    int updateByPrimaryKeySelective(YeeB2cBank record);

    int updateByPrimaryKey(YeeB2cBank record);
}