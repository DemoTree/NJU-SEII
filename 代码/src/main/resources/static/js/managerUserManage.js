$(document).ready(function () {
    getUserList();

    /**
     * 请求已有管理员列表
     * @author zzy
     * @date 6/10
     */
    function getUserList() {
        getRequest(
            '/manager/user/all',
            function (res) {
                renderUserList(res.content);
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
    function renderUserList(users){
        $('.user-on-list').empty();
        var bodyContent = "";
        users.forEach(function(user){
            bodyContent +=
                "<div class='user-item-container'>"+
                "<li id='user-" + user.id + "' class='user-item' data-user='" + JSON.stringify(user) + "'>" +
                "<div class='user-item-text' ' >" + user.username + "</div>" +
                "<a class='primary-text' id='user-modify-btn' onclick='js_method()'>   修改</a>" +
                "</li>" +
                "</div>";
        });
        $('.user-on-list').append(bodyContent);
    }

    /**
     * 获取管理员表单信息
     * @author zzy
     * @date 6/10
     */
    function getUserForm() {
        return {
            username: $('#user-name-input').val(),
            password: $('#user-password-input').val(),
            secondPassword: $('#user-second-password-input').val(),
            role: "user"
        };
    }

    /**
     * 添加管理员
     * @author zzy
     * @date 6/10
     */
    $("#user-form-btn").click(function () {
        var formData = getUserForm();
        if(!validateUserForm(formData)) {
            return;
        }
        postRequest(
            '/register',
            formData,
            function (res) {
                if (res.success) {
                    getUserList();
                    $("#userModal").modal('hide');
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
    function validateUserForm(data) {
        var isValidate = true;
        if (!data.username || data.username.length < 4 || data.username.length > 10) {
            isValidate = false;
            $('#user-name-input').parent('.input-group').addClass('has-error');
            $('#user-name-error-input').css("visibility", "visible");
            $('#user-name-error-input').text("用户名长度应大于4个字符小于10个字符");
        }
        if (!data.password || data.password.length < 6 || data.password.length > 12) {
            isValidate = false;
            $('#user-password-input').parent('.input-group').addClass('has-error');
            $('#user-password-error-input').css("visibility", "visible");
            $('#user-password-error-input').text("密码长度应大于6个字符小于12个字符");
        }

        if (!data.secondPassword) {
            isValidate = false;
            $('#user-second-password-input').parent('.input-group').addClass('has-error');
            $('#user-second-password-error-input').css("visibility", "visible");
            $('#user-second-password-error-input').text("请再次输入密码");
        } else if (data.secondPassword != data.password) {
            isValidate = false;
            $('#user-second-password-input').parent('.input-group').addClass('has-error');
            $('#user-second-password-error-input').css("visibility", "visible");
            $('#user-second-password-error-input').text("两次输入密码不一致");
        }

        return isValidate;
    }

    $(document).on('click','.user-item',function (e) {
        var user = JSON.parse(e.currentTarget.dataset.user);
        $("#user-edit-name-input").val(user.username);
        $("#user-edit-password-input").val(user.password);
        $('#userEditModal').modal('show');
        $('#userEditModal')[0].dataset.userId = user.id;
    });

    /**
     * 修改管理员
     * @author zzy
     * @date 6/10
     */
    $('#user-edit-form-btn').click(function () {
        var form = {
            id: Number($('#userEditModal')[0].dataset.userId),
            role: "user",
            username: $("#user-edit-name-input").val(),
            password : $("#user-edit-password-input").val()
        };
        if (!validateEdituserForm(form)){
            return
        }

        postRequest(
            '/manager/user/update',
            form,
            function (res) {
                if(res.success){
                    getUserList();
                    $("#userEditModal").modal('hide');
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
    function validateEditUserForm(data) {
        var isValidate = true;
        if (!data.username || data.username.length < 4 || data.username.length > 10) {
            isValidate = false;
            $('#user-edit-name-input').parent('.input-group').addClass('has-error');
            $('#user-edit-name-error-input').css("visibility", "visible");
            $('#user-edit-name-error-input').text("用户名长度应大于4个字符小于10个字符");
        }
        if (!data.password || data.password.length < 6 || data.password.length > 12) {
            isValidate = false;
            $('#user-edit-password-input').parent('.input-group').addClass('has-error');
            $('#user-edit-password-error-input').css("visibility", "visible");
            $('#user-edit-password-error-input').text("密码长度应大于6个字符小于12个字符");
        }
        return isValidate;
    }

    /**
     * 删除管理员
     * @author zzy
     * @date 6/10
     */
    $("#user-edit-remove-btn").click(function () {
        var r=confirm("确认要删除该账户吗");
        if (r) {
            deleteRequest(
                '/manager/user/delete/' + Number($('#userEditModal')[0].dataset.userId),
                null,
                function (res) {
                    if(res.success){
                        getUserList();
                        $("#userEditModal").modal('hide');
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