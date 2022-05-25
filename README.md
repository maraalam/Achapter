# Achapter

Las plataformas para descubrir y compartir tus gustos sobre lectura son anticuadas, limitadas en funcionamiento y complicadas de utilizar. ¿Quieres encontrar tu próxima lectura, o tal vez compartir con tus amigos el progreso de tu actual lectura? ¿Tienes una lista de libros pendientes de la que siempre te olvidas? Proponemos una nueva aplicación amigable y divertida en la que encontrar tus libros favoritos y expresarte en una comunidad con tus mismos intereses.

## En desarrollo
- Que el uso API de Google se vea correctamente
- Faltas algunas pruebas Karate 
- Falta que el usuario pueda marcar como objetivos como cuántos libros quiere leer en el año. Esto puede ser en estadísticas. 
- Que en libros.html muestre libros solo de la saga. (Falta relacion en el modelo)

En administrador se puede agregar que pueda:
- Modificar la información de un libro.
- Modificar los libros disponibles.
- Ver reportes
  

## Usuarios
- Se puede registrar un usuario en la aplicación. Con estos datos luego se puede hacer login. 
- Hasta el momento, se puede hacer login rápido usando los botones de "a" y "b"

## Flujo Principal y adicionales
- Guardar un libro en la biblioteca de un usuario
- Buscar libros con el buscador dentro de la propia BD (Search en el Nav Bar)
- Ver perfiles y libros que estan leyendo otros usuarios (Pruebas por implementar).
- Registrarse e iniciar sesión (Pruebas por implementar).


## USO DE AJAX
1. Se usa para acceder a la API de Google. La barra de búsqueda en la página  principal de la web busca libros en base a la data que introduzcas haciendo solicitudes a la api de Google. El libro luego se puede guardar en la BD (index.js): 

