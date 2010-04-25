update ACUPOINT set CHINESE_NAME = replace(CHINESE_NAME, '髎', '豂') where CHINESE_NAME like '%髎%';
update ACUPOINT set ALIAS = replace(ALIAS, '髎', '豂') where ALIAS like '%髎%';

update ACUPOINT set CHINESE_NAME = '胃俞' where CHINESE_NAME = '喂俞';

------------------------------------------------------------------------------
alter table ACUPOINT add column DESCRIPTION_ZH  text;
