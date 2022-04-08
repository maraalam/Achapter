package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A review a user can publish about a book.
 */
@Entity
@Data
public class Review implements Transferable<Review.Transfer>{

    private static Logger log = LogManager.getLogger(Message.class);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private Long id;
    @ManyToOne
    private User author;
    @ManyToOne
    private Book book;
    private String title;
    private String text;
    private float score;

    private LocalDateTime dateSent;


    /**
     * Objeto para persistir a/de JSON
     * @author mfreire
     */
    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private String author;
        private String book;
        private String sent;
        private String title;
        private String text;
        private float score;
        long id;
        public Transfer(Review m) {
            this.author = m.getAuthor().getUsername();
            this.book = m.getBook().getTitulo();
            this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
            this.title = m.getTitle();
            this.text = m.getText();
            this.score = m.getScore();
            this.id = m.getId();
        }
    }

    @Override
    public Review.Transfer toTransfer() {
        return new Review.Transfer(author.getUsername(), book.getTitulo(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
                title, text, score, id
        );
    }
}
