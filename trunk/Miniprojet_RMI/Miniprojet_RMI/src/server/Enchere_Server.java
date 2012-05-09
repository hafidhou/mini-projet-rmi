package server;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Enchere_Server extends UnicastRemoteObject implements
        InterfaceServer {

    ArrayList<User>    users;
    ArrayList<Auction> auctions;

    int                n_users;
    int                n_auctions;

    public Enchere_Server() throws RemoteException {
        users = new ArrayList<User>();
        auctions = new ArrayList<Auction>();

        n_users = 0;
        n_auctions = 0;
    }
    
    public Enchere_Server(ArrayList<User> users, ArrayList<Auction> auctions) throws RemoteException {
        this.users = users;
        this.auctions = auctions;
        n_users = users.size() + 1;
        n_auctions = auctions.size() + 1;
        
    }

    

    @Override
    public boolean addAuction(Auction a) throws RemoteException {
        if (!auctions.add(a))
            return false;

        n_auctions++;
        return true;
    }

    public boolean addUser(User u) throws RemoteException {
        if (!users.add(u)) {
            return false;
        }

        n_users++;
        return true;
    }

    @Override
    public boolean placeBid(Auction auct, User bidder, double bid)
            throws RemoteException {
        if (!isRegistered(bidder))
            return false;
        if (!isActive(auct))
            return false;

        return auct.placeBid(bidder, bid);
    }

    public boolean isActive(Auction auct) throws RemoteException {
        if (!auctions.contains(auct))
            return false;
        return auct.isActive();
    }

    public boolean isRegistered(User bidder) throws RemoteException {
        return users.contains(bidder);
    }

    public boolean isRegistered(String login) throws RemoteException {
        for (User u : users) {
            if (u.getLogin().equals(login))
                return true;
        }
        return false;
    }

    public User getByLogin(String login) {
        for (User u : users) {
            if (u.getLogin().equals(login))
                return u;
        }
        return null;
    }

    @Override
    public User connection(String login, String password)
            throws RemoteException {
        User search = getByLogin(login);
        if (search == null)
            return null;
        if (search.getPassword().equals(password))
            return search;
        return null;

    }

    @Override
    public boolean register(String login, String password)
            throws RemoteException {
        if (isRegistered(login))
            return false;

        return addUser(new User(n_users, login, password));
    }
    
    @Override
    public ArrayList<Auction> getAllAuctions() throws RemoteException {
        return auctions;
    }

    @Override
    public ArrayList<Auction> getOwnAuctions(User u) throws RemoteException {
        ArrayList<Auction> ownAuctions = new ArrayList<Auction>();
        for(Auction a : auctions) {
            if(u.equals(a.getCreator())) ownAuctions.add(a);
        }
        
        return ownAuctions;
    }
    
    public static void main(String[] args) {
        // Création du serveur
        InterfaceServer o = null;
        try {
            o = new Enchere_Server();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Enregistrement du serveur dans le registre RMI
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(2001);
            reg.bind("RMI_Enchere_Serveur", o);
        } catch (AccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
