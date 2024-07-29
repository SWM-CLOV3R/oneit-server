#!/bin/bash
REPOSITORY=/home/ubuntu/oneit # 배포된 프로젝트 경로
cd $REPOSITORY # 해당 경로로 이동

APP_NAME=oneit_server
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> No applications are currently running, so do not exit"
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH deploy"
nohup java -jar \
        -Dspring.profiles.active=dev \
        build/libs/$JAR_NAME > $REPOSITORY/nohup.out 2>&1 &