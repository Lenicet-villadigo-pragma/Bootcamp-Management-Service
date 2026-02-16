CREATE TABLE IF NOT EXISTS rchallenge_bootcamp_pragma.bootcamp (
     id BIGINT auto_increment NOT NULL,
     name varchar(50) NOT NULL,
     description varchar(90) NULL,
     start_date DATETIME DEFAULT now() NOT NULL,
     estimated_time BIGINT NULL,
     CONSTRAINT bootcamp_pk PRIMARY KEY (id),
     CONSTRAINT bootcamp_unique UNIQUE KEY (name)
);

CREATE TABLE IF NOT EXISTS rchallenge_bootcamp_pragma.bootcamp_skill (
   bootcamp_id BIGINT NOT NULL,
   skill_id BIGINT NOT NULL,
   CONSTRAINT bootcamp_skill_pk PRIMARY KEY (bootcamp_id,skill_id)
);