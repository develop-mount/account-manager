#!/bin/bash

# 设置应用的 jar 包名
APP_NAME="./account-manager-1.0.1.jar"

# 检查应用是否已经在运行
PIDS=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')

if [ -n "$PIDS" ]; then
  echo "开始关闭应用正在运行的应用，PID: $PIDS"
  kill -15 PIDS
fi

echo "应用已停止"
