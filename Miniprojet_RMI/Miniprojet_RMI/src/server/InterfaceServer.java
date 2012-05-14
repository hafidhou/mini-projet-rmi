package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
 * Cette interface décrit les fonctionnalités
 * de l'application serveur. Toutes les méthodes
 * sont implémentées par le serveur.
 */
public interface InterfaceServer extends Remote {
    // Créer un compte
    public boolean register(String login, String password) throws RemoteException;
    // S'identifier
    public User connection(String login, String password) throws RemoteException;
    // Créer une enchère
    public boolean addAuction(int duree, String desc, double montant, User creator) throws RemoteException;
    // Enchérir
    public boolean placeBid(Auction auct, User bidder, double bid) throws RemoteException;
    // Consulter les enchères
    public ArrayList<Auction> getAllAuctions() throws RemoteException;
    // Consulter ses enchères
    public ArrayList<Auction> getOwnAuctions(User u) throws RemoteException;
    // Consulter une ench�re
    public Auction getAuctionById(int id) throws RemoteException;
}
