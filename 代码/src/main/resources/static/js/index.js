$(document).ready(function () {
    /**
     * 实现回车触发登录
     * @author zzy
     * @date 5.23
     */
    $('#login-container').bind('keyup', function(event) {
        if (event.keyCode == "13") {
            $('#login-btn').click();
        }
    });

    /**
     * 修改管理员判断
     * @author zzy
     * @date 6/8
     */
    $("#login-btn").click(function () {
        var formData = getLoginForm();
        if (!validateLoginForm(formData)) {
            return;
        }

        postRequest(
            '/login',
            formData,
            function (res) {
                if (res.success) {
                    sessionStorage.setItem('username', formData.username);
                    sessionStorage.setItem('id', res.content.id);
                    console.log(res.content);
                    if (res.content.role == "admin") {
                        sessionStorage.setItem('role', 'admin');
                        window.location.href = "/admin/movie/manage"
                    } else if (res.content.role == "user"){
                        sessionStorage.setItem('role', 'user');
                        window.location.href = "/user/home"
                    }
                    else {
                        sessionStorage.setItem("role", "manager");
                        window.location.href = "/manager/admin"
                    }
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(error);
            });
    });

    function getLoginForm() {
        return {
            username: $('#index-name').val(),
            password: $('#index-password').val()
        };
    }

    function validateLoginForm(data) {
        var isValidate = true;
        if (!data.username) {
            isValidate = false;
            $('#index-name').parent('.input-group').addClass('has-error');
            $('#index-name-error').css("visibility", "visible");
        }
        if (!data.password) {
            isValidate = false;
            $('#index-password').parent('.input-group').addClass('has-error');
            $('#index-password-error').css("visibility", "visible");
        }
        return isValidate;
    }
});