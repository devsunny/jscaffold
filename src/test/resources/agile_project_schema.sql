create multiset table test.Person no test, no good
(
	person_id varchar2(32 BYTE) not null primary key,
	first_name varchar(64) not null,
	last_name varchar(64) not null,
	middle_name varchar(32) null
	
)

create table Project
(
	project_id integer not null identity primary key,
	project_name varchar(128) not null,
	description varchar(256) null,
	start_date date null,
	end_date date null
);

