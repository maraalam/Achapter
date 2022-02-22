package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name="User.byUsername",
                query="SELECT u FROM User u "
                        + "WHERE u.username = :username AND u.enabled = TRUE"),
        @NamedQuery(name="User.hasUsername",
                query="SELECT COUNT(u) "
                        + "FROM User u "
                        + "WHERE u.username = :username")
})
@Table(name="IWUser")
public class User implements Transferable<User.Transfer> {

    public enum Role {
        USER,			// normal users 
        ADMIN,          // admin users
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    private boolean enabled;
    private String roles; // split by ',' to separate roles

  
    private String about;

	@OneToMany
	@JoinColumn(name = "sender_id")
	private List<Message> sent = new ArrayList<>();
	@OneToMany
	@JoinColumn(name = "recipient_id")
	private List<Message> received = new ArrayList<>();
    @ManyToMany     //ManyToMany
    private List<User> followed = new ArrayList<>();
    @ManyToMany
    private List<User> followers = new ArrayList<>();

    @OneToMany
    private Map<String, List<Book>> library= new HashMap<>(); //nombre de la lista con sus libros
    @OneToMany
    private Map<Book, Long> libros_leyendo= new HashMap<>(); //nombre de la lista con sus libros (Libros actualmente siendo leidos)
    @OneToMany
    private List<Book> libros_enFisico = new ArrayList<>(); //lista de prestamos
    @OneToMany
    private List<Map<Book, Long>> listaPuntuaciones   = new ArrayList<>(); // lista de lectura
    
    /**
     * Checks whether this user has a given role.
     * @param role to check
     * @return true iff this user has that role.
     */
    public boolean hasRole(Role role) {
        String roleName = role.name();
        return Arrays.asList(roles.split(",")).contains(roleName);
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
	
    }

	@Override
    public Transfer toTransfer() {
		
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}

