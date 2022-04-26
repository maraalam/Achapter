---------- IWUSER ----------
/*
DESCRIPCION: usuario de la aplicación
ATRIBUTOS: 
   id -> Id del usurio [Long]
   about -> Descripcion [String]
   enable -> si el usuario está bloqueado [boolean]
   first_name -> nombre del usuario [String]
   last_name -> apellido del usuario
   roles -> roles del usuario [String] Separados por coma: (rol1, rol2, rol3...) 
   username -> nombre de usurio
   password -> contraseña */

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


---------- BOOK ----------
/* 
DESCRIPCION: Libro en la aplicación
ATRIBUTOS: 
   id -> Id del libro [Long]
   autor -> autor del libro [String]
   descripcion -> sinopsis del libro [String]
   fecha -> fecha de publicación [String]
   generos -> generos [String] separados por coma sin espacios antes y después de la coma
   isbn -> isbn del libro [String]
   numpaginas -> número de páginas del libro [Int]
   puntuación -> puntuación del libro [Long]
   saga -> saga del libro [String]
   titulo -> titulo del libro [String]
   volumen -> volument del libro [String]*/

INSERT INTO Book (id, autor, descripcion, fecha, generos, portada, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(0, 'J. K. Rowling', 'Harry Potter se ha quedado huérfano y vive en casa de sus abominables tíos..', '2001', 'Juvenile Fiction', 
'https://quijotesyquijotinas2.files.wordpress.com/2010/09/harry-potter.jpg', '8498382661', 254, 5, 'Harry Potter', 'Harry Potter y la piedra filosofal', '1');

INSERT INTO Book (id, autor, descripcion, fecha, generos, portada, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(1, 'J. K. Rowling', 'Tras derrotar una vez más a lord Voldemort, su siniestro enemigo en Harry Potter..', '1999', 'Juvenile Fiction',
'https://diarioinca.com/images/libro/harry-potter-y-la-camara-secreta.jpg', '8478884955', 286, 4, 'Harry Potter', 'Harry Potter y la cámara secreta', '2');

INSERT INTO Book (id, autor, descripcion, fecha, generos, portada, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(2, 'Sally Rooney', 'Después de Conversaciones entre amigos..', '2019-10-03', 'Fiction', 
'https://imagessl8.casadellibro.com/a/l/t7/18/9788439736318.jpg', '8439736452', 256, 4, 'none', 'Gente normal', '1');

INSERT INTO Book (id, autor, descripcion, fecha, generos, portada, isbn, numpaginas, puntuación, saga, titulo, volumen)
VALUES(3, 'Brandon Sanderson', 'La aclamada continuación de El camino de los reyes es, como el primer volumen..', '2015-08-27', 'Fiction,Juvenile Fiction', 
'https://images-na.ssl-images-amazon.com/images/I/91iLAw42-GL.jpg', '0439064864', 1248, 4, 'El Archivo de las Tormentas', 'Palabras radiantes (El Archivo de las Tormentas 2)', '2');



---------- POST ----------
/*
DESCRIPCIÓN : post en la aplicación
ATRIBUTOS: 
   id -> id del post [Long]
   date_set -> fecha de publicación del post [LocalDateTime]
   text -> texto del post [String]
   title -> titulo del post [String]
   author_id -> id del usuario autor del libro [User]*/

INSERT INTO POST (id, date_sent, text, title, author_id)
VALUES (10, CURRENT_DATE +1002, 'oh no, se ha caído otro árbol, pero...', '1213', 21111);

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE, AUTHOR_ID)
VALUES (3, CURRENT_DATE +2020, 'hola como estas', 'saludos', 21111);

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE, AUTHOR_ID)
VALUES (15, CURRENT_DATE +2022, 'hola como estasw', 'saludos', 21111);

INSERT INTO POST (id, DATE_SENT, TEXT, TITLE, AUTHOR_ID)
VALUES (6, CURRENT_DATE +4040, 'the queen has fallen. All hail your new ruler.', 'saludos', 11111);



---------- PHYSICAL BOOK ----------
/* 
DESCRIPCION: Libro fisico para prestamo
ATRIBUTOS: 
   id -> id del libro de prestamo [Long]
   libro_id -> id del libro al que se refiere [Book]
   owner_id -> id del usuario que lo presta [User]
   destinatario_id -> id del usuario que lo ha adquirido [User]
   fecha_prestamo -> fecha del prestamo [String]
   fecha_devolución -> fecha de la devulucion [String]*/

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(1, 0, 11111,21111, '2022-03-01', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(2, 2, 11111, 21111, '2022-03-02', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(3, 3, 21111, null, '2022-03-05', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(4, 3, 21111, null, '2022-03-05', null);

INSERT INTO physical_book (id, libro_id, owner_id, destinatario_id, fecha_prestamo, fecha_devolucion)
VALUES(5, 1, 11111, null, '2022-03-05', null);



---------- PROGRESS ----------
/*
DESCRIPICION: Progreso de un libro
ATRIBUTOS: 
   id -> id del progreso [Long]
   num_paginas -> número de páginas leídas del libro [Long]
   porcentaje -> porcentaje del libro leido [Long]
   estado -> estado del libro [String]: terminado, quieroLeer, leyendo, abandonado, pausado
   book_id -> id del libro [Book]
   user_id -> id del usuario que está leyendo el libro [User]*/

INSERT INTO PROGRESS (ID, NUM_PAGINAS, PORCENTAJE, ESTADO, BOOK_ID, USER_ID)
VALUES(0, 3, 100, 'terminado', 0, 21111);
INSERT INTO PROGRESS (ID, NUM_PAGINAS, PORCENTAJE, ESTADO, BOOK_ID, USER_ID)
VALUES(1, 3, 30, 'abandonado', 1, 21111);



---------- LIBRARY ----------
/* 
DESCRIPCION: Libreria del usuario
ATRIBUTOS: 
   id -> id de la libreria [Long]
   user_id -> id del usuario al que pertenece la libreria [User]*/

INSERT INTO LIBRARY (id, USER_ID)
VALUES(0, 21111);



---------- LIBRARY_BOOKS ----------
/* 
DESCRIPCION: lista de libros de la libreria (Library). 
ATRIBUTOS: 
   library_id -> id de la libreria [Library]
   books_id -> id del libro [Book]
   books_key -> id del progreso [Progress]*/

INSERT INTO LIBRARY_BOOKS(LIBRARY_ID, BOOKS_ID, BOOKS_KEY)
VALUES(0, 0, 0);