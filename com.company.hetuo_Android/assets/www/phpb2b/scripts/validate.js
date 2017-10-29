<!--//
$(document).ready(function(){
    $("#LoggingFrm").validate({
		rules: {
			"data[login_name]": { required: true},
			"data[login_pass]": { required: true}
		},
		messages: {
			"data[login_name]": "请输入正确的用户名",
			"data[login_pass]": "请输入密码"
		}
	});
    $("#PostFrm").validate({
		rules: {
			"data[trade][title]": { required: true,"minlength":4},
			"data[trade][content]": { required: true}
		},
		messages: {
			"data[trade][title]": "请输入供求标题",
			"data[trade][content]": "请输入供求详情"
		}
	});  
    $("#regfrm").validate({
		rules: {
			"data[member][username]": { required: true,rangelength:[5,20]},
			"data[member][userpass]": { required: true,rangelength:[6,20]},
			"data[member][email]": { required: true, email:true},
			"re_memberpass": { required: true, equalTo: "#memberpass"}
		},
		messages: {
			"data[member][username]": {
				required:"请输入正确的用户名",
				rangelength:"请输入介于5-20个字符的用户名"
			},
			"data[member][userpass]": {
				required:"请输入密码",
				rangelength:"请输入介于6-20个字符的密码"
			},
			"data[member][email]": "请输入正确的Email",
			"re_memberpass": "密码不一致请重新输入"
		}
	}); 

   $('#dataMemberUsername').blur(function (){
	 var username = $("#dataMemberUsername").val();
	 if(username.length<5){
		 return;
	 }
	 var params = "username="+username;
	 var action = "checkusername";
     $.ajax({
       url:'ajax.php?action='+action,
       type:'get',
       dataType:'json',
       data:params,
       success:update_checkusernameDiv
     });
   });	
   
   $('#dataUsername').blur(function (){
	 var username = $("#dataMemberUsername").val();
	 if(username.length<5){
		 return;
	 }
	 var params = "username="+username;
	 var action = "checkusername";
     $.ajax({
       url:'ajax.php?action='+action,
       type:'get',
       dataType:'json',
       data:params,
       success:getpass_checkusernameDiv
     });
   });	

   $('#memberpass').blur(function (){
	 var userpass = $("#memberpass").val();
	  if(userpass.length<6){
		 return;
	    }
	 });

   $('#exchange_imgcapt').click(function (){
	 $('#imgcaptcha').attr('src','captcha.php?sid=' + Math.random());
	 $('#login_auth').focus();
	 return false;
   });	

   $('#dataMemberEmail').blur(function (){
	 var email = $("#dataMemberEmail").val();
	 if(email.length<5){
		 return;
	 }
	 var params = "email="+email;
	 var action = "checkemail";
     $.ajax({
       url:'ajax.php?action='+action,
       type:'get',
       dataType:'json',
       data:params,
       success:update_checkemailDiv
     });
   });	
});
function update_checkusernameDiv(data){
	var errorNumber = data.isError;
	if(errorNumber!=0)
	{
		$("#Submit").attr('disabled', true);
		$("#membernameDiv").html('<img src="images/check_error.gif" alt="不能使用">用户名已经存在');
	}else{
		$("#Submit").attr('disabled', false);
		$("#membernameDiv").html('<img src="images/check_right.gif" alt="可以使用">');
	}
}
function update_checkemailDiv(data){
	var errorNumber = data.isError;
	if(errorNumber!=0)
	{
		$("#Submit").attr('disabled', true);
		$("#memberemailDiv").html('<img src="images/check_error.gif" alt="不能使用">Email已经存在');
	}else{
		$("#Submit").attr('disabled', false);
		$("#memberemailDiv").html('<img src="images/check_right.gif" alt="可以使用">');
	}
}
function login(frm){
	if($('#LoginName').val() == ""){
			alert("请输入正确的用户名");
			$('#LoginName').focus();
			return false;
		}else if($('#UserEmail').val() == ""){
			alert("请输入Email地址");
			$('#UserEmail').focus();
			return false;
	}
}
function getpass_checkusernameDiv(data){
	var errorNumber = data.isError;
	if(errorNumber!=0)
	{
		$("#GoNext").attr('disabled', true);
		$("#checkusernameDiv").html('验证失败');
	}else{
		$("#GoNext").attr('disabled', false);
		$("#checkusernameDiv").html('');
	}
}
//-->