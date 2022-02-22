package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;


@Entity
@Data
public class Book implements Transferable<Book.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titulo;
	private String autor;
    private String portada;
    private String saga;
    private String fecha;
    private String descripcion;
    private long puntuación;
	private int numPaginas;


	
   	@OneToMany
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
        public Transfer(Book m) {
            
        }
    }


	@Override
	public Transfer toTransfer() {
		return null;
    }
}
