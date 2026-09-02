#!/bin/bash

# Запуск Velocity
pushd "velocity" > /dev/null
chmod +x start.sh
screen -dmS velocity ./start.sh
popd > /dev/null

# Запуск сервера авторизации
pushd "auth" > /dev/null
chmod +x start.sh
screen -dmS auth ./start.sh
popd > /dev/null

# Запуск сервера "Хаб"
pushd "hub" > /dev/null
chmod +x start.sh
screen -dmS hub ./start.sh
popd > /dev/null

# Запуск режима "Яма"
pushd "pit" > /dev/null
chmod +x start.sh
screen -dmS pit ./start.sh
popd > /dev/null

# Запуск режима "Анархия"
pushd "anarchy" > /dev/null
chmod +x start.sh
screen -dmS anarchy ./start.sh
popd > /dev/null

# Запуск режима "Симулятор"
pushd "simulator" > /dev/null
chmod +x start.sh
screen -dmS simulator ./start.sh
popd > /dev/null

echo "Все сервера успешно запущены в фоновых сессиях screen!"
