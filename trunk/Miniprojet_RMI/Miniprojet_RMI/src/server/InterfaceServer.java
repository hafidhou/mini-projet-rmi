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
    // Consulter les enchères
    public ArrayList<Auction> getAllAuctions() throws RemoteException;
    // Consulter une ench�re
    public Auction getAuctionById(int id) throws RemoteException;
}
