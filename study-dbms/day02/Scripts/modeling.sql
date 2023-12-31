/*
   TBL_USER
   -----------------------
   ID : NUMBER PRIMARY KEY
   -----------------------
   USER_ID : VARCHAR2(1000)
   USER_PW : VARCHAR2(1000)
   USER_ADDRESS : VARCHAR2(2000)
   USER_EMAIL : VARCHAR2(2000) : UNIQUE
   USER_BIRTH : DATE
*/

CREATE TABLE TBL_USER(
   ID NUMBER CONSTRAINT PK_USER PRIMARY KEY,
   USER_ID VARCHAR2(1000) UNIQUE NOT NULL,
   USER_PW VARCHAR2(1000) NOT NULL,
   USER_ADDRESS VARCHAR2(1000),
   USER_EMAIL VARCHAR2(1000) UNIQUE NOT NULL,
   USER_BIRTH DATE
);

CREATE TABLE TBL_PRODUCT(
   ID NUMBER CONSTRAINT PK_PRODUCT PRIMARY KEY,
   PRODUCT_NAME VARCHAR2(1000) NOT NULL,
   PRODUCT_PRICE NUMBER DEFAULT 0,
   PRODUCT_STOCK NUMBER DEFAULT 0
);

CREATE TABLE TBL_ORDER(
   ID NUMBER CONSTRAINT PK_ORDER PRIMARY KEY,
   ORDER_DATE DATE DEFAULT CURRENT_TIMESTAMP,
   USER_ID NUMBER,
   PRODUCT_ID NUMBER,
   CONSTRAINT FK_ORDER_USER FOREIGN KEY(USER_ID)
   REFERENCES TBL_USER(ID),
   CONSTRAINT FK_ORDER_PRODUCT FOREIGN KEY(PRODUCT_ID)
   REFERENCES TBL_PRODUCT(ID)
);


/*
 * 요구 사항
 * 
 * 커뮤니티 게시판을 만들고 싶어요.
 * 게시판에는 게시글 제목과 게시글 내용, 작성한 시간, 작성자가 있고,
 * 게시글에는 댓글이 있어서 댓글 내용들이 나와야 해요
 * 작성자는 회원(TBL_USER)정보를 그대로 사용해요.
 * 댓글에도 작성자가 필요해요.
 * 
 * 
 * */

CREATE TABLE TBL_POST (
 	ID NUMBER CONSTRAINT PK_POST PRIMARY KEY,
    POST_TITLE VARCHAR2(1000) NOT NULL,
    POST_CONTENT VARCHAR2(1000) NOT NULL,
    POST_CREATED_DATE DATE DEFAULT CURRENT_TIMESTAMP,
    USER_ID NUMBER,
    CONSTRAINT FK_POST_USER FOREIGN KEY (USER_ID)
    REFERENCES TBL_USER(ID)
);

CREATE TABLE TBL_COMMENT_TABLE (
	ID NUMBER CONSTRAINT PK_COMMENT PRIMARY KEY,
	CONTENT VARCHAR2(1000),
	USER_ID NUMBER,
	POST_ID NUMBER,
	CONSTRAINT FK_COMMENT_USER FOREIGN KEY (USER_ID)
	REFERENCES TBL_USER(ID),
	CONSTRAINT FK_COMMENT_POST FOREIGN KEY (POST_ID)
	REFERENCES TBL_POST(ID)
);


/*
 * 요구사항
 * 
 * 마이 페이지에서 회원 프로필을 구현.
 * 회원당 프로필을 여러개 설정 가능
 * 대표 이미지로 선택된 프로필만 화면에 출력
 * 
 * */

CREATE TABLE TBL_MYPAGE_PROFIL (
 	ID NUMBER CONSTRAINT PK_PROFIL PRIMARY KEY,
 	PROFIL_ORIGINAL_NAME VARCHAR2(1000),
 	PROFIL_SYSTEM_NAME VARCHAR2(1000),
 	PROFIL_PRESENTATION VARCHAR(100) DEFAULT 'NONE',
 	USER_ID NUMBER,
 	CONSTRAINT FK_MYPAGE_USER FOREIGN KEY (USER_ID)
 	REFERENCES TBL_USER
);


