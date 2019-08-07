create table build_note (id bigint not null auto_increment, created_at date, description longtext, priority integer not null, unique_identifier varchar(255), updated_at date, computer_build_id bigint not null, primary key (id));
create table change_password_token (id bigint not null auto_increment, created_date datetime, expiration_date datetime, token varchar(255), user_id bigint not null, primary key (id));
create table computer_build (id bigint not null auto_increment, build_description varchar(255), build_identifier varchar(255), build_notes_count integer, computer_parts_count integer, created_at date, directions_count integer, name varchar(40) not null, overclocking_notes_count integer, purpose_count integer, updated_at date, user_id bigint, primary key (id));
create table computer_part (id bigint not null auto_increment, name varchar(255), other_note longtext, place_purchased_at varchar(255), price double precision not null, purchase_date date not null, unique_identifier varchar(255), computer_build_id bigint not null, primary key (id));
create table direction (id bigint not null auto_increment, description longtext not null, unique_identifier varchar(255), computer_build_id bigint not null, primary key (id));
create table email_verification_token (id bigint not null auto_increment, created_date datetime, expiration_date datetime, token varchar(255), user_id bigint not null, primary key (id));
create table overclocking_note (id bigint not null auto_increment, created_at date, description longtext, priority integer not null, unique_identifier varchar(255), updated_at date, computer_build_id bigint not null, primary key (id));
create table purpose (id bigint not null auto_increment, created_at date, description longtext, priority integer not null, unique_identifier varchar(255), updated_at date, computer_build_id bigint not null, primary key (id));
create table user (id bigint not null auto_increment, created_at date, email varchar(255), enabled bit not null, full_name varchar(255), password varchar(255), updated_at date, username varchar(255), primary key (id));
alter table build_note add constraint UK_dgbwtmnmm7gd30vqm94xwvc8r unique (unique_identifier);
alter table computer_build add constraint UK_ctsf0o3mvj6w0ro9xxqkc6nax unique (build_identifier);
alter table computer_part add constraint UK_tcjjr3qqjmkoj4yg2oltn165n unique (unique_identifier);
alter table direction add constraint UK_hirmg7cubhb5jm6jy0h6u9l9m unique (unique_identifier);
alter table overclocking_note add constraint UK_o12k01i62fgc4o1yrmk0y09e3 unique (unique_identifier);
alter table purpose add constraint UK_5n52i87ihubousikst7i9mka8 unique (unique_identifier);
alter table user add constraint UK_ob8kqyqqgmefl0aco34akdtpe unique (email);
alter table user add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);
alter table build_note add constraint FK1t8qyyfmr67exvqsb8spw59qv foreign key (computer_build_id) references computer_build (id);
alter table change_password_token add constraint FKcku5bn3qvu7gvv3vxjk756rfc foreign key (user_id) references user (id);
alter table computer_build add constraint FKcme1nng7uue0fy0qqdirgl0t3 foreign key (user_id) references user (id);
alter table computer_part add constraint FKq3tcsehgkvox1gb242pyk6uj foreign key (computer_build_id) references computer_build (id);
alter table direction add constraint FKfqt8bu411441bdm5ucm4sqium foreign key (computer_build_id) references computer_build (id);
alter table email_verification_token add constraint FKqmvt3qcly3hbvde97srchdo3x foreign key (user_id) references user (id);
alter table overclocking_note add constraint FKk0xnrm0by9rdf46b2s539jyq1 foreign key (computer_build_id) references computer_build (id);
alter table purpose add constraint FKmyf4jo7jk51m6ijtfe5f8jlci foreign key (computer_build_id) references computer_build (id);
