$(document).ready(function () {
    getMovieList();

    function getMovieList() {
        var time=0;
        getRequest(
            "/refund/get",
            function (res) {
                var refund=res.content;
                time=refund.time
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )

        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id'),
            function (res) {
                renderTicketList(res.content,time);
            },
            function (error) {
                alert(error);
            });
    }

    // TODO:填空
    function renderTicketList(list,time) {
        list.forEach(function(ticketInfo){
            getRequest(
                '/schedule/'+ ticketInfo.scheduleId,
                function (res) {
                    renderTicket(res.content,ticketInfo,time);
                },
                function (error) {
                    alert(error) ;
                }
            );
        });
    }

    function renderTicket(schedule, ticketInfo, time){
        var bodyContent="";
        var stateFlag="";
        var date=(new Date()).getTime();
        var start = schedule.startTime;
        start=start.substring(0,19);
        start = start.replace(/-/g,'/');
        start=start.substring(0,10)+" "+start.substring(11,19);
        var starttime = (new Date(start)).getTime();
        var differ=starttime-date;
        var standard=time*60*1000;
        if (ticketInfo.state==1){
            stateFlag="已完成";
        }
        else if(ticketInfo.state==2){
            stateFlag ="已失效";
        }
        else if(ticketInfo.state==3){
            stateFlag="已退票";
        }
        else{
            stateFlag ="待支付";
        }

        if((!ticketInfo.isOut)&&(differ>=standard)&&ticketInfo.state!=3){
            bodyContent += "<tr><td nowrap=\"nowrap\">" + schedule.movieName+ "</td>" +
                "<td nowrap=\"nowrap\">" + schedule.hallName + "</td>" +
                "<td nowrap=\"nowrap\">" + (ticketInfo.rowIndex+1) + "排" + (ticketInfo.columnIndex+1) + "列" + "</td>" +
                "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.startTime).substring(1,11) + " " + JSON.stringify(schedule.startTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.endTime).substring(1,11) + " " + JSON.stringify(schedule.endTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + stateFlag + "</td>" +
                "<td nowrap=\"nowrap\"><button href=\"#\" id=\"out\" onclick='outTicket(\""+ticketInfo.id+"\")'>出票</button><button href=\"#\" id=\"out\" onclick='refundTicket(\""+ticketInfo.id+"\")'>退票</button></td>" +
                "</tr>";
        }
        else if((!ticketInfo.isOut)&&(differ<standard&&differ>=0)){
            bodyContent += "<tr><td nowrap=\"nowrap\">" + schedule.movieName + "</td>" +
                "<td nowrap=\"nowrap\">" + schedule.hallName + "</td>" +
                "<td nowrap=\"nowrap\">" + (ticketInfo.rowIndex+1) + "排" + (ticketInfo.columnIndex+1) + "列" + "</td>" +
                "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.startTime).substring(1,11) + " " + JSON.stringify(schedule.startTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.endTime).substring(1,11) + " " + JSON.stringify(schedule.endTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + stateFlag + "</td>" +
                "<td nowrap=\"nowrap\"><button href=\"#\" id=\"out\" onclick='outTicket(\""+ticketInfo.id+"\")'>出票</button></td>"+
                "</tr>";
        }
        else{
            bodyContent += "<tr><td nowrap=\"nowrap\">" + schedule.movieName + "</td>" +
                "<td nowrap=\"nowrap\">" + schedule.hallName + "</td>" +
                "<td nowrap=\"nowrap\">" + (ticketInfo.rowIndex+1) + "排" + (ticketInfo.columnIndex+1) + "列" + "</td>" +
                "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.startTime).substring(1,11) + " " + JSON.stringify(schedule.startTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.endTime).substring(1,11) + " " + JSON.stringify(schedule.endTime).substring(12,20) + "</td>" +
                "<td nowrap=\"nowrap\">" + stateFlag + "</td>" +
                "</tr>";
        }
        $('#ticket-body').append(bodyContent);
    }
});

function outTicket(ticketId) {
    postRequest(
        "/ticket/out?ticketId="+ticketId,
        null,
        function (res) {
            location.reload();
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    )
}
function refundTicket(ticketId) {
    postRequest(
        "/ticket/refund?ticketId="+ticketId,
        ticketId,
        function (res) {
            location.reload();
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    )}