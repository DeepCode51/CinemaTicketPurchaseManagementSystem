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
                <!-- 订单管理 -->
                <div class="five card">
                    <div>
                        <div class="title">订单管理</div>
                        <hr/>
                    </div>
                    <!-- 订单列表 -->
                    <div class="ticketlist">
                        <table class="layui-hide" id="ticket_table_id" lay-filter="TicketTable" style="margin-right: 5%;"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>

    <!--     订单管理      -->
    <script type="text/html" id="ticketbar">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
    </script>
    <script type="text/html" id="tickettoolbar">
        <div class="ticketcheck">
            <input type="button" class="layui-btn-primary layui-btn-sm changeticketBtn" style="width:70px;" lay-event="changeticketbtn" value="用户帐号">
            <input id="ticketfindtext" type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入用户账号" class="layui-input">
            <button class="layui-btn layui-btn-sm" lay-event="findticketbtn">搜索</button>
        </div>
    </script>
    <script type="text/html" id="backticketbar">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="pass">通过</a>
    </script>

    

    <script>
        var clientHeight = document.documentElement.clientHeight;
        window.onload = function(){
            initHeader();
            initTicket();  //订单界面
        }

        //初始化订单管理界面
        function initTicket(){
            var PageStatus=0;
            var actionUrl = "";
            //订单列表
            layui.use('table', function(){
                var table = layui.table;
                table.render({
                    elem: '#ticket_table_id'
                    ,url: url + "/order"
                    ,where: {
              	      method: 'findOrderByUserName'
              	    }
                    ,method: 'post'
                    ,toolbar: '#tickettoolbar'
                    ,defaultToolbar: []
                    ,title: '订单列表'
                    ,cols: [[
                        {field:'order_id', title:'订单编号', width:160,unresize:true,sort: true,fixed:'left'}
                        ,{field:'order_user', title:'用户账号', width:100, unresize: true,templet:'<div>{{d.order_user.user_name}}</div>'}
                        ,{field:'order_schedule', title:'场次', width:180, unresize: true,templet:'<div>{{d.order_schedule.schedule_startTime}}</div>'}
                        ,{field:'order_position',title:'位置',width:100,unresize:true}                       
                        ,{field:'order_schedule',title:'价格',width:80,unresize:true,templet:'<div>￥{{d.order_schedule.schedule_price}}</div>'}
                        ,{field:'order_schedule',title:'电影',width:240,unresize:true,templet:'<div>{{d.order_schedule.schedule_movie.movie_cn_name}}</div>'}
                        ,{field:'order_schedule', title:'影厅', width:100, unresize: true, templet:'<div>{{d.order_schedule.schedule_hall.hall_name}}</div>'}
                        ,{field:'order_schedule',title:'影院',width:240,unresize:true,templet:'<div>{{d.order_schedule.schedule_hall.hall_cinema.cinema_name}}</div>'}
                        ,{field:'order_state',title:'订单状态',width:100,unresize:true,align:'center',templet:function(d){if(d.order_state == 1) return '<div style="color:#337ab7">完成</div>';else if(d.order_state == 0) return '<div style="color:#ef4238">申请退票</div>';else return '<div style="color:#5cb85c">已退票</div>';}}
                        ,{title:'操作', width:100, unresize: true, align:'center', toolbar: '#ticketbar'}
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
                    },
                });
              
                //监听工具条
                table.on('tool(TicketTable)', function(obj){
                    var data = obj.data;
                    if(obj.event === 'detail'){
                        layer.msg('订单编号：'+ data.order_id + '&nbsp;&nbsp;&nbsp;用户：' + data.order_user.user_name + '<br>场次：'+ data.order_schedule.schedule_startTime + '&nbsp;&nbsp;&nbsp;位置：' + data.order_position + '<br>电影：《' + data.order_schedule.schedule_movie.movie_cn_name + '》&nbsp;&nbsp;&nbsp;价格：￥' + data.order_schedule.schedule_price + '<br>影院：'+ data.order_schedule.schedule_hall.hall_cinema.cinema_name + '&nbsp;&nbsp;&nbsp;影厅：' + data.order_schedule.schedule_hall.hall_name, {offset: clientHeight/4,area:['400px','140px']});
                    }
                });
                //监听头工具条
                table.on('toolbar(TicketTable)', function(obj){
                    var checkStatus = table.checkStatus(obj.config.id);
                    var changeticketbtn = $('.changeticketBtn');
                    var ticketfindtext = $('#ticketfindtext');
                    switch(obj.event){
                        case 'findticketbtn':
                            var url_temp;
                            var find_temp = $('#ticketfindtext').val();
                            find_temp = find_temp.toString();
                            //订单管理
                            if(PageStatus == 0){
                                if(changeticketbtn.val()=="订单编号"){
                                    url_temp = '/order';
                                    table.reload('ticket_table_id', {
                                        url: url + url_temp
                                        ,method: "POST"
                                        ,where: {
                                        	method: 'findOrderById',
                                            order_id: find_temp
                                        }
                                        ,page: {
                                            curr: 1 //重新从第 1 页开始
                                        }
                                    });
                                }
                                else{
                                    url_temp = '/order';
                                    table.reload('ticket_table_id', {
                                        url: url + url_temp
                                        ,method: "POST"
                                        ,where: {
                                        	method: 'findOrderByUserName',
                                            user_name: find_temp
                                        }
                                        ,page: {
                                            curr: 1 //重新从第 1 页开始
                                        }
                                    });
                                }
                            }
                        break;
                        case 'changeticketbtn':
                            if(changeticketbtn.val()=="订单编号"){
                                changeticketbtn.val("用户帐号");
                                ticketfindtext.attr("placeholder","请输入用户帐号");
                            }
                            else{
                                changeticketbtn.val("订单编号");
                                ticketfindtext.attr("placeholder","请输入订单编号");
                            }
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