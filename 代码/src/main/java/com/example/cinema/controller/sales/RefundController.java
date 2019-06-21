package com.example.cinema.controller.sales;


import com.example.cinema.bl.sales.RefundService;
import com.example.cinema.vo.RefundForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    RefundService refundService;

    @PostMapping("/set")
    public ResponseVO setRefund(@RequestBody RefundForm refundForm){
        return refundService.setRefund(refundForm);
    }

    @GetMapping("/get")
    public ResponseVO getRefund(){
        return refundService.getRefund();
    }
}
