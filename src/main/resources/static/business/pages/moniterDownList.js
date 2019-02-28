var interval = 5;
//定时
/*var pageFreshInterval = setInterval(function (){
	location.replace(location.href);
},1000*interval);*/

$(function(){
	$('.table-sort').dataTable({
		"aaSorting": [[1,'asc']],//默认第几个排序
		"bStateSave": true,//状态保存
		"aoColumnDefs": [
		  //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
		  {"orderable":false,"aTargets":[0,2,3,4]}// 制定列不参与排序
		]
	});
	
	$("#updateInterval").click(function(){
		interval = $(this).val();
		clearInterval(pageFreshInterval);
		pageFreshInterval = setInterval(function (){
			$('#gridData').load("moniterDownListLoad");
		}, 1000*interval);
	});
	
	//停止刷新
	$("#stopFresh").click(function(){
		if(!$(this).prop("checked") && pageFreshInterval){
			console.log("消除定时")
			clearInterval(pageFreshInterval);
		} else {
			pageFreshInterval = setInterval(function (){
				$('#gridData').load("moniterDownListLoad");
			}, 1000*interval);
		}
	});
});

/*用户-添加*/
function member_add(title,url,w,h){
	layer_show(title,url,w,h);
}
/*关注终端-添加*/
function moniterAttention_add(title,url,w,h){
	url += "?ids="+"123456-111111-200001";
	layer_show(title,url,w,h);
}
/*用户-查看*/
function member_show(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*用户-停用*/
function member_stop(obj,id){
	layer.confirm('确认要停用吗？',function(index){
		$.ajax({
			type: 'POST',
			url: '',
			dataType: 'json',
			success: function(data){
				$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_start(this,id)" href="javascript:;" title="启用"><i class="Hui-iconfont">&#xe6e1;</i></a>');
				$(obj).parents("tr").find(".td-status").html('<span class="label label-defaunt radius">已停用</span>');
				$(obj).remove();
				layer.msg('已停用!',{icon: 5,time:1000});
			},
			error:function(data) {
				console.log(data.msg);
			},
		});		
	});
}

/*用户-启用*/
function member_start(obj,id){
	layer.confirm('确认要启用吗？',function(index){
		$.ajax({
			type: 'POST',
			url: '',
			dataType: 'json',
			success: function(data){
				$(obj).parents("tr").find(".td-manage").prepend('<a style="text-decoration:none" onClick="member_stop(this,id)" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>');
				$(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
				$(obj).remove();
				layer.msg('已启用!',{icon: 6,time:1000});
			},
			error:function(data) {
				console.log(data.msg);
			},
		});
	});
}
/*终端-编辑*/
function moniter_edit(title,url,id,w,h,obj){
	layer_show(title,url+"?id="+obj.getAttribute("value"),w,h);
}
/*心跳-设置*/
function heartbeat_Edit(title,url,id,w,h,obj){
	layer_show(title,url+"?id="+obj.getAttribute("value"),w,h);
}
/*查看终端详情*/
function moniter_view(title,url,id,w,h,obj){
	var index = layer.open({
		type: 2,
		title: title,
		content: url+"?id="+obj.getAttribute("value")
	});
	layer.full(index);
	//layer_show(title,url+"?id="+obj.getAttribute("value"),w,h);	
}
/*查看设备详情*/
function device_view(title,url,id,w,h,obj){
	url += "?id="+obj.getAttribute("value")+"&ip="+obj.outerText;
	layer_show(title,url,w,h);	
}
/*终端-删除*/
function moniter_del(obj,id){
	layer.confirm('确认要删除吗？',function(index){
		$.ajax({
			type: 'POST',
			url: 'moniterDelete',
			data:{id: obj.getAttribute("value")},
			success: function(data){
				$(obj).parents("tr").remove();
				layer.msg('删除成功!',{icon:1,time:1000});
			},
			error:function(data) {
				console.log(data.msg);
				layer.msg('删除失败!',{icon:1,time:1000});
			},
		});		
	});
}
//发送消息请求
function sendMsg(){
	$.ajax({
		  type: 'POST',
		  url: 'getSystemMsg',
		  data: {id:$("#id").val()},
		  success: function(data){
			  console.log(data.length);
			   $("#divSystemMsg").text(data.length);
		  },
		  dataType: 'json'
	});
}