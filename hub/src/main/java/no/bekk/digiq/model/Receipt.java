package no.bekk.digiq.model;

import static com.google.common.collect.Iterables.transform;

import java.util.Map;

import no.bekk.digiq.Message.Status;
import no.bekk.digiq.model.Resultat.MatchType;
import no.digipost.xsd.avsender1_6.XmlDigipostMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlIdentifisertMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;
import no.digipost.xsd.avsender1_6.XmlUgyldigMottakerResultat;
import no.digipost.xsd.avsender1_6.XmlUidentifisertMottakerResultat;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

public class Receipt {

    private final XmlMasseutsendelseResultat xmlReceipt;
    private Map<String, Resultat> results;

    public Receipt(XmlMasseutsendelseResultat result) {
        this.xmlReceipt = result;

        Iterable<Resultat> digipost = transform(result.getMottakerResultater().getDigipostResultat().getResultater(), fromDigipost);
        Iterable<Resultat> notDigipost = transform(result.getMottakerResultater().getIdentifisertResultat().getResultater(),
                fromNotDigipost);
        Iterable<Resultat> unknown = transform(result.getMottakerResultater().getUidentifisertResultat().getResultater(), fromUnknown);
        Iterable<Resultat> invalid = transform(result.getMottakerResultater().getUgyldigResultat().getResultater(), fromInvalid);

        this.results =Maps.uniqueIndex(Iterables.concat(digipost, notDigipost, unknown, invalid), new Function<Resultat, String>() {
            @Override
            public String apply(Resultat input) {
                return input.getRecepientId();
            }
        });
    }

    public String getRefJobbId() {
        return xmlReceipt.getJobbSammendrag().getRefJobbId();
    }

    private static final Function<XmlDigipostMottakerResultat, Resultat> fromDigipost = new Function<XmlDigipostMottakerResultat, Resultat>() {
        @Override
        public Resultat apply(XmlDigipostMottakerResultat input) {
            return new Resultat(MatchType.DIGIPOST, input.getKundeId());
        }
    };
    private static final Function<? super XmlIdentifisertMottakerResultat, ? extends Resultat> fromNotDigipost = new Function<XmlIdentifisertMottakerResultat, Resultat>() {
        @Override
        public Resultat apply(XmlIdentifisertMottakerResultat input) {
            return new Resultat(MatchType.NOT_DIGIPOST, input.getKundeId());
        }
    };
    private static final Function<? super XmlUidentifisertMottakerResultat, ? extends Resultat> fromUnknown = new Function<XmlUidentifisertMottakerResultat, Resultat>() {
        @Override
        public Resultat apply(XmlUidentifisertMottakerResultat input) {
            return new Resultat(MatchType.UNKNOWN, input.getKundeId());
        }
    };
    private static final Function<? super XmlUgyldigMottakerResultat, ? extends Resultat> fromInvalid = new Function<XmlUgyldigMottakerResultat, Resultat>() {
        @Override
        public Resultat apply(XmlUgyldigMottakerResultat input) {
            return new Resultat(MatchType.INVALID, input.getKundeId(), input.getKode().name());
        }
    };

    public Status toStatus(String recepientId) {
        Resultat resultat = results.get(recepientId);
        if (resultat == null) {
            return Status.NO_RECEIPT;
        }
        return resultat.toStatus();
    }
}
