create table Person
(
	person_id varchar(32) not null primary key,
	first_name varchar(64) not null,
	last_name varchar(64) not null,
	middle_name varchar(32) null
	
);

create table Project
(
	project_id integer not null primary key,
	project_name varchar(128) not null,
	description varchar(256) null,
	start_date date null,
	end_date date null
);


create table project_team
(
	project_id integer not null,
	person_id varchar(32) not null,
	primary key (project_id, person_id)
)
;

create table project_release
(
	release_id integer not null primary key,	
	project_id integer not null,
	release_name varchar(128) not null,	
	major_version integer not null,
	minor_version integer not null,
	build_number  integer not null,
	release_notes varchar(256) not null,
	target_date  date null,
	actual_released_date date null
);


create table project_release_feature
(
	feature_id integer not null primary key,
	release_id integer not null,
	feature_name varchar(128) not null,		
	description varchar(256) not null
);



create table project_feature_backlog
(
	backlog_id integer not null primary key,
	feature_id integer not null,
	backlog_story_name varchar(128) not null,
	description varchar(256) not null,
	function_input varchar(256) not null,
	function_output varchar(256) not null,
	target_date  date not null,
	completed_date date null
)
;

create table backlog_assignment
(
	backlog_id integer not null,
	person_id varchar(32) not null,	
	primary key (backlog_id, person_id)
)
;

create table backlog_dependency
(
	backlog_id integer not null,
	dependee_backlog_id integer not null,	
	primary key (backlog_id, dependee_backlog_id)
)

;







