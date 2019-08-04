package com.mogoo.market.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.mogoo.market.utils.DateUtils;
import com.mogoo.parser.UnderlineResultCallback;
import com.mogoo.parser.XmlResultCallback;

/**
 * @see 评论信息
 */
public class Comments 
{
    /** APKID*/
    private String apkId;
    
    /** 评论 ID*/
    private String cm_id;     
    /** 标题 */
    private String title;
    /** 被评论的软件版本 */
    private String version;
    /** 用户 ID */
    private String name;
    /** 评分 */
    private String rating;
    /** 评论日期 YYYY-MM-DD */
    private String date;
    /** 评论信息 */
    private String content;
    
 
    static class CommentsTag 
    {
        /** 节点 */
        public static final String NODE = "node";
        /** 评论 ID*/
        private static final String ID = "id";      
        /** 标题 */
        private static final String TITLE = "t"; 
        /** 被评论的软件版本 */
        private static final String VERSION = "v"; 
        /** 用户 ID */
        private static final String USER_ID = "n"; 
        /** 评分 */
        private static final String RATING = "r"; 
        /** 评论日期 */
        private static final String DATE = "d"; 
        /** 评论信息 */
        private static final String SUMMARY = "s"; 
    }
    
    /**
     * 添加评论解析类
     * @author luo
     */
    public static class SubmitCommentCallBack extends UnderlineResultCallback 
    {
        
    }
    
	/**
     * 评论列表解析类
     * @author luo
     */
    public static class CommentsListCallback extends XmlResultCallback 
    {
        private Comments comments ;
        public ArrayList<Comments> commentsList = new ArrayList<Comments>();
        private StringBuilder mStringBuilder = new StringBuilder();
        
        @Override
        public Object getResult() 
        {
            return commentsList;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) 
        {
        	mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(CommentsTag.NODE)) 
            { 
                for(int i=0; i<attributes.getLength(); i++)
                {
                    String iQName = attributes.getQName(i);
                    if(iQName.equalsIgnoreCase(CommentsTag.ID))
                    {
                        String iQNameValue = attributes.getValue(iQName);
                        comments = new Comments();
                        comments.cm_id = iQNameValue;
                    }
                }
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) 
        {
            String data = mStringBuilder.toString();
            
            if(localName.equals(CommentsTag.TITLE)) {
                comments.title = data;
            }else if(localName.equals(CommentsTag.VERSION)) {
                comments.version = data;
            }else if(localName.equals(CommentsTag.USER_ID)) {
                comments.name = data;
            }else if(localName.equals(CommentsTag.RATING)) {
                comments.rating = data;
            }else if(localName.equals(CommentsTag.DATE)) {
                comments.date = DateUtils.getDateStrYYYY_MM_DD(data);
            }else if(localName.equals(CommentsTag.SUMMARY)) {
                comments.content = data;
            }
            if(localName.equalsIgnoreCase(CommentsTag.NODE))
            {
                commentsList.add(comments);
                comments = null;
            } 
            
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) 
        { 
            super.characters(ch, start, length);
            mStringBuilder.append(ch, start, length);
        }     
    }


    
    public String getApkId() {
        return apkId;
    }


    public void setApkId(String apkId) {
        this.apkId = apkId;
    }


    public String getId() {
        return cm_id;
    }


    public void setId(String id) {
        this.cm_id = id;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getVersion() {
        return version;
    }


    public void setVersion(String version) {
        this.version = version;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getRating() {
        return rating;
    }


    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getComment() {
        return content;
    }


    public void setComment(String comment) {
        this.content = comment;
    }

}
