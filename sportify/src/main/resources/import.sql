INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at , deleted) VALUES (gen_random_uuid(), 'admin_user', '{bcrypt}$2a$12$q4eBcJphwLiFIb1pEM47wuokLamviOKc2BFv0HHfUqJ8NyPBlc9xa', 'Alice Johnson', '1985-03-10', 'admin@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), 0);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'regular_user', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'Jane Smith', '1995-08-20', 'user@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), 1);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'writer_user', '{bcrypt}$2a$12$wyIc2c9yMIA/EwfAcdJ2yeD0I0odnyJT0dKJM/udmw4VQmbuzWsnK', 'John Doe', '1990-05-15', 'writer@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), 2);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'user1', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'User 1', '1990-01-01', 'user1@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'user1'), 1);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'user2', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'User 2', '1990-01-02', 'user2@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'user2'), 2);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'user3', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'User 3', '1990-01-03', 'user3@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'user3'), 1);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'user4', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'User 4', '1990-01-04', 'user4@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'user4'), 2);
INSERT INTO users (id, username, password, nombre, fecha_nacimiento, email, enabled, activation_token, created_at, deleted) VALUES (gen_random_uuid(), 'user5', '{bcrypt}$2a$12$zKYqTeiCPvPHMdKUo6VAp.G66i.y9SGGqzpv73vqp6c1QjEx7c83O', 'User 5', '1990-01-05', 'user5@example.com', true, null, NOW(), false); INSERT INTO user_roles (user_id, roles) VALUES ((SELECT id FROM users WHERE username = 'user5'), 0);

INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 1', 'titular-de-la-noticia-1', 'Cuerpo de la noticia 1. Este es el contenido de la noticia.', NOW() - INTERVAL '1 day', (SELECT id FROM users WHERE username = 'writer_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 2', 'titular-de-la-noticia-2', 'Cuerpo de la noticia 2. Este es el contenido de la noticia.', NOW() - INTERVAL '2 days', (SELECT id FROM users WHERE username = 'admin_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 3', 'titular-de-la-noticia-3', 'Cuerpo de la noticia 3. Este es el contenido de la noticia.', NOW() - INTERVAL '3 days', (SELECT id FROM users WHERE username = 'writer_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 4', 'titular-de-la-noticia-4', 'Cuerpo de la noticia 4. Este es el contenido de la noticia.', NOW() - INTERVAL '4 days', (SELECT id FROM users WHERE username = 'writer_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 5', 'titular-de-la-noticia-5', 'Cuerpo de la noticia 5. Este es el contenido de la noticia.', NOW() - INTERVAL '5 days', (SELECT id FROM users WHERE username = 'admin_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 6', 'titular-de-la-noticia-6', 'Cuerpo de la noticia 6. Este es el contenido de la noticia.', NOW() - INTERVAL '6 days', (SELECT id FROM users WHERE username = 'writer_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 7', 'titular-de-la-noticia-7', 'Cuerpo de la noticia 7. Este es el contenido de la noticia.', NOW() - INTERVAL '7 days', (SELECT id FROM users WHERE username = 'writer_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 8', 'titular-de-la-noticia-8', 'Cuerpo de la noticia 8. Este es el contenido de la noticia.', NOW() - INTERVAL '8 days', (SELECT id FROM users WHERE username = 'admin_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 9', 'titular-de-la-noticia-9', 'Cuerpo de la noticia 9. Este es el contenido de la noticia.', NOW() - INTERVAL '9 days', (SELECT id FROM users WHERE username = 'writer_user'));
INSERT INTO noticia (id, titular, slug, cuerpo, fecha_publicacion, autor_id) VALUES (gen_random_uuid(), 'Titular de la noticia 10', 'titular-de-la-noticia-10', 'Cuerpo de la noticia 10. Este es el contenido de la noticia.', NOW() - INTERVAL '10 days', (SELECT id FROM users WHERE username = 'writer_user'));

INSERT INTO deporte (id, nombre, descripcion) VALUES (gen_random_uuid(), 'Futbol', 'Deporte de equipo jugado entre dos equipos de once jugadores cada uno, con una pelota esférica.');
INSERT INTO deporte (id, nombre, descripcion) VALUES (gen_random_uuid(), 'Baloncesto', 'Deporte de equipo en el que dos equipos de cinco jugadores compiten por anotar puntos lanzando una pelota a través de un aro.');
INSERT INTO deporte (id, nombre, descripcion) VALUES (gen_random_uuid(), 'Tenis', 'Deporte de raqueta jugado entre dos jugadores (individuales) o dos parejas (dobles), en una cancha rectangular dividida por una red.');
INSERT INTO deporte (id, nombre, descripcion) VALUES (gen_random_uuid(), 'Atletismo', 'Conjunto de disciplinas deportivas que incluyen carreras, saltos, lanzamientos y pruebas combinadas.');
INSERT INTO deporte (id, nombre, descripcion) VALUES (gen_random_uuid(), 'Natacion', 'Deporte acuático que consiste en recorrer una distancia en el agua en el menor tiempo posible, utilizando diferentes estilos.');

INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'LaLiga EaSports', 'Primera liga de Futbol.', (SELECT id FROM deporte WHERE nombre = 'Futbol'), 'laliga-easports');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Futbol 2', 'Segunda liga de Futbol.', (SELECT id FROM deporte WHERE nombre = 'Futbol'), 'liga-futbol-2');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Futbol 3', 'Tercera liga de Futbol.', (SELECT id FROM deporte WHERE nombre = 'Futbol'), 'liga-futbol-3');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Futbol 4', 'Cuarta liga de Futbol.', (SELECT id FROM deporte WHERE nombre = 'Futbol'), 'liga-futbol-4');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Futbol 5', 'Quinta liga de Futbol.', (SELECT id FROM deporte WHERE nombre = 'Futbol'), 'liga-futbol-5');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Baloncesto 1', 'Primera liga de Baloncesto.', (SELECT id FROM deporte WHERE nombre = 'Baloncesto'), 'liga-baloncesto-1');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Baloncesto 2', 'Segunda liga de Baloncesto.', (SELECT id FROM deporte WHERE nombre = 'Baloncesto'), 'liga-baloncesto-2');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Baloncesto 3', 'Tercera liga de Baloncesto.', (SELECT id FROM deporte WHERE nombre = 'Baloncesto'), 'liga-baloncesto-3');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Baloncesto 4', 'Cuarta liga de Baloncesto.', (SELECT id FROM deporte WHERE nombre = 'Baloncesto'), 'liga-baloncesto-4');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Baloncesto 5', 'Quinta liga de Baloncesto.', (SELECT id FROM deporte WHERE nombre = 'Baloncesto'), 'liga-baloncesto-5');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Tenis 1', 'Primera liga de Tenis.', (SELECT id FROM deporte WHERE nombre = 'Tenis'), 'liga-tenis-1');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Tenis 2', 'Segunda liga de Tenis.', (SELECT id FROM deporte WHERE nombre = 'Tenis'), 'liga-tenis-2');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Tenis 3', 'Tercera liga de Tenis.', (SELECT id FROM deporte WHERE nombre = 'Tenis'), 'liga-tenis-3');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Tenis 4', 'Cuarta liga de Tenis.', (SELECT id FROM deporte WHERE nombre = 'Tenis'), 'liga-tenis-4');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Tenis 5', 'Quinta liga de Tenis.', (SELECT id FROM deporte WHERE nombre = 'Tenis'), 'liga-tenis-5');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Atletismo 1', 'Primera liga de Atletismo.', (SELECT id FROM deporte WHERE nombre = 'Atletismo'), 'liga-atletismo-1');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Atletismo 2', 'Segunda liga de Atletismo.', (SELECT id FROM deporte WHERE nombre = 'Atletismo'), 'liga-atletismo-2');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Atletismo 3', 'Tercera liga de Atletismo.', (SELECT id FROM deporte WHERE nombre = 'Atletismo'), 'liga-atletismo-3');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Atletismo 4', 'Cuarta liga de Atletismo.', (SELECT id FROM deporte WHERE nombre = 'Atletismo'), 'liga-atletismo-4');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Atletismo 5', 'Quinta liga de Atletismo.', (SELECT id FROM deporte WHERE nombre = 'Atletismo'), 'liga-atletismo-5');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Natacion 1', 'Primera liga de Natacion.', (SELECT id FROM deporte WHERE nombre = 'Natacion'), 'liga-natacion-1');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Natacion 2', 'Segunda liga de Natacion.', (SELECT id FROM deporte WHERE nombre = 'Natacion'), 'liga-natacion-2');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Natacion 3', 'Tercera liga de Natacion.', (SELECT id FROM deporte WHERE nombre = 'Natacion'), 'liga-natacion-3');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Natacion 4', 'Cuarta liga de Natacion.', (SELECT id FROM deporte WHERE nombre = 'Natacion'), 'liga-natacion-4');
INSERT INTO liga (id, nombre, descripcion, deporte_id, nombre_no_espacio) VALUES (gen_random_uuid(), 'Liga Natacion 5', 'Quinta liga de Natacion.', (SELECT id FROM deporte WHERE nombre = 'Natacion'), 'liga-natacion-5');

INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Real Madrid', 'real-madrid', 'Madrid', 'España', 'real_madrid.png', '1902-03-06', (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'FC Barcelona', 'fc-barcelona', 'Barcelona', 'España', 'fc_barcelona.png', '1899-11-29', (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Atletico Madrid', 'atletico-madrid', 'Madrid', 'España', 'atletico_madrid.png', '1903-04-26', (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Valencia CF', 'valencia-cf', 'Valencia', 'España', 'valencia_cf.png', '1919-03-18', (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Sevilla FC', 'sevilla-fc', 'Sevilla', 'España', 'sevilla_fc.png', '1890-01-25', (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports'));

INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Los Angeles Lakers', 'los-angeles-lakers', 'Los Angeles', 'EEUU', 'lakers.png', '1947-06-06', (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Chicago Bulls', 'chicago-bulls', 'Chicago', 'EEUU', 'bulls.png', '1966-01-16', (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Miami Heat', 'miami-heat', 'Miami', 'EEUU', 'heat.png', '1988-11-05', (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Golden State Warriors', 'golden-state-warriors', 'San Francisco', 'EEUU', 'warriors.png', '1946-06-06', (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Boston Celtics', 'boston-celtics', 'Boston', 'EEUU', 'celtics.png', '1946-06-06', (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1'));

INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Roger Federer Academy', 'roger-federer-academy', 'Basilea', 'Suiza', 'federer_academy.png', '2010-04-05', (SELECT id FROM liga WHERE nombre = 'Liga Tenis 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Djokovic Center', 'djokovic-center', 'Belgrado', 'Serbia', 'djokovic_center.png', '2014-03-22', (SELECT id FROM liga WHERE nombre = 'Liga Tenis 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Murray Tennis Club', 'murray-tennis-club', 'Dunblane', 'Escocia', 'murray_club.png', '2012-09-10', (SELECT id FROM liga WHERE nombre = 'Liga Tenis 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Serena Williams Academy', 'serena-williams-academy', 'Florida', 'EEUU', 'serena_academy.png', '2013-06-08', (SELECT id FROM liga WHERE nombre = 'Liga Tenis 1'));

INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Jamaican Sprinters', 'jamaican-sprinters', 'Kingston', 'Jamaica', 'jamaican_sprinters.png', '2000-05-15', (SELECT id FROM liga WHERE nombre = 'Liga Atletismo 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'US Track Stars', 'us-track-stars', 'Los Angeles', 'EEUU', 'us_track_stars.png', '1995-07-30', (SELECT id FROM liga WHERE nombre = 'Liga Atletismo 1'));

INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Swim USA', 'swim-usa', 'Washington', 'EEUU', 'swim_usa.png', '2000-01-01', (SELECT id FROM liga WHERE nombre = 'Liga Natacion 1'));
INSERT INTO equipo (id, nombre, nombre_no_espacio, ciudad, pais, escudo, fecha_creacion, liga_id) VALUES (gen_random_uuid(), 'Aussie Dolphins', 'aussie-dolphins', 'Sídney', 'Australia', 'aussie_dolphins.png', '1998-05-22', (SELECT id FROM liga WHERE nombre = 'Liga Natacion 1'));


-- Asociaciones entre usuarios y equipos seguidos
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM equipo WHERE nombre = 'Real Madrid'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM equipo WHERE nombre = 'FC Barcelona'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM equipo WHERE nombre = 'Atletico Madrid'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM equipo WHERE nombre = 'Valencia CF'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM equipo WHERE nombre = 'Sevilla FC'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM equipo WHERE nombre = 'Los Angeles Lakers'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'user3'), (SELECT id FROM equipo WHERE nombre = 'Chicago Bulls'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'user4'), (SELECT id FROM equipo WHERE nombre = 'Miami Heat'));
INSERT INTO usuario_equipo (usuario_id, equipo_id) VALUES ((SELECT id FROM users WHERE username = 'user5'), (SELECT id FROM equipo WHERE nombre = 'Golden State Warriors'));

