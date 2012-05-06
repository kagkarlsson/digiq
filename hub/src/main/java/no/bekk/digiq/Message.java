package no.bekk.digiq;


public class Message {

    public String subject;
	public String digipostAddress;
	public String personalIdentificationNumber;
	public String name;
	public String addressline1;
	public String addressline2;
	public String zipCode;
	public String city;
	public String country;
	public byte[] content;
	public Status status;
	public final long id;

	public Message(long id, String subject, String digipostAddress, String personalIdentificationNumber,
			String name, String addressline1, String addressline2,
			String zipCode, String city, String country, byte[] content, Status status) {
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
		this.content = content;
		this.status = status;
	}

	public static Message fromForsendelse(Forsendelse f) {
		return new Message(0, f.subject, f.digipostAdresse, f.foedselsnummer, f.navn,
				f.adresselinje1, f.adresselinj2, f.postnummer, f.poststed,
				f.land, f.pdf, Status.IDENTIFY);
	}
	
	public enum Status {
		IDENTIFY, DIGIPOST, PRINT, SENT_DIGIPOST, SENT_PRINT, NOT_SENT
	}

}
