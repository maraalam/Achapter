package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

/**
 * A book on the system.
 */
@Entity
@Data
@NamedQueries({
        @NamedQuery(name="Book.all",
                query="SELECT b FROM Book b"),
        @NamedQuery(name="Book.byTitulo",
                query="SELECT DISTINCT b FROM Book b "
                + "WHERE b.titulo LIKE CONCAT('%', :titulo, '%')"),
        @NamedQuery(name="Book.byISBN",
                query="SELECT DISTINCT b FROM Book b "
                + "WHERE b.ISBN LIKE CONCAT('%', :isbn, '%')"),
        @NamedQuery(name="Book.allGenre",
                query="SELECT DISTINCT b.generos FROM Book b "),
        @NamedQuery(name="Book.byId",
                query="SELECT DISTINCT b FROM Book b "
                + "WHERE b.id = :id"),
        @NamedQuery(name="Book.autores",
                query="SELECT DISTINCT b.autor from Book b"),
        @NamedQuery(name="Book.generos",
                query="SELECT DISTINCT b.generos from Book b"),
        @NamedQuery(name="Book.allYears",
                query="SELECT DISTINCT b.fecha from Book b")
})

public class Book implements Transferable<Book.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; // primary key of the dataset
    private String ISBN;
    private String titulo;
    private String autor;
    private String portada;
    private String saga;
    private String volumen;
    private String generos; // split by ','

    private String fecha = "sinfecha";
    private String descripcion;
    private long puntuación;
   
    private int numpaginas;

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
		private int numpaginas;
		long id;
		public Transfer(Book b) {
                        this.titulo = b.getTitulo();
                        this.autor = b.getAutor();
                        this.portada = b.getPortada();
                        this.saga = b.getSaga();
                        this.fecha = b.getFecha();//DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(b.getFecha());
                        this.descripcion = b.getDescripcion();
                        this.puntuación = b.getPuntuación();
                        this.numpaginas = b.getNumpaginas();
                        this.id = b.getId();
		}
    }


	@Override
	public Transfer toTransfer() {
		return new Transfer(titulo, autor, portada, saga,fecha,
                descripcion, puntuación, numpaginas, id );
    }
}
