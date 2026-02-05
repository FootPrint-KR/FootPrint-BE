# Blog

Velog 시리즈 "발자취" 작성용.
커밋 전 Claude Code에게 해당 회차 블로그 초안을 요청할 것.
블로그 초안 파일(`*회차.md`)은 `.gitignore`에 포함되어 git에 올라가지 않음.

## 시리즈 목록

| 회차 | 제목 | 상태 |
|------|------|------|
| 1회차 | [기술 스택 선정, 프로젝트 생성](https://velog.io/@dnjswns0010/%EC%82%AC%EC%9D%B4%EB%93%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%8B%A4-%EC%A3%BD%EC%9D%80-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%82%B4%EB%A6%AC%EA%B8%B0-1%ED%9A%8C%EC%B0%A8-5%EB%85%84-%EC%A0%84-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%97%B4%EC%96%B4%EB%B3%B4%EA%B3%A0-%EA%B2%B0%EC%8B%AC%ED%95%9C-%EC%9D%B4%ED%9B%84) | 발행 완료 |
| 2회차 | [프로젝트 세팅 & 문서 정비](https://velog.io/@dnjswns0010/%EC%82%AC%EC%9D%B4%EB%93%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B0%9C%EC%9E%90%EC%B7%A8-2%ED%9A%8C%EC%B0%A8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%84%B8%ED%8C%85-%EB%AC%B8%EC%84%9C-%EC%A0%95%EB%B9%84-k2s2l8r9) | 발행 완료 |
| 3회차 | [V1 Entity 구현 & 개발 환경 구축](https://velog.io/@dnjswns0010/%EC%82%AC%EC%9D%B4%EB%93%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B0%9C%EC%9E%90%EC%B7%A8-3%ED%9A%8C%EC%B0%A8-V1-Entity-%EA%B5%AC%ED%98%84-%EA%B0%9C%EB%B0%9C-%ED%99%98%EA%B2%BD-%EA%B5%AC%EC%B6%95) | 발행 완료 |
| 4회차 | [Auth 도메인 구현 (JWT 인증)](https://velog.io/@dnjswns0010/%EC%82%AC%EC%9D%B4%EB%93%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%B0%9C%EC%9E%90%EC%B7%A8-4%ED%9A%8C%EC%B0%A8-Auth-%EB%8F%84%EB%A9%94%EC%9D%B8-%EA%B5%AC%ED%98%84-JWT-%EC%9D%B8%EC%A6%9D) | 발행 완료 |
| 5회차 | Region/Category/User 도메인 구현 | 작성 중 |

## 회차별 작업 내역

### 1회차: 기술 스택 선정, 프로젝트 생성
- 2021년 학원 프로젝트 "유레카" 리빌딩 결정
- 기술 스택 선정 (Java 21, Spring Boot, PostgreSQL, Next.js 등)
- Spring Initializr로 프로젝트 생성

### 2회차: 프로젝트 세팅 & 문서 정비
- 인프라 코드 구성 (SecurityConfig, SwaggerConfig, CacheConfig, S3Config, JpaConfig)
- 공통 응답/예외 처리 (ApiResponse, BusinessException, ErrorCode, GlobalExceptionHandler)
- BaseEntity (JPA Auditing)
- 도메인별 패키지 구조 생성
- 문서 8개 작성 (entity-design, api-design, auth-policy, business-rules, coding-patterns, decisions, flow, error-codes)
- GitHub Actions CI 워크플로우 추가

### 3회차: V1 Entity 구현 & 개발 환경 구축
- ErrorCode 인터페이스 패턴으로 리팩토링 (인터페이스 + 도메인별 enum)
- V1 Entity 11개 구현 (User, Post, Region, City, Category, SubCategory, PostImage, PostLike, RecommendCity, CommonCodeGroup, CommonCode)
- Enum 2개 (Gender, UserRole)
- Docker Compose로 로컬 PostgreSQL 17 환경 구축
- Spring Profiles 환경 분리 (local, prod, test)
- H2 인메모리 DB 테스트 설정
- CI gradlew 실행 권한 수정
- CodeRabbit AI 코드 리뷰 도입
- spring-boot-starter-webmvc → spring-boot-starter-web 수정

### 4회차: Auth 도메인 구현 (JWT 인증)
- JWT 기반 인증 구현 (Session → JWT 전환)
- Access Token (30분) + Refresh Token (14일) 전략
- Refresh Token Rotation 적용
- AuthController: signup, login, reissue API
- AuthService: 회원가입(중복체크+BCrypt), 로그인(JWT발급), 토큰 재발급
- JwtTokenProvider: 토큰 생성/검증/클레임 추출
- JwtAuthenticationFilter: Bearer 토큰 추출 및 SecurityContext 설정
- RefreshToken Entity/Repository
- Auth/User ErrorCode & Exception
- SecurityConfig: JWT 필터 등록, permitAll 엔드포인트 설정
- 패키지 구조 정리 (빈 플레이스홀더 정리)

### 5회차: Region/Category/User 도메인 구현
- Region 도메인: 지역/도시 목록 조회 API (RegionController, RegionService, RegionRepository)
- Category 도메인: 카테고리/서브카테고리 목록 조회 API (CategoryController, CategoryService, CategoryRepository)
- User 도메인: 내 프로필 조회/수정/탈퇴 API (UserController, UserService)
- Caffeine Cache 적용 (regions, cities, categories, subCategories)
- Soft Delete 패턴 적용 (User.delete() → deletedAt + status 변경)
- `/me` 엔드포인트 패턴 (본인 리소스 접근)

## 기술 스택 버전 정리

| 구분 | 기존 (2021) | 변경 (2026) |
|------|-------------|-------------|
| 언어 | Java 9 | Java 21 (LTS) |
| 백엔드 | JSP/Servlet | Spring Boot 3.5.9 |
| ORM | JDBC | Spring Data JPA 3.5.x |
| DB | Oracle 11g | PostgreSQL 17 |
| 프론트엔드 | JSP + jQuery 3.x | React 18.3.1 + Next.js 14.2.18 |
| 자바스크립트 | JavaScript (ES5) | TypeScript 5.7.2 |
| 상태관리 | - | Zustand 5.0.2 |
| 데이터 페칭 | Ajax | TanStack Query 5.62.8 + Axios 1.7.9 |
| 스타일링 | CSS | Tailwind CSS 3.4.17 |
| 런타임 | - | Node.js 18.18.0 |
