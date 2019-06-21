$(document).ready(function() {
    getRefund();
    function getRefund() {
        getRequest(
            "/refund/get",
            function (res) {
                var refund=res.content;
                renderRefund(refund);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    }
    function renderRefund(refund) {
        $(".content-refund").empty();
        var refundDomStr="";
        refundDomStr=
            '<div class="refund-container">' +
            '    <div class="refund-line">' +
            '        <span class="title">退票策略</span>'+
            '    </div>' +
            '    <div class="refund-line">'+
            '        <span>退票时间'+refund.time+'</span>'+
            '    </div>'+
            '    <div class="refund-line">'+
            '        <span>退款比例'+refund.persent+'</span>'+
            '    </div>'
        $(".content-refund").append(refundDomStr);
    }

    $("#activity-form-btn").click(function () {
        var form= {
            time : $("#refund-time-input").val(),
            persent : $("#refund-persent-input").val()
        }
        postRequest(
            "/refund/set",
            form,
            function (res) {
                if(res.success){
                    getRefund();
                    $("#activityModal").modal("hide");
                }
                else{
                    alert(res.message);
                }
            },
            function (res) {
                alert(JSON.stringify(error));
            }
        )
    });
})
