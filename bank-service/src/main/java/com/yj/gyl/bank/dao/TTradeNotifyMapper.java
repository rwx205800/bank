package com.yj.gyl.bank.dao;

import com.yj.gyl.bank.model.TTradeNotify;
import com.yj.gyl.bank.model.TTradeNotifyExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TTradeNotifyMapper {
    long countByExample(TTradeNotifyExample example);

    int deleteByExample(TTradeNotifyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TTradeNotify record);

    int insertSelective(TTradeNotify record);

    List<TTradeNotify> selectByExample(TTradeNotifyExample example);

    TTradeNotify selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TTradeNotify record, @Param("example") TTradeNotifyExample example);

    int updateByExample(@Param("record") TTradeNotify record, @Param("example") TTradeNotifyExample example);

    int updateByPrimaryKeySelective(TTradeNotify record);

    int updateByPrimaryKey(TTradeNotify record);
}