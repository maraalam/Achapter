package examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.ucm.fdi.iw.model.*;

public class BookTest /*implements Books */{
    
    public List<Book>  findByTitleIsLikeOrAuthorIsLikeOrSynopsisIsLike( String title,String author, String synopsis) 
  {
        List<Book> listBooks = new ArrayList<Book>();
     
        Book b1 = new Book();
        b1.setAutor("Daniela");
        b1.setTitulo("Este es un titulo");
        b1.setIsbn("123456");
   
        listBooks.add(b1);
    
        return listBooks;
  }

   
}
