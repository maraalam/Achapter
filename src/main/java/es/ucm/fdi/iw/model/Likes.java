
package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;

/**
 * Un like realizado por un usuario a un post
 */
@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "Likes.all",
                query = "SELECT l FROM Likes l"),
        @NamedQuery(name="Likes.byId",
	            query="SELECT l FROM Likes l "
			            + "WHERE l.post.id = :postId"),
        @NamedQuery(name="Likes.byUser",
                query="SELECT l FROM Likes l "
                        + "WHERE l.usuario.id = :userId"),
        @NamedQuery(name="Likes.byuserpost",
                query="SELECT l FROM Likes l "
                        + "WHERE l.usuario.id = :userId and l.post.id=:postId"),
                        
})
public class Likes implements Transferable<Likes.Transfer> {

    
    private static Logger log = LogManager.getLogger(Message.class);
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private Long id;
    @ManyToOne
    private User usuario; //el que ha dado like
    @ManyToOne
    private Post post; //post al que se dio like 

    /**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
        @Getter
        @AllArgsConstructor
            public static class Transfer {
                    private User usuario;
                    private Post post;
                   
                    private long id;
                    public Transfer(Likes m) {
                            this.usuario = m.getUsuario();
                            this.post = m.getPost();
                            this.id = m.getId();
                    }
            }
    
            @Override
            public Transfer toTransfer() {
                    return new Transfer(usuario, post, id
            );
        }


}


