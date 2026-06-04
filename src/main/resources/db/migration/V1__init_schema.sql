-- ============================================================
-- V1 - Initial Schema
-- ============================================================

CREATE TABLE users (
    id               BIGSERIAL PRIMARY KEY,
    username         VARCHAR(50)  NOT NULL UNIQUE,
    email            VARCHAR(100) NOT NULL UNIQUE,
    password         VARCHAR(255) NOT NULL,
    role             VARCHAR(20)  NOT NULL,
    display_name     VARCHAR(100),
    avatar_name      VARCHAR(100),
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100)
);

CREATE TABLE leagues (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL UNIQUE,
    description      VARCHAR(500),
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100)
);

CREATE TABLE seasons (
    id                          BIGSERIAL PRIMARY KEY,
    league_id                   BIGINT       NOT NULL REFERENCES leagues(id),
    name                        VARCHAR(100) NOT NULL,
    status                      VARCHAR(20)  NOT NULL,
    first_match_at              TIMESTAMP,
    league_prediction_lock_at   TIMESTAMP,
    deleted                     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at                  TIMESTAMP    NOT NULL,
    updated_at                  TIMESTAMP,
    created_by                  VARCHAR(100),
    updated_by                  VARCHAR(100)
);

CREATE TABLE teams (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL UNIQUE,
    short_name       VARCHAR(10),
    logo_url         VARCHAR(255),
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100)
);

CREATE TABLE season_teams (
    id               BIGSERIAL PRIMARY KEY,
    season_id        BIGINT NOT NULL REFERENCES seasons(id),
    team_id          BIGINT NOT NULL REFERENCES teams(id),
    deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    UNIQUE (season_id, team_id)
);

CREATE TABLE players (
    id               BIGSERIAL PRIMARY KEY,
    team_id          BIGINT       NOT NULL REFERENCES teams(id),
    name             VARCHAR(100) NOT NULL,
    role             VARCHAR(50),
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100)
);

CREATE TABLE matches (
    id               BIGSERIAL PRIMARY KEY,
    season_id        BIGINT       NOT NULL REFERENCES seasons(id),
    team1_id         BIGINT       NOT NULL REFERENCES teams(id),
    team2_id         BIGINT       NOT NULL REFERENCES teams(id),
    scheduled_at     TIMESTAMP    NOT NULL,
    lock_at          TIMESTAMP    NOT NULL,
    venue            VARCHAR(200),
    status           VARCHAR(20)  NOT NULL,
    match_number     INT,
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100)
);

CREATE TABLE match_results (
    id                   BIGSERIAL PRIMARY KEY,
    match_id             BIGINT NOT NULL UNIQUE REFERENCES matches(id),
    winner_team_id       BIGINT REFERENCES teams(id),
    is_tie               BOOLEAN NOT NULL DEFAULT FALSE,
    toss_winner_team_id  BIGINT REFERENCES teams(id),
    player_of_match_id   BIGINT REFERENCES players(id),
    published_by         BIGINT NOT NULL REFERENCES users(id),
    published_at         TIMESTAMP NOT NULL,
    deleted              BOOLEAN NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMP NOT NULL,
    updated_at           TIMESTAMP,
    created_by           VARCHAR(100),
    updated_by           VARCHAR(100)
);

CREATE TABLE league_predictions (
    id                  BIGSERIAL PRIMARY KEY,
    season_id           BIGINT NOT NULL REFERENCES seasons(id),
    user_id             BIGINT NOT NULL REFERENCES users(id),
    team_id             BIGINT NOT NULL REFERENCES teams(id),
    predicted_position  INT    NOT NULL,
    deleted             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP,
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    UNIQUE (season_id, user_id, team_id)
);

CREATE TABLE match_predictions (
    id                              BIGSERIAL PRIMARY KEY,
    match_id                        BIGINT NOT NULL REFERENCES matches(id),
    user_id                         BIGINT NOT NULL REFERENCES users(id),
    predicted_winner_id             BIGINT REFERENCES teams(id),
    predicted_toss_winner_id        BIGINT REFERENCES teams(id),
    predicted_player_of_match_id    BIGINT REFERENCES players(id),
    deleted                         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at                      TIMESTAMP NOT NULL,
    updated_at                      TIMESTAMP,
    created_by                      VARCHAR(100),
    updated_by                      VARCHAR(100),
    UNIQUE (match_id, user_id)
);

CREATE TABLE email_logs (
    id               BIGSERIAL PRIMARY KEY,
    to_address       VARCHAR(255) NOT NULL,
    subject          VARCHAR(255) NOT NULL,
    body             TEXT,
    type             VARCHAR(30)  NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    sent_at          TIMESTAMP,
    failure_reason   TEXT,
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL,
    updated_at       TIMESTAMP,
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100)
);

-- Indexes for common query patterns
CREATE INDEX idx_seasons_league_id       ON seasons(league_id);
CREATE INDEX idx_matches_season_id       ON matches(season_id);
CREATE INDEX idx_players_team_id         ON players(team_id);
CREATE INDEX idx_season_teams_season_id  ON season_teams(season_id);
CREATE INDEX idx_league_pred_season_user ON league_predictions(season_id, user_id);
CREATE INDEX idx_match_pred_match_user   ON match_predictions(match_id, user_id);
CREATE INDEX idx_email_logs_status       ON email_logs(status);
