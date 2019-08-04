/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Structured description of message values to be matched. A MessageFilter 
 * can match against operation code, status code, and process in a message.
 * 
 * <h3>Filter Rules</h3>
 * <p>A match is based on the following rules.  Note that
 * for a MessageFilter to match a Message, three conditions must hold:
 * the <strong>message ID</strong> must match, and if message ID is not specified,
 * the <strong>operation code</strong> and <strong>process code</strong> must match, and
 * the <strong>status code</strong> if specified must match.
 * 
 * <p><strong>Process code</strong> matches if the given value match the
 * message process. Note that a MessageFilter with no process code will only
 * match a message that does not have a process code. 
 * 
 * <p><strong>Operation code</strong> match if the given value match the operation
 * code of the message, <em>or</em> if no operation code was specified in the filter.
 * 
 * <p><strong>Status code</strong> match if the given value match the status code
 * of the message received, <em>or</em> if not status code was specified in the filter.
 * 
 * @author Stanley Lam
 */
public class MessageFilter implements Parcelable {
	
	/**
	 * The part of match constant that describes the match occurred. May be either
	 * {@link #MATCH_OPERATION}, {@link #MATCH_PROCESS}, or {@link #MATCH_IDENTIFIER}.
	 */
	public static final int MATCH_ADJUSTMENT_MASK = 0xfff0000;
	
	/**
	 * The filter matched a message with the same process.
	 */
	public static final int MATCH_PROCESS = 0x0100000;

	/**
	 * The filter matched a message with the same operation.
	 */
	public static final int MATCH_OPERATION = 0x0200000;

	/**
	 * The filter matched a message with the same status code.
	 */
	public static final int MATCH_STATUS = 0x0300000;

	/**
	 * The filter matched a message with the same identifier
	 */
	public static final int MATCH_IDENTIFIER = 0x400000;

	/**
	 * The filter didn't match due to different process types.
	 */
	public static final int NO_MATCH_PROCESS = -1;

	/**
	 * The filter didn't match due to different operation types.
	 */
	public static final int NO_MATCH_OPERATION = -2;

	/**
	 * The filter didn't match due to different status codes.
	 */
	public static final int NO_MATCH_STATUS = -3;

	/**
	 * The filter didn't match due to different identifier
	 */
	public static final int NO_MATCH_IDENTIFIER = -4;

	private ArrayList<String> mProcesses = null;
	private ArrayList<String> mOperations = null;
	private ArrayList<String> mIdentifiers = null;

	/**
	 * New empty MessageFilter.
	 */
	public MessageFilter() {
	}
	
	/**
	 * New {@link MessageFilter} that matches an identifier
	 * 
	 * @param identifier the identifier to match
	 */
	public MessageFilter(UUID identifier) {
		addIdentifier(identifier);
	}
	
	/**
	 * New {@link MessageFilter} that matches a single process with no operation.
	 * 
	 * @param process
	 *            The process to match, i.e.
	 *            {@link Message#PROCESS_INSTANT_MESSAGING}
	 */
	public MessageFilter(String process) {
		addProcess(process);
	}
  
	/**
	 * New {@link MessageFilter} that matches a single message process and an operation
	 * code
	 * 
	 * @param process
	 *            The process to match i.e.
	 *            {@link Message#PROCESS_INSTANT_MESSAGING}
	 * @param operation
	 *            The operation to match i.e. {@link Message#OPERATION_CODE_GET_BLACKLIST}
	 */
	public MessageFilter(String process, String operation) {
		addProcess(process);
		addOperation(operation);
	}
	
	/**
	 * Adds a new identifier to match against. If an identifier is included in
	 * the filter, then the ID of a {@link Message} must be one of the matching
	 * identifiers. If no identifier is included, the Message ID is ignored.
	 * 
	 * @param identifier
	 *            the UUID to match against
	 */
	public final void addIdentifier(UUID identifier) {
		if (identifier != null) {
			addIdentifier(identifier.toString());
		}
	}

	/**
	 * Adds a new identifier to match against. If an identifier is included in
	 * the filter, then the ID of a {@link Message} must be one of the matching
	 * identifiers. If no identifier is included, the Message ID is ignored.
	 * 
	 * @param identifier
	 *            the ID of a {@link Message}
	 */
	public final void addIdentifier(String identifier) {
		if (mIdentifiers == null) {
			mIdentifiers = new ArrayList<String>();
		}

		if (identifier != null && !mIdentifiers.contains(identifier)) {
			mIdentifiers.add(identifier);
		}
	}

