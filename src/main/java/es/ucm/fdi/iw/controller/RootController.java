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
import java.lang.reflect.Field;
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

    @GetMapping("/libro")
    public String libro(Model model) {
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
 @ResponseBody
 @Transactional
public String crearBook(@RequestBody  JsonNode data, Model model){
            log.info("En funcion");
            Book b = new Book();
            
            b.setAutor(data.get("autor").asText());
            b.setGeneros("Fiction");
            b.setISBN(data.get("isbn").asText());
         
            b.setNumpaginas(500);
            b.setPuntuación(5);
   
            b.setTitulo(data.get("titulo").asText());
            model.addAttribute("Book", b);
            
           //Do Something
            
           //model.addAttribute(b);
          // entityManager.getTransaction().begin();
           entityManager.persist(b);
          // entityManager.getTransaction().commit();


        
        return "{\"titulo\": " + data.get("titulo").asText() + "}";
    }

    @PostMapping("save/{id}")
    @ResponseBody
    @Transactional
    public String addToLibrary(@PathVariable long id, Model model, HttpSession session) {

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
        u.addToLibrary(b, pro);
        entityManager.persist(u);

        log.info("Book with id {} added to user {}'s library", id, u.getId());
        return "{\"result\": \"ok.\"}";
    }

    private String getObjectId(Object o) {
		try {
			Field f = o.getClass().getDeclaredField("id");
			f.setAccessible(true);
			return ""+f.get(o);
		} catch (Exception e) {
			log.warn("Error retrieving id of class " + o.getClass().getSimpleName(), e);
			return null;
		}
	}

    /*
    @RequestMapping(value = "/addBook", method = RequestMethod.POST)
    public String submit(@ModelAttribute("Book") Book b, Model model) {
       
        if (result.hasErrors()) {
            return "error";
        }
        
        entityManager.createNamedQuery(
            " insert into Book (id, autor, descripcion, fecha, generos, imag, isbn, numpaginas, puntuación, saga, titulo, volumen) values (8, 'Sally Rooney', 'Después de Conversaciones entre amigos..', '2019-10-03', '[Fiction]', 
            'http://books.google.com/books/content?id=QcWrDwAAQBAJ&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api', '8439736452', 256, 4, 'none', 'Gente normal 2', '1')",  
            Book.class);

        return "index";
    }
    
    public Optional<Book> save(Book book) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(book);
            entityManager.getTransaction().commit();
            return Optional.of(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    

    @PostMapping("/addBook")
    public String submit(@ModelAttribute("Book") Book b, Model model) {
       
        model.addAttribute("Book", b);

        System.out.println(b.getTitulo().toString());
        log.info("Guardar libro");
        entityManager.getTransaction().begin();
        entityManager.persist(b);
        entityManager.getTransaction().commit();

        return "addBook";
    }
@GetMapping("/addBook")
    public String submit2( @ModelAttribute("Book") Book b, Model model) {
       
        Book b = new Book();
        b.setAutor("Daniela");
        b.setTitulo("ksksks");
        b.setId(1234);
        model.addAttribute("Book",b );

        return "redirect:/index";
    }
    
*/


/*
@PostMapping
public R<Book> addBook(@RequestBody Book books) {
    try {
        booksRepository.save(books);
    } catch (Exception e) {
        log.error("Creates a new books fails:" + e.getMessage());
    }

    return new R<Book>().success();
}
*/
}

/*
@PostMapping("/{id}")
	
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id, 
			@ModelAttribute User edited, 
			@RequestParam(required=false) String pass2,
			Model model, HttpSession session) throws IOException {

        User requester = (User)session.getAttribute("u");
        User target = null;
        if (id == -1 && requester.hasRole(Role.ADMIN)) {
            // create new user with random password
            target = new User();
            target.setPassword(encodePassword(generateRandomBase64Token(12)));
            target.setEnabled(true);
            entityManager.persist(target);
            entityManager.flush(); // forces DB to add user & assign valid id
            id = target.getId();   // retrieve assigned id from DB
        }
        
        // retrieve requested user
        target = entityManager.find(User.class, id);
        model.addAttribute("user", target);
		
		if (requester.getId() != target.getId() &&
				! requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}
		
		if (edited.getPassword() != null) {
            if ( ! edited.getPassword().equals(pass2)) {
                // FIXME: complain
            } else {
                // save encoded version of password
                target.setPassword(encodePassword(edited.getPassword()));
            }
		}		
		target.setUsername(edited.getUsername());
		target.setFirstName(edited.getFirstName());
		target.setLastName(edited.getLastName());

		// update user session so that changes are persisted in the session, too
        if (requester.getId() == target.getId()) {
            session.setAttribute("u", target);
        }

		return "user";
	}	
    */
