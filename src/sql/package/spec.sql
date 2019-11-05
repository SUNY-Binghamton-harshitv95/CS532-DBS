create or replace package srs as

type refcursor is ref cursor;

-- Problem 2
-- Functions to get refcursor pointing to the table's cursor
-- Procedures to display table data in sqlplus console
procedure show_students;
function get_students return refcursor;
procedure show_courses;
function get_courses return refcursor;
procedure show_classes;
function get_classes return refcursor;
procedure show_enrollments;
function get_enrollments return refcursor;
procedure show_prerequisites;
function get_prerequisites return refcursor;
procedure show_logs;
function get_logs return refcursor;


-- Problem 3
procedure add_student (
    sid students.sid%type,
    firstname students.firstname%type,
    lastname students.lastname%type,
    status students.status%type,
    gpa students.gpa%type,
    email students.email%type
);

-- Problem 4
function get_enrollment_details(student_id students.sid%type) return refcursor;
procedure show_enrollment_details(student_id students.sid%type);

-- Problem 5
function get_prerequisites(
    deptcode courses.dept_code%type,
    courseno courses.course_no%type
    ) return refcursor;
procedure show_prerequisites(
    deptcode courses.dept_code%type,
    courseno courses.course_no%type
);

-- Problem 6
function get_class_students(
    class_id classes.classid%type
) return refcursor;
procedure show_class_students(
    class_id classes.classid%type
);

-- Problem 7
procedure enroll_student(
    student_id students.sid%type,
    class_id classes.classid%type,
    status_out out varchar2
    );


end;
/
show errors