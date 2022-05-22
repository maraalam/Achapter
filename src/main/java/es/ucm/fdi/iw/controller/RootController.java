package es.ucm.fdi.iw.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import es.ucm.fdi.iw.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.boot.spi.InFlightMetadataCollector.EntityTableXref;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);
    //private final BookService bookservice;

    @Autowired 
    private EntityManager entityManager;

    //@Autowired
    //BookRepository booksRepository;
    /*
    @Autowired
    public RootController() {
        this.bookservice = bookservice;
    }
*/

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        //model.addAttribute("user", new User());
        //model.addAttribute("libros", bookservice.mockBooks());
        return "index";
    }

    @GetMapping("/prestamos")
    public String prestamos(Model model) {
        return "prestamos";
    }

    @GetMapping("/posts")
    public String posts(Model model, HttpSession session) {

        User self = entityManager.find(
                User.class, ((User) session.getAttribute("u")).getId());
        
        List<Likes> lLikes= entityManager.createNamedQuery("Likes.byUser", Likes.class)
            .setParameter("userId", self.getId())
            .getResultList();
        List<Long> lIDPost = new ArrayList<Long>();

        for(Likes like: lLikes){
            lIDPost.add(like.getPost().getId());
        }

        model.addAttribute("likesPost", lIDPost);
        return "posts";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        return "usuarios";
    }


    @GetMapping("/mensajeria")
    public String mensajeria(Model model, HttpSession session) {


        long userId = ((User) session.getAttribute("u")).getId();
        List<Message> f = new ArrayList<Message>();
        List<Message> f2 = new ArrayList<Message>();
        List<Long> listId = new ArrayList<Long>();

        List<Message> lr= entityManager.createNamedQuery("Message.allMessagesRUser", Message.class)
        .setParameter("userId", userId)
        .getResultList();

        for(Message m : lr){
            if(!listId.contains(m.getSender().getId())){
                f.add(m);
                listId.add(m.getSender().getId());
            }
        }

        List<Message> ls = entityManager.createNamedQuery("Message.allMessagesSUser", Message.class)
        .setParameter("userId", userId)
        .getResultList();

        for(Message m : ls){
            if(!listId.contains(m.getRecipient().getId())){
                f2.add(m);
                listId.add(m.getRecipient().getId());
            }
        }


        model.addAttribute("mensajesR", f);
        model.addAttribute("mensajesS", f2);
        
        model.addAttribute("friends",
                entityManager.createNamedQuery("User.friends", User.class)
                        .setParameter("username", ((User)session.getAttribute("u")).getUsername())
                        .getResultList()
        );
        return "mensajeria";
    }

    @GetMapping("/libro")
    public String libro(Model model,  @RequestParam long id, HttpSession session) {
        log.info("[RootController.Libro] Accediendo al libro " + id);
        
        Book b = entityManager.createNamedQuery("Book.byId", Book.class).setParameter("id", id).getSingleResult();
        model.addAttribute("searchBookById", b);

        User user = entityManager.find(
            User.class, ((User)session.getAttribute("u")).getId());

        log.info("[RootController.Libro]" + user.getId());
        

        String libreria = "";
        if(user.getLibrary()!=null && user.getLibrary().containsB(b.getId())){
            libreria = user.getLibrary().get(b).getEstado();
        }
        else{
            libreria = "sinLibreria";
        }
        log.info("[RootController.Libro] " + libreria );
        model.addAttribute("libreriaLibro", libreria);
        return "libro";
    }

    @GetMapping("/buscar")
    public String buscar(Model model, @RequestParam(required = false) String query, @RequestParam(required = false) String type) {
        log.info("[RootController.Buscar] Buscando "+ type + ": "+ query);

        type = type!=null && !type.isEmpty() ? type : "titulo";

        query = query!=null ? query : "";

        model.addAttribute("query", query);
        model.addAttribute("queryType", type);
  
        return "buscar";
    }
    /*
        @GetMapping("/buscar_usuarios")
        public String buscarUsuarios(Model model, @RequestParam("query")String consulta, @RequestParam("type")String tipo) {
            log.info("buscando " + consulta + " " + tipo);
        
            
            User u = entityManager.createNamedQuery("User.byUsername", User.class)
                        .setParameter("username", consulta)
                        .getSingleResult();
                
                model.addAttribute("usuarios", u);

            return "usuarios";
        }
    */
    @ModelAttribute("books")
    public List<Book> getBooksList() {
        return entityManager.createNamedQuery("Book.all", Book.class).getResultList();
    }

    @ModelAttribute("booksAutores")
    public List<String> getAuthorList(){
        return entityManager.createNamedQuery("Book.autores", String.class).getResultList();
    }




    // User u = entityManager.createNamedQuery("User.byUsername", User.class)

    @ModelAttribute("generos")
    public HashSet<String> getGenerosList() {
        
        HashSet<String> listaGeneros = new HashSet<String>(); 
        List<String> generos = entityManager.createNamedQuery("Book.allGenre", String.class).getResultList();
        for (String g : generos){
            String[] gen = g.split(",");
            listaGeneros.addAll(Arrays.asList(gen));
        }

        return listaGeneros;
    }

    @ModelAttribute("posts")
    public List<Post> getPostsList() {
        return entityManager.createNamedQuery("Post.all", Post.class).setMaxResults(10).getResultList();
    }

    /*
    @ModelAttribute("friends")
    public List<User> getFriendsList(User user) {
        return entityManager.createNamedQuery("User.friends", User.class)
                .setParameter("username", user.getUsername())
                .getResultList();
    }
        */

        

    @ModelAttribute("prestamosSinDestinatario")
    public List getPhysicalBooksNoDestList() {
        return entityManager.createNamedQuery("PhysicalBook.allNoDest").setMaxResults(10).getResultList();
    }

    @PostMapping("/addBook")
    @Transactional
    public String crearBook(@RequestBody JsonNode data, Model model){
        log.info("crearBook");
        log.info("[RootController.crearBook] Libro añadido : " + data.get("titulo").asText());

        Book b = new Book();

        b.setAutor(data.get("autor").asText());
        b.setGeneros(data.get("categories").asText().trim());
        b.setISBN(data.get("isbn").asText());
        b.setNumpaginas(data.get("pagecount").asInt());
        b.setPuntuación(0);
        b.setPortada(data.get("portada").asText());
        b.setTitulo(data.get("titulo").asText());

        model.addAttribute("Book", b);
        
        entityManager.persist(b);
        entityManager.flush();
        
        return "index";
    }


    @PostMapping("/save")
    @Transactional
    public String addToLibrary(@RequestParam String tipoLibreria, @RequestParam Long id,
        Model model, HttpSession session, HttpServletRequest request/*Return a la página desde donde se llamo*/) {

        
        log.info("[RootController.save] Guardar libro " + id + " en libreria " + tipoLibreria);

        // Busco el usuario por el id de la sesion
        User user = entityManager.find(
                User.class, ((User)session.getAttribute("u")).getId());

        // Busco el libro por el id 
        Book book = entityManager.createNamedQuery("Book.byId", Book.class)
                                .setParameter("id", id).getSingleResult();


        //Si el tipo de libreria es 'sinLibreria' entonces elimino el libro si está en la libreria
        if(tipoLibreria.contains("sinLibreria")){

            //Si el usuario tiene libreria y contiene el libro -> lo borro
            if(user.getLibrary()!=null && user.getLibrary().containsB(book.getId())){
                entityManager.remove(user.getLibrary().get(book));

                user.getLibrary().removeBook(book.getId());
                entityManager.persist(user.getLibrary());
                entityManager.flush();
                entityManager.persist(user);
                entityManager.flush();
                
                log.info("[RootController.save] Book with id {} removed from user {}'s library", id, user.getId());
                
            }

            return "redirect:"+ request.getHeader("Referer");

        }


        // Si el usuario no tiene libreria la creo y persisto datos
        if (user.getLibrary() == null) {
            Library lib = new Library(user);
            user.setLibrary(lib);
            entityManager.persist(lib);
            entityManager.flush();
            entityManager.persist(user);
            entityManager.flush();
        }

        // Progreso
        Progress progress = new Progress();
        Library l = user.getLibrary();

        if(l.containsB(id)){
            progress = l.get(book);
            progress.setEstado(tipoLibreria);
            
            if(tipoLibreria.equals("terminado")){
            int cont = (int) model.getAttribute("contTerminado");
            model.addAttribute("contTerminado", cont+1);
        }       
        }else{    
            progress.setBook(book);
            progress.setUser(user);
            progress.setEstado(tipoLibreria);
            log.info("nuevo progreso");
        }

        entityManager.persist(progress);
        entityManager.flush();


        l.put(book, progress);
        //user.addToLibrary(book, progress);
        entityManager.persist(l);
        entityManager.flush();
        entityManager.persist(user);
        entityManager.flush();

        log.info("[RootController.save] Book with id {} added to user {}'s library", id, user.getId());
        return "redirect:"+ request.getHeader("Referer");
    }


    @ModelAttribute("usuarios")
    public List<User> getUsuariosList( Model model) {
        log.info("Obteniendo usuarios");

        return entityManager.createNamedQuery("User.all", User.class).getResultList();
    }

    @ModelAttribute("following")
    public List<User> getUsuariosfollowingList( Model model, HttpSession session) {
        log.info("Obteniendo following");
        
        User requester = (User)session.getAttribute("u");
        if(requester!=null){
        log.info(requester.getId());
        User u = entityManager.createNamedQuery("User.byId", User.class)
                    .setParameter("id", requester.getId())
                    .getSingleResult();

        return u.getFollowed();
        }
        return new ArrayList<User>();
    }

    
        
    @PostMapping("/addFollow")
    @Transactional
    public String addFollow(@RequestBody  JsonNode data, Model model){
        log.info("En funcion addFollow: " + data.get("usernameFollowed").asText() + " quiere seguir a " + data.get("usernameFollowing").asText());
        User usernameFollowed = entityManager.createNamedQuery("User.byUsername", User.class)
        .setParameter("username", data.get("usernameFollowed").asText())
        .getSingleResult();
        User usernameFollowing = entityManager.createNamedQuery("User.byUsername", User.class)
        .setParameter("username", data.get("usernameFollowing").asText())
        .getSingleResult();
        
            
        usernameFollowed.getFollowers().add(usernameFollowing); //es que es seguido
        usernameFollowing.getFollowed().add(usernameFollowed); //el que esta siguiendo 
        
        entityManager.persist(usernameFollowed);
        entityManager.flush();
        entityManager.persist(usernameFollowing);
        entityManager.flush();

        
        // model.addAttribute("following",  list);
        

        return "index";
    }


    @PostMapping("{tipo}/usuariosfriends")
    @Transactional
    public String followers(@PathVariable long tipo, Model model, HttpSession session) {
        log.info("En funcion followers: ");
        
        User self = entityManager.find(
                User.class, ((User)session.getAttribute("u")).getId());
            
        
        if(tipo==1){
            model.addAttribute("usuariosf", self.getFollowed());
        }
        else
            model.addAttribute("usuariosf",self.getFollowers());
        
        return "usuariosfriends";
    }
        

    @ResponseStatus(
        value=HttpStatus.FORBIDDEN,
        reason="Username ya existe en el sistema, usa otro")  // 403
    public static class UsernameNoPermitidoException extends RuntimeException {}



    @GetMapping("/register")
    @Transactional
    public String registerRUser(Model model) throws IOException {
        log.info("createUser");

        model.addAttribute("user", new User());

        return "register";
    }



    @PostMapping("likes/{id_post}")
    @Transactional
    public String crearLike(@PathVariable long id_post, Model model, HttpSession session){
        log.info("crearLike");

        User usuario = entityManager.find(
            User.class, ((User)session.getAttribute("u")).getId());
    
        log.info("[RootController.crearLike] Post : " + id_post + " Usuario :" +  usuario.getId()) ;


        Post post = entityManager.find(
                Post.class, id_post);
        //Post p = entityManager.createNamedQuery("Post.all", Post.class).setMaxResults(10).
        List<Likes> people = entityManager.createNamedQuery("Likes.byId", Likes.class)
                .setParameter("postId", (id_post))
                .getResultList();

        Likes l;

        int p = -1;
        for (int i = 0; i < people.size(); ++i) {
            if (people.get(i).getUsuario() == usuario) {
                p = i;
                break;
            }
        }

        if (p == -1) {
            l = new Likes();

            l.setPost(post);
            l.setUsuario(usuario);
            post.setLikes(post.getLikes() + 1);

            entityManager.persist(l);
            entityManager.flush();
            entityManager.persist(post);
            entityManager.flush();
        } else {
            l = people.get(p);

            post.setLikes(post.getLikes() - 1);

            entityManager.remove(l);
        }

        model.addAttribute("Likes", l);

        return "redirect:../posts";

    }
}

/*
        List<Usuario> l = new ArrayList<Usuario>();
        for(User u : entityManager.createQuery("select u FROM User u", User.class).getResultList()){
            log.info(new Usuario(u.getUsername(), u.getFirstName() + " " + u.getLastName(), u.getAbout()));
            l.add(new Usuario(u.getUsername(), u.getFirstName() + " " + u.getLastName(), u.getAbout()));
        }
        */

    /*
     @PostMapping("/addBook")
 @ResponseBody
 @Transactional
   public String crearBook(@RequestParam(required=false) String autor, @RequestParam(required=false) String titulo, @RequestParam(required=false) String isbn,
        Model model){
            log.info("En funcion");
            Book b = new Book();
            
            b.setAutor(autor);
           
            b.setISBN(isbn);
         
            b.setNumpaginas(500);
            b.setPuntuación(5);
   
            b.setTitulo(titulo);
            model.addAttribute("Book", b);
            
           //Do Something
            
           //model.addAttribute(b);
          // entityManager.getTransaction().begin();
           entityManager.persist(b);
          // entityManager.getTransaction().commit();

   
            entityManager.flush();  
        
        return "{\"titulo\": " + titulo + "}";
}

    */
