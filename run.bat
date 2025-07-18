# Скрипт для запуска программы с правильной кодировкой
@echo off
chcp 65001 > nul
java -Dfile.encoding=UTF-8 -jar target\xmlparser.jar %1
pause