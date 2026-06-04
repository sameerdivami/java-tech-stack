-- ============================================================
-- V2 - Seed Data (all passwords = Test@123)
-- ============================================================

-- Users
INSERT INTO users (username, email, password, role, display_name, avatar_name, active, deleted, created_at, created_by)
VALUES
    ('testadmin', 'testadmin@familyleague.com', '$2y$10$F8hlc9iuoPizwhp4hkM1b.7nHpLWHgk0Q/WnLQ/khMACy2iYwoWWO', 'ADMIN', 'Test Admin', 'admin',   true, false, '2026-01-01 00:00:00', 'seed'),
    ('alice',     'alice@familyleague.com',     '$2y$10$F8hlc9iuoPizwhp4hkM1b.7nHpLWHgk0Q/WnLQ/khMACy2iYwoWWO', 'USER',  'Alice',      'alice',   true, false, '2026-01-01 00:00:00', 'seed'),
    ('bob',       'bob@familyleague.com',       '$2y$10$F8hlc9iuoPizwhp4hkM1b.7nHpLWHgk0Q/WnLQ/khMACy2iYwoWWO', 'USER',  'Bob',        'bob',     true, false, '2026-01-01 00:00:00', 'seed'),
    ('charlie',   'charlie@familyleague.com',   '$2y$10$F8hlc9iuoPizwhp4hkM1b.7nHpLWHgk0Q/WnLQ/khMACy2iYwoWWO', 'USER',  'Charlie',    'charlie', true, false, '2026-01-01 00:00:00', 'seed'),
    ('diana',     'diana@familyleague.com',     '$2y$10$F8hlc9iuoPizwhp4hkM1b.7nHpLWHgk0Q/WnLQ/khMACy2iYwoWWO', 'USER',  'Diana',      'diana',   true, false, '2026-01-01 00:00:00', 'seed'),
    ('eve',       'eve@familyleague.com',       '$2y$10$F8hlc9iuoPizwhp4hkM1b.7nHpLWHgk0Q/WnLQ/khMACy2iYwoWWO', 'USER',  'Eve',        'eve',     true, false, '2026-01-01 00:00:00', 'seed');

-- League
INSERT INTO leagues (name, description, deleted, created_at, created_by)
VALUES ('Family Premier League', 'Internal family cricket prediction league', false, '2026-01-01 00:00:00', 'seed');

-- Teams
INSERT INTO teams (name, short_name, deleted, created_at, created_by)
VALUES
    ('Mumbai Lions',     'ML', false, '2026-01-01 00:00:00', 'seed'),
    ('Chennai Kings',    'CK', false, '2026-01-01 00:00:00', 'seed'),
    ('Delhi Daredevils', 'DD', false, '2026-01-01 00:00:00', 'seed'),
    ('Kolkata Knights',  'KK', false, '2026-01-01 00:00:00', 'seed'),
    ('Bangalore Royals', 'BR', false, '2026-01-01 00:00:00', 'seed'),
    ('Hyderabad Risers', 'HR', false, '2026-01-01 00:00:00', 'seed'),
    ('Punjab Warriors',  'PW', false, '2026-01-01 00:00:00', 'seed'),
    ('Rajasthan Riders', 'RR', false, '2026-01-01 00:00:00', 'seed');

-- Season
INSERT INTO seasons (league_id, name, status, first_match_at, league_prediction_lock_at, deleted, created_at, created_by)
VALUES (
    (SELECT id FROM leagues WHERE name = 'Family Premier League'),
    'Season 2026', 'ACTIVE',
    '2026-05-01 19:30:00',
    '2026-05-01 15:30:00',
    false, '2026-01-01 00:00:00', 'seed'
);

-- Season Teams (all 8 teams)
INSERT INTO season_teams (season_id, team_id, deleted, created_at, created_by)
SELECT
    (SELECT id FROM seasons WHERE name = 'Season 2026'),
    id, false, '2026-01-01 00:00:00', 'seed'
FROM teams;

