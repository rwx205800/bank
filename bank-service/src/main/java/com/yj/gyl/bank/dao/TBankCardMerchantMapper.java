package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TBankCardMerchant;
import com.yj.gyl.bank.model.TBankCardMerchantExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TBankCardMerchantMapper {
    long countByExample(TBankCardMerchantExample example);

    int deleteByExample(TBankCardMerchantExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TBankCardMerchant record);

    int insertSelective(TBankCardMerchant record);

    List<TBankCardMerchant> selectByExample(TBankCardMerchantExample example);

    TBankCardMerchant selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TBankCardMerchant record, @Param("example") TBankCardMerchantExample example);

    int updateByExample(@Param("record") TBankCardMerchant record, @Param("example") TBankCardMerchantExample example);

    int updateByPrimaryKeySelective(TBankCardMerchant record);

    int updateByPrimaryKey(TBankCardMerchant record);
}