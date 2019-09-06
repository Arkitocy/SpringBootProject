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

    var base = new Base64();
    var username;
    $.ajax({
        type: "POST",
        url: "getUser",
        success: function (user) {
            username = base.decode(user.username);
            if (username != "") {
                if (user.role == "用户") {
                    self.location = "index.html";
                }
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


    $("#allid").click(function () {
        $("#chossen").text("全部订单");
        document.getElementById("tableid").removeAttribute("hidden");
        getall(0)
    })
    $("#waitpayid").click(function () {
        $("#chossen").text("待付款");
        document.getElementById("tableid").removeAttribute("hidden");
        getstatus("待付款", 0);
        $("#alterbtn").append(
            "<button type='button' class='btn btn-warning'>取消订单</button>"
        )


    })
    $("#waitpostid").click(function () {
        $("#tbodyid").empty();
        $("#chossen").text("待发货");
        getstatus("待发货", 0);
        document.getElementById("tableid").removeAttribute("hidden");
    })
    $("#waitgetid").click(function () {
        $("#tbodyid").empty();
        $("#chossen").text("待收货");
        getstatus("待收货", 0);
        document.getElementById("tableid").removeAttribute("hidden");
    })
    $("#completeid").click(function () {
        $("#tbodyid").empty();
        $("#chossen").text("完成");
        getstatus("完成", 0);
        document.getElementById("tableid").removeAttribute("hidden");
    })
    $("#waitbackid").click(function () {
        $("#tbodyid").empty();
        $("#chossen").text("退款");
        getstatus("退款", 0);
        document.getElementById("tableid").removeAttribute("hidden");
    })


    function getall(pagen) {
        $(".pagination").empty();

        $.ajax({
            type: "POST",
            url: "order/adminall/" + pagen,
            dataType: "json",
            success: function (res) {
                console.log(res.content);
                $("#tbodyid").empty();
                for (var i = 0; i < res.content.length; i++) {

                    $("#tbodyid").append(
                        "<tr><td>" + res.content[i][0]
                        + "</td>"
                        + "<td>" + res.content[i][1]
                        + "</td>"
                        + "<td>" + res.content[i][2]
                        + "</td>"
                        + "<td>" + res.content[i][3]
                        + "</td>"
                        + "<td>" + res.content[i][4]
                        + "</td>"
                        + "<td>" + res.content[i][5]
                        + "</td>"
                        + "<td>" + res.content[i][6]
                        + "</td>"
                        + "<td>" + res.content[i][7]
                        + "</td>"
                        + "<td>" + res.content[i][8]
                        + "</td></tr>"
                    );

                }
                var pagenum = res.totalPages;
                $(".pagination").empty();
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="firstpage">首页</a></li>');
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="previosepage">上一页</a></li>');
                for (var j = 0; j < pagenum; j++) {
                    $(".pagination").append('<li class="page-item" id="page' + j + '"><a class="page-link" href="#">' + (j + 1) + '</a></li>');
                }
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="nextpage">下一页</a></li>');
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="lastpage">尾页</a></li>');
                $(".page-item").removeClass("active");
                $("#page" + pagen).addClass("active");
                $("#nextpage").click(function () {
                    var pagenum1 = Number(pagen) + Number(1);
                    if (pagen < pagenum - 1) {
                        getall(pagenum1);
                    }
                });
                $("#previosepage").click(function () {
                    var pagenum2 = Number(pagen) - Number(1);
                    if (pagen > 0) {
                        getall(pagenum2);
                    }
                });
                $("#lastpage").click(function () {
                    console.log(1);
                    getall(pagenum - 1);
                });
                $("#firstpage").click(function () {
                    console.log(1);
                    getall(0);
                });
                $(".page-item").click(function () {
                    var page1 = this.id.substr(4);
                    getall(page1);
                });
            }
        })
    }


    function getstatus(status, pagen) {
        $(".pagination").empty();

        $.ajax({
            type: "POST",
            url: "order/adminOrderPage/" + status + "/" + pagen,
            dataType: "json",
            success: function (res) {
                console.log(res.content[0][0]);
                $("#tbodyid").empty();
                for (var i = 0; i < res.content.length; i++) {

                    $("#tbodyid").append(
                        "<tr><td>" + res.content[i][0]
                        + "</td>"
                        + "<td>" + res.content[i][1]
                        + "</td>"
                        + "<td>" + res.content[i][2]
                        + "</td>"
                        + "<td>" + res.content[i][3]
                        + "</td>"
                        + "<td>" + res.content[i][4]
                        + "</td>"
                        + "<td>" + res.content[i][5]
                        + "</td>"
                        + "<td>" + res.content[i][6]
                        + "</td>"
                        + "<td>" + res.content[i][7]
                        + "</td>"
                        + "<td>" + res.content[i][8]
                        + "</td></tr>"
                    );

                }
                var pagenum = res.totalPages;
                $(".pagination").empty();
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="firstpage">首页</a></li>');
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="previosepage">上一页</a></li>');
                for (var j = 0; j < pagenum; j++) {
                    $(".pagination").append('<li class="page-item" id="page' + j + '"><a class="page-link" href="#">' + (j + 1) + '</a></li>');
                }
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="nextpage">下一页</a></li>');
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="lastpage">尾页</a></li>');
                $(".page-item").removeClass("active");
                $("#page" + pagen).addClass("active");
                $("#nextpage").click(function () {
                    var pagenum1 = Number(pagen) + Number(1);
                    if (pagen < pagenum - 1) {
                        getstatus(status, pagenum1);
                    }
                });
                $("#previosepage").click(function () {
                    var pagenum2 = Number(pagen) - Number(1);
                    if (pagen > 0) {
                        getstatus(status, pagenum2);
                    }
                });
                $("#lastpage").click(function () {
                    console.log(1);
                    getstatus(status, pagenum - 1);
                });
                $("#firstpage").click(function () {
                    console.log(1);
                    getstatus(status, 0);
                });
                $(".page-item").click(function () {
                    var page1 = this.id.substr(4);
                    getstatus(status, page1);
                });
            }
        })
    }
    $("#searchfinishTime").val(getNowFormatDate())
    $("#searchstartTime").val(getNowFormatDate())
    function getNowFormatDate() {
        var date = new Date();
        var seperator1 = "-";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate;
        return currentdate;
    }
    function selectOrder(pagen){
        $(".pagination").empty();
        $.ajax({
            type: "POST",
            url: "order/adminSelectOrder/"+$("#selectname").val() +"/"+$("#selectstatus").val()+"/"+$("#searchstartTime").val()+"/"+$("#searchfinishTime").val()+"/"+ pagen,
            dataType: "json",
            success: function (res) {
                console.log(res.content);
                $("#tbodyid").empty();
                for (var i = 0; i < res.content.length; i++) {

                    $("#tbodyid").append(
                        "<tr><td>" + res.content[i][0]
                        + "</td>"
                        + "<td>" + res.content[i][1]
                        + "</td>"
                        + "<td>" + res.content[i][2]
                        + "</td>"
                        + "<td>" + res.content[i][3]
                        + "</td>"
                        + "<td>" + res.content[i][4]
                        + "</td>"
                        + "<td>" + res.content[i][5]
                        + "</td>"
                        + "<td>" + res.content[i][6]
                        + "</td>"
                        + "<td>" + res.content[i][7]
                        + "</td>"
                        + "<td>" + res.content[i][8]
                        + "</td></tr>"
                    );
                }
                var pagenum = res.totalPages;
                $(".pagination").empty();
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="firstpage">首页</a></li>');
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="previosepage">上一页</a></li>');
                for (var j = 0; j < pagenum; j++) {
                    $(".pagination").append('<li class="page-item" id="page' + j + '"><a class="page-link" href="#">' + (j + 1) + '</a></li>');
                }
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="nextpage">下一页</a></li>');
                $(".pagination").append('<li class="" ><a class="page-link" href="#" id="lastpage">尾页</a></li>');
                $(".page-item").removeClass("active");
                $("#page" + pagen).addClass("active");
                $("#nextpage").click(function () {
                    var pagenum1 = Number(pagen) + Number(1);
                    if (pagen < pagenum - 1) {
                        selectOrder(pagenum1);
                    }
                });
                $("#previosepage").click(function () {
                    var pagenum2 = Number(pagen) - Number(1);
                    if (pagen > 0) {
                        selectOrder(pagenum2);
                    }
                });
                $("#lastpage").click(function () {
                    console.log(1);
                    selectOrder(pagenum - 1);
                });
                $("#firstpage").click(function () {
                    console.log(1);
                    selectOrder(0);
                });
                $(".page-item").click(function () {
                    var page1 = this.id.substr(4);
                    selectOrder(page1);
                });
            }
        })
    }

    $("#selectbtn").click(function () {
        selectOrder(0);
    })

    $("#selectbtn2").click(function () {
        $("#selectname").text("");
        $("#selectname").val("");
        $("#selectstatus").val("全部")
        $("#selectname").text("全部");

    })
})





