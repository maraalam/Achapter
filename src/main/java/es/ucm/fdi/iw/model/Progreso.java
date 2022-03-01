package es.ucm.fdi.iw.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Progreso implements Transferable<Progreso.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //primary key of the dataset
    private String titulo;
	private String autor;
    private String portada;
    private String saga;
    private String fecha;
    private String descripcion;
    private long puntuación;
	private int numPaginas;


	
   	@OneToMany(targetEntity=Review.class)  
    private List<Review> reviewsPropias;
	//@ManyToMany
    //private List<String> generos;
	
	
	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
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
            this.fecha = b.getFecha();
            this.descripcion = b.getDescripcion();
            this.puntuación = b.getPuntuación();
            this.numPaginas = b.getNumPaginas();
        }
    }


	@Override
	public Transfer toTransfer() {
		return new Transfer(titulo, autor, portada, saga, fecha,
        descripcion, puntuación, numPaginas, id );
    }
}
