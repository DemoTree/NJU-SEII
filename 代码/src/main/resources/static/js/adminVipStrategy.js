var whichtype;

$(document).ready(function() {


    getStrategys();

    function getStrategys() {
        getRequest(
            '/vip/getLastVipStrategy',
            function (res) {
                var strategys = res.content;
                renderStrategys(strategys);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function renderStrategys(strategys) {
        $(".content-activity").empty();
        var activitiesDomStr = "";

        strategys.forEach(function (strategy) {
            var level;
            switch (strategy.viptype) {
                case 1:level="青铜会员";break;
                case 2:level="白银会员";break;
                case 3:level="黄金会员";break;
                case 4:level="白金会员";break;
                case 5:level="钻石会员";break;
                default:level="至尊黑客卡";

            }

            activitiesDomStr+=
                "<div class='activity-container'>" +
                "    <div class='activity-card card'>" +
                "       <div class='activity-line'>" +
                "           <span class='title'>"+level+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>充值基础金额："+strategy.basis+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>充值赠送金额："+strategy.addition+"</span>" +
                "       </div>" +
                "       <div class='activity-line'>" +
                "           <span>升级标准："+strategy.cell+"</span>" +
                "       </div>" +
                "    </div>" +
                "<button id= \"vipstrategy"+strategy.viptype+"\" type=\"button\" class=\"btn btn-primary\" data-backdrop=\"static\" data-toggle=\"modal\" data-target=\"#activityModal2\">修改优惠策略</button>"+
                "</div>";
        });
        $(".content-activity").append(activitiesDomStr);

        strategys.forEach(function (strategy) {
            str="vipstrategy"+strategy.viptype;
            document.getElementById(str).addEventListener("click", function (e) {
                whichtype=this.id.replace("vipstrategy","");

            })
        })






    }



    $("#activity-form-btn").click(function () {
        var form = {
            basis: $("#activity-description-input").val(),
            addition: $("#activity-start-date-input").val(),
            cell:$("#activity-end-date-input").val(),
        };

        postRequest(
            '/vip/addVipStrategy',
            form,
            function (res) {
                if(res.success){
                    getStrategys();
                    $("#activityModal").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });












    $("#activity-form-btn2").click(function (e) {

        var form = {
            basis: $("#activity-description-input2").val(),
            addition: $("#activity-start-date-input2").val(),
            cell:$("#activity-end-date-input2").val(),
            viptype:Number(whichtype),
        };
        postRequest(
            '/vip/updateVipStrategy?basis='+form.basis+'&addition='+form.addition+'&cell='+form.cell+'&viptype='+form.viptype,
            null,
            function (res) {
                if(res.success){
                    getStrategys();
                    $("#activityModal2").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });


























    //ES6新api 不重复集合 Set
    var selectedMovieIds = new Set();
    var selectedMovieNames = new Set();

    $('#activity-movie-input').change(function () {
        var movieId = $('#activity-movie-input').val();
        var movieName = $('#activity-movie-input').children('option:selected').text();
        if(movieId==-1){
            selectedMovieIds.clear();
            selectedMovieNames.clear();
        } else {
            selectedMovieIds.add(movieId);
            selectedMovieNames.add(movieName);
        }
        renderSelectedMovies();
    });

    //渲染选择的参加活动的电影
    function renderSelectedMovies() {
        $('#selected-movies').empty();
        var moviesDomStr = "";
        selectedMovieNames.forEach(function (movieName) {
            moviesDomStr += "<span class='label label-primary'>"+movieName+"</span>";
        });
        $('#selected-movies').append(moviesDomStr);
    }



});
