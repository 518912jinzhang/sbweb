$(function(){
	$('.table-sort').dataTable({
		"aaSorting": [[1,'asc']],//默认第几个排序
		"bStateSave": true,//状态保存
		"aoColumnDefs": [
		  //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
		  {"orderable":false,"aTargets":[0,8]}// 制定列不参与排序
		]
	});
	
});

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

/*获取列表数据*/
function controlTableDatas() 
{
    var salesDetailTable = new $.fn.dataTable.Api('#gridTable');
    var length = salesDetailTable.rows().data().length;
    var list = [];
    for (var i = 0; i < length; i++) {
        list.push(salesDetailTable.rows().data()[i]);
    }
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
/*打开模块窗口*/
function openModalDialog(title,url,id,w,h,obj){
	layer_show(title,url+"?id="+obj.getAttribute("value"),w,h);
}
/*打开模块窗口*/
function openModalDialogFull(title,url,id,w,h,obj){
	url = url+"?id="+obj.getAttribute("value");
	var index = layer.open({
		type: 2,
		title: title,
		content: url
	});
	layer.full(index);
}

/*密码-修改*/
function change_password(title,url,id,w,h,obj){
	layer_show(title,url+"?id="+obj.getAttribute("value"),w,h);	
}

/*用户-删除*/
function member_del(obj,id){
	layer.confirm('确认要删除吗？',function(index){
		$.ajax({
			type: 'POST',
			url: 'personDelete',
			data:{id: obj.getAttribute("value")},
			dataType: 'json',
			success: function(data){
				$(obj).parents("tr").remove();
				layer.msg('已删除!',{icon:1,time:1000});
				windown.href = location.replace(location.href);
			},
			error:function(data) {
				console.log(data.msg);
			},
		});		
	});
}