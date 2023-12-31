CREATE TABLE TBL_MEMBER(
	MEMBER_NAME VARCHAR2(255),
	MEMBER_AGE NUMBER
);

DROP TABLE TBL_MEMBER;

CREATE TABLE CAR_TABLE(
	ID_NUMBER NUMBER CONSTRAINT PK_CAR PRIMARY KEY,
	CAR_BRAND VARCHAR2(255),
	RELEASE_DATE DATE,
	CAR_COLOR VARCHAR2(255),
	CAR_VALUE NUMBER
);

DROP TABLE CAR_TABLE;

ALTER TABLE CAR_TABLE DROP CONSTRAINT PK_CAR;
ALTER TABLE CAR_TABLE CREATE CONSTRAINT PK_CAR;

CREATE TABLE TBL_ANIMAL(
	ANIMAL_ID NUMBER CONSTRAINT PK_ANIMAL PRIMARY KEY,
	ANIMAL_KIND VARCHAR(255),
	ANIMAL_AGE NUMBER,
	ANIMAL_FEED VARCHAR(255)
);

ALTER TABLE TBL_ANIMAL CREATE CONSTRAINT PK_ID PRIMARY KEY(ID);
ALTER TABLE TBL_ANIMAL DROP CONSTRAINT PK_ID;

ALTER TABLE TBL_ANIMAL ADD (ANIMAL_SEX CHAR(1));
ALTER TABLE TBL_ANIMAL RENAME COLUMN ANIMAL_FEED TO ANIMAL_FEED_NAME;

ALTER TABLE TBL_ANIMAL DROP COLUMN ANIMAL_SEX;
ALTER TABLE TBL_ANIMAL MODIFY ANIMAL_SEX CHAR(6);

DROP TABLE TBL_ANIMAL;


CREATE TABLE TBL_STUDENT (
	ID NUMBER CONSTRAINT PK_STUDENT PRIMARY KEY,
	STUDENT_ID VARCHAR2(255) UNIQUE,
	STUDENT_NAME VARCHAR2(255) NOT NULL,
	STUDENT_MAJOR VARCHAR2(255) NOT NULL,
	STUDENT_GENDER CHAR(1) DEFAULT 'N' CONSTRAINT BAN_GENDER CHECK(STUDENT_GENDER IN ('M', 'F')),
	STUDENT_BIRTH DATE CONSTRAINT BAN_DATE CHECK(STUDENT_BIRTH >= TO_DATE('1980-01-01', 'YYYY-MM-DD'))
);
