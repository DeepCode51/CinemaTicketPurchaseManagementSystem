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
                <!-- 放映厅管理 -->
                <div class="six card">
                    <div>
                        <div class="title">放映厅管理</div>
                        <hr/>
                    </div>
                    <!-- 放映厅列表 -->
                    <div class="cinemalist">
                        <table class="layui-hide" id="hall_table_id" lay-filter="HallTable" style="margin-right: 5%;"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>

    <script type="text/html" id="hallbar">
        <a class="layui-btn layui-btn-xs" lay-event="edit">保存</a>
    </script>
    <script type="text/html" id="halltoolbar">
        <div class="layui-btn-container addhallbtn">
            <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="halladd">添加放映厅</button>     
        </div>
    </script>
    <script>
        var clientHeight = document.documentElement.clientHeight;
        var AddHallHtml;
        var type = [];
        var cinemaArr = [];
        var cinemaJsonArr = [];
        window.onload = function(){
        	initHeader();
            initHtml(); //初始化html
            initHall(); //放映厅界面
        }

        //初始化html
        function initHtml(){
            AddHallHtml =
            "<h3 class=\"addtitle\">放映厅信息</h3>" +
            "<div class=\"layui-form-item schedule-div\">" +
                "<label class=\"scheduleLabel\">名称</label>" +
                "<div class=\"layui-input-inline addselect\">" +
                    "<input id=\"hall_name\" type=\"text\" name=\"hall_name\" lay-verify=\"title\" autocomplete=\"off\" class=\"layui-input\">" +
                "</div>" +
            "</div>"+
            "<div class=\"layui-form-item schedule-div\">" +
	            "<label class=\"scheduleLabel\">影院</label>" +
	            "<div class=\"layui-form layui-input-inline addselect drop-cinema\">" + 
	                "<select id=\"select_cinema_name\" name=\"modules\" lay-verify=\"required\" lay-search=\"\" lay-filter=\"selectCinema\">" +
	                    "<option value=''>选择影院</option>" +
	                "</select>" +
	            "</div>" +
	        "</div>"+
	        "<div class=\"layui-form-item schedule-div\">" +
                "<label class=\"scheduleLabel\">容量</label>" +
                "<div class=\"layui-input-inline addselect\">" +
                    "<input id=\"hall_capacity\" type=\"number\" name=\"hall_capacity\" lay-verify=\"title\" autocomplete=\"off\" class=\"layui-input\">" +
                "</div>" +
            "</div>";
        }

        //初始化放映厅管理界面
        function initHall(){
            var actionUrl = "";
            //放映厅列表
            layui.use(['laypage', 'layer', 'table'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                var table = layui.table;
                table.render({
                    elem: '#hall_table_id'
                    ,url: url + '/hall'
                    ,where: {
                	      method: 'findAllHall'
                	    }
                    ,method: 'post'
                    ,toolbar: '#halltoolbar'
                    ,defaultToolbar: []
                    ,title: '放映厅列表'
                    ,cols: [[
                        {field:'hall_id', title:'放映厅ID', width:102, unresize: true, sort: true}
                        ,{field:'hall_name', title:'放映厅名称', width:150, unresize: true,sort: true, edit: "text"}
                        ,{field:'hall_capacity', title:'放映厅容量', width:150, unresize: true,sort: true, edit: "text"}
                        ,{field:'cinema_name', title:'影院名称', width:270, unresize: true,sort: true, templet:'<div>{{d.hall_cinema.cinema_name}}</div>'}
                        ,{title:'操作', width:150, unresize: true, align:'center', toolbar: '#hallbar'}
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
                table.on('tool(HallTable)', function(obj){
                    var data = obj.data;
                   if(obj.event === 'edit'){
                        layer.alert('确定要对id为“'+ JSON.stringify(data.hall_id) + '”的放映厅修改进行保存吗？',{icon: 0,offset: clientHeight/5},
                            function () {
                                $.ajax({
                                    type:'post',
                                    url: url + "/hall",
                                    dataType:'json',
                                    data: {
                                    	method:"updateHall",
                                    	hall_id: data.hall_id,
                                        hall_name: data.hall_name,
                                        hall_capacity: data.hall_capacity
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
                                            layer.alert('修改失败！',{icon: 0,offset: clientHeight/5},
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
                table.on('toolbar(HallTable)', function(obj){
                    var checkStatus = table.checkStatus(obj.config.id);
                    switch(obj.event){
                        case 'halladd':
                            //添加放映厅
                            layer.open({
                                type: 1
                                ,title: "添加放映厅" //不显示标题栏
                                ,closeBtn: false
                                ,area: '400px;'
                                ,shade: 0.8
                                ,offset: clientHeight/5
                                ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                                ,btn: ['确认添加', '取消']
                                ,yes: function(){
                                    var hall_name = $('#hall_name').val();
                                    var select_cinema_name = $('#select_cinema_name').val();
                                    var hall_capacity = $('#hall_capacity').val();
                                    if(hall_name == "" || select_cinema_name == "" || hall_capacity== ""){
                                        layer.alert('添加信息不能存在空，添加失败！',{icon: 0,offset: clientHeight/5},
                                            function (){
                                                layer.close(layer.index);
                                            }
                                        );
                                    }
                                    else{
                                        $.ajax({
                                            type:'post',
                                            url: url + "/hall",
                                            dataType:'json',
                                            data: {
                                            	method:"add",
                                            	hall_name: hall_name,
                                            	cinema_id: select_cinema_name,
                                            	hall_capacity: hall_capacity
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
                                                    layer.alert(date.msg,{icon: 0,offset: clientHeight/5},
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
                                ,content: AddHallHtml
                                ,success: function(layero){
                                    var btn = layero.find('.layui-layer-btn');
                                    btn.find('.layui-layer-btn0').attr({
                                    });
                                    
                                    $.ajax({
                                        type:'post',
                                        url: url + "/hall",
                                        dataType:'json',
                                        data: {
                                        	method:"findAllCinema"
                                        },
                                        success:function (objs) {
                                            cinemaArr = objs.cinema;
                                        	for(var i=0;i<cinemaArr.length;i++){
                                                $("#select_cinema_name").append(new Option(cinemaArr[i].value,cinemaArr[i].id));
                                                layui.form.render("select");
                                            }
                                        }
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
    </script>
</body>
</html>