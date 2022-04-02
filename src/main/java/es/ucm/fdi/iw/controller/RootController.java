package es.ucm.fdi.iw.controller;

import com.fasterxml.jackson.databind.JsonNode;
import es.ucm.fdi.iw.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


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
    public String login(Model model) {
        return "login";
    }

	@GetMapping("/")
    public String index(Model model) {
        //model.addAttribute("libros", bookservice.mockBooks());
        return "index";
    }

    @GetMapping("/prestamos")
    public String prestamos(Model model) {
        return "prestamos";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        return "posts";
    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model, HttpSession session) {
        model.addAttribute("friends",
                entityManager.createNamedQuery("User.friends", User.class)
                        .setParameter("username", ((User)session.getAttribute("u")).getUsername())
                        .getResultList()
        );
        return "mensajeria";
    }

    @GetMapping("/libro/{id}")
    public String libro(Model model, @PathVariable long id) {
        log.info("buscando libro " + id);
        model.addAttribute("searchBookById", entityManager.createNamedQuery("Book.byId", Book.class).setParameter("id", id).getSingleResult());
        return "libro";
    }

    @GetMapping("/buscar")
    public String buscar(Model model, @RequestParam("query")String consulta) {
        log.info("buscando" + consulta);
        model.addAttribute("searchBook", entityManager.createNamedQuery("Book.byTitulo", Book.class).setParameter("titulo", consulta).getResultList());
        return "buscar";
    }

    @ModelAttribute("books")
    public List<Book> getBooksList() {
        return entityManager.createQuery("select b from Book b", Book.class).getResultList();
    }

    // User u = entityManager.createNamedQuery("User.byUsername", User.class)

    @ModelAttribute("generos")
    public HashSet<String> getGenerosList() {
        
        HashSet<String> listaGeneros = new HashSet<String>(); 
        List<String> generos = entityManager.createQuery("SELECT DISTINCT b.generos FROM Book b", String.class).getResultList();
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
public String crearBook(@RequestBody  JsonNode data, Model model){
            log.info("En funcion");
            Book b = new Book();

            b.setAutor(data.get("autor").asText());
            b.setGeneros(data.get("categories").asText().trim());
            b.setISBN(data.get("isbn").asText());
         
            b.setImag(data.get("img").asText());
            log.info(data.get("pagecount").asInt());
           
            b.setNumpaginas(data.get("pagecount").asInt());
            log.info(b.getNumpaginas());
            b.setPuntuación(0);
   
            b.setTitulo(data.get("titulo").asText());
            model.addAttribute("Book", b);
            
           
           entityManager.persist(b);
        
        return "index";
    }


    @PostMapping("save/{tipoLibreria}/{id}")
    @ResponseBody
    @Transactional
    public String addToLibrary(@PathVariable String tipoLibreria, @PathVariable long id, Model model, HttpSession session, String libreria) {

        log.info("En funcion GUARDAR EN LIBRERIA");
        log.info("Libreria:" + tipoLibreria);
        User u = entityManager.find(
                User.class, ((User)session.getAttribute("u")).getId());
        Book b = entityManager.find(Book.class, id);

        if (u.getLibrary() == null) {
            Library lib = new Library(u);
//            lib.setOwner(u);
            u.setLibrary(lib);
            entityManager.persist(lib);
        }
        Progreso pro = new Progreso();
        pro.setBook(b);
        pro.setUser(u);
        entityManager.persist(pro);
        entityManager.flush();
        u.addToLibrary(b, pro, tipoLibreria);
        entityManager.persist(u);

        log.info("Book with id {} added to user {}'s library", id, u.getId());
        return "index";
    }

}
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
