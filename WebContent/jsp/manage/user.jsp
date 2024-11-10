<%@page import="com.movie.bean.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	User user = (User)request.getSession().getAttribute("user");
	if(user == null){
		response.sendRedirect("../mainPage.jsp");
	}else{
	    if(user.getUser_role() != 1){
	        response.sendRedirect("../mainPage.jsp");
	    }
	}
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>后台管理</title>
</head>
<body>
    <!-- ------------------------------------------------------------------- -->
    <!-- 导航栏 -->
    <jsp:include page="../header.jsp"/>


    <!-- 主体 -->
    <div class="container">
        <div class="contents">
            <div class="nav-next">
                <div class="nav-title">
                    <h3>后台管理</h3>
                </div>
                <a class="cardId" href="/jsp/manage/user.jsp">用户管理</a>
                <a class="cardId" href="/jsp/manage/movie.jsp">电影管理</a>
                <a class="cardId" href="/jsp/manage/cinema.jsp">影院管理</a>
                <a class="cardId" href="/jsp/manage/hall.jsp">放映厅管理</a>
                <a class="cardId" href="/jsp/manage/schedule.jsp">场次管理</a>
                <a class="cardId" href="/jsp/manage/comment.jsp">评论管理</a>
                <a class="cardId" href="/jsp/manage/order.jsp">订单管理</a>
            </div>
            <div class="nav-body">
                <!-- 用户管理 -->
                <div class="one card" style="display: block;">
                    <div>
                        <div class="title">用户管理</div>
                        <hr/>
                    </div>
                    <!-- 用户列表 -->
                    <div class="userlist">
                        <table class="layui-hide" id="user_table_id" lay-filter="UserTable" style="margin-right: 5%;"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>

    <script type="text/html" id="userbar">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">保存</a>
    </script>
    <script type="text/html" id="usertoolbar">
        <div class="layui-btn-container adduserbtn">
            <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="useradd">添加用户</button>     
        </div>
        <div class="usercheck">
            <input id="userfindtext" type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入用户账号" class="layui-input">
            <button class="layui-btn layui-btn-sm" lay-event="finduserbtn">搜索</button> 
        </div>
    </script>
    <script>
        var clientHeight = document.documentElement.clientHeight;
        var AddUserHtml;
        var type = [];
        window.onload = function(){
        	initHeader();
            initHtml(); //初始化html
            initUser(); //用户界面
        }

        //初始化html
        function initHtml(){
            AddUserHtml =
            "<h3 class=\"addusertitle\">用户信息</h3>" +
            "<div class=\"layui-form-item\">" +
                "<label class=\"layui-form-label\">账号</label>" +
                "<div class=\"layui-input-block addusertext\">" +
                    "<input id=\"user_name\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"username\" class=\"layui-input\">" +
                "</div>" +
            "</div>" +
            "<div class=\"layui-form-item\">" +
                "<label class=\"layui-form-label\">密码</label>" +
                "<div class=\"layui-input-block addusertext\">" +
                    "<input id=\"user_pwd\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"password\" class=\"layui-input\">" +
                "</div>" +
            "</div>" +
            "<div class=\"layui-form-item\">" +
                "<label class=\"layui-form-label\">邮箱</label>" +
                "<div class=\"layui-input-block addusertext\">" +
                    "<input id=\"user_email\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"email\" class=\"layui-input\">" +
                "</div>" +
            "</div>";
        }

        //初始化用户管理界面
        function initUser(){
            var actionUrl = "";
            //用户列表
            layui.use(['laypage', 'layer', 'table'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                var table = layui.table;
                table.render({
                    elem: '#user_table_id'
                    ,url: url + '/user'
                    ,where: {
                    	method:'findAllUser',
                    }
                    ,method: 'post'
                    ,toolbar: '#usertoolbar'
                    ,defaultToolbar: []
                    ,title: '用户列表'
                    ,cols: [[
                        {field:'user_id', title:'用户ID', width:102, unresize: true, sort: true}
                        ,{field:'user_name', title:'用户账号', width:150, unresize: true,sort: true}
                        ,{field:'user_pwd', title:'密码', width:150, unresize: true, edit: "text"}
                        ,{field:'user_email', title:'邮箱', width:270, unresize: true, sort: true, edit: "text"}
                        ,{title:'操作', width:150, unresize: true, align:'center', toolbar: '#userbar'}
                    ]]
                    ,page: {layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                        ,curr: 1 //设定初始在第 1 页
                        ,groups: 5 //只显示 5 个连续页码
                        ,first: false //显示首页
                        ,last: false //显示尾页
                        ,limits: [10,15,20]
                    }
                    ,response: {
                        statusCode: 0 //重新规定成功的状态码为 200，table 组件默认为 0
                    }
                    ,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
                        return {
                            "code": res.code, //解析接口状态
                            "msg": res.msg, //解析提示文本
                            "count": res.count, //解析数据长度
                            "data": res.data //解析数据列表
                        };
                    }
                });
                //监听工具条
                table.on('tool(UserTable)', function(obj){
                    var data = obj.data;
                    if(obj.event === 'detail'){
                        layer.msg('ID：'+ data.user_id + '</br>账号：'+ data.user_name + '</br>密码：'+ data.user_pwd  + '</br>邮箱：'+ data.user_email, {offset: clientHeight/4,area: '300px;'});
                    }
                    else if(obj.event === 'edit'){
                        layer.alert('确定要对id为“'+ JSON.stringify(data.user_id) + '”的用户修改进行保存吗？',{icon: 0,offset: clientHeight/5},
                            function () {
                                $.ajax({
                                    type:'post',
                                    url: url + "/user",
                                    dataType:'json',
                                    data: {
                                    	method:'updateUser',
                                        user_id: data.user_id,
                                        user_name: data.user_name,
                                        user_pwd: data.user_pwd,
                                        user_email: data.user_email,
                                    },
                                    success:function (date) {
                                        if(date.state == "success"){
                                            layer.alert('修改成功！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                    location.reload();
                                                }
                                            );
                                        }else{
                                            layer.alert('用户名已存在，修改失败！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                }
                                            );
                                        }
                                    }
                                });
                            }
                        );
                    }
                });
                //监听头工具条
                table.on('toolbar(UserTable)', function(obj){
                    var checkStatus = table.checkStatus(obj.config.id);
                    switch(obj.event){
                        case 'useradd':
                            //添加用户
                            layer.open({
                                type: 1
                                ,title: "添加用户" //不显示标题栏
                                ,closeBtn: false
                                ,area: '400px;'
                                ,shade: 0.8
                                ,offset: clientHeight/5
                                ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                                ,btn: ['确认添加', '取消']
                                ,yes: function(){
                                    var user_name = $('#user_name').val(),
                                        user_pwd = $('#user_pwd').val()
                                        user_email = $('#user_email').val();
                                    if((user_name == "") || (user_pwd == "") || (user_email == "")){
                                        layer.alert('添加信息不能存在空，添加失败！',{icon: 0,offset: clientHeight/5},
                                            function (){
                                                layer.close(layer.index);
                                            }
                                        );
                                    }
                                    else{
                                        $.ajax({
                                            type:'post',
                                            url: url + "/user",
                                            dataType:'json',
                                            data: {
                                            	method:'register',
                                                user_name: user_name,
                                                user_pwd: user_pwd,
                                                user_email: user_email,
                                            },
                                            success:function (date) {
                                                if(date.state == "success"){
                                                    layer.alert('添加成功！',{icon: 0,offset: clientHeight/5},
                                                        function (){
                                                            layer.closeAll();
                                                            location.reload();
                                                        }
                                                    );
                                                }else{
                                                    layer.alert('用户名已存在，添加失败！',{icon: 0,offset: clientHeight/5},
                                                        function (){
                                                            layer.closeAll();
                                                        }
                                                    );
                                                }
                                            }
                                        });
                                    }

                                }
                                ,btnAlign: 'c'
                                ,moveType: 0 //拖拽模式，0或者1
                                ,content: AddUserHtml
                                ,success: function(layero){
                                    var btn = layero.find('.layui-layer-btn');
                                    btn.find('.layui-layer-btn0').attr({
                                    });
                                }
                            });
                        break;
                        case 'finduserbtn':
                            var user_name = $('#userfindtext').val();
                            table.reload('user_table_id', {
                                url: url + '/user'
                                ,method: "POST"
                                ,where: {
                                	method:'findUserInfosByName',
                                    user_name: user_name
                                }
                                ,page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        break;
                            
                    };
                });
            });
        }


        function sortUp(a,b){
                return a-b;
         }
    </script>
    <!-- ------------------------------------------------------------------- -->
</body>
</html>