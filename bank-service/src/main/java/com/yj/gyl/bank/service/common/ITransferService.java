package com.yj.gyl.bank.service.common;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.TransferSend;
import com.yj.gyl.bank.model.TTransferRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by renfei on 2017/12/19.
 */
public interface ITransferService {

    /**
     * 查询状态为成功或处理中的记录
     *
     * @param projectId 项目ID
     * @return
     */
    List<TTransferRecord> listTransferRecord(Long projectId);

    /**
     * 保存单笔打款记录
     *
     * @param projectId 项目Id
     * @param amount    金额
     * @param status    状态
     * @param batchNo   批次号
     * @param orderNo   订单编号
     */
    Boolean saveTransferRecord(Long projectId, BigDecimal amount, int status, String batchNo, String orderNo);

    /**
     * 更新
     *
     * @param transferRecord
     */
    void updateTransferRecord(TTransferRecord transferRecord);

    /**
     * 单笔打款
     *
     * @param amount  金额
     * @param orderId 订单号
     * @param batchNo 打款批次号
     * @return
     */
    CommonResponse transferSingle(BigDecimal amount, String orderId, String batchNo);

    CommonResponse transferSingle(TransferSend transferSend);
    /**
     * 查询打款明细
     *
     * @param orderNo 订单号
     * @param batchNo 打款批次号
     * @param page    页码
     * @return
     */
    CommonResponse transAcountDetail(String orderNo, String batchNo, Integer page);

    TTransferRecord getTransferRecord(Long userId,String orderNo);
}
