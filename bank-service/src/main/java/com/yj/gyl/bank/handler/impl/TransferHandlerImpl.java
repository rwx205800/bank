package com.yj.gyl.bank.handler.impl;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.TransferRecordDto;
import com.yj.gyl.bank.handler.ITransferHandler;
import com.yj.gyl.bank.model.TTransferRecord;
import com.yj.gyl.bank.rsdto.TransactionPayEnum;
import com.yj.gyl.bank.service.common.ITransferService;
import com.yj.gyl.bank.service.yibao.util.DateUtils;
import com.yj.gyl.bank.service.yibao.util.IdcardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by renfei on 2017/12/19.
 */
@Component
public class TransferHandlerImpl implements ITransferHandler {
    @Autowired
    private ITransferService transferService;

    @Override
    public CommonResponse transferSingle(String projectId, String amount) {
        //批量号
        String batchNo = IdcardUtils.getRandam15();
        String orderId = DateUtils.getFlowTimeDate() + new Random().nextInt(999999999);

        List<TTransferRecord> recordList = transferService.listTransferRecord(Long.valueOf(projectId));
        //没有处理中、成功记录重新打款
        if (recordList == null || recordList.size() < 1) {
            boolean result = transferService.saveTransferRecord(Long.valueOf(projectId), new BigDecimal(amount), 0, batchNo, orderId);

            CommonResponse commonResponse = transferService.transferSingle(new BigDecimal(amount), orderId, batchNo);
            //默认状态:处理中
            int status = 0;
            if (commonResponse != null && commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) {
                status = 1;
            } else if (commonResponse.getResult() == 1) {
                status = 2;
            }
            if (status != 0) {
                TTransferRecord transferRecord = new TTransferRecord();
                transferRecord.setProjectId(Long.valueOf(projectId));
                transferRecord.setOrderNo(orderId);
                transferRecord.setBatchNo(batchNo);
                transferRecord.setStatus(status);
                transferRecord.setMessage(commonResponse.getMsg());
                transferRecord.setUpdateTime(new Date());
                transferService.updateTransferRecord(transferRecord);
            }
            return commonResponse;
        }
        //有成功记录直接返回
        for (TTransferRecord record : recordList) {
            if (record.getStatus() == 1) {
                //已有成功的记录
                return CommonResponse.build(TransactionPayEnum.GLOBAL_SUCCESS);
            }
        }
        //处理中的记录，调用查询接口查询处理结果
        for (TTransferRecord record : recordList) {
            if (record.getStatus() == 0) {
                CommonResponse commonResponse = transferService.transAcountDetail(record.getOrderNo(), record.getBatchNo(), 1);
                //默认状态:处理中
                int status = 0;
                if (commonResponse != null && commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) {
                    status = 1;
                } else if (commonResponse.getResult() == 1) {
                    status = 2;
                }
                if (status != 0) {
                    TTransferRecord transferRecord = new TTransferRecord();
                    transferRecord.setProjectId(Long.valueOf(projectId));
                    transferRecord.setOrderNo(record.getOrderNo());
                    transferRecord.setBatchNo(record.getBatchNo());
                    transferRecord.setStatus(status);
                    transferRecord.setMessage(commonResponse.getMsg());
                    transferRecord.setUpdateTime(new Date());
                    transferService.updateTransferRecord(transferRecord);
                }
                return commonResponse;
            }
        }
        return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
    }

    @Override
    public CommonResponse transAcountDetail(TransferRecordDto transferRecordDto) {
        if (transferRecordDto.getProjectId() == null || transferRecordDto.getProjectId() < 1) {
            return CommonResponse.builder().result(1).status(1).msg("项目ID有误").build();
        }
        if (!StringUtils.hasText(transferRecordDto.getOrderNo())) {
            return CommonResponse.builder().result(1).status(1).msg("订单号不能为空").build();
        }
        if (!StringUtils.hasText(transferRecordDto.getBatchNo())) {
            return CommonResponse.builder().result(1).status(1).msg("批次号不能为空").build();
        }
        CommonResponse commonResponse = transferService.transAcountDetail(transferRecordDto.getOrderNo(), transferRecordDto.getBatchNo(), transferRecordDto.getPageSize());
        int status = 0;
        if (commonResponse != null && commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) {
            status = 1;
        } else if (commonResponse.getResult() == 1) {
            status = 2;
        }
        if (status != 0) {
            TTransferRecord transferRecord = new TTransferRecord();
            transferRecord.setProjectId(transferRecordDto.getProjectId());
            transferRecord.setOrderNo(transferRecordDto.getOrderNo());
            transferRecord.setBatchNo(transferRecordDto.getBatchNo());
            transferRecord.setStatus(status);
            transferRecord.setMessage(commonResponse.getMsg());
            transferRecord.setUpdateTime(new Date());
            transferService.updateTransferRecord(transferRecord);
        }
        Map<String, String> map = new HashMap<>();
        map.put("batchNo", transferRecordDto.getBatchNo());
        map.put("orderNo", transferRecordDto.getOrderNo());
        commonResponse.setData(map);
        return commonResponse;
    }
}
