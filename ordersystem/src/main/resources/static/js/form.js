$(document).ready(function () {
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

    addressInit('cmbProvince', 'cmbCity', 'cmbArea');

    var base = new Base64();
    var addrid = "";
    var username = "";
    var payway = "";
    var cheap = ""
    $.ajax({
        type: "POST",
        url: "getUser",
        success: function (user) {
            username = base.decode(user.username);
            if (username != "") {
                $("#loginusername").text(username);
                $.ajax({
                    type: "POST",
                    url: "user/getHeadImg/" + user.username,
                    dataType: "json",
                    success: function (json2) {
                        var headimgid = json2.headimgid;
                        $("#headimg1").attr("src", "image/" + headimgid);
                    }
                });
            } else {
                self.location = "login.html";
            }
        }
    })

    $("#logoutbtn").click(function () {
        $.ajax({
            type: "POST",
            url: "logout"
        })
        self.location = "login.html";
    })

    $("#profilebtn").click(function () {
        self.location = "profile.html";
    })


    $("#radioCustom1").click(function () {
        $("#saveaddressmodal").modal("show");
        $("#phone").blur(function () {
            if (!(/^1[3456789]\d{9}$/.test($("#phone").val()))) {
                $("#phonep").show();
            } else {
                $("#phonep").hide();
            }
        });

        $("#savenewaddressbtn").click(function () {
            var mainaddress = $("#cmbProvince").val() + "   " + $("#cmbCity").val() + " " + $("#cmbArea").val();
            var detailaddress = $("#detailaddress").val();
            var phone = $("#phone").val();
            var reciever = $("#receiver").val();
            console.log(detailaddress);
            console.log(mainaddress);
            console.log(phone);
            console.log(reciever);
            if (reciever == null || detailaddress == null || !(/^1[3456789]\d{9}$/.test(phone)) || mainaddress == null) {
                alert("请填写完整信息");
            } else {
                var adata = {
                    "reciever": reciever,
                    "phone": phone,
                    "mainaddress": mainaddress,
                    "detailaddress": detailaddress
                }
                console.log(username);
                var data = JSON.stringify(adata);
                $.ajax({
                    type: "POST",
                    url: "address/addAddress/" + base.encode(username),
                    contentType: "application/json",
                    data: data,
                    success: function (res) {
                        console.log(res.result);
                        if (res.result == "success") {
                            $("#saveaddressmodal").modal("hide");
                            var addr = res.useraddress;
                            addrid = addr.id;
                            $("#newaddrshow").append("收件人:  " + addr.reciever + " 联系电话: " + addr.phone + " 地址:  " + addr.mainaddress + " " + addr.detailaddress)
                        } else {
                            alert("地址存储失败")
                        }
                    }
                })
            }
        })
    })


    $("#radioCustom2").click(function () {
        $.ajax({
            type: "POST",
            url: "address/showAddress/" + base.encode(username),
            dataType: "json",
            success: function (res) {
                console.log(res);
                $("#address").empty();
                for (var i = 0; i < res.length; i++) {
                    $("#address").append(
                        "<option id='" + res[i].id + "'>收件人:    " + res[i].reciever + " 联系电话:   " + res[i].phone + " 地址:    " + res[i].mainaddress + " " + res[i].detailaddress + "</option>"
                    );
                }
            }
        })
    })

    $("#address").change(function () {
        addrid = $("#address").find("option:checked").attr("id");
    })

    $("button[name='postbtn']").click(function () {
            console.log(username)
            console.log(addrid)
            console.log(payway)
        }
    )
    $("#radioCustom3").click(function () {
        payway = "zfb";
    })
    $("#radioCustom4").click(function () {
        payway = "wechat";
    })
    $("#radioCustom5").click(function () {
        $.ajax({
            type: "POST",
            url: "user/findusercheap/" + base.encode(username),
            contentType: "application/json",
            success: function (res) {
                $("#checkcheap").text("你已有抵扣" + res.cheap + "元");
                $("#checkcheap").val(res.cheap);
            }
        })
    })
})