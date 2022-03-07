package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;


@Entity
@Data
public class Progreso implements Transferable<Progreso.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        public Transfer(Progreso p) {
           
            this.porcentaje = p.getPorcentaje();
            this.numPaginas= p.getNumPaginas();
            this.id = p.getId();
        }
    }


	@Override
	public Transfer toTransfer() {
		return new Transfer(porcentaje, numPaginas, id);
    }
}
