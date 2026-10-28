-- Usuarios de ejemplo. Contraseñas: hunter1 a hunter5 (guardadas con hash BCrypt).
-- No tienen token: el token se genera cuando un usuario se registra por la API.

INSERT INTO USERAPI (id_user, name, email, password, created, modified, last_login, token, is_active) VALUES
('6e8b6014-2c70-478d-9bc1-e50deb22f46b', 'Jhon Lizcano', 'jhon@lizcano.org',
 '$2a$10$UcIAYwz9xqZ8Tj49p19LU.Rcb/RbxwR1.Il1MKQ1x31TA4PuU5puu', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, TRUE),
('ac975290-fbfa-4452-9507-0c98ffab8a3b', 'Arledis Jaimes', 'antonella@lizcano.com',
 '$2a$10$IJ0wYWNFXorCn3qDqMTiyenndG55HnaU9wZHSxUk.Ib6CHZo48dGK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, TRUE),
('f9f9547e-8a8a-4292-a1ab-5e3285e39678', 'Karent Lizcano', 'karent@lizcano.co',
 '$2a$10$Bu6S4APeJVVOf591LR6R.udn6Nzq/gLrXPK/5YpWgiCD0chBuq.7m', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, TRUE),
('a266e990-f96e-44e8-b8fb-36a35be693f3', 'Ana Parra', 'ana.parra@gmail.com',
 '$2a$10$oABZZ497.SEjIe.s9WGNeeZqAvrjMFU92de0GH5V9qSwPqhcgCN8a', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, TRUE),
('a10b3f00-48c9-4151-a3ac-d39df04c55c2', 'Edgar Lizcano', 'edgar@hotmail.com',
 '$2a$10$XLwbWf8vI09Dy3FhKqxJ5.hAEC/l31LE/7h7vf8sDafE3y.N95cvW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, TRUE);

INSERT INTO PHONES (number, city_code, country_code, id_user) VALUES
('3001234567', '1', '57', '6e8b6014-2c70-478d-9bc1-e50deb22f46b'),
('3007654321', '1', '58', '6e8b6014-2c70-478d-9bc1-e50deb22f46b'),
('3101234567', '2', '59', 'ac975290-fbfa-4452-9507-0c98ffab8a3b'),
('3051234567', '2', '42', 'f9f9547e-8a8a-4292-a1ab-5e3285e39678'),
('3057654321', '3', '40', 'a266e990-f96e-44e8-b8fb-36a35be693f3'),
('3201234567', '3', '10', 'a10b3f00-48c9-4151-a3ac-d39df04c55c2');