package client;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.InterfaceServer;

public class Enchere_Client {
    public static void main(String[] args) throws AccessException, RemoteException, NotBoundException {
        InterfaceServer o =null;
        try {
            o = (InterfaceServer) Naming.lookup("rmi://localhost:2001/Ding");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }
}
