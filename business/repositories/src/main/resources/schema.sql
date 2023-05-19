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

comment on table member is 'Таблица описывает сущность сотрудник';

comment on column member.id is 'Уникальный идентификатор сотрудника';

comment on column member.lastname is 'Фамилия (обязательное поле)';

comment on column member.firstname is 'Имя (обязательное поле)';

comment on column member.patronymic is 'Отчество (не обязательное)';

comment on column member.job_title is 'Должность (не обязательное)';

comment on column member.account is 'Учетная запись (не обязательное), но  уникальное значение среди активных сотрудников';

comment on column member.email is 'Адрес электронной почты (не обязательное)';

comment on column member.status is 'Статус сотрудника - (обязательное поле) фиксированный набор значений (ACTIVE, REMOVE)';

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

comment on table project is 'Таблица описывает сущность проект';

comment on column project.id is 'Уникальный идентификатор проекта';

comment on column project.codename is 'Код проекта - некоторое уникальное имя проекта. Является обязательным и уникальным среди всех проектов.';

comment on column project.title is 'Наименование - текстовое значение содержащее короткое наименование проекта.. Обязательное поле.';

comment on column project.description is 'Описание - текстовое значение содержащее более детальную информацию о проекте. Не обязательное поле.';

comment on column project.status is 'Статус проекта - текстовое значение, обозначающее состояние проекта. Список статусов фиксированный (DRAFT, DEVELOPING, TESTING, FINISHED). Обязательное поле.';

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
    constraint team_pk
        primary key (member_id, project_id)
);

comment on table team is 'Группа сотрудников, объединенных общим проектом';

comment on column team.project_id is 'Сущность - проект';

comment on column team.member_id is 'Сущность - сотрудник';

comment on column team.role is 'Роль сотрудника. В разных командах один сотрудник может принимать различные роли, но только одну роль внутри проекта. Список ролей фиксированный: TEAMLEAD, ANALYST, DEVELOPER, QA.';

alter table team
    owner to postgres;

create table task
(
    id                 serial
        constraint task_pk
            primary key,
    title              varchar(100) not null,
    description        text,
    responsible_member integer,
    hours_cost         integer      not null
        constraint check_hours_cost
            check (hours_cost > 0),
    deadline           timestamp    not null,
    status             varchar(20)  not null
        constraint check_task_status
            check ((status)::text = ANY
                   ((ARRAY ['NEW'::character varying, 'WORKING'::character varying, 'FINISHED'::character varying, 'CLOSED'::character varying])::text[])),
    author             integer      not null,
    creation_date      timestamp    not null,
    lastchange_date    timestamp,
    constraint task_team_fk
        foreign key (responsible_member, author) references team,
    constraint check_lastchange_date
        check (lastchange_date >= creation_date),
    constraint check_deadline
        check (deadline > creation_date)
);

comment on table task is 'Таблица описывает сущность задача';

comment on column task.id is 'Уникальный идентификатор задачи';

comment on column task.title is 'Наименование задачи - текстовое значение, отражающее краткую информацию о задачи (обязательное поле).';

comment on column task.description is 'Описание задачи - текстовое значение, содержащее детальное описание задачи. (не обязательное поле)';

comment on column task.responsible_member is 'Исполнитель задачи - сотрудник, которому необходимо исполнить задачу. (не обязательное поле). Можно выбрать исполнителя только участника проекта ( сотрудник добавленный в команду проекта). Назначить исполнителя можно только сотрудника в статусе Активный.';

comment on column task.hours_cost is 'Трудозатраты - оценка, сколько в часах необходимо на ее исполнение. (обязательное поле)';

comment on column task.deadline is 'Крайний срок - дата, когда задача должна быть исполнена. Нельзя выбрать дату если дата меньше, чем  дата создания + трудозатраты. Обязательное поле.';

comment on column task.status is 'Статус задачи - фиксированный список состояний задачи (NEW, WORKING, FINISHED, CLOSED). ';

comment on column task.author is 'Автор задачи - заполняется автоматически, тем кто создавал задачу. Автором задачи может являться только участник проекта.';

comment on column task.creation_date is 'Дата создания - дата когда задача была создана.';

comment on column task.lastchange_date is 'Дата последнего изменения задачи - дата последнего редактирования задачи (но не изменение статуса задачи).';

alter table task
    owner to postgres;