	/**
	 * Return the number of identifiers in the filter
	 * 
	 * @return the number of identifiers in the filter
	 */
	public final int countIdentifiers() {
		return mIdentifiers != null ? mIdentifiers.size() : 0;
	}
  
	/**
	 * Is the given identifier included in the filter?
	 * 
	 * @param identifier
	 *            the identifier that the filter supports
	 * 
	 * @return {@code true} if the identifier is explicitly mentioned in the
	 *         filter, {@code false} otherwise
	 */
	public final boolean hasIdentifier(String identifier) {
		return mIdentifiers != null && mIdentifiers.contains(identifier);
	}

	/**
	 * Match this filter against the identifier of a {@link Message}. If no
	 * identifier was specified for the filter, the match will always succeed
	 * 
	 * @param identifier
	 *            the desired identifier to look for
	 * @return {@code true} if the identifier is listed in the filter,
	 *         {@code false} otherwise
	 */
	public final boolean matchIdentifier(String identifier) {
		return mIdentifiers == null || mIdentifiers.size() == 0 || hasIdentifier(identifier);
	}

	/**
	 * Adds a new message process to match against. If no identifier is included
	 * in the filter and any actions are included, then a Message's process must
	 * be one of those values to match. Conversely, if an identifier is included
	 * in the filter, adding a process has no impact on matching.
	 * 
	 * @param process
	 *            Name of the process to match, i.e.
	 *            {@link Message#PROCESS_ACCOUNT}
	 */
	public final void addProcess(String process) {
		if (mProcesses == null) {
			mProcesses = new ArrayList<String>();
		}

		if (process != null && !mProcesses.contains(process)) {
			mProcesses.add(process);
		}
	}

	/**
	 * Return the number of processes in the filter
	 * 
	 * @return the number of processes in the filter
	 */
	public final int countProcesses() {
		return mProcesses != null ? mProcesses.size() : 0;
	}

	/**
	 * Determines whether or not the given process is included in the filter
	 * 
	 * @param process
	 *            The process that the filter supports
	 * @return true if the process is explicitly mentioned in the filter, false
	 *         otherwise
	 */
	public final boolean hasProcess(String process) {
		return mProcesses != null && mProcesses.contains(process);
	}

	/**
	 * Returns whether or not a {@link Message} must match against the processes
	 * included in this filter
	 * 
	 * @return {@code true} if the message must match with on of the processes
	 *         in this filter, {@code false} otherwise
	 */
	public final boolean mustMatchProcess() {
		return countIdentifiers() == 0;
	}

	/**
	 * Match this filter against a message's process code. If no process was
	 * specified for the filter, the match will always fail
	 * 
	 * @param process
	 *            The desired process to look for
	 * @return true if the process is listed in the filter, false otherwise
	 */
	public final boolean matchProcess(String process) {
		return hasProcess(process);
	}

	/**
	 * Add a new operation to match against. If any operations are included in
	 * the Message filter, then a Message's operation code must be one of those
	 * values to match.
	 * 
	 * @param operation
	 *            Name of the operation to match, i.e. {@link}
	 */
	public final void addOperation(String operation) {
		if (mOperations == null) {
			mOperations = new ArrayList<String>();
		}

		if (operation != null && !mOperations.contains(operation)) {
			mOperations.add(operation);
		}
	}

	/**
	 * Return the number of operations in the filter
	 * 
	 * @return The number of operations in the filter
	 */
	public final int countOperations() {
		return mOperations != null ? mOperations.size() : 0;
	}

	/**
	 * Determines whether or not the given operation is included in the filter
	 * 
	 * @param operation
	 *            The operation that the filter supports
	 * @return true if the operation is explicitly mentioned in the filter,
	 *         false otherwise
	 */
	public final boolean hasOperation(String operation) {
		return mOperations != null && mOperations.contains(operation);
	}

	/**
	 * Match this filter against a message's operation code. If no operation was
	 * specified for the filter, the match will always succeed
	 * 
	 * @param operation
	 *            The desired operation to look for
	 * @return true if the operation is listed in the filter or if no operation
	 *         was specified for the filter
	 */
	public final boolean matchOperation(String operation) {
		return mOperations == null || mOperations.size() == 0 || hasOperation(operation);
	}

