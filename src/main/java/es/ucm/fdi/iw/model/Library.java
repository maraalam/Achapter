package es.ucm.fdi.iw.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
public class Library {
    @Id
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;
    @ManyToMany(targetEntity = Progreso.class)
    Map<Long, Pair<Book, Progreso>> books;

}
