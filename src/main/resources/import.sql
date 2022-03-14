-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

--insert books

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(1, 'J. K. Rowling', 'Harry Potter se ha quedado huérfano y vive en casa de sus abominables tíos..', '2001', '["Juvenile Fiction"]', 
'http://books.google.com/books/content?id=p3QQjwEACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api', '8498382661', 254, 5, 'Harry Potter', 'Harry Potter y la piedra filosofal', '1');

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(2, 'J. K. Rowling', 'Tras derrotar una vez más a lord Voldemort, su siniestro enemigo en Harry Potter..', '1999', '["Juvenile Fiction"]',
'http://books.google.com/books/content?id=_-hnPrimlnYC&printsec=frontcover&img=1&zoom=5&source=gbs_api', '8478884955', 286, 4, 'Harry Potter', 'Harry Potter y la cámara secreta', '2');

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(3, 'Sally Rooney', 'Después de Conversaciones entre amigos..', '2019-10-03', '["Fiction"]', 
'http://books.google.com/books/content?id=QcWrDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api', '8439736452', 256, 4, 'none', 'Gente normal', '1');

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(4, 'Brandon Sanderson', 'La aclamada continuación de El camino de los reyes es, como el primer volumen..', '2015-08-27', '["Fiction"]', 
'http://books.google.com/books/content?id=8w-YCgAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api', '0439064864', 1248, 4, 'El Archivo de las Tormentas', 'Palabras radiantes (El Archivo de las Tormentas 2)', '2');

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE,AUTHOR_ID)
VALUES (1, DATE('2019-09-01'), 'hola como estas', 'saludos', 1);