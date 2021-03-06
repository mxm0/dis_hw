drop table estate_agent
drop table estate
drop table house
drop table appartment
drop table person
drop table purchase_contract
drop table contract
drop table tenancy_contract
drop table sells
drop table rents
drop table manages

CREATE TABLE estate_agent(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, name varchar(255), address varchar(255), login varchar(40) not NULL unique, password varchar(65) not NULL)


CREATE TABLE estate(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, city varchar(255), postalcode varchar(40), street varchar(40), street_number varchar(40), square_area varchar(40))

CREATE TABLE house(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, floors varchar(255), price varchar(40), garden varchar(40), estate_id int, constraint house_constraint foreign key (estate_id) references estate(id) on delete cascade)


CREATE TABLE appartment(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, floors varchar(255), price varchar(40), garden varchar(40), rooms integer, balcony smallint, builtinKitchen smallint, estate_id int, constraint appartment_constraint foreign key (estate_id) references estate(id) on delete cascade)


CREATE TABLE person(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, first_name varchar(255) not null, name varchar(255) not null, address varchar(255), constraint person_name UNIQUE (first_name, name))


CREATE TABLE contract(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, sign_date date, place_id varchar(255))


CREATE TABLE purchase_contract(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, installment INTEGER, interest_rate varchar(255), contract_id int, constraint purchase_contract_constraint foreign key (contract_id) references contract(id) on delete cascade)


CREATE TABLE tenancy_contract(id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1, NO CACHE) PRIMARY KEY, start_date date, duration varchar(40),additional_costs varchar(40),contract_id int, constraint tenancy_contract_constraint foreign key (contract_id) references contract(id) on delete cascade)


CREATE TABLE manages(PRIMARY KEY(estate_agent_id,estate_id), estate_agent_id int not null, estate_id int not null, constraint estate_agent_id_constraint foreign key (estate_agent_id) references estate_agent(id) on delete cascade, constraint estate_id_constraint foreign key (estate_id) references estate(id) on delete cascade, constraint uniquename unique (estate_id))


CREATE TABLE rents(PRIMARY KEY(appartment_id,person_id,tenancy_contract_id), tenancy_contract_id int not null, appartment_id int not null, person_id int not null, constraint appartment_id_constraint foreign key (appartment_id) references appartment(id) on delete cascade, constraint estate_id_constraint foreign key (person_id) references person(id) on delete cascade, constraint tenancy_id_constraint foreign key (tenancy_contract_id) references tenancy_contract(id) on delete cascade)


CREATE TABLE sells(PRIMARY KEY(house_id,person_id,purchase_contract_id), house_id int not null, person_id int not null, purchase_contract_id int not null, constraint house_id_constraint foreign key (house_id) references house(id) on delete cascade, constraint estate_id_constraint foreign key (person_id) references person(id) on delete cascade, constraint purchase_contract_id foreign key (purchase_contract_id) references purchase_contract(id) on delete cascade)
