#!/bin/bash

# 设置应用的 jar 包名
APP_NAME="./account-manager-1.0.1.jar"

# 日志文件路径
LOG_FILE="./app.log"

# 检查应用是否已经在运行
PIDS=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')

if [ -n "$PIDS" ]; then
  echo "应用已经在运行，PID: $PIDS"
  exit 1
fi

# 使用 nohup 后台启动应用并将日志输出到指定文件
nohup java -Djava.security.egd=file:/dev/./urandom \
           -Dspring.profiles.active=prod \
           -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC -Xms1024m -Xmx2048m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m \
           -jar $APP_NAME > $LOG_FILE 2>&1 &

echo "应用已启动，日志输出到 $LOG_FILE"
