# Footprint 아키텍처

## 패키지 구조

```
com.footprint
├── FootprintApplication.java
├── global/
│   ├── config/
│   ├── common/
│   ├── security/
│   ├── exception/
│   └── util/
└── domain/
    ├── auth/             # v1: JWT 인증 / v5: OAuth
    ├── user/             # v1: 회원
    ├── post/             # v1: 게시글 + 정렬
    ├── region/           # v1: 지역
    ├── category/         # v1: 카테고리
    ├── image/            # v1: 이미지
    ├── like/             # v1: 좋아요
    ├── comment/          # v2: 댓글 / v3: 대댓글
    ├── bookmark/         # v2: 북마크
    ├── tag/              # v3: 태그
    ├── follow/           # v4: 팔로우
    ├── notification/     # v4: 알림
    ├── report/           # v2: 신고
    ├── search/           # v2: 검색
    └── admin/            # v2~: 관리자
        ├── code/         # 공통코드 관리
        ├── member/       # 사용자 관리
        └── content/      # 게시물 관리
```

## 도메인 내부 구조

각 도메인은 아래 구조를 따릅니다.

```
domain/{도메인명}/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
└── service/
```

## 버전별 로드맵

| 버전 | 도메인 |
|------|--------|
| **v1** | auth, user, post, region, category, image, like |
| **v2** | comment, bookmark, report, search, admin |
| **v3** | tag, 대댓글 |
| **v4** | follow, notification |
| **v5** | OAuth |

## 기술 스택

### Backend
| 구분 | 기술 |
|------|------|
| 언어 | Java 21 |
| 프레임워크 | Spring Boot 3.5 |
| ORM | Spring Data JPA |
| DB | PostgreSQL |
| 인증 | JWT + Spring Security |
| 문서화 | Springdoc OpenAPI (Swagger) |
| 빌드 | Gradle |

### Frontend
| 구분 | 기술 |
|------|------|
| 프레임워크 | Next.js 14 |
| 언어 | TypeScript |
| 스타일 | Tailwind CSS |
| 상태관리 | Zustand |
| 서버상태 | React Query |
