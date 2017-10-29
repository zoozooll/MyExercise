var comment_case = '';
var comment_handel = '';
function showCommList(type,id,element,a){
	ajax.send('apps.php?q=ajax&a=showcommlist','type='+type+'&id='+id,function(){
		var rText = ajax.request.responseText.split('\t');
		if (rText[0] == 'success') {
			if (typeof(element)=='string') {
				element = getObj(element);
			}
			var whole	= elementBind('div');
			
			var comm_post	= elementBind('div',false,'cc');
			
			var input_p	= elementBind('p','comm_p_'+type+'_'+id+'_'+0);
			var span = elementBind('span',false,'fl');
			input_p.appendChild(span);
			
			var comm_input	= elementBind('input','comm_input_'+type+'_'+id+'_'+0,'input','width: 200px;');
			comm_input.setAttribute('type','text');
			comm_input.setAttribute('name','title');
			comm_input.setAttribute('maxlength','200');
			input_p.appendChild(comm_input);
			
			var comm_list	= elementBind('div','comm_list_'+type+'_'+id);
			whole.appendChild(comm_list);
			element.appendChild(whole);
			if (rText[1] != '') {
				var comments =  JSONParse(rText[1]);
				if (typeof(comments)=='object') {
					for (var i in comments) {
						if (typeof(comments[i])=='object') {
							creatComment(comments[i],comm_list,'0');
						}
					}
				}
			}
			this.container = comm_list;
			comment_handel = comm_list;

			var comm_button = elementBind('input',false,'btn','margin-left: 5px;cursor: pointer;');
			comm_button.setAttribute('type','button');
			comm_button.setAttribute('value','回复');
			comm_button.onclick = function () {
				sendComment(type,id,0,'comm_list_'+type+'_'+id);
			}
			input_p.appendChild(comm_button);
			comm_post.appendChild(input_p);
			whole.appendChild(comm_post);
			
			a.innerHTML = '收起回复';
			a.onclick	= function () {
				this.innerHTML = '回复(' + comm_list.getElementsByTagName('dl').length + ')';
				this.onclick = function() {
					if (whole.style.display=='none') {
						whole.style.display	= '';
						this.innerHTML	= '收起回复';
					} else {
						whole.style.display = 'none';
						this.innerHTML	= '回复(' + comm_list.getElementsByTagName('dl').length + ')';
					}
				}
				whole.style.display	= 'none';
			}
			comm_input.focus();
			if(IsElement('upPanel')){
				getObj('upPanel').scrollTop+= 50;
			}else{
				document.documentElement.scrollTop += 50;
			}
			
		} else {
			ajax.guide();
		}
	});
}

function showCommCase(){
	if (comment_case != '') {
		arr	= comment_case.split('_');
		if (arr[4] != 0) {
			delElement(comment_case);
		} else {
			var base_s_p = getObj('comm_p_'+arr[2]+'_'+arr[3]+'_'+0);
			base_s_p.style.display = 'none';
			var base_h_p = getObj('comm_hidden_p_'+arr[2]+'_'+arr[3]);
			base_h_p.style.display = '';
		}
	}
}

