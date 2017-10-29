function getFormAsString(form)
{   
    returnString = "";   
    formElements = form.elements;   
    var first = true;   
    for (var i=0;i<formElements.length;i++) {   
      var e = formElements[i];   
      if(e.name == null || e.name==""){   
        continue;   
      }   
      if(e.type == "radio" || e.type == "checkbox"){   
        if(e.checked){//判断单选按钮是否被选中   
          if(first == true){   
          first = false;   
           returnString += e.name + "=" + e.value;   
        }else{   
                 returnString += "&" + e.name + "=" + e.value;
             }   
           }   
        }else{   
          if(first == true){   
        first = false;   
        returnString += e.name + "=" + e.value;   
      }else{   
            returnString += "&" + e.name + "=" + e.value;   
          }   
        }   
    }   
    return returnString; 
} 
function autoAjax()
{
	var ajaxes=document.getElementsByName("ajax");
	for(var i=0;i<ajaxes.length;i++)
	{
		if(ajaxes[i].tagName=="A")
		{	
			var target=ajaxes[i].getAttributeNode('target');
			var href=ajaxes[i].getAttributeNode('href');
			if(target && href)
			{
				if(undefined==ajaxes[i].onclick)
				{
					ajaxes[i].onclick=function()
					{
						var target=this.getAttributeNode('target');
						var href=this.getAttributeNode('href');
						var ajax=new Ajax();
						var mode=eval(target.value);
						var content=href.value.substring(href.value.indexOf("?")+1);
						ajax.getInfo(href.value==""?window.location:href.value,"post","app",content,function(content)
						{
							if(mode instanceof Function)mode(content);
							else  if(document.getElementById(mode))document.getElementById(mode).innerHTML=content;
							autoAjax();
						});
						return false;
					}
				}
			}
		}
		else if(ajaxes[i].tagName=="FORM")
		{

			var target=ajaxes[i].getAttributeNode('target');
			var action=ajaxes[i].getAttributeNode('action');
			if(target && action)
			{	
				if(undefined==ajaxes[i].onclick)
				{
					ajaxes[i].onsubmit=function()
					{
						target=this.getAttributeNode('target');
						action=this.getAttributeNode('action');
						var ajax=new Ajax();
						var mode=eval(target.value);
						ajax.getInfo(action.value==""?window.location:action.value,"post","app",getFormAsString(this),function(content)
						{
							if(mode instanceof Function)mode(content);
							else if(document.getElementById(mode))document.getElementById(mode).innerHTML=content;
							autoAjax();
						});
						return false;
					}
				}
			}
		}
	}
}
autoAjax();