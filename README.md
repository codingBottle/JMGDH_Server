# <img src="https://github.com/codingBottle/JMGDH_Server/assets/85906821/9d0d5123-81e6-4701-b966-72edaf2507b3" alt="포잇캘린더 로고" width="50" height="50"></img> 포잇캘린더 

포잇캘린더는 구글 연동을 통해 개인 캘린더를 만들고 일정을 관리할 수 있는 웹 어플리케이션입니다. <br>
사용자는 간편한 구글 로그인을 통해 개인 캘린더를 생성하고, 다양한 일정을 추가, 수정, 삭제할 수 있습니다.

<br><br>
## 주요 기능

### 캘린더
- **구글 캘린더 연동**
- **특정 날짜에 일정 추가/수정/삭제**
- **연-월-일 일정 조회**
- **주간 일정 조회** (특정 두 날짜 사이 일정)
- **연-월 일정 조회**
- 팀 캘린더 생성
  - 팀 가져오기
  - 멤버가 속한 팀 목록 가져오기
  - 팀 업데이트
  - 팀 삭제하기
  - 팀원 스케쥴 조회
  - 팀원 이메일로 검색
  - 그룹 나가기
  - 팀 초대 코드 생성
  - 초대 코드로 팀 가입

### 투두
- **회원가입 시 기본 투두 태그 5개 생성**
- **원하는 태그 안에 투두 일정 생성 / 수정 / 삭제**
- **투두 체크리스트 관리**
- **날짜로 태그와 할일 목록 조회**
- **태그 추가/수정/삭제/조회**

### 친구관리
- **친구요청**
- **요청 수락**
- **요청 거절**
- **친구요청 목록 조회**
- **친구 목록 조회**
- **친구 삭제**

### 로그인/회원가입
- **only 구글 연동**

<br><br>
## 제작 과정

### 사용된 언어 및 도구

- Java
- Spring Boot
- MySQL
- IntelliJ IDEA
- Postman
- Discord (커뮤니케이션 및 협업)
- ...

### 개발 환경 구축

1. [Java](https://www.java.com) 및 [Spring Boot](https://spring.io/projects/spring-boot) 설치
2. [MySQL](https://www.mysql.com) 설치 및 설정
3. [IntelliJ IDEA](https://www.jetbrains.com/idea) 설치
4. [Postman](https://www.postman.com/downloads) 설치
5. ...

<br><br>
## 설치 및 실행

1. 소스 코드를 다운로드하거나 복제합니다.
2. IntelliJ IDEA 또는 원하는 IDE에서 프로젝트를 엽니다.
3. MySQL에 데이터베이스를 생성하고 연결 정보를 설정합니다.
4. `application.properties` 파일에서 데이터베이스 연결 정보를 설정합니다.
5. 애플리케이션을 실행합니다.

```bash
./mvnw spring-boot:run(그냥 예시로 적어둔것 수정해야함)
```

1. 브라우저에서 [For_It Calendar](https://www.jmgdh.duckdns.org/test)에 접속하여 애플리케이션을 사용합니다.

<br><br>
## 기여
프로젝트에 기여하고 싶으시다면, 이슈를 생성하거나 풀 리퀘스트를 보내주세요. 우리의 프로젝트를 함께 발전시켜 나가요!

