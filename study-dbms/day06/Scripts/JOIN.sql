CREATE TABLE TBL_MEMBER(
 	ID NUMBER CONSTRAINT PK_MEMBER PRIMARY KEY,
 	MEMBER_NAME VARCHAR2(1000) NOT NULL
);

CREATE TABLE TBL_POST(
	ID NUMBER CONSTRAINT PK_POST PRIMARY KEY,
	POST_TITLE VARCHAR2(1000) NOT NULL,
	POST_CONTENT VARCHAR2(1000) NOT NULL,
	MEMBER_ID NUMBER,
	CONSTRAINT RK_POST_MEMBER FOREIGN KEY (MEMBER_ID)
	REFERENCES TBL_MEMBER(ID)
);
