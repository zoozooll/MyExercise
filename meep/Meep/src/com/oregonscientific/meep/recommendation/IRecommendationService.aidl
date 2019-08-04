/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import com.oregonscientific.meep.recommendation.Recommendation;
import com.oregonscientific.meep.recommendation.IRecommendationServiceCallback;
import com.oregonscientific.meep.account.Account;

/**
 * Interface for interacting with the Recommendation Service
 */
interface IRecommendationService {
	
	/**
	 * Adds an entry to the recommendation list in local store. Note that the 
	 * recommendation is only added in the local store. Account must call 
	 * {@link #updateRecommendations()} to update the list of recommendations with
	 * 
	 * @param user  The user whose recommendation to add to. This 
	 * should be the tag that uniquely identifies the user
	 * @param recommendation The recommendation to add to the local store
	 */
	boolean addRecommendation(String user, in Recommendation recommendation);
	
	/**
	 * Removes the {@code recommendation} from the recommendation list. Note that
	 * the recommendation is only removed in the local store. Account must call 
	 * {@link #updateRecommendations()} to update the list of recommendations with
	 * server 
	 * 
	 * @param user  The user whose recommendation to remove from. This 
	 * should be the tag that uniquely identifies the user
	 * @param recommendation The recommendation to remove from the local store
	 */
	boolean removeRecommendation(String user, in Recommendation recommendation);
	
	/**
	 * Synchronize recommendation list in database with server's recommendation list
	 *  
	 * @param user  The user whose recommendations to apply to. This 
	 * should be the tag that uniquely identifies the user
	 * @param type the type of recommendation to synchronize with server
	 */
	void syncRecommendations(String user, String type);
	
	/**
	 * Retrieve recommendation list from database
	 * 
	 * @param user user the use whose {@link Recommendation} to retrieve. This should be the meep tag that uniquely identifies the user
	 * @param type the type of recommendation to retrieve
	 * @return A list of Recommendation objects
	 */
	List<Recommendation> getRecommendations(String user, String type);
	
	/**
	 * Retrieve all recommendations for the given {@code user}
	 * 
	 * @param user the use whose {@link Recommendation} to retrieve. This should be the meep tag that uniquely identifies the user
	 * @return A list of {@link Recommendation} objects or {@code null} if none is found
	 */
	List<Recommendation> getAllRecommendations(String user);
	
	/**
	 * Determines whether or not the given URL is one of the recommendations
	 */
	boolean isUrlRecommended(String user, String url);
	
	/**
	 * Register the {@code callback} to be run when the given {@code type} of recommendation
	 * is received
	 * 
	 * @param type The type of recommendation to register for. Usually one of {@link #TYPE_WEB_FROM_ADMIN}
	 * {@link #TYPE_YOUTUBE_FROM_ADMIN}, {@link #TYPE_WEB_FROM_PARENT}, or {@link #TYPE_YOUTUBE_FROM_PARENT}
	 * @param callback The callback to invoke when the given {@code type} of recommendation is
	 * received
	 */
	void registerCallback(String type, IRecommendationServiceCallback callback);
	
	/**
	  * Removes a previously registered {@code callback} from receiving the given {@code type} 
	  * of recommendation
	  * 
	  * @param type The type of recommendation to register for. Usually one of {@link #TYPE_WEB_FROM_ADMIN}
	 * {@link #TYPE_YOUTUBE_FROM_ADMIN}, {@link #TYPE_WEB_FROM_PARENT}, or {@link #TYPE_YOUTUBE_FROM_PARENT}
	 * @param callback The callback to unregister for
	  */
	void unregisterCallback(String type, IRecommendationServiceCallback callback);
	 
 }
 