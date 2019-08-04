/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import com.oregonscientific.meep.recommendation.Recommendation;

/**
 * Interface for interacting with the Recommendation
 */
oneway interface IRecommendationServiceCallback {
	
	/**
	 * Called by the recommendation service when a recommendation is received.
	 * Only the registered type of recommendation is delivered.
	 * 
	 * @param recommendation The Recommendation received
	 */
	void onReceiveRecommendation(out List<Recommendation> recommandations);
	
}