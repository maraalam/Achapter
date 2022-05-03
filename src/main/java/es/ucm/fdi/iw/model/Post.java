package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * A post a user can publish on their profile.
 */
@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "Post.all",
                query = "SELECT p FROM Post p ORDER BY p.dateSent DESC"),
        @NamedQuery(name="Post.byId",
	            query="SELECT m FROM Post m "
			            + "WHERE m.id = :postId " )

})
//.createQuery(" SELECT l FROM Library l INNER JOIN  LIBRARY_BOOKS_QUIERO_LEER  nl " + "ON nl.LIBRARY_ID.id   <> :LIBRARY_ID   ", Tuple.class)
public class Post implements Transferable<Post.Transfer> {

    private static Logger log = LogManager.getLogger(Message.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private Long id;
    @ManyToOne
    private User author;
    private String title;
    private String text;
    private Integer likes;
    private LocalDateTime dateSent;
    @OneToMany
    private Set<User> likesTable = new HashSet<>();


    /**
     * Objeto para persistir a/de JSON
     * @author mfreire
     */
    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private String author;
        private String sent;
        private String title;
        private String text;
        long id;
        public Transfer(Post m) {
            this.author = m.getAuthor().getUsername();
            this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
            this.title = m.getTitle();
            this.text = m.getText();
            this.id = m.getId();
        }
    }

    @Override
    public Post.Transfer toTransfer() {
        return new Post.Transfer(author.getUsername(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
                title, text, id
        );
    }
}