function creatComment(obj,element,ifpost){
	if (typeof(obj)!='object') {
		return false;
	}
	var id	= obj['id'];
	var uid = obj['uid'];
	var type 	= obj['type'];
	var typeid 	= obj['typeid'];
	var username= obj['username'];
	var title	= obj['title'];
	var postdate= obj['postdate'];
	var upid	= obj['upid'];
	var iconurl = obj['face'];
	
	var dl	= elementBind('dl','comment_'+id,'cc');
	
	var dt	= elementBind('dt','','fl mr10');
	var img	= elementBind('img','','fl img-20');
	img.src	= iconurl;
	dt.appendChild(img);
	dl.appendChild(dt);
	var dd = elementBind('dd');
	var user	= elementBind('a',false,'mr10');
	user.href	= modeBase + '&q=user&u=' + uid;
	user.innerHTML = username+':';
	dd.appendChild(user);
	
	var content	= elementBind('span',false,'mr10');
	content.innerHTML = title;
	dd.appendChild(content);
	
	var time	= elementBind('span',false,'gray mr10 f10');
	time.innerHTML = postdate;
	dd.appendChild(time);
	
	if (winduid == uid || groupid == 3) {
		var dela= elementBind('a','del_'+id,'del','cursor: pointer;');
		dela.onclick = function () {
			ajax.send('apps.php?q=ajax&a=commdel','id='+id,function(){
				var rText = ajax.request.responseText.split('\t');
				if (rText[0] == 'success') {
					delElement('comment_'+id);
				} else {
					ajax.guide();
				}
			});
		};
		dela.innerHTML = '删除';
		dd.appendChild(dela);
	}
	
	dl.appendChild(dd);	
	
	if (typeof(element)=='object') {
		if (ifpost == 1) {
			var dls = element.getElementsByTagName('dl');
			if (typeof(dls)=='object' && dls.length>1) {
				var firstdd = dls[0];
				element.insertBefore(dl,firstdd);
			} else {
				element.appendChild(dl);
			}
		}else{
			element.appendChild(dl);
		}
	}
}

function sendComment(type,typeid,upid,container){
	if (getObj('comm_input_'+type+'_'+typeid+'_'+upid)) {
		var title = getObj('comm_input_'+type+'_'+typeid+'_'+upid).value;
		var title_length = strlen(title);
		if (title_length < 3) {
			showDialog('error','评论内容字数不能少于3字节');
			return false;
		}
		if (title_length >= 200) {
			showDialog('error','评论内容字数不能多于200字节');
			return false;
		}
	} else {
		showDialog('error','没有评论条件');
		return false;
	}
	container = objCheck(container);
	getObj('comm_input_'+type+'_'+typeid+'_'+upid).value = '';
	ajax.send('apps.php?q=ajax&a=commreply','type='+type+'&id='+typeid+'&upid='+upid+'&title='+ajax.convert(title),function(){
		var rText = ajax.request.responseText.split('\t');
		if (rText[0] == 'success') {
			var date = new Date();
			var thispost = dateFormat(date,'yyyy-mm-dd hh:ii');
			var thisobj = {
				'id'		: rText[1],
				'uid'		: winduid,
				'username'	: char_cv(windid),
				'type'		: type,
				'typeid'	: typeid,
				'title'		: char_cv(title),
				'postdate'	: thispost,
				'upid'		: upid,
				'face'		: rText[2]
			};
			creatComment(thisobj,container,'1');
		} else {
			ajax.guide();
		}
	});
}

function delComment(id,numid) {
	ajax.send('apps.php?q=ajax&a=commdel','id='+id,function(){
		var rText = ajax.request.responseText.split('\t');
		if (rText[0] == 'success') {
			if (numid == 0) {
				getObj('commnet_'+ id).style.display = 'none';
			} else if (numid == 1) {
				getObj('subcommnet_'+ id).style.display = 'none';
			} else if (numid == 2) {
				getObj('com_board_'+ id).style.display = 'none';
			}
		} else {
			ajax.guide();
		}
	});
}

function createCommnetInput(type,typeid,upid){
		if (IsElement('comment_input_'+type+'_'+typeid+'_'+upid)) {
			delElement('comment_input_'+type+'_'+typeid+'_'+upid);
			return false;
		} else {
			var input_p = elementBind('div','comment_input_'+type+'_'+typeid+'_'+upid,'cc','margin-top:10px;');
			var span = elementBind('span',false,'');
			input_p.appendChild(span);
			var comm_input	= elementBind('input','comm_input_'+type+'_'+typeid+'_'+upid,'input','width: 200px;');
			comm_input.setAttribute('type','text');
			comm_input.setAttribute('name','title');
			comm_input.setAttribute('maxlength','200');
			input_p.appendChild(comm_input);
			
			var comm_button = elementBind('input',false,'bt','margin-left: 5px;cursor: pointer;');
			comm_button.setAttribute('type','button');
			comm_button.setAttribute('value','回复');
			comm_button.onclick = function () {
				postComment(type,typeid,upid);
			}
			input_p.appendChild(comm_button);
			getObj('commentbox_'+typeid+'_'+upid).appendChild(input_p);
			comm_input.focus();
			if(IsElement('upPanel')){
				getObj('upPanel').scrollTop+= 50;
			}else{
				document.documentElement.scrollTop += 50;
			}
		}
}

