package com.mogoo.market.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import android.database.Cursor;

import com.mogoo.market.database.GameCateSQLTable;
import com.mogoo.parser.XmlResultCallback;

public class Game {

	private String mId;
	private String mName;
	private String mDescription;
	private String mImgUrl;
	private String mCount;
	
	public Game() {
		
	}
	
	public Game(String id, String name, String coverUrl, String desc, String count) {
		this.mId = id;
		this.mName = name;
		this.mImgUrl = coverUrl;
		this.mDescription = desc;
		this.mCount = count;
	}
	
	public String getId() {
		return mId;
	}
	
	public void setId(String id) {
		mId = id;
	}
	
	public String getName() {
		return mName;
	}
	
	public void setName(String name) {
		mName = name;
	}	

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String desc) {
		mDescription = desc;
	}
	
	public String getCount() {
		return mCount;
	}

	public void setCount(String count) {
		mCount = count;
	}
	
	public String getImgUrl() {
		return mImgUrl;
	}
	
	public void setImgUrl(String url) {
		mImgUrl = url;
	}	
	
	public static Game getGame(Cursor cursor) {
		String id = cursor.getString(GameCateSQLTable.NUMBER_GAME_CATE_ID);
		String name = cursor.getString(GameCateSQLTable.NUMBER_GAME_CATE_NAME);
		String coverUrl = cursor.getString(GameCateSQLTable.NUMBER_GAME_CATE_COVER_URL);
		String desc = cursor.getString(GameCateSQLTable.NUMBER_GAME_CATE_DESC);
		String count = cursor.getString(GameCateSQLTable.NUMBER_GAME_CATE_COUNT);
		Game game = new Game(id, name, coverUrl, desc, count);
		return game;
	}
	
    //解析类
	public static class GameCallback extends XmlResultCallback {
        private Game game ;
        private ArrayList<Game> gameList = new ArrayList<Game>();
        private StringBuilder mStringBuilder = new StringBuilder();
        
        @Override
        public Object getResult() {
            return gameList;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) {
        	mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(TopicTag.NODE)) { 
                for(int i=0; i<attributes.getLength(); i++){
                    String iQName = attributes.getQName(i);
                    if(iQName.equalsIgnoreCase(TopicTag.ID)){
                        String iQNameValue = attributes.getValue(iQName);
                        game = new Game();
                        game.setId(iQNameValue);
                    }
                }
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String data = mStringBuilder.toString();
            
            if(localName.equals(TopicTag.NAME)) {
                game.setName(data);
            }else if(localName.equals(TopicTag.IMG_URL)) {
            	game.setImgUrl(data);
            } else if (localName.equals(TopicTag.DESE)) {
            	game.setDescription(data);
			} else if (localName.equals(TopicTag.COUNT)) {
				game.setCount(data);
			} 
            if(localName.equalsIgnoreCase(TopicTag.NODE)){
            	gameList.add(game);
            	game = null;
            } 
            
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) { 
            super.characters(ch, start, length);
            mStringBuilder.append(ch, start, length);
        } 
        
    }
	
	static class TopicTag {
		static final String NODE = "rc";
		static final String ID = "id";
		static final String NAME = "n";
		static final String IMG_URL = "icp";
		static final String DESE = "nj";
		static final String COUNT = "to";
	}
}
