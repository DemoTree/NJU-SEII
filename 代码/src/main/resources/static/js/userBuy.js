$(document).ready(function () {
    getMovieList();

    function getMovieList() {
        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id'),
            function (res) {
                renderTicketList(res.content);
            },
            function (error) {
                alert(error);
            });
    }

    // TODO:填空
    function renderTicketList(list) {
        list.forEach(function(ticketInfo){
            getRequest(
                '/schedule/'+ ticketInfo.scheduleId,
                function (res) {
                    renderTicket(res.content,ticketInfo);
                },
                function (error) {
                    alert(error) ;
                }
            );
        });
    }

    function renderTicket(schedule, ticketInfo){
        var bodyContent="";
        var stateFlag="";
        if (ticketInfo.state==1){
            stateFlag="已完成";
        }
        else{
            stateFlag ="已失效";
        }

        bodyContent += "<tr><td nowrap=\"nowrap\">" + schedule.movieName + "</td>" +
            "<td nowrap=\"nowrap\">" + schedule.hallName + "</td>" +
            "<td nowrap=\"nowrap\">" + (ticketInfo.rowIndex+1) + "排" + (ticketInfo.columnIndex+1) + "列" + "</td>" +
            "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.startTime).substring(1,11) + " " + JSON.stringify(schedule.startTime).substring(12,20) + "</td>" +
            "<td nowrap=\"nowrap\">" + JSON.stringify(schedule.endTime).substring(1,11) + " " + JSON.stringify(schedule.endTime).substring(12,20) + "</td>" +
            "<td nowrap=\"nowrap\">" + stateFlag + "</td>" +
            "</tr>";
        $('#ticket-body').append(bodyContent);
    }


});