-- Players (11 per team, all names unique across the dataset)
INSERT INTO players (team_id, name, role, active, deleted, created_at, created_by)
SELECT t.id, p.name, p.role, true, false, '2026-01-01 00:00:00', 'seed'
FROM (VALUES
    -- Mumbai Lions
    ('Mumbai Lions',     'Rohit Sharma',          'BATSMAN'),
    ('Mumbai Lions',     'Suryakumar Yadav',       'BATSMAN'),
    ('Mumbai Lions',     'Tilak Varma',            'BATSMAN'),
    ('Mumbai Lions',     'Tim David',              'BATSMAN'),
    ('Mumbai Lions',     'Ishan Kishan',           'WICKET_KEEPER'),
    ('Mumbai Lions',     'Hardik Pandya',          'ALL_ROUNDER'),
    ('Mumbai Lions',     'Naman Dhir',             'ALL_ROUNDER'),
    ('Mumbai Lions',     'Jasprit Bumrah',         'BOWLER'),
    ('Mumbai Lions',     'Piyush Chawla',          'BOWLER'),
    ('Mumbai Lions',     'Jason Behrendorff',      'BOWLER'),
    ('Mumbai Lions',     'Arjun Tendulkar',        'BOWLER'),

    -- Chennai Kings
    ('Chennai Kings',    'Ruturaj Gaikwad',        'BATSMAN'),
    ('Chennai Kings',    'Devon Conway',           'BATSMAN'),
    ('Chennai Kings',    'Rachin Ravindra',        'BATSMAN'),
    ('Chennai Kings',    'Ajinkya Rahane',         'BATSMAN'),
    ('Chennai Kings',    'MS Dhoni',               'WICKET_KEEPER'),
    ('Chennai Kings',    'Ravindra Jadeja',        'ALL_ROUNDER'),
    ('Chennai Kings',    'Shivam Dube',            'ALL_ROUNDER'),
    ('Chennai Kings',    'Daryl Mitchell',         'ALL_ROUNDER'),
    ('Chennai Kings',    'Deepak Chahar',          'BOWLER'),
    ('Chennai Kings',    'Maheesh Theekshana',     'BOWLER'),
    ('Chennai Kings',    'Tushar Deshpande',       'BOWLER'),

    -- Delhi Daredevils
    ('Delhi Daredevils', 'David Warner',           'BATSMAN'),
    ('Delhi Daredevils', 'Prithvi Shaw',           'BATSMAN'),
    ('Delhi Daredevils', 'Sarfaraz Khan',          'BATSMAN'),
    ('Delhi Daredevils', 'Shai Hope',              'BATSMAN'),
    ('Delhi Daredevils', 'Rishabh Pant',           'WICKET_KEEPER'),
    ('Delhi Daredevils', 'Axar Patel',             'ALL_ROUNDER'),
    ('Delhi Daredevils', 'Mitchell Marsh',         'ALL_ROUNDER'),
    ('Delhi Daredevils', 'Anrich Nortje',          'BOWLER'),
    ('Delhi Daredevils', 'Kuldeep Yadav',          'BOWLER'),
    ('Delhi Daredevils', 'Ishant Sharma',          'BOWLER'),
    ('Delhi Daredevils', 'Mukesh Kumar',           'BOWLER'),

    -- Kolkata Knights
    ('Kolkata Knights',  'Shreyas Iyer',           'BATSMAN'),
    ('Kolkata Knights',  'Nitish Rana',            'BATSMAN'),
    ('Kolkata Knights',  'Rinku Singh',            'BATSMAN'),
    ('Kolkata Knights',  'Angkrish Raghuvanshi',   'BATSMAN'),
    ('Kolkata Knights',  'Dinesh Karthik',         'WICKET_KEEPER'),
    ('Kolkata Knights',  'Andre Russell',          'ALL_ROUNDER'),
    ('Kolkata Knights',  'Sunil Narine',           'ALL_ROUNDER'),
    ('Kolkata Knights',  'Ramandeep Singh',        'ALL_ROUNDER'),
    ('Kolkata Knights',  'Varun Chakravarthy',     'BOWLER'),
    ('Kolkata Knights',  'Lockie Ferguson',        'BOWLER'),
    ('Kolkata Knights',  'Harshit Rana',           'BOWLER'),

    -- Bangalore Royals
    ('Bangalore Royals', 'Virat Kohli',            'BATSMAN'),
    ('Bangalore Royals', 'Faf du Plessis',         'BATSMAN'),
    ('Bangalore Royals', 'Rajat Patidar',          'BATSMAN'),
    ('Bangalore Royals', 'Cameron Green',          'BATSMAN'),
    ('Bangalore Royals', 'Anuj Rawat',             'WICKET_KEEPER'),
    ('Bangalore Royals', 'Glenn Maxwell',          'ALL_ROUNDER'),
    ('Bangalore Royals', 'Mahipal Lomror',         'ALL_ROUNDER'),
    ('Bangalore Royals', 'Mohammed Siraj',         'BOWLER'),
    ('Bangalore Royals', 'Harshal Patel',          'BOWLER'),
    ('Bangalore Royals', 'Wanindu Hasaranga',      'BOWLER'),
    ('Bangalore Royals', 'Josh Hazlewood',         'BOWLER'),

    -- Hyderabad Risers
    ('Hyderabad Risers', 'Travis Head',            'BATSMAN'),
    ('Hyderabad Risers', 'Abhishek Sharma',        'BATSMAN'),
    ('Hyderabad Risers', 'Aiden Markram',          'BATSMAN'),
    ('Hyderabad Risers', 'Mayank Agarwal',         'BATSMAN'),
    ('Hyderabad Risers', 'Heinrich Klaasen',       'WICKET_KEEPER'),
    ('Hyderabad Risers', 'Pat Cummins',            'ALL_ROUNDER'),
    ('Hyderabad Risers', 'Marco Jansen',           'ALL_ROUNDER'),
    ('Hyderabad Risers', 'Shahbaz Ahmed',          'ALL_ROUNDER'),
    ('Hyderabad Risers', 'Bhuvneshwar Kumar',      'BOWLER'),
    ('Hyderabad Risers', 'T Natarajan',            'BOWLER'),
    ('Hyderabad Risers', 'Umran Malik',            'BOWLER'),

    -- Punjab Warriors
    ('Punjab Warriors',  'Shikhar Dhawan',         'BATSMAN'),
    ('Punjab Warriors',  'Prabhsimran Singh',      'BATSMAN'),
    ('Punjab Warriors',  'Shahrukh Khan',          'BATSMAN'),
    ('Punjab Warriors',  'Bhanuka Rajapaksa',      'BATSMAN'),
    ('Punjab Warriors',  'Jonny Bairstow',         'WICKET_KEEPER'),
    ('Punjab Warriors',  'Sam Curran',             'ALL_ROUNDER'),
    ('Punjab Warriors',  'Liam Livingstone',       'ALL_ROUNDER'),
    ('Punjab Warriors',  'Rishi Dhawan',           'ALL_ROUNDER'),
    ('Punjab Warriors',  'Arshdeep Singh',         'BOWLER'),
    ('Punjab Warriors',  'Kagiso Rabada',          'BOWLER'),
    ('Punjab Warriors',  'Nathan Ellis',           'BOWLER'),

    -- Rajasthan Riders
    ('Rajasthan Riders', 'Jos Buttler',            'BATSMAN'),
    ('Rajasthan Riders', 'Yashasvi Jaiswal',       'BATSMAN'),
    ('Rajasthan Riders', 'Devdutt Padikkal',       'BATSMAN'),
    ('Rajasthan Riders', 'Jason Roy',              'BATSMAN'),
    ('Rajasthan Riders', 'Sanju Samson',           'WICKET_KEEPER'),
    ('Rajasthan Riders', 'Ben Stokes',             'ALL_ROUNDER'),
    ('Rajasthan Riders', 'Riyan Parag',            'ALL_ROUNDER'),
    ('Rajasthan Riders', 'Ravichandran Ashwin',    'BOWLER'),
    ('Rajasthan Riders', 'Trent Boult',            'BOWLER'),
    ('Rajasthan Riders', 'Yuzvendra Chahal',       'BOWLER'),
    ('Rajasthan Riders', 'Kuldeep Sen',            'BOWLER')
) AS p(team_name, name, role)
JOIN teams t ON t.name = p.team_name;

