package es.ucm.fdi.iw.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A user's personal library.
 */
@Data
@Entity
@NamedQueries({
        @NamedQuery(name = "Library.byOwner",
                query = "SELECT l FROM Library l WHERE l.owner.id = :owner")
}
)
public class Library {

    private static final Logger log = LogManager.getLogger(Library.class);

    /*public static final String terminado = "terminado";
    public static final String quieroLeer = "quieroLeer";
    public static final String leyendo = "leyendo";
    public static final String abandonados = "abandonados";
    public static final String pausados = "pausados";*/

    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Library(){

    }

    @ManyToMany(targetEntity = Progress.class)
    private Map<Long, Progress> books;

    public Library(User u) {
        this.owner = u;
        this.books = new HashMap<>();
    }

    public void put(Book b, Progress p) {
        this.books.put(b.getId(), p);
    }

    public Progress get(Book b) {
        return this.books.get(b.getId());
    }

    /*public HashMap<Long, Progress> getQuieroLeer(){
        HashMap<Long, Progress> quieroLeer = new HashMap<>();
        for (Map.Entry<Long, Progress> entry : this.books.entrySet()) {
            if(entry.getValue().getEstado().equals(Library.quieroLeer)){
                quieroLeer.put(entry.getKey(), entry.getValue());
            }
        }

        return quieroLeer;
    }

    public Map<Long, Progress> test(){
        return this.books;
    }*/
}
