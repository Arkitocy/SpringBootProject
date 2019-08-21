$(function () {
    //base64加密
    function Base64() {
        // private property
        _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        // public method for encoding
        this.encode = function (input) {
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            input = _utf8_encode(input);
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output +
                    _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                    _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
            }
            return output;
        }
        // public method for decoding
        this.decode = function (input) {
            var output = "";
            var chr1, chr2, chr3;
            var enc1, enc2, enc3, enc4;
            var i = 0;
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
            while (i < input.length) {
                enc1 = _keyStr.indexOf(input.charAt(i++));
                enc2 = _keyStr.indexOf(input.charAt(i++));
                enc3 = _keyStr.indexOf(input.charAt(i++));
                enc4 = _keyStr.indexOf(input.charAt(i++));
                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;
                output = output + String.fromCharCode(chr1);
                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }
            }
            output = _utf8_decode(output);
            return output;
        }
        // private method for UTF-8 encoding
        _utf8_encode = function (string) {
            string = string.replace(/\r\n/g, "\n");
            var utftext = "";
            for (var n = 0; n < string.length; n++) {
                var c = string.charCodeAt(n);
                if (c < 128) {
                    utftext += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                } else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
            }
            return utftext;
        }
        // private method for UTF-8 decoding
        _utf8_decode = function (utftext) {
            var string = "";
            var i = 0;
            var c = c1 = c2 = 0;
            while (i < utftext.length) {
                c = utftext.charCodeAt(i);
                if (c < 128) {
                    string += String.fromCharCode(c);
                    i++;
                } else if ((c > 191) && (c < 224)) {
                    c2 = utftext.charCodeAt(i + 1);
                    string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                    i += 2;
                } else {
                    c2 = utftext.charCodeAt(i + 1);
                    c3 = utftext.charCodeAt(i + 2);
                    string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                    i += 3;
                }
            }
            return string;
        }
    }
    var base = new Base64();
    $("#regidter").attr('disabled', true);
    $("#namep").hide();

    //验证用户名是否合格
    $("#register-username").blur(function () {
        var name = $("#register-username").val();
        if (this.value.length == 0) {
            $("#namepp").show();
            $("#regidter").attr('disabled', true);
        } else if (name.length > 0) {
            $("#namepp").hide();
            $("#regidter").attr('disabled', false);
        }
        if (/^[0-9a-zA-Z_]{1,30}$/.test(name)) {
            $("#namep").hide();
            $("#regidter").attr('disabled', false);
        } else if (name.length > 30) {
            $("#namep").show();
            $("#regidter").attr('disabled', true);
        }
        var username = base.encode(name);
        $.getJSON("user/checkName/" + username, function (json) {
            var rs = json.result + "";
            console.log(rs);
            if (rs == 'false') {
                $("#nameppp").hide();
                $("#regidter").attr('disabled', false);
            } else {
                $("#nameppp").show();
                $("#regidter").attr('disabled', true);
            }
        });
    });
    //验证邮箱是否合格
    $("#register-email").blur(function () {
        var email = $("#register-email").val();
        var checkEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (checkEmail.test(email)) {
            $("#emailp").hide();
            $("#regidter").attr('disabled', false);
        } else {
            $("#emailp").show();
            $("#regidter").attr('disabled', true);
        }
        var emailcode = base.encode(email);
        $.getJSON("user/checkEmail/" + emailcode, function (json) {
            var rs = json.result + "";
            if (rs == 'false') {
                $("#emailpp").hide();
                $("#regidter").attr('disabled', false);
            } else {
                $("#emailpp").show();
                $("#regidter").attr('disabled', true);
            }
        });

    })
    //验证密码
    $("#register-password1").blur(function () {
        var pwd = $("#register-password1").val();
        if (/^[0-9a-zA-Z_]{6,15}$/.test(pwd)) {
            $("#pwdp").hide();
            $("#regidter").attr('disabled', false);
        } else {
            $("#pwdp").show();
            $("#regidter").attr('disabled', true);

        }
    });
    //重验密码
    $("#register-password2").blur(function () {
        var pwd2 = $("#register-password2").val();
        if (pwd2 != $("#register-password1").val()) {
            $("#pwd3p").show();
            $("#regidter").attr('disabled', true);
        } else if (pwd2 == $("#register-password1").val()) {
            $("#pwd3p").hide();
            $("#regidter").attr('disabled', false);
        }
        if (pwd2.length == 0) {
            $("#pwd2p").show();
            $("#regidter").attr('disabled', true);
        } else if (pwd2.length > 0) {
            $("#pwd2p").hide();
            $("#regidter").attr('disabled', false);
        }
    });
    //验证是否允许
    if (document.getElementById("register-agree").checked == false) {
        $("#regidter").attr('disabled', true);
    } else {
        $("#regidter").attr('disabled', false);
    }
    $("#register-agree").click(function () {
        if (document.getElementById("register-agree").checked == true) {
            $("#regidter").attr('disabled', false);
        } else {
            $("#regidter").attr('disabled', true);
        }
    })

    //注册
    $("#regidter").click(function () {
        console.log($("#register-username").val());
        var username = base.encode($("#register-username").val());
        var password = base.encode($("#register-password1").val());
        var email = base.encode($("#register-email").val());
        console.log(username);
        var adata = {
            "username": username,
            "password": password,
            "email": email
        }
        console.log(adata);
        var data = JSON.stringify(adata);
        console.log(data);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            data: data,
            url: "user/register",
            success: function (res) {
                console.log(res);
                if (res != "" && res == "success") {
                    alert("注册成功");
                    window.location.href = "login.html";
                } else {
                    alert("注册失败");
                    window.location.href = "register.html";
                }
            },
            error: function () {
                alert("失败");
                window.location.href = "register.html";
            }
        });
    })

})
