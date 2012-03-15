package no.bekk.digiq;

import no.bekk.digiq.Message.Status;

public class MessageBuilder {

	private long id = 1;
	private String digipostAddress;
	private String personalIdentificationNumber;
	private String name = "Fornavn Etternavn";
	private String addressline1 = "Testgata 4";
	private String addressline2 ="c/o Jan Nordmann";
	private String zipCode = "1234";
	private String city = "Bergen";
	private String country;
	private byte[] content = "Hej".getBytes();
	private Status status = Status.IDENTIFY;

	private MessageBuilder() {
		
	}
	
	public static MessageBuilder newMessage() {
		return new MessageBuilder();
	}

	public Message build() {
		return new Message(id, digipostAddress, personalIdentificationNumber, name, addressline1, addressline2, zipCode, city, country, content, status);
	}

}
