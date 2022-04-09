-- insert admin (username a, password aa)
INSERT INTO IWUser (id, about, enabled, first_name, last_name, roles, username, password)
VALUES (11111, 'Le gusta fantasia', TRUE, 'Ana', 'Perez', 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, about, enabled, first_name, last_name, roles, username, password)
VALUES (21111, 'Le gusta romance',TRUE, 'Barbara', 'Benitez', 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, about, enabled, first_name, last_name, roles, username, password)
VALUES (31111, 'Le gusta terror',TRUE, 'Clara', 'Porta', 'USER', 'c',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, about, enabled, first_name, last_name, roles, username, password)
VALUES (41111, 'Le gusta No ficcion',TRUE, 'Daniela', 'Cordova', 'USER', 'd',
        '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

--insert books

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(1111, 'J. K. Rowling', 'Harry Potter se ha quedado huérfano y vive en casa de sus abominables tíos..', '2001', 'Juvenile Fiction', 
'https://quijotesyquijotinas2.files.wordpress.com/2010/09/harry-potter.jpg', '8498382661', 254, 5, 'Harry Potter', 'Harry Potter y la piedra filosofal', '1');

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(2111, 'J. K. Rowling', 'Tras derrotar una vez más a lord Voldemort, su siniestro enemigo en Harry Potter..', '1999', 'Juvenile Fiction',
'https://diarioinca.com/images/libro/harry-potter-y-la-camara-secreta.jpg', '8478884955', 286, 4, 'Harry Potter', 'Harry Potter y la cámara secreta', '2');

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(31111, 'Sally Rooney', 'Después de Conversaciones entre amigos..', '2019-10-03', 'Fiction', 
'https://imagessl8.casadellibro.com/a/l/t7/18/9788439736318.jpg', '8439736452', 256, 4, 'none', 'Gente normal', '1');

INSERT INTO Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(4111, 'Brandon Sanderson', 'La aclamada continuación de El camino de los reyes es, como el primer volumen..', '2015-08-27', 'Fiction,Juvenile Fiction', 
'https://images-na.ssl-images-amazon.com/images/I/91iLAw42-GL.jpg', '0439064864', 1248, 4, 'El Archivo de las Tormentas', 'Palabras radiantes (El Archivo de las Tormentas 2)', '2');


--insert post

INSERT INTO POST (id, date_sent, text, title, author_id)
VALUES (10, CURRENT_DATE, 'oh no, se ha caído otro árbol, pero...', '1213', 21111);

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE, AUTHOR_ID)
VALUES (3, CURRENT_DATE, 'hola como estas', 'saludos', 21111);

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE, AUTHOR_ID)
VALUES (15, CURRENT_DATE, 'hola como estasw', 'saludos', 21111);

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE, AUTHOR_ID)
VALUES (6, CURRENT_DATE, 'the queen has fallen. All hail your new ruler.', 'saludos', 11111);



--insert physicalbooks

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(1, 1111, 11111,21111, '2022-03-01', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(2, 31111, 11111, 21111, '2022-03-02', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(3, 4111, 21111, null, '2022-03-05', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(4, 4111,21111, null, '2022-03-05', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(5, 2111, 11111, null, '2022-03-05', null);

-- insert library
INSERT INTO PROGRESS (ID, NUM_PAGINAS, PORCENTAJE, BOOK_ID, USER_ID)
VALUES(2111, null, null, 1111, 21111);

INSERT INTO LIBRARY (id, USER_ID)
VALUES(1, 21111);


INSERT INTO LIBRARY_BOOKS_QUIERO_LEER (ID, PROGRESO_ID, BOOKS_QUIERO_LEER_KEY )
VALUES(1, 2111, 1111);