create table member
(
    id        serial
        constraint members_pk
            primary key,
    lastname   varchar(50)                                         not null,
    firstname  varchar(50)                                         not null,
    patronymic varchar(50),
    job_title  varchar(100),
    account    varchar(50),
    email      varchar(50),
    status     varchar(20)                                         not null
        constraint check_member_status
            check ((status)::text = ANY ((ARRAY ['ACTIVE'::character varying, 'REMOVED'::character varying])::text[]))
);

alter table member
    owner to postgres;

create table project
(
    id          serial
        constraint project_pk
            primary key,
    codename    varchar(50)  not null,
    title       varchar(100) not null,
    description text,
    status      varchar(20)  not null
        constraint check_project_status
            check ((status)::text = ANY
                   ((ARRAY ['DRAFT'::character varying, 'DEVELOPING'::character varying, 'TESTING'::character varying, 'FINISHED'::character varying])::text[]))
);

alter table project
    owner to postgres;

create table team
(
    project_id integer     not null
        constraint team_project_id_fk
            references project,
    member_id  integer     not null
        constraint team_member_id_fk
            references member,
    role       varchar(50) not null
        constraint check_role
            check ((role)::text = ANY
                   ((ARRAY ['TEAMLEAD'::character varying, 'ANALYST'::character varying, 'DEVELOPER'::character varying, 'QA'::character varying])::text[])),
    id         serial
        constraint team_pk
            primary key
);

alter table team
    owner to postgres;

create table task
(
    id                 serial
        constraint task_pk
            primary key,
    title              varchar(100) not null,
    description        text,
    responsible_member integer
        constraint task_responsible__fk
            references team,
    hours_cost         integer      not null
        constraint check_hours_cost
            check (hours_cost > 0),
    deadline           timestamp    not null,
    status             varchar(20)  not null
        constraint check_task_status
            check ((status)::text = ANY
                   ((ARRAY ['NEW'::character varying, 'WORKING'::character varying, 'FINISHED'::character varying, 'CLOSED'::character varying])::text[])),
    author             integer      not null
        constraint task_author_fk
            references team,
    creation_date      timestamp    not null,
    lastchange_date    timestamp,
    constraint check_lastchange_date
        check (lastchange_date >= creation_date),
    constraint check_deadline
        check (deadline > creation_date)
);

alter table task
    owner to postgres;