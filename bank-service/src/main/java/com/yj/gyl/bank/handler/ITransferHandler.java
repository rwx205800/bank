package com.yj.gyl.bank.handler;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.TransferRecordDto;

/**
 * Created by renfei on 2017/12/19.
 */
public interface ITransferHandler {

    /**
     * 单笔打款
     *
     * @param projectId 项目ID
     * @param amount    金额
     * @return
     */
    CommonResponse transferSingle(String projectId, String amount);

    /**
     * 查询打款明细
     *
     * @param transferRecordDto
     * @return
     */
    CommonResponse transAcountDetail(TransferRecordDto transferRecordDto);
}
