<?PHP
        /*****************************
        *        RSS�ĵ�������-(lovered.GV)
        ******************************/
        class RssParse
        {
                var $url;                        //rss�ļ��ĵ�ַ
                var $data;                        //rss�ļ�������
                var $version;                //rss�ļ��İ汾��
                var $channel;                //rss�ļ��е�Ƶ����Ϣ
                var $items;
       
                //��XML�����йص�����####################
                var $xml_parser;        //xml���������
                var $depth;                        //XML��ǰ�������
                var $tag;                        //��ǰ���ڽ�����XMLԪ��
                var $prev_tag;                //��ǰ���ڽ�������һ��Ԫ��
                var $marker;                //��������ƶ������
                var $event;                        //ʵ������:CHANNEL and ITEM
                var $item_index;        //itemԪ������

                //��ʼ��#################################
               public function RssParse($rss_url)
                {
                        $h=fopen($rss_url,"r");
                        if($h)
                        {
                                $this->url=$rss_url;

                                while(!feof($h))
                                        $this->data.=fgets($h,4096);
                                fclose($h);

                                //��ʼ��xml������
                                $this->xml_parser = xml_parser_create("UTF-8");
                                xml_set_object($this->xml_parser, &$this);
                                xml_parser_set_option($this->xml_parser, XML_OPTION_CASE_FOLDING, 1);
                                xml_set_element_handler($this->xml_parser, "startElement", "endElement");
                                xml_set_character_data_handler($this->xml_parser, "characterData");
                                //��ʼ��������
                                if (!xml_parse($this->xml_parser, $this->data))
                                        trigger_error("XML error: ".xml_error_string(xml_get_error_code($this->xml_parser))." at line ".xml_get_current_line_number($this->xml_parser),E_USER_ERROR);
                        }
                        else
                                trigger_error("�޷���ʼ��RSS����,{$rss_url}�޷����ʻ��߲�����",E_USER_ERROR);
                }

                //��������ITEM��Ϣ#######################
                function GetItems()
                {
                        return $this->items;
                }

                //����Ƶ����Ϣ###########################
                function GetChannel()
                {
                        return $this->channel;
                }

                //����RSS�ļ��İ汾��Ϣ##################
                function GetVersion()
                {
                        return $this->version;
                }

                //��ʼ����XMLԪ��########################
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

                //����ĳ��Ԫ�ؽ���ʱ#####################
                function endElement($parser, $name)
                {
                        $this->depth--;
                        return;
                }

                //��������###############################
                function characterData($parser, $data)
                {
                        $data=iconv("utf-8","gb2312",trim($data));


                        //������Ϊchanel�µ�����ʱִ��
                        if( $this->event=="CHANNEL" && $this->marker==$this->depth )
                        {
                                $this->channel[$this->tag]=$data;
                        }

                        //������Ϊitem�µ�����ʱִ��
                        if( $this->event=="ITEM" && $this->marker==$this->depth )
                        {
                                if($this->prev_tag==$this->tag)
                                        $this->items[$this->item_index][$this->tag].=$data;
                                else
                                        $this->items[$this->item_index][$this->tag]=$data;
                        }

                        $this->prev_tag=$this->tag;
                }

                //�Ƿ���һ����Ч��RSS��ַ################
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