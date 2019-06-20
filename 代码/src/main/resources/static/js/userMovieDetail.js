var dates;
var movieId;

$(document).ready(function () {
    movieId = parseInt(window.location.href.split('?')[1].split('&')[0].split('=')[1]);

    getSchedule();

    function getSchedule() {
        getRequest(
            '/schedule/search/audience?movieId=' + movieId,
            function (res) {
                if (res.success) {
                    $('#schedule').css("display", "");
                    dates = res.content;
                    repaintScheduleDate(0);
                } else {
                    $('#none-hint').css("display", "");
                }
            },
            function (error) {
                alert(error);
            }
        );
    }

});

function repaintScheduleDate(curDateLoc) {
    var dateContent = ""
    for (var i = 0; i < dates.length; i++) {
        var date = dates[i].date.substring(5, 7) + "月" + dates[i].date.substring(8, 10) + "日";
        if (i == 0) date += "（今天）";
        else if (i == 1) date += "（明天）";
        dateContent += '<li role="presentation" id="schedule-date' + i + '"><a href="#"  onclick="repaintScheduleDate(\'' + i + '\')">' + date + '</a></li>';
    }
    $('#schedule-date').html(dateContent);

    $('#schedule-date' + curDateLoc).addClass("active");
    repaintScheduleBody(curDateLoc);
}

function repaintScheduleBody(curDateLoc) {
    var scheduleItems = dates[curDateLoc].scheduleItemList;

    if (scheduleItems.length == 0) {
        $('#date-none-hint').css("display", "");
    } else {
        $('#date-none-hint').css("display", "none");
    }
    var bodyContent = "";
    for (var i = 0; i < scheduleItems.length; i++) {
        bodyContent += "<tr><td>" + scheduleItems[i].startTime.substring(11, 16) + "</td>" +
            "<td>预计" + scheduleItems[i].endTime.substring(11, 16) + "散场</td>" +
            "<td>" + scheduleItems[i].hallName + "</td>" +
            "<td><b>" + scheduleItems[i].fare.toFixed(2) + "</b></td>" +
            "<td><a class='btn btn-primary' href='/user/movieDetail/buy?id="+movieId+"&scheduleId="+scheduleItems[i].id+"' role='button'>选座购票</a></td></tr>";
    }

    $('#schedule-body').html(bodyContent);
}