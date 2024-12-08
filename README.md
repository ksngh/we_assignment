# 쿠폰 관리 서비스

## 프로젝트 개요

사용자에게 쿠폰을 생성, 관리, 사용 및 조회하는 기능을 제공합니다.

---

## 사용 기술 스택

- **Backend**: Java 21, Spring Boot 3.4.0, JPA (Hibernate)
- **Database**: MariaDB, mongoDB
- **Cache**: Redis
- **Deployment**: Docker
- **Authentication**: JWT
- **Build :** Gradle

---

## 요구 사항

- 1개의 주제당 n개의 쿠폰 코드 발행이 가능합니다.
- 쿠폰 코드는 사용자별 1회 사용이 가능합니다.
- 쿠폰 코드는 숫자와 알파벳을 혼용하여 16자리로 구성됩니다.
- 단일 쿠폰 코드에 대해서는 동시 요청시에도 중복 사용이 되지 않도록 처리합니다.
- 필수 구현 사항
    1. 쿠폰 발행
    2. 쿠폰 사용(redeem)
    3. 일괄 쿠폰 정지 (주제별)
- 선택 구현 사항
    1. Unit Test 작성
    2. API 문서 작성
    3. 누적되는 쿠폰 데이터 조회 성능 조회

---

## ERD

아래는 쿠폰 관리 서비스의 데이터베이스 설계입니다:
![coupon_erd](https://github.com/user-attachments/assets/ac08035c-f6e6-4ea1-8d76-72cea9d26bed)



### ERD 설명

- **Coupon**: 쿠폰 정보를 저장하는 테이블.
- **User**: 사용자 정보를 저장하는 테이블.
- **CouponRedemption**: 쿠폰과 사용자 간의 매핑 테이블 (중간 테이블).
- **CouponTopic**: 쿠폰의 그룹 정보를 관리하는 테이블.

---

## 인프라 설계

서비스의 인프라는 다음과 같이 구성되어 있습니다:
![coupon drawio](https://github.com/user-attachments/assets/71866c7a-b7d3-4e0d-a701-8294c0e3837f)


### 인프라 설명

- **Application Layer**: Spring Boot 기반의 애플리케이션. MVC 패턴으로 작업
- **Database**: MariaDB를 사용하여 관계형 데이터 저장. mongoDB를 사용하여 오래된 데이터 파티셔닝
- **Cache**: Redis를 사용하여 쿠폰 사용시 동시성 문제 예방
- **Monitoring :** Prometheus를 이용하여 Circuit Breaker 구현

---

## 시현 영상

시현은 POST MAN과 intelliJ IDE로 진행하였으며, 순서는 다음과 같습니다.

1. 회원 가입 - 로그인 (jwt 토큰 발급)
2. 쿠폰 토픽 생성 - 쿠폰 생성 - 쿠폰 토픽으로 일괄 비활성화 처리
3. 쿠폰 일괄 조회 및 필터처리 확인
4. 쿠폰 코드로 쿠폰 사용 처리 하기
    1. 쿠폰 사용 오류 내서 fallback 함수 발동하는지 확인
    2. prometheus로 fallback 로그 확인
5. 배치를 통해 쿠폰 데이터 mongoDB로 마이그레이션 후 일괄 삭제
6. 테스트 코드 실행 후 REST Docs 생성 확인하기

---

## 주요 기능

### 1. 쿠폰 topic 관리

- 쿠폰 주제 생성
- 쿠폰 주제 조회
- 주제별 쿠폰 활성화 / 비활성화
- 주제별 쿠폰 삭제 (soft delete)

### 2. 쿠폰 관리

- 일괄 쿠폰 정보 조회
- 쿠폰 생성
- 쿠폰 사용 처리 (코드 입력)
- 오래된 쿠폰 데이터 관리

### 3. 사용자 관리

- 사용자 회원가입
- jwt를 이용하여 인증 및 인가 관리

---

## 트러블 슈팅

### 1. 쿠폰 사용시 발생하는 동시성 문제

- **문제**: 쿠폰 사용 시 여러 사용자가 동시에 요청을 보내면 중복 사용 발생.
- **원인**: 데이터베이스 락이 적용되지 않아 동시 업데이트가 발생.
- **해결**: Redis를 활용하여 동시 요청 처리, Redis에 문제 발생 시 resilience4j를 통해 fallback 메소드 실행

### 2. 오래된 데이터 누적 시 발생하는 쿼리 성능 감소 문제

- **문제**: 쿠폰 데이터 누적시 쿼리 성능이 감소
- **해결**: DB index 설정, mongoDB에 주기적으로 파티셔닝 하여 쿼리 성능 문제 해결

---

## 개선점

---

## 프로젝트 실행 방법

### 1. Clone Repository

git clone 

cd coupon-service

### 2. Build and Run

### 3. API 문서 확인

rest Docs를 통해 API 명세를 확인할 수 있습니다:

---

## 작업 방식

1. 이슈를 생성하여 문제를 기록합니다.
2. 기능 별로 브랜치를 생성하여 해당 브랜치의 기능에서 작업을 합니다.
3. Pull Request를 통해 코드를 머지하였습니다.
