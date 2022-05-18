package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;

/**
 * Progress a user has made with a book.
 */
@Entity
@Data
@NamedQueries({
        @NamedQuery(name="Progreso.byUser",
                query = "SELECT p FROM Progress p " + "WHERE p.user.id = :user"), // <> :
        @NamedQuery(name="Progreso.allBook",
        query="SELECT DISTINCT b FROM Progress b WHERE b.book.id = :id")
}
)
public class Progress implements Transferable<Progress.Transfer> {

    public static final String terminado = "terminado";
    public static final String quieroLeer = "quieroLeer";
    public static final String leyendo = "leyendo";
    public static final String abandonados = "abandonado";
    public static final String pausados = "pausado";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; //primary key of the dataset

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    //estado se usará para determinar si el libro está terminado, leyendose, pausado...
    private String estado;
    private Long porcentaje;
    private Long numPaginas; //Es el num paginas leidas

    @Getter
    @AllArgsConstructor
    public static class Transfer {

        private String estado;
        private Long porcentaje;
        private Long numPaginas;
        long id;
        public Transfer(Progress p) {
            this.estado = p.getEstado();
            this.porcentaje = p.getPorcentaje();
            this.numPaginas= p.getNumPaginas();
            this.id = p.getId();
        }
    }

	@Override
	public Transfer toTransfer() {
		return null;
    }
}
