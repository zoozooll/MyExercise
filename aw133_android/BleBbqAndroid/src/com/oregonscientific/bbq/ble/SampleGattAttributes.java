/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oregonscientific.bbq.ble;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    
	public static final String BLE_BBQ_SCANNABLE = "IDTQ133P";
	public static final String BLE_BBQ_JOIN = "IDTQ133A";
	private static HashMap<String, String> attributes = new HashMap<String, String>();
    public static final String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    
    private static final String UUIDSTR_FORMAT = "2899%s-c277-48a8-91cb-b29ab0f01ac4";
    /**It will be shown in the advertise data*/
    public static final String UUIDSTR_SERVICE_ADVERTISE = String.format(UUIDSTR_FORMAT, "fe00");
    /**The command send from the APPs & the return value from device*/
    public static final String UUIDSTR_CHARACTERISTIC_COMMAND = String.format(UUIDSTR_FORMAT, "8e03");
    /**Channel 1 cooking information*/
    public static final String UUIDSTR_CHARACTERISTIC_CH1INFO = String.format(UUIDSTR_FORMAT, "8e10");
    /**Channel 2 cooking information*/
    public static final String UUIDSTR_CHARACTERISTIC_CH2INFO = String.format(UUIDSTR_FORMAT, "8e11");
    
    public static final String UUIDSTR_SERVICE_INFORMATIONS = "0000180a-0000-1000-8000-00805f9b34fb";
    public static final String UUIDSTR_CHARACTERISTIC_BBQ_VERSION = "00002a26-0000-1000-8000-00805f9b34fb";
    
    
    //Command bytes
    public static final byte COMMANDBYTE_REQUEST_CHANNEL_DATA_RESEND = (byte) 0x81;
    public static final byte COMMANDBYTE_SETUP_BBQ_CHANNEL = (byte) 0x82;
    public static final byte COMMANDBYTE_START_OPERATION = (byte) 0x83;
    public static final byte COMMANDBYTE_STOP_OPERATION = (byte) 0x84;
    public static final byte COMMANDBYTE_REQUEST_CUSTOM_DONENESS = (byte) 0x85;
    public static final byte COMMANDBYTE_CLEAR_CUSTOM_DONENESS = (byte) 0x86;
    public static final byte COMMANDBYTE_SET_CUSTOM_DONENESS = (byte) 0x90;
    
    public static final UUID UUID_SERVICE_ADVERTISE = UUID.fromString(UUIDSTR_SERVICE_ADVERTISE);
    public static final UUID UUID_CHARACTERISTIC_COMMAND = UUID.fromString(UUIDSTR_CHARACTERISTIC_COMMAND);
    public static final UUID UUID_CHARACTERISTIC_CH1INFO = UUID.fromString(UUIDSTR_CHARACTERISTIC_CH1INFO);
    public static final UUID UUID_CHARACTERISTIC_CH2INFO = UUID.fromString(UUIDSTR_CHARACTERISTIC_CH2INFO);
    
    static {
        // Sample Services.
        attributes.put("0000180d-0000-1000-8000-00805f9b34fb", "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(HEART_RATE_MEASUREMENT, "Heart Rate Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
