#!/bin/bash
echo "최근 크래시 로그를 찾는 중..."
adb logcat -d | grep -A 100 "FATAL EXCEPTION" | tail -150
