### 🔔 개발 환경
- Spring Framework 5.3.34
- Java 11
- Apache Tomcat 9.0.84
- IDE : Eclipse (for Enterprise Java and Web Developers)
- SQL Mapper : SpringJdbcTemplate
- DB : MySQL
- template: JSP

## Section1. 회원가입과 이메일 인증
### 1. 이메일 인증
- JavaMailSender 라이브러리 활용
### 2. 비밀번호 암호화
- [BCrypt를 통해 암호화한 비밀번호를 DB에 저장하기](https://velog.io/@melodie104/BCrypt-비밀번호-암호화)
### 3. 민감한 정보 암호화하기
- [Jasypt 라이브러리를 활용하기](https://velog.io/@melodie104/Jasypt를-이용한-설정-정보-암호화)
### 4. Exception 처리 
### 5. JdbcTemplate 주요 메서드
## Section2. 로그인과 세션
### 1. Statement vs. PreparedStatement
### 2. 암호화된 비밀번호 비교하기
- [로그인 인증 : 사용자 입력 비밀번호와 DB에 저장된 암호문을 비교](https://velog.io/@melodie104/BCrypt-비밀번호-암호화)  
  -> 직접 복호화하지 않고 BCrypt 라이브러리 비교메서드를 이용해서 확인
### 3. 로그인 검증 필터 처리
- [필터에 대한 이해](https://velog.io/@melodie104/Filter)
- 로그인 후 원래 접근하려던 페이지로 이동하기
  - request.getRequsetURL과 <input type="hidden" ..>를 활용
### 4. Exception 처리
### 5. Enum을 활용한 에러별 코드
## Section3. 게시판
### 1. 페이징 처리
  - 페이징에 필요한 계산의 이해
  - 페이징에 필요한 데이터와 메서드를 하나의 모듈로 만들기 (ex. PageHandler)
### 2. 좋아요/싫어요
### 3. 게시판 CRUD
### Exception 처리
