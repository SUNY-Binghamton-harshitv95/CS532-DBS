create or replace trigger tr_student_delete
after delete on students
for each row
begin
    delete from enrollments where sid = :old.sid;
exception
    when others then
        dbms_output.put_line('Failed to delete all enrollments of ' || :old.sid || '; Caused by:' || chr(10) || sqlerrm);
end;
/


create or replace trigger tr_student_log
after delete or insert on students
for each row
declare
    student_id students.sid%type;
    operation varchar2(6);
begin
    if inserting then
        student_id := :new.sid;
        operation := 'insert';
    elsif deleting then
        student_id := :old.sid;
        operation := 'delete';
    end if;
    insert into
    logs (who, time, table_name, operation, key_value)
    values (user, sysdate, 'students', operation, student_id);
exception
    when others then
        dbms_output.put_line('Failed to add log entry for ' || operation || ' performed on "students"');
end;
/

show errors;