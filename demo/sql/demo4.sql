create or replace package refcursor_jdbc as
type ref_cursor is ref cursor;
function getstudents(vgpa in number)
return ref_cursor;
end;
/
show errors
create or replace package body refcursor_jdbc as
function getstudents(vgpa in number)
return ref_cursor as
rc ref_cursor;
begin
/* Open a ref cursor for a given query */
open rc for
select * from students
where gpa > vgpa;
return rc;
end;
end;
/
show errors  
