INSERT INTO endpoints (name, url, check_interval_seconds, enabled, created_at, response_time_threshold_ms, failure_threshold)
VALUES ('Google', 'https://google.com', 30, true, NOW(), 5000, 1);

INSERT INTO endpoints (name, url, check_interval_seconds, enabled, created_at, response_time_threshold_ms, failure_threshold)
VALUES ('GitHub', 'https://github.com', 30, true, NOW(), 5000, 1);

INSERT INTO endpoints (name, url, check_interval_seconds, enabled, created_at, response_time_threshold_ms, failure_threshold)
VALUES ('Fake Down Site', 'https://thisurldoesnotexist.fake', 30, true, NOW(), 5000, 1);