-- Matches: 4 COMPLETED, 1 LOCKED, 5 UPCOMING
INSERT INTO matches (season_id, team1_id, team2_id, scheduled_at, lock_at, venue, status, match_number, deleted, created_at, created_by)
VALUES
    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Mumbai Lions'),
     (SELECT id FROM teams WHERE name = 'Chennai Kings'),
     '2026-05-01 19:30:00', '2026-05-01 18:30:00', 'Wankhede Stadium, Mumbai',          'COMPLETED', 1, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Delhi Daredevils'),
     (SELECT id FROM teams WHERE name = 'Kolkata Knights'),
     '2026-05-05 19:30:00', '2026-05-05 18:30:00', 'Arun Jaitley Stadium, Delhi',       'COMPLETED', 2, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Bangalore Royals'),
     (SELECT id FROM teams WHERE name = 'Hyderabad Risers'),
     '2026-05-10 15:30:00', '2026-05-10 14:30:00', 'M. Chinnaswamy Stadium, Bengaluru', 'COMPLETED', 3, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Punjab Warriors'),
     (SELECT id FROM teams WHERE name = 'Rajasthan Riders'),
     '2026-05-15 19:30:00', '2026-05-15 18:30:00', 'PCA Stadium, Mohali',               'COMPLETED', 4, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Mumbai Lions'),
     (SELECT id FROM teams WHERE name = 'Kolkata Knights'),
     '2026-06-04 19:30:00', '2026-06-04 18:30:00', 'Eden Gardens, Kolkata',             'LOCKED',    5, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Chennai Kings'),
     (SELECT id FROM teams WHERE name = 'Rajasthan Riders'),
     '2026-06-08 19:30:00', '2026-06-08 18:30:00', 'Chepauk Stadium, Chennai',          'UPCOMING',  6, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Delhi Daredevils'),
     (SELECT id FROM teams WHERE name = 'Hyderabad Risers'),
     '2026-06-12 19:30:00', '2026-06-12 18:30:00', 'Arun Jaitley Stadium, Delhi',       'UPCOMING',  7, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Bangalore Royals'),
     (SELECT id FROM teams WHERE name = 'Punjab Warriors'),
     '2026-06-16 19:30:00', '2026-06-16 18:30:00', 'M. Chinnaswamy Stadium, Bengaluru', 'UPCOMING',  8, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Kolkata Knights'),
     (SELECT id FROM teams WHERE name = 'Rajasthan Riders'),
     '2026-06-20 19:30:00', '2026-06-20 18:30:00', 'Eden Gardens, Kolkata',             'UPCOMING',  9, false, '2026-01-01 00:00:00', 'seed'),

    ((SELECT id FROM seasons WHERE name = 'Season 2026'),
     (SELECT id FROM teams WHERE name = 'Mumbai Lions'),
     (SELECT id FROM teams WHERE name = 'Hyderabad Risers'),
     '2026-06-25 19:30:00', '2026-06-25 18:30:00', 'Wankhede Stadium, Mumbai',          'UPCOMING', 10, false, '2026-01-01 00:00:00', 'seed');

