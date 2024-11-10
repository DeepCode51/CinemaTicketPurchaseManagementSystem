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
                <!-- 评论管理 -->
                <div class="four card">
                    <div>
                        <div class="title">评论管理</div>
                        <hr/>
                    </div>
                    <!-- 评论列表 -->
                    <div class="commentlist">
                        <table class="layui-hide" id="comment_table_id" lay-filter="CommentTable" style="margin-right: 5%;"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>

    <!-- ------------------------------------------------------------------- -->

    <!--     评论管理      -->
    <script type="text/html" id="commentbar">
        <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
        <a class="layui-btn layui-btn-xs" lay-event="edit">保存</a>
        <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="delete">删除</a>
    </script>
    <script type="text/html" id="commenttoolbar">
        <div class="commentcheck">
            <input id="commentfindtext" type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入用户账号" class="layui-input">
            <button class="layui-btn layui-btn-sm" lay-event="findcommentbtn">搜索</button>
        </div>
    </script>

    <script>
        var clientHeight = document.documentElement.clientHeight;
        window.onload = function(){
            initHeader();
            initComment(); //评论界面
        }

        //初始化评论管理界面
        function initComment(){
            //评论列表
            layui.use('table', function(){
                var table = layui.table;
                table.render({
                    elem: '#comment_table_id'
                    ,url: url + '/comment'
                    ,where: {
              	      method: 'findAllCommentsPage'
              	    }
                    ,method: 'post'
                    ,toolbar: '#commenttoolbar'
                    ,defaultToolbar: []
                    ,title: '订单列表'
                    ,cols: [[
                        {field:'comment_id', title:'评论编号', width:102, unresize: true, sort: true}
                        ,{field:'comment_user', title:'用户账号', width:100, unresize: true,templet: '<div>{{d.comment_user.user_name}}</div>'}
                        ,{field:'comment_time', title:'评论时间', width:250, unresize: true,templet: "<div>{{layui.util.toDateString(d.comment_time, 'yyyy年MM月dd日 HH:mm:ss')}}</div>"}
                        ,{field:'comment_content', title:'评论内容', width:590, unresize: true, edit: "text"}
                        ,{title:'操作', width:200, unresize: true, align:'center', toolbar: '#commentbar'}
                    ]]
                    ,page: {layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
                        ,curr: 1 //设定初始在第 1 页
                        ,groups: 5 //只显示 5 个连续页码
                        ,first: false //显示首页
                        ,last: false //显示尾页
                        ,limits: [10,15,20]
                    }
                    ,response: {
                        statusCode: 0 //重新规定成功的状态码为 200，table 组件默认为 00
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
                table.on('tool(CommentTable)', function(obj){
                    var data = obj.data;
                    if(obj.event === 'detail'){
                        layer.msg('ID：'+ data.comment_id + '</br>账号：'+ data.comment_user.user_name + '</br>时间：'+ data.comment_time  + '</br>内容：'+ data.comment_content, {offset: clientHeight/4});
                    }
                    else if(obj.event === 'edit'){
                        console.log(obj);
                        layer.alert('确定要对id为“'+ JSON.stringify(data.comment_id) + '”的评论内容修改进行保存吗？',{icon: 0,offset: clientHeight/5},
                            function () {
                                $.ajax({
                                    type:'post',
                                    url: url + "/comment",
                                    dataType:'json',
                                    data: {
                                    	method: 'updateComment',
                                        comment_id: data.comment_id,
                                        comment_content: data.comment_content
                                    },
                                    success:function (date) {
                                        if(date.code == 0){
                                            layer.alert('修改成功！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
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
                    else if(obj.event === 'delete'){
                        layer.alert('确定要对id为“'+ JSON.stringify(data.comment_id) + '”的评论进行删除吗？',{icon: 0,offset: clientHeight/5},
                            function () {
                                $.ajax({
                                    type:'post',
                                    url: url + "/comment",
                                    dataType:'json',
                                    data: {
                                    	method: 'deleteComemnt',
                                        comment_id: data.comment_id,
                                    },
                                    success:function (date) {
                                        if(date.code == 0){
                                            layer.alert('删除成功！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                    location.reload();
                                                }
                                            );
                                        }else{
                                            layer.alert('删除失败！',{icon: 0,offset: clientHeight/5},
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
                table.on('toolbar(CommentTable)', function(obj){
                    var checkStatus = table.checkStatus(obj.config.id);
                    switch(obj.event){
                        case 'findcommentbtn':
                            var user_name = $('#commentfindtext').val();
                            table.reload('comment_table_id', {
                                url: url + '/comment'
                                ,method: "POST"
                                ,where: {
                                	method: 'findAllCommentsPage',
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
</body>
</html>