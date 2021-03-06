package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name="User.all",
                query="SELECT u FROM User u"),
        @NamedQuery(name="User.byUsername",
                query="SELECT u FROM User u "
                        + "WHERE u.username = :username AND u.enabled = TRUE"),
        @NamedQuery(name="User.byUsersnames",
                query="SELECT DISTINCT u FROM User u "
                        + "WHERE u.username LIKE CONCAT('%', :username, '%') AND u.enabled = TRUE"),
        @NamedQuery(name="User.byId",
                query="SELECT u FROM User u "
                        + "WHERE u.id = :id AND u.enabled = TRUE"),
        @NamedQuery(name="User.hasUsername",
                query="SELECT COUNT(u) "
                        + "FROM User u "
                        + "WHERE u.username = :username"),
        @NamedQuery(name="User.friends",
                query="SELECT u FROM User u "
                        + "WHERE u.username <> :username") //FIXME returns all users but self, not friends.
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

    //Libros en progreso
    @OneToOne
    private Library library;

	@OneToMany(fetch = FetchType.EAGER) // temporal solution
	@JoinColumn(name = "sender_id")
	private List<Message> sent = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER) // temporal solution
	@JoinColumn(name = "recipient_id")
	private List<Message> received = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> followed = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> followers = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "owner_id")
    private List<PhysicalBook> owned = new ArrayList<>(); //lista de libros en posesi??n
    @OneToMany
    @JoinColumn(name = "owner_id") // Esta creo q no hace falta, porque se puede preguntar cu??les libros tienen
                                   // destinatario == null con la de arriba
    private List<PhysicalBook> leased = new ArrayList<>(); //lista de libros en posesi??n, prestados
    @OneToMany
    @JoinColumn(name = "author_id")
    private List<Review> reviews  = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = "author_id")
    private List<Post> posts  = new ArrayList<>();

    /**
     * Checks whether this user has a given role.
     * @param role to check
     * @return true iff this user has that role.
     */
    public boolean hasRole(Role role) {
        String roleName = role.name();
        return Arrays.asList(roles.split(",")).contains(roleName);
    }

    public void addToLibrary(Book b, Progress p) {
        library.put(b, p);
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private String username;
		private int totalReceived;
		private int totalSent;
        public Transfer(User u) {
            this.id = u.getId();
            this.username=u.getUsername();

        }
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(id,	username, received.size(), sent.size());
	}

	@Override
	public String toString() {
		return toTransfer().toString();
	}
}

