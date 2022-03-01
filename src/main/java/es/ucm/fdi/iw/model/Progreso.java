package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
public class Progreso implements Transferable<Progreso.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //primary key of the dataset
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
		return null;
    }
}
