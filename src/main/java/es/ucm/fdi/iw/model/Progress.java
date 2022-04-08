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
@NamedQueries(
        @NamedQuery(name="Progreso.byUser",
                query = "SELECT p FROM Progress p " + "WHERE p.user.id <> :user")
)
public class Progress implements Transferable<Progress.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; //primary key of the dataset

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    private Long porcentaje;
    private Long numPaginas;

    @Getter
    @AllArgsConstructor
    public static class Transfer {

        private Long porcentaje;
        private Long numPaginas;
        long id;
        public Transfer(Progress p) {
           
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