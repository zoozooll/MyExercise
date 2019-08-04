package com.oregonscientific.meep.youtube;

import java.util.List;

import android.util.Log;

import com.google.api.client.util.Key;

public class YoutubeGData {
		@Key Feed feed; // Feed is an object, not a list
		@Key String version;
	    @Key String encoding;
	    @Key Entry entry;
	    
		public String getVersion() {
	    	return version;
		}
	    
		public void setVersion(String version) {
	    	this.version = version;
		}
	    
		public String getEncoding() {
			return encoding;
		}
	    
		public void setEncoding(String encoding) {
			this.encoding = encoding;
		}
	    
		public Feed getFeed() {
			return feed;
		}
	    
		public void setFeed(Feed feed) {
			this.feed = feed;
		}
		
		public Entry getEntry() {
			return entry;
		}
	    
		public void setEntry(Entry entry) {
			this.entry = entry;
		}
		
	public class Feed {
		@Key String xmlns;
		@Key List<Entry> entry;
		@Key OpenSearch$totalResults openSearch$totalResults; 

		public OpenSearch$totalResults getOpenSearch$totalResults() {
			return openSearch$totalResults;
		}

		public void setOpenSearch$totalResults(OpenSearch$totalResults openSearch$totalResults) {
			this.openSearch$totalResults = openSearch$totalResults;
		}

		public String getXmlns() {
			return xmlns;
		}
	
		public void setXmlns(String xmlns) {
			this.xmlns = xmlns;
		}
	
		public List<Entry> getEntry() {
			return entry;
		}
	
		public void setEntry(List<Entry> entry) {
			this.entry = entry;
		}
	}
	  
	public class Entry {
		@Key Title title;
		@Key Yt$statistics yt$statistics;
		@Key Media$group media$group;
		@Key App$control app$control;
		@Key("link") List<Link> link;

		
		
			
		
		public List<Link> getLinks() {
			return link;
		}

		public void setLinks(List<Link> link) {
			this.link = link;
		}
		public Title getTitle() {
			return title;
		}
	
		public void setTitle(Title title) {
			this.title = title;
		}
	
		//2013-02-21 - Raymond - Check Video status
		public App$control getApp$control() {
			return app$control;
		}
	
		public void setApp$control(App$control app$control) {
			this.app$control = app$control;
		}
		
		public Yt$statistics getYt$statistics() {
			return yt$statistics;
		}
	
		public void setYt$statistics(Yt$statistics yt$statistics) {
			this.yt$statistics = yt$statistics;
		}
	
		public Media$group getMedia$group() {
			return media$group;
		}
	
		public void setMedia$group(Media$group media$group) {
			this.media$group = media$group;
		}
	}
	  
	public class Title {
		@Key String type;
		@Key String $t;
		  
		public String getType() {
			return type;
		}
	
		public void setType(String type) {
			this.type = type;
		}
	
		public String get$t() {
			return $t;
		}
	
		public void set$t(String $t) {
			this.$t = $t;
		}
	}
	  
	public class Media$group{
		@Key Yt$videoid yt$videoid;
		@Key Yt$duration yt$duration;

		@Key Yt$thumbnail yt$thumbnail;
		public Yt$thumbnail getYt$thumbnail() {
			return yt$thumbnail;
		}
	
		public void setYt$thumbnail(Yt$thumbnail yt$thumbnail) {
			Log.e("yt$thumbnail",yt$thumbnail.get$t());
			this.yt$thumbnail = yt$thumbnail;
		}


		//2013-02-21 - Raymond - Check Video status
		@Key List<Media$restriction> media$restriction;
		public List<Media$restriction> getMedia$restriction() {
			return media$restriction;
		}
	
		public void setMedia$restriction(List<Media$restriction> media$restriction) {
			this.media$restriction = media$restriction;
		}		
		
	
		
		public Yt$videoid getYt$videoid() {
			return yt$videoid;
		}
	
		public void setYt$videoid(Yt$videoid yt$videoid) {
			Log.e("yt$videoid",yt$videoid.get$t());
			this.yt$videoid = yt$videoid;
		}
	
		public Yt$duration getYt$duration() {
			return yt$duration;
		}
	
		public void setYt$duration(Yt$duration yt$duration) {
			this.yt$duration = yt$duration;
		}
	}
	  
	public class Yt$videoid {
		@Key String $t;
	
		public String get$t() {
			return $t;
		}
	
		public void set$t(String $t) {
			this.$t = $t;
		}
	}
	  
	public class Yt$statistics {
		@Key int viewCount;
	
		public int getViewCount() {
			return viewCount;
		}
	
		public void setViewCount(int viewCount) {
			this.viewCount = viewCount;
		}
	}
	  
	public class Yt$duration {
		@Key int seconds;
	
		public int getSeconds() {
			return seconds;
		}
	
		public void setSeconds(int seconds) {
			this.seconds = seconds;
		}
	}
	
	public static class Link {
		@Key("@href")
	    String href;

	    @Key("@rel")
	    String rel;

	    public static String find(List<Link> links, String rel) {
	      if (links != null) {
	        for (Link link : links) {
	          if (rel.equals(link.rel)) {
	            return link.href;
	          }
	        }
	      }
	    return null;
	    }
	}
	public class OpenSearch$totalResults {
		@Key int $t;
	
		public int get$t() {
			return $t;
		}
	
		public void set$t(int $t) {
			this.$t = $t;
		}
	}


	public class Yt$state {
		@Key String $t;
	
		public String get$t() {
			return $t;
		}
	
		public void set$t(String $t) {
			this.$t = $t;
		}
	}

//
//	public class Media$restriction {
//		@Key String $t;
//	
//		public String get$t() {
//			return $t;
//		}
//	
//		public void set$t(String $t) {
//			this.$t = $t;
//		}
//	}
	
	
	
	public static class Media$restriction {
		@Key("@type")
	    String type;

	    @Key("@relationship")
	    String relationship;

	    public static String find(List<Media$restriction> restrictions, String rel) {
	      if (restrictions != null) {
	        for (Media$restriction restriction : restrictions) {
	            return restriction.relationship;	          
	        }
	      }
	    return null;
	    }
	}	
	
	public class Yt$thumbnail {
		@Key String $t;
	
		public String get$t() {
			return $t;
		}
	
		public void set$t(String $t) {
			this.$t = $t;
		}
	}
	
	
	  
	//2013-02-21 - Raymond - Check Video status	
	public class App$control{
		@Key Yt$state yt$state;
		  
		public Yt$state getYt$state() {
			return yt$state;
		}
	
		public void setYt$state(Yt$state yt$state) {
			this.yt$state = yt$state;
		}
			
	}
	
}
