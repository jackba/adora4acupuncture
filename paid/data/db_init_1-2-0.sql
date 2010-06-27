-- MERIDIAN_CHANNEL table
insert into MERIDIAN_CHANNEL 
			(PK_ID, NAME, CHINESE_NAME, CODE, PICTURE, DESCRIPTION) 
      select '1', 'Lung Meridian', '手太阴肺经', 'LU', 'meridian_lu', null 
union select '2', 'Large Intestine Meridian', '手阳明大肠经', 'LI', 'meridian_li', null 
union select '3', 'Stomach Meridian', '足阳明胃经', 'ST', 'meridian_st', null 
union select '4', 'Spleen Meridian', '足太阴脾经', 'SP', 'meridian_sp', null 
union select '5', 'Heart Meridian', '手少阴心经', 'HT', 'meridian_ht', null 
union select '6', 'Small Intestine Meridian', '手太阳小肠经', 'SI', 'meridian_si', null 
union select '7', 'Bladder Meridian', '足太阳膀胱经', 'BL', 'meridian_bl', null 
union select '8', 'Kidney Meridian', '足少阴肾经', 'KI', 'meridian_ki', null 
union select '9', 'Pericardium Meridian', '手厥阴心包经', 'PC', 'meridian_pc', null 
union select '10', 'Triple Energizer Meridian', '手少阳三焦经', 'TE', 'meridian_te', null 
union select '11', 'Gallbladder Meridian', '足少阳胆经', 'GB', 'meridian_gb', null 
union select '12', 'Liver Meridian', '足厥阴肝经', 'LR', 'meridian_lr', null 
union select '13', 'Governor Vessel', '督脉', 'GV', 'meridian_gv', null 
union select '14', 'Conception Vessel', '任脉', 'CV', 'meridian_cv', null
union select '15', 'Extraordinary - Head & Neck', '头颈部', 'EX-HN', 'meridian_ex-hn', null
;

-- ACUPOINT table
insert into ACUPOINT 
            (CHANNEL_ID, ORDER_NUM, CODE, PICTURE, CHINESE_NAME, NAME, ALIAS, DESCRIPTION, DESCRIPTION_ZH) 
