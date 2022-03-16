package es.ucm.fdi.iw.controller;

import java.util.Arrays;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import es.ucm.fdi.iw.BookService;
import es.ucm.fdi.iw.model.Book;

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
        return "buscar";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
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
        b.setTitulo("ksksks");
        b.setId(1234);
        model.addAttribute("Book",b );

        return "redirect:/index";
    }
@PostMapping("/addBook")
@Transactional
    public String submit( @ModelAttribute Book b,  Model model) {
    
  
        model.addAttribute("Book",b );
        entityManager.persist(b);
        entityManager.flush();

        return "addBook";
    }
}
*/
        @Transactional
		@RequestMapping(value="/addBook", method = RequestMethod.POST)
		public String crearBook(
				@RequestParam("autor") String autor,
                @RequestParam("descripcion") String descripcion,
                @RequestParam("fecha") String fecha,
                @RequestParam("generos") String generos,
                @RequestParam("ISBN") String ISBN,
                @RequestParam("imag") String imag,
                @RequestParam("saga") String saga,
                @RequestParam("titulo") String titulo,
                @RequestParam("volumen") String volumen,
				HttpServletRequest request, HttpServletResponse response,
				Model model, 
				HttpSession session){
			
                    Book b = new Book();
                    b.setAutor(autor);
                    b.setDescripcion(descripcion);
                    b.setFecha(fecha);
                    b.setGeneros(generos);
                    b.setISBN(ISBN);
                    b.setImag(imag);
                    b.setNumpaginas(500);
                    b.setPuntuación(5);
                    b.setSaga(saga);
                    b.setTitulo(titulo);
                    b.setVolumen(volumen);
					return "redirect:/user-restaurant";
		}
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
