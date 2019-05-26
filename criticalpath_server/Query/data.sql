

ALTER TABLE event MODIFY description VARCHAR(5000);

ALTER TABLE task_status MODIFY description VARCHAR(5000);

ALTER TABLE close_date MODIFY description VARCHAR(5000);

ALTER TABLE critical_path MODIFY description VARCHAR(5000);

ALTER TABLE stock_take_cycle add stoke_take_date datetime;

ALTER TABLE location_division add ram_user_id bigint(20);

ALTER TABLE critical_path add complete_date datetime;

ALTER TABLE critical_path MODIFY start_day int;

ALTER TABLE critical_path MODIFY length int;

ALTER TABLE stock_take_cycle add is_display tinyint(1);

ALTER TABLE stock_take_cycle ALTER is_display SET DEFAULT 1;

ALTER TABLE province add third_party_id bigint(20);

ALTER TABLE  api_logger MODIFY remark longtext; 

ALTER TABLE  api_logger drop column request_obj;

INSERT INTO `cp_db_storage`.`user_login` (
`id` ,
`created_date_time` ,
`is_active` ,
`is_delete` ,
`updated_date_time` ,
`email` ,
`password` ,
`role`
)
VALUES (
'31', '2019-03-20 00:00:00', '1', '0', NULL , 'pepstore_api_user','$2a$10$oyZ75fEKXJ6xzmhOGYOjBe1Ji4BrLeagjDH3bQKMRZbW0JmBuLYPW', 'ROLE_API_USER'
);

# Drop table api_logger;

#2019-03-28

UPDATE permission SET perm_key='Manage Company' WHERE perm_key = 'Manage Brand';

UPDATE permission SET perm_key='Manage Branch' WHERE perm_key = 'Manage Store';

UPDATE permission SET perm_key='Manage Branch Close Date' WHERE perm_key = 'Manage Store Close Date';

UPDATE permission SET perm_key='Manage Company User' WHERE perm_key = 'Manage Brand User';

INSERT INTO permission (`perm_key`, `display_order`) VALUES ('Manage Notification', '14');

INSERT INTO user_login_permissions (`user_login_id`, `permissions_id`) VALUES ((SELECT id FROM user_login where email = 'admin@cp.com'), (SELECT id FROM permission where perm_key = 'Manage Notification'));

ALTER TABLE manage_notification MODIFY description text;

ALTER TABLE manage_notification MODIFY body text;

#Build 20190402

INSERT INTO `permission` (`perm_key`, `display_order`) VALUES ('View API Log', '15');

INSERT INTO `user_login_permissions` (`user_login_id`, `permissions_id`) VALUES ((SELECT  id from cp_db_storage.user_login where email = 'admin@cp.com'), (SELECT id FROM permission where perm_key = 'View API Log'));

#Build 20190420
DROP INDEX UK_r00bbxfx1rmkh52g2j6v98mwa ON user;

#Build 20190426
ALTER TABLE location_division drop column ram_user_id;

#Build 20190502
INSERT INTO `permission` (`perm_key`, `display_order`) VALUES ('View Audit Log', '16');

INSERT INTO user_login_permissions (`user_login_id`, `permissions_id`) VALUES ((SELECT id FROM user_login where email = 'admin@cp.com'), (SELECT id FROM permission where perm_key = 'View Audit Log'));

#Build 20190507
UPDATE permission SET `perm_key`='Manage Non Trading Date' WHERE `id`='10';

#Build 20190513
INSERT INTO `cp_db_storage`.`user_login` (
`id` ,
`created_date_time` ,
`is_active` ,
`is_delete` ,
`updated_date_time` ,
`email` ,
`password` ,
`role`
)
VALUES (
'34', '2019-05-11 00:00:00', '1', '0', NULL , 'adminro@cp.com','$2a$10$kFaTfGPJeYyuYhSST96ibugH.m5RUNnL0obLD7Z90EgTlaKZz1KKi', 'ROLE_ADMIN_READONLY'
);

INSERT INTO user_login_permissions (`user_login_id`, `permissions_id`) 
VALUES ((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Country')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Province')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Location Division')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Task Status')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Event')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Days Of Week')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Webhook')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Company')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Branch')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Non Trading Date')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Critical Path')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Company User')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Stock Take Cycle')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'Manage Notification')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'View API Log')),
((SELECT id FROM user_login where email = 'adminro@cp.com'), 
(SELECT id FROM permission where perm_key = 'View Audit Log'));