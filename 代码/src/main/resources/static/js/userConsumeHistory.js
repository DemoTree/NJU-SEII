/**
 * @author 蔡明卫
 * @date 6/1
 */

$(document).ready(function () {
    getConsumeHistory();

    function getConsumeHistory() {
        getRequest(
            '/ticket/consumehistory/' + sessionStorage.getItem('id'),
            function (res) {
                renderConsumeHistoryList(res.content);
            },
            function (error) {
                alert(error);
            });
    }

    // 渲染消费记录
    function renderConsumeHistoryList(list) {
        list.forEach(function(consumeRecord){
            console.log(consumeRecord)
            if(consumeRecord.consumeType==2){
                var tag="+  ";
                var strtype="退票";
            }else {
                if(consumeRecord.consumeType==1){
                    var strtype="购买电影票";
                }else {
                    var strtype="购买会员卡";
                }
                var tag="-  ";
            }
            var bodyContent="";
            bodyContent += "<tr><td nowrap=\"nowrap\">" + JSON.stringify(consumeRecord.consumeTime).substring(1,11) + " " + JSON.stringify(consumeRecord.consumeTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" +tag+consumeRecord.amountOfMoney + "</td>" +
                "<td nowrap=\"nowrap\">" + strtype + "</td>" +
                "<td nowrap=\"nowrap\">" + (consumeRecord.consumeWay==0?"会员卡":"银行卡") + "</td>" +
                "<td nowrap=\"nowrap\">" + consumeRecord.consumeCardId + "</td>" +
                "</tr>";
            $('#consumeHistory-body').append(bodyContent);


        });
    }



});