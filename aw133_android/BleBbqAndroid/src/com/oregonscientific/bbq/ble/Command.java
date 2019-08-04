/**
 * 
 */
package com.oregonscientific.bbq.ble;

import java.io.Serializable;
import java.util.Arrays;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author aaronli
 *
 */
public class Command implements Parcelable {

	/**
	 * 
	 */
	private int mData;
	private CommandType type;
	private BluetoothGattCharacteristic characteristic;
	private byte[] value;
	private boolean isUsing;
	
	
	/**
	 * 
	 */
	public Command() {
	}

	private Command(Parcel in) {
		mData = in.readInt();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((characteristic == null) ? 0 : characteristic.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + Arrays.hashCode(value);
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (characteristic == null) {
			if (other.characteristic != null)
				return false;
		} else if (!characteristic.equals(other.characteristic))
			return false;
		if (type != other.type)
			return false;
		if (!Arrays.equals(value, other.value))
			return false;
		return true;
	}
	/**
	 * @return the type
	 */
	public CommandType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(CommandType type) {
		this.type = type;
	}
	/**
	 * @return the characteristic
	 */
	public BluetoothGattCharacteristic getCharacteristic() {
		return characteristic;
	}
	/**
	 * @param characteristic the characteristic to set
	 */
	public void setCharacteristic(BluetoothGattCharacteristic characteristic) {
		this.characteristic = characteristic;
	}
	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}
	/**
	 * @return the isUsing
	 */
	public boolean isUsing() {
		return isUsing;
	}
	/**
	 * @param isUsing the isUsing to set
	 */
	public void setUsing(boolean isUsing) {
		this.isUsing = isUsing;
	}
	
	
	public static enum CommandType {
		READ,
		WRITE,
		INDIACATION,
		NOTIFY
	}


	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mData); 
		
	};
	
	public static final Parcelable.Creator<Command> CREATOR = new Parcelable.Creator<Command>() {
		public Command createFromParcel(Parcel in) {
			return new Command(in);
		}

		public Command[] newArray(int size) {
			return new Command[size];
		}
	};
}
