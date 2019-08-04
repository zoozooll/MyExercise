/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.serialization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.msm.internal.MessageTypeAdapter;

public class MessagePropertyTypeAdapterFactories {
	
	/** 
	 * The singleton instance of the {@link MessagePropertyTypeAdapterFactories} 
	 */
	private static MessagePropertyTypeAdapterFactories instance;
	
	/**
	 * A list of {@link PropertyTypeAdapterFactory} to serialize and deserialize the internal 
	 * properties of a {@link Message}
	 */
	private final Map<MessageFilter, PropertyTypeAdapterFactory> factories = 
		Collections.synchronizedMap(new HashMap<MessageFilter, PropertyTypeAdapterFactory>());
	
	public static MessagePropertyTypeAdapterFactories getInstance() {
		if (instance == null) {
			instance = new MessagePropertyTypeAdapterFactories();
		}
		return instance;
	}
	
	/**
	 * Configures the factory for custom serialization or deserialization of Message. User
	 * can provide a custom {@link com.google.gosn.TypeAdapter} to serialize or deserialize 
	 * properties of a {@link com.oregonscientific.meep.msm.Message}.
	 * 
	 * @param filter The filter to determine the type of {@link com.google.gson.TypeAdapter} 
	 * to use for a particular kind of {@link com.oregonscientific.meep.msm.Message}.
	 * @param factory The type adapter factory to register with the factory. It is possible to
	 * use the same {@link com.google.gson.TypeAdapter} for multiple filter
	 */
	public void registerTypeAdapterFactory(MessageFilter filter, PropertyTypeAdapterFactory factory) {
		if (filter != null && factory != null && !factories.containsKey(filter)) {
			factories.put(filter, factory);
		}
	}
	
	/**
	 * Retrieves the {@link PropertyTypeAdapterFactory} that best matches the given data
	 * 
	 * @param message the message to compare against
	 * @return the registered {@link MessageTypeAdapter} that matches the given data.
	 * <code>null</code> if no adapter is registered for the given data
	 */
	public PropertyTypeAdapterFactory getTypeAdapterFactory(final Message message) {
		MessageFilter filter = getBestMatchingFilter(message);
		return filter == null ? null : factories.get(filter);
	}
	
	/**
	 * Retrieve a list of {@link MessageFilter} that matches the given data
	 * 
	 * @param message the message to compare against
	 * @return a list of {@link MessageFilter} that matches the given data
	 */
	private List<MessageFilter> getMatchingFilters(Message message) {
		List<MessageFilter> result = new ArrayList<MessageFilter>();
		
		// Retrieve a list of matching filters
		Set<MessageFilter> filters = factories.keySet();
		for (MessageFilter filter : filters) {
			if (filter.match(message) > 0 && !result.contains(filter)) {
				result.add(filter);
			}
		}
		
		return result;
	}
	
	/**
	 * Retrieves the {@link MessageFilter} that best matches the given data
	 * 
	 * @param message the message to compare against
	 * @return A {@link MessageFilter} that best matches the given data, <code>null</code>
	 * if no filter matches the given data
	 */
	private MessageFilter getBestMatchingFilter(final Message message) {
		List<MessageFilter> filters = getMatchingFilters(message);
		
		// Sort the matching filters to return the best matching
		Collections.sort(filters, new Comparator<MessageFilter>() {

			@Override
			public int compare(MessageFilter lhs, MessageFilter rhs) {
				int lhsMatching = lhs.match(message);
				int rhsMatching = rhs.match(message);
				
				int result = 0;
				if (lhsMatching < rhsMatching) {
					result = -1;
				} else if (lhsMatching > rhsMatching) {
					result = 1;
				}
				return result;
			}
			
		});
		
		// Returns the first element in the list
		return filters.isEmpty() ? null : filters.get(0);
	}

}
