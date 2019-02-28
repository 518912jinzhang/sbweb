$(document).ready(function(){
	$("#login").click(function(event){
		event.stopPropagation();
		if(!$("#identifyCode").val()){
			alert("请先获取验证码！");
		} else {
			$("form").submit();
		}
	});
	
	//获取验证码
	$("#btnIdentifCode").click(function(){
		if($(this).attr("disabled") != "disabled"){
			$.ajax({
				  type: 'POST',
				  url: 'getIdentifCode',
				  data: {id:$("#id").val(), pwd:$("#pwd").val()},//{id:'Mri', pwd:'MriManager'},
				  success: function(data){
					  $("#errMsg").hide();
					  if(data.isUserExist == 'LE'){
						  $("#errMsg").show();
						  $("#errMsg").html("账号、密码错误，请重新输入！");
					  } else if(data.isUserExist == 'LC'){
						  $("#errMsg").show();
						  $("#errMsg").html("验证码错误，请重新输入！");
					  } else if(data.isUserExist == 'LT'){
						  $("#errMsg").show();
						  $("#errMsg").html("验证码超时，请重新输入！");
					  } else {
						  //$("#errMsg").html("验证码发送成功");
						  time();
						  //$("#identifyCode").val(data.identifyCode);
					  }
				  },
				  dataType: 'json'
			});
		}
	});
	var wait=60;//时间  
    var t;//计时器  
    function time(){
        if (wait == 0) {  
            $('#btnIdentifCode').removeAttr('disabled'); 
            $('#btnIdentifCode').html("获取验证码");  
            wait = 60;  
        }else{  
            $('#btnIdentifCode').attr('disabled','disabled');  
            $('#btnIdentifCode').html("重新发送验证码(" + wait + "s)");  
            wait--;  
            t=setTimeout(function(){  
                time();
            },1000)  
        }  
    }  
    function stopTime(){  
        clearTimeout(t);  
    }
});