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


create or replace trigger tr_enroll_class_size
after delete on enrollments
for each row
declare
    class_size classes.class_size%type;
begin
    select classes.class_size
    into class_size
    from classes
    where classid = :new.classid;
    update classes
    set class_size = class_size - 1
    where classid = :new.classid;
exception
    when others then
        dbms_output.put_line('Failed to update class size after dropping student [' || :new.sid || '] from class [' || :new.classid || ']. Caused by:' || chr(10) || sqlerrm);
end;
/