// �����������ļ�
var outlookbar=new outlook();
var t;
t=outlookbar.addtitle('��������','������ҳ',1)
outlookbar.additem('�鿴��������',t,'adminLookSelf.action')
outlookbar.additem('�޸ĸ�������',t,'adminupdate.jsp')
outlookbar.additem('���ĵ�¼����',t,'adminmodifypassword.jsp')

t=outlookbar.addtitle('�������','������ҳ',1)
outlookbar.additem('��¼��ѧ��̳',t,'../vbb/forumdisplay.php?s=320e689ffabc5daa0be8b02c284d9968&forumid=39')
outlookbar.additem('���������ʼ�',t,'mailto:wangyong31893189@163.com')

t=outlookbar.addtitle('��������','������ҳ',1)
outlookbar.additem('��δͨ������',t,'un_pass.php')
outlookbar.additem('�Ѿ�ͨ������',t,'al_pass.php')
outlookbar.additem('�޸���������',t,'modify.php')
outlookbar.additem('׫д��������',t,'sub_new.php')
outlookbar.additem('Ͷ�����ѧ��',t,'#')

t=outlookbar.addtitle('�˳�ϵͳ','������ҳ',1)
outlookbar.additem('����˳���¼',t,'loginout.jsp')

t=outlookbar.addtitle('��������','ϵͳ����',1)
outlookbar.additem('�鿴��������',t,'adminLookSelf.action')
outlookbar.additem('�޸ĸ�������',t,'adminupdate.jsp')
outlookbar.additem('���ĵ�¼����',t,'adminmodifypassword.jsp')

t=outlookbar.addtitle('�û�����','ϵͳ����',1)
outlookbar.additem('�鿴����(���|����)',t,'adminqueryUser.action')
outlookbar.additem('ɾ������',t,'userdelete.jsp')
outlookbar.additem('�������|����',t,'useradd.jsp')

t=outlookbar.addtitle('�������','ϵͳ����',1)
outlookbar.additem('��¼��ѧ��̳',t,'/bbs/index.jsp')
outlookbar.additem('���������ʼ�',t,'mailto:wangyong31893189@163.com')



