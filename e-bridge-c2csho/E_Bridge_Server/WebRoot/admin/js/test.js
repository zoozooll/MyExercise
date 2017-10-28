//表示当前高亮的节点
var highlightindex=-1;

$(document).ready(function(){
	var wordInput=$("#word");
	//获得select元素的值
	var selectOption=$("#selectName");
	var EntityNameText=$("select option:selected").val();	
	selectOption.click(function(){
		EntityNameText=$(this).val();		
	});
	var wordInputOffset=wordInput.offset();
	//自动补全框最开始应该隐藏起来
	var autoNode=$("#auto");
	autoNode.hide().css("border","1px black solid")
	.css("background-color","white")
	.css("z-index","100")
	.css("position","absolute")
	.css("top",wordInputOffset.top+wordInput.height()+6+"px")
	.css("left",wordInputOffset.left-178+"px").width(wordInput.width()+130+"px");
	// 给文本框添加键盘按下并弹起的事件
	var audiNo=$("#audiNo");
	var audiYes=$("#audiYes");
	var venderId=$("#venderId");
	
	audiNo.click(function(){
		$.post("SearchServlet",{word:wordText,entityName:EntityNameText},function(data){
			//将dom对象data转换成JQuery对象
			var jqueryObj=$(data);
			//找到所有的word节点
			var wordNodes=jqueryObj.find("word");
			//遍历所有的word节点,取出单词内容,然后将单词内容添加到弹出框中
			
			//需要清空原来的内容
			autoNode.html("");
			
			wordNodes.each(function(i){
				//获取单词内容
				var wordNode=$(this);				
				//新建div节点,将单词内容加入到弹出框的节点中
				//将新建的节点加入到弹出框的节点中
				var newDivNode=$("<div>").attr("id",i);
				newDivNode.html(wordNode.text()).appendTo(autoNode);				
				//增加鼠标进入事件,高亮节点
				newDivNode.mouseover(function(){
					var autoDiv=$("#auto").children("div");
					//将原来高亮节点取消高亮
					if(highlightindex!=-1){
						autoDiv.eq(highlightindex)
						.css("background-color","white")
						.css("color","black");
					}
					//记录新高亮索引
					highlightindex=$(this).attr("id");
					//鼠标进入节点高亮
					$(this).css("background-color","red").css("color","yellow");
				});
				//增加鼠标进入事件,取消当前节点的高亮
				newDivNode.mouseout(function(){
					//取消鼠标移出节点的高亮
					$(this).css("background-color","red").css("color","yellow");
					 
				});
				//增加鼠标点击事件,可以进行补全
				newDivNode.click(function(){					
					//取出高亮节点的文本内容
					var comText=$(this).text();
					$("#auto").hide()
					highlightindex=-1;
					//文本框中的内容变成高亮节点的内容
					$("#word").val(comText);
					
				});
				
			});
			if(wordNodes.length>0){
				autoNode.show();
			}else{
				autoNode.hide();
				//弹出框隐藏同时,高亮节点索引值也制成-1
				highlightindex=-1;
			}
		
		},"xml");
		
	});
	audiYes.click(function(){});
	wordInput.keyup(function(event){
		//处理文本框中的键盘事件
		var myEvent=event||window.event;
		var keyCode=myEvent.keyCode;
		//如果输入的是退格键或删除键,也应该将文本框内容中的最新信息发送给服务器
		
		//如果输入的是字母,应该将文本框中最新的信息发送给服务器
		if(keyCode>=65&&keyCode<=90||keyCode==8||keyCode==46){
		
		//1.先获取文本框中的内容
		var wordText=$("#word").val();
		if(wordText!==""){
		//1.  将文本框中的内容发送给服务器
		
		}else{
			autoNode.hide();
			//弹出框隐藏同时,高亮节点索引值也制成-1
			highlightindex=-1;
		}
		
		
		}else if(keyCode==38||keyCode==40){
			// 如果输入的是向上38向下40的按键
			if(keyCode==38){
				//向上
				var autoNodes=$("#auto").children("div");
				if(highlightindex!=-1){
					//如果存在高亮节点,刚将背景色改成白色
					autoNodes.eq(highlightindex).css("background-color","white").css("color","black");
					highlightindex =highlightindex-1;
				}else{
					highlightindex=autoNodes.length-1;
				}
				
				if(highlightindex==autoNodes.length){
					//如果修改索引值以后index变成-1,则将索引值指向最后一个元素
					highlightindex=0;
				}
				//让现在高亮的内容变成红色
				autoNodes.eq(highlightindex).css("background-color","red").css("color","yellow");
				
			}
			if(keyCode==40){
				//向下
				var autoNodes=$("#auto").children("div");
				if(highlightindex!=-1){
					//如果存在高亮节点,刚将背景色改成白色
					autoNodes.eq(highlightindex).css("background-color","white").css("color","black");					
				}
				highlightindex=highlightindex+1;
				if(highlightindex==autoNodes.length){
					//如果修改索引值以后index变成-1,则将索引值指向最后一个元素
					highlightindex=0;
				}
				//让现在高亮的内容变成红色
				autoNodes.eq(highlightindex).css("background-color","red").css("color","yellow");
			}
		}else if(keyCode==13){
			var divText=$("#auto").hide().children("div").eq(highlightindex);
			//如果输入回车
			
			//下拉框有高亮内容
			if(highlightindex!=-1){
				//取出高亮节点的文本内容
				var comText=divText.text();
				highlightindex=-1;
				//文本框中的内容变成高亮节点的内容
				$("#word").val(comText);
				
			}else{
			//下拉框没有高亮内容
				//alert("文本框中["+$('#word').val()+"]被提交了");	
				$("#auto").hide();
				//让文本框失去焦点
				$("#word").get(0).blur();
			}
		}
	});
	$("input[type='image']").blur();
	$("input[type='button']").click(function(){
		//alert("文本框中["+$('#word').val()+"]被提交了");
	});
	
});