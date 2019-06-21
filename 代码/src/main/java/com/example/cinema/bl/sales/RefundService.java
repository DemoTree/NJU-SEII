package com.example.cinema.bl.sales;

import com.example.cinema.vo.RefundForm;
import com.example.cinema.vo.ResponseVO;

public interface RefundService {//退票策略

    ResponseVO setRefund(RefundForm refundForm);

    ResponseVO getRefund();
}
