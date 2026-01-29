# 코딩 패턴 가이드

## 정적 팩토리 메서드 패턴

### 1. Entity → DTO 변환: `from()`

```java
public record PostResponse(
    Long id,
    String title,
    String summary,
    int viewCount
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getSummary(),
            post.getViewCount()
        );
    }
}
```

### 2. DTO → Entity 변환: `toEntity()`

```java
@Getter
public class CreatePostRequest {

    private String title;
    private String content;

    public Post toEntity(User user, City city, SubCategory subCategory) {
        return Post.builder()
                .user(user)
                .city(city)
                .subCategory(subCategory)
                .title(title)
                .content(content)
                .build();
    }
}
```

### 3. 다중 Entity 변환

```java
public record PostDetailResponse(
    Long id,
    String title,
    String content,
    AuthorSummary author,
    String cityName
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            AuthorSummary.from(post.getUser()),
            post.getCity().getName()
        );
    }

    public record AuthorSummary(Long id, String nickname) {
        public static AuthorSummary from(User user) {
            return new AuthorSummary(user.getId(), user.getNickname());
        }
    }
}
```

### 4. 공통 응답: `success()`, `error()`

```java
return ApiResponse.success(PostResponse.from(post));
return ApiResponse.error(PostErrorCode.POST_NOT_FOUND);
```

---

## 도메인 예외 패턴

### 정적 팩토리 메서드로 예외 생성

```java
public class PostException extends BusinessException {

    private PostException(ErrorCode errorCode) {
        super(errorCode);
    }

    public static PostException notFound() {
        return new PostException(PostErrorCode.POST_NOT_FOUND);
    }

    public static PostException unauthorized() {
        return new PostException(PostErrorCode.UNAUTHORIZED);
    }
}
```

### 사용 예시

```java
// 권장
throw PostException.notFound();

// 지양
throw new PostException(PostErrorCode.POST_NOT_FOUND);
```

---

## Entity Builder 패턴

### 기본 구조

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public Post(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }
}
```

---

## 패턴 요약

| 용도 | 메서드명 | 위치 | 예시 |
|------|---------|------|------|
| Entity → DTO | `from()` | Response DTO | `PostResponse.from(post)` |
| DTO → Entity | `toEntity()` | Request DTO | `request.toEntity(user, city)` |
| 성공 응답 | `success()` | ApiResponse | `ApiResponse.success(data)` |
| 에러 응답 | `error()` | ApiResponse | `ApiResponse.error(errorCode)` |
| 도메인 예외 | `notFound()` 등 | 도메인 Exception | `PostException.notFound()` |
