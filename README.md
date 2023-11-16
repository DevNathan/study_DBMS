조남호 | Nathan-Cho<br>
07.05.23 ~ 07.17.23

***
# DBMS - RDBMS

### 목차
1. [DBMS 개론](#1-DBMS-개론)

***
## 1. DBMS 개론
### DB(Database)
    데이터가 모여 있는 기지,
    데이터만을 다루고 저장하는 곳이다.

### DBMS(Database Management System)
    DB를 관리할 수 있는 구체적인 시스템
    대표적으로 Oracle, MySQL, MS-SQL, PostgreSQL 등이 있으며
    RDBMS와 NoSQL로 더욱 상세히 나눌 수 있다(NoSQL의 경우는 여기서 다루지 않음).

### DBMS의 소통 방식
![image](https://github.com/DevNathan/study_DBMS/assets/142222091/43d8e6bb-c965-425d-9839-4386a83b5506)

### RDBMS(Related Databse Management System)
관계형 데이트베이스 시스템이라 불리며 테이블끼리 서로 관계를 맺는것이 특징이다.<br>
해당 글은
<br><br>
예를 들어,<br>
TABLE A
번호(PK) | 이름 | 나이 | 아이디(UQ)
--- | --- | --- | ---
1 | John Doe | 20 | 000123
<br>

TABLE B
주문번호(PK) | 번호(FK) | 날짜 | 상품수량
--- | --- | --- | ---
20000000 | 1 | 0101 | 5

위의 두 테이블이 "번호"를 기준으로 관계될 수 있으며 이것이 관계형 데이터베이스의 특징이다라고 말할 수 있다.
<br><br>
- Primary Key(PK)

      고유한 값.
      각 행의 구분점으로 사용된다.
      중복이 없고 NULL값을 허용하지 않는다.
- Foreign Key(FK)

		다른 테이블의 PK를 의미한다.
		보통 테이블끼리 관계를 맺을 때 사용한다.
		중복이 가능하고 NULL도 허용한다.
- Unique Key

      NULL은 허용하지만 중복은 허용하지 않는다.

### SQL(Structured Query Language)
	데이터베이스와 소통하는 언어이며 스크립트 언어로 작동한다.
#### 컴파일 언어와 인터프리터(스크립트) 언어의 차이점
- 컴파일
	- 파일 단위로 해석한다(일괄처리).
 	- 수정이 거의 없을 때 효율적이다.
- 인터프리트
	- 한 줄 단위로 해석한다(개별처리).
 	-  빈번한 수정 시 또는 부분 실행 시 효율적이다.
 
#### SQL문(Query 문) - DDL, DML, DCL, TCL
1. DDL(Data Definition Language) : 데이터 정의어
	- 테이블 조작, 제어 관련 쿼리 문

			1) CREATTE : 테이블 생성
			2) DROP : 테이블 삭제
			3) ALTER : 테이블 변경
			4) TRUNCATE : 테이블 안에 있는 내용 전체 삭제

2. 자료형(Type) : 용량은 부족할 일 없이 충분히 확보한다.
	1) 숫자

			NUMBER(precision): 정수
			NUMBER(precision, 소수점 자리수): 실수
	2) 문자열
  
			CHAR(n) : 고정형
			CHAR(4)에 'A'를 넣으면 A^^^ 빈 자리가 공백으로 채워진다.
			형식을 정한 날짜, 주민등록번호, 상태값 등 글자 수가 절대 변하지 않는 값을 넣는다.
			VARCHAR(precision), VARCHAR2(precision) : 가변형
			값의 길이만큼 공간이 배정된다. 글자 수에 변화가 있는 값을 넣는다.
			(VARCHAR2만 사용할 것)
	3) 날짜

  			DATE : FORMAT에 맞춰서 날짜를 저장하는 타입

### 무결성
	데이터의 정확성, 일관성, 유효성이 유지되는 것

  	정확성: 데이터는 애매하지 않아야 한다.
	일관성: 각 사용자가 일관된 데이터를 볼 수 있도록 해야한다.
	유효성: 데이터가 실제 존재하는 데이터여야 한다.

1. 개체 무결성

		모든 테이블이 PK로 선택된 컬럼을 가져야 한다.
2. 참조 무결성

		두 테이블의 데이터가 항상 일관된 값을 가지도록 유지하는 것.
3. 도메인(컬럼) 무결성

		컬럼의 타입, NULL값의 허용 등에 대한 사항을 정의하고
		올바른 데이터가 입력되었는지를 확인하는 것.
