package client;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import server.InterfaceServer;
import server.User;

public class Enchere_Client {

    InterfaceServer server;
    Scanner         lecteur;

    public Enchere_Client(InterfaceServer is) {
        if (is == null)
            throw new RuntimeException();

        server = is;
        lecteur = new Scanner(System.in);
    }

    // Exécution du client
    public static void main(String[] args) throws AccessException,
            RemoteException, NotBoundException {

        // Connexion avec le serveur
        InterfaceServer o = null;
        try {
            o = (InterfaceServer) Naming
                    .lookup("rmi://localhost:2001/RMI_Enchere_Serveur");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        // Lancement du client
        Enchere_Client c = new Enchere_Client(o);
        c.run();
    }

    // Exécution du client
    public void run() {
        int choice = 0;
        while (choice != 3) {
            choice = menu_auth();
            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    authenticate();
                    break;
            }
        }
    }

    // Menu de départ
    // 1 : s'inscrire
    // 2 : s'identifier
    // 3 : quitter
    private int menu_auth() {
        System.out
                .println("=== Bienvenue sur la contrefaçon chinoise d'Ebay ===");
        System.out.println("1 - Créer un compte");
        System.out.println("2 - Se connecter");
        System.out.println("3 - Quitter");

        int choix = 0;
        do {
            choix = lireInt();
            if (choix >= 1 || choix <= 3)
                return choix;
            else
                System.out.println("Entrez un entier entre 1 et 3 !");
        } while (choix < 1 || choix > 3);

        return 0;
    }

    private void authenticate() {
        System.out.println("== Se connecter ==");
        System.out.print("Entrez votre pseudo : ");
        String login = lireString();
        System.out.print("Entrez votre mot de passe : ");
        String mdp = lireString();

        // Connexion côté serveur
        User success = null;
        try {
            success = server.connection(login, mdp);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Résultat de la connexion
        if (success != null) {
            System.out.println("\nConnecté !");
        } else {
            System.out.println("\nMauvais nom d'utilisateur ou mot de passe");
        }
    }

    private void register() {
        System.out.println("== Créer un compte ==");
        System.out.print("Entrez un pseudo : ");
        String login = lireString();
        System.out.print("Entrez un mot de passe : ");
        String mdp = lireString();

        // Création du compte côté serveur
        boolean success = false;
        try {
            success = server.register(login, mdp);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (success) {
            System.out.println("\nInscription complète");
        } else {
            System.out.println("\nErreur lors de l'inscription");
        }
    }

    // Service de lecture d'entier
    private int lireInt() {
        String ligne = lecteur.nextLine();
        return Integer.parseInt(ligne);
    }

    private String lireString() {
        return lecteur.nextLine();
    }
}