/*
   1.학사 관리 시스템에서 학생과 교수가 존재
   2.학생은 학번, 이름, 전공, 학년 등
   3.교수는 교수 번호, 이름, 전공, 직위 등
   4.과목은 고유한 과목 번호, 과목명, 학점 등
   6.학생은 여러 개의 과목을 수강가능
   7.교수는 여러 개의 과목을 강의가능
   8.학생이 수강한 과목은 성적이 기록됩니다. 
*/

CREATE TABLE TBL_STUDENT (
	ID NUMBER CONSTRAINT PK_STUDENT_ID PRIMARY KEY,
	STUDENT_NAME VARCHAR2(30) NOT NULL,
	STUDENT_COURSE VARCHAR2(255) NOT NULL,
	STUDENT_GRADE NUMBER NOT NULL
);

-- 교수 테이블 생성
CREATE TABLE TBL_PROFESSOR (
    ID NUMBER CONSTRAINT PK_PROFESSOR_ID PRIMARY KEY,
    PROFESSOR_NAME VARCHAR(255) NOT NULL,
    PROFESSOR_COURSE VARCHAR(255) NOT NULL,
    PROFESSOR_POSITION VARCHAR(255) NOT NULL
);

-- 과목 테이블 생성
CREATE TABLE TBL_COURSE (
	ID NUMBER CONSTRAINT PK_COURSE_ID PRIMARY KEY,
	COURSE_NAME VARCHAR2(50) NOT NULL,
	COURSE_CREDIT NUMBER NOT NULL,
	COURSE_PROFESSOR_ID NUMBER,
	CONSTRAINT RK_COURSE_PROFESSOR FOREIGN KEY (COURSE_PROFESSOR_ID)
	REFERENCES TBL_PROFESSOR(ID)
);

-- 학생-과목 연결 테이블 생성
CREATE TABLE TBL_STUDENT_COURSE_APPLY (
	ID NUMBER CONSTRAINT PK_APPLIED_COURSE_ID PRIMARY KEY,
	COURSE_ID NUMBER NOT NULL,
	STUDENT_ID NUMBER NOT NULL,
	CONSTRAINT RK_APPLY_COURSE FOREIGN KEY (COURSE_ID)
	REFERENCES TBL_COURSE(ID),
	CONSTRAINT RK_APPLY_STUDENT FOREIGN KEY (STUDENT_ID)
	REFERENCES TBL_STUDENT(ID)
);

-- 교수-과목 연결 테이블 생성
CREATE TABLE TBL_PROFESSOR_COURSE_APPLY(
	
);
	

/*
 * 요구사항
 * 
 * 대/소 카테고리
 * 
 * */

CREATE TABLE TBL_MAJOR_CATE (
	ID NUMBER CONSTRAINT PK_ID PRIMARY KEY,
	NAME VARCHAR2(255)
);

CREATE TABLE TBL_MINOR_CATE (
	ID NUMBER CONSTRAINT PK_MINOR_ID PRIMARY KEY,
	MAJOR_ID NUMBER,
	NAME VARCHAR2(255),
	CONSTRAINT FK_MAJOR_ID FOREIGN KEY (MAJOR_ID) REFERENCES TBL_MAJOR_CATE(ID)
);

/*
 * 요구 사항
 * 
 * 회의실 예약 서비스를 제작하고 싶습니다.
 * 회원별로 등급이 존재하고 사무실마다 회의실이 여러 개 있습니다.
 * 회의실 이용 가능 시간은 파트 타임으로서 여러 시간대가 존재합니다.
 * */

CREATE TABLE USER_DATA (
	USER_ID NUMBER CONSTRAINT PK_USER_ID PRIMARY KEY,
	USER_NAME VARCHAR2(255),
	USER_GRADE CHAR(1) DEFAULT 'D'
);

CREATE TABLE MEETING_ROOM (
	MEETING_ROOM_ID NUMBER CONSTRAINT PK_MRID PRIMARY KEY,
	OFFICE_ROOM_NUMBER NUMBER NOT NULL,
	AVAILABLE_CAPACITY NUMBER NOT NULL,
	AVAILABLE_TIME DATE
);

CREATE TABLE RESERVATION_INFO (
	USER_ID NUMBER,
	MEETING_ROOM_ID NUMBER,
	CONSTRAINT FK_USER_ID FOREIGN KEY (USER_ID)
	REFERENCES USER_DATA(USER_ID),
	CONSTRAINT FK_MR_ID FOREIGN KEY (MEETING_ROOM_ID)
	REFERENCES MEETING_ROOM(MEETING_ROOM_ID)
);


