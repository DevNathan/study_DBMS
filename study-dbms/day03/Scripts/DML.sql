CREATE SEQUENCE SEQ_PARENT;

CREATE TABLE TBL_PARENT(
	ID NUMBER CONSTRAINT PK_PARENT PRIMARY KEY,
	PARENT_NAME VARCHAR2(1000) NOT NULL,
	PARENT_AGE NUMBER,
	PARENT_ADDRESS VARCHAR2(1000) NOT NULL,
	PARENT_PHONE VARCHAR2(1000) NOT NULL UNIQUE,
	PARENT_GENDER VARCHAR2(1000) NOT NULL
);

INSERT INTO TBL_PARENT
VALUES(SEQ_PARENT.NEXTVAL, '한동석', 40, '경기도 남양주', '01012341234', '남자');

SELECT * FROM TBL_PARENT;
SELECT
	ID,
	PARENT_NAME,
	PARENT_AGE,
	PARENT_ADDRESS,
	PARENT_PHONE,
	PARENT_GENDER
FROM
	TBL_PARENT;


UPDATE
	TBL_PARENT
SET
	PARENT_NAME = '조남호'
WHERE
	ID = 1;
	
UPDATE
	TBL_PARENT
SET
	PARENT_ADDRESS = '대전광역시 서구'
WHERE
	ID = 1;
	
UPDATE
	TBL_PARENT
SET
	PARENT_PHONE = '01036316448'
WHERE
	ID = 1;
	
DELETE
FROM
	TBL_PARENT
WHERE
	ID = 2;