function postComment(type,typeid,upid,form) {
	if (getObj('comm_input_'+type+'_'+typeid+'_'+upid)) {
		var title = getObj('comm_input_'+type+'_'+typeid+'_'+upid).value;
		var title_length = strlen(title);
		if (title_length < 3) {
			showDialog('error','评论内容字数不能少于3字节');
			return false;
		}
		if (title_length >= 200) {
			showDialog('error','评论内容字数不能多于200字节');
			return false;
		}
	} else {
		showDialog('error','没有评论条件');
		return false;
	}
	ajax.send('apps.php?q=ajax&a=commreply','type='+type+'&id='+typeid+'&upid='+upid+'&title='+ajax.convert(title),function(){
		
		var rText = ajax.request.responseText.split('\t');
		if (rText[0] == 'success') {
			
			var commentid = rText[1];
			var face = rText[2];
			var title = rText[3];
			if (upid == 0 && type != 'board') {
				var dl	= elementBind('dl','commnet_'+commentid);
			} else if (upid > 0 && type != 'board') {
				var dl	= elementBind('dl','subcommnet_'+commentid);
			} else if (type == 'board') {
				var dl	= elementBind('dl','com_board_'+commentid);
			}
			//dl.setAttribute('class','cc');
			dl.className='cc';
			var dt	= elementBind('dt');
			var img	= elementBind('img','','img-50');
			img.src	= face;
			dt.appendChild(img);
			if (upid == 0 && type != 'board') {
				var del_a = elementBind('a','','del fr mr10','cursor: pointer;');
				del_a.setAttribute('onclick',function(){pwConfirm('是否确定删除本条评论',this,function(){delComment(commentid,'0')})});
				del_a.innerHTML = '删除';
			}
			var dd2 = elementBind('dd','','dd60');
			var username_a = elementBind('a');
			username_a.href = 'u.php';
			username_a.innerHTML = windid;
			var postdate_span = elementBind('span','','gray');
			var date = new Date();
			var thispost = dateFormat(date,'yyyy-mm-dd hh:ii');
			postdate_span.innerHTML = thispost;
			var title_p = elementBind('p');
			title_p.setAttribute('class','f14');
			title_p.innerHTML = title;
			if (upid>0 || type == 'board') {
				var del_a = elementBind('a','','del','cursor:pointer;');
				if (type == 'board') {
					del_a.setAttribute('onclick',function(){pwConfirm('是否确定删除本条评论',this,function(){delComment(commentid,'2')})});
				}
				if (upid>0) {
					del_a.setAttribute('onclick',function(){pwConfirm('是否确定删除本条评论',this,function(){delComment(commentid,'1')})});
				}
				del_a.innerHTML = '删除';
			}
			if (upid == 0 && type != 'board') {
				dd2.appendChild(del_a);
			}
			dd2.appendChild(username_a);
			dd2.appendChild(postdate_span);
			if (upid>0 || type == 'board') {
				dd2.appendChild(del_a);
			}
			dd2.appendChild(title_p);
			dl.appendChild(dt);
			
			dl.appendChild(dd2);
			if (upid == 0 && type != 'board') {
				var createcommentbox = getObj('createcommentbox');
				createcommentbox.insertBefore(dl,createcommentbox.firstChild);
				getObj('comm_input_'+type+'_'+typeid+'_'+upid).value = '';
			} else {
				var subcommentbox = getObj('commentbox_'+typeid+'_'+upid);
				subcommentbox.appendChild(dl);
			}
			if (upid > 0 || type == 'board') {
				delElement('comment_input_'+type+'_'+typeid+'_'+upid);
			}
			
		} else {
			ajax.guide();
		}
		
	});
}





