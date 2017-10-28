// 导航栏配置文件
var outlookbar=new outlook();
var t;
t=outlookbar.addtitle('基本设置','管理首页',1)
outlookbar.additem('查看个人资料',t,'adminLookSelf.action')
outlookbar.additem('修改个人资料',t,'adminupdate.jsp')
outlookbar.additem('更改登录密码',t,'adminmodifypassword.jsp')

t=outlookbar.addtitle('广告设置','管理首页',1)
outlookbar.additem('登录文学论坛',t,'../vbb/forumdisplay.php?s=320e689ffabc5daa0be8b02c284d9968&forumid=39')
outlookbar.additem('发出电子邮件',t,'mailto:wangyong31893189@163.com')

t=outlookbar.addtitle('新闻设置','管理首页',1)
outlookbar.additem('尚未通过文章',t,'un_pass.php')
outlookbar.additem('已经通过文章',t,'al_pass.php')
outlookbar.additem('修改现有文章',t,'modify.php')
outlookbar.additem('撰写最新文章',t,'sub_new.php')
outlookbar.additem('投稿给文学报',t,'#')

t=outlookbar.addtitle('退出系统','管理首页',1)
outlookbar.additem('点击退出登录',t,'loginout.jsp')

t=outlookbar.addtitle('基本设置','系统设置',1)
outlookbar.additem('查看个人资料',t,'adminLookSelf.action')
outlookbar.additem('修改个人资料',t,'adminupdate.jsp')
outlookbar.additem('更改登录密码',t,'adminmodifypassword.jsp')

t=outlookbar.addtitle('用户管理','系统设置',1)
outlookbar.additem('查看所有(买家|卖家)',t,'adminqueryUser.action')
outlookbar.additem('删除卖家',t,'userdelete.jsp')
outlookbar.additem('增加买家|卖家',t,'useradd.jsp')

t=outlookbar.addtitle('广告设置','系统设置',1)
outlookbar.additem('登录文学论坛',t,'/bbs/index.jsp')
outlookbar.additem('发出电子邮件',t,'mailto:wangyong31893189@163.com')



