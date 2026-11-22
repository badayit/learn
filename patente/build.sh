#!/bin/sh
# Builds from app.html (the single source):
#   index.html                         web version (GitHub Pages)
#   privacy.html                       standalone privacy policy (URL for Google Play)
#   android/app/src/main/assets/www/   offline copy for the Android app
set -e
cd "$(dirname "$0")"
HEAD='<!doctype html>
<html lang="it">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
<meta name="theme-color" content="#0B4EA2">'
RESET='<style>:root{padding-top:env(safe-area-inset-top,0px);padding-bottom:env(safe-area-inset-bottom,0px)}body{margin:0}[hidden]{display:none!important}</style>
</head>
<body>'
{ echo "$HEAD"; echo '<link rel="manifest" href="manifest.json">'; echo "$RESET"; cat app.html; printf '\n</body>\n</html>\n'; } > index.html

# Android: local fonts, safe-area insets come from the app as CSS variables, no manifest.
mkdir -p android/app/src/main/assets/www
{ echo "$HEAD"; echo "$RESET"; cat app.html; printf '\n</body>\n</html>\n'; } \
 | sed -e 's#<link rel="preconnect" href="https://fonts.googleapis.com">##' \
       -e 's#<link rel="stylesheet" href="https://fonts.googleapis.com/[^"]*">#<link rel="stylesheet" href="fonts/fonts.css">#' \
       -e 's#env(safe-area-inset-top, *0px)#var(--sat, 0px)#g' \
       -e 's#env(safe-area-inset-bottom, *0px)#var(--sab, 0px)#g' \
       -e 's#env(safe-area-inset-left, *0px)#var(--sal, 0px)#g' \
       -e 's#env(safe-area-inset-right, *0px)#var(--sar, 0px)#g' \
 > android/app/src/main/assets/www/index.html
grep -q 'fonts.googleapis' android/app/src/main/assets/www/index.html && { echo "Android build still loads Google Fonts"; exit 1; }

node tools/privacy.js > privacy.html
echo "Built index.html, privacy.html, android assets"
