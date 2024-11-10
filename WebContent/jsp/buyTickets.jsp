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
    <script src="../static/js/header.js" charset="utf-8"></script>
    <script src="../static/js/Api.js"></script>

    <script src="../static/layui/layui.js" charset="utf-8"></script>
    <link rel="stylesheet" href="../static/layui/css/layui.css" media="all">
    <title>选影院</title>
</head>
<body>
    <!-- ------------------------------------------------------------------- -->
    <!-- 导航栏 -->
    <jsp:include page="header.jsp"/>
  

    <!-- 巨幕 -->
    <div class="banner2">
        <div class="wrapper clearfix">
            <div class="celeInfo-left">
                <div class="avatar-shadow">
                    <!-- 图片 -->
                </div>
            </div>
            
            <div class="celeInfo-right clearfix">
                <div class="movie-brief-container">
                    <!-- 上 -->
                </div>
                <div class="action-buyBtn">
                    <div class="action clearfix" data-val="{movieid:42964}">
                        <a class="score-btn " data-bid="b_rxxpcgwd" onclick="writeComment()">
                            <div>
                                <i class="icon score-btn-icon"></i>
                                <span class="score-btn-msg" data-act="comment-open-click">评分</span>
                            </div>
                        </a>
                    </div>
                </div>

                <div class="movie-stats-container">
                    <div class="movie-index">
                        <p class="movie-index-title">用户评分</p>
                        <div class="movie-index-content score normal-score">
                            <span class="index-left info-num ">
                                <!-- 评分 -->
                            </span>
                            <div class="index-right">
                                <div class="star-wrapper">
                                    <div id="MovieScore"></div>
                                </div>
                                <span class="score-num">
                                    <!-- 评分数 -->
                                </span>
                            </div>
                        </div>
                    </div>   

                </div>

            </div>
        </div>
    </div>

    <!-- 占位符 -->
    <div style="margin-top: 50px;"></div>

    <!-- 主体 -->
    <div class="main">
        <div class="main-inner main-buyticket">
            <!-- 标签 -->
            <div class="tags-panel">
                <ul class="tags-lines">
                    <li class="tags-line">
                        <div class="tags-title">日期:</div>
                        <ul class="tags tags-date">
                            <!-- 日期 -->
                        </ul>
                    </li>
                    <li class="tags-line tags-line-border" data-type="brand">
                        <div class="tags-title">影院:</div>
                        <ul class="tags tags-brand">
                            <!-- 品牌 -->
                        </ul>
                    </li>

            
                </ul>
            </div>	
            <!-- 列表 -->
            <div class="cinemas-list">
                <h2 class="cinemas-list-header">影院列表</h2>   
            </div>
            <!-- 分页 -->
            <!-- <div class="cinema-pager">
                <ul class="list-pager">	
                </ul>
            </div> -->
            <div id="demo0"></div>
        </div>
    </div>

    <!-- 脚 -->
    <jsp:include page="footer.jsp"/>

    <script>
        var clientHeight = document.documentElement.clientHeight;
        var movie_id;
        var date;
        var brand;
        var hall;
        var page;
        var WriteCommentHtml;
        var CinemaLength;
        var page=1;
        var limit=5;
        var total;
        var DateStr = ["今天","明天","后天"],
        BrandStr = ["全部"],
        HallStr = ["全部","普通厅","IMAX厅","CGS中国巨幕厅","杜比全景声厅","DTS:X临境音厅","4K厅","4D厅"];
        window.onload = function(){
            initHeader();
            initParams(); //参数
            initBanner(); //巨幕
            initHtml(); //HTML
            //initTags(); //标签和分页
        }

        //初始化HTML
        function initHtml(){
            WriteCommentHtml = 
                "<h3 class=\"commenttitle\">评论信息</h3>" +
                // "<div class=\"layui-form-item\">" +
                //     "<label class=\"layui-form-label usernametext\">用户帐号</label>" +
                //     "<div class=\"layui-input-block username\">" +
                //         "<input id=\"user_name_write\" type=\"text\" name=\"title\" lay-verify=\"title\" disabled=\"disabled\" style=\"cursor:not-allowed;\" autocomplete=\"off\" placeholder=\"username\" class=\"layui-input\">" +
                //     "</div>" +
                // "</div>" +
                "<div style=\"text-align:center; margin:30px 0;\">" +
                    "<div id=\"GiveScore\"></div>" +
                    "<p style=\"color:#888;\">点击星星进行评分</p>" +
                "</div>"+
                "<div class=\"layui-form-item\">" +
                    "<label class=\"layui-form-label commentcontenttext\">评论内容</label>" +
                    "<div class=\"layui-input-block commentcontent\">" +
                        "<textarea id=\"comment_content_write\" style=\"height:150px;\" placeholder=\"请输入评论内容\" autocomplete=\"off\" class=\"layui-textarea\" name=\"desc\" class=\"layui-input\"></textarea>" +
                    "</div>" +
                "</div>"
            ;
        }

        //初始化巨幕
        function initBanner(){
            movie_id = getUrlParams('movie_id');
            var avatar = $(".avatar-shadow");
            var movieBriefContainer = $(".movie-brief-container");
            var infoNum = $(".info-num");
            var scoreNum = $(".score-num");
            var stonefontNum = $(".stonefont-num");
            var actionBuyBtn = $(".action-buyBtn");
            var StonefontTemp;

            $.ajax({
                type:'post',
                url: url + "/movie",
                dataType:'json',
                data: {
                	method:"findMovieById",
                    movie_id: movie_id,
                    cinema_id:brand,
                    date:date,
                    page:page,
                    limit:limit
                },
                success:function (obj) {
                    StonefontTemp = obj.data.movie_boxOffice;
                    StonefontTemp += "万";
                    avatar.append("<img class=\"avatar\" src=\"" + obj.data.movie_picture + "\" alt=\"\">");
                    movieBriefContainer.append(
                    "<h3 class=\"name\">" + obj.data.movie_cn_name + "</h3>" +
                    "<div class=\"ename ellipsis\">" + obj.data.movie_fg_name + "</div>" +
                    "<ul>" +
                        "<li class=\"ellipsis\">" + obj.data.movie_type + "</li>" +
                        "<li class=\"ellipsis\">" + obj.data.movie_duration + " / " + obj.data.movie_country + "</li>" +
                        "<li class=\"ellipsis\">" + datefomate(obj.data.releaseDate) + "</li>" +
                    "<ul>");
                    infoNum.append("<span class=\"stonefont\">" + obj.data.movie_score + "</span>");
                    scoreNum.append("<span class=\"stonefont\">" + obj.data.movie_commentCount + "</span>人评分");
                    stonefontNum.append("<span class=\"stonefont\">" + StonefontTemp + "</span>");
                    actionBuyBtn.append("<a class=\"btn buy\" href=\"./movieDetail.jsp?movie_id=" + movie_id + "\" data-act=\"more-detail-click\">查看更多电影详情</a>");
                    layui.use('rate', function(){
                        var rate = layui.rate;
                        rate.render({
                            elem: '#MovieScore'
                            ,value: (obj.data.movie_score / 2)
                            ,half: true
                            ,readonly: true
                        })
                    });
                    initList(obj);
                    
                    //初始化影院数组
                    for(var i=0;i<obj.allCinemaList.length;i++){
                    	BrandStr.push(obj.allCinemaList[i].cinema_name);
                    }
                    initTags(); //标签
                    total = obj.cinemaCount;
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
      //写评论
        function writeComment(){
            layui.use(['rate','laypage', 'layer', 'table'], function(){
                var laypage = layui.laypage;
                var layer = layui.layer;
                var table = layui.table;
                var rate = layui.rate;
                //写评论
                layer.open({
                    type: 1
                    ,title: "编写评论" //不显示标题栏
                    ,closeBtn: false
                    ,area: '430px;'
                    ,shade: 0.8
                    ,offset: clientHeight/20
                    ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                    ,btn: ['确认评价', '取消']
                    ,yes: function(){
                    	var comment_score = $('#GiveScore span').text().replace("分",'');
                        var comment_content = $('#comment_content_write').val();
                        if(comment_content == ""){
                            layer.alert('评论内容不能空，评论失败！',{icon: 0,offset: clientHeight/5},
                                function (){
                                    layer.close(layer.index);
                                }
                            );
                        }
                        else{
                            if(comment_content.length > 150){
                                layer.alert('字数超过150个，评论失败！',{icon: 0,offset: clientHeight/5},
                                    function (){
                                        layer.close(layer.index);
                                    }
                                );
                            }else{
                                console.log(movie_id);
                                console.log(comment_content);
                                $.ajax({
                                    type:'post',
                                    url: url + "/comment",
                                    dataType:'json',
                                    data: {
                                    	method:"addCommentByUser",
                                        movie_id: movie_id,
                                        comment_content: comment_content,
                                        comment_score: comment_score
                                    },
                                    success:function (obj) {
                                        if(obj.code == 0){
                                            layer.alert('评价成功！',{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                    location.reload();
                                                }
                                            );
                                        }else{
                                            layer.alert(obj.msg,{icon: 0,offset: clientHeight/5},
                                                function (){
                                                    layer.closeAll();
                                                }
                                            );
                                        }
                                    }
                                });

                            }
                        }
                    }
                    ,btnAlign: 'c movie-last'
                    ,moveType: 0 //拖拽模式，0或者1
                    ,content: WriteCommentHtml
                    ,success: function(layero){
                    	rate.render({
                            elem: '#GiveScore'
                            ,value: 4.5
                            ,half: true
                            ,text: true
                            ,setText: function(value){
                                this.span.text(value*2+"分");
                            }
                        })
                     }
                });
            });
        }
        //初始化标签和分页
        function initTags(){
            var Data = new Date();
            var Month = Data.getMonth() + 1;
            var Day = Data.getDate();
            var tagsDate = $(".tags-date"),
                tagsBrand = $(".tags-brand"),
                tagsHall = $(".tags-hall");
            tagsDate.empty();
            tagsBrand.empty();
            tagsHall.empty();
            var DateActive = [],
                BrandActive = [],
                HallActive = [];
                PageActive = [];
            var urlTemp = ["&date="+date,"&brand="+brand,"&hall="+hall,"&page="+page];
            DateActive = inputTags(DateStr.length, DateActive, date);
            BrandActive = inputTags(BrandStr.length, BrandActive, brand);
            HallActive = inputTags(HallStr.length, HallActive, hall);
            for(var i=0;i<DateStr.length;i++){
                urlTemp[0] = "&date="+ i;
                tagsDate.append(
                    "<li " + DateActive[i] + ">" +
                        "<a href=\"?movie_id="+ movie_id + urlTemp[0] + urlTemp[1] + urlTemp[2] + urlTemp[3] +"\">"+
                            DateStr[i] + " " + Month + "月" + (Day+i) +
                        "</a>" +
                    "</li>"
                );
            }
            urlTemp = ["&date="+date,"&brand="+brand,"&hall="+hall,"&page="+page];
            for(var i=0;i<BrandStr.length;i++){
                urlTemp[1] = "&brand="+ i;
                tagsBrand.append(
                    "<li " + BrandActive[i] + ">" +
                        "<a href=\"?movie_id="+ movie_id + urlTemp[0] + urlTemp[1] + urlTemp[2] + urlTemp[3] +"\">"+
                            BrandStr[i] +
                        "</a>" +
                    "</li>"
                );
            }
           /*  urlTemp = ["&date="+date,"&brand="+brand,"&hall="+hall,"&page="+page];
            for(var i=0;i<AreaStr.length;i++){
                urlTemp[2] = "&area="+ i;
                tagsArea.append(
                    "<li " + AreaActive[i] + ">" +
                        "<a href=\"?movie_id="+ movie_id + urlTemp[0] + urlTemp[1] + urlTemp[2] + urlTemp[3] +"\">"+
                            AreaStr[i] +
                        "</a>" +
                    "</li>"
                );
            } */
            urlTemp = ["&date="+date,"&brand="+brand,"&hall="+hall,"&page="+page];
            for(var i=0;i<HallStr.length;i++){
                urlTemp[3] = "&hall="+ i;
                tagsHall.append(
                    "<li " + HallActive[i] + ">" +
                        "<a href=\"?movie_id="+ movie_id + urlTemp[0] + urlTemp[1] + urlTemp[2] + urlTemp[3] +"\">"+
                            HallStr[i] +
                        "</a>" +
                    "</li>"
                );
            }


        }
        //导入活跃标签
        function inputTags(length, Active, tags){
            for(var i=0;i<length;i++){
                if(tags==null&&i==0){
                    Active.push("class=\"active\"");
                    break;
                }
                if(i==tags){
                    Active.push("class=\"active\"");
                }
                else
                Active.push(" ");
            }
            return Active;
        }

        //初始化电影院列表
        function initList(obj){
            console.log(obj);
            var cinemasList = $(".cinemas-list");
            cinemasList.empty();
            var ListLength;       
            var TempPrice = [];
            var MinPrice = [];
            for(var i = 0;i< obj.cinemaList.length;i++){
                var scheduleCount = 0;
                for(var j = 0;j< obj.cinemaList[i].hallList.length;j++){
                    if(obj.cinemaList[i].hallList[j].scheduleList.length ==0){
                        scheduleCount++;
                    }
                }
                /* if(scheduleCount == obj.cinemaList[i].hallList.length){
                    obj.cinemaList.splice(i,1);
                } */
            }
            CinemaLength = obj.cinemaList.length;
            for(var i=0;i<obj.cinemaList.length;i++){
                TempPrice[i] = "";
                for(var j=0;j<obj.cinemaList[i].hallList.length;j++){
                    for(var p=0;p<obj.cinemaList[i].hallList[j].scheduleList.length;p++){
                        TempPrice[i] += obj.cinemaList[i].hallList[j].scheduleList[p].schedule_price;
                        TempPrice[i] += ",";                  
                    }
                }
                TempPrice[i] = TempPrice[i].substr(0, TempPrice[i].length - 1);  
                //对价格排序
                MinPrice[i] = TempPrice[i].split(',').sort(function (a,b) {
                    return a-b;
                });
            }
            if(obj.cinemaList.length<11){
                ListLength = obj.cinemaList.length;
            }
            else{
                ListLength = 10;
            }
            for(var i=0;i<ListLength;i++){
                cinemasList.append(
                    "<div class=\"cinema-cell\">" +
                        "<div class=\"cinema-info\">" +
                            "<a class=\"cinema-name\">" + obj.cinemaList[i].cinema_name + "</a>" +
                            "<p class=\"cinema-address\">地址：" + obj.cinemaList[i].cinema_address + "</p>" +
                        "</div>" +
                        "<div class=\"buy-btn\">" +
                            "<a href=\"./selectSeat.jsp?cinema_id=" + obj.cinemaList[i].cinema_id + "&movie_id=" + obj.data.movie_id + "\">选座购票</a>" +
                        "</div>" +
                        "<div class=\"price\">" +
                            "<span class=\"rmb red\">￥</span>" +
                            "<span class=\"price-num red\"><span class=\"stonefont\">"+ MinPrice[i].shift() +"</span></span>" +
                            "<span style=\"margin-left:5px;\">起</span>" +
                        "</div>" +
                    "</div>"
                );
            }
        }

        //初始化url参数
        function initParams(){
            if(getUrlParams('date') == null){
                date = "0";
            }else{
                date = getUrlParams('date');
            }
            if(getUrlParams('brand') == null){
                brand = "0";
            }else{
                brand = getUrlParams('brand');
            }
            if(getUrlParams('hall') == null){
                hall = "0";
            }else{
                hall = getUrlParams('hall');
            }
            if(getUrlParams('page') == null){
                page = "1";
            }else{
                page = getUrlParams('page');
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