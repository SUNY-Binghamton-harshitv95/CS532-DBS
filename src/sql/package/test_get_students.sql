declare
    type rcur is ref cursor; rc rcur;
    st students%rowtype;
begin
    rc := srs.get_students();
    fetch rc into st;
    while rc%found
    loop
        dbms_output.put_line(st.sid || ', ' || st.firstname);
        fetch rc into st;
    end loop;
end;
/