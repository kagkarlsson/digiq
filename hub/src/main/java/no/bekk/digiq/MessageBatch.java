package no.bekk.digiq;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class MessageBatch {

    @Id
    @Column(name = "ID")
    public long id;

    @OneToMany
    public List<Message> messsages;

    private MessageBatch() {
        
    }
    
}
