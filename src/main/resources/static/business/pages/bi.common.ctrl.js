$(function(){
	 var fulls = "left=0,screenX=0,top=0,screenY=0,scrollbars=1";    //定义弹出窗口的参数
	 if (window.screen) {
		 var ah = screen.availHeight - 30;
	     var aw = screen.availWidth - 10;
	     fulls += ",height=" + ah;
	     fulls += ",innerHeight=" + ah;
	     fulls += ",width=" + aw;
	     fulls += ",innerWidth=" + aw;
	     fulls += ",resizable"
	 } else {
	     fulls += ",resizable"; // 对于不支持screen属性的浏览器，可以手工进行最大化。 manually
	 }
	 function openNewWindow(url,name){
		 window.open(url,name,fulls);
	 }

	//访问BI
	$("#showBI").click(function(){
		openNewWindow("map","数据监控");
	});

});
