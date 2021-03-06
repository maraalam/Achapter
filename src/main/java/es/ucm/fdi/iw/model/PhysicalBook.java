package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A physical copy of a book a user has.
 */
@Entity
@Data
@NamedQueries({
    @NamedQuery(name="PhysicalBook.allNoDest",
        query="SELECT b FROM PhysicalBook b WHERE b.destinatario IS NULL"),
        @NamedQuery(name="PhysicalBook.byId",
        query="SELECT b FROM PhysicalBook b WHERE b.id = :id"),
    @NamedQuery(name="PhysicalBook.allConDest",
        query="SELECT b FROM PhysicalBook b WHERE b.destinatario IS NOT NULL"),
    @NamedQuery(name="PhysicalBook.allBook",
        query="SELECT DISTINCT b FROM PhysicalBook b WHERE b.libro.id = :id"),
    @NamedQuery(name="PhysicalBook.BookFrom",
        query="SELECT DISTINCT b FROM PhysicalBook b WHERE b.libro.id = :id AND b.owner.id = :oid"),
    @NamedQuery(name="PhysicalBook.allBookFromUser",
        query="SELECT DISTINCT b FROM PhysicalBook b WHERE b.owner.id = :id"),
        @NamedQuery(name="PhysicalBook.allBookToUser",
        query="SELECT DISTINCT b FROM PhysicalBook b WHERE b.destinatario.id = :id"),
    @NamedQuery(name="PhysicalBook.allBookFromUserSinDest",
        query="SELECT DISTINCT b FROM PhysicalBook b WHERE b.owner.id = :id AND   b.destinatario IS  NULL"),
     @NamedQuery(name="PhysicalBook.allBookFromUserConDest",
        query="SELECT DISTINCT b FROM PhysicalBook b WHERE b.owner.id = :id AND b.destinatario IS NOT NULL"),
})
public class PhysicalBook implements Transferable<PhysicalBook.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //primary key of the dataset
    @ManyToOne
    private Book libro;
    @ManyToOne
    private User owner;
    @ManyToOne
    private User destinatario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    /**
     * Objeto para persistir a/de JSON
     * @author mfreire
     */
    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private String libro;
        private String propietario;
        private String destinatario;
        private String fechaPrestamo;
        private String fechaDevolucion;
        long id;
        
        public Transfer(PhysicalBook b) {
            this.libro = b.getLibro().getISBN();
            this.propietario = b.getOwner().getUsername();
            this.destinatario = b.getDestinatario().getUsername();
            this.fechaPrestamo = b.getFechaPrestamo() == null ?
                    null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(b.getFechaPrestamo());
            this.fechaDevolucion = b.getFechaDevolucion() == null ?
                    null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(b.getFechaDevolucion());
        }
    }


    @Override
    public Transfer toTransfer() {
        return new Transfer(libro.getISBN(), owner.getUsername(), destinatario.getUsername(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fechaPrestamo),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fechaDevolucion), id );
    }
}
