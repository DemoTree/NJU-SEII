/**
 * @author 蔡明卫
 * @date 6/1
 */

$(document).ready(function() {
    var couponId=0;
    var userList=[];



    getCoupon();//查询可送优惠券
    getVipConsumebyMoney(0);//查询满足条件的用户

    //输入新的赠送条件金额
    $('#btnclick').click(function () {
        var val=$('#money').val();
        if(val==null){
            val=0;
        }
        getVipConsumebyMoney(val)
    })




    //选中后赠送
    $('#checkuser').click(function () {
        couponId=0;
        userList=[];
        $('input[name="user"]:checked').each(function() {
            userList.push($(this).val())
        })
        if ($("input[name='coupon']:checked").val()) {
            couponId = $("input[name='coupon']:checked").val();
        }

        if (userList.length!=0&&couponId!=0){
            postRequest(
                '/vip/addcoupon?idList='+userList+"&couponId="+couponId,
                null,
                function (res) {
                    alert("赠送成功")
                    window.location.reload();

                },
                function (error) {
                    alert(error)
                })
        } else {
            if (couponId==0){
                alert("请选择优惠券")
                window.location.reload();
            } else {
                alert("请选择想要赠送的会员")
                window.location.reload();
            }
        }
        })


    function getVipConsumebyMoney(money) {
        getRequest(
            '/vip/getVipConsumebyMoney/'+money,
            function (res) {
                renderVipConsumeList(res.content);
            },
            function (error) {
                alert(error);
            }
        );

    }


    //渲染满足条件的用户
    function renderVipConsumeList(vipConsumes) {
        $(".content-activity").empty();
        var str = "";
        vipConsumes.forEach(function (vipConsume) {
            str+=
                "<div class='activity-container'>" +
                "    <div class='activity-card card'>" +
                "       <div class='activity-line'>" +
                "           <span class='title'>用户信息</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>会员id："+vipConsume.vipId+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>消费总额："+vipConsume.consume+"</span>" +
                "       </div>" +
                "       <input  class='cbx' type='checkbox' name='user' value='"+vipConsume.userId+"'>"+
                "     </div>" +
                "</div>";
        });
        $(".content-activity").append(str);
    }


    function getCoupon() {
        getRequest(
            '/coupon/getall',
            function (res) {
                renderCouponList(res.content);
            },
            function (error) {
                alert(error);
            }
        );

    }


    //渲染优惠券
    function renderCouponList(coupons) {
        $(".content-coupon").empty();
        var str = "";
        coupons.forEach(function (coupon) {
            str+=
                "<div class='activity-container'>" +
                "    <div class='activity-card card'>" +
                "       <div class='activity-line'>" +
                "           <span class='title'>优惠券信息</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>优惠券描述："+coupon.description+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>优惠券使用门槛："+coupon.targetAmount+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>优惠金额："+coupon.discountAmount+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>有效日期：" + formatDate(coupon.startTime) + " ~ " + formatDate(coupon.endTime) + "</span>" +
                "       </div>" +
                "<input type='radio' name='coupon' value='"+coupon.id+"'>"+

                "     </div>" +
                "</div>";

        });
        if (str==""){
            str="<p>无可用的优惠券</p>"
        }
        $(".content-coupon").append(str);

    }


    function formatDate(date) {
        return date.substring(5, 10).replace("-", ".");
    }



});