$(document).ready(function () {
    getRechangeRecord();

    function getRechangeRecord() {
        getRequest(
            '/vip/getRechangeRecord/' + sessionStorage.getItem('id'),
            function (res) {
                renderRechangeRecordList(res.content);
            },
            function (error) {
                alert(error);
            });
    }

    // TODO:填空
    function renderRechangeRecordList(list) {
        list.forEach(function(rechangeRecord){
            var bodyContent="";
            bodyContent += "<tr><td nowrap=\"nowrap\">" + JSON.stringify(rechangeRecord.rechangeTime).substring(1,11) + " " + JSON.stringify(rechangeRecord.rechangeTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + rechangeRecord.amountOfMoney + "</td>" +
                "<td nowrap=\"nowrap\">" + rechangeRecord.bonus+ "</td>" +
                "<td nowrap=\"nowrap\">" + rechangeRecord.balance + "</td>" +
                "<td nowrap=\"nowrap\">" + rechangeRecord.consumeCardId + "</td>" +
                "</tr>";
            $('#rechangeRecord-body').append(bodyContent);


        });
    }



});