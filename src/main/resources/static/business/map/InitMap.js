
//同步获取终端地图渲染坐标信息
var mapMoniterList = [];
$(function(){

//	setSty("divChart1", $.session.get('divChart1'));
//	setSty("divChart2", $.session.get('divChart2'));
//	setSty("divChart3", $.session.get('divChart3'));
//	setSty("divChart4", $.session.get('divChart4'));
	setSty("map_demo", $.session.get('divMap'));
//	setSty("divTable1", $.session.get('divTable1'));
//	setSty("divTable2", $.session.get('divTable2'));
//	setSty("divTable3", $.session.get('divTable3'));
//	setSty("divTable4", $.session.get('divTable4'));

//	console.log($.session.get('divChart1'))
//	console.log($.session.get('divChart2'))
//	console.log($.session.get('divChart3'))
//	console.log($.session.get('divChart4'))
//	console.log($.session.get('divMap'))
//	console.log($.session.get('divTable1'))
//	console.log($.session.get('divTable2'))
//	console.log($.session.get('divTable3'))
//	console.log($.session.get('divTable4'))
	
	$.ajax({
		  type: 'POST',
		  url: 'getMapMoniterList',
		  data: {},
		  dataType: 'json',
		  async: false,
		  success: function(data){
			  mapMoniterList = data;
			  console.log("map moniter data:" + data);
//			  layer.msg('地图终端数据加载完成!',{icon:1,time:1000});
		  }
	});

	var map = new BMap.Map("map_demo");
	var point = new BMap.Point(118.816114,32.061308);
	map.centerAndZoom(point, 14);
	map.enableScrollWheelZoom();//启用滚轮放大缩小
	map.addEventListener("tilesloaded",function(){     
		$(".BMap_cpyCtrl").hide();
		$(".anchorBL").hide(); 
    });//地图加载完毕后 隐藏百度图标
	map.addEventListener("zoomend", function(evt) {
		map.clearOverlays();
		for(var i=0; i < data_info.length; i++){
			//自定义图标
			var option = {
				imgSrc: "map/images/legend01.png",//"../baojing.gif",
				lon: data_info[i][0],
				lat: data_info[i][1],
				moniterId: data_info[i][2],
				width:'24px',
				height:'24px'
			}
			initCustomOverlay(option);
		}
			
	});
	
	//比例尺,缩放平移控件
  	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
	var top_left_navigation = new BMap.NavigationControl();
	map.addControl(top_left_control);
	map.addControl(top_left_navigation);
	
//	//显示范围
//	var bound = new BMap.Bounds(new BMap.Point(114.785199,34.286661),new BMap.Point(115.301473,34.581061));
//	try {
//		BMapLib.AreaRestriction.setBounds(map, bound);
//	} catch (e) {
//		alert(e);
//	}
	//115.049373,34.433575
	var data_info = [];
	for(var i=0;i<mapMoniterList.length; i++){
		data_info.push([mapMoniterList[i].lon-0, mapMoniterList[i].lat-0, mapMoniterList[i].id]);
	}

	for(var i=0; i < data_info.length; i++){
		//自定义图标
		var option = {
			imgSrc: "map/images/legend01.png",//"../baojing.gif",
			lon: data_info[i][0],
			lat: data_info[i][1],
			moniterId: data_info[i][2],
			width:'24px',
			height:'24px'
		}
		initCustomOverlay(option);
	}
	
	//系统title
	$("#map_title_control").click(function(){
		$("#map_title").slideToggle();
	});
	//点击图例
	$(".legend-item").click(function(){
		alert("图例切换隐藏显示")
	});
	
	//异常设备列表点击根据设备经纬度移动地图到具体位置
	var warnOverLay = null;
	$("#locationInfoToMap").click(function(){
	    var lat = $(this).attr("lat")-0;
	    var lon = $(this).attr("lon")-0;
		var optionWarn = {
			imgSrc: "map/images/baojing7.gif",
			lon: lon,//lon-0.00440,
			lat: lat,//lat+0.002169,
			offset: {x:30,y:30},
			width: '83px',
			height: '58',
		}
		if(null != warnOverLay){
			map.removeOverlay(warnOverLay);
		}
		warnOverLay = initCustomOverlay(optionWarn);
		setTimeout(function(){
			map.panTo(new BMap.Point(lon,lat));   //1秒后移动到设置经纬度所在地
		}, 1000);
		ShowDiv("divMap");
	});

	
	//添加普通标注到地图中
	function initOverlay(map,data_info){
		var marker = new BMap.Marker(new BMap.Point(data_info[i][0],data_info[i][1]));  // 创建标注
		var moniterId = data_info[i][2];
		map.addOverlay(marker);               // 将标注添加到地图中
		addClickHandler(moniterId, marker);
	}
	function addClickHandler(moniterId,marker){
		marker.addEventListener("click",
			function(e){
				//终端详情信息获取
				openInfo(moniterId, e);
			}
		);
	}
	
	function openInfo(content,e) {
		var opts = {
			width : 250,     // 信息窗口宽度
			height: 80,     // 信息窗口高度
			title : "信息窗口" , // 信息窗口标题
			enableMessage:true//设置允许信息窗发送短息
		};
		var p = e.target;
		var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
		var infoWindow = new BMap.InfoWindow(content, opts);  // 创建信息窗口对象 
		map.openInfoWindow(infoWindow, point); //开启信息窗口
	}
	
	//自定义覆盖物
	function initCustomOverlay(option){
		//1、定义构造函数并继承Overlay
	　　//定义自定义覆盖物的构造函数  
		function ComplexCustomOverlay(point){
		  this._point = point;
		}
		ComplexCustomOverlay.prototype = new BMap.Overlay();
		ComplexCustomOverlay.prototype.initialize = function(map){
			// 保存map对象实例 
			this._map = map;
			// 创建div元素，作为自定义覆盖物的容器  
			var div = this._div = document.createElement("div");
			div.style.position = "absolute";
			div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);//聚合功能?
			// 可以根据参数设置元素外观
			div.style.height = option.height || "30px";
			div.style.width = option.width || "20px";

			var arrow = this._arrow = document.createElement("img");
			arrow.src = option.imgSrc;
			arrow.style.width = option.width || "30px";
			arrow.style.height = option.height || "20px";
			arrow.style.top = "22px";
			arrow.style.left = "10px";
			div.appendChild(arrow);

			// 将div添加到覆盖物容器中  
			map.getPanes().markerPane.appendChild(div);//getPanes(),返回值:MapPane,返回地图覆盖物容器列表
			// 需要将div元素作为方法的返回值，当调用该覆盖物的show、  
			// hide方法，或者对覆盖物进行移除时，API都将操作此元素。
			return div;
		}
		option.offset = option.offset || {x:0,y:0};
		option.offset.x = option.offset.x || 0;
		option.offset.y = option.offset.y || 0;
		//3、绘制覆盖物实现绘制方法
		ComplexCustomOverlay.prototype.draw = function(){
			var map = this._map;
			var pixel = map.pointToOverlayPixel(this._point);
			this._div.style.left = pixel.x - option.offset.x + "px";//pixel.x - parseInt(this._arrow.style.left) + "px";
			this._div.style.top  = pixel.y - option.offset.y + "px";
		}
		//4、自定义覆盖物添加事件方法
		ComplexCustomOverlay.prototype.addEventListener = function(event,fun){
			this._div['on'+event] = fun;
		}
		//var myCompOverlay = new ComplexCustomOverlay(new BMap.Point(option.lat,option.lon));
		var myCompOverlay = new ComplexCustomOverlay(new BMap.Point(option.lon,option.lat));
		map.addOverlay(myCompOverlay);//将标注添加到地图中
		//5、 为自定义覆盖物添加点击事件
		myCompOverlay.addEventListener('click',function(){
			hide_show();
		});
		function hide_show(){ 
			showInfo(option);
		}
		return myCompOverlay;
	}
	var infoBox = null;
	// 终端详情
	function showInfo(option){
		var moniterInfo = {};
		$.ajax({
			  type: 'POST',
			  url: 'getMoniterInfo',
			  data: {"moniterId": option.moniterId},
			  dataType: 'json',
			  async: false,
			  success: function(data) {
				  moniterInfo = data;
				  console.log("终端详情：");
				  console.log(moniterInfo);
			  }
		});
		
		var html ="<div class='infoBoxContent' style='-webkit-transform-origin-x: 0;-webkit-transform: scale(0.90);'>"
			+"<div class='title'><span>ID：</span><span>{id}</span><span style='margin-left:5px'>IP：</span><span>{ip}</span></div>"
			+"<div class='list'>"
			+"<ul>"
			+"<li><div class='deviceImg'><img src='map/images/route02.png' style='width:60px;'/></div><div class='deviceDesc'>"
			+"<span class='deviceInfo-1'>路口：{road}</span><span class='deviceInfo-2'>类型：{version}</span><span class='deviceInfo-3'>状态：正常</span><span class='deviceInfo-4'>温度：-</span><span class='deviceInfo-5'>湿度：-</span><span class='deviceInfo-6'>箱门：开</span></div></li>"
			+"<li class='li-wlan-info'>"
			+"<table class='table table-condensed'>"
			//+"<caption class=''>网口/电源口详情</caption>"
			   +"<thead >"
				  +"<tr>"
					 +"<th style='text-align:center;padding:2px;'>类型</th><th style='text-align:center;padding:2px;'>状态</th><th style='text-align:center;padding:2px;'>设备类型</th>"
				  +"</tr>"
			   +"</thead>"
			   +"<tbody>"
				  +"<tr>"
					 +"<td style='padding:0px;'>上传网口</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>半球型</td>"
				  +"</tr>"
				  +"<tr>"
					 +"<td style='padding:0px;'>下载网口1</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>筒型</td>"
				  +"</tr>"
				  +"<tr style='"+(moniterInfo.moniterType != '1'?'':'display:none')+"'>"
					 +"<td style='padding:0px;'>下载网口2</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>枪型</td>"
				  +"</tr>"
				  +"<tr style='"+(moniterInfo.moniterType != '1'?'':'display:none')+"'>"
					 +"<td style='padding:0px;'>下载网口3</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>球型</td>"
				  +"</tr>"
				  +"<tr>"
					 +"<td style='padding:0px;'>输入电源</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>-</td>"
				  +"</tr>"
				  +"<tr>"
					 +"<td style='padding:0px;'>输出电源口1</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>-</td>"
				  +"</tr>"
		  		  +"<tr style='"+(moniterInfo.moniterType != '1'?'':'display:none')+"'>"
					 +"<td style='padding:0px;'>输出电源口2</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>-</td>"
				  +"</tr>"
				  +"<tr style='"+(moniterInfo.moniterType != '1'?'':'display:none')+"'>"
					 +"<td style='padding:0px;'>输出电源口3</td><td style='padding:0px;'>正常</td><td style='padding:0px;'>-</td>"
				  +"</tr>"
			   +"</tbody>"
			+"</table>"
			+"</li>"
			+"</ul></div>"
			+"</div>";
		if(infoBox){
			infoBox.close();
		}
		//数据渲染
		html = setMoniterData(html, moniterInfo);
		infoBox = new BMapLib.InfoBox(map,html,{
			boxStyle:{
				background:"url('map/images/bg_pop4.png') no-repeat 0px -13px",
				'background-size':"290px 300px ",
				width: "266px",
				height: "250px",
			},
			closeIconUrl: "map/images/pop_close.png",
			closeIconMargin: "15px 19px 10px 10px",
			enableAutoPan: true,
			cursor:"pointer",
			'z-index':999999,
			align: INFOBOX_AT_BOTTOM
		});

		var poi = new BMap.Point(option.lon, option.lat-0.0003);
		var marker = new BMap.Marker(poi);
		map.addOverlay(marker);
		infoBox.open(marker);
		marker.enableDragging();
		marker.addEventListener("click",function(e){
			infoBox.open(marker);
		});
		marker.addEventListener("drag", function(e){    
			 console.log("当前位置：" + e.point.lng + ", " + e.point.lat);    
			 }
		)
	}
	
	//终端数据渲染
	var setMoniterData = function(html, moniterInfo) {
		html = html.replace("{road}", moniterInfo.desc || '-');
		html = html.replace("{deviceType}", moniterInfo.desc || '-');
		html = html.replace("{road}", moniterInfo.desc || '-');
		html = html.replace("{id}", moniterInfo.id || '-');
		html = html.replace("{version}", moniterInfo.moniterType);
		html = html.replace("{ip}", moniterInfo.ip || '-');
		
		return html;
	}
});

function setSty(id,param){
	if($("#"+id).length == 0){
		alert("系统加载模板配置信息出错");
	}
	var css = param.split(",");
	$("#"+id).css("left", css[0]);
	$("#"+id).css("top", css[1]);
	$("#"+id).css("width", css[2]);
	$("#"+id).css("height", css[3]);
}

//弹出隐藏层
function ShowDiv(show_div){
	var scrollHeight = document.body.scrollHeight; //文档高度
//	document.getElementById(bg_div).style.height=scrollHeight+'px';
	
	document.getElementById(show_div).style.display='block';
//	document.getElementById(bg_div).style.display='block';
}
//关闭弹出层
function CloseDiv(show_div)
{
//	document.getElementById("label").value = '';
	document.getElementById(show_div).style.display='none';
//	document.getElementById(bg_div).style.display='none';
}

