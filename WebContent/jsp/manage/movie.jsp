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
                <!-- 电影管理 -->
                <div class="two card">
                    <div>
                        <div class="title">电影管理</div>
                        <hr/>
                    </div>
                    <div class="main-inner">
                        <div class="addMovie">
                            <img alt="" src="${ctx }/static/images/addMovie.png" onclick="addConfirm(-1)">
                            <span>添加电影</span>
                        </div>
                        <div class="movie-grid">
                            <div class="panel-header">
                                <span class="panel-title">
                                </span>
                            </div>
                            <div id="demo0"></div>
                            <div class="panel-content">
                                <ul class="movies-list">
                                </ul>
                            </div>
                            
                        </div>
                        
                    </div>
                </div>
                
            </div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="../footer.jsp"/>

    <script>
        var clientHeight = document.documentElement.clientHeight;
        var MoviesListHtml;
        var AddMoviesHtml;
        var ActorNum = 1;
        var temp, flag;
        var changeticketbtn1 = $('.changeticketBtn');
        var selectmovieName;
        var movieArr = [];
        var movieArray = []; //电影数组
        var movieType = []; //电影类型数组
        var movieTypeBoxOffice = []; //电影类型票房数组
        var type = [];
        var page = 1;
        var limit = 8;
        window.onload = function(){
            initHeader();
            initHtml(); //初始化html
            initMovies(); //电影界面
        }

        //初始化html
        function initHtml(){
            AddMoviesHtml =
            "<h3 class=\"addusertitle\">电影信息</h3>" +
            "<div class=\"textside\">" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">电影名</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input id=\"movie_cn_name\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieCnName\" class=\"layui-input\">" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">电影外名</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input id=\"movie_fg_name\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieFgName\" class=\"layui-input\">" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">导演</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input id=\"movie_director\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieDirector\" class=\"layui-input\">" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\" style=\"display:inline-block;\">演职人员</label>" +
                    "<div class=\"layui-input-block addusertext\" style=\"display:inline-block; margin-left: 10px !important; width: 118px;\">" +
                        "<input id=\"movie_actor1\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieActor1\" class=\"layui-input\">" +
                    "</div>" +
                    "<span style=\"margin-left:10px\">饰</span>" +
                    "<div class=\"layui-input-block addusertext\" style=\"display:inline-block; margin-left: 10px !important; width: 118px;\">" +
                        "<input id=\"movie_role1\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieRole1\" class=\"layui-input\">" +
                    "</div>" +
                    "<button type=\"button\" class=\"layui-btn layui-btn-xs\" onclick=\"addActor()\">+</button>" +
                    "<button type=\"button\" class=\"layui-btn layui-btn-xs\" onclick=\"deleteActor()\">-</button>" +
                "</div>" +
                "<div class=\"layui-form-item temp\">" +
                    "<label class=\"layui-form-label\">电影详情</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<textarea id=\"movie_detail\" placeholder=\"MovieDetail\" class=\"layui-textarea\"></textarea>" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">电影时长</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input id=\"movie_duration\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieDuration\" class=\"layui-input\">" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">电影类型</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input id=\"movie_type\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieType\" class=\"layui-input\">" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">上映时间</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input type=\"text\" class=\"layui-input\" id=\"movie_releaseDate\" autocomplete=\"off\" name=\"movie_releaseDate\" placeholder=\"yyyy-MM-dd\">" +
                    "</div>" +
                "</div>" +
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label\">制片地区</label>" +
                    "<div class=\"layui-input-block addusertext\">" +
                        "<input id=\"movie_country\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieCountry\" class=\"layui-input\">" +
                    "</div>" +
                "</div>" +
            "</div>" +
            "<div class=\"pictureside\">" +
                "<div class=\"layui-upload\">" +
                    "<p class=\"movie-picture\">电影海报</p>" +
                    "<div class=\"layui-upload-list\">" +
                        "<img class=\"layui-upload-img\" id=\"demo1\">" +
                        "<p id=\"demoText\"></p>" +
                    "</div>" +
                    "<a href=\"javascript:;\" class=\"file\">选择文件" +
                        "<input type=\"file\" name=\"file\" id=\"file\">" +
                    "</a>" +
                "</div>" +
            "</div>";
        }

        //初始化电影管理界面
        function initMovies(){
        	
            var MoviesNum = $(".two").find(".panel-title");
            var MovieLi =  $(".two").find(".movies-list");
            MoviesNum.empty();
            MovieLi.empty();
            $.ajax({
                type:'post',
                url: url + "/movie",
                dataType:'json',
                data: {method:'findAllMoviesBack',page:page,limit:limit},
                success:function (obj) {
                    console.log(obj);
                    MoviesNum.append("<span class=\"textcolor_red\">正在热映（" + obj.count + "部）</span>");
                    for(var i=0;i<obj.data.length;i++){
                        MoviesListHtml =
                        "<li>" +
                            "<div class=\"movie-item\">" +
                                "<a href=\"javascript:void(0)\" target=\"_blank\" data-act=\"playingMovie-click\" data-val=\""+ obj.data[i].movie_id +"\">" +
                                    "<div class=\"movie-poster\">" +
                                        "<img src=\""+ obj.data[i].movie_picture +"\" onclick=\"movieDetail("+obj.data[i].movie_id+")\">" +
                                        "<div class=\"movie-overlay movie-overlay-bg\">" +
                                            "<div class=\"movie-info\">" +
                                                "<div class=\"movie-score\"><i class=\"integer\">"+ obj.data[i].movie_score +"</i></div>" +
                                                "<div class=\"movie-title movie-title-padding\" title=\"\">"+ obj.data[i].movie_cn_name +"</div>\"" +
                                            "</div>" +
                                        "</div>" +
                                    "</div>" +
                                "</a>" +
                                "<div class=\"moive-btn\">" +
                                    "<div class=\"movies-detail movie-detail-strong movie-sale\">" +
                                        "<a class=\"active\" onclick=\"addConfirm("+ obj.data[i].movie_id +")\" target=\"_blank\" data-act=\"salePlayingMovie-click\" data-val=\"\">修改</a>" +
                                    "</div>" +
                                    "<div class=\"movies-detail movie-detail-strong movie-sale\">" +
                                        "<span id=\"deleteId\" style=\"display:none;\">${u.id}</span>" +
                                        "<a class=\"active\" onclick=\"deleteConfirm("+ obj.data[i].movie_id +")\" data-act=\"salePlayingMovie-click\" id=\"delete\">删除</a>" +
                                    "</div>" +
                                "</div>" +
                            "</div>" +
                        "</li>";
                        MovieLi.append(MoviesListHtml);
                    }
                    //初始化分页
                    	layui.use(['laypage', 'layer'], function(){
                      	  var laypage = layui.laypage
                      	  ,layer = layui.layer;
                      	  
                      	  //总页数低于页码总数
                      	  laypage.render({
                      	    elem: 'demo0'
                      	    ,limit:limit
                      	    ,curr:page
                      	    ,count: obj.count //数据总数
                      	    ,jump: function(obj, first){
                      	      //obj包含了当前分页的所有参数，比如：
                      	      console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                      	      console.log(obj.limit); //得到每页显示的条数
                      	      page=obj.curr;
                      	      limit=obj.limit;
                      	      
                      	      //首次不执行
                      	      if(!first){
                      	    	initMovies()
                      	      }
                      	    }
                      	  });
                      })
                }
            });


        }
        function movieDetail(movie_id){
            window.open("/jsp/movieDetail.jsp?movie_id=" + movie_id);
        }
        //电影添加&修改点击事件
        function addConfirm(movie_id){
            var file;
            var formData = new FormData();
            var method;
            var tempurl;
            //添加电影
            if(movie_id == -1){
            	method = "addMovie";
                tempurl = "/movie";
                temp = "添加";
                flag = 0;
            }
            //修改电影
            else{
            	method = "updateMovie";
                tempurl = "/movie";
                temp = "修改";
                flag = 1;
            }
            layui.use(['laypage', 'layer', 'table','laydate'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                var table = layui.table;
                var laydate = layui.laydate;
                laydate.render({
                    elem: '#movie_releaseDate'
                    ,type: 'date'
                    ,format:'yyyy-MM-dd'
                  });
                //电影添加
                layer.open({
                    type: 1
                    ,title: temp + "电影" //不显示标题栏
                    ,closeBtn: false
                    ,area: '750px;'
                    ,shade: 0.8
                    ,offset: clientHeight/20
                    ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                    ,btn: ['确认' + temp, '取消']
                    ,yes: function(){
                        var movie_cn_name = $('#movie_cn_name').val(),
                            movie_fg_name = $('#movie_fg_name').val(),
                            movie_director = $('#movie_director').val(),
                            movie_actor = "",
                            movie_detail = $('#movie_detail').val(),
                            movie_duration = $('#movie_duration').val(),
                            movie_type = $('#movie_type').val(),
                            movie_releaseDate = $('#movie_releaseDate').val(),
                            movie_country = $('#movie_country').val();
                        for(var i = 1;i<(ActorNum+1);i++){
                            movie_actor += $("#movie_actor" + i).val() + ":";
                            if(i==ActorNum){
                                movie_actor += $("#movie_role" + i).val();
                            }
                            else{
                                movie_actor += $("#movie_role" + i).val() + ",";
                            }
                        }
                        if((movie_cn_name == "") || (movie_director == "") || (movie_actor == "") ||
                            (movie_detail == "") || (movie_duration == "") || (movie_type == "") || 
                            (movie_fg_name == "") || (movie_releaseDate == "") || (movie_country == "")){
                            layer.alert(temp + '信息不能存在空，' + temp + '失败！',{icon: 0,offset: clientHeight/5},
                                function (){
                                    layer.close(layer.index);
                                }
                            );
                            return;
                        }
                        //添加
                        if(flag == 0){
                            if(file == null){
                                layer.alert('图片信息不能存在空，' + temp + '失败！',{icon: 0,offset: clientHeight/5},
                                    function (){
                                        layer.close(layer.index);
                                    }
                                );
                            }else{
                                formData.append("movie_cn_name",movie_cn_name);
                                formData.append("movie_fg_name",movie_fg_name);
                                formData.append("movie_director",movie_director);
                                formData.append("movie_actor",movie_actor);
                                formData.append("movie_detail",movie_detail);
                                formData.append("movie_duration",movie_duration);
                                formData.append("movie_type",movie_type);
                                formData.append("movie_releaseDate",movie_releaseDate);
                                formData.append("movie_country",movie_country);
                                formData.append("method",method);
                                formData.append("file",file);
                                $.ajax({
                                    type:'post',
                                    url: url + tempurl,
                                    data: formData,
                                    processData: false,
                                    contentType: false,
                                    success:function (result) {
                                        var obj = result;
                                        if(obj.code == 0){
                                            layer.alert(temp + '成功！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                    location.reload();
                                                }
                                            );
                                        }else{
                                            layer.alert(temp + '失败！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                }
                                            );
                                        }
                                    }
                                });
                            }
                           // 修改电影
                        } else{
                        	if((movie_cn_name == "") || (movie_director == "") || (movie_actor == "") ||
                                    (movie_detail == "") || (movie_duration == "") || (movie_type == "") || 
                                    (movie_fg_name == "") || (movie_releaseDate == "") || (movie_country == "")){
                                    layer.alert(temp + '信息不能存在空，' + temp + '失败！',{icon: 0,offset: clientHeight/5},
                                        function (){
                                            layer.close(layer.index);
                                        }
                                    );
                                    return;
                                }
                            if(file != null){
                                formData.append("file",file);
                            }
                            formData.append("movie_cn_name",movie_cn_name);
                            formData.append("movie_fg_name",movie_fg_name);
                            formData.append("movie_director",movie_director);
                            formData.append("movie_actor",movie_actor);
                            formData.append("movie_detail",movie_detail);
                            formData.append("movie_duration",movie_duration);
                            formData.append("movie_type",movie_type);
                            formData.append("movie_releaseDate",movie_releaseDate);
                            formData.append("movie_country",movie_country);
                            formData.append("movie_id",movie_id);
                            formData.append("method",method);
                            $.ajax({
                                type:'post',
                                url: url + tempurl,
                                data: formData,
                                processData: false,
                                contentType: false,
                                success:function (result) {
                                    var obj = result;
                                    if(obj.code == 0){
                                        layer.alert(temp + '成功！',{icon: 0,offset: clientHeight/5},
                                            function (){
                                                layer.closeAll();
                                                location.reload();
                                            }
                                        );
                                    }else{
                                        layer.alert(temp + '失败！',{icon: 0,offset: clientHeight/5},
                                            function (){
                                                layer.closeAll();
                                            }
                                        );
                                    }
                                }
                            });
                        }
                    }
                    ,btnAlign: 'c movie-last'
                    ,moveType: 0 //拖拽模式，0或者1
                    ,content: AddMoviesHtml
                    ,success: function(layero){
                    	laydate.render({
                            elem: '#movie_releaseDate'
                            ,type: 'date'
                            ,format:'yyyy-MM-dd'
                          });
                        if(flag == 1){
                            var TextSide = $(".textside").find(".temp");
                            var StrActor,StrRole;            
                            $.ajax({
                                type:'post',
                                url: url + "/movie",
                                dataType:'json',
                                data: {
                                	method:"findMovieById",
                                    movie_id: movie_id
                                },
                                success:function (obj) {
                                	
                                    $('#movie_cn_name').val(obj.data.movie_cn_name);
                                    $('#movie_director').val(obj.data.movie_director);
                                    ActorTemp = obj.data.movie_actor;
                                    $('#movie_detail').val(obj.data.movie_detail);
                                    $('#movie_duration').val(obj.data.movie_duration);
                                    $('#movie_type').val(obj.data.movie_type);
                                    $('#movie_fg_name').val(obj.data.movie_fg_name);
                                    $('#movie_releaseDate').val(dateFormat('YYYY-mm-dd',obj.data.releaseDate));
                                    $('#movie_country').val(obj.data.movie_country);
                                    $('#demo1').attr('src', obj.data.movie_picture);

                                    StrActor = ActorTemp.split(',');
                                    $('#movie_actor1').val(StrActor[0].split(':')[0]);
                                    $('#movie_role1').val(StrActor[0].split(':')[1]);
                                    for(var i = 1;i<StrActor.length;i++){
                                        StrRole = StrActor[i].split(':');
                                        TextSide.before(
                                            "<div class=\"layui-form-item\">" +
                                                "<label class=\"layui-form-label\" style=\"display:inline-block;\">人员" + (i+1) + "</label>" +
                                                "<div class=\"layui-input-block addusertext actor\" style=\"display:inline-block; margin-left: 10px !important; width: 118px;\">" +
                                                    "<input id=\"movie_actor" + (i+1) + "\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieActor" + (i+1) + "\" class=\"layui-input\">" +
                                                "</div>" +
                                                "<span style=\"margin-left:10px\">饰</span>" +
                                                "<div class=\"layui-input-block addusertext\" style=\"display:inline-block; margin-left: 10px !important; width: 118px;\">" +
                                                    "<input id=\"movie_role" + (i+1) + "\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieRole" + (i+1) + "\" class=\"layui-input\">" +
                                                "</div>" +
                                            "</div>"
                                        );
                                        $('#movie_actor'+(i+1)).val(StrRole[0]);
                                        $('#movie_role'+(i+1)).val(StrRole[1]);
                                    }
                                    ActorNum = StrActor.length;
                                }
                            });
                        }
                        else{
                            ActorNum = 1;
                        }
                        //图片上传
                        layui.use('upload', function(){
                            var $ = layui.jquery
                            ,upload = layui.upload;         
                            //普通图片上传
                            var uploadInst = upload.render({
                                elem: '#file'
                                ,auto: false
                                , choose: function (obj) {
                                    //预读本地文件
                                    obj.preview(function (index, file, result) {
                                        $('#demo1').attr('src', result); //图片链接（base64）
                                    })
                                    file = $('#file')[0].files[0];
                                }
                            });
                        });
                    }
                });
            });
        }
        //电影下架点击事件
        function deleteConfirm(movie_id){
            console.log(movie_id);
            layui.use(['layer'], function(){
                var layer = layui.layer;
                layer.alert('确定要对id为“'+ movie_id + '”的电影修改进行删除吗？',{icon: 0,offset: clientHeight/5},
                    function () {
                        $.ajax({
                            type:'post',
                            url: url + "/movie",
                            dataType:'json',
                            data: {
                            	method:'deleteMovie',
                                movie_id: movie_id,
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
            });
        }
        //电影添加演员
        function addActor(){
            var TextSide = $(".textside").find(".temp");
            if(ActorNum<5){
                ActorNum++;
                TextSide.before(
                    "<div class=\"layui-form-item\">" +
                        "<label class=\"layui-form-label\" style=\"display:inline-block;\">人员" + ActorNum + "</label>" +
                        "<div class=\"layui-input-block addusertext actor\" style=\"display:inline-block; margin-left: 10px !important; width: 118px;\">" +
                            "<input id=\"movie_actor" + ActorNum + "\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieActor" + ActorNum + "\" class=\"layui-input\">" +
                        "</div>" +
                        "<span style=\"margin-left:10px\">饰</span>" +
                        "<div class=\"layui-input-block addusertext\" style=\"display:inline-block; margin-left: 10px !important; width: 118px;\">" +
                            "<input id=\"movie_role" + ActorNum + "\" type=\"text\" name=\"title\" lay-verify=\"title\" autocomplete=\"off\" placeholder=\"MovieRole" + ActorNum + "\" class=\"layui-input\">" +
                        "</div>" +
                    "</div>"
                );
            }
            else{
                layui.use(['laypage', 'layer'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                    layer.alert('最多只能存在5个演职人员！',{icon: 0,offset: clientHeight/5},
                        function (){
                            layer.close(layer.index);
                        }
                    );
                });
            }
        }
        //电影删除演员
        function deleteActor(){
            console.log(ActorNum);
            if(ActorNum>1){
                var TextSide = $(".textside").find(".layui-form-item");
                TextSide[(ActorNum+2)].remove();
                ActorNum--;
            }
            else{
                layui.use(['laypage', 'layer'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                    layer.alert('最少要存在1个演职人员！',{icon: 0,offset: clientHeight/5},
                        function (){
                            layer.close(layer.index);
                        }
                    );
                });
            }
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
                        ,{field:'comment_time', title:'评论时间', width:180, unresize: true, sort: true}
                        ,{field:'comment_content', title:'评论内容', width:590, unresize: true, edit: "text"}
                        ,{title:'操作', width:240, unresize: true, align:'center', toolbar: '#commentbar'}
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
                                    	method:"updateComment",
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
                                    	method:"deleteComemnt",
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
                                	method:"findCommentsByUserName",
                                    user_name: user_name
                                }
                                ,page: {
                                    curr: 1 //重新从第 1 页开始
                                }
                            });
                        break;
                        case 'findcommentall':
                            table.render({
                                elem: '#comment_table_id'
                                ,url: url + '/comment'
                                ,where: {
                                	method:"findAllCommentsPage"
                                }
                                ,method: 'post'
                                ,toolbar: '#commenttoolbar'
                                ,title: '订单列表'
                                ,cols: [[
                                    {field:'comment_id', title:'评论编号', width:102, unresize: true, sort: true}
                                    ,{field:'comment_user', title:'用户账号', width:100, unresize: true,templet: '<div>{{d.comment_user.user_name}}</div>'}
                                    ,{field:'comment_time', title:'评论时间', width:180, unresize: true, sort: true}
                                    ,{field:'comment_content', title:'评论内容', width:590, unresize: true, edit: "text"}
                                    ,{title:'操作', width:240, unresize: true, align:'center', toolbar: '#commentbar'}
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
                        break;
                    };
                });
            });
        }
        function sortUp(a,b){
                return a-b;
         }
        function dateFormat(fmt, value) {
            let ret;
            var date = new Date(value);
            let opt = {
                "Y+": date.getFullYear().toString(),        // 年
                "m+": (date.getMonth() + 1).toString(),     // 月
                "d+": date.getDate().toString(),            // 日
                "H+": date.getHours().toString(),           // 时
                "M+": date.getMinutes().toString(),         // 分
                "S+": date.getSeconds().toString()          // 秒
                // 有其他格式化字符需求可以继续添加，必须转化成字符串
            };
            for (var k in opt) {
                ret = new RegExp("(" + k + ")").exec(fmt);
                if (ret) {
                    fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
                };
            };
            return fmt;
        }
    </script>
    <!-- ------------------------------------------------------------------- -->
</body>
</html>