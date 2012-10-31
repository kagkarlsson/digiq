package no.bekk.digiq;

import java.io.Serializable;

public class Forsendelse implements Serializable {

	private static final long serialVersionUID = 1L;

	public String subject;
	public String digipostAdresse;
	public String foedselsnummer;
	public String navn;
	public String adresselinje1;
	public String adresselinj2;
	public String postnummer;
	public String poststed;
	public String land;
	public String kanalnavn;
	public byte[] pdf;



	public Forsendelse(String subject, String digipostAdresse, String foedselsnummer,
			String navn, String adresselinje1, String adresselinj2,
			String postnummer, String poststed, String land, String kanalNavn, byte[] pdf) {
		this.subject = subject;
        this.digipostAdresse = digipostAdresse;
		this.foedselsnummer = foedselsnummer;
		this.navn = navn;
		this.adresselinje1 = adresselinje1;
		this.adresselinj2 = adresselinj2;
		this.postnummer = postnummer;
		this.poststed = poststed;
		this.land = land;
        kanalnavn = kanalNavn;
		this.pdf = pdf;
	}

}
