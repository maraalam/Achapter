package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
@Data
public class PhysicBook implements Transferable<PhysicBook.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; //primary key of the dataset
    @ManyToOne
    private Book libro;
    @ManyToOne
    private User propietario;
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
        public Transfer(PhysicBook b) {
            this.libro = b.getLibro().getISBN();
            this.propietario = b.getPropietario().getUsername();
            this.destinatario = b.getDestinatario().getUsername();
            this.fechaPrestamo = b.getFechaPrestamo() == null ?
                    null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(b.getFechaPrestamo());
            this.fechaDevolucion = b.getFechaDevolucion() == null ?
                    null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(b.getFechaDevolucion());
        }
    }


    @Override
    public Transfer toTransfer() {
        return new Transfer(libro.getISBN(), propietario.getUsername(), destinatario.getUsername(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fechaPrestamo),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fechaDevolucion), id );
    }
}
