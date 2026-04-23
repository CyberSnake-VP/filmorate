-- категории фильмов
-- такой merge работает только h2 в postgresql строже обязательно
MERGE INTO genres (genre_id, genre_name) VALUES (1, 'Комедия');
MERGE INTO genres (genre_id, genre_name) VALUES (2, 'Драма');
MERGE INTO genres (genre_id, genre_name) VALUES (3, 'Мультфильмы');
MERGE INTO genres (genre_id, genre_name) VALUES ( 4, 'Биография');
MERGE INTO genres (genre_id, genre_name) VALUES ( 5, 'Триллер');
MERGE INTO genres (genre_id, genre_name) VALUES (6, 'Документальный');
MERGE INTO genres (genre_id, genre_name) VALUES (7, 'Боевик');

MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (1, 'G');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (2, 'PG');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (3, 'PG-13');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (4, 'R');
MERGE INTO mpa_ratings (mpa_id, mpa_name) VALUES (5, 'NC-17');
