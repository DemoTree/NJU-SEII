var colors = [
    '#FF6666',
    '#3399FF',
    '#FF9933',
    '#66cccc',
    '#FFCCCC',
    '#9966FF',
    'steelblue'
];

$(document).ready(function() {
    var hallId,
        scheduleDate = formatDate(new Date()),
        schedules = [];

    initSelectAndDate();

    function getSchedules() {

        getRequest(
            '/schedule/search?hallId='+hallId+'&startDate='+scheduleDate.replace(/-/g,'/'),
            function (res) {
                schedules = res.content;
                renderScheduleTable(schedules);
             },
            function (error) {
                alert(JSON.stringify(error));
             }
        );
    }

    function renderScheduleTable(schedules){
        $('.schedule-date-container').empty();
        $(".schedule-time-line").siblings().remove();
        schedules.forEach(function (scheduleOfDate) {
            $('.schedule-date-container').append("<div class='schedule-date'>"+formatDate(new Date(scheduleOfDate.date))+"</div>");
            var scheduleDateDom = $(" <ul class='schedule-item-line'></ul>");
            $(".schedule-container").append(scheduleDateDom);
            scheduleOfDate.scheduleItemList.forEach(function (schedule,index) {
                var scheduleStyle = mapScheduleStyle(schedule);
                var scheduleItemDom =$(
                    "<li id='schedule-"+ schedule.id +"' class='schedule-item' data-schedule='"+JSON.stringify(schedule)+"' style='background:"+scheduleStyle.color+";top:"+scheduleStyle.top+";height:"+scheduleStyle.height+"'>"+
                    "<span>"+schedule.movieName+"</span>"+
                    "<span class='error-text'>¥"+schedule.fare+"</span>"+
                    "<span>"+formatTime(new Date(schedule.startTime))+"-"+formatTime(new Date(schedule.endTime))+"</span>"+
                    "</li>");
                scheduleDateDom.append(scheduleItemDom);
            });
        })
    }

    function mapScheduleStyle(schedule) {
        var start = new Date(schedule.startTime).getHours()+new Date(schedule.startTime).getMinutes()/60,
            end = new Date(schedule.endTime).getHours()+new Date(schedule.endTime).getMinutes()/60 ;
        return {
            color: colors[schedule.movieId%colors.length],
            top: 40*start+'px',
            height: 40*(end-start)+'px'
        }
    }

    function initSelectAndDate() {
        $('#schedule-date-input').val(scheduleDate);
        getCinemaHalls();
        getAllMovies();
        
        // 过滤条件变化后重新查询
        $('#hall-select').change (function () {
            hallId=$(this).children('option:selected').val();
            getSchedules();
        });
        $('#schedule-date-input').change(function () {
            scheduleDate = $('#schedule-date-input').val();
            getSchedules();
        });
    }

    function getCinemaHalls() {
        var halls = [];
        getRequest(
            '/hall/all',
            function (res) {
                halls = res.content;
                hallId = halls[0].id;
                halls.forEach(function (hall) {
                    $('#hall-select').append("<option value="+ hall.id +">"+hall.name+"</option>");
                    $('#schedule-hall-input').append("<option value="+ hall.id +">"+hall.name+"</option>");
                    $('#schedule-edit-hall-input').append("<option value="+ hall.id +">"+hall.name+"</option>");
                });
                getSchedules();
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function getAllMovies() {
        getRequest(
            '/movie/all/exclude/off',
            function (res) {
                var movieList = res.content;
                movieList.forEach(function (movie) {
                    $('#schedule-movie-input').append("<option value="+ movie.id +">"+movie.name+"</option>");
                    $("#schedule-edit-movie-input").append("<option value="+ movie.id +">"+movie.name+"</option>");
                });
            },
            function (error) {
                alert(error);
            }
        );
    }

    $(document).on('click','.schedule-item',function (e) {
        var schedule = JSON.parse(e.target.dataset.schedule);
        $("#schedule-edit-hall-input").children('option[value='+schedule.hallId+']').attr('selected',true);
        $("#schedule-edit-movie-input").children('option[value='+schedule.movieId+']').attr('selected',true);
        $("#schedule-edit-start-date-input").val(schedule.startTime.slice(0,16));
        $("#schedule-edit-end-date-input").val(schedule.endTime.slice(0,16));
        $("#schedule-edit-price-input").val(schedule.fare);
        $('#scheduleEditModal').modal('show');
        $('#scheduleEditModal')[0].dataset.scheduleId = schedule.id;
        console.log(schedule);
    });
    
    $('#schedule-form-btn').click(function () {
        var form = {
            hallId: $("#schedule-hall-input").children('option:selected').val(),
            movieId : $("#schedule-movie-input").children('option:selected').val(),
            startTime: $("#schedule-start-date-input").val(),
            endTime: $("#schedule-end-date-input").val(),
            fare: $("#schedule-price-input").val()
        };
        //todo 需要做一下表单验证？
        if(!validSchedule(form)) {
            return;
        }

        postRequest(
            '/schedule/add',
            form,
            function (res) {
                if(res.success){
                    getSchedules();
                    $("#scheduleModal").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    function validSchedule(form){
        var isValidate = true;
        //有值
        if(!form.hallId) {
            isValidate = false;
            $('#schedule-movie-input').parent('.form-group').addClass('has-error');
            alert("影厅不存在!");
        }
        if(!form.movieId) {
            isValidate = false;
            $('#schedule-hall-input').parent('.form-group').addClass('has-error');
            alert("电影不存在!");
        }
        //当前时间
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth()+1;
        month = month < 10 ? "0" + month : month;
        var day = now.getDate();
        day = day < 10 ? "0" + day : day;
        var hour = now.getHours();
        var min = now.getMinutes();
        var time = year + "-" + month + "-" + day + "T" + hour + ":" + min;
        //开始时间不能早于当前时间
        if(!form.startTime) {
            isValidate = false;
            $('#schedule-start-date-input').parent('.form-group').addClass('has-error');
            alert("开始时间不存在!");
        }
        else if(form.startTime<time){
            isValidate = false;
            $('#schedule-start-date-input').parent('.form-group').addClass('has-error');
            alert("排片日期不能早于当前时间!");
        }
        //结束时间不能早于开始时间，排片时间不能跨天
        if(!form.endTime) {
            isValidate = false;
            $('#schedule-end-date-input').parent('.form-group').addClass('has-error');
            alert("结束时间不存在!");
        }
        else if(form.endTime<form.startTime) {
            isValidate = false;
            $('#schedule-end-date-input').parent('.form-group').addClass('has-error');
            alert("结束时间不能早于开始时间!");
        }
        //票价为非负整数
        var regFare = /^\d+(\.{0,1}\d+){0,1}$/;
        if(!form.fare) {
            isValidate = false;
            $('schedule-price-input').parent('.form-group').addClass('has-error');
            alert("票价不存在!");
        }
        else if(!regFare.test(form.fare)) {
            isValidate = false;
            $('schedule-price-input').parent('.form-group').addClass('has-error');
            alert("票价应为非负数!");
        }

        return isValidate;
    }

    $('#schedule-edit-form-btn').click(function () {
        var form = {
            id: Number($('#scheduleEditModal')[0].dataset.scheduleId),
            hallId: $("#schedule-edit-hall-input").children('option:selected').val(),
            movieId : $("#schedule-edit-movie-input").children('option:selected').val(),
            startTime: $("#schedule-edit-start-date-input").val(),
            endTime: $("#schedule-edit-end-date-input").val(),
            fare: $("#schedule-edit-price-input").val()
        };
        //todo 需要做一下表单验证？
        if(!validScheduleEdit(form)) {
            return;
        }

        postRequest(
            '/schedule/update',
            form,
            function (res) {
                if(res.success){
                    getSchedules();
                    $("#scheduleEditModal").modal('hide');
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    function validScheduleEdit(form){
        var isValidate = true;

        //有值
        if(!form.hallId) {
            isValidate = false;
            $('#schedule-edit-movie-input').parent('.form-group').addClass('has-error');
            alert("影厅不存在!");
        }
        if(!form.movieId) {
            isValidate = false;
            $('#schedule-edit-hall-input').parent('.form-group').addClass('has-error');
            alert("电影不存在!");
        }
        //当前时间
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth()+1;
        month = month < 10 ? "0" + month : month;
        var day = now.getDate();
        day = day < 10 ? "0" + day : day;
        var hour = now.getHours();
        var min = now.getMinutes();
        var time = year + "-" + month + "-" + day + "T" + hour + ":" + min;
        //开始时间不能早于当前时间
        if(!form.startTime) {
            isValidate = false;
            $('#schedule-edit-start-date-input').parent('.form-group').addClass('has-error');
            alert("开始时间不存在!");
        }
        else if(form.startTime<time){
            isValidate = false;
            $('#schedule-edit-start-date-input').parent('.form-group').addClass('has-error');
            alert("排片日期不能早于当前时间!");
        }
        //结束时间不能早于开始时间，排片时间不能跨天
        if(!form.endTime) {
            isValidate = false;
            $('#schedule-edit-end-date-input').parent('.form-group').addClass('has-error');
            alert("结束时间不存在!");
        }
        else if(form.endTime<form.startTime) {
            isValidate = false;
            $('#schedule-edit-end-date-input').parent('.form-group').addClass('has-error');
            alert("排片时间不能早于电影上映时间!");
        }
        //票价为非负整数
        var regFare = /^\d+(\.{0,1}\d+){0,1}$/;
        if(!form.fare) {
            isValidate = false;
            $('schedule-edit-price-input').parent('.form-group').addClass('has-error');
            alert("票价不存在!");
        }else if(!regFare.test(form.fare)) {
            isValidate = false;
            $('schedule-edit-price-input').parent('.form-group').addClass('has-error');
            alert("票价应为非负数!");
        }

        return isValidate;
    }

    $("#schedule-edit-remove-btn").click(function () {
        var r=confirm("确认要删除该排片信息吗")
        if (r) {
            deleteRequest(
                '/schedule/delete/batch',
                {scheduleIdList:[Number($('#scheduleEditModal')[0].dataset.scheduleId)]},
                function (res) {
                    if(res.success){
                        getSchedules();
                        $("#scheduleEditModal").modal('hide');
                    } else{
                        alert(res.message);
                    }
                },
                function (error) {
                    alert(JSON.stringify(error));
                }
            );
        }
    })

});

