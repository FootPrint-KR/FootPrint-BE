# 인증/권한 정책

## 인증 방식

### JWT 토큰

| 토큰 | 만료 시간 | 용도 | 저장 위치 |
|------|----------|------|----------|
| Access Token | 30분 | API 인증 | 클라이언트 |
| Refresh Token | 14일 | Access Token 재발급 | DB + 클라이언트 |

### 토큰 구조

```json
// Access Token Payload
{
  "sub": "1",
  "email": "user@example.com",
  "role": "USER",
  "iat": 1704067200,
  "exp": 1704070800
}
```

### Refresh Token 관리

- **DB 저장**: 로그인 성공 시 저장
- **로그아웃**: DB에서 Refresh Token 삭제
- **재발급**: 기존 토큰 삭제 후 새 토큰 저장 (Rotation)

---

## API 권한 매트릭스

### Public (인증 불필요)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | /api/auth/signup | 회원가입 |
| POST | /api/auth/login | 로그인 |
| POST | /api/auth/reissue | 토큰 재발급 |
| GET | /api/posts | 게시글 목록 |
| GET | /api/posts/{id} | 게시글 상세 |
| GET | /api/regions | 지역 목록 |
| GET | /api/categories | 카테고리 목록 |
| GET | /swagger-ui/** | Swagger UI |
| GET | /v3/api-docs/** | API 문서 |

### Authenticated (로그인 필요)

| Method | Endpoint | 설명 | 권한 |
|--------|----------|------|------|
| GET | /api/users/me | 내 프로필 | 본인 |
| PATCH | /api/users/me | 프로필 수정 | 본인 |
| DELETE | /api/users/me | 회원 탈퇴 | 본인 |
| POST | /api/posts | 게시글 작성 | 로그인 사용자 |
| PATCH | /api/posts/{id} | 게시글 수정 | 작성자 본인 |
| DELETE | /api/posts/{id} | 게시글 삭제 | 작성자 본인 |
| POST | /api/posts/{id}/images | 이미지 업로드 | 작성자 본인 |
| POST | /api/posts/{id}/like | 좋아요 토글 | 로그인 사용자 |
| POST | /api/posts/{id}/comments | 댓글 작성 | 로그인 사용자 |
| PATCH | /api/comments/{id} | 댓글 수정 | 작성자 본인 |
| DELETE | /api/comments/{id} | 댓글 삭제 | 작성자 본인 |
| POST | /api/posts/{id}/bookmark | 북마크 토글 | 로그인 사용자 |

---

## 권한 체크 로직

### 본인 확인

```java
if (!post.getUser().getId().equals(currentUserId)) {
    throw PostException.unauthorized();
}
```

---

## 에러 응답

### 인증 실패 (401)

```json
{
  "status": 401,
  "message": "로그인이 필요합니다",
  "data": null
}
```

### 권한 부족 (403)

```json
{
  "status": 403,
  "message": "권한이 없습니다",
  "data": null
}
```

---

## 토큰 재발급

### POST /api/auth/reissue

**Request**
```json
{
  "refreshToken": "리프레시 토큰"
}
```

**Response (200)**
```json
{
  "status": 200,
  "message": "Success",
  "data": {
    "accessToken": "새 액세스 토큰",
    "refreshToken": "새 리프레시 토큰"
  }
}
```
