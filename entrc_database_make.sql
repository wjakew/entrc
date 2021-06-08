/*
programmer Jakub Wawak
all rights reserved
kubawawak@gmail.com
version (from schema) v1.0.0
sql script that reloads ENTRC database
*/	
USE entrc_database;

-- checking if old data is on the database 
drop table if exists PROGRAM_LOG;
drop table if exists ERROR_LOG;
drop table if exists RUNTIME;
drop table if exists ENTRC_API_DATA;
drop table if exists CONFIGURATION;
drop table if exists USER_MESSAGE;
drop table if exists ENTRANCE_EXIT;
drop table if exists ENTRANCE;
drop table if exists USER_LOG;
drop table if exists DATA_LOG;
drop table if exists GRAVEYARD;
drop table if exists ADMIN_GRAVEYARD;
drop table if exists ADMIN_PRIVILAGES;
drop table if exists ANNOUNCEMENT;
drop table if exists ADMIN_DATA;
drop table if exists BARCODE_DATA;
drop table if exists WORKER;

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
-- table for storing client app configuration
CREATE TABLE RUNTIME
(
runtime_id INT PRIMARY KEY AUTO_INCREMENT,
runtime_license VARCHAR(19),
runtime_macaddress VARCHAR(50)
);
-- table for storing entrc_api data
CREATE TABLE ENTRC_API_DATA
(
entrc_api_data INT PRIMARY KEY AUTO_INCREMENT,
entrc_api_appcode VARCHAR(10),
entrc_api_desc VARCHAR(100)
);
-- table for storing configuration data
CREATE TABLE CONFIGURATION
(
entrc_user_exit_pin VARCHAR(10),
entrc_user_ask_pin VARCHAR(10),
entrc_user_manage_pin VARCHAR(10),
entrc_admin_manage_pin VARCHAR(10),
entrc_admin_show_ip VARCHAR(10)
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
-- table for storing log for barcodes
CREATE TABLE BARCODE_DATA
(
barcode_data_id INT PRIMARY KEY AUTO_INCREMENT,
barcode_date DATE,
worker_id INT,
barcode_raw_data VARCHAR(100),
CONSTRAINT fk_barcodedata FOREIGN KEY (worker_id) REFERENCES WORKER(worker_id)
);
-- table for storing admin data (id1)
CREATE TABLE ADMIN_DATA
(
admin_id INT PRIMARY KEY AUTO_INCREMENT,
admin_login VARCHAR(30),
admin_password VARCHAR(150),
admin_email VARCHAR(50),
admin_level INT,
admin_active INT
);
-- table for setting announcments
CREATE TABLE ANNOUNCEMENT
(
announcement_id INT PRIMARY KEY AUTO_INCREMENT,
announcement_update TIMESTAMP,
announcement_data TEXT,
admin_id INT,

CONSTRAINT fk_announcment FOREIGN KEY (admin_id) REFERENCES ADMIN_DATA(admin_id)
);
-- table for storing privilages for admin access
CREATE TABLE ADMIN_PRIVILAGES
(
admin_privilages_id INT PRIMARY KEY AUTO_INCREMENT,
admin_id INT,
admin_privilages_string VARCHAR(150),

CONSTRAINT fk_adminprivilages FOREIGN KEY (admin_id) REFERENCES ADMIN_DATA(admin_id)
);
-- table for storing fired workers
CREATE TABLE GRAVEYARD
(
graveyard_id INT PRIMARY KEY AUTO_INCREMENT,
graveyard_date DATE,
worker_id INT,
admin_id INT,

CONSTRAINT fk_graveyard FOREIGN KEY (worker_id) REFERENCES WORKER(worker_id),
CONSTRAINT fk_graveyard2 FOREIGN KEY (admin_id) REFERENCES ADMIN_DATA(admin_id)
);
-- table for storing fired admins
CREATE TABLE ADMIN_GRAVEYARD
(
graveyard_id INT PRIMARY KEY AUTO_INCREMENT,
graveyard_date DATE,
admin_id INT,
CONSTRAINT fk_a_graveyard2 FOREIGN KEY (admin_id) REFERENCES ADMIN_DATA(admin_id)
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
-- creating empty worker
INSERT INTO WORKER
(worker_login,worker_name,worker_surname,worker_pin,worker_position)
VALUES
("brak","brak","brak","brak","brak");
-- creating configuration data
INSERT INTO CONFIGURATION
(entrc_user_exit_pin,entrc_user_ask_pin,entrc_user_manage_pin,entrc_admin_manage_pin,entrc_admin_show_ip)
VALUES
("999999","666666","7777777","28450872","69696969");
-- creating admin account
INSERT INTO ADMIN_DATA
(admin_login,admin_password,admin_level,admin_email,admin_active)
VALUES
("admin","21232f297a57a5a743894a0e4a801fc3",1,"",1);