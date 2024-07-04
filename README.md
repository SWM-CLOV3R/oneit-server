# Oneit API server

### 개요
Oneit 선물 플랫폼의 API server 입니다.

### 실행 가이드
* 레포지토리 clone
* dev branch 에서 실행
* local test

### API Endpoints
> API Sheat : https://api.oneit.gift/api-swagger


| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| GET | /api/hello | TEST API |

---
### CI/CD 파이프라인
Build
1. jdk 17 설치
2. gradle 실행 권한 부여
3. gradle 빌드
4. 빌드 파일 artifacture 업로드

Deploy
1. 빌드 파일 artufacture 다운로드
2. 빌드 파일 실행

