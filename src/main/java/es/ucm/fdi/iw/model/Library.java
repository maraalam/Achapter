package es.ucm.fdi.iw.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
public class Library {

    public static final String terminado = "terminado";
    public static final String quieroLeer = "quieroLeer";
    public static final String leyendo = "leyendo";
    public static final String abandonados = "abandonados";
    public static final String pausados = "pausados";
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany(targetEntity = Progreso.class)
    private Map<Long, Progreso> books_quiero_leer;
    
    @ManyToMany(targetEntity = Progreso.class)
    private Map<Long, Progreso> books_terminados;

    @ManyToMany(targetEntity = Progreso.class)
    private Map<Long, Progreso> books_leyendo;

    @ManyToMany(targetEntity = Progreso.class)
    private Map<Long, Progreso> books_abandonados; //dropeados

    @ManyToMany(targetEntity = Progreso.class)
    private Map<Long, Progreso> books_pausados;

    public Library() {
        //¿?
    }

    public Library(User u) {
        this.owner = u;
        this.books_quiero_leer = new HashMap<>();
        this.books_terminados = new HashMap<>();
        this.books_leyendo = new HashMap<>();
        this.books_abandonados = new HashMap<>();
        this.books_pausados = new HashMap<>();
    }

    public void put(Book b, Progreso p, String libreria) {
        switch (libreria){
            case terminado:
                this.books_terminados.put(b.getId(), p);
            break;
            case abandonados:
                this.books_abandonados.put(b.getId(), p);
            break;
            case pausados:
                this.books_pausados.put(b.getId(), p);
            break;
            case leyendo:
                this.books_leyendo.put(b.getId(), p);
            break;
            case quieroLeer:
                this.books_quiero_leer.put(b.getId(), p);
            break;
        }
    }
}
