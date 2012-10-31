package no.bekk.digiq;

import org.springframework.test.util.ReflectionTestUtils;

import no.bekk.digiq.Message.Status;

public class MessageBuilder {

	private long id;
	private String subject = "Subject";
	private String digipostAddress;
	private String personalIdentificationNumber;
	private String name = "Fornavn Etternavn";
	private String addressline1 = "Testgata 4";
	private String addressline2 ="c/o Jan Nordmann";
	private String zipCode = "1234";
	private String city = "Bergen";
	private String country;
	private Status status = Status.IDENTIFY;
	private String channel = "dummychannel";

	private MessageBuilder() {
		
	}
	
	public static MessageBuilder newMessage() {
		return new MessageBuilder();
	}
	
	public Message build() {
		Message message = new Message(subject, digipostAddress, personalIdentificationNumber, name, addressline1, addressline2, zipCode, city, country, status, channel);
		if (id > 0) {
		    ReflectionTestUtils.setField(message, "id", id);
		}
        return message;
	}

    public MessageBuilder withId(long id) {
        this.id = id;
        return this;
    }
    
    public MessageBuilder withPersonalIdentificationNumber(String fnr) {
        this.personalIdentificationNumber = fnr;
        return this;
    }
    
    public MessageBuilder withNoAdress() {
        name = null;
        addressline1 = null;
        addressline2 =null;
        zipCode = null;
        city = null;
        country = null;
        return this;
    }

    public MessageBuilder withDigipostadress(String digipostadress) {
        digipostAddress = digipostadress;
        return this;
    }

    public MessageBuilder withStatus(Status status) {
        this.status = status;
        return this;
    }

    public MessageBuilder withChannel(String channel) {
        this.channel = channel;
        return this;
    }

}
