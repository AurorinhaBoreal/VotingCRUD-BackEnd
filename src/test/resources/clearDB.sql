DELETE FROM tbl_log WHERE EXISTS ( SELECT 1 FROM tbl_log );

DELETE FROM tbl_agenda_users_voted WHERE EXISTS (SELECT 1 FROM tbl_agenda_users_voted);

DELETE FROM tbl_agenda WHERE EXISTS ( SELECT 1 FROM tbl_agenda );

DELETE FROM tbl_user WHERE EXISTS ( SELECT 1 FROM tbl_user );