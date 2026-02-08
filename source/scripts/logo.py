#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os

"""
根据 PNG 图片输出适用于 MacOS 的应用图标
"""

# 原 PNG 图片
logo_png_file = '/Users/shouhwang/Documents/gimp/i18n/logo.png'
# 不同尺寸 PNG 图片输出目录
output_dir = "/Users/shouhwang/Documents/gimp/i18n/app.iconset"
# 生成的 icns 文件的输出目录
icns_output_dir = "/Users/shouhwang/Documents/gimp/i18n"
# 项目的 icns 文件的目录，最终会复制到这里
destination = '\'/Users/shouhwang/Desktop/repo/compose/Easy I18n/composeApp/icons/app.icns\''

if not os.path.exists(output_dir):
    os.mkdir(output_dir)
os.system('sips -z   16   16 %s --out %s/icon_16x16.png'      % (logo_png_file, output_dir))
os.system('sips -z   32   32 %s --out %s/icon_16x16@2x.png'   % (logo_png_file, output_dir))
os.system('sips -z   32   32 %s --out %s/icon_32x32.png'      % (logo_png_file, output_dir))
os.system('sips -z   64   64 %s --out %s/icon_32x32@2x.png'   % (logo_png_file, output_dir))
os.system('sips -z   64   64 %s --out %s/icon_64x64.png'      % (logo_png_file, output_dir))
os.system('sips -z  128  128 %s --out %s/icon_64x64@2x.png'   % (logo_png_file, output_dir))
os.system('sips -z  128  128 %s --out %s/icon_128x128.png'    % (logo_png_file, output_dir))
os.system('sips -z  256  256 %s --out %s/icon_128x128@2x.png' % (logo_png_file, output_dir))
os.system('sips -z  256  256 %s --out %s/icon_256x256.png'    % (logo_png_file, output_dir))
os.system('sips -z  512  512 %s --out %s/icon_256x256@2x.png' % (logo_png_file, output_dir))
os.system('sips -z  512  512 %s --out %s/icon_512x512.png'    % (logo_png_file, output_dir))
os.system('sips -z 1024 1024 %s --out %s/icon_512x512@2x.png' % (logo_png_file, output_dir))
os.system('iconutil -c icns %s' % (output_dir))
os.system('cp %s/app.icns %s' % (icns_output_dir, destination))
