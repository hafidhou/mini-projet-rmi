package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
 * Cette interface décrit les fonctionnalités
 * de l'application serveur. Toutes les méthodes
 * sont implémentées par le serveur.
 */
public interface InterfaceServer extends Remote {
    public boolean addUser(User u) throws RemoteException;
    public boolean addAuction(Auction a) throws RemoteException;
    public boolean placeBid(Auction auct, User bidder, double bid) throws RemoteException;
    public boolean isRegistered(User bidder) throws RemoteException;
    public boolean isActive(Auction auct) throws RemoteException;
}
