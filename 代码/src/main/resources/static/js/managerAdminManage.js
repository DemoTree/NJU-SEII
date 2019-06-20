$(document).ready(function () {
    getAdminList();

    /**
     * 请求已有管理员列表
     * @author zzy
     * @date 6/10
     */
    function getAdminList() {
        getRequest(
            '/manager/admin/all',
            function (res) {
                renderAdminList(res.content);
            },
            function (error) {
                alert(error);
            });
    }

    /**
     * 渲染管理员列表
     * @author zzy
     * @date 6/10
     */
    function renderAdminList(admins){
        $('.admin-on-list').empty();
        var bodyContent = "";
        admins.forEach(function(admin){
            bodyContent +=
                "<div class='admin-item-container'>"+
                "<li id='admin-" + admin.id + "' class='admin-item' data-user='" + JSON.stringify(admin) + "'>" +
                "<div class='admin-item-text' ' >" + admin.username + "</div>" +
                "<a class='primary-text' id='admin-modify-btn' onclick='js_method()'>   修改</a>" +
                "</li>" +
                "</div>";
        });
        $('.admin-on-list').append(bodyContent);
    }

    /**
     * 获取管理员表单信息
     * @author zzy
     * @date 6/10
     */
    function getAdminForm() {
        return {
            username: $('#admin-name-input').val(),
            password: $('#admin-password-input').val(),
            secondPassword: $('#admin-second-password-input').val(),
            role: "admin"
        };
    }

    /**
     * 添加管理员
     * @author zzy
     * @date 6/10
     */
    $("#admin-form-btn").click(function () {
        var formData = getAdminForm();
        if(!validateAdminForm(formData)) {
            return;
        }
        postRequest(
            '/register',
            formData,
            function (res) {
                if (res.success) {
                    getAdminList();
                    $("#adminModal").modal('hide');
                }
                else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(error);
            });
    });


    /**
     * 添加管理员表单验证
     * @author zzy
     * @date 6/10
     */
    function validateAdminForm(data) {
        var isValidate = true;
        if (!data.username || data.username.length < 4 || data.username.length > 10) {
            isValidate = false;
            $('#admin-name-input').parent('.input-group').addClass('has-error');
            $('#admin-name-error-input').css("visibility", "visible");
            $('#admin-name-error-input').text("用户名长度应大于4个字符小于10个字符");
        }
        if (!data.password || data.password.length < 6 || data.password.length > 12) {
            isValidate = false;
            $('#admin-password-input').parent('.input-group').addClass('has-error');
            $('#admin-password-error-input').css("visibility", "visible");
            $('#admin-password-error-input').text("密码长度应大于6个字符小于12个字符");
        }

        if (!data.secondPassword) {
            isValidate = false;
            $('#admin-second-password-input').parent('.input-group').addClass('has-error');
            $('#admin-second-password-error-input').css("visibility", "visible");
            $('#admin-second-password-error-input').text("请再次输入密码");
        } else if (data.secondPassword != data.password) {
            isValidate = false;
            $('#admin-second-password-input').parent('.input-group').addClass('has-error');
            $('#admin-second-password-error-input').css("visibility", "visible");
            $('#admin-second-password-error-input').text("两次输入密码不一致");
        }

        return isValidate;
    }

    $(document).on('click','.admin-item',function (e) {
        var user = JSON.parse(e.currentTarget.dataset.user);
        $("#admin-edit-name-input").val(user.username);
        $("#admin-edit-password-input").val(user.password);
        $('#adminEditModal').modal('show');
        $('#adminEditModal')[0].dataset.userId = user.id;
    });

    /**
     * 修改管理员
     * @author zzy
     * @date 6/10
     */
    $('#admin-edit-form-btn').click(function () {
        var form = {
            id: Number($('#adminEditModal')[0].dataset.userId),
            role: "admin",
            username: $("#admin-edit-name-input").val(),
            password : $("#admin-edit-password-input").val()
        };
        if (!validateEditAdminForm(form)){
            return
        }

        postRequest(
            '/manager/admin/update',
            form,
            function (res) {
                if(res.success){
                    getAdminList();
                    $("#adminEditModal").modal('hide');
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
     * 修改管理员表单验证
     * @author zzy
     * @date 6/10
     */
    function validateEditAdminForm(data) {
        var isValidate = true;
        if (!data.username || data.username.length < 4 || data.username.length > 10) {
            isValidate = false;
            $('#admin-edit-name-input').parent('.input-group').addClass('has-error');
            $('#admin-edit-name-error-input').css("visibility", "visible");
            $('#admin-edit-name-error-input').text("用户名长度应大于4个字符小于10个字符");
        }
        if (!data.password || data.password.length < 6 || data.password.length > 12) {
            isValidate = false;
            $('#admin-edit-password-input').parent('.input-group').addClass('has-error');
            $('#admin-edit-password-error-input').css("visibility", "visible");
            $('#admin-edit-password-error-input').text("密码长度应大于6个字符小于12个字符");
        }
        return isValidate;
    }

    /**
     * 删除管理员
     * @author zzy
     * @date 6/10
     */
    $("#admin-edit-remove-btn").click(function () {
        var r=confirm("确认要删除该账户吗");
        if (r) {
            deleteRequest(
                '/manager/admin/delete/' + Number($('#adminEditModal')[0].dataset.userId),
                null,
                function (res) {
                    if(res.success){
                        getAdminList();
                        $("#adminEditModal").modal('hide');
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
});