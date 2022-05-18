
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
			            + "WHERE l.post.id = :postId")
})
public class Likes{
    
    private static Logger log = LogManager.getLogger(Message.class);
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private Long id;
    @ManyToOne
    private User usuario; //el que ha dado like
    @ManyToOne
    private Post post; //post al que se dio like 



}


