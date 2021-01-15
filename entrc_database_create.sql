/*
programmer Jakub Wawak
all rights reserved
kubawawak@gmail.com
version (from schema) v1.0.0
sql script that creates ENTRC database
*/	
-- table for stroing program log
CREATE TABLE PROGRAM_LOG
(
program_log_id INT PRIMARY KEY AUTO_INCREMENT,
program_log_desc VARCHAR(300)
);
CREATE TABLE ERROR_LOG
(
error_log_id INT PRIMARY KEY AUTO_INCREMENT,
error_log_code VARCHAR(10),
error_log_desc VARCHAR(250)
);
-- table for storing configuration data
CREATE TABLE CONFIGURATION
(
entrc_user_exit_pin VARCHAR(10),
entrc_user_ask_pin VARCHAR(10),
entrc_user_manage_pin VARCHAR(10)
);
-- table for storing worker data (id0)
CREATE TABLE WORKER
(
worker_id INT PRIMARY KEY AUTO_INCREMENT,
worker_login VARCHAR(30),
worker_name VARCHAR(50),
worker_surname VARCHAR(100),
worker_pin VARCHAR(4),
worker_position VARCHAR(20)
);
-- table for storing admin data (id1)
CREATE TABLE ADMIN_DATA
(
admin_id INT PRIMARY KEY AUTO_INCREMENT,
admin_login VARCHAR(30),
admin_password VARCHAR(50),
admin_level INT
);
-- table for storing log made by admin (id2)
CREATE TABLE DATA_LOG
(
data_log_id INT PRIMARY KEY AUTO_INCREMENT,
admin_id INT,
data_log_date TIMESTAMP,
data_log_action VARCHAR(30),
data_log_desc VARCHAR(200),

CONSTRAINT fk_datalog1 FOREIGN KEY (admin_id) REFERENCES ADMIN_DATA(admin_id)
);
-- table for storing log made by users (id3)
CREATE TABLE USER_LOG
(
user_log_id INT PRIMARY KEY AUTO_INCREMENT,
user_log_date TIMESTAMP,
worker_id INT,
user_log_action VARCHAR(20),
user_log_desc VARCHAR(100),
user_log_photo_src VARCHAR(200),

CONSTRAINT fk_userlog1 FOREIGN KEY (worker_id) REFERENCES WORKER(worker_id)
);
-- table for storing entrance data made by workers (id4)
CREATE TABLE ENTRANCE
(
entrance_id INT PRIMARY KEY AUTO_INCREMENT,
worker_id INT,
log_id INT,
entrance_time TIMESTAMP,
entrance_finished INT,

CONSTRAINT fk_entrance1 FOREIGN KEY (worker_id) REFERENCES WORKER(worker_id),
CONSTRAINT fk_entrance2 FOREIGN KEY (log_id) REFERENCES USER_LOG(user_log_id)
);
-- table for storing entrance exit data made by workers (id5)
CREATE TABLE ENTRANCE_EXIT
(
entrance_exit_id INT PRIMARY KEY AUTO_INCREMENT,
worker_id INT,
user_log_id INT,
entrance_exit_time TIMESTAMP,

CONSTRAINT fk_entranceexit2 FOREIGN KEY (worker_id) REFERENCES WORKER(worker_id),
CONSTRAINT fk_entranceexit3 FOREIGN KEY (user_log_id) REFERENCES USER_LOG(user_log_id)
);
-- table for messages between admin and users (id6)
CREATE TABLE USER_MESSAGE
(
user_message_id INT PRIMARY KEY AUTO_INCREMENT,
admin_id INT,
worker_id INT,
user_message_content VARCHAR(250),
user_message_seen INT,

CONSTRAINT fk_usermessage1 FOREIGN KEY (worker_id) REFERENCES WORKER(worker_id),
CONSTRAINT fk_usermessag2 FOREIGN KEY (admin_id) REFERENCES ADMIN_DATA(admin_id)
);
