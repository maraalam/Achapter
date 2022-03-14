package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NamedQueries({
        @NamedQuery(name = "Posts.all",
                query = "SELECT p FROM Post p ORDER BY p.dateSent DESC")
})
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

    private LocalDateTime dateSent;


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
