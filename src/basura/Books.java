package es.ucm.fdi.iw.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface Books  {
    
    List<Book> findByTitleIsLikeOrAuthorIsLikeOrSynopsisIsLike(
        String titulo,
        String autor,
        String isbn
    );

}
