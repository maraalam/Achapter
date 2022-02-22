# Achapter

Las plataformas para descubrir y compartir tus gustos sobre lectura son anticuadas, limitadas en funcionamiento y complicadas de utilizar. ¿Quieres encontrar tu próxima lectura, o tal vez compartir con tus amigos el progreso de tu actual lectura? ¿Tienes una lista de libros pendientes de la que siempre te olvidas? Proponemos una nueva aplicación amigable y divertida en la que encontrar tus libros favoritos y expresarte en una comunidad con tus mismos intereses.

## ROLES DE USUARIO

### Usuario no registrado

Únicamente podrá buscar libros, consultar su información y acceder a los perfiles públicos de los usuarios.

### Usuario registrado

Este usuario tendrá un perfil en el que puede:

- Buscar un libro y su información,  recomendaciones relacionadas al mismo, ver quiénes han sido los últimos usuarios que sigues en leer un libro.
  
- Seguir perfiles de otros lectores y escritores.
  

- Crear distintas listas de libros (leídos, en proceso, lista libros que leere,  lista de libros propuestos para leer durante un año, entre otras) y acceder a estadísticas generadas sobre estos.
  
- Organizar su perfil como quiera para manifestar su creatividad en torno a este mundo. Incluyendo en este un muro de post en su perfil.
  
- Crear, unirse a clubes de lectura y seguir las últimas tendencias en el mundo de la lectura.
  
- Marcarse objetivos como número de libros o páginas que se desean leer en un tiempo específico, géneros que desea leer, etc.
  
- Podrá ver estadísticas sobre la información que tiene en su perfil (ej: libros leídos/leyendo, cuántos libros leyó durante un año, páginas leídas, etc.)
  

### Usuario administrador

Administran la información pertinente de la aplicación:

- Añadir un libro.
  
- Eliminar un libro.
  
- Modificar la información de un libro.
  
- Modificar los libros disponibles.
  
- Eliminar las cuentas de otros usuarios si estas han sido reportadas por malos comportamientos.
  

## VISTAS

### Índice [Achapter: índice](http://localhost:8080/)

El índice varía dependiendo de si el usuario está logueado o no en la aplicación.

#### Usuario logueado

- En la barra superior se le mostrarán opciones como "Posts" o "Préstamos".
  
- Se le muestran los últimos libros modificados para que si quiere editarlos pueda hacerlo de forma rápida.
  
- Se muestran los objetivos establecidos por el usuario (páginas leídas por semana, libros leidos por mes, libros leídos este año...).
  
- Se muestra información general sobre la aplicación (como libros más leidos, géneros más buscados...).
  

#### Usuario no logueado

- Se muestra información general sobre la aplicación (como libros más leídos, géneros más buscados...).
  

### Mensajería [Achapter: mensajería](http://localhost:8080/mensajeria)

En la parte izquierda de la vista se encuentran los mensajes de las personas que te siguen y tú sigues.

En la parte derecha de la vista se encuentran los chats grupales (o clubs de lectura).

### Posts [Achapter: posts](http://localhost:8080/posts)

En la parte izquierda de la vista se encuentran una serie de filtros que permiten buscar más cómodamente posts que tengan ciertas características (popularidad, año de publicación, autor...).

En la parte derecha de la vista encontramos una barra de búsqueda para filtrar por coincidencias y debajo los posts.

### Búsqueda [Achapter: búsqueda](http://localhost:8080/buscar?)

A través de esta vista el usuario podrá buscar libros.

Podrá aplicar una serie de filtros (ordenaciones varias, filtrar por autor, fechas de publicación...) para facilitar la búsqueda.

En la parte central se le mostrarán los libros que corresponden a su búsqueda.

De cada libro se mostrará:

- Portada.
  
- Título.
  
- Autor.
  
- Valoración.
  

Al seleccionar un libro se pasará a la vista de "Libro".

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
  
- Libros pertenecientes a la misma saga.
  
- Comentarios de tus amigos sobre el libro (los amigos son personas que sigues y te siguen) con valoración y fecha.
  

También se le dará la posibilidad al usuario de añadirlo a una de sus listas de lectura.

### Perfil del usuario [Achapter: perfil](http://localhost:8080/user/2#)

El perfil del usuario es una vista que actúa de hub al resto de vistas.

Desde el perfil de un usuario otro usuario puede:

- Acceder a sus posts.
  
- Acceder a su biblioteca.
  
- Acceder a sus préstamos (libros físicos que puede prestar).
  
- Acceder a sus listas de lectura.
  
- Acceder a sus objetivos y estadísticas.
  

Además, un usuario a través del perfil de otro usuario podrá:

- Seguir al usuario del perfil.
  
- Escribirle un post.

### Administración [Achapter: administración](http://localhost:8080/admin/)
En esta vista el administrador puede revisar los reportes que le llegan de los usuarios y acceder mediante botones a otras vistas que le permiten:
- Añadir libros a la aplicación.
- Editar libros de la aplicación.
- Eliminar libros de la aplicación.
- Banear usuarios.

### Préstamos [Achapter: préstamos](http://localhost:8080/prestamos)

En la vista de préstamos general de la aplicación.

A esta vista se puede acceder mediante el botón del menú de la parte superior y contiene lo siguiente:

- Un conjunto de filtros para facilitar la búsqueda de préstamos que coincidan con los requisitos del usuario.
  
- Una lista de libros a prestar de usuarios de la aplicación (usuarios generales, no tus amigos).
  
- Una lista de libros a prestar de usuarios amigos (personas que sigues y te siguen).
  

### Préstamos de un usuario (no desarrollada aún)

En esta vista se mostrarán los préstamos que un usuario tiene disponibles.

A esta vista solo se accederá desde el perfil del usuario en cuestión.

En ambas vistas, al darle al botón de "Solicitar" se le enviará una notificación al dueño del libro con un mensaje en la vista de posts indicando que una persona solicita su libro
