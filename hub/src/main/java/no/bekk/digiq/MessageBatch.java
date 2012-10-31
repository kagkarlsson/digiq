package no.bekk.digiq;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_BATCH")
public class MessageBatch {

    public static final MessageBatch EMPTY = new MessageBatch(UUID.randomUUID().toString(), new ArrayList<Message>());

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "batch", fetch = FetchType.EAGER)
    private List<Message> messages;
    
    @Column(name = "DIGIPOST_JOBB_ID")
    public String digipostJobbId;

    @SuppressWarnings("unused")
    private MessageBatch() {
    }

    public MessageBatch(String digipostJobbId, List<Message> messages) {
        this.digipostJobbId = digipostJobbId;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public boolean isEmpty() {
        return messages.size() == 0;
    }
    
}
