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
                <!-- 场次管理 -->
                <div class="three card">
                    <div>
                        <div class="title">场次管理</div>
                        <hr/>
                    </div>
                    <!-- 场次列表 -->
                    <div class="schedulelist">
                        <table class="layui-hide" id="schedule_table_id" lay-filter="ScheduleTable" style="margin-right: 5%;"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>
    <!--     场次管理      -->
    <script type="text/html" id="schedulebar">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">下架</a>
    </script>
    <script type="text/html" id="scheduletoolbar">
        <div class="layui-btn-container addbtn">
            <button class="layui-btn layui-btn-warm layui-btn-sm" lay-event="scheduleadd">添加场次</button>     
        </div>
        <div class="schedulecheck">
            <input id="scheduletext" type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入电影名" class="layui-input">
            <button class="layui-btn layui-btn-sm" lay-event="findschedulebtn">搜索</button> 
        </div>
        <div class="scheduleonall">
            <button class="layui-btn layui-btn-sm" lay-event="scheduleonallbtn">显示上映</button>
        </div>
        <div class="scheduledownall">
            <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="scheduledownallbtn">显示下架</button>
        </div>
    </script>
    <script type="text/html" id="scheduledownbar">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
    </script>

    <script>
        var clientHeight = document.documentElement.clientHeight;
        var addScheduleContent;
        var cinemaJsonArr = [];
        var cinemaArr = [];
        var type = [];
        window.onload = function(){
            initHeader();
            initHtml(); //初始化html
            initSchedule(); //场次界面
        }
        //初始化html
        function initHtml(){
            addScheduleContent =
            "<h3 class=\"addtitle\">场次信息</h3>" +
            "<div class=\"layui-form-item schedule-div aaaaaaa\">" +
                "<label class=\"scheduleLabel\">电影</label>" +
                "<div class=\"layui-form layui-input-inline addselect drop-cinema\" lay-filter=\"selectMovie\">" + 
                    "<select id=\"select_movie_name\" name=\"modules\" lay-verify=\"required\" lay-search=\"\">" +
                        "<option>选择电影</option>" +
                    "</select>" +
                "</div>" +
            "</div>" +
            "<div class=\"layui-form-item schedule-div\">" +
                "<label class=\"scheduleLabel\">影院</label>" +
                "<div class=\"layui-form layui-input-inline addselect drop-cinema\">" + 
                    "<select id=\"select_cinema_name\" name=\"modules\" lay-verify=\"required\" lay-search=\"\" lay-filter=\"selectCinema\">" +
                        "<option >选择影院</option>" +
                    "</select>" +
                "</div>" +
            "</div>" +
            "<div class=\"layui-form-item schedule-div\">" +
                "<label class=\"scheduleLabel\">放映厅</label>" +
                "<div class=\"layui-form layui-input-inline addselect drop-hall\" lay-filter=\"select\">" + 
                    "<select id=\"select_schedule_hall\" name=\"modules\" lay-verify=\"required\" lay-search=\"\">" +
                        "<option>选择放映厅</option>" +
                    "</select>" +
                "</div>" +
            "</div>" +
            "<div class=\"layui-form-item schedule-div\">" +
                "<label class=\"scheduleLabel\">场次时间</label>" +
                "<div class=\"layui-input-inline addselect\">" +
                    "<input type=\"text\" class=\"layui-input\" id=\"schedule_startTime_Text\" placeholder=\"yyyy-MM-dd HH:mm\">" +
                "</div>" +
            "</div>" +
            "<div class=\"layui-form-item schedule-div\">" +
                "<label class=\"scheduleLabel\">价格</label>" +
                "<div class=\"layui-input-inline addselect\">" +
                    "<input id=\"schedule_price_Text\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"money\" class=\"layui-input\">" +
                "</div>" +
            "</div>";
        }

         //初始化场次管理界面
         function initSchedule(){
            var ScheduleStatus=0; //0：上映      1：下架
            //场次列表
            layui.use(['table','form'], function(){
                var table = layui.table;
                var form = layui.form;
                table.render({
                    elem: '#schedule_table_id'
                    ,url: url + '/schedule'
                    ,method: 'post'
                    ,where: {"schedule_state": 1,method: 'findScheduleByMovieName'}
                    ,toolbar: '#scheduletoolbar'
                    ,defaultToolbar: []
                    ,title: '场次列表'
                    ,cols: [[
                        {field:'schedule_id', title:'场次编号', width:100, unresize: true, sort: true}
                        ,{field:'schedule_hall', title:'影院', width:240, unresize: true,templet:'<div>{{d.schedule_hall.hall_cinema.cinema_name}}</div>'}
                        ,{field:'schedule_hall', title:'影院地址', width:300, unresize: true,templet:'<div>{{d.schedule_hall.hall_cinema.cinema_address}}</div>'}
                        ,{field:'schedule_hall', title:'影厅', width:100, unresize: true, templet:'<div>{{d.schedule_hall.hall_name}}</div>'}
                        ,{field:'schedule_movie', title:'电影', width:240, unresize: true,templet:'<div>{{d.schedule_movie.movie_cn_name}}</div>'}
                        ,{field:'schedule_startTime', title:'放映时间', width:180, unresize: true, sort: true}
                        ,{field:'schedule_price', title:'价格(￥)', width:100,align:'center', unresize: true}
                        ,{field:'orderList',title:'订单数量',width:100,unresize:true,align:'center',templet:'<div>{{d.orderList == null?0:d.orderList.length}}</div>'}
                        ,{field:'schedule_remain',title:'剩余票数',width:100,align:'center',unresize:true}
                        ,{field:'orderList',title:'场次收入(￥)',width:120,align:'center',unresize:true,templet:'<div>{{d.orderList == null?0:d.orderList.length * d.schedule_price}}</div>'}
                        ,{title:'操作', width:200, unresize: true, align:'center', toolbar: '#schedulebar'}
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
                            "data": res.data, //解析数据列表
                        };
                    }
                });
                //监听工具条
                table.on('tool(ScheduleTable)', function(obj){
                    var data = obj.data;
                    if(obj.event === 'detail'){
                        console.log(obj);
                        layer.msg('编号：'+ data.schedule_id + '</br>影院：'+ data.schedule_hall.hall_cinema.cinema_name + '&nbsp;&nbsp;&nbsp;放映厅：'+ data.schedule_hall.hall_name  + '</br>影院地址：'+ data.schedule_hall.hall_cinema.cinema_address + '</br>电影：'+ data.schedule_movie.movie_cn_name + '</br>场次：' + data.schedule_startTime + '&nbsp;&nbsp;&nbsp;价格：' + data.schedule_price, {offset: clientHeight/5});
                    }
                    else if(obj.event === 'edit'){
                        layer.alert('确定要对id为“'+ data.schedule_id + '”的场次进行下架吗？',{icon: 0,offset: clientHeight/5},
                            function () {
                                $.ajax({
                                    type:'post',
                                    url: url + "/schedule",
                                    dataType:'json',
                                    data: {
                                    	method: 'offlineSchedule',
                                        schedule_id: data.schedule_id,
                                    },
                                    success:function (result) {
                                        console.log(data.schedule_id);
                                        if(result.code == 0){
                                            layer.alert(result.mgs,{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                    location.reload();
                                                }
                                            );
                                        }else{
                                            layer.alert(result.mgs,{icon: 0,offset: clientHeight/5},
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
                //头工具栏事件
                table.on('toolbar(ScheduleTable)', function(obj){
                    var checkStatus = table.checkStatus(obj.config.id);
                    switch(obj.event){
                        case 'scheduleadd':
                            //示范一个公告层
                            layer.open({
                                type: 1
                                ,title: '添加场次' //不显示标题栏
                                ,closeBtn: false
                                ,area: '400px;'
                                ,shade: 0.8
                                ,offset: clientHeight/10
                                ,id: 'scheduleadd' //设定一个id，防止重复弹出
                                ,btn: ['确认添加', '取消']
                                ,yes: function(){
                                    console.log("queding");
                                    var select_movie_name = $('#select_movie_name').val();
                                    var select_cinema_name = $("#select_cinema_name").val();
                                    var select_schedule_hall = $('#select_schedule_hall').val();
                                    var schedule_startTime_Text = $("#schedule_startTime_Text").val();
                                    var schedule_price_Text = $('#schedule_price_Text').val();
                                    if((select_movie_name == "选择电影") || (select_cinema_name == "选择影院") || (select_schedule_hall == "选择放映厅") ||
                                        (schedule_startTime_Text =="") || (schedule_price_Text =="")){
                                        layer.alert('添加信息不能存在空，添加失败！',{icon: 0,offset: clientHeight/5},
                                            function (){
                                                layer.close(layer.index);
                                            }
                                        );
                                    }
                                    else{
                                        $.ajax({
                                            type:'post',
                                            url: url + "/schedule",
                                            dataType:'json',
                                            data: {
                                            	method: 'addSchedule',
                                                movie_name: select_movie_name,
                                                cinema_name: select_cinema_name,
                                                hall_name: select_schedule_hall,
                                                schedule_startTime: schedule_startTime_Text,
                                                schedule_price: schedule_price_Text,
                                            },
                                            success:function (result) {
                                                if(result.code == 0){
                                                    layer.alert(result.mgs,{icon: 0,offset: clientHeight/5},
                                                        function (){
                                                            layer.closeAll();
                                                            location.reload();
                                                        }
                                                    );
                                                }else{
                                                    layer.alert(result.mgs,{icon: 0,offset: clientHeight/5},
                                                        function (){
                                                            layer.closeAll();
                                                        }
                                                    );
                                                }
                                            }
                                        });
                                    }
                                }
                                ,end: function(){
                                    cinemaJsonArr = [];
                                }
                                ,btnAlign: 'c'
                                ,moveType: 1 //拖拽模式，0或者1
                                ,content: addScheduleContent
                                ,success: function(layero){
                                    layui.use(['form','laydate','layedit'], function(){
                                        var form = layui.form, 
                                        layedit = layui.layedit, 
                                        laydate = layui.laydate;
                                        laydate.render({
                                          elem: '#schedule_startTime_Text'
                                          ,type: 'datetime'
                                          ,format:'yyyy-MM-dd HH:mm'
                                        });
                                        form.render('select' ,'select');
                                    });
                                    $.ajax({
                                        type:'post',
                                        url: url + "/schedule",
                                        dataType:'json',
                                        data: {method: 'findAllSchedule'},
                                        success:function (objs) {
                                            movieArr = objs.movieName;
                                            cinemaArr = objs.cinema;
                                            //解析json数组 简化
                                            for(var i = 0;i < cinemaArr.length;i++){
                                                var cinemaJson = {};
                                                for(var key in cinemaArr[i]){
                                                    var cinemaName = key; 
                                                    var hallArr = [];
                                                    for(var j = 0;j < cinemaArr[i][key].length; j++){
                                                        hallArr.push(cinemaArr[i][key][j].hall_name);
                                                    }
                                                    cinemaJson.cinema = key;
                                                    cinemaJson.hallArr = hallArr;
                                                }
                                                cinemaJsonArr.push(cinemaJson);
                                            }

                                            console.log(cinemaJsonArr);
                                            for(var i=0;i<movieArr.length;i++){
                                               $("#select_movie_name").append(new Option(movieArr[i]));
                                               layui.form.render("select");
                                            }

                                            for(var i=0;i<cinemaJsonArr.length;i++){
                                                $("#select_cinema_name").append(new Option(cinemaJsonArr[i].cinema));
                                                layui.form.render("select");
                                            }
                                        }
                                    });
                                }
                            });
                        break;
                        case 'findschedulebtn':
                            var find_temp = $('#scheduletext').val();
                            //上映
                            if(ScheduleStatus==0){
                                table.reload('schedule_table_id', {
                                    url: url + "/schedule"
                                    ,method: "POST"
                                    ,where: {
                                    	method: 'findScheduleByMovieName',
                                        movie_name: find_temp
                                    }
                                    ,page: {
                                        curr: 1 //重新从第 1 页开始
                                    }
                                });
                            }
                            //下架
                            else{
                                table.reload('schedule_table_id', {
                                    url: url + "/schedule"
                                    ,method: "POST"
                                    ,where: {
                                    	method: 'findOffScheduleByMovieName',
                                        movie_name: find_temp
                                    }
                                    ,page: {
                                        curr: 1 //重新从第 1 页开始
                                    }
                                });
                            }
                        break;
                        case 'scheduleonallbtn':
                            ScheduleStatus = 0;
                            table.render({
                                elem: '#schedule_table_id'
                                ,url: url + '/schedule'
                                ,method: 'post'
                                ,where: {"schedule_state": 1,method: 'findScheduleByMovieName'}
                                ,toolbar: '#scheduletoolbar'
                                ,defaultToolbar: []
                                ,title: '场次列表'
                                ,cols: [[
                                    {field:'schedule_id', title:'场次编号', width:100, unresize: true, sort: true}
                                    ,{field:'schedule_hall', title:'影院', width:240, unresize: true,templet:'<div>{{d.schedule_hall.hall_cinema.cinema_name}}</div>'}
                                    ,{field:'schedule_hall', title:'影院地址', width:300, unresize: true,templet:'<div>{{d.schedule_hall.hall_cinema.cinema_address}}</div>'}
                                    ,{field:'schedule_hall', title:'影厅', width:100, unresize: true, templet:'<div>{{d.schedule_hall.hall_name}}</div>'}
                                    ,{field:'schedule_movie', title:'电影', width:240, unresize: true,templet:'<div>{{d.schedule_movie.movie_cn_name}}</div>'}
                                    ,{field:'schedule_startTime', title:'放映时间', width:180, unresize: true, sort: true}
                                    ,{field:'schedule_price', title:'价格(￥)', width:100,align:'center', unresize: true}
                                    ,{field:'orderList',title:'订单数量',width:100,unresize:true,align:'center',templet:'<div>{{d.orderList.length}}</div>'}
                                    ,{field:'schedule_remain',title:'剩余票数',width:100,align:'center',unresize:true}
                                    ,{field:'orderList',title:'场次收入(￥)',width:120,align:'center',unresize:true,templet:'<div>{{d.orderList.length * d.schedule_price}}</div>'}
                                    ,{title:'操作', width:200, unresize: true, align:'center', toolbar: '#schedulebar'}
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
                                        "data": res.data, //解析数据列表
                                        "income": res.income //解析数据列表
                                    };
                                }
                            });
                        break;
                        case 'scheduledownallbtn':
                            ScheduleStatus = 1;
                            table.render({
                                elem: '#schedule_table_id'
                                ,url: url + '/schedule'
                                ,method: 'post'
                                ,where: {"schedule_state": 0,method: 'findScheduleByMovieName',}
                                ,toolbar: '#scheduletoolbar'
                                	,defaultToolbar: []
                                ,title: '场次列表'
                                ,cols: [[
                                    {field:'schedule_id', title:'场次编号', width:100, unresize: true, sort: true}
                                    ,{field:'schedule_hall', title:'影院', width:150, unresize: true,templet:'<div>{{d.schedule_hall.hall_cinema.cinema_name}}</div>'}
                                    ,{field:'schedule_hall', title:'影院地址', width:250, unresize: true,templet:'<div>{{d.schedule_hall.hall_cinema.cinema_address}}</div>'}
                                    ,{field:'schedule_hall', title:'影厅', width:100, unresize: true, templet:'<div>{{d.schedule_hall.hall_name}}</div>'}
                                    ,{field:'schedule_movie', title:'电影', width:240, unresize: true,templet:'<div>{{d.schedule_movie.movie_cn_name}}</div>'}
                                    ,{field:'schedule_startTime', title:'放映时间', width:180, unresize: true, sort: true}
                                    ,{field:'schedule_price', title:'价格(￥)', width:100,align:'center', unresize: true}
                                    ,{field:'orderList',title:'订单数量',width:100,unresize:true,align:'center',templet:'<div>{{d.orderList.length}}</div>'}
                                    ,{field:'schedule_remain',title:'剩余票数',width:100,align:'center',unresize:true}
                                    ,{field:'orderList',title:'场次收入(￥)',width:120,align:'center',unresize:true,templet:'<div>{{d.orderList.length * d.schedule_price}}</div>'}
                                    ,{title:'操作', width:200, unresize: true, align:'center', toolbar: '#scheduledownbar'}
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
                                        "data": res.data, //解析数据列表
                                        "income": res.income //解析数据列表
                                    };
                                }
                            });
                        break;
                    };
                });
                //监听影院二级联动
                form.on('select(selectCinema)',function(data){
                    $("#select_schedule_hall").empty();
                    layui.form.render("select");
                    for(var i = 0;i < cinemaJsonArr.length;i++){
                        if(cinemaJsonArr[i].cinema == data.value){
                            for(var j=0;j<cinemaJsonArr[i].hallArr.length;j++){
                                $("#select_schedule_hall").append(new Option(cinemaJsonArr[i].hallArr[j]));
                                layui.form.render("select");
                            }
                        }
                    }
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