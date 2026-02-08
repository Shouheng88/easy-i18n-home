#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import platform

"""
项目构建脚本
"""

VERSION = '1.0.2'

system_name = platform.system()
if system_name == 'Windows':
    print("Build project for Windows>>>")
    os.system('cd .. && gradlew.bat clean packageReleaseMsi -Pbuild_version=%s' % (VERSION))
elif system_name == 'Darwin':
    print("Build project for MacOS>>>")
    RESOURCE_DIRECTORY = "/Users/shouhwang/Desktop/repo/other/i18n-resources"
    JDK_PATH = '/Users/shouhwang/.gradle/jdks/eclipse_adoptium-17-aarch64-os_x.2/jdk-17.0.14+7/Contents/Home'

    os.system('cd .. && ./gradlew clean packageReleaseDmg -Dorg.gradle.java.home=%s -Pbuild_version=%s' % (JDK_PATH, VERSION))
    os.system('cp -f ../composeApp/mapping.txt %s/mappings/%s_mappings.txt' % (RESOURCE_DIRECTORY, VERSION))
    dir = '%s/%s' % (RESOURCE_DIRECTORY, VERSION)
    if not os.path.exists(dir):
        os.mkdir(dir)
    os.system('cp -f ../composeApp/build/compose/binaries/main-release/dmg/*.dmg %s/%s.dmg' % (dir, VERSION))
    os.system('cp -rf ../composeApp/build/compose/binaries/main-release/app/* /%s/%s.app' % (dir, VERSION))
elif system_name == 'Linux':
    print("Build project for Linux>>>")
else:
    print(f"未知系统：{system_name}")
