#!/bin/sh
# Builds index.html (a standalone page you can host anywhere) from app.html.
cd "$(dirname "$0")"
{
  printf '<!doctype html>\n<html lang="it">\n<head>\n<meta charset="utf-8">\n'
  printf '<meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">\n'
  printf '<meta name="theme-color" content="#0B4EA2">\n<link rel="manifest" href="manifest.json">\n'
  printf '<style>:root{padding-top:env(safe-area-inset-top,0px);padding-bottom:env(safe-area-inset-bottom,0px)}body{margin:0}[hidden]{display:none!important}</style>\n'
  printf '</head>\n<body>\n'
  cat app.html
  printf '\n</body>\n</html>\n'
} > index.html
