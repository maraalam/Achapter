package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.Book;

//@Controller()
//@RequestMapping("book")
@Controller()
@RequestMapping("index")
public class BookController {
    
    
    @RequestMapping("/index")
    public String getBooks(Model model){
        Book b = new Book();
        b.setAutor("Daniela");
        b.setTitulo("Este es el libro de Daniela");
        b.setISBN("12345");
        model.addAttribute("libros",b);

        Book b2 = new Book();
        b2.setAutor("Gabriela");
        b2.setTitulo("Este es el libro de Gabriela");
        b2.setISBN("78910");
        model.addAttribute("libros",b2);
        return "index";
    }

}

