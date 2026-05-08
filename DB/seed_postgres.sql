-- Postgres seed data for Render deployment
-- Run this after the first deploy when Hibernate has created tables via ddl-auto=update

-- Insert default user (password is bcrypt hash of 'blake')
INSERT INTO "user" (username, password, enabled, email, role)
VALUES ('blake', '$2b$12$8K7fjiKefT4RL3cvCofJveLusGMfSeLNr2b0OsKBIC/.yWwbSIClK', true, NULL, 'standard')
ON CONFLICT DO NOTHING;

-- Sample artists
INSERT INTO artist (name, band, image) VALUES
('2Pac', NULL, 'image_url_2pac'),
('A', NULL, 'image_url_a'),
('Adele', NULL, 'image_url_adele'),
('Aerosmith', NULL, 'image_url_aerosmith'),
('Alabama Shakes', NULL, 'image_url_alabama_shakes'),
('Arctic Monkeys', NULL, 'image_url_arctic_monkeys'),
('Beyonce', NULL, 'image_url_beyonce'),
('Bob Dylan', NULL, 'image_url_bob_dylan'),
('Bruce Springsteen', NULL, 'image_url_bruce_springsteen'),
('Coldplay', NULL, 'image_url_coldplay')
ON CONFLICT DO NOTHING;

-- Link artists to default user (favorites)
INSERT INTO user_has_artist (user_id, artist_id)
SELECT u.id, a.id FROM "user" u, artist a
WHERE u.username = 'blake'
ON CONFLICT DO NOTHING;

-- Sample events (StubHub integration data)
INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT '2Pac Tribute Concert', 'Madison Square Garden', 'New York', 'NY', 'US', '2026-07-15 20:00:00', 'https://www.stubhub.com/event/123456', 75.00, 350.00, 'USD', 'stubhub', 'sh-123456', a.id
FROM artist a WHERE a.name = '2Pac'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Adele Live 2026', 'The O2 Arena', 'London', NULL, 'UK', '2026-08-20 19:30:00', 'https://www.stubhub.com/event/234567', 120.00, 800.00, 'USD', 'stubhub', 'sh-234567', a.id
FROM artist a WHERE a.name = 'Adele'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Adele - Las Vegas Residency', 'Caesars Palace', 'Las Vegas', 'NV', 'US', '2026-09-10 21:00:00', 'https://www.stubhub.com/event/234568', 200.00, 1500.00, 'USD', 'stubhub', 'sh-234568', a.id
FROM artist a WHERE a.name = 'Adele'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Aerosmith Farewell Tour', 'Fenway Park', 'Boston', 'MA', 'US', '2026-06-28 19:00:00', 'https://www.stubhub.com/event/345678', 90.00, 500.00, 'USD', 'stubhub', 'sh-345678', a.id
FROM artist a WHERE a.name = 'Aerosmith'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Alabama Shakes Reunion Show', 'Ryman Auditorium', 'Nashville', 'TN', 'US', '2026-10-05 20:00:00', 'https://www.stubhub.com/event/456789', 65.00, 250.00, 'USD', 'stubhub', 'sh-456789', a.id
FROM artist a WHERE a.name = 'Alabama Shakes'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Arctic Monkeys World Tour', 'Red Rocks Amphitheatre', 'Morrison', 'CO', 'US', '2026-08-15 19:30:00', 'https://www.stubhub.com/event/567890', 85.00, 400.00, 'USD', 'stubhub', 'sh-567890', a.id
FROM artist a WHERE a.name = 'Arctic Monkeys'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Beyonce Renaissance Tour', 'SoFi Stadium', 'Los Angeles', 'CA', 'US', '2026-11-01 20:00:00', 'https://www.stubhub.com/event/678901', 150.00, 2000.00, 'USD', 'stubhub', 'sh-678901', a.id
FROM artist a WHERE a.name = 'Beyonce'
ON CONFLICT DO NOTHING;

INSERT INTO event (name, venue, city, state, country, event_date, ticket_url, min_price, max_price, currency, source, external_id, artist_id)
SELECT 'Coldplay Music of the Spheres', 'Wembley Stadium', 'London', NULL, 'UK', '2026-07-25 18:00:00', 'https://www.stubhub.com/event/789012', 95.00, 600.00, 'USD', 'stubhub', 'sh-789012', a.id
FROM artist a WHERE a.name = 'Coldplay'
ON CONFLICT DO NOTHING;
