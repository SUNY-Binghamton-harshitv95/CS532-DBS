drop table students;
create table students (sid char(4) primary key check (sid like 'B%'),
    firstname varchar2(15) not null, lastname varchar2(15) not null, status varchar2(10) 
    check (status in ('freshman', 'sophomore', 'junior', 'senior', 'graduate')), 
    gpa number(3,2) check (gpa between 0 and 4.0), email varchar2(20));
