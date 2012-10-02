package no.bekk.digiq;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {

    @Id
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
    
    @ManyToOne
    public MessageBatch batch;
    
    private Message() {
    }

	public Message(long id, String subject, String digipostAddress, String personalIdentificationNumber,
			String name, String addressline1, String addressline2,
			String zipCode, String city, String country, Status status) {
		this.id = id;
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
	}
	
	public static Message fromForsendelse(Forsendelse f) {
		return new Message(0, f.subject, f.digipostAdresse, f.foedselsnummer, f.navn,
				f.adresselinje1, f.adresselinj2, f.postnummer, f.poststed,
				f.land, Status.IDENTIFY);
	}
	
	public enum Status {
		IDENTIFY, DIGIPOST, PRINT, SENT_DIGIPOST, SENT_PRINT, NOT_SENT
	}

}
