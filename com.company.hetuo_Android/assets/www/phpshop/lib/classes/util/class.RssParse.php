<?PHP
        /*****************************
        *        RSS文档解析类-(lovered.GV)
        ******************************/
        class RssParse
        {
                var $url;                        //rss文件的地址
                var $data;                        //rss文件的内容
                var $version;                //rss文件的版本号
                var $channel;                //rss文件中的频道信息
                var $items;
       
                //与XML解析有关的属性####################
                var $xml_parser;        //xml解析器句柄
                var $depth;                        //XML当前解析深度
                var $tag;                        //当前正在解析的XML元素
                var $prev_tag;                //当前正在解析的上一个元素
                var $marker;                //用来标记制定的深度
                var $event;                        //实践名称:CHANNEL and ITEM
                var $item_index;        //item元素索引

                //初始化#################################
               public function RssParse($rss_url)
                {
                        $h=fopen($rss_url,"r");
                        if($h)
                        {
                                $this->url=$rss_url;

                                while(!feof($h))
                                        $this->data.=fgets($h,4096);
                                fclose($h);

                                //初始化xml解析器
                                $this->xml_parser = xml_parser_create("UTF-8");
                                xml_set_object($this->xml_parser, &$this);
                                xml_parser_set_option($this->xml_parser, XML_OPTION_CASE_FOLDING, 1);
                                xml_set_element_handler($this->xml_parser, "startElement", "endElement");
                                xml_set_character_data_handler($this->xml_parser, "characterData");
                                //开始解析数据
                                if (!xml_parse($this->xml_parser, $this->data))
                                        trigger_error("XML error: ".xml_error_string(xml_get_error_code($this->xml_parser))." at line ".xml_get_current_line_number($this->xml_parser),E_USER_ERROR);
                        }
                        else
                                trigger_error("无法初始化RSS对象,{$rss_url}无法访问或者不存在",E_USER_ERROR);
                }

                //返回所有ITEM信息#######################
                function GetItems()
                {
                        return $this->items;
                }

                //返回频道信息###########################
                function GetChannel()
                {
                        return $this->channel;
                }

                //返回RSS文件的版本信息##################
                function GetVersion()
                {
                        return $this->version;
                }

                //开始解析XML元素########################
                function startElement($parser, $name, $attribs)
                {
                        $this->depth++;
                        $this->tag=$name;

                        switch($name)
                        {
                                case "RSS":
                                        $this->event=$name;
                                        $this->version=$attribs["VERSION"];
                                        break;
                                case "CHANNEL":
                                        $this->event=$name;
                                        $this->marker=$this->depth+1;
                                        break;
                                case "ITEM":
                                        $this->item_index++;
                                        $this->event=$name;
                                        $this->marker=$this->depth+1;
                                        break;
                                default:
                                        return NULL;
                        }
                }

                //结束某个元素解析时#####################
                function endElement($parser, $name)
                {
                        $this->depth--;
                        return;
                }

                //处理数据###############################
                function characterData($parser, $data)
                {
                        $data=iconv("utf-8","gb2312",trim($data));


                        //当数据为chanel下的数据时执行
                        if( $this->event=="CHANNEL" && $this->marker==$this->depth )
                        {
                                $this->channel[$this->tag]=$data;
                        }

                        //当数据为item下的数据时执行
                        if( $this->event=="ITEM" && $this->marker==$this->depth )
                        {
                                if($this->prev_tag==$this->tag)
                                        $this->items[$this->item_index][$this->tag].=$data;
                                else
                                        $this->items[$this->item_index][$this->tag]=$data;
                        }

                        $this->prev_tag=$this->tag;
                }

                //是否是一个有效的RSS地址################
                public static function IsRss($rss_url)
                {
                        $rss_url=trim($rss_url);

                        if($rss_url=="")
                                return false;

                        if($h=@fopen($rss_url,"r"))
                        {
                                $text=@fread($h,512);
                                @fclose($h);

                                if(eregi("<RSS",$text))
                                        return true;
                                else
                                        return false;
                        }
                        else
                                return false;
                }
        }
?>