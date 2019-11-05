CREATE OR REPLACE PROCEDURE 
show_status(sid_in in students.sid%type,
    status_out out students.status%type) is
begin 
    select status into status_out from students where students.sid = sid_in;
end;
/
