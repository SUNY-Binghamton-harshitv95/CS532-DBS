create sequence seq_logid
increment by 1
start with 1000
maxvalue 9999
order;

alter table logs
modify logid number(4) default seq_logid.nextval not null;