# Footprint ERD

## v1 ERD - 기본 마이그레이션

```
┌─────────────────────────────────────────────────────────────────┐
│                        BaseEntity (공통)                         │
│  - createdAt: LocalDateTime                                      │
│  - updatedAt: LocalDateTime                                      │
└─────────────────────────────────────────────────────────────────┘

┌───────────────────┐     ┌─────────────────┐
│  CommonCodeGroup  │     │   CommonCode    │
├───────────────────┤     ├─────────────────┤
│ id (PK)           │────▶│ id (PK)         │
│ code (UK)         │     │ groupId (FK)    │
│ name              │     │ code            │
│ description       │     │ name            │
│ isActive          │     │ sortOrder       │
│ createdAt         │     │ isActive        │
│ updatedAt         │     │ createdAt       │
└───────────────────┘     │ updatedAt       │
                          └─────────────────┘

┌─────────────────┐           ┌─────────────────┐
│     Region      │           │    Category     │
├─────────────────┤           ├─────────────────┤
│ id (PK)         │           │ id (PK)         │
│ name            │           │ name            │
│ createdAt       │           │ type (공통코드)  │
│ updatedAt       │           │ createdAt       │
└────────┬────────┘           │ updatedAt       │
         │                    └────────┬────────┘
         ▼                             │
┌─────────────────┐                    ▼
│      City       │           ┌─────────────────┐
├─────────────────┤           │   SubCategory   │
│ id (PK)         │           ├─────────────────┤
│ regionId (FK)   │           │ id (PK)         │
│ name            │           │ categoryId (FK) │
│ createdAt       │           │ name            │
│ updatedAt       │           │ createdAt       │
└────────┬────────┘           │ updatedAt       │
         │                    └────────┬────────┘
         │                             │
         │    ┌───────────────────┐    │
         │    │       User        │    │
         │    ├───────────────────┤    │
         │    │ id (PK)           │    │
         │    │ email (UK)        │    │
         │    │ password (해시)    │    │
         │    │ nickname (UK)     │    │
         │    │ name              │    │
         │    │ birth             │    │
         │    │ gender (ENUM)     │    │
         │    │ emailVerified     │    │
         │    │ role (ENUM)       │    │
         │    │ status (공통코드)  │    │
         │    │ createdAt         │    │
         │    │ updatedAt         │    │
         │    │ deletedAt (soft)  │    │
         │    └─────────┬─────────┘    │
         │              │              │
         ▼              ▼              ▼
┌──────────────────────────────────────────┐
│                  Post                    │
├──────────────────────────────────────────┤
│ id (PK)                                  │
│ userId (FK)                              │
│ cityId (FK)                              │
│ subCategoryId (FK)                       │
│ title                                    │
│ content (TEXT)                           │
│ summary                                  │
│ viewCount                                │
│ likeCount (역정규화)                      │
│ status (공통코드)                         │
│ createdAt                                │
│ updatedAt                                │
│ deletedAt (soft delete)                  │
└─────────────────┬────────────────────────┘
                  │
      ┌───────────┼───────────┐
      ▼           ▼           ▼
┌──────────┐ ┌──────────┐ ┌──────────────┐
│PostImage │ │ PostLike │ │  PostReport  │
├──────────┤ ├──────────┤ ├──────────────┤
│ id (PK)  │ │ id (PK)  │ │ id (PK)      │
│ postId   │ │ postId   │ │ postId (FK)  │
│ url      │ │ userId   │ │ userId (FK)  │
│ seq      │ │ createdAt│ │ reason       │
│ createdAt│ └──────────┘ │ status(공통) │
└──────────┘ (복합UK:      │ createdAt    │
              post+user)   └──────────────┘

┌─────────────────┐
│  RecommendCity  │
├─────────────────┤
│ id (PK)         │
│ cityId (FK)     │
│ thumbnailUrl    │
│ imageUrl        │
│ displayOrder    │
│ isActive        │
│ createdAt       │
│ updatedAt       │
└─────────────────┘
```

## 공통코드 예시 데이터

### CommonCodeGroup
| code | name | description |
|------|------|-------------|
| USER_STATUS | 회원 상태 | 회원 활성화 상태 |
| POST_STATUS | 게시글 상태 | 게시글 공개 상태 |
| CATEGORY_TYPE | 카테고리 타입 | 메인 카테고리 구분 |
| REPORT_STATUS | 신고 상태 | 신고 처리 상태 |

### CommonCode
| group | code | name | sortOrder |
|-------|------|------|-----------|
| USER_STATUS | ACTIVE | 활성 | 1 |
| USER_STATUS | INACTIVE | 비활성 | 2 |
| USER_STATUS | BANNED | 정지 | 3 |
| POST_STATUS | DRAFT | 임시저장 | 1 |
| POST_STATUS | PUBLISHED | 게시됨 | 2 |
| POST_STATUS | HIDDEN | 숨김 | 3 |
| CATEGORY_TYPE | TRAVEL | 여행가자 | 1 |
| CATEGORY_TYPE | FOOD | 맛집가자 | 2 |
| CATEGORY_TYPE | ACTIVITY | 놀러가자 | 3 |
| REPORT_STATUS | PENDING | 대기 | 1 |
| REPORT_STATUS | RESOLVED | 처리완료 | 2 |
| REPORT_STATUS | REJECTED | 반려 | 3 |

## ENUM (고정값만)

```java
// 성별 - 거의 변경 없음
public enum Gender {
    MALE, FEMALE, OTHER
}

// 사용자 권한 - 거의 변경 없음
public enum UserRole {
    USER, ADMIN
}
```

## v2 추가 테이블 (댓글 + 북마크)

```
┌──────────────┐     ┌──────────────┐
│   Comment    │     │   Bookmark   │
├──────────────┤     ├──────────────┤
│ id (PK)      │     │ id (PK)      │
│ postId (FK)  │     │ postId (FK)  │
│ userId (FK)  │     │ userId (FK)  │
│ content      │     │ createdAt    │
│ createdAt    │     └──────────────┘
│ updatedAt    │
│ deletedAt    │
└──────────────┘
```

## v3 추가 테이블 (태그 + 대댓글)

```
┌──────────────┐     ┌──────────────┐
│     Tag      │     │   PostTag    │
├──────────────┤     ├──────────────┤
│ id (PK)      │     │ id (PK)      │
│ name (UK)    │     │ postId (FK)  │
│ createdAt    │     │ tagId (FK)   │
└──────────────┘     └──────────────┘

Comment 테이블에 parentId (FK, self) 추가 → 대댓글
```

## v4 추가 테이블 (팔로우 + 알림)

```
┌──────────────┐     ┌──────────────────┐
│    Follow    │     │   Notification   │
├──────────────┤     ├──────────────────┤
│ id (PK)      │     │ id (PK)          │
│ followerId   │     │ userId (FK)      │
│ followingId  │     │ type (공통코드)   │
│ createdAt    │     │ content          │
└──────────────┘     │ isRead           │
                     │ createdAt        │
                     └──────────────────┘
```

## 기존 → 신규 매핑

| 기존 테이블 | 신규 테이블 |
|-------------|-------------|
| users | User |
| board | Post |
| area | Region |
| city | City |
| main_option | Category |
| detail_option | SubCategory |
| image | PostImage |
| (recommend_cnt) | PostLike |
| (report_cnt) | PostReport |
| recommend_city | RecommendCity |
| (신규) | CommonCodeGroup |
| (신규) | CommonCode |
