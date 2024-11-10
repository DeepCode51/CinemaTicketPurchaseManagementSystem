<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <link href="../static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="../static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script src="../static/bootstrap/js/jquery-3.3.1.min.js"></script>
    <script src="../static/bootstrap/js/bootstrap.min.js"></script>
    <link rel="icon" type="image/x-icon" href="../static/images/logo.ico"/>
    <link rel="stylesheet" type="text/css" href="../static/css/header.css">
    <link rel="stylesheet" type="text/css" href="../static/css/main.css">
    <link rel="stylesheet" type="text/css" href="../static/css/footer.css">
    <link rel="stylesheet" type="text/css" href="../static/css/buyTickets.css">
    <link rel="stylesheet" type="text/css" href="../static/css/movieDetail.css">
    <link rel="stylesheet" type="text/css" href="../static/css/selectSeat.css">
    <script src="../static/js/header.js" charset="utf-8"></script>
    <script src="../static/js/Api.js"></script>
    <script src="../static/layui/layui.js" charset="utf-8"></script>
    <link rel="stylesheet" href="../static/layui/css/layui.css" media="all">
    <title>选场次</title>
</head>
<body>
    <!-- ------------------------------------------------------------------- -->
    <!-- 导航栏 -->
    <jsp:include page="header.jsp"/>
    
    <!-- 巨幕 -->
    <div class="banner2">
        <div class="wrapper clearfix">
            <div class="cinema-main clearfix">
                <div class="cinema-brief-container">
                	<div id="info">
                		
                	</div>
                    <div class="telphone">电话：0335-2661222</div>
                    <div class="features-group">
                        <div class="group-title">影院服务</div>

                        <div class="feature">
                            <span class="tag ">3D眼镜</span>
                            <p class="desc text-ellipsis" title="免押金">免押金</p>
                        </div>
                        <div class="feature">
                            <span class="tag ">可停车</span>
                            <p class="desc text-ellipsis" title="可停车">停车场可凭电影票在影城票台领取3小时内免停权益</p>
                        </div>
                    </div>
                </div>
            </div>
                
        </div>
    </div>

    <!-- 主体 -->
    <div class="main">
        <div class="main-inner main-bodyz">

            <div class="show-list active" data-index="0">
                <!-- 电影信息 -->
                <div class="movie-info">
                </div>
                <!-- 观影时间 -->
                <div class="show-date">
                    <span>观影时间 :</span>  
                </div>
                <!-- 场次列表 -->
                <div class="plist-container active">
                    <table class="plist">
                        <thead>
                            <tr>
                                <th>放映时间</th>
                                <th>语言版本</th>
                                <th>放映厅</th>
                                <th>售价（元）</th>
                                <th>选座购票</th>
                            </tr>
                        </thead>
                        <tbody>
                          
                        </tbody>
                    </table>
                </div>
                <div id="demo0"></div>
            </div>
        </div>
    </div>

     <!-- 脚 -->
     <jsp:include page="footer.jsp"/>

    <script>
    	var dataIndex=0;
    	var page=1;
    	var limit=5;
    	var total;
        window.onload = function(){
        	initParams();
            initBanner(); //初始化巨幕
            initHeader();
        }

        //初始化巨幕
        function initBanner(){
            movie_id = getUrlParams('movie_id');
            cinema_id = getUrlParams('cinema_id');
            var cinemaBriefContainer = $(".cinema-brief-container");
            $.ajax({
                type:'post',
                url: url + "/schedule",
                dataType:'json',
                data: {
                	method:'findScheduleByCinemaAndMovie',
                    movie_id: movie_id,
                    cinema_id: cinema_id,
                    dataIndex:dataIndex,
                    page:page,
                    limit:limit
                },
                success:function (obj) {
                    console.log(obj);
                    $("#info").empty();
                   	$("#info").append(
                          "<h3 class=\"name text-ellipsis\">" + obj.cinema.cinema_name + "</h3>" +
                          "<div class=\"address text-ellipsis\">" + obj.cinema.cinema_address + "</div>"
                     );
                    
                    initMoive(obj); //初始化电影信息
                    initSchedule(obj); //初始化场次信息
                    total = obj.count;
                    init_pageHtml();
                }
            });
        }
      //初始化分页
        function init_pageHtml(){
        	layui.use(['laypage', 'layer'], function(){
          	  var laypage = layui.laypage
          	  ,layer = layui.layer;
          	  
          	  //总页数低于页码总数
          	  laypage.render({
          	    elem: 'demo0'
          	    ,limit:limit
          	    ,curr:page
          	    ,count: total //数据总数
          	    ,jump: function(obj, first){
          	      //obj包含了当前分页的所有参数，比如：
          	      console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
          	      console.log(obj.limit); //得到每页显示的条数
          	      page=obj.curr;
          	      limit=obj.limit;
          	      
          	      //首次不执行
          	      if(!first){
          	    	initBanner()
          	      }
          	    }
          	  });
          })
        }
		function addDate(date,days){ 
	       var d=new Date(date); 
	       d.setDate(d.getDate()+days); 
	       var m=d.getMonth()+1; 
	       return d; 
		} 
        //初始化电影信息
        function initMoive(obj){
           // console.log(obj);
            var Data = new Date();
            var Month = Data.getMonth() + 1;
            var Day = Data.getDate();
            var tomorrow = addDate(Data,1);
            var thirdDay = addDate(tomorrow,1);
            var movieInfo = $(".movie-info");
            var showDate = $(".show-date");
            movieInfo.empty();
            showDate.empty();
            movieInfo.append(
                "<div>" +
                    "<h3 class=\"movie-name\">"+ obj.movie.movie_cn_name +"</h3>" +
                    "<span class=\"score sc\">"+ obj.movie.movie_score +"</span>" +
                "</div>" +
                "<div class=\"movie-desc\">" +
                    "<div>" +
                        "<span class=\"key\">时长 : </span>" +
                        "<span class=\"value\">"+ obj.movie.movie_duration +"</span>" +
                    "</div>" +
                    "<div>" +
                        "<span class=\"key\">类型 :</span>" +
                        "<span class=\"value\">"+ obj.movie.movie_type +"</span>" +
                    "</div>" +
                    "<div>" +
                        "<span class=\"key\">导演 :</span>" +
                        "<span class=\"value\">"+ obj.movie.movie_director+"</span>" +
                    "</div>" +
                "</div>"
            )
            if(dataIndex == 0){
            	 showDate.append("<span class=\"date-item active\" data-index=\"0\" onclick=\"selectDate('0')\">今天" + Month + "月" + (Day) + "</span>  ");
                 showDate.append("<span class=\"date-item\" data-index=\"1\" onclick=\"selectDate('1')\">明天" + (tomorrow.getMonth() + 1) + "月" + (tomorrow.getDate()) + "</span>  ");
                 showDate.append("<span class=\"date-item\" data-index=\"2\" onclick=\"selectDate('2')\">后天" + (thirdDay.getMonth() + 1) + "月" + (thirdDay.getDate()) + "</span>  ");
            }else if(dataIndex == 1){
            	 showDate.append("<span class=\"date-item\" data-index=\"0\" onclick=\"selectDate('0')\">今天" + Month + "月" + (Day) + "</span>  ");
                 showDate.append("<span class=\"date-item active\" data-index=\"1\" onclick=\"selectDate('1')\">明天" + (tomorrow.getMonth() + 1) + "月" + (tomorrow.getDate()) + "</span>  ");
                 showDate.append("<span class=\"date-item\" data-index=\"2\" onclick=\"selectDate('2')\">后天" + (thirdDay.getMonth() + 1) + "月" + (thirdDay.getDate()) + "</span>  ");
            }else if(dataIndex == 2){
            	showDate.append("<span class=\"date-item\" data-index=\"0\" onclick=\"selectDate('0')\">今天" + Month + "月" + (Day) + "</span>  ");
                showDate.append("<span class=\"date-item\" data-index=\"1\" onclick=\"selectDate('1')\">明天" + (tomorrow.getMonth() + 1) + "月" + (tomorrow.getDate()) + "</span>  ");
                showDate.append("<span class=\"date-item active\" data-index=\"2\" onclick=\"selectDate('2')\">后天" + (thirdDay.getMonth() + 1) + "月" + (thirdDay.getDate()) + "</span>  ");
            }
           
        }
        function selectDate(dataIndex){
        	window.location.href="${ctx}/jsp/selectSeat.jsp?cinema_id="+getUrlParams('cinema_id')+"&movie_id="+getUrlParams('movie_id')+"&dataIndex="+dataIndex;
        }

        //初始化场次信息
        function initSchedule(obj){
            var plist = $(".plist").find("tbody");
            plist.empty();
            for(var i = 0;i < obj.data.length;i++){
                plist.append(
                    "<tr class=\"\">" +
                        "<td> <span class=\"begin-time\">"+ obj.data[i].schedule_startTime +"</span> <br> </td>" +
                        "<td> <span class=\"lang\">" + obj.data[i].schedule_movie.movie_country +"</span> </td>" +
                        "<td> <span class=\"hall\">" + obj.data[i].schedule_hall.hall_name + "</span> </td>" +
                        "<td> <span class=\"sell-price\"> <span class=\"stonefont\">" + obj.data[i].schedule_price + "</span> </span> </td>" +
                        "<td> <a href=\"./buySeat.jsp?schedule_id="+ obj.data[i].schedule_id +"\" class=\"buy-btn normal\">选座购票</a> </td>" +
                    "</tr>"   
                            );
            }
        }
      	//初始化url参数
        function initParams(){
            if(getUrlParams('dataIndex') == null){
            	dataIndex = 0;
            }else{
            	dataIndex = getUrlParams('dataIndex');
            }
        }
                    
        //获取url参数
        function getUrlParams(name) { // 不传name返回所有值，否则返回对应值
            var url = window.location.search;
            if (url.indexOf('?') == 1) { return false; }
            url = url.substr(1);
            url = url.split('&');
            var name = name || '';
            var nameres;
            // 获取全部参数及其值
            for(var i=0;i<url.length;i++) {
                var info = url[i].split('=');
                var obj = {};
                obj[info[0]] = decodeURI(info[1]);
                url[i] = obj;
            }
            // 如果传入一个参数名称，就匹配其值
            if (name) {
                for(var i=0;i<url.length;i++) {
                    for (var key in url[i]) {
                        if (key == name) {
                            nameres = url[i][key];
                        }
                    }
                }
            } else {
                nameres = url;
            }
            // 返回结果
            return nameres;
        }

    </script>
</body>
</html>