-- Asociaciones entre usuarios y deportes seguidos
INSERT INTO usuario_deporte (usuario_id, deporte_id) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM deporte WHERE nombre = 'Futbol'));
INSERT INTO usuario_deporte (usuario_id, deporte_id) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM deporte WHERE nombre = 'Baloncesto'));
INSERT INTO usuario_deporte (usuario_id, deporte_id) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM deporte WHERE nombre = 'Tenis'));
INSERT INTO usuario_deporte (usuario_id, deporte_id) VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM deporte WHERE nombre = 'Atletismo'));
INSERT INTO usuario_deporte (usuario_id, deporte_id) VALUES ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM deporte WHERE nombre = 'Natacion'));

-- Asociaciones entre usuarios y ligas seguidas
INSERT INTO usuario_liga (usuario_id, liga_id) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports'));
INSERT INTO usuario_liga (usuario_id, liga_id) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1'));
INSERT INTO usuario_liga (usuario_id, liga_id) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM liga WHERE nombre = 'Liga Tenis 1'));
INSERT INTO usuario_liga (usuario_id, liga_id) VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM liga WHERE nombre = 'Liga Atletismo 1'));
INSERT INTO usuario_liga (usuario_id, liga_id) VALUES ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM liga WHERE nombre = 'Liga Natacion 1'));

-- Asociaciones entre noticias y deportes
UPDATE noticia SET deporte_id = (SELECT id FROM deporte WHERE nombre = 'Futbol') WHERE titular = 'Titular de la noticia 1';
UPDATE noticia SET deporte_id = (SELECT id FROM deporte WHERE nombre = 'Baloncesto') WHERE titular = 'Titular de la noticia 2';
UPDATE noticia SET deporte_id = (SELECT id FROM deporte WHERE nombre = 'Tenis') WHERE titular = 'Titular de la noticia 3';
UPDATE noticia SET deporte_id = (SELECT id FROM deporte WHERE nombre = 'Atletismo') WHERE titular = 'Titular de la noticia 4';
UPDATE noticia SET deporte_id = (SELECT id FROM deporte WHERE nombre = 'Natacion') WHERE titular = 'Titular de la noticia 5';

-- Asociaciones entre noticias y equipos
UPDATE noticia SET equipo_id = (SELECT id FROM equipo WHERE nombre = 'Real Madrid') WHERE titular = 'Titular de la noticia 1';
UPDATE noticia SET equipo_id = (SELECT id FROM equipo WHERE nombre = 'Los Angeles Lakers') WHERE titular = 'Titular de la noticia 2';
UPDATE noticia SET equipo_id = (SELECT id FROM equipo WHERE nombre = 'Roger Federer Academy') WHERE titular = 'Titular de la noticia 3';
UPDATE noticia SET equipo_id = (SELECT id FROM equipo WHERE nombre = 'Jamaican Sprinters') WHERE titular = 'Titular de la noticia 4';
UPDATE noticia SET equipo_id = (SELECT id FROM equipo WHERE nombre = 'Swim USA') WHERE titular = 'Titular de la noticia 5';

-- Asociaciones entre noticias y ligas
UPDATE noticia SET liga_id = (SELECT id FROM liga WHERE nombre = 'LaLiga EaSports') WHERE titular = 'Titular de la noticia 1';
UPDATE noticia SET liga_id = (SELECT id FROM liga WHERE nombre = 'Liga Baloncesto 1') WHERE titular = 'Titular de la noticia 2';
UPDATE noticia SET liga_id = (SELECT id FROM liga WHERE nombre = 'Liga Tenis 1') WHERE titular = 'Titular de la noticia 3';
UPDATE noticia SET liga_id = (SELECT id FROM liga WHERE nombre = 'Liga Atletismo 1') WHERE titular = 'Titular de la noticia 4';
UPDATE noticia SET liga_id = (SELECT id FROM liga WHERE nombre = 'Liga Natacion 1') WHERE titular = 'Titular de la noticia 5';

-- Asociaciones entre comentarios y usuarios
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 1'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 1'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 1'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 2'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 2'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 2'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 3'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 3'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 3'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 4'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 4'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 4'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 5'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 5'),'Gran noticia', 'Me encantó esta noticia.' );
INSERT INTO comentario (fecha_comentario,  id_usuario_comentario,id_noticia_comentario, comentario, titulo) VALUES ( NOW() ,(SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 5'),'Gran noticia', 'Me encantó esta noticia.' );

-- Asociaciones entre likes y usuarios
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 1'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 2'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'admin_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 3'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 1'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 2'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'writer_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 3'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 1'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 2'));
INSERT INTO like_user (id_usuario_like, id_noticia_like) VALUES ((SELECT id FROM users WHERE username = 'regular_user'), (SELECT id FROM noticia WHERE titular = 'Titular de la noticia 3'));