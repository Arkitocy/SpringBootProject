$(document).ready(function () {
    addressInit('cmbProvince', 'cmbCity', 'cmbArea');

    //base64加密 向后端传输的数据统一使用 base.encode()加密 后端传来的数据使用base.decode()解密
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
    var username;
    //页面刷新时从cookie中取出username
    $.ajax({
        type: "POST",
        url: "user/getCookie",
        dataType: "json",
        success: function (data) {
            var loginUsername = base.decode(data.loginUsername)
            if (loginUsername == "") {
                self.location = "login.html";
            }
            $("#loginusername").text(loginUsername);
            username = loginUsername;
            $("#username").attr("value", username);
            $("#username").text(username);
            //根据username查找邮箱
            $.ajax({
                type: "POST",
                url: "user/getEmail/" + data.loginUsername,
                dataType: "json",
                success: function (json) {
                    var email = base.decode(json.email);
                    $("#email").attr("value", email);
                }
            });
            $.ajax({
                type: "POST",
                url: "user/getHeadImg/" + data.loginUsername,
                dataType: "json",
                success: function (json2) {
                    var headimgid = json2.headimgid;
                    console.log(headimgid);
                    $("#headimg1").attr("src", "image/" + headimgid);
                    $("#headimg2").attr("src", "image/" + headimgid);
                }
            });

        },
        error: function () {
            alert("cookies 信息获取失败！");
        }
    })

    //注销
    $("#logoutbtn").click(function () {
        $.ajax({
            type: "POST",
            url: "user/logoutCookie",
            dataType: "json",
            success: function (data) {
                console.log(data);
                self.location = "login.html";
            }
        })
    })
    //进入profile页面
    $("#profilebtn").click(function () {
        self.location = "profile.html";
    })

    //新建收货地址
    $("#saveaddressmodalbtn").click(function () {
        $("#saveaddressmodal").modal("show");
        $("#phone").blur(function () {
            if (!(/^1[3456789]\d{9}$/.test($("#phone").val()))) {
                $("#phonep").show();
            } else {
                $("#phonep").hide();
            }
        });
        $("#savenewaddressbtn").click(function () {
            var mainaddress = $("#cmbProvince").val() + " " + $("#cmbCity").val() + " " + $("#cmbArea").val();
            var userid //依据上面的username去user表找id
            var detailaddress = $("#detailaddress").val();
            var phone = $("#phone").val();
            if (userid == null || detailaddress == null || !(/^1[3456789]\d{9}$/.test(phone)) || mainaddress == null) {
                alert("请填写完整信息");
            } else {
                //用.ajax方法将数据存入数据库中，返回map对象然后重新刷新页面
                console.log(mainaddress);
                console.log(detailaddress);
                console.log(phone);
                console.log(username);
            }
        })
    })
    //判断用户名是否合格
    $("#username").blur(function () {
        var name = $("#username").val();
        if (this.value.length == 0) {
            $("#namepp").show();
            $("#saveallbtn").attr('disabled', true);
        } else if (name.length > 0) {
            $("#namepp").hide();
            $("#saveallbtn").attr('disabled', false);
        }
        if (/^[0-9a-zA-Z_]{1,30}$/.test(name)) {
            $("#namep").hide();
            $("#saveallbtn").attr('disabled', false);
        } else if (name.length > 30) {
            $("#namep").show();
            $("#saveallbtn").attr('disabled', true);
        }
        var username = base.encode(name);
        $.getJSON("user/checkName/" + username, function (json) {
            var rs = json.result + "";
            console.log(rs);
            if (rs == 'false') {
                $("#nameppp").hide();
                $("#saveallbtn").attr('disabled', false);
            } else {
                $("#nameppp").show();
                $("#saveallbtn").attr('disabled', true);
            }
        });
    });
    //判断邮箱是否合格
    $("#email").blur(function () {
        var email = $("#email").val();
        var checkEmail = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (checkEmail.test(email)) {
            $("#emailp").hide();
            $("#saveallbtn").attr('disabled', false);
        } else {
            $("#emailp").show();
            $("#saveallbtn").attr('disabled', true);
        }
        var emailcode = base.encode(email);
        $.getJSON("user/checkEmail/" + emailcode, function (json) {
            var rs = json.result + "";
            if (rs == 'false') {
                $("#emailpp").hide();
                $("#saveallbtn").attr('disabled', false);
            } else {
                $("#emailpp").show();
                $("#saveallbtn").attr('disabled', true);
            }
        });

    })
    //保存用户名以及邮箱
    $("#saveallbtn").click(function () {
        var id;
        console.log(username);
        var uname = base.encode($("#username").val());
        var uemail = base.encode($("#email").val());
        console.log(uname);
        console.log(uemail);
        var adata = {
            "username": uname,
            "email": uemail
        };
        var data = JSON.stringify(adata);
        $.ajax({
            type: "POST",
            url: "user/update/" + base.encode(username),
            contentType: "application/json",
            data: data,
            success: function (res) {
                console.log(res.result);
                //设置cookie
                $.getJSON("user/setCookie", {"username": uname},
                    function (json) {
                        if (json.result == "success") {
                            self.location = "profile.html";
                        } else {
                            alert("添加cookie失败")
                            self.location = "profile.html";
                        }

                    });
            }
        })
    })
    //修改头像
    $("#headimg2").click(function () {
        $('#uploadmodal').modal("show");
        $("button[name='uploadbtn2']").click(function () {
            var formData = new FormData(document.getElementById("upload-form"));
            $.ajax({
                url: "user/uploadheadimg/" + base.encode(username),
                method: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (resp) {
                    if (resp.result == "success") {
                        var headimgid = resp.headimgid;
                        $("#headimg2").attr("src", "image/" + headimgid);
                        $("#headimg1").attr("src", "image/" + headimgid);
                        $('#uploadmodal').modal('hide');
                    } else {
                        alert("上传失败");
                    }
                }
            });

        })
    })
})