	/**
	 * Test whether this filter matches the given message. A match is only
	 * successful if process and operaton code in the Message match against the
	 * filter
	 * 
	 * @param message
	 *            The message to compare against
	 * @return how well the filter matches against. Negative if it does match,
	 *         zero or positive if it does
	 */
	public final int match(Message message) {
		return message == null ? NO_MATCH_PROCESS : match(message.getProcess(), message.getOperation(), message.getStatus(), message.getMessageID());
	}

	/**
	 * Tests whether this filter matches the given data. A match is only
	 * successful if process, operation and status code match against the
	 * filter.
	 * 
	 * @param process
	 *            The process to match against {@link Message#getProcess()}
	 * @param operation
	 *            The operation to match against {@link Message#getOperation()}
	 * @param status
	 *            The status code to match against {@link Message#getStatus()}
	 * @param identifier
	 *            the identifier to match against {@link Message#getMessageID()}
	 * 
	 * @return A positive integer or one of the error codes
	 *         {@link #NO_MATCH_PROCESS} if the process did not match,
	 *         {@link #NO_MATCH_OPERATION} if the operation did not match or
	 *         {@link #NO_MATCH_STATUS} if the status code returned did not
	 *         match.
	 */
	public final int match(String process, String operation, int status, UUID identifier) {
		int result = 0;
		if (identifier == null || !matchIdentifier(identifier.toString())) {
			return NO_MATCH_IDENTIFIER;
		} else {
			result = MATCH_IDENTIFIER;
		}
		
		if (mustMatchProcess()) {
			if (process == null || !matchProcess(process)) {
				return NO_MATCH_PROCESS;
			} else {
				result = MATCH_PROCESS;
			}
		} else {
			if (process != null && matchProcess(process)) {
				result += MATCH_PROCESS;
			}
		}
		
		if (operation != null && !matchOperation(operation)) {
			return NO_MATCH_OPERATION;
		} else {
			result += MATCH_OPERATION;
		}

		// TODO: match status

		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != getClass()) {
			return false;
		}

		MessageFilter otherFilter = (MessageFilter) other;
		boolean sameProcesses = false;
		if (mProcesses != null && otherFilter.mProcesses != null) {
			sameProcesses = mProcesses.containsAll(otherFilter.mProcesses);
		} else if (mProcesses == null && otherFilter.mProcesses == null) {
			sameProcesses = true;
		}

		boolean sameOperations = false;
		if (mOperations != null && otherFilter.mOperations != null) {
			sameOperations = mOperations.containsAll(otherFilter.mOperations);
		} else if (mOperations == null && otherFilter.mOperations == null) {
			sameOperations = true;
		}

		boolean sameIdentifiers = false;
		if (mIdentifiers != null && otherFilter.mIdentifiers != null) {
			sameIdentifiers = mIdentifiers.containsAll(otherFilter.mIdentifiers);
		} else if (mIdentifiers == null && otherFilter.mIdentifiers == null) {
			sameIdentifiers = true;
		}

		return sameProcesses && sameOperations && sameIdentifiers;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (mProcesses != null) {
			dest.writeInt(1);
			dest.writeStringList(mProcesses);
		} else {
			dest.writeInt(0);
		}

		if (mOperations != null) {
			dest.writeInt(1);
			dest.writeStringList(mOperations);
		} else {
			dest.writeInt(0);
		}

		if (mIdentifiers != null) {
			dest.writeInt(1);
			dest.writeStringList(mIdentifiers);
		} else {
			dest.writeInt(0);
		}
	}

	/** Implement the Parcelable interface {@hide} */
	public static final Creator<MessageFilter> CREATOR = new Creator<MessageFilter>() {

		@Override
		public MessageFilter createFromParcel(Parcel source) {
			MessageFilter result = new MessageFilter();
			if (source.readInt() != 0) {
				if (result.mProcesses == null) {
					result.mProcesses = new ArrayList<String>();
				}
				source.readStringList(result.mProcesses);
			}

			if (source.readInt() != 0) {
				if (result.mOperations == null) {
					result.mOperations = new ArrayList<String>();
				}
				source.readStringList(result.mOperations);
			}

			if (source.readInt() != 0) {
				if (result.mIdentifiers == null) {
					result.mIdentifiers = new ArrayList<String>();
				}
				source.readStringList(result.mIdentifiers);
			}
			return result;
		}

		@Override
		public MessageFilter[] newArray(int size) {
			return new MessageFilter[size];
		}

	};

}
