package es.ucm.fdi.iw.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.service.BookService;
import es.ucm.fdi.iw.model.Book;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);
    //private final BookService bookservice;

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
        Book b = new Book();
        b.setAutor("Daniela");
        b.setTitulo("Este es el libro de Daniela");
        b.setIsbn("12345");
        Book b2 = new Book();
        b2.setAutor("Gabriela");
        b2.setTitulo("Este es el libro de Gabriela");
        b2.setIsbn("78910");
        return Arrays.asList(
            b, b2
        );
    }
 
}
