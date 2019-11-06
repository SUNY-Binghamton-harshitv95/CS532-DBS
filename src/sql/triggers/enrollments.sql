create or replace trigger tr_enroll_class_size
after insert on enrollments
for each row
declare
    class_size classes.class_size%type;
begin
    select classes.class_size
    into class_size
    from classes
    where classid = :new.classid;
    update classes
    set class_size = class_size + 1
    where classid = :new.classid;
exception
    when others then
        dbms_output.put_line('Failed to update class size after enrolling student [' || :new.sid || '] into class [' || :new.classid || ']. Caused by:' || chr(10) || sqlerrm);
end;
/


create or replace trigger tr_drop_course_class_size
after delete on enrollments
for each row
declare
    class_size classes.class_size%type;
begin
    select classes.class_size
    into class_size
    from classes
    where classid = :old.classid;
    update classes
    set class_size = class_size - 1
    where classid = :old.classid;
exception
    when others then
        dbms_output.put_line('Failed to update class size after dropping student [' || :old.sid || '] from class [' || :old.classid || ']. Caused by:' || chr(10) || sqlerrm);
end;
/


create or replace trigger tr_enrollments_log
after delete or insert on enrollments
for each row
declare
    student_id students.sid%type;
    class_id classes.classid%type;
    operation varchar2(6);
begin
    if inserting then
        student_id := :new.sid;
        class_id := :new.classid;
        operation := 'insert';
    elsif deleting then
        student_id := :old.sid;
        class_id := :old.classid;
        operation := 'delete';
    end if;
    insert into
    logs (who, time, table_name, operation, key_value)
    values (user, sysdate, 'enrollments', operation, student_id || ', ' || class_id);
exception
    when others then
        dbms_output.put_line('Failed to add log entry for ' || operation || ' performed on "enrollments"');
end;
/
show errors;