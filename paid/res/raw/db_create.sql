create table MERIDIAN_CHANNEL
(
	PK_ID			integer 	primary key		autoincrement,
	NAME			text		unique			not null,
    CHINESE_NAME    text        unique          not null,
	CODE	        text		unique			not null,
    PICTURE         text,
	DESCRIPTION		text
);

create table ACUPOINT
(
	PK_ID			integer    primary key		autoincrement,
    CODE            text       unique           not null,
    ORDER_NUM       int                         not null,
	NAME			text		     			not null,
    CHINESE_NAME    text                        not null,
	ALIAS		    text,
    PICTURE         text,
    DESCRIPTION     text,
    DESCRIPTION_ZH  text,
	CHANNEL_ID      integer
	                references MERIDIAN_CHANNEL(PK_ID) on delete restrict
);

------------------------------------------------------------------------------
