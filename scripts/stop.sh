#!/bin/bash
echo "> stop.sh"
REPOSITORY=/home/ubuntu/oneit

APP_NAME=oneit
CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]; then
  echo "> No applications are currently running, so do not exit"
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi
