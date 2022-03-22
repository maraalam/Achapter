package es.ucm.fdi.iw.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class Library {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany(targetEntity = Progreso.class)
    private Map<Long, Progreso> books;

    public Library() {
        //¿
    }

    public Library(User u) {
        this.owner = u;
        this.books = new HashMap<>();
    }

    public void put(Book b, Progreso p) {
        this.books.put(b.getId(), p);
    }
}
