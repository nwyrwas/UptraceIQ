INSERT INTO endpoints (name, url, check_interval_seconds, enabled, created_at)
VALUES ('Google', 'https://google.com', 30, true, NOW());

INSERT INTO endpoints (name, url, check_interval_seconds, enabled, created_at)
VALUES ('GitHub', 'https://github.com', 30, true, NOW());

INSERT INTO endpoints (name, url, check_interval_seconds, enabled, created_at)
VALUES ('Fake Down Site', 'https://thisurldoesnotexist.fake', 30, true, NOW());
