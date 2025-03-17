<div align="center">
  <h1>WANNA GIFT IT, ONE!T</h1>
  <h3>원하는 선물 바구니에 담아 함께 고르자!</h3>
  <p>선물 추천 및 같이 고르기 서비스</p>

  <h5>
    ⬇️ 바로가기 ⬇️
  </h5>
  <a href="https://www.oneit.gift">
    <img src="https://github.com/user-attachments/assets/ca8726d2-8175-44fe-b3a9-47cb71402851" width="200px" />
  </a>
</div>

<div align="center">
  🍀 Team CLOV3R 🍀
</div>

## 💝 Overview
| 선물 추천 및 함께 고르기 플랫폼
- 프로젝트 배경
  - 온라인 선물하기의 등장으로 선물 구매 및 배송의 편리성이 크게 증가했습니다.
  - 히자만 이로 인해 피로감을 느끼고, [선물을 선정하는 데에 오히려 어려움을 느끼는 사람들](https://www.newsway.co.kr/news/view?ud=2023071309013624079)이 생겼습니다.
  - 선물할 수 있는 선택지가 점점 많아져 혼란한 소비자들은 선물을 주고 받는 과정이 오히려 스트레스로 다가와 진정한 '선물'이라는 의미가 퇴색됩니다.
- 프로젝트 목적
  - 온라인 선물하기 사용자의 선물 선정 고민 완화
  - 여럿이 함께 고르는 선물 커뮤니티
  - 만족도 높은 선물 선정 및 선물 제품 공급자의 새로운 홍보 채널 확보
- 프로젝트 성과
  - (24.11 기준) 누적 사용자 3000, 최대 DAU 200
  - [공식 인스타그램 운영](https://www.instagram.com/oneit.gift/)

## 🍀 Team
- [Frontend](https://github.com/SWM-CLOV3R/oneit-client) : 신혜준 
- [Data](https://github.com/SWM-CLOV3R/oneit-dataset), [Backend](https://github.com/SWM-CLOV3R/oneit-server) : 전세연
- [Backend](https://github.com/SWM-CLOV3R/oneit-server) :최성연

## ⭐️ What we did
- 상대방의 정보를 입력하면 적절한 선물을 추천
  - 성격 유형 테스트 형식으로 초반 사용자 유입 요소로 홍보
- 주요 온라인 선물하기 플랫폼에서 가져온 퀄리티 높은 선물 Pool을 제공
  - 매주 테마별로 선정한 선물 컬렉션
  - '선물'관점에서 제품의 특징을 담은 해시태그와 제품 AI 요약
  - 키워드 기반 제품 필터링
- 함께 선물을 고를 수 있는 '선물 바구니'
  - 친구를 초대하여 참여자들만 볼 수 있는 선물 위시리스트
  - 워닛에서 제공하는 선물 제품을 골라담기
  - 상대방에게 원하는 선물에 대한 의견을 받아 선물 선정에 도움을 받을 수 있다
  


## ✨ Branch Convention
- main
  - 배포 가능한 상태의 코드만을 관리하는 프로덕션용 브랜치
- dev
  - 개발 전용 브랜치
  - 기능 개발이 완료된 브랜치를 병합하여 테스트를 진행



## 🤝 Commit Convention
- feat : 기능 개발
- chore : 잡일 (ex. 오타 수정, 기타 등등)
- fix : 버그 잡기
- test : 테스트 코드
- refactor : 코드 구조 개선
- style : CSS
- build : 빌드 관련 작업 (ex. build.gradle)
- ci : 배포 자동화 관련 작업
- docs : 문서 작업 (ex. README.md)
- perf : 성능 개선



## 📌 Architecture
### 1️⃣ System Architecture
![image](https://github.com/user-attachments/assets/fee3ec7c-4ca6-413e-980f-ebf01912a4e1)

### 2️⃣ Infrastructure Architecture
![image](https://github.com/user-attachments/assets/e8f4f345-f453-40a1-998a-5b661a59bb4b)

### 3️⃣ CI/CD Archtecture
![image](https://github.com/user-attachments/assets/a11b31a6-2f2b-4371-b92d-6bdf37aa6841)





## 📕 Tech Stack
### 1️⃣ Framework & Library
- JDK 17
- SpringBoot 3.3.0
- Spring Data JPA 3.3.0
- QueryDsl 5.1.0
- Spring Boot Batch
- Logback 1.5.6
- Sentry 7.9.0
- Slack API
- Swagger 2.2.15
- JUnit 5.10.2
- Firebase 9.2

### 2️⃣ Build Tools
- Gradle 8.10.2

### 3️⃣ Database
- MySQL 8.3.0
- Flyway 10.10.0

### 4️⃣ Infra
- AWS EC2
- AWS S3
- AWS Route53
- AWS VPC
- AWS RDS
- AWS CodeDeploy
- Docker
- Nginx
- GitHub Actions
