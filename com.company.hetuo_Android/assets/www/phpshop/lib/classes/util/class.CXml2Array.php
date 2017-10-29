<?php
/*
 * CXml2Array.php
 * $Header: d:/cvs/classistd/x2a/CXml2Array.php,v 1.10 2004/02/26 06:32:17 Administrator Exp $
 *
 * Class to convert an XML file into array
 *
 * Copyright (C) 2003,2004  Andrioli Darvin <darvin@andrioli.com>
 *
 *   @author     cooc <yemasky@msn.com>
	版权所有，国家软件登记号：2009SR06466 ……
	任何媒体、网站或个人未经本人协议授权不得修改本程序￥
 */

class CXml2Array
{
/**
 * Array containg the file XML
 * structure:
 *  ['text']                 -> text inside the tag
 *  ['attrib'][attribname]   -> tag's attribute
 *  ['child'][]              -> child tag information. It holds many child with the
 *                              same name
 *
 * @var array
 */
var $TheArray;

/**
 * Temporary arrays
 */
var $tmpAttrib;
var $tmpTag;

var $AttribFirstTime;
var $TagFirstTime;
var $IntTmpTag;
/**
 * Class initializer
 * @param mixed object DOM XML or array returned by GetTag
 * @access public
 * @see GetTag()
 */
function CXml2Array($node)
{
if(is_array($node))
   $this->LoadFromArray($node);
else
   {
   if(is_object($node))
     $this->TheArray=$this->Parse($node);
   else
     trigger_error('Invalid parameter',E_USER_ERROR);
   }
$this->tmpAttrib=$this->TheArray['attrib'];
$this->tmpTag=$this->TheArray['child'];
$this->AttribFirstTime=true;
$this->TagFirstTime=true;
}

/**
 * Parse one XML node.
 * @param object DOMXML
 * @param integer Nesting level
 * @access private
 */
function Parse($Node,$level=0,$NodeName='')
{
$type=array( 1 =>'XML_ELEMENT_NODE',
             2 =>'XML_ATTRIBUTE_NODE',
             3 =>'XML_TEXT_NODE',
             4 =>'XML_CDATA_SECTION_NODE',
             5 =>'XML_ENTITY_REF_NODE',
             6 =>'XML_ENTITY_NODE',
             17 =>'XML_ENTITY_DECL_NODE'
           );
// print_r($Node);
$Text='';
//$NodeName='';
$Contents=array();
$Attrib=array();
// load the attribs
$AttrTbl=$Node->attributes();
if($AttrTbl) {
   // First I'm looking for the attrib Id
   foreach($AttrTbl as $Att)
     {
     $AttribName=strtolower($Att->name());
     $Attrib[$AttribName]=$Att->value();
     }
  }
// childs parse
$child = $Node->first_child();
while($child) {
//echo '<br>child:';
// echo '<br>['.$level.']'.$type[$child->node_type()];
switch($child->node_type())
     {
     case XML_ENTITY_REF_NODE :
          $ret=$this->Parse($child,$level+1);
          // var_dump($ret);
          foreach($ret['child'] as $key => $value)
            {
            //if(array_key_exists($key,$Contents))
            //  {
            //  $t=array_merge($Contents[$key],$value);
            //  $Contents[$key]=$t;
            //  }
            // else
            //  {
              $Contents[]=$value;
            //  }
            }
          //return($ret);
          break;
     case XML_ENTITY_DECL_NODE :
          $ret=$this->Parse($child,$level+1);
          return($ret);
          break;
     case XML_TEXT_NODE:
          $Text.=rtrim($child->node_value());
          break;
     case XML_CDATA_SECTION_NODE:
          $Text.=rtrim($child->node_value());
          //$Text.=ereg_replace(' ','&nbsp;',$passo1);
          //$Text.=htmlentities(trim($child->node_value()));
          break;
     case XML_ELEMENT_NODE:
          $NName=strtolower($child->node_name());
          $Contents[]=$this->Parse($child,$level+1,$NName);
          break;
     }  // switch node type
  $child = $child->next_sibling();
  }
return(array('child' => $Contents,
             'text' => $Text,
             'attrib' => $Attrib,
             'nodename' => $NodeName)
            );
}

/**
 * Load the array from another array retrieved from this class.
 * It is usefull to apply the module's function to the deepest part of the
 * original array
 * @param array
 */
function LoadFromArray($data)
{
$this->TheArray=$data;
// if(!array_key_exists('attrib',$this->TheArray))
//   $this->TheArray['attrib']=array();
if(!array_key_exists('text',$this->TheArray))
   $this->TheArray['text']='';
}

/**
 * Dump the array contents in human format.
 * Usefull for debug purpose
 * @access public
 */
function ArrayDump()
{
// var_dump($this->TheArray);
//echo '<hr>';
$this->_dump($this->TheArray);
}

/**
 * Internal function used to dump the array
 * @param array data to dump
 * @param integer
 * @access private
 */
function _dump($data,$level=0)
{
$ta=20;
$c='.';
echo str_repeat($c,$level*$ta).'[text]:'.$data['text'].'<br>';
echo str_repeat($c,$level*$ta).'[nodename]:'.$data['nodename'].'<br>';
echo str_repeat($c,$level*$ta).'[attrib]:'.'<br>';
foreach($data['attrib'] as $key => $value)
   {
   echo str_repeat($c,$level*$ta).'.........['.$key.']:'.$value.'<br>';
   }
echo str_repeat($c,$level*$ta).'[child]:'.'<br>';
foreach($data['child'] as $key => $value)
   {
   //var_dump($data['tag']);
   echo str_repeat($c,$level*$ta).'.........['.$key.']:'.'<br>';
   // foreach($data['child'][$key] as $key1 => $value1)
   //    {
       //var_dump($value1);
   //    echo str_repeat($c,($level+1)*$ta-4).'['.$key1.']';
       $this->_dump($value,$level+1);
   //    }
   }
}


/************************************
 *  Retrieve functions
 ************************************/
/**
 * Does exist the request tag? The function looks only if the tag exists
 * as child of the root element.
 * @param string
 * @return bool
 * @access public
 */
function ExistTag($TagName)
{
$noItem=count($this->TheArray['child']);
$Found=FALSE;
for($i=0;$i<$noItem&&!$Found;$i++)
   {
   if($this->TheArray['child'][$i]['nodename']==$TagName)
     $Found=TRUE;
   }
return($Found);
}

/**
 * Does exist the request attribute? The function looks only if the attribute exists
 * as child of the root element.
 * @param string
 * @return bool
 * @access public
 */
function ExistAttribute($AttribName)
{
return(array_key_exists($AttribName,$this->TheArray['attrib']));
}

/**
 * Return the XML as array
 * @access public
 */
function GetArray()
{
return($this->TheArray);
}

/**
 * The function return all childs of the root element named $TagName
 * @param string
 * @return array
 * @access public
 */
function GetTag($TagName)
{
$noItem=count($this->TheArray['child']);
$ret=array();
$Found=FALSE;
for($i=0;$i<$noItem;$i++)
   {
   if($this->TheArray['child'][$i]['nodename']==$TagName)
     {
     $ret[]=$this->TheArray['child'][$i];
     $Found=TRUE;
     }
   }

if($Found)
   return($ret);
else
   return(FALSE);
}

/**
 * The function return all childs of the root element named $TagName
 * @param string
 * @return array
 * @access public
 */
function GetTagPos($TagName,$Position=0)
{
$count=0;
$noItem=count($this->TheArray['child']);
for($i=0;$i<$noItem;$i++)
   {
   if($this->TheArray['child'][$i]['nodename']==$TagName)
     {
     if($count==$Position)
        return($this->TheArray['child'][$i]);
     $count++;
     }
   }
return(FALSE);
}

/**
 * The function return all childs of $TagName. Note $TagName should be a child of the
 * root element
 * @param string
 * @param integer Select from which child retrieve the information, if more child of
 *                root element have the same name
 * @return array
 * @access public
 */
function GetTagChilds($TagName,$Position=0)
{
$noItem=count($this->TheArray['child']);
$count=0;
for($i=0;$i<$noItem;$i++)
   {
   if($this->TheArray['child'][$i]['nodename']==$TagName)
     {
     if($count==$Position)
        return($this->TheArray['child'][$i]['child']);
     $count++;
     }
   }
return(FALSE);
}

/**
 * The function return all attributes of $TagName. Note $TagName should be a child of the
 * root element
 * @param string
 * @param integer Select from which child retrieve the information, if more child of
 *                root element have the same name
 * @return array
 * @access public
 */
function GetTagAttributes($TagName,$Position=0)
{
$noItem=count($this->TheArray['child']);
$count=0;
for($i=0;$i<$noItem;$i++)
   {
   if($this->TheArray['child'][$i]['nodename']==$TagName)
     {
     if($count==$Position)
        return($this->TheArray['child'][$i]['attrib']);
     $count++;
     }
   }
return(FALSE);
}

/**
 * Return value of the named attribute
 * @param string
 * @return string
 * @access public
 */
function GetAttribute($AttribName)
{
if($this->ExistAttribute($AttribName))
   return($this->TheArray['attrib'][$AttribName]);
else
   return(FALSE);
}

/**
 * Return value of the root tag
 * @return string
 * @access public
 */
function GetText()
{
return($this->TheArray['text']);
}

/**
 * Return the name of the root tag
 * @return string
 * @access public
 */
function GetNodeName()
{
return($this->TheArray['nodename']);
}


/**
 * The function iterate over attribute's array of the root element
 * @param bool Set to TRUE to reset the internal pointer
 * @return array
 * @access public
 */
function EachAttribute($reset=FALSE)
{
// var_dump($this->TheArray);
// var_dump($this->tmpAttrib);
if(!is_array($this->tmpAttrib))
  return(FALSE);
if($reset)
  {
  $this->AttribFirstTime=TRUE;
  return(TRUE);
  }

if($this->AttribFirstTime)
   {
   reset($this->tmpAttrib);
   $this->AttribFirstTime=false;
   }
else
   if(!next($this->tmpAttrib))
      return(FALSE);
$value=current($this->tmpAttrib);
$key=key($this->tmpAttrib);

return(array($key,$value));
}

/**
 * The function iterate over tag's array of the root element
 * Public method
 * @param bool Set to TRUE to reset the internal pointer
 * @return object CXml2Array
 * @access public
 * @see _ChildLoop()
 */
function EachChild($reset=FALSE)
{
return($this->_ChildLoop($reset,TRUE));
}

/**
 * The function iterate over tag's array of the root element
 * Public method
 * @param bool Set to TRUE to reset the internal pointer
 * @return array
 * @access public
 * @see _ChildLoop()
 */
function EachChildArray($reset=FALSE)
{
return($this->_ChildLoop($reset,FALSE));
}

/**
 * The function iterate over tag's array of the root element
 * @param bool Set to TRUE to reset the internal pointer (return TRUE)
 * @param bool Set to TRUE to return an object CXml2Array, o FALSE to return tha
 *             child's value as array
 * @return object CXml2Array
 * @access public
 * @see _EachChild()
 * @see _EachChildArray()
 */
function _ChildLoop($reset,$ReturnObj)
{
if(!is_array($this->tmpTag))
  return(FALSE);
if(count($this->tmpTag)==0)
  return(FALSE);
if($reset)
  {
  $this->TagFirstTime=TRUE;
  return(TRUE);
  }
if($this->TagFirstTime)
   {
   reset($this->tmpTag);
   // $this->IntTmpTag=current($this->tmpTag);
   if(!is_array($this->tmpTag))
      return(FALSE);
   // reset($this->IntTmpTag);
   $this->TagFirstTime=false;
   }
else
   // if(!next($this->IntTmpTag))
   //  {
      if(!next($this->tmpTag))
         return(FALSE);
   //   else
   //     {
   //     $this->IntTmpTag=current($this->tmpTag);
   //     reset($this->IntTmpTag);
   //     }
   // }
    
$value=current($this->tmpTag);
$key=$value['nodename'];
if($ReturnObj)
  {
  if(!is_array($value))
    {
    print_r($this->tmpTag);
    die();
    }
  $tmpObj=new CXml2Array($value);
  return(array($key,$tmpObj));
 }
else
  return(array($key,$value));
}

}
?>

