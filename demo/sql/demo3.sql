create or replace package refcursor_jdbc1 as
type ref_cursor is ref cursor;
function getstudents
    return ref_cursor;
end;
/
show errors
create or replace package body refcursor_jdbc1 as
function getstudents
return ref_cursor as
rc ref_cursor;
begin
    open rc for
    select * from students;
    return rc;
end;
end;
/
show errors
