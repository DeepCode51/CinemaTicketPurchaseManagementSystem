//验证用户身份
function init_manage(){
    var user_json = JSON.parse(localStorage.getItem("userJson"));
    var user_role = user_json.user_role;  //身份验证
    var nav = $(".nav").find("ul");
    var html;
    if(user_json.user_role == 1){
        html = "<li class=\"layui-nav-item\"><a href=\"/jsp/manage/user.jsp\" onclick=\"managePage()\">管理</a></li>"
        nav.append(html);
    }
    function managePage(){
        localStorage.setItem("cardId",0);
    }
}
//初始化
function initHeader(){
    var LayuiNavMore = $(".header-li");
    console.log(LayuiNavMore);
    var user_json = JSON.parse(localStorage.getItem("userJson"));
    console.log(user_json);
    layui.use('element', function(){
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        //监听导航点击
        element.on('nav(demo)', function(elem){
            //console.log(elem)
            layer.msg(elem.text());
        });
    });
    if(user_json == null){
        LayuiNavMore.append(
            "<a href=\"javascript:;\" ><img src=\"../static/images/head.jpg\" class=\"layui-nav-img\"></a>" +
            "<dl class=\"layui-nav-child nav-image\">" +
                "<dd><a href=\"./login.jsp\">登录</a></dd>" +
            "</dl>"
        );
    }
    else{
        var HeadImg = "";
    	if(user_json.user_headImg == null || typeof user_json.user_headImg == "undefined"){
            HeadImg = "../upload/head/demo.jpg";
        }else{
            HeadImg = user_json.user_headImg;
        }
        LayuiNavMore.append(
            "<a href=\"javascript:;\" ><img src=\"" + HeadImg + "\" class=\"layui-nav-img\"></a>" +
            "<dl class=\"layui-nav-child nav-image\">" +
            "<dd><a href=\"/jsp/center.jsp\" onclick=\"mycenter()\">我的订单</a></dd>" +
            "<hr/>" +
            "<dd><a href=\"/jsp/center.jsp\" onclick=\"myinformation()\">基本信息</a></dd>" +
                "<hr/>" +
                "<dd><a onclick=\"ReLogin()\" style=\"text-decoration: none; cursor: pointer;\">注销</a></dd>" +
                "<hr/>" +
            "</dl>"
        );
        init_manage();
    }

}

//初始化
/*function initHeader(){
    var LayuiNavMore = $(".header-li");
    console.log(LayuiNavMore);
    var user_json = JSON.parse(localStorage.getItem("userJson"));
    console.log(user_json);
    layui.use('element', function(){
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        //监听导航点击
        element.on('nav(demo)', function(elem){
            //console.log(elem)
            layer.msg(elem.text());
        });
    });
    if(user_json == null){
        LayuiNavMore.append(
            "<a href=\"javascript:;\" style=\"padding: 0;height: 42px; width: 42px;\"><img src=\"../static/images/head.jpg\" class=\"layui-nav-img\"></a>" +
            "<dl class=\"layui-nav-child nav-image\">" +
                "<dd><a href=\"./login.jsp\">登录</a></dd>" +
            "</dl>"
        );
    }
    else{
        var HeadImg = "";
    	if(user_json.user_headImg == null || typeof user_json.user_headImg == "undefined"){
            HeadImg = "../upload/head/demo.jpg";
        }else{
            HeadImg = user_json.user_headImg;
        }
        LayuiNavMore.append(
            "<a href=\"javascript:;\" style=\"padding: 0;height: 42px; width: 42px;\"><img src=\"" + HeadImg + "\" class=\"layui-nav-img\"></a>" +
            "<dl class=\"layui-nav-child nav-image\">" +
            "<dd><a href=\"/jsp/center.jsp\" onclick=\"mycenter()\">我的订单</a></dd>" +
            "<hr/>" +
            "<dd><a href=\"/jsp/center.jsp\" onclick=\"myinformation()\">基本信息</a></dd>" +
                "<hr/>" +
                "<dd><a onclick=\"ReLogin()\" style=\"text-decoration: none; cursor: pointer;\">注销</a></dd>" +
                "<hr/>" +
            "</dl>"
        );
        init_manage();
    }

}*/
function mycenter(){
    localStorage.setItem("usercardId",0);
}
function myinformation(){
    localStorage.setItem("usercardId",1);
}
//注销
function ReLogin(){
    layui.use(['layer'], function(){
    var layer = layui.layer;
        layer.alert('确认要注销吗？',{icon: 0,offset: clientHeight/5},
            function (){
                $.ajax({
                    type:'post',
                    url: url + "/user",
                    dataType:'json',
                    data: {method:'logout'},
                    success:function (obj) {
                        localStorage.removeItem('userJson');
                        layer.closeAll();
                        window.location.href = "/jsp/mainPage.jsp";
                    }
                });
            }
        );
    });
}