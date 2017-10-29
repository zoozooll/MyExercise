<?php
if(!$IWEB_SHOP_IN) {
	die('Hacking attempt');
}

//XML操作类
class XMLOperator
{
	private $dom;
	public function __construct($xmlfile="",$encoding="UTF-8")
	{
		$this->dom=new DOMDocument(); 
		$this->dom->encoding=$encoding;
		$this->preserveWhiteSpace=false;
		$this->dom->formatOutput=true;
		if(file_exists($xmlfile))$this->dom->load($xmlfile);
	}
	//查询节点
	public function query($query)
	{
		$xpath=new DOMXpath($this->dom);
		$element = $xpath->query($query);
		if($element->length>0)
		{
			return $element;
		}
		else
		{
			return false;
		}
	}
	//添加节点
	public function addNode($query,$name,$value="",$attrs="")
	{
		$target_node=$this->query($query);
		if($target_node)
		{
			$node=$this->dom->createElement($name,$value);
			if($attrs!="")
			{
				$tems=explode(";",$attrs);
				if(count($tems)>0)
				{
					for($i=0;$i<count($tems);$i++)
					{
						$tem=explode("=",$tems[$i]);
						$node->setAttribute($tem[0],$tem[1]);
					}
				}
			}
			$target_node->item(0)->appendChild($node);
			return true;
		}
		else
		{
			return false;
		}
	}
	//添加节点属性
	public function addAttr($query,$name,$value)
	{
		$node=$this->query($query);
		if($node)
		{
			$node->item(0)->setAttribute($name,$value);
			return true;
		}
		else
		{
			return false;
		}
	}
	//删除节点	
	function delNode($query)
	{
		$node=$this->query($query);
		if($node)
		{
			$node->item(0)->parentNode->removeChild($node->item(0));
			return false;
		}
		else
		{
			return false;
		}
	}
	//删除属性
	function delAttr($query,$name)
	{
		$node=$this->query($query);
		if($node)
		{
			$node->item(0)->removeAttribute($name);
			return true;
		}
		else
		{
			return false;
		}
	}
	//修改属性
	public function updAttr($query,$name,$value)
	{
		$node=$this->query($query);
		if($node)
		{
			$node->item(0)->setAttribute($name,$value);
			return true;
		}
		else
		{
			return false;
		}
	}
	//修改节点
	public function updNode($query,$value)
	{
		$node=$this->query($query);
		if($node)
		{
			$node->item(0)->nodeValue=$value;
			return true;
		}
		else
		{
			return false;
		}
	} 
	//保存文件
	public function save($file)
	{
		$this->dom->save($file);
	}
}
?>