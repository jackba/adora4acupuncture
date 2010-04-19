update ACUPOINT set CHINESE_NAME = replace(CHINESE_NAME, '髎', '豂') where CHINESE_NAME like '%髎%';
update ACUPOINT set ALIAS = replace(ALIAS, '髎', '豂') where ALIAS like '%髎%';

------------------------------------------------------------------------------
alert table ACUPOINT add column DESCRIPTION_ZH  text;
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
[定位]  胸前壁外上方，前正中线旁开6寸，平第一肋间隙处。
[解剖]  当胸大肌、胸小肌处，内侧深层为第一肋间内、外肌；上外侧有腋动、静脉，胸肩峰动、静脉；布有锁骨上神经中间支，胸前神经分支及第一肋间神经外侧皮支。
[主治]  咳嗽，气喘，肺胀满，胸痛，肩背痛。
[操作]  向外斜刺或平刺0.5~0.8寸，不可向内深刺，以免伤及肺脏。
[配伍]  配尺泽治咳嗽；配肩髎治肩痛。
[附注]  肺的募穴，手、足太阴经交会穴。' where CODE = 'LU1';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU2';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU3';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU4';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU5';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU6';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU7';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU8';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU9';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU10';
update ACUPOINT set DESCRIPTION_ZH = '<<针灸学>>@TCM100.COM: 
' where CODE = 'LU11';
