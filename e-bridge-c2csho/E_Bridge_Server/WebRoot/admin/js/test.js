//��ʾ��ǰ�����Ľڵ�
var highlightindex=-1;

$(document).ready(function(){
	var wordInput=$("#word");
	//���selectԪ�ص�ֵ
	var selectOption=$("#selectName");
	var EntityNameText=$("select option:selected").val();	
	selectOption.click(function(){
		EntityNameText=$(this).val();		
	});
	var wordInputOffset=wordInput.offset();
	//�Զ���ȫ���ʼӦ����������
	var autoNode=$("#auto");
	autoNode.hide().css("border","1px black solid")
	.css("background-color","white")
	.css("z-index","100")
	.css("position","absolute")
	.css("top",wordInputOffset.top+wordInput.height()+6+"px")
	.css("left",wordInputOffset.left-178+"px").width(wordInput.width()+130+"px");
	// ���ı�����Ӽ��̰��²�������¼�
	var audiNo=$("#audiNo");
	var audiYes=$("#audiYes");
	var venderId=$("#venderId");
	
	audiNo.click(function(){
		$.post("SearchServlet",{word:wordText,entityName:EntityNameText},function(data){
			//��dom����dataת����JQuery����
			var jqueryObj=$(data);
			//�ҵ����е�word�ڵ�
			var wordNodes=jqueryObj.find("word");
			//�������е�word�ڵ�,ȡ����������,Ȼ�󽫵���������ӵ���������
			
			//��Ҫ���ԭ��������
			autoNode.html("");
			
			wordNodes.each(function(i){
				//��ȡ��������
				var wordNode=$(this);				
				//�½�div�ڵ�,���������ݼ��뵽������Ľڵ���
				//���½��Ľڵ���뵽������Ľڵ���
				var newDivNode=$("<div>").attr("id",i);
				newDivNode.html(wordNode.text()).appendTo(autoNode);				
				//�����������¼�,�����ڵ�
				newDivNode.mouseover(function(){
					var autoDiv=$("#auto").children("div");
					//��ԭ�������ڵ�ȡ������
					if(highlightindex!=-1){
						autoDiv.eq(highlightindex)
						.css("background-color","white")
						.css("color","black");
					}
					//��¼�¸�������
					highlightindex=$(this).attr("id");
					//������ڵ����
					$(this).css("background-color","red").css("color","yellow");
				});
				//�����������¼�,ȡ����ǰ�ڵ�ĸ���
				newDivNode.mouseout(function(){
					//ȡ������Ƴ��ڵ�ĸ���
					$(this).css("background-color","red").css("color","yellow");
					 
				});
				//����������¼�,���Խ��в�ȫ
				newDivNode.click(function(){					
					//ȡ�������ڵ���ı�����
					var comText=$(this).text();
					$("#auto").hide()
					highlightindex=-1;
					//�ı����е����ݱ�ɸ����ڵ������
					$("#word").val(comText);
					
				});
				
			});
			if(wordNodes.length>0){
				autoNode.show();
			}else{
				autoNode.hide();
				//����������ͬʱ,�����ڵ�����ֵҲ�Ƴ�-1
				highlightindex=-1;
			}
		
		},"xml");
		
	});
	audiYes.click(function(){});
	wordInput.keyup(function(event){
		//�����ı����еļ����¼�
		var myEvent=event||window.event;
		var keyCode=myEvent.keyCode;
		//�����������˸����ɾ����,ҲӦ�ý��ı��������е�������Ϣ���͸�������
		
		//������������ĸ,Ӧ�ý��ı��������µ���Ϣ���͸�������
		if(keyCode>=65&&keyCode<=90||keyCode==8||keyCode==46){
		
		//1.�Ȼ�ȡ�ı����е�����
		var wordText=$("#word").val();
		if(wordText!==""){
		//1.  ���ı����е����ݷ��͸�������
		
		}else{
			autoNode.hide();
			//����������ͬʱ,�����ڵ�����ֵҲ�Ƴ�-1
			highlightindex=-1;
		}
		
		
		}else if(keyCode==38||keyCode==40){
			// ��������������38����40�İ���
			if(keyCode==38){
				//����
				var autoNodes=$("#auto").children("div");
				if(highlightindex!=-1){
					//������ڸ����ڵ�,�ս�����ɫ�ĳɰ�ɫ
					autoNodes.eq(highlightindex).css("background-color","white").css("color","black");
					highlightindex =highlightindex-1;
				}else{
					highlightindex=autoNodes.length-1;
				}
				
				if(highlightindex==autoNodes.length){
					//����޸�����ֵ�Ժ�index���-1,������ֵָ�����һ��Ԫ��
					highlightindex=0;
				}
				//�����ڸ��������ݱ�ɺ�ɫ
				autoNodes.eq(highlightindex).css("background-color","red").css("color","yellow");
				
			}
			if(keyCode==40){
				//����
				var autoNodes=$("#auto").children("div");
				if(highlightindex!=-1){
					//������ڸ����ڵ�,�ս�����ɫ�ĳɰ�ɫ
					autoNodes.eq(highlightindex).css("background-color","white").css("color","black");					
				}
				highlightindex=highlightindex+1;
				if(highlightindex==autoNodes.length){
					//����޸�����ֵ�Ժ�index���-1,������ֵָ�����һ��Ԫ��
					highlightindex=0;
				}
				//�����ڸ��������ݱ�ɺ�ɫ
				autoNodes.eq(highlightindex).css("background-color","red").css("color","yellow");
			}
		}else if(keyCode==13){
			var divText=$("#auto").hide().children("div").eq(highlightindex);
			//�������س�
			
			//�������и�������
			if(highlightindex!=-1){
				//ȡ�������ڵ���ı�����
				var comText=divText.text();
				highlightindex=-1;
				//�ı����е����ݱ�ɸ����ڵ������
				$("#word").val(comText);
				
			}else{
			//������û�и�������
				//alert("�ı�����["+$('#word').val()+"]���ύ��");	
				$("#auto").hide();
				//���ı���ʧȥ����
				$("#word").get(0).blur();
			}
		}
	});
	$("input[type='image']").blur();
	$("input[type='button']").click(function(){
		//alert("�ı�����["+$('#word').val()+"]���ύ��");
	});
	
});