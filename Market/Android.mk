# Copyright 2007-2008 The Android Open Source Project

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

# lxr add 2012.07.18
LOCAL_STATIC_JAVA_LIBRARIES := supportv4
# lxr add end 2012.07.18

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := MogooMarket

# Builds against the public SDK
#LOCAL_SDK_VERSION := current

# lxr add 2012.07.18
include $(BUILD_PACKAGE)
include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES :=supportv4:libs/android-support-v4.jar
include $(BUILD_MULTI_PREBUILT)
# lxr add end 2012.07.18

# LOCAL_STATIC_JAVA_LIBRARIES += android-common

# LOCAL_REQUIRED_MODULES := SoundRecorder

# LOCAL_PROGUARD_FLAGS := -include $(LOCAL_PATH)/proguard.flags

# include $(BUILD_PACKAGE)

# This finds and builds the test apk as well, so a single make does both.
include $(call all-makefiles-under,$(LOCAL_PATH))