-- Match Results (4 COMPLETED matches)
INSERT INTO match_results (match_id, winner_team_id, is_tie, toss_winner_team_id, player_of_match_id, published_by, published_at, deleted, created_at, created_by)
VALUES
    ((SELECT id FROM matches WHERE match_number = 1),
     (SELECT id FROM teams   WHERE name = 'Mumbai Lions'),
     false,
     (SELECT id FROM teams   WHERE name = 'Chennai Kings'),
     (SELECT id FROM players WHERE name = 'Rohit Sharma'),
     (SELECT id FROM users   WHERE username = 'testadmin'),
     '2026-05-01 23:00:00', false, '2026-05-01 23:00:00', 'seed'),

    ((SELECT id FROM matches WHERE match_number = 2),
     (SELECT id FROM teams   WHERE name = 'Kolkata Knights'),
     false,
     (SELECT id FROM teams   WHERE name = 'Kolkata Knights'),
     (SELECT id FROM players WHERE name = 'Andre Russell'),
     (SELECT id FROM users   WHERE username = 'testadmin'),
     '2026-05-05 23:00:00', false, '2026-05-05 23:00:00', 'seed'),

    ((SELECT id FROM matches WHERE match_number = 3),
     (SELECT id FROM teams   WHERE name = 'Hyderabad Risers'),
     false,
     (SELECT id FROM teams   WHERE name = 'Hyderabad Risers'),
     (SELECT id FROM players WHERE name = 'Pat Cummins'),
     (SELECT id FROM users   WHERE username = 'testadmin'),
     '2026-05-10 19:00:00', false, '2026-05-10 19:00:00', 'seed'),

    ((SELECT id FROM matches WHERE match_number = 4),
     (SELECT id FROM teams   WHERE name = 'Rajasthan Riders'),
     false,
     (SELECT id FROM teams   WHERE name = 'Rajasthan Riders'),
     (SELECT id FROM players WHERE name = 'Jos Buttler'),
     (SELECT id FROM users   WHERE username = 'testadmin'),
     '2026-05-15 23:00:00', false, '2026-05-15 23:00:00', 'seed');

-- League Predictions (3 users predict final standings for all 8 teams)
INSERT INTO league_predictions (season_id, user_id, team_id, predicted_position, deleted, created_at, created_by)
SELECT
    (SELECT id FROM seasons WHERE name = 'Season 2026'),
    u.id, t.id, lp.pos,
    false, '2026-04-30 10:00:00', 'seed'
FROM (VALUES
    ('alice',   'Mumbai Lions',      1),
    ('alice',   'Chennai Kings',     2),
    ('alice',   'Bangalore Royals',  3),
    ('alice',   'Kolkata Knights',   4),
    ('alice',   'Rajasthan Riders',  5),
    ('alice',   'Hyderabad Risers',  6),
    ('alice',   'Delhi Daredevils',  7),
    ('alice',   'Punjab Warriors',   8),

    ('bob',     'Chennai Kings',     1),
    ('bob',     'Kolkata Knights',   2),
    ('bob',     'Mumbai Lions',      3),
    ('bob',     'Hyderabad Risers',  4),
    ('bob',     'Rajasthan Riders',  5),
    ('bob',     'Bangalore Royals',  6),
    ('bob',     'Punjab Warriors',   7),
    ('bob',     'Delhi Daredevils',  8),

    ('charlie', 'Bangalore Royals',  1),
    ('charlie', 'Mumbai Lions',      2),
    ('charlie', 'Rajasthan Riders',  3),
    ('charlie', 'Delhi Daredevils',  4),
    ('charlie', 'Chennai Kings',     5),
    ('charlie', 'Kolkata Knights',   6),
    ('charlie', 'Hyderabad Risers',  7),
    ('charlie', 'Punjab Warriors',   8)
) AS lp(username, team_name, pos)
JOIN users u ON u.username = lp.username
JOIN teams t ON t.name     = lp.team_name;

-- Match Predictions (3 users predict outcomes for completed + upcoming matches)
INSERT INTO match_predictions (match_id, user_id, predicted_winner_id, predicted_toss_winner_id, predicted_player_of_match_id, deleted, created_at, created_by)
SELECT
    m.id, u.id, tw.id, tt.id, pl.id,
    false,
    m.scheduled_at - INTERVAL '2 hours',
    'seed'
FROM (VALUES
    ('alice',   1, 'Mumbai Lions',     'Chennai Kings',    'Rohit Sharma'),
    ('alice',   2, 'Delhi Daredevils', 'Kolkata Knights',  'Shreyas Iyer'),
    ('alice',   3, 'Bangalore Royals', 'Hyderabad Risers', 'Virat Kohli'),
    ('alice',   4, 'Punjab Warriors',  'Rajasthan Riders', 'Sam Curran'),
    ('alice',   6, 'Chennai Kings',    'Chennai Kings',    'MS Dhoni'),
    ('alice',   7, 'Delhi Daredevils', 'Delhi Daredevils', 'Rishabh Pant'),

    ('bob',     1, 'Chennai Kings',    'Chennai Kings',    'MS Dhoni'),
    ('bob',     2, 'Kolkata Knights',  'Kolkata Knights',  'Andre Russell'),
    ('bob',     3, 'Hyderabad Risers', 'Hyderabad Risers', 'Pat Cummins'),
    ('bob',     4, 'Rajasthan Riders', 'Punjab Warriors',  'Jos Buttler'),
    ('bob',     6, 'Rajasthan Riders', 'Rajasthan Riders', 'Sanju Samson'),
    ('bob',     8, 'Bangalore Royals', 'Bangalore Royals', 'Glenn Maxwell'),

    ('charlie', 1, 'Mumbai Lions',     'Mumbai Lions',     'Jasprit Bumrah'),
    ('charlie', 2, 'Kolkata Knights',  'Delhi Daredevils', 'Varun Chakravarthy'),
    ('charlie', 3, 'Bangalore Royals', 'Bangalore Royals', 'Glenn Maxwell'),
    ('charlie', 4, 'Punjab Warriors',  'Rajasthan Riders', 'Arshdeep Singh'),
    ('charlie', 7, 'Hyderabad Risers', 'Hyderabad Risers', 'Abhishek Sharma'),
    ('charlie', 9, 'Rajasthan Riders', 'Kolkata Knights',  'Yashasvi Jaiswal')
) AS mp(username, match_num, winner, toss_winner, potm)
JOIN users   u  ON u.username     = mp.username
JOIN matches m  ON m.match_number = mp.match_num
JOIN teams   tw ON tw.name        = mp.winner
JOIN teams   tt ON tt.name        = mp.toss_winner
JOIN players pl ON pl.name        = mp.potm;
