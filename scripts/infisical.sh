echo "> infisical.sh"

# 1. ubuntu ec2 instance에서 infisical CLI 설치
curl -1sLf 'https://dl.cloudsmith.io/public/infisical/infisical-cli/setup.deb.sh' | sudo -E bash
sudo apt-get update && sudo apt-get install infisical

# 2. Systems Manager Parameter Store에서 INFISICAL 데이터 가져오기
sudo apt-get install jq -y
export PROJECT_ID=$(aws secretsmanager get-secret-value --secret-id "PROJECT_ID" --query "SecretString" --output text | jq -r .PROJECT_ID)
export CLIENT_ID=$(aws secretsmanager get-secret-value --secret-id "CLIENT_ID" --query "SecretString" --output text | jq -r .CLIENT_ID)
export CLIENT_SECRET=$(aws secretsmanager get-secret-value --secret-id "CLIENT_SECRET" --query "SecretString" --output text | jq -r .CLIENT_SECRET)
export ACTIVE_ENV=dev

# 3. INFISICAL_TOKEN 받아오기 (infisical 로그인 with client id and client secret)
export INFISICAL_TOKEN=$(infisical login --method=universal-auth --client-id=$CLIENT_ID --client-secret=$CLIENT_SECRET --silent --plain)

# 4. INFISICAL 환경변수 파일 가져오기
infisical secrets --projectId=$PROJECT_ID --env=$ACTIVE_ENV --recursive > /home/ubuntu/oneit/secrets_raw.txt

# 5. 표 형식의 데이터를 KEY=VALUE 형식으로 변환하여 .env 파일로 저장
awk 'BEGIN {FS="│"} NR>2 {gsub(/^ +| +$/, "", $2); gsub(/^ +| +$/, "", $3); if ($2 && $3) print $2 "=" $3}' /home/ubuntu/oneit/secrets_raw.txt > /home/ubuntu/oneit/.env

# 6. .env 파일을 읽어 환경변수 설정
export $(cat /home/ubuntu/oneit/.env | xargs)

# 7.환경변수 잘 저장됐는지 확인
echo "> $RDS_USER : $RDS_ENDPOINT"
echo "> $DEV_JWT_SECRET_KEY"



