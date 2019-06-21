package com.example.cinema.bl.sales;

import com.example.cinema.vo.RefundForm;
import com.example.cinema.vo.ResponseVO;

public interface RefundService {

    ResponseVO setRefund(RefundForm refundForm);

    ResponseVO getRefund();
}
