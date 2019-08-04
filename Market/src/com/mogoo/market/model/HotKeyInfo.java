package com.mogoo.market.model;


import org.xml.sax.Attributes;

import com.mogoo.parser.XmlResultCallback;

import java.util.ArrayList;

public class HotKeyInfo {
    
    //热关键字 名词
    private String name;
    
    
    static class HotKeyInfoTag {
        /** 节点 */
        public static final String NODE = "node";
        
    }
    
    

    /**
     * 解析类
     * @author luo
     */
    public static class HotKeyInfoListCallback extends XmlResultCallback {
        private HotKeyInfo hotKeyInfo ;
        public ArrayList<HotKeyInfo> hotKeyInfoList = new ArrayList<HotKeyInfo>();
        private StringBuilder mStringBuilder = new StringBuilder();
        
        @Override
        public Object getResult() {
            
            return hotKeyInfoList;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) {
        	mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(HotKeyInfoTag.NODE)) {             
                hotKeyInfo = new HotKeyInfo();
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String data = mStringBuilder.toString();
            
            if(localName.equals(HotKeyInfoTag.NODE)) {
                hotKeyInfo.name = data;
            }
        	if(localName.equalsIgnoreCase(HotKeyInfoTag.NODE)){
                hotKeyInfoList.add(hotKeyInfo);
                hotKeyInfo = null;
            } 
            
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) { 
            super.characters(ch, start, length);
            mStringBuilder.append(ch, start, length);
        }     
    }



	public String getName() {
		return name;
	}
    

}
