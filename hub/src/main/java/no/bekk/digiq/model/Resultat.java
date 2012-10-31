package no.bekk.digiq.model;

import no.bekk.digiq.Message.Status;



public class Resultat {

    private final MatchType matchType;
    private final String details;
    private final String kundeId;

    enum MatchType{
        DIGIPOST(Status.DIGIPOST),
        NOT_DIGIPOST(Status.NOT_DIGIPOST),
        UNKNOWN(Status.UNKNOWN),
        INVALID(Status.INVALID);
        
        private final Status status;

        private MatchType(Status status) {
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }
    }
    
    
    public Resultat(MatchType matchType) {
        this(matchType, null);
    }


    public Resultat(MatchType matchType, String kundeId) {
        this(matchType, kundeId, null);
    }


    public Resultat(MatchType matchType, String kundeId, String details) {
        this.matchType = matchType;
        this.kundeId = kundeId;
        this.details = details;
    }


    public String getRecepientId() {
        return kundeId;
    }


    public Status toStatus() {
        return matchType.getStatus();
    }
    
}