/*
 * 요구사항
 * 
 * 광고회사
 * 
 * 기업 정보 : 이름, 주소, 대표번호, 기업종류(스타트업, 중소기업, 중견기업, 대기업)
 * 광고 : 제목, 내용, 광고 기한 있고 여러개의 광고를 신청 가능
 * 광고 선택방식은 대, 중, 소 카테고리로 분류하여 광고 선택.
 * */

CREATE TABLE COMPANY (
	ID NUMBER CONSTRAINT PK_COMPANY_ID PRIMARY KEY,
	COMPANY_NAME VARCHAR2(255) NOT NULL,
	COMPANY_ADRESS VARCHAR2(255) NOT NULL,
	MAIN_PHONENUMBER VARCHAR2(255) NOT NULL
);

CREATE TABLE AD_CATEGORY_A (
	ID NUMBER CONSTRAINT PK_CATE_A_ID PRIMARY KEY,
	A_NAME VARCHAR2(255),
);

CREATE TABLE AD_CATEGORY_B (
	ID NUMBER CONSTRAINT PK_CATE_B_ID PRIMARY KEY,
	CATE_A_ID NUMBER,
	B_NAME VARCHAR2(255),
	CONSTRAINT FK_B_A FOREIGN KEY (CATE_A_ID)
	REFERENCES AD_CATEGORY_A(ID)
);

CREATE TABLE AD_CATEGORY_C (
	ID NUMBER CONSTRAINT PK_CATE_C_ID PRIMARY KEY,
	CATE_B_ID NUMBER,
	C_NAME VARCHAR2(255),
	CONSTRAINT FK_C_B FOREIGN KEY (CATE_B_ID)
	REFERENCES AD_CATEGORY_B(ID)
);

CREATE TABLE AD (
	ID NUMBER CONSTRAINT PK_AD_ID PRIMARY KEY,
	TITLE VARCHAR2(255),
	CONTENT VARCHAR2(1000),
	AD_CATEGORY_C_ID NUMBER,
	START_DATE DATE,
	END_DATE DATE,
	CONSTRAINT
);

CREATE TABLE TBL_AD_APPLY (
	ID NUMBER CONSTRAINT PK_APPLY_ID PRIMARY KEY,
	COMPANY_ID NUMBER,
	AD_ID NUMBER
		CONSTRAINT FK_APPLY_COMPANY FOREIGN KEY (COMPANY_ID)
	REFERENCES COMPANY_TYPE(ID)
);

/*
 * 요구사항
 * 
 * 음료수마다 당첨번호 1개 포함,
 * 당첨자의 정보를 알아야 상품을 배송할 수 있음
 * 당첨번호마다 당첨 상품이 있고, 당첨 상품이 배송중인지 배송완료상태인지 구분할 수 있어야 함
 * */

CREATE TABLE TBL_DRINK (
	ID NUMBER CONSTRAINT PK_DRINK PRIMARY KEY,
	DRINK_NAME VARCHAR2(255) NOT NULL,
	DRINK_PRICE NUMBER DEFAULT 0
);

CREATE TABLE TBL_PRODUCT (
	ID NUMBER CONSTRAINT PK_PRODUCT PRIMARY KEY,
	PRODUCT_NAME VARCHAR2(255) NOT NULL
);

CREATE TABLE TBL_LOTTERY (
	ID NUMBER CONSTRAINT PK_LOTTERY PRIMARY KEY,
	PRODUCT_ID NUMBER
	CONSTRAINT FK_LOTTERY_PRODUCT FOREIGN KEY (PRODUCT_ID)
	REFERENCES TBL_PRODUCT(ID)
);

CREATE TABLE TBL_CIRCULATION (
	ID NUMBER CONSTRAINT PK_DRINK PRIMARY KEY,
	DRINK_ID NUMBER,
	LOTTERY_ID NUMBER,
	CONSTRAINT FK_CIRCULATION_DRINK FOREIGN KEY (DRINK_ID)
	REFERENCES TBL_DRINKID),
	CONSTRAINT FK_CIRCULATION_LOTTERY FOREIGN KEY (LOTTERY_ID)
	REFERENCES TBL_LOTTERY(ID)
);



