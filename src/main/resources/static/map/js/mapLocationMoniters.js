$(function() {
	debugger;
	var ah = screen.availHeight - 30;
	var aw = screen.availWidth - 10;
	$("#container").width(aw);
	$("#container").height(ah);
	//map中的container必须与div中id保持一致
	var map = new BMap.Map("container");
	var point = new BMap.Point(118.816114,32.061308);
	map.centerAndZoom(point, 14);
	map.enableScrollWheelZoom();//启用滚轮放大缩小
	map.addEventListener("tilesloaded", function() {
		$(".BMap_cpyCtrl").hide();
		$(".anchorBL").hide();
	});//地图加载完毕后 隐藏百度图标

	//比例尺,缩放平移控件
	var top_left_control = new BMap.ScaleControl({
		anchor : BMAP_ANCHOR_TOP_LEFT
	});
	var top_left_navigation = new BMap.NavigationControl();
	map.addControl(top_left_control);
	map.addControl(top_left_navigation);
	//逆地址解析（将经纬度转化为地址）
	var geoc = new BMap.Geocoder();
	map.enableContinuousZoom(); //启用地图惯性拖拽，默认禁用
	
	var data_info = parent.mapMoniterDatas || [];
	for(var i=0; i < data_info.length; i++){
		var lon = $(data_info[i][0]).attr("lon")-0;
		var lat = $(data_info[i][0]).attr("lat")-0;
		//自定义图标
		var option = {
			imgSrc: "map/images/marker_red_sprite.png",
			lon: lon,
			lat: lat,
			moniterId: data_info[i][1],
			width:'39px',
			height:'25px'
		}
		initCustomOverlay(option,map);
	}
	

	//自定义覆盖物
	function initCustomOverlay(option, map){
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
			arrow.src = option.imgSrc;//"../baojing.gif";
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
		//3、绘制覆盖物实现绘制方法
		ComplexCustomOverlay.prototype.draw = function(){
			var map = this._map;
			var pixel = map.pointToOverlayPixel(this._point);
			this._div.style.left = pixel.x - parseInt(this._arrow.style.left) + "px";
			this._div.style.top  = pixel.y - 30 + "px";
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
});
