# EnvProvider

Spring Boot 기반의 파일 정보 제공 및 다운로드 서비스입니다.

## 프로젝트 구조

```
src/main/java/com/craftconnect/envprovider/
├── controller/
│   ├── FileController.java
│   └── GlobalExceptionHandler.java
├── dto/
│   └── FileInfoDTO.java
├── service/
│   └── FileProviderService.java
└── EnvproviderApplication.java
```

## 파일 시스템 구조

### 디렉토리 구조
```
Dev/
└── app_files/
    └── {애플리케이션명}/
        └── {YYYY_MM_DD_HHmmSS}/
            ├── info.json
            ├── abc.yml
            ├── abcd.yml
            └── ...
```

### info.json 파일 구조
```json
{
  "files": [
    {
      "filename": "abc.yml",
      "targetFileLocation": "src/main/resources"
    },
    {
      "filename": "abcd.yml",
      "targetFileLocation": "src/main/resources"
    }
  ]
}
```

## API 문서

### 1. 파일 정보 조회
**GET** `/file/info/{appName}`

애플리케이션의 최신 파일 정보를 조회합니다.

#### 요청
- `appName`: 애플리케이션 이름 (예: "myApp")

#### 응답
```json
[
  {
    "fileName": "2024_06_01_153000/abc.yml",
    "targetFileLocation": "src/main/resources"
  },
  {
    "fileName": "2024_06_01_153000/abcd.yml",
    "targetFileLocation": "src/main/resources"
  }
]
```

#### 예시
```bash
curl http://localhost:8082/file/info/myApp
```

### 2. 파일 다운로드
**GET** `/file/download/{appName}/{latestDir}/{filename}`

특정 파일을 다운로드합니다.

#### 요청
- `appName`: 애플리케이션 이름 (예: "myApp")
- `latestDir`: 최신 디렉토리명 (예: "2024_06_01_153000")
- `filename`: 파일명 (예: "abc.yml")

#### 응답
- 파일 바이너리 데이터
- Content-Disposition 헤더로 다운로드 파일명 지정

#### 예시
```bash
curl -O http://localhost:8082/file/download/myApp/2024_06_01_153000/abc.yml
```

## 서버 설정

### 포트
- 기본 포트: 8082
- 설정 파일: `src/main/resources/application.yml`

```yaml
server:
  port: 8082
```

## 실행 방법

### 1. IDE에서 실행
- `EnvproviderApplication.java`의 main 메서드 실행

### 2. 커맨드라인에서 실행
```bash
# Maven
./mvnw spring-boot:run

# Gradle
./gradlew bootRun
```

### 3. JAR 파일로 실행
```bash
# 빌드
./mvnw clean package

# 실행
java -jar target/envprovider-0.0.1-SNAPSHOT.jar
```

## 예외 처리

### HTTP 상태 코드
- **200 OK**: 정상 응답
- **400 Bad Request**: 잘못된 파일 경로 (MalformedURLException)
- **500 Internal Server Error**: 기타 서버 오류

### 예외 응답 예시
```json
"잘못된 파일 경로입니다."
```

## 주의사항

1. **파일 경로**: Windows 환경에서도 경로 구분자는 `/`를 사용합니다.
2. **디렉토리명**: 날짜/시간 형식은 `YYYY_MM_DD_HHmmSS`를 정확히 지켜야 합니다.
3. **info.json**: JSON 형식이 올바르게 작성되어야 합니다.
4. **파일 권한**: 애플리케이션이 `Dev/app_files` 디렉토리에 접근 권한이 있어야 합니다.

## 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 