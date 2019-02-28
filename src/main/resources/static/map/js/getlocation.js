$(function() {
	var lng = $("#lon", parent.document).val();
	var lat = $("#lat", parent.document).val();
	
	var ah = screen.availHeight - 30;
	var aw = screen.availWidth - 10;
	$("#container").width(aw);
	$("#container").height(ah);
	//map中的container必须与div中id保持一致
	var map = new BMap.Map("container");
	lng = lng? lng-0:118.816114;
	lat = lat? lat-0:32.061308;
	var point = new BMap.Point(lng, lat);
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

/*	//显示范围
	var bound = new BMap.Bounds(new BMap.Point(114.785199, 34.286661),
			new BMap.Point(115.301473, 34.581061));
	try {
		BMapLib.AreaRestriction.setBounds(map, bound);
	} catch (e) {
		alert(e);
	}*/
	if(lng && lat){
		var marker = new BMap.Marker(new BMap.Point(lng, lat));
		map.addOverlay(marker);
	}
	
	//逆地址解析（将经纬度转化为地址）
	var geoc = new BMap.Geocoder();
	map.enableContinuousZoom(); //启用地图惯性拖拽，默认禁用
	//添加单击事件
	map.addEventListener("click", function(e) {
		map.clearOverlays();//清空原来的标注
		$("#longitude").val(e.point.lng);
		$("#latitude").val(e.point.lat);
		var marker = new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat)); // 创建标注，为要查询的地方对应的经纬度
		map.addOverlay(marker);
		geoc.getLocation(e.point, function(rs) {
			var addComp = rs.addressComponents;
			$("#province", parent.document).val(addComp.province);
			$("#city", parent.document).val(addComp.city);
			$("#district", parent.document).val(addComp.district);
			$("#street", parent.document).val(addComp.street+addComp.streetNumber);
			$("#streetNumber", parent.document).val(addComp.streetNumber);

			setLatLon();
		});
	});
	//复制经纬度
	$("#copyMapLocation").click(function(){
		var lng = $("#longitude").val();
		var lat = $("#latitude").val();
		$("#lon", parent.document).val(lng);
		$("#lat", parent.document).val(lat);
		parent.window.closeMap();
	});
	
});

function setLatLon (){
	var lng = $("#longitude").val();
	var lat = $("#latitude").val();
	$("#lon", parent.document).val(lng);
	$("#lat", parent.document).val(lat);
	parent.window.closeMap();
}