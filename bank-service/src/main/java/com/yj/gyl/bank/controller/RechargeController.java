package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.IRechargeServiceApi;
import com.yj.gyl.bank.dto.ConfirmRechargeDto;
import com.yj.gyl.bank.dto.PreRechargeDto;
import com.yj.gyl.bank.dto.YeePayRechargeDto;
import com.yj.gyl.bank.handler.IRechargeHandler;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hepei
 * @date 2017/12/13 17:27:53
 */
@RestController
public class RechargeController extends AbstractController implements IRechargeServiceApi {
    @Autowired
    private IRechargeHandler rechargeHandler;

    @Override
    public CommonResponse<TradeRecordRsDto> preRecharge(@RequestBody PreRechargeDto preRechargeDto) {
        return rechargeHandler.preRecharge(preRechargeDto);
    }

    @Override
    public CommonResponse<TradeRecordRsDto> confirmRecharge(@RequestBody ConfirmRechargeDto confirmRechargeDto) {
        return rechargeHandler.confirmRecharge(confirmRechargeDto);
    }

    @Override
    public CommonResponse rechargeResendSMS(@RequestParam("orderNo") String orderNo) {
        return rechargeHandler.rechargeResendSMS(orderNo);
    }

    @Override
    public CommonResponse rechargeRecord(@RequestParam("orderNo") String orderNo) {
        return rechargeHandler.rechargeRecord(orderNo);
    }

    @Override
    public CommonResponse yeePayRecharge(@RequestBody YeePayRechargeDto rechargeDto) {
        return rechargeHandler.yeePayRecharge(rechargeDto);
    }

    /**
     * 网银充值记录查询
     *
     * @param orderNo
     * @return
     */
    @Override
    public CommonResponse yeePayRechargeRecord(@RequestParam("orderNo") String orderNo) {
        return rechargeHandler.yeePayRechargeRecord(orderNo);
    }

    /**
     * 获取银行通道编码表
     *
     * @return
     */
    @Override
    public CommonResponse yeePayBankFrpList() {
        return rechargeHandler.yeePayBankFrpList();
    }
}
