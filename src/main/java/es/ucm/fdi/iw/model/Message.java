package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A message that users can send each other.
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Message.countUnread",
	query="SELECT COUNT(m) FROM Message m "
			+ "WHERE m.recipient.id = :userId AND m.dateRead = null"),
	@NamedQuery(name="Message.byUsers",
	query="SELECT m FROM Message m "
			+ "WHERE m.recipient.id = :userId AND m.sender.id = :senderId")
})
@Data
public class Message implements Transferable<Message.Transfer>, Comparable<Message>{
	
	private static Logger log = LogManager.getLogger(Message.class);	
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;
	@ManyToOne
	private User sender;
	@ManyToOne
	private User recipient;
	private String text;
	
	private LocalDateTime dateSent;
	
	private LocalDateTime dateRead;
	
	@Override
  	public int compareTo(Message o) {
    	return dateSent.compareTo(o.dateSent);
  	}

	/**
	 * Objeto para persistir a/de JSON
	 * @author mfreire
	 */
    @Getter
    @AllArgsConstructor
	public static class Transfer {
		private String from;
		private String to;
		private String sent;
		private String received;
		private String text;
		long id;
		public Transfer(Message m) {
			this.from = m.getSender().getUsername();
			this.to = m.getRecipient().getUsername();
			this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateSent());
			this.received = m.getDateRead() == null ?
					null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(m.getDateRead());
			this.text = m.getText();
			this.id = m.getId();
		}
	}

	@Override
	public Transfer toTransfer() {
		return new Transfer(sender.getUsername(), recipient.getUsername(), 
			DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
			dateRead == null ? null : DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateRead),
			text, id
        );
    }
}
