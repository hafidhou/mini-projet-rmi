package server;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Auction implements Serializable {

	private int               idAuction;
	private GregorianCalendar startAuction;
	private int               durationAuction;
	private String            descriptionAuction;
	private User              creatorAuction;
	private User              bidderAuction;
	private double            bidAuction;
	private boolean           statusAuction;

    // Constructeur d'une nouvelle enchère
    public Auction(int id, int duration, String description, User creator,
            double bid) throws RuntimeException {
        idAuction = id;

        // Début de l'enchère : moment de sa création
        startAuction = new GregorianCalendar();
        durationAuction = duration;
        descriptionAuction = description;
        creatorAuction = creator;

        // Pas encore d'acquéreur
        bidderAuction = null;
        // Si l'enchère est négative, erreur
        if (bid > 0)
            bidAuction = bid;
        else
            throw new RuntimeException();
        // Enchère active
        statusAuction = true;
    }

    // Constructeur d'enchères persistantes
    public Auction(int id, GregorianCalendar start, int duration,
            String description, User creator, User bidder, double bid) {
        idAuction = id;
        startAuction = start;
        durationAuction = duration;
        descriptionAuction = description;
        creatorAuction = creator;
        bidderAuction = bidder;

        // Si l'enchère est terminée, statut : désactivée
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar end = ((GregorianCalendar) start.clone());
        end.add(GregorianCalendar.DATE, duration);
        if (current.after(end))
            statusAuction = false;
        else
            statusAuction = true;

        // Si l'enchère est négative, erreur
        if (bid > 0)
            bidAuction = bid;
        else
            throw new RuntimeException();
    }

    // Constructeur d'enchères persistantes (utilisé pour les tests)
    public Auction(int id, int duration, String description, User creator,
            User bidder, double bid, boolean status) {
        idAuction = id;
        startAuction = new GregorianCalendar();
        durationAuction = duration;
        descriptionAuction = description;
        creatorAuction = creator;
        bidderAuction = bidder;
        statusAuction = status;

        // Si l'enchère est négative, erreur
        if (bid > 0)
            bidAuction = bid;
        else
            throw new RuntimeException();
    }

    // Enchérir
    // Retourne faux si la nouvelle enchère est mauvaise
    public boolean placeBid(User bidder, double newBid) {
        if (newBid <= bidAuction)
            return false;

        bidderAuction = bidder;
        bidAuction = newBid;
        return true;
    }

    public boolean isActive() {
        return statusAuction;
    }
    
    public void deactivate() {
        statusAuction = false;
    }
    
    public User getCreator() {
        return creatorAuction;
    }
    
    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder();
    	str.append("Ench�re n�.").append(this.idAuction).append("\n");
    	str.append("Description : ").append(this.descriptionAuction).append("\n");
    	str.append("Createur : ").append(this.creatorAuction.getLogin()).append("\n");
    	str.append("Dernier ench�risseur : ").append(this.bidderAuction.getLogin()).append("\n");
    	str.append("Montant actuel : ").append(this.bidAuction).append("\n");
    	if(this.isActive()) {
    		long remainingTime = getRemainingTime();
    		if(remainingTime < 0) {
    			this.statusAuction = false;
    			str.append("Ench�re termin�e.");
    		} else {
    			long jours = remainingTime / (1000*60*60*24);
    			remainingTime -= jours * (1000*60*60*24);
    			long heures = remainingTime / (1000*60*60);
    			remainingTime -= heures * (1000*60*60);
    			long minutes = remainingTime / (1000*60);
    			remainingTime -= minutes * (1000*60);
    			long secondes = remainingTime / (1000);
    			remainingTime -= secondes * (1000);
    			
    			str.append("temps restant : \n").append(jours).append(" jours, ");
    			str.append(heures).append(" heures, ").append(minutes).append(" minutes, ");
    			str.append(secondes).append(" secondes et ");
    			str.append(remainingTime).append("millisecondes\n");
    		}
    	}
    	else
    		str.append("Ench�re termin�e.");
    	
    	return str.toString();
    }
    
    private long getRemainingTime() {
    	GregorianCalendar now = new GregorianCalendar();
    	GregorianCalendar end = (GregorianCalendar) this.startAuction.clone();
    	end.add(GregorianCalendar.DAY_OF_MONTH, this.durationAuction);
    	
    	return end.getTimeInMillis() - now.getTimeInMillis();
    }
}