/*이커머스 창업 준비중입니다. 기업과 사용자 간 거래를 위해 기업의 정보와 사용자 정보가 필요합니다.
기업의 정보는 기업 이름, 주소, 대표번호가 있고
사용자 정보는 이름, 주소, 전화번호가 있습니다. 결제 시 사용자 정보와 기업의 정보, 결제한 카드의 정보 모두 필요하며,
상품의 정보도 필요합니다. 상품의 정보는 이름, 가격, 재고입니다.
사용자는 등록한 카드의 정보를 저장할 수 있으며, 카드의 정보는 카드번호, 카드사, 회원 정보가 필요합니다.*/

CREATE TABLE TBL_COMPANY_INFO (
	ID NUMBER CONSTRAINT PK_COMPANY_ID PRIMARY KEY,
	COMPANY_NAME VARCHAR2(255) NOT NULL,
	COMPANY_ADRESS VARCHAR2(255) NOT NULL,
	COMPANY_MAIN_NUMBER VARCHAR2(50)
);

CREATE TABLE TBL_USER_INFO (
	ID NUMBER CONSTRAINT PK_USER_ID PRIMARY KEY,
	USER_NAME VARCHAR2(255) NOT NULL,
	USER_ADRESS VARCHAR2(255) NOT NULL,
	USER_NUMBER VARCHAR2(50)
);

CREATE TABLE TBL_CARD_INFO (
	ID NUMBER CONSTRAINT PK_CARD_ID PRIMARY KEY,
	USER_ID NUMBER NOT NULL,
	CARD_NUMBER VARCHAR2(20) NOT NULL,
	CARD_COMPANY VARCHAR2(255) NOT NULL,
	CARD_GIVEN_NAME VARCHAR2(255),
	CONSTRAINT FK_CARD_USER FOREIGN KEY (CARD_ID)
	REFERENCES TBL_USER_INFO(ID)
);

CREATE TABLE TBL_PRODUCT_INFO (
	ID NUMBER CONSTRAINT PK_PRODUCT_ID PRIMARY KEY,
	COMPANY_ID NUMBER NOT NULL,
	PRODUCT_NAME VARCHAR2(255) NOT NULL,
	PRODUCT_PRICE NUMBER NOT NULL,
	PRODUCT_STOCK NUMBER NOT NULL,
	CONSTRAINT FK_PRODUCT_COMPANY FOREIGN KEY (COMPANY_ID)
	REFERENCES TBL_COMPANY_INFO(ID)
);

CREATE TABLE TBL_ORDER (
	ID NUMBER CONSTRAINT PK_ORDER PRIMARY KEY,
	USER_ID NUMBER,
	ORDER_DATE DATE DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT FK_ORDER_USER FOREIGN KEY(USER_ID)
	REFERENCES TBL_USER_INFO(ID)
);

CREATE TABLE TBL_ORDER_DETAIL (
	ID NUMBER CONSTRAINT PK_ORDER_DETAIL PRIMARY KEY,
	ORDER_ID NUMBER,
	PRODUCT_COUNT NUMBER,
	PRODUCT_ID NUMBER,
	CONSTRAINT FK_ORDER_DETAIL_ORDER FOREIGN KEY(ORDER_ID)
	REFERENCES TBL_ORDER(ID),
	CONSTRAINT FK_ORDER_DETAIL_PRODUCT FOREIGN KEY(PRODUCT_ID)
	REFERENCES TBL_PRODUCT_INFO(ID)
);

CREATE TABLE TBL_PAYMENT (
	ID NUMBER CONSTRAINT PK_PAYMENT PRIMARY KEY,
	USER_ID NUMBER,
	ORDER_ID NUMBER,
	CARD_ID NUMBER,
	CONSTRAINT FK_PAYMENT_USER FOREIGN KEY(USER_ID)
	REFERENCES TBL_USER_INFO(ID),
	CONSTRAINT FK_PAYMENT_ORDER FOREIGN KEY(ORDER_ID)
	REFERENCES TBL_ORDER(ID),
	CONSTRAINT FK_PAYMENT_CARD FOREIGN KEY(CARD_ID)
	REFERENCES TBL_CARD_INFO(ID)
);


