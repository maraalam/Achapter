package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@Table(name="Book")
public class Book implements Transferable<Book.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // primary key of the dataset
    private String isbn;
    private String titulo;
    private String autor;
    private String portada;
    private String saga;
    private String volumen;
    private String generos; // split by ','
    private LocalDate fecha;
    private String descripcion;
    private long puntuación;
    private int numPaginas;

   	@OneToMany(targetEntity=Review.class)
    @JoinColumn(name = "book_id")
    private List<Review> reviewsPropias;
	
    @Getter
    @AllArgsConstructor
    public static class Transfer {
	private String titulo;
	private String autor;
    private String portada;
    private String saga;
    private String fecha;
    private String descripcion;
    private long puntuación;
	private int numPaginas;
        long id;
        public Transfer(Book b) {
            this.titulo = b.getTitulo();
            this.autor = b.getAutor();
            this.portada = b.getPortada();
            this.saga = b.getSaga();
            this.fecha = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(b.getFecha());
            this.descripcion = b.getDescripcion();
            this.puntuación = b.getPuntuación();
            this.numPaginas = b.getNumPaginas();
            this.id = b.getId();
        }
    }


	@Override
	public Transfer toTransfer() {
		return new Transfer(titulo, autor, portada, saga,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fecha),
                descripcion, puntuación, numPaginas, id );
    }
}
