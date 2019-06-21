
$(document).ready(function() {

    var canSeeDate = 0;

    getCanSeeDayNum();
    getCinemaHalls();
    initType();

    function getCinemaHalls() {
        var halls = [];
        getRequest(
            '/hall/all',
            function (res) {
                halls = res.content;
                renderHall(halls);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function renderHall(halls){
        $('#hall-card').empty();
        var hallDomStr = "";
        halls.forEach(function (hall) {
            var seat = "";
            for(var i =0;i<hall.row;i++){
                var temp = "";
                for(var j =0;j<hall.column;j++){
                    temp+="<div class='cinema-hall-seat'></div>";
                }
                seat+= "<div>"+temp+"</div>";
            }
            var hallDom =
                "<div id='hall-" + hall.id + "' class='hall-item' data-hall='" + JSON.stringify(hall) + "'>" +
                // "<div class='cinema-hall'>" +
                "<div>" +
                "<span class='cinema-hall-name'>"+ hall.name +"</span>" +
                "<span class='cinema-hall-size'>"+ hall.column +'*'+ hall.row +"</span>" +
                "<span class='cinema-hall-type'>"+ "&nbsp&nbsp" + hall.type +"</span>" +
                "<a style='margin-left:20px;' class='primary-text' id='hall-modify-btn' onclick='js_method()'>修改</a>" +
                "<input type=\"number\" class=\"form-control\" id=\"hall-set-input\" style=\"display: none;width: 200px;\">" +
                "</div>" +
                "<div class='cinema-seat'>" + seat +
                "</div>" +
                "</div>";
            hallDomStr+=hallDom;
        });
        $('#hall-card').append(hallDomStr);
    }

    function getCanSeeDayNum() {
        getRequest(
            '/schedule/view',
            function (res) {
                canSeeDate = res.content;
                $("#can-see-num").text(canSeeDate);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    $('#canview-modify-btn').click(function () {
        $("#canview-modify-btn").hide();
        $("#canview-set-input").val(canSeeDate);
        $("#canview-set-input").show();
        $("#canview-confirm-btn").show();
    });

    $('#canview-confirm-btn').click(function () {
        var dayNum = $("#canview-set-input").val();
        // 验证一下是否为数字
        postRequest(
            '/schedule/view/set',
            {day:dayNum},
            function (res) {
                if(res.success){
                    getCanSeeDayNum();
                    canSeeDate = dayNum;
                    $("#canview-modify-btn").show();
                    $("#canview-set-input").hide();
                    $("#canview-confirm-btn").hide();
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    /**
     * 获取影厅种类
     * @author zzy
     * @date 6/3
     */
    function initType() {
        $('#hall-type-input').append("<option>2D厅</option>");
        $('#hall-type-input').append("<option>3D厅</option>");
        $('#hall-type-input').append("<option>IMAX厅</option>");
        $('#hall-edit-type-input').append("<option>2D厅</option>");
        $('#hall-edit-type-input').append("<option>3D厅</option>");
        $('#hall-edit-type-input').append("<option>IMAX厅</option>");
    }

    /**
     * 添加影厅
     * @author:zzy
     */
    $('#hall-form-btn').click(function () {
        var form = {
            name: $('#hall-name-input').val(),
            row: Number($("#hall-row-input").val()),
            column: Number($("#hall-column-input").val()),
            type: string2Int($("#hall-type-input").children('option:selected').val())
        };
        if(!validHall(form)) {
            return;
        }
        postRequest(
            '/hall/add',
            form,
            function (res) {
                if(res.success){
                    getCinemaHalls();
                    $("#hallModal").modal('hide');
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    /**
     * 影厅修改弹窗
     * @author zzy
     * @date 6/3
     */
    $(document).on('click','.hall-item',function (e) {
        var hall = JSON.parse(e.currentTarget.dataset.hall);
        $("#hall-edit-name-input").val(hall.name);
        $("#hall-edit-column-input").val(hall.column);
        $("#hall-edit-row-input").val(hall.row);
        $("#hall-edit-type-input").val(hall.type);
        $('#hallEditModal').modal('show');
        $('#hallEditModal')[0].dataset.hallId = hall.id;
    });

    /**
     * 修改影厅
     * @author zzy
     * @date 6/3
     */
    $('#hall-edit-form-btn').click(function () {
        var form = {
            id: Number($('#hallEditModal')[0].dataset.hallId),
            name: $('#hall-edit-name-input').val(),
            column: Number($("#hall-edit-column-input").val()),
            row: Number($("#hall-edit-row-input").val()),
            type: string2Int($("#hall-edit-type-input").children('option:selected').val())
        };

        if(!validEditHall(form)) {
            return;
        }

        postRequest(
            '/hall/update',
            form,
            function (res) {
                if(res.success){
                    getCinemaHalls();
                    $("#hallEditModal").modal('hide');
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    });

    /**
     * 添加影厅信息表单验证
     * @author:zzy
     */
    function validHall(form) {
        var isValidate = true;
        if(!form.name){
            isValidate = false;
            $('#hall-name-input').parent('.form-group').addClass('has-error');
            alert("请输入正确的影厅名称！");
        }
        if(!form.column){
            isValidate = false;
            $('#hall-column-input').parent('.form-group').addClass('has-error');
            alert("请输入正确的座位数信息！");
        }
        if(!form.row){
            isValidate = false;
            $('#hall-row-input').parent('.form-group').addClass('has-error');
            alert("请输入正确的排数信息！");
        }
        return isValidate;
    }

    /**
     * 修改影厅信息表单验证
     * @author:zzy
     */
    function validEditHall(form) {
        var isValidate = true;
        if(!form.name){
            isValidate = false;
            $('#hall-edit-name-input').parent('.form-group').addClass('has-error');
            alert("请输入正确的影厅名称！");
        }
        if(!form.column){
            isValidate = false;
            $('#hall-edit-column-input').parent('.form-group').addClass('has-error');
            alert("请输入正确的座位数信息！");
        }
        if(!form.row){
            isValidate = false;
            $('#hall-edit-row-input').parent('.form-group').addClass('has-error');
            alert("请输入正确的排数信息！");
        }
        return isValidate;
    }

    /**
     * 删除影厅信息
     * @author zzy
     * @date 6/3
     */
    $("#hall-edit-remove-btn").click(function () {
        var r=confirm("确认要删除该影厅信息吗");
        if (r) {
            console.log([Number($('#hallEditModal')[0].dataset.hallId)])
            deleteRequest(
                '/hall/delete/batch',
                {hallIdList:[Number($('#hallEditModal')[0].dataset.hallId)]},
                function (res) {
                    if(res.success){
                        getCinemaHalls();
                        $("#hallEditModal").modal('hide');
                    } else{
                        alert(res.message);
                    }
                },
                function (error) {
                    alert(JSON.stringify(error));
                }
            );
        }
    });

    function string2Int(type){
        var typeInteger=0;
        switch(type){
            case "2D厅":
                typeInteger=1;
                break;
            case "3D厅":
                typeInteger=2;
                break;
            case "IMAX厅":
                typeInteger=3;
                break;
            default:
                typeInteger=0;
        }
        return typeInteger;
    }
});