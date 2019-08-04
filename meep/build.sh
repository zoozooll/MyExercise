#!/bin/bash

set -e

find . -iname "*.apk" -exec rm -f "{}" \;

git diff HEAD~1 | grep "+++" | cut -d/ -f2 | sort | uniq |
while read entry
do
    if [ -d "${entry}" ]; then
        echo "Building ${entry}..."

        pushd "${entry}"
        rm -rf bin/*
        rm -rf gen/*
        ant -Dsdk.dir=/usr/local/android-sdk-linux debug

        if [ $? -ne 0 ]; then
            exit $?
        fi
        popd
    fi
done

exit 0

