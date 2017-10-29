<!--//
$(document).ready(function() {    
   $('#oldpass').blur(function (){
     var params=$('#ChangePassFrm').serialize(); //序列化表单的值
     $.ajax({
       url:'../ajax.php', //后台处理程序
       type:'post',         //数据发送方式
       dataType:'json',     //接受数据格式
       data:params,         //要传递的数据
       success:update_checkoldpwdDiv  //回传函数(这里是函数名)
     });
   });
   $('#dataMemberUsername').blur(function (){
	 var params = $('#getPasswdFrm').serialize(); //序列化表单的值
	 var action = "checkusername";
     $.ajax({
       url:'ajax.php?action='+action, //后台处理程序
       type:'get',         //数据发送方式
       dataType:'json',     //接受数据格式
       data:params,         //要传递的数据
       success:update_checkusernameDiv  //回传函数(这里是函数名)
     });
   });
});
function checkInput(){
	if($('#oldpass').val() == ""){
		alert("请填写原来的密码！");
		$('#oldpass').focus();
		return false;
	}
	if($('#newpass').val() != $('#re_newpass').val()){
		alert("前后密码输入不一致");
		$('newpass').focus();
		return false;
	}
	if($('#newpass').val() == $('#oldpass').val()){
		alert("密码与原密码一致，无须修改");
		$('#newpass').focus();
		return false;
	}
	$("#BtnChangePwd").attr('disabled', false);
	document.changepassfrm.submit();
}
function update_checkoldpwdDiv (data)
{
	var errorNumber = data.isError;
	if(errorNumber!=0)
	{
		$("#checkoldpwdDiv").html('<img src="../images/check_error.gif" alt="验证失败" />');
		$("#BtnChangePwd").attr('disabled', true);
	}else{
		$("#checkoldpwdDiv").html('<img src="../images/check_right.gif" alt="验证通过" />');
		$("#BtnChangePwd").attr('disabled', false);
	}
}
//-->