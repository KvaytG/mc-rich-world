@echo off
title RichWorld Launcher
rem Запуск Velocity
pushd "velocity"
start "" /max "start.bat"
popd
rem Запуск сервера авторизации
pushd "auth"
start "" /max "start.bat"
popd
rem Запуск сервера "Хаб"
pushd "hub"
start "" /max "start.bat"
popd
rem Запуск режима "Яма"
pushd "pit"
start "" /max "start.bat"
popd
rem Запуск режима "Анархия"
pushd "anarchy"
start "" /max "start.bat"
popd
rem Запуск режима "Симулятор"
pushd "simulator"
start "" /max "start.bat"
popd
rem Конец
exit
