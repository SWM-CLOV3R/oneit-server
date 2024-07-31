echo "> start.sh"

#!/bin/bash
REPOSITORY=/home/ubuntu/oneit

JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

echo "> $JAR_PATH deploy"
echo "> nohup java -jar -Dspring.profiles.active=dev $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &"
echo "> $RDS_USER : $RDS_ENDPOINT"
nohup java -jar -Dspring.profiles.active=dev $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &
