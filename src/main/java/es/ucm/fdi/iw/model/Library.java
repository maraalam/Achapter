package es.ucm.fdi.iw.model;

import lombok.Data;

import javax.persistence.*;
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
    Map<Long, Pair<Book, Progreso>> books;

}
