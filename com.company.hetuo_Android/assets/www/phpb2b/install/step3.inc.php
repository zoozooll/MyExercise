<?php include 'header.share.php';?>
	 <table width="100%" cellpadding="0" cellspacing="0" class="table_list">
                  <tr>
                    <th><?php echo $_check_project;?></th>
                    <th><?php echo $_current_env;?></th>
                    <th><?php echo $_suggest_env;?></th>
                    <th><?php echo $_function_respond;?></th>
                  </tr>
                  <tr>
                    <td><?php echo $_operation_system;?></td>
                    <td><?php echo php_uname();?></td>
                    <td>Windows_NT/Linux/Freebsd</td>
                    <td><font color="yellow">&radic;</font></td>
                  </tr>
                  <tr>
                    <td>Web <?Php echo $_server;?></td>
                    <td><?php echo $_SERVER['SERVER_SOFTWARE'];?></td>
                    <td>Apache/IIS</td>
                    <td><font color="yellow">&radic;</font></td>
                  </tr>
                  <tr>
                    <td>php <?Php echo $_version;?></td>
                    <td>php <?php echo phpversion();?></td>
                    <td>php 4.3.0 <?php echo $_or_higher;?></td>
                    <td><?php if(phpversion() >= '4.3.0'){ ?><font color="yellow">&radic;<?php }else{ ?><font color="red"><?php echo $_cant_install;?></font><?php }?></font></td>
                  </tr>
                  <tr>
                    <td>Mysql <?php echo $_extension;?></td>
                    <td><?php if(extension_loaded('mysql')){ ?>&radic;<?php }else{ ?>&times;<?php }?></td>
                    <td><?php echo $_open_fine;?></td>
                    <td><?php if(extension_loaded('mysql')){ ?><font color="yellow">&radic;</font><?php }else{ ?><font color="red"><?php echo $_cant_install;?></font><?php }?></td>
                  </tr>
                  <tr>
                    <td>GD <?php echo $_extension;?></td>
                    <td><?php if($gd_support){ ?>&radic; (<?php echo $_support;?> <?php echo $gd_support;?>)<?php }else{ ?>&times;<?php }?></td>
                    <td><?php echo $_open_fine;?></td>
                    <td><?php if($gd_support){ ?><font color="yellow">&radic;</font><?php }else{ ?><font color="red"><?php echo $_gd_not_support;?></font><?php }?></td>
                  </tr>
                  <tr>
                    <td>Zlib <?php echo $_extension;?></td>
                    <td><?php if(extension_loaded('zlib')){ ?>&radic;<?php }else{ ?>&times;<?php }?></td>
                    <td><?php echo $_open_fine;?></td>
                    <td><?php if(extension_loaded('zlib')){ ?><font color="yellow">&radic;</font><?php }else{ ?><font color="red">不支持Gzip功能</font><?php }?></td>
                  </tr>
                  <tr>
                    <td>Iconv/mb_string <?php echo $_extension;?></td>
                    <td><?php if(extension_loaded('iconv') || extension_loaded('mbstring')){ ?>&radic;<?php }else{ ?>&times;<?php }?></td>
                    <td><?php echo $_open_fine;?></td>
                    <td><?php if(extension_loaded('iconv') || extension_loaded('mbstring')){ ?><font color="yellow">&radic;</font><?php }else{ ?><font color="red"><?php echo $_low_words_convert;?></font><?php }?></td>
                  </tr>
                  <tr>
                    <td>allow_url_fopen</td>
                    <td><?php if(ini_get('allow_url_fopen')){ ?>&radic;<?php }else{ ?>&times;<?php }?></td>
                    <td><?php echo $_open_fine;?></td>
                    <td><?php if(ini_get('allow_url_fopen')){ ?><font color="yellow">&radic;</font><?php }else{ ?><font color="red"><?php echo $_distance_image_not_allowed;?></font><?php }?></td>
                  </tr>
                  <tr>
                    <td>PHP<?php echo $_information;?> PHPINFO</td>
                    <td colspan="3" align="center"><a href="install.php?act=phpinfo" target="_blank" style="text-decoration:underline;" title="<?php echo L("view_information", "tpl", "phpinfo");?>">PHPINFO</a></td>
                  </tr>
                </table>
<form id="install" action="install.php" method="get">
<input type="hidden" name="step" value="4">
</form>
<input type="button" onclick="javascript:history.go(-1);" value="<?php echo $_go_back;?> : <?php echo $steps[--$step];?>" class="btn" /><?php if($is_right) { ?>
<input type="button" onClick="$('#install').submit();" class="btn" value="<?php echo $_next_step;?> : <?php echo $_perm_check;?>" />
<?php }else{ ?>
<a onclick="alert('<?php echo $_env_not_allowed;?>');" class="btn"><span><?php echo $_not_check_cant_install;?></span></a>
 <?php }?>
  </div>
</div>
</body>
</html>