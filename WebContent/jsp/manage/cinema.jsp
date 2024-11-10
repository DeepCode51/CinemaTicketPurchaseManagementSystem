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
                <!-- 影院管理 -->
                <div class="six card">
                    <div>
                        <div class="title">影院管理</div>
                        <hr/>
                    </div>
                    <!-- 影院列表 -->
                    <div class="cinemalist">
                        <table class="layui-hide" id="cinema_table_id" lay-filter="CinemaTable" style="margin-right: 5%;"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>

    <script type="text/html" id="cinemabar">
        <a class="layui-btn layui-btn-xs" lay-event="edit">保存</a>
    </script>
    <script type="text/html" id="cinematoolbar">
        <div class="layui-btn-container addcinemabtn">
            <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="cinemaadd">添加影院</button>     
        </div>
    </script>
    <script>
        var clientHeight = document.documentElement.clientHeight;
        var AddCinemaHtml;
        var type = [];
        window.onload = function(){
        	initHeader();
            initHtml(); //初始化html
            initCinema(); //影院界面
        }

        //初始化html
        function initHtml(){
            AddCinemaHtml =
            "<h3 class=\"addusertitle\">影院信息</h3>" +
            "<div class=\"layui-form-item\">" +
                "<label class=\"layui-form-label\">名称</label>" +
                "<div class=\"layui-input-block addusertext\">" +
                    "<input id=\"cinema_name\" type=\"text\" name=\"cinema_name\" lay-verify=\"title\" autocomplete=\"off\" class=\"layui-input\">" +
                "</div>" +
            "</div>"+
            "<div class=\"layui-form-item\">" +
	            "<label class=\"layui-form-label\">地址</label>" +
	            "<div class=\"layui-input-block addusertext\">" +
	                "<input id=\"cinema_address\" type=\"text\" name=\"cinema_address\" lay-verify=\"title\" autocomplete=\"off\" class=\"layui-input\">" +
	            "</div>" +
	        "</div>";
        }

        //初始化影院管理界面
        function initCinema(){
            var actionUrl = "";
            //影院列表
            layui.use(['laypage', 'layer', 'table'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                var table = layui.table;
                table.render({
                    elem: '#cinema_table_id'
                    ,url: url + '/cinema'
                    ,method: 'get'
                   	,where: {
            	      method: 'findAllCinema'
            	    }
                    ,toolbar: '#cinematoolbar'
                    ,defaultToolbar: []
                    ,title: '影院列表'
                    ,cols: [[
                        {field:'cinema_id', title:'影院ID', width:102, unresize: true, sort: true}
                        ,{field:'cinema_name', title:'影院名称', width:150, unresize: true,sort: true, edit: "text"}
                        ,{field:'cinema_address', title:'影院地址', width:270, unresize: true,sort: true, edit: "text"}
                        ,{title:'操作', width:150, unresize: true, align:'center', toolbar: '#cinemabar'}
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
                table.on('tool(CinemaTable)', function(obj){
                    var data = obj.data;
                   if(obj.event === 'edit'){
                        layer.alert('确定要对id为“'+ JSON.stringify(data.cinema_id) + '”的影院修改进行保存吗？',{icon: 0,offset: clientHeight/5},
                            function () {
                                $.ajax({
                                    type:'post',
                                    url: url + "/cinema",
                                    dataType:'json',
                                    data: {
                                    	method:'updateCinema',
                                    	cinema_id: data.cinema_id,
                                        cinema_name: data.cinema_name,
                                        cinema_address: data.cinema_address
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
                                            layer.alert('影院名已存在，修改失败！',{icon: 0,offset: clientHeight/5},
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
                table.on('toolbar(CinemaTable)', function(obj){
                    var checkStatus = table.checkStatus(obj.config.id);
                    switch(obj.event){
                        case 'cinemaadd':
                            //添加影院
                            layer.open({
                                type: 1
                                ,title: "添加影院" //不显示标题栏
                                ,closeBtn: false
                                ,area: '400px;'
                                ,shade: 0.8
                                ,offset: clientHeight/5
                                ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                                ,btn: ['确认添加', '取消']
                                ,yes: function(){
                                    var cinema_name = $('#cinema_name').val();
                                    var cinema_address = $('#cinema_address').val();
                                    if(cinema_name == "" || cinema_address == ""){
                                        layer.alert('添加信息不能存在空，添加失败！',{icon: 0,offset: clientHeight/5},
                                            function (){
                                                layer.close(layer.index);
                                            }
                                        );
                                    }
                                    else{
                                        $.ajax({
                                            type:'post',
                                            url: url + "/cinema",
                                            dataType:'json',
                                            data: {
                                            	method:'add',
                                            	cinema_name: cinema_name,
                                            	cinema_address: cinema_address
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
                                                    layer.alert('影院名已存在，添加失败！',{icon: 0,offset: clientHeight/5},
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
                                ,content: AddCinemaHtml
                                ,success: function(layero){
                                    var btn = layero.find('.layui-layer-btn');
                                    btn.find('.layui-layer-btn0').attr({
                                    });
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
        function datefomate(value) {
            if(value==null || value == undefined){
                return "";
            }
            var date = new Date(value);
            
            Y = date.getFullYear(),
                m = date.getMonth()+1,
                d = date.getDate(),
                H = date.getHours(),
                i = date.getMinutes(),
                s = date.getSeconds();
            return Y+'-'+m+'-'+d;
        };
    </script>
</body>
</html>