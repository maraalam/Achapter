package es.ucm.fdi.iw.controller;
import java.util.List;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Book;
import es.ucm.fdi.iw.model.Post;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);
    //private final BookService bookservice;

    @Autowired 
    private EntityManager entityManager;

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

    @GetMapping("/buscar")
    public String buscar(Model model) {
        /*
        Book u = entityManager.createNamedQuery("Book.byTitulo", Book.class)
        .setParameter("titulo", )
        .getSingleResult();
        */
        return "buscar";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        List<Post> posts = entityManager.createNamedQuery("Posts.all").getResultList();
        model.addAttribute("postList", posts);
        return "posts";
    }

    @GetMapping("/mensajeria")
    public String mensajeria(Model model) {
        return "mensajeria";
    }

    @GetMapping("/libro")
    public String libro(Model model) {
        return "libro";
    }

    @ModelAttribute("books")
    public List<Book> getBooksList() {
        return entityManager.createQuery("select b from Book b", Book.class).getResultList();
       
        /*Book b = new Book();
        b.setAutor("Daniela");
        b.setTitulo("Este es el libro de Daniela");
        b.setIsbn("12345");
        Book b2 = new Book();
        b2.setAutor("Gabriela");
        b2.setTitulo("Este es el libro de Gabriela");
        b2.setIsbn("78910");
        return Arrays.asList(
            b, b2
        );*/

    }
    // User u = entityManager.createNamedQuery("User.byUsername", User.class)

    @ModelAttribute("generos")
    public List<String> getGenerosList() {
        return entityManager.createQuery("SELECT DISTINCT generos FROM Book b", String.class).getResultList();

    }

    @ModelAttribute("posts")
    public List<Post> getPostsList() {
        return entityManager.createQuery("select b from Post b", Post.class).getResultList();

    }



    
@RequestMapping("/buscar")
public String buscar(Model model, @RequestParam("query")String consulta) {
    log.info("buscando" + consulta);
   model.addAttribute("searchBook", entityManager.createNamedQuery("Book.byTitulo", Book.class).setParameter("titulo", consulta).getResultList());
   return "buscar";
}


    /*
    @RequestMapping("/list_contact")
    public String listContact(Model model) {
         
        ContactBusiness business = new ContactBusiness();
        List<Contact> contactList = business.getContactList();
         
        model.addAttribute("contacts", contactList);       
         
        return "contact";
    }
    */
//@GetMapping("/buscar?query={id}")
}