<form name="formmyhome">
	<table width="52%" border="0">
	{if success!=0}<tr><td width="34%"><font color="#FF0000">错误！修改不成功！请检查您输入的密码是否正确。</font></td><td width="66%"></td></tr>{/if}
	  <tr>
		<td style="text-align:right;">我的姓名：</td>
		<td><input id="username" size="25" name="username" value="{$userinfo.name}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的Email：</td>
		<td><input id="email" size="25" name="email" value="{$userinfo.email}" />{if erroremail==1}<font color="#FF0000">Email错误！</font>{/if}</td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的密码：</td>
		<td><input name="oldpassword" type="password" id="oldpassword" size="25" />{if errorpassword==1}<font color="#FF0000">密码错误！</font>{/if}</td>
	  </tr>
	  <tr>
		<td style="text-align:right;">修改密码：</td>
		<td><input name="password" type="password" id="password" size="25" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">请再次输入修改密码：</td>
		<td><input name="repassword" type="password" id="repassword" size="25" /></td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的联系电话：</td>
		<td><input id="phone" size="25" name="phone" value="{$userinfo.phone}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的移动电话：</td>
		<td><input id="mobile" size="25" name="mobile" value="{$userinfo.mobile}" /></td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的邮寄地址：</td>
		<td><input id="address" size="25" name="address" value="{$userinfo.address}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">地址邮编：</td>
		<td><input id="postcode" size="25" name="postcode" value="{$userinfo.postcode}" /></td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的性别：</td>
		<td><input id="sex" size="25" name="sex" value="{$userinfo.sex}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">我的生日：</td>
		<td><input id="birthday" size="25" name="birthday" value="{$userinfo.birthday}" /></td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">QQ：</td>
		<td><input id="qq" size="25" name="qq" value="{$userinfo.qq}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">MSN：</td>
		<td><input id="msn" size="25" name="msn" value="{$userinfo.msn}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">taobao旺旺：</td>
		<td><input id="taobao" size="25" name="taobao" value="{$userinfo.taobao}" /></td>
	  </tr>
	  <tr>
		<td style="text-align:right;">Skype：</td>
		<td><input id="skype" size="25" name="skype" value="{$userinfo.skype}" /></td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	  <tr>
		<td></td>
		<td><input name="modify" type="button" onclick="checkdata()" value="修改" /> <input value="重填" type="reset" /></td>
	  </tr>
	  <tr>
		<td></td>
		<td></td>
	  </tr>
	</table>
</form>