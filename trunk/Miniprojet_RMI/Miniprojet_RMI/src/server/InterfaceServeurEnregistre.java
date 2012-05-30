package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceServeurEnregistre extends Remote {
 // Consulter ses enchères
    public ArrayList<Auction> getOwnAuctions(User u) throws RemoteException;
 // Créer une enchère
    public boolean addAuction(int duree, String desc, double montant, User creator) throws RemoteException;
    // Enchérir
    public boolean placeBid(Auction auct, User bidder, double bid) throws RemoteException;
}
