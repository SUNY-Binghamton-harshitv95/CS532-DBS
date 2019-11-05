    create or replace package body srs as
    procedure show_students is
    begin
        declare
            cursor cur is (select * from students);
            cur_row cur%rowtype;
            has_records boolean := false;
        begin
            if (not cur%isopen) then
                open cur;
            end if;
            fetch cur into cur_row;
            while cur%found
            loop
                dbms_output.put(cur_row.sid || ', ');
                dbms_output.put(cur_row.firstname || ', ');
                dbms_output.put(cur_row.lastname || ', ');
                dbms_output.put(cur_row.status || ', ');
                dbms_output.put(cur_row.gpa || ', ');
                dbms_output.put(cur_row.email);
                dbms_output.new_line;
                fetch cur into cur_row;
                has_records := true;
            end loop;
            if (not has_records) then
                dbms_output.put_line('No Records found in Students table');
            end if;
            close cur;
        end;
    end;

    function get_students
    return refcursor as
    rc refcursor;
    begin
        open rc for
        select * from students;
        return rc;
    end;

    procedure show_courses is
    has_records boolean := false;
    begin
        for cur in (select * from courses)
        loop
            dbms_output.put(cur.DEPT_CODE || ', ');
            dbms_output.put(cur.COURSE_NO || ', ');
            dbms_output.put(cur.TITLE);
            dbms_output.new_line;
            has_records := true;
        end loop;
        if (not has_records) then
            dbms_output.put_line('No Records found in Courses table');
        end if;
    end;

    function get_courses
    return refcursor as
    rc refcursor;
    begin
        open rc for
        select * from courses;
        return rc;
    end;

    procedure show_classes is
    has_records boolean := false;
    begin
        for cur in (select * from classes)
        loop
            dbms_output.put(cur.CLASSID || ', ');
            dbms_output.put(cur.DEPT_CODE || ', ');
            dbms_output.put(cur.COURSE_NO || ', ');
            dbms_output.put(cur.SECT_NO || ', ');
            dbms_output.put(cur.YEAR || ', ');
            dbms_output.put(cur.SEMESTER || ', ');
            dbms_output.put(cur.LIMIT || ', ');
            dbms_output.put(cur.CLASS_SIZE);
            dbms_output.new_line;
        end loop;
        if (not has_records) then
            dbms_output.put_line('No Records found in Classes table');
        end if;
    end;

    function get_classes
    return refcursor as
    rc refcursor;
    begin
        open rc for
        select * from classes;
        return rc;
    end;


    procedure show_enrollments is
    has_records boolean := false;
    begin
        for cur in (select * from enrollments)
        loop
            dbms_output.put(cur.SID || ', ');
            dbms_output.put(cur.CLASSID || ', ');
            dbms_output.put(cur.LGRADE);
            dbms_output.new_line;
        end loop;
        if (not has_records) then
            dbms_output.put_line('No Records found in Enrollments table');
        end if;
    end;

    function get_enrollments
    return refcursor as
    rc refcursor;
    begin
        open rc for
        select * from enrollments;
        return rc;
    end;

    procedure show_prerequisites is
    has_records boolean := false;
    begin
        for cur in (select * from prerequisites)
        loop
            dbms_output.put(cur.DEPT_CODE || ', ');
            dbms_output.put(cur.COURSE_NO || ', ');
            dbms_output.put(cur.PRE_DEPT_CODE || ', ');
            dbms_output.put(cur.PRE_COURSE_NO);
            dbms_output.new_line;
        end loop;
        if (not has_records) then
            dbms_output.put_line('No Records found in Prerequisites table');
        end if;
    end;

    function get_prerequisites
    return refcursor as
    rc refcursor;
    begin
        open rc for
        select * from prerequisites;
        return rc;
    end;

    procedure show_logs is
    has_records boolean := false;
    begin
        for cur in (select * from logs)
        loop
            dbms_output.put(cur.logid || ', ');
            dbms_output.put(cur.WHO || ', ');
            dbms_output.put(cur.TIME || ', ');
            dbms_output.put(cur.TABLE_NAME || ', ');
            dbms_output.put(cur.OPERATION || ', ');
            dbms_output.put(cur.KEY_VALUE);
        end loop;
        if (not has_records) then
            dbms_output.put_line('No Logs Records found in Database');
        end if;
    end;

    function get_logs
    return refcursor as
    rc refcursor;
    begin
        open rc for
        select * from logs;
        return rc;
    end;



    procedure add_student (
        sid students.sid%type,
        firstname students.firstname%type,
        lastname students.lastname%type,
        status students.status%type,
        gpa students.gpa%type,
        email students.email%type
    ) is
    begin
        insert into students (
            sid,
            firstname,
            lastname,
            status,
            gpa,
            email
        )
        values (
            sid,
            firstname,
            lastname,
            status,
            gpa,
            email
        );
    end;


    function get_enrollment_details (
        student_id students.sid%type
    )
    return refcursor
    as
    rc refcursor;
    begin
        open rc for
        select
        s.sid, s.lastname, s.status,
        cl.classid, cl.dept_code || cl.course_no as course,
        c.title, cl.year, cl.semester
        from (select * from students where sid = student_id) s
        left outer join enrollments e
        on s.sid = e.sid
        left outer join classes cl
        on e.classid = cl.classid
        left outer join courses c
        on c.dept_code = cl.dept_code and c.course_no = cl.course_no;
        return rc;
    end;


    procedure show_enrollment_details (
        student_id students.sid%type
    )
    is
    cursor cr is
        select
        s.sid, s.lastname, s.status,
        cl.classid, cl.dept_code || cl.course_no as course,
        c.title, cl.year, cl.semester
        from (select * from students where sid = student_id) s
        left outer join enrollments e
        on s.sid = e.sid
        left outer join classes cl
        on e.classid = cl.classid
        left outer join courses c
        on c.dept_code = cl.dept_code and c.course_no = cl.course_no;
    has_records boolean := false;
    cur_row cr%rowtype;
    begin
        if (not cr%isopen) then
            open cr;
        end if;
        fetch cr into cur_row;
        if (cr%notfound) then
            dbms_output.put_line('The sid is invalid');
        elsif cur_row.classid is null then
            dbms_output.put_line('The student has not taken any course');
        else
            while cr%found
            loop
                has_records := true;
                if (cur_row.classid is null) then
                    exit;
                end if;
                dbms_output.put_line(
                    cur_row.sid || ', ' ||
                    cur_row.lastname || ', ' ||
                    cur_row.status || ', ' ||
                    cur_row.classid || ', ' ||
                    cur_row.course || ', ' ||
                    cur_row.title || ', ' ||
                    cur_row.year || ', ' ||
                    cur_row.semester
                    );

                fetch cr into cur_row;
            end loop;
        end if;
        if (cr%isopen) then
            close cr;
        end if;
    end;


    function get_prerequisites(
        deptcode courses.dept_code%type,
        courseno courses.course_no%type
    ) return refcursor
    as
    rc refcursor;
    begin
        open rc for
        select PRE_DEPT_CODE || PRE_COURSE_NO as pre_course
        from prerequisites
        start with dept_code = deptcode and course_no = courseno
        connect by prior PRE_COURSE_NO = course_no
        and prior PRE_DEPT_CODE = dept_code;
    end;


    procedure show_prerequisites(
        deptcode courses.dept_code%type,
        courseno courses.course_no%type
    )
    is
    begin
        for cur_row in (
            select PRE_DEPT_CODE || PRE_COURSE_NO as pre_course
            from prerequisites
            start with dept_code = deptcode and course_no = courseno
            connect by prior PRE_COURSE_NO = course_no
            and prior PRE_DEPT_CODE = dept_code
        ) loop
            dbms_output.put_line(cur_row.pre_course);
        end loop;
    end;

    function get_class_students(
        class_id classes.classid%type
    ) return refcursor
    as
    rc refcursor;
    begin
        open rc for
        select classid, title, semester, year, sid, lastname
        from (select * from classes where classid = class_id)
        left outer join enrollments using (classid)
        left outer join courses using (dept_code, course_no)
        left outer join students using (sid)
        order by classid;
        return rc;
    end;

    procedure show_class_students(
        class_id classes.classid%type
    ) is
    cursor c is
    select classid, title, semester, year, sid, lastname
    from (select * from classes where classid = class_id)
    left outer join enrollments using (classid)
    left outer join courses using (dept_code, course_no)
    left outer join students using (sid)
    order by classid;
    has_records boolean := false;
    begin
        for cur_row in c loop
            has_records := true;
            if (cur_row.sid is null) then
                dbms_output.put_line('No student is enrolled in the class');
                exit;
            end if;
            dbms_output.put_line(
                cur_row.classid || ', ' ||
                cur_row.title || ', ' ||
                cur_row.semester || ', ' ||
                cur_row.year || ', ' ||
                cur_row.sid || ', ' ||
                cur_row.lastname
            );
        end loop;
        if (not has_records) then
            dbms_output.put_line('The cid is invalid');
        end if;
    end;


    -- Problem 7
    procedure enroll_student(
        student_id students.sid%type,
        class_id classes.classid%type,
        status_out out varchar2
    ) is
    cid classes.classid%type;
    class_size classes.class_size%type;
    class_limit classes.limit%type;
    begin
        -- Checking if sid is valid
        declare
            studentid students.sid%type;
        begin
            select sid into studentid from students where sid = student_id;
        exception
            when no_data_found then
                status_out := 'The sid is invalid';
                dbms_output.put_line(status_out);
                return;
        end;

        -- Checking if cid is valid
        declare
            class_full exception;
        begin
            select classid, class_size, limit into cid, class_size, class_limit from classes where classid = class_id;
            if (class_size = class_limit) then
                raise class_full;
            end if;
        exception
            when no_data_found then
                status_out := 'The cid is invalid';
                dbms_output.put_line(status_out);
                return;
            when class_full then
                status_out := 'The class is full';
                dbms_output.put_line(status_out);
                return;
        end;

        --  Checking if the student is already enrolled in the class
        declare
            ctr_enrollments integer := 0;
        begin
            for cur_row in (select classid from enrollments where sid = student_id)
            loop
                if (cur_row.classid = cid) then
                    status_out := 'The student is already in this class';
                    dbms_output.put_line(status_out);
                    return;
                end if;
                ctr_enrollments := ctr_enrollments + 1;
            end loop;
            if (ctr_enrollments = 3) then
                status_out := 'You are overloaded';
                dbms_output.put_line(status_out);
            elsif (ctr_enrollments >= 4) then
                status_out := 'Students cannot be enrolled in more than four classes in the same semester';
                dbms_output.put_line(status_out);
                return;
            end if;
        end;

        -- Checking if all prerequisites are completed
        begin
            for cur_row in (select pre_course, lgrade from 
            (
                select PRE_DEPT_CODE || PRE_COURSE_NO as pre_course
                from prerequisites
                start with dept_code = 'CS' and course_no = 542
                connect by prior PRE_COURSE_NO = course_no
                and prior PRE_DEPT_CODE = dept_code) pre
                left outer join (
                    select dept_code || course_no as course, lgrade
                    from enrollments join classes using (classid)
                    where sid = 'B001'
                ) cl
                on pre.pre_course = cl.course
                where (lgrade is null or lgrade > 'C')
            )
            loop
                status_out := 'Prerequisite courses have not been completed';
                dbms_output.put_line(status_out);
                return;
            end loop;
        end;

    end;


    end;
    /
    show errors;