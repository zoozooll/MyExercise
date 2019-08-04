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

package com.idt.bw.ble;

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
    
    public static final String BPD_SERVICE_UUID = "00001810-0000-1000-8000-00805f9b34fb";
    public static final String BPD_CHARACTERISTIC_MEASUREMENT = "00002a35-0000-1000-8000-00805f9b34fb";
    public static final String BPD_CHARACTERISTIC_CUFF_PRESSURE = "00002a36-0000-1000-8000-00805f9b34fb";
    
    public static final UUID UUID_BPD_SERVICE = UUID.fromString(BPD_SERVICE_UUID) ;
    public static final UUID UUID_BPD_CHARACTERISTIC_MEASUREMENT = UUID.fromString(BPD_CHARACTERISTIC_MEASUREMENT);
    public static final UUID UUID_BPD_CHARACTERISTIC_CUFF_PRESSURE = UUID.fromString(BPD_CHARACTERISTIC_CUFF_PRESSURE);
    
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