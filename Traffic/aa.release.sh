#!/bin/bash

# Set here your own NDK path if needed
# export PATH=$PATH:~/src/endless_space/android-ndk-r4b
# Set environment to CrystaX NDK with RTTI and exceptions instead of original NDK
# export PATH=$PATH:~/src/endless_space/android-ndk-r4-crystax/ndk-build

 ant release&& cd bin && ./sign_align.bat traffic-unsigned.apk 
