package no.bekk.digiq;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public long id;
    @Column(name = "SUBJECT")
    public String subject;
    @Column(name = "DIGIPOSTADDRESS")
	public String digipostAddress;
    @Column(name = "PERSONAL_IDENTIFICATION_NUMBER")
	public String personalIdentificationNumber;
    @Column(name = "NAME")
	public String name;
    @Column(name = "ADDRESSLINE1")
	public String addressline1;
    @Column(name = "ADDRESSLINE2")
	public String addressline2;
    @Column(name = "ZIPCODE")
	public String zipCode;
    @Column(name = "CITY")
	public String city;
    @Column(name = "COUNTRY")
	public String country;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
	public Status status;
    @Column(name = "CHANNEL")
    public String channel;
    
    @ManyToOne
    @JoinColumn(name = "BATCH_ID")
    public MessageBatch batch;
    
    private Message() {
    }

	public Message(String subject, String digipostAddress, String personalIdentificationNumber,
			String name, String addressline1, String addressline2,
			String zipCode, String city, String country, Status status, String channel) {
        this.subject = subject;
		this.digipostAddress = digipostAddress;
		this.personalIdentificationNumber = personalIdentificationNumber;
		this.name = name;
		this.addressline1 = addressline1;
		this.addressline2 = addressline2;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
		this.status = status;
        this.channel = channel;
	}
	
	public static Message fromForsendelse(Forsendelse f) {
		return new Message(f.subject, f.digipostAdresse, f.foedselsnummer, f.navn,
				f.adresselinje1, f.adresselinj2, f.postnummer, f.poststed,
				f.land, Status.IDENTIFY, f.kanalnavn);
	}
	
	public enum Status {
		IDENTIFY, 
		PRINT, 
		SENT_DIGIPOST, 
		SENT_PRINT, 
		NOT_SENT, 
		DIGIPOST("Sendt i Digipost"), 
		NOT_DIGIPOST("Identifisert, men ikke Digipost-bruker"), 
		UNKNOWN ("Uidentifisert"), 
		INVALID("Ugyldig"), 
		NO_RECEIPT ("Mangler i kvittering");

		private final String norwegian;
		private Status() {
		    this("-");
		}
        private Status(String norwegian) {
            this.norwegian = norwegian;
		    
		}
        public String inNorwegian() {
            return norwegian;
        }
	}

    public String getRecepientId() {
        return String.valueOf(id);
    }

    public String getRecepientIdentificationString() {
        if (digipostAddress != null) {
            return digipostAddress;
        } else if (personalIdentificationNumber != null) {
            return StringUtils.left(personalIdentificationNumber, 6) + "*****";
        } else {
            return name;
        }
    }

}
