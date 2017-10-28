
$(document).ready(function(){
	var autoNode=$("#searchAuto");
	autoNode.hide().css("border","1px black solid");
	
	$("#cond").keyup(function(){
		var wordText=$("#cond").val();
		$.post("SearchServlet",{cond:wordText},function(data){
			var jqueryObj=$(data);
			var wordNodes=jqueryObj.find("word");			
			wordNodes.each(function(){
				var wordNodes=$(this);
				$("<div>").html(wordNodes.text()).appendTo(autoNode);
				alert(wordNodes.text());
			});
			if(wordNodes.length>0){
				autoNode.show();
			}
		
		},"xml");
	});
	$("input[type='image']").click(function(){
		alert("文本框中的【"+$("#cond").val()+"】被提交了");
	});
	
});