package es.service;

import java.util.ArrayList;

import java.util.List;
import es.ucm.fdi.iw.model.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    
    public List<Book> mockBooks() {
        List<Book> lBooks = new ArrayList<Book>();
        Book b = new Book();
        b.setAutor("Daniela");
        b.setTitulo("Este es el libro de Daniela");
        b.setIsbn("12345");
        lBooks.add(b);

        Book b2 = new Book();
        b2.setAutor("Gabriela");
        b2.setTitulo("Este es el libro de Gabriela");
        b2.setIsbn("78910");

        lBooks.add(b2);
        return null;
    }

}
