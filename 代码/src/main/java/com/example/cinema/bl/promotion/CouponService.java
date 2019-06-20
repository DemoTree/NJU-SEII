package com.example.cinema.bl.promotion;

import com.example.cinema.vo.CouponForm;
import com.example.cinema.vo.ResponseVO;

/**
 * Created by liying on 2019/4/17.
 */
public interface CouponService {

    ResponseVO getCouponsByUser(int userId);

    ResponseVO addCoupon(CouponForm couponForm);

    ResponseVO issueCoupon(int couponId,int userId);

}
