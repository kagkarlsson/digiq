package no.bekk.digiq.xml;

import java.util.ArrayList;
import java.util.List;

import no.bekk.digiq.Message;
import no.digipost.xsd.avsender1_6.XmlAdresse;
import no.digipost.xsd.avsender1_6.XmlAdresseFormat1;
import no.digipost.xsd.avsender1_6.XmlBrev;
import no.digipost.xsd.avsender1_6.XmlDokumentListe;
import no.digipost.xsd.avsender1_6.XmlForsendelse;
import no.digipost.xsd.avsender1_6.XmlForsendelseListe;
import no.digipost.xsd.avsender1_6.XmlJobbInnstillinger;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelse;
import no.digipost.xsd.avsender1_6.XmlMottaker;
import no.digipost.xsd.avsender1_6.XmlNavn;
import no.digipost.xsd.avsender1_6.XmlNavnFormat1;
import no.digipost.xsd.avsender1_6.XmlStandardDistribusjon;

public class MasseutsendelseBuilder {

    private long avsenderId;
    private String jobbId = String.valueOf(System.currentTimeMillis());
    private String jobbNavn = jobbId;
    private List<Message> mottakere;

    private MasseutsendelseBuilder() {
        mottakere = new ArrayList<Message>();
    }

    public MasseutsendelseBuilder withJobbId(String jobbId) {
        this.jobbId = jobbId;
        return this;
    }

    public MasseutsendelseBuilder withJobbNavn(String jobbNavn) {
        this.jobbNavn = jobbNavn;
        return this;
    }

    public MasseutsendelseBuilder withRecipients(List<Message> recipients) {
        for (Message message : recipients) {
            mottakere.add(message);
        }
        return this;
    }

    public static MasseutsendelseBuilder newMasseutsendelse() {

        return new MasseutsendelseBuilder();
    }

    public XmlMasseutsendelse build() {

        XmlMasseutsendelse masseutsendelse = new XmlMasseutsendelse();
        XmlJobbInnstillinger jobbInnstillinger = new XmlJobbInnstillinger();
        jobbInnstillinger.setAvsenderId(avsenderId);
        jobbInnstillinger.setJobbId(jobbId);
        jobbInnstillinger.setJobbNavn(jobbNavn);
        jobbInnstillinger.setKlientinformasjon("Digiq_v1.0");
        masseutsendelse.setJobbInnstillinger(jobbInnstillinger);

        XmlStandardDistribusjon dist = new XmlStandardDistribusjon();

        XmlDokumentListe dokumenter = new XmlDokumentListe();
        XmlForsendelseListe forsendelser = new XmlForsendelseListe();
        for (int i = 0; i < mottakere.size(); i++) {
            XmlBrev b = new XmlBrev();
            Message m = mottakere.get(i);
            String id = String.valueOf(m.id);
            b.setId("id-" + id);
            b.setFil(id + ".pdf");
            dokumenter.getDokumenter().add(b);

            XmlForsendelse forsendelse = new XmlForsendelse();
            forsendelse.setBrev(b);

            forsendelse.setMottaker(newMottaker(m));
            forsendelser.getForsendelser().add(forsendelse);
        }
        dist.setPost(dokumenter);
        dist.setForsendelser(forsendelser);

        masseutsendelse.setStandardDistribusjon(dist);
        return masseutsendelse;
    }

    private XmlMottaker newMottaker(Message mottaker) {
        XmlMottaker xmlMottaker = new XmlMottaker();
        if (mottaker.personalIdentificationNumber != null) {
            xmlMottaker.setFoedselsnummer(mottaker.personalIdentificationNumber);
        } else if (mottaker.digipostAddress != null) {
            xmlMottaker.setDigipostadresse(mottaker.digipostAddress);
        } else {
            XmlNavn navn = new XmlNavn();
            xmlMottaker.setKundeId(String.valueOf(mottaker.id));
            XmlNavnFormat1 navnFormat1 = new XmlNavnFormat1();
            navnFormat1.setFulltNavnFornavnFoerst(mottaker.name);
            navn.setNavnFormat1(navnFormat1);
            xmlMottaker.setNavn(navn);
            XmlAdresse adresse = new XmlAdresse();
            XmlAdresseFormat1 adresseFormat1 = new XmlAdresseFormat1();
            adresseFormat1.setAdresselinje1(mottaker.addressline1);
            adresseFormat1.setAdresselinje2(mottaker.addressline2);
            adresseFormat1.setPostnummer(mottaker.zipCode);
            adresseFormat1.setPoststed(mottaker.city);
            adresseFormat1.setLand(mottaker.country);
            adresse.setAdresseFormat1(adresseFormat1);
            xmlMottaker.setAdresse(adresse);
        }
        return xmlMottaker;
    }

}
