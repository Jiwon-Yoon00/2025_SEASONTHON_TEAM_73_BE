# 🏠리빙메이트 (LivingMate)

## 목차
1. [프로젝트 소개](#-프로젝트-소개)
2. [팀원 소개](#-팀원-소개)
3. [기술 스택](#-기술-스택)
4. [프로젝트 아키텍처](#-프로젝트-아키텍처)
5. [API 응답 구조](#-api-응답)
6. [디렉토리 구조](#-디렉토리-구조)
7. [Branch & Commit 전략](#-branch--commit-전략)

<details>
<summary>리빙메이트 요약 PPT 보기</summary>
<div markdown="1">
  <img width="100%" alt="slide1" src="https://github.com/user-attachments/assets/8a5fd31b-1942-43b5-a15f-3337481e7ccc" />
  <img width="100%" alt="slide2" src="https://github.com/user-attachments/assets/86c6d9c2-9db1-4cb9-81ab-6272fa89da9e" />
  <img width="100%" alt="slide3" src="https://github.com/user-attachments/assets/da03e97a-7bc0-4271-9d36-7a030cc5c39d" />
  <img width="100%" alt="slide4" src="https://github.com/user-attachments/assets/976e3e06-673e-487e-b5a2-bdd7014d339f" />
  <img width="100%" alt="slide5" src="https://github.com/user-attachments/assets/78187d32-bd55-4db3-8bcf-1570b3cc5887" />
  <img width="100%" alt="slide6" src="https://github.com/user-attachments/assets/19c02b02-e493-4714-a598-a51b335d8f5c" />
  <img width="100%" alt="slide7" src="https://github.com/user-attachments/assets/920d0d15-a5b1-4b7b-95ca-3a2c74f29207" />
  <img width="100%" alt="slide8" src="https://github.com/user-attachments/assets/e5746e40-4a8e-4203-b9aa-2551670c0830" />
  <img width="100%" alt="slide9" src="https://github.com/user-attachments/assets/efb3fa68-b7cf-4432-ada5-c04cdf2e214b" />
  <img width="100%" alt="slide10" src="https://github.com/user-attachments/assets/ddde4aad-9185-4367-bedf-1db33a501f89" />
  <img width="100%" alt="slide11" src="https://github.com/user-attachments/assets/b1772a94-6982-4d89-8397-da6833d660ec" />
</div>
</details>

---

# 🏠 프로젝트 소개

## 프로젝트 개요
리빙메이트는 **성향 기반 룸메이트 매칭 서비스**로, 1인 가구 증가와 서울 내 주거난으로 발생하는 정보 불평등과 불편함을 해결합니다.  
사용자는 설문 기반 성향 조사와 AI 추천 기능을 통해 **나에게 맞는 룸메이트**를 빠르고 편리하게 찾을 수 있습니다.

## 프로젝트 배경
- 서울 내 집 마련 기간: **약 25.1년** → 1인 가구 증가  
- 2052년까지 매년 약 **7.4만 명 1인 가구** 증가 예상  
- 공유 주거(Co-living) 시장 급성장  
  - 지난 9년간 **5배 성장**  
  - 대기업 및 서울시 공유 주택 정책 시행  
- 문제점: 룸메이트 선택 시 필요한 정보 부족 및 기준 모호  

## 서비스 소개
리빙메이트는 **성향 조사 기반 매칭**으로 다음 문제를 해결합니다:  
1. 룸메이트 정보 불균형 문제  
2. 정보 누락으로 발생하는 불필요한 연락 최소화  
3. 주관적 기준 사용으로 발생하는 매칭 불편 해소  

### 핵심 개념/기능
1. **성향 조사 기반 매칭**
      - 13문항 설문으로 생활 습관, 출퇴근 시간, 청결 등 세부 기준 분석
      - 누락 정보 방지로 정확한 매칭 지원

2. **AI 추천**
    - 사용자가 선택한 3가지 생활 기준에 최적화된 룸메 후보 10명 추천
    - GPT API 기반 데이터 분석

3. **유저 필터**
    - 13문항 중 중요 5개 기준 선택 가능
    - 집 유무, 생활 기준 등 다중 필터 제공
    - 맞춤형 후보 검색 가능

4. **홈 대시보드**
    - Sharer(집 있음) / Joiner(집 없음) 구분
    - 맞춤 추천, 사용자 관리, UI/UX 개선

5. **사용자 신원 관리**
    - SMS 문자 인증 1차 본인 인증
    - 재학/재직 증명서 업로드 통한 2단계 인증

## 비즈니스 모델
1. **초기 사용자 확보**  
   - 대학생 중심: 기숙사 배정 불안 → Joiner 모집  
   - 커뮤니티 기반 Sharer 모집  
2. **수익화 전략**  
   - ‘그라운드 룰 키트’ 제공  
   - 3일간 상단 노출되는 ‘급구 부스터’ 기능  
3. **확장 전략**  
   - 일상 케어 및 생활 관리 프리미엄 서비스  
   - 대학/쉐어하우스 협업  

---

# 팀원 소개

| 이름 | 역할 | 한 줄 소개 |
|------|------|------------|
| 이재민 | 팀장 / 기획 | 프로젝트 전반 기획 및 일정 관리 |
| 주현지 | 디자이너 | UI/UX 디자인 및 프로토타입 제작 |
| 엄현용 | 프론트엔드 | RN 기반 화면 구현 및 UI 연동 |
| 한유진 | 프론트엔드 | RN 기반 화면 구현 및 UI 연동 |
| 윤지원 | 백엔드 | 로그인/회원가입, 마이페이지 CRUD, 채팅, 프로필 API, 유저 필터, 문자인증 |
| 정다운 | 백엔드 | 게시글 CRUD, 지도 API, 배포/CI-CD, S3 설정, AI 추천 기능 |


# 🛠️ 기술 스택

## 백엔드 기술 스택

- 언어 / 프레임워크: Java 17, Spring Boot 3.5
- 웹 / API: Spring Web, Spring WebSocket, Spring WebFlux
- 데이터베이스 / ORM: MySQL, Spring Data JPA, QueryDSL
- 보안: Spring Security, JWT
- 클라우드 / 외부 서비스: AWS S3, Nurigo SMS SDK, 카카오맵 API
- 개발 편의 / 생산성: Lombok, Spring Boot DevTools
- 테스트: JUnit 5, Spring Boot Test

## 인프라 기술 스택

- AWS EC2 + Docker
- AWS RDS MySQL
- AWS S3
- Docker Hub
- GitHub Actions (CI/CD)

---

# 📋 프로젝트 아키텍처
<img width="100%" alt="architecture" src="https://github.com/user-attachments/assets/b1772a94-6982-4d89-8397-da6833d660ec" />

---

# 💬 API 응답 구조

리빙메이트 백엔드는 **일관된 API 응답 구조**와 **체계적인 에러 메시지 관리**를 통해 안정적인 서비스 경험을 제공합니다.
### 1. 공통 응답 구조

```java
public class Response<T> {
    private boolean success; // 요청 성공 여부
    private String code;     // 응답 코드 (성공/에러 구분)
    private String message;  // 사용자/개발자 메시지
    private T data;          // 실제 응답 데이터
}
```

## 성공응답 예시
```java
{
    "success": true,
    "code": "COMMON200",
    "message": "요청이 성공적으로 처리되었습니다.",
    "data": { // 데이터 응답}
}
```

## 실패 응답 예시
```
{
    "success": false,
    "code": "COMMON404",
    "message": "사용자를 찾을 수 없습니다"
} 
```
---

# 📁 디렉토리 구조
```html
src
└── main
└── java/com/season/livingmate
  ├── auth
  ├── chat
  ├── config
  ├── exception
  ├── geo
  ├── global
  ├── gpt
  ├── main
  ├── map
  ├── post
  ├── s3
  └── user
```

# 🌳 Branch & Commit 전략

## Branch 전략
- **main**: 운영 환경 반영  
- **develop**: 기능 개발 후 머지  
- **feature/**: 기능 단위 개발 (예: `feat/#11-login`)  
- **fix/**: 버그 수정 (예: `fix/#7-payment-bug`)  
- **hotfix/**: 긴급 오류 수정  

> 개발 흐름: `feature/* → develop → main`

## Commit Types

| 타입        | 설명 |
|------------|------|
| FEAT       | 새로운 기능 구현 |
| MOD        | 코드/내부 파일 수정 |
| ADD        | 부수적 코드/라이브러리/파일 추가 |
| CHORE      | 작은 변경(버전, 패키지 구조, 타입/변수명 등) |
| DEL        | 불필요한 코드/파일 삭제 |
| FIX        | 버그/오류 수정 |
| HOTFIX     | 긴급 버그/오류 수정 |
| MERGE      | 브랜치 병합 |
| MOVE       | 파일/코드 이동 |
| RENAME     | 파일 이름 변경 |
| REFACTOR   | 코드 전면 수정 |
| DOCS       | 문서 수정(README, WIKI 등) |

## Issue
- 형식: `[타입] 메시지`  
- 예시: `[FEAT] 게시물 CRUD 기능`

## Branch Naming
- 형식: `타입/#이슈번호-이름`  
- 예시: `feat/#11-login`, `fix/#7-payment-bug`

## Commit
- 형식: `[타입/#이슈번호] 메시지`  
- 예시: `[FEAT/#11] DTO 추가`, `[FIX/#7] 결제 오류 수정`

## Pull Request
- 형식: `[타입/#이슈번호] 메시지`  
- 예시: `[FEAT/#11] 게시물 CRUD 기능 구현`

## Merge
- 조건: 코드 리뷰 후 머지  
- 형식: `[MERGE] #번호 -> 대상브랜치`  
- 예시: `[MERGE] #11 -> develop`
