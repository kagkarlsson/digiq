package no.bekk.digiq.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import no.digipost.xsd.avsender1_6.XmlDigipostMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlDigipostResultatListe;
import no.digipost.xsd.avsender1_6.XmlIdentifisertMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlIdentifisertResultatListe;
import no.digipost.xsd.avsender1_6.XmlJobbSammendrag;
import no.digipost.xsd.avsender1_6.XmlJobbStatus;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;
import no.digipost.xsd.avsender1_6.XmlMottakerResultatListe;
import no.digipost.xsd.avsender1_6.XmlMottakerSammendrag;
import no.digipost.xsd.avsender1_6.XmlUgyldigKode;
import no.digipost.xsd.avsender1_6.XmlUgyldigMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlUgyldigResultatListe;
import no.digipost.xsd.avsender1_6.XmlUidentifisertMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlUidentifisertResultatListe;

public class MasseutsendelseResultatBuilder {

    private String avsenderId = "1";
    private String behandlerId = "1";
    private String jobbId = "JobbId1";
    private int nrDigipost = 1;
    private int nrTotal = 1;
    private boolean feilet = false;
    
    private Map<String, String> digipostRecipients = new HashMap<String, String>();
    private Map<String, String> notDigipostRecipients = new HashMap<String, String>();
    private Map<String, String> unknownRecipients = new HashMap<String, String>();
    private Map<String, String> invalidRecipients = new HashMap<String, String>();

    private MasseutsendelseResultatBuilder() {
    }
    
    public static MasseutsendelseResultatBuilder newResult() {
        return new MasseutsendelseResultatBuilder();
    }
    
    public MasseutsendelseResultatBuilder withDigipostRecipient(String id, String digipostAdress) {
        digipostRecipients.put(id, digipostAdress);
        return this;
    }

    public XmlMasseutsendelseResultat build() {
        XmlMasseutsendelseResultat resultat = new XmlMasseutsendelseResultat();
        
        XmlJobbSammendrag jobbSammendrag = new XmlJobbSammendrag();
        jobbSammendrag.setRefAvsenderId(avsenderId);
        jobbSammendrag.setRefBehandlerId(behandlerId);
        jobbSammendrag.setRefJobbId(jobbId);
        jobbSammendrag.setStartTid(new DateTime());
        jobbSammendrag.setSluttTid(new DateTime());
        resultat.setJobbSammendrag(jobbSammendrag);
        
        XmlMottakerSammendrag mottakerSammendrag = new XmlMottakerSammendrag();
        mottakerSammendrag.setDigipost(nrDigipost);
        mottakerSammendrag.setTotal(nrTotal);
        resultat.setMottakerSammendrag(mottakerSammendrag);
        
        XmlMottakerResultatListe mottakerresultater = new XmlMottakerResultatListe();
        mottakerresultater.setDigipostResultat(new XmlDigipostResultatListe());
        mottakerresultater.setIdentifisertResultat(new XmlIdentifisertResultatListe());
        mottakerresultater.setUgyldigResultat(new XmlUgyldigResultatListe());
        mottakerresultater.setUidentifisertResultat(new XmlUidentifisertResultatListe());

        for(Entry<String, String> e : digipostRecipients.entrySet()) {
            XmlDigipostMottakerResultat digiRes = new XmlDigipostMottakerResultat();
            digiRes.setKundeId(e.getKey());
            digiRes.setDigipostadresse(e.getValue());
            digiRes.setVasket(false);
            mottakerresultater.getDigipostResultat().getResultater().add(digiRes);
        }
        for(Entry<String, String> e : notDigipostRecipients.entrySet()) {
            XmlIdentifisertMottakerResultat res = new XmlIdentifisertMottakerResultat();
            res.setKundeId(e.getKey());
            res.setVasket(false);
            mottakerresultater.getIdentifisertResultat().getResultater().add(res);
        }
        for(Entry<String, String> e : unknownRecipients.entrySet()) {
            XmlUidentifisertMottakerResultat res = new XmlUidentifisertMottakerResultat();
            res.setKundeId(e.getKey());
            res.setVasket(false);
            mottakerresultater.getUidentifisertResultat().getResultater().add(res);
        }
        for(Entry<String, String> e : invalidRecipients.entrySet()) {
            XmlUgyldigMottakerResultat res = new XmlUgyldigMottakerResultat();
            res.setKundeId(e.getKey());
            res.setVasket(false);
            res.setKode(XmlUgyldigKode.USPESIFISERT);
            mottakerresultater.getUgyldigResultat().getResultater().add(res);
        }
        
        resultat.setMottakerResultater(mottakerresultater);
        
        resultat.setStatus(feilet ? XmlJobbStatus.FEILET : XmlJobbStatus.SUKSESS);
        
        return resultat;
    }

    public MasseutsendelseResultatBuilder withJobbId(String jobbId) {
        this.jobbId = jobbId;
        return this;
    }

    public MasseutsendelseResultatBuilder withNotDigipostRecipient(String id) {
        notDigipostRecipients.put(id, "dummy");
        return this;
    }
    public MasseutsendelseResultatBuilder withUnknownRecipient(String id) {
        unknownRecipients.put(id, "dummy");
        return this;
    }
    public MasseutsendelseResultatBuilder withInvalidRecipient(String id) {
        invalidRecipients.put(id, "dummy");
        return this;
    }
}
