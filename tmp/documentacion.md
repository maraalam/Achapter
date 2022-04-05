# INFORMACIÓN SOBRE SPRING Y JPA.

**Tabla de contenido**

* [JPA - CREACIÓN DE PERSISTENCIA](#jpa---creaci-n-de-persistencia)
  + [Uso de las etiquetas](#uso-de-las-etiquetas)
* [JPA - GESTION DE QUERIES](#jpa---gestion-de-queries)
  + [Ejemplo definición de namedQuery](#ejemplo-definici-n-de-namedquery)
  + [Ejemplo de petición usando la namedQuery](#ejemplo-de-petici-n-usando-la-namedquery)
* [JPA - Clase EntityManager](#jpa---clase-entitymanager)
  + [Persist()](#persist--)
  + [Find()](#find--)
  + [Remove()](#remove--)

<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>



## JPA - CREACIÓN DE PERSISTENCIA

JPA es una API que nos permite gestionar la persistencia de nuestra aplicación haciendo de intermediario entre la base de datos y el modelo.

Los **objetos persistentes son también llamados Entities** y se definen con la etiqueta **@Entity** encima de una clase lo cual creará una tabla en la base de datos para dicha clase.

Etiquetas útiles:

- @SequenceGenerator

- @GenerataredValue

- @Columns

- @Table

- @Id

**@SecuenceGenerator** y **@GeneratedValue** sirven para  definir el valor de la clave primaria de la clase y como va secuenciandose.

**@SecuenceGenerator** crea secuenciador de  nombre *"nombre"* que va de 1 en 1 (valor por defecto).

### Uso de las etiquetas

Crear secuenciador de nombre *"nombre de la sequencia"* nombre que va de 1 en 1 (valor por defecto):

```java
@SequenceGenerator(name="nombre de la secuencia", sequenceName ="nombre de la secuencia")
```

Especificar que se va a usar el secuenciador *"nombre de la secuencia"* (es decir, que irá de 1 en 1 generando las claves primarias):

```java
@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="nombre de la secuencia")
```

Creación de una columna en una entidad Entity (hay más atributos):

```java
@Column(name="nombre columna">,nullable=true/false,unique=true/false)
```

Cambio del nombre de la tabla (por defecto, Entity crea la tabla con el nombre de la clase, con table se puede cambiar):

```java
@Table(name="nuevo nombre")
```

Definición de la clave primaria de la entidad Entity:

```java
@Id
private long id
```

## JPA - GESTION DE QUERIES

Para evitar repetir consultas y por motivos de seguridad usaremos **NamedQueries**. Las NamedQueries se definen en clases con la etiqueta @Entity y solo actuan sobre dicha tabla (como es obvio). Para definir una serie de NamedQueries usamos:

```java
@NamedQueries({
    @NamedQuery(),
    @NamedQuery(),
    ...
})
```

 Cada @NamedQuery tiene como atributo:

- name -> identificador de la query.

- query -> la query en si que se tramitará a la base de datos.

### Ejemplo definición de namedQuery

```java
@NamedQueries({
    @NamedQuery(name="cogerNombreAlumno", query="SELECT name from Alumno a where a.name=:name") 
})
```

(**:name** es lo que se pasamos como input) 

### Ejemplo de petición usando la namedQuery

Para lanzar la consulta basta con lanzar desde **un controlador**:

```java
<clase de la Entity> entity = (<clase de la Entity>)entityManager.createNamedQuery("name de la query").setParameter("nombre parametro", valor).getSingleResult()
```

(En este caso he usado *getSingleResult()* pero hay varias opciones)

```java
@NamedQueries({
    @NamedQuery(name="cogerDNIAlumno", query="SELECT dni FROM Alumno a WHERE a.dni=:dni_alumno")
})

Alumno alumno = (Alumno)entityManager.createNamedQuery("cogerDNIAlumno").setParameter("dni_alumno", "03390415H").getSingleResult()
```

## JPA - Clase EntityManager

La clase **EntityManager** gestiona las comunicaciones con la base de datos. Se define en los controladores. 

```java
@AutoWired
private EntityManager entityManager
```

La clase tiene 3 métodos útiles que son los que más vamos a usar, persist, find, createNamedQuery (que ya lo hemos visto arriba) y remove.

### Persist()

Es el método que utiliza EntityManager para almacenar nuevas entidades en la base de datos.

```java
Persona pers = new Persona("Paco", "Sanchez", 26)
entityManager.persists(pers)
```

Con esto lo que se hace es que, suponiendo que Persona tiene atributos *nombre*,*apellidos*, *edad*, se cree una nueva fila en la tabla Personas con valores *Paco*,*Sanchez*,26.

### Find()

Find sirve para búscar una Entity en la base de datos **a través de su clave primaria**.

```java
Persona pers = entityManager.find(Persona.class, "Paco")
```

### Remove()

Remove sirve para eliminar una Entity de la base de datos **a través de su clave primaria**.

```java
entityManager.remove(Persona.class, "Paco")
```


