package server;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Auction implements Serializable {

    int               idAuction;
    GregorianCalendar startAuction;
    int               durationAuction;
    String            descriptionAuction;
    User              creatorAuction;
    User              bidderAuction;
    double            bidAuction;
    boolean           statusAuction;

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
}