Índice [Achapter: índice](http://localhost:8080/)

2. Para cargar una imagen de perfil a un usuario (user.html y iw.js): 

Perfil del usuario [Achapter: perfil](http://localhost:8080/user/2#)

3. Para seguir a usuarios en usuarios.html y usuariosfriends.html
Lista de usuarios en la App (usuarios.html) [Achapter: usuarios](http://localhost:8080/usuarios)
Lista de usuarios que sigo o me siguen (Depende del boton escogido antes) en la App (usuariosfriends.html) [Achapter: usuariosfriends](http://localhost:8080/usuariosfriends)

## ROLES DE USUARIO

### Usuario no registrado

Únicamente podrá buscar libros, consultar su información y acceder a los perfiles públicos de los usuarios.

### Usuario registrado

Este usuario tendrá un perfil en el que puede:

- Buscar un libro y su información,  recomendaciones relacionadas al mismo, ver quiénes han sido los últimos usuarios que sigues en leer un libro.
  
- Seguir perfiles de otros lectores y escritores.
  
- Añadir un libro.
- 
- Crear distintas Biblioteca de libros (Leyendo, Quiero leer,  Pausados,Terminados, Abandonados) y acceder a estadísticas generadas sobre estos en su perfil.
  
- Organizar su perfil como quiera para manifestar su creatividad en torno a este mundo. Incluyendo en este un muro de post en su perfil.
  
- Marcarse objetivos como número de libros o páginas que se desean leer en un tiempo específico, géneros que desea leer, etc.
  
- Podrá ver estadísticas sobre la información que tiene en su perfil (ej: libros leídos/leyendo, cuántos libros leyó durante un año, páginas leídas, etc.)
  

### Usuario administrador

Administran la información pertinente de la aplicación:
  
- Eliminar un libro.
- Añadir un libro.
- Eliminar las cuentas de otros usuarios si estas han sido reportadas por malos comportamientos.
  

## VISTAS

### Índice [Achapter: índice](http://localhost:8080/)

El índice varía dependiendo de si el usuario está logueado o no en la aplicación.
- En este se muestra max 10 libros, se planea en un futuro que muestre el top 10 de libros que se estan leyendo actualmente.
- Los géneros que más se están leyendo.
- Con la barra de búsqueda debajo del banner (no la del nav) se puede acceder a la API de Google y buscar cualquier libro. Se debe estar logueado y además permite 
guardar el libro en la Base de Datos. 

#### Usuario logueado

- En la barra superior se le mostrarán opciones como "Posts" o "Préstamos".
  
- Se le muestran los últimos libros modificados para que si quiere editarlos pueda hacerlo de forma rápida.
  
- Se muestran los objetivos establecidos por el usuario (páginas leídas por semana, libros leidos por mes, libros leídos este año...). (Por desarrollar)
  
- Se muestra información general sobre la aplicación (como libros más leidos, géneros más buscados...).
  

#### Usuario no logueado

- Se muestra información general sobre la aplicación (como libros más leídos, géneros más buscados...).
  
### Mensajería [Achapter: mensajería](http://localhost:8080/mensajeria)

- En la parte izquierda de la vista se encuentran los mensajes de las personas que te siguen y tú sigues.
- Existe un botón para crear un nuevo chat con una persona

### Registrar [Achapter: register](http://localhost:8080/register)

- Vista en la que se puede registrar un nuevo usuario

### Login [Achapter: login](http://localhost:8080/login)

- Vista en la que se puede loguear un usuario ya registrado


### Chat entre usuarios [Achapter: chat](http://localhost:8080/chat)

- Vista en la que se ven los mensajes entre dos usuarios. 


### Posts [Achapter: posts](http://localhost:8080/posts)

En la parte izquierda de la vista se encuentran una serie de filtros que permiten buscar más cómodamente posts que tengan ciertas características (popularidad, año de publicación, autor...).

En la parte derecha de la vista encontramos una barra de búsqueda para filtrar por coincidencias y debajo los posts.

### Búsqueda [Achapter: búsqueda](http://localhost:8080/buscar?)

A través de esta vista el usuario podrá buscar libros.

Podrá aplicar una serie de filtros (ordenaciones varias, filtrar por autor, fechas de publicación, géneros...) para facilitar la búsqueda. Además se podrá buscar un autor o un género dentro del filtro debido a que en ocasiones esta lista puede ser muy larga.

En la parte central se le mostrarán los libros que corresponden a su búsqueda.

De cada libro se mostrará:

- Portada.
  
- Título.
  
- Autor.
  
- Valoración.
  

Al seleccionar un libro se pasará a la vista de "Libro".
Además puede añadirlo directamente a su biblioteca 'quiero leer' mediante el botón que aparece encima del libro.

### Información de un libro [Achapter: libro](http://localhost:8080/libro)

En esta vista se mostrará información más detallada del libro que el usuario ha seleccionado.

Se le mostrará:

- Portada.
  
- Título.
  
- Autor.
  
- Fecha de publicación.
  
- Sinopsis.
  
- Géneros.
  
- Estado de la lectura (terminado, en progreso, sin empezar, pausado, abandonado).
    - Además, aparecerá -sin biblioteca- en el caso de que el no se encuentre en ninguna de las biliotecas del usuario
    - Aparecerá -eliminar de la bilioteca- en el caso de que el usuario la tenga en alguna de las bibliotecas y desee borrarlo. 
  
- Libros pertenecientes a la misma saga.
  
- Comentarios de tus amigos sobre el libro (los amigos son personas que sigues y te siguen) con valoración y fecha.
  

También se le dará la posibilidad al usuario de añadirlo a una de sus listas de lectura.

### Perfil del usuario [Achapter: perfil](http://localhost:8080/user/2#)

El perfil del usuario es una vista que actúa de hub al resto de vistas.

Desde el perfil de un usuario otro usuario puede:

- Acceder a sus posts.
  
- Acceder a su biblioteca.
    - Se muestran las diferentes bibliotecas que existen en la aplicación con la lista de libros del usuario que pertenecen a esa biblioteca.
  
- Acceder a sus préstamos (libros físicos que puede prestar y libros físicos prestados).
  
- Acceder a sus objetivos y estadísticas.

- Ver sus seguidores y a quienes sigue
  

Además, un usuario a través del perfil de otro usuario podrá :

- Seguir al usuario del perfil (Funciona la logica pero no se ve en la vista el "seguido". 
  
- Enviarle un mensaje (va la vista chat.html)

### Administración [Achapter: administración](http://localhost:8080/admin/) (Por mejorar)
En esta vista el administrador puede revisar los reportes que le llegan de los usuarios y acceder mediante botones a otras vistas que le permiten:
- Eliminar libros de la aplicación.
- Banear usuarios.

### Préstamos [Achapter: préstamos](http://localhost:8080/prestamos)

En la vista de préstamos general de la aplicación.

A esta vista se puede acceder mediante el botón del menú de la parte superior y contiene lo siguiente:

- Un conjunto de filtros para facilitar la búsqueda de préstamos que coincidan con los requisitos del usuario.
  
- Una lista de libros a prestar de usuarios de la aplicación 
  
### Préstamos [Achapter: usuarios](http://localhost:8080/usuarios)

En la vista de usuarios general de la aplicación.

A esta vista se puede acceder mediante el botón con un niño en la parte superior y contiene lo siguiente:

- Una lista de usuarios de la aplicación.
- Puede seguir a otros usarios usando esta vista
  


