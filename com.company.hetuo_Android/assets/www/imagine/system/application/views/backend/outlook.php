<html>
	<head>
		<script type="text/javascript" src="<?=base_url();?>system/js/backend/index/outlook.js"></script>
        <script type="text/javascript" src="<?=base_url();?>system/js/backend/jquery.js"></script>
	</head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<body marginwidth="0" marginheight="0" bgcolor="#D0D0D0">
		<script type="text/javascript">
		var OUTLOOKBAR_DEFINITION = {
	format:{
		target:'main',
		blankImage:'<?=base_url();?>system/images/backend/b.gif',
		rollback:true,
		animationSteps:5,
		animationDelay:30,
		templates:{
			panel:{
			common:'<table width="100%" height="37" border="0" cellspacing="0" cellpadding="0" background="<?=base_url();?>system/images/backend/panel_middle_{state}.gif"><tr><td><img src="<?=base_url();?>system/images/backend/panel_left_{state}.gif" width="10" height="37" /></td><td align="center"><div style="font: bold 11pt trebuchet ms, arial;">{text}</div></td><td align="right"><img src="<?=base_url();?>system/images/backend/panel_right_{state}.gif" width="10" height="37" /></td></tr></table>',
		    normal:{state:'n'},
			rollovered:{state:'r'},
			clicked:{state:'c'}
			},
			
			item:{
				common:'<table border="0" width="100%"><tr><td><table width="100%" bgcolor="{borderColor}" border="0" cellspacing="1" cellpadding="0"><tr><td><table width="100%" border="0" bgcolor="{backgroundColor}" cellspacing="0" cellpadding="5"><tr align="center"><td><img src="<?=base_url();?>system/images/backend/icon_{icon}_{state}.gif" width="48" height="48" /></td></tr><tr align="center"><td><span style="font: 9pt verdana;">{text}</span></td></tr></table></td></tr></table></td></tr></table>',
				normal:{borderColor:'#D0D0D0', backgroundColor:'#D0D0D0', state:'n'},
				rollovered:{borderColor:'#0A246A', backgroundColor:'#B6BDD2', state:'r'}
			},
			
			upArrow:{
				common:'<img src="<?=base_url();?>system/images/backend/btn_up_{state}.gif" width="26" height="26" />',
				normal:{state:'n'},
				rollovered:{state:'r'},
				clicked:{state:'c'}
			},
			downArrow:{
				common:'<img src="<?=base_url();?>system/images/backend/btn_down_{state}.gif" width="26" height="26" />',
				normal:{state:'n'},
				rollovered:{state:'r'},
				clicked:{state:'c'}
			}
		}
	},
	panels:[
		{text:"产品",
			items:[
				{text:"明细", icon:'12', url:"<?=site_url('backend/product/all_view')?>"},
				{text:"增加", icon:'12', url:"<?=site_url('backend/product/add_view')?>"}
			]
		},

		{text:"技术资料",
			items:[
				{text:"明细", icon:'14', url:"<?=site_url('backend/skill/all_view')?>"},
				{text:"增加", icon:'14', url:"<?=site_url('backend/skill/add_view')?>"}
			]
		},

		/*{text:"公司动态",
			items:[
				{text:"明细", icon:'15', url:"<?=site_url('backend/company/all_view')?>"},
				{text:"增加", icon:'15', url:"<?=site_url('backend/company/add_view')?>"}
			]
		},*/
		
		{text:"新闻中心 ",
			items:[
				{text:"明细", icon:'16', url:"<?=site_url('backend/shows/all_view')?>"},
				{text:"增加", icon:'16', url:"<?=site_url('backend/shows/add_view')?>"}
			]
		},
		/*{text:"相关文章 ",
			items:[
				{text:"明细", icon:'16', url:"<?=site_url('backend/article/all_view')?>"},
				{text:"增加", icon:'16', url:"<?=site_url('backend/article/add_view')?>"}
			]
		},*/
		{text:"管理中心",
			items:[
				{text:"明细", icon:'16', url:"<?=site_url('backend/user/all_view')?>"},
				{text:"增加", icon:'16', url:"<?=site_url('backend/user/add_view')?>"}
			]
		},
	  {text:"首页横幅",
			items:[
				{text:"明细", icon:'14', url:"<?=site_url('backend/head/all_view')?>"},
				{text:"增加", icon:'14', url:"<?=site_url('backend/head/add_view')?>"}
			]
		},
		{text:"退出",
			items:[
				{text:"退出", icon:'17', url:"<?=site_url('backend/index/logout')?>"}
				
			]
		}
	]
};
		 new COOLjsOutlookBar(OUTLOOKBAR_DEFINITION);
		</script>
	</body>
</html>
