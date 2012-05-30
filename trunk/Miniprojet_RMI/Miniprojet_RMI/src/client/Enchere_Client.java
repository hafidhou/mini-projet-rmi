package client;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import server.Auction;
import server.InterfaceServer;
import server.InterfaceServeurEnregistre;
import server.User;

public class Enchere_Client {

    InterfaceServer            server;
    InterfaceServeurEnregistre server_auth;
    Scanner                    lecteur;
    User                       currentUser = null;

    public Enchere_Client(InterfaceServer is, InterfaceServeurEnregistre ise) {
        if (is == null)
            throw new RuntimeException();
        if (ise == null)
            throw new RuntimeException();

        server = is;
        server_auth = ise;
        lecteur = new Scanner(System.in);
    }

    // Ex�cution du client
    public static void main(String[] args) throws AccessException,
            RemoteException, NotBoundException {

        // Connexion avec le serveur
        InterfaceServer server1 = null;
        InterfaceServeurEnregistre server2 = null;
        try {
            Object server = Naming
                    .lookup("rmi://localhost:2001/RMI_Enchere_Serveur");
            server1 = (InterfaceServer) server;
            server2 = (InterfaceServeurEnregistre) server;
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        // Lancement du client
        Enchere_Client c = new Enchere_Client(server1, server2);
        c.run();
    }

    // Ex�cution du client
    public void run() {
        menu_principal();
    }

    private boolean authenticate() {
        System.out.println("== Se connecter ==");
        System.out.print("Entrez votre pseudo : ");
        String login = lireString();
        System.out.print("Entrez votre mot de passe : ");
        String mdp = lireString();

        // Connexion c�t� serveur
        User success = null;
        try {
            success = server.connection(login, mdp);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // R�sultat de la connexion
        if (success != null) {
            System.out.println("\nConnecte !");
            currentUser = success;
            return true;
        } else {
            System.out.println("\nMauvais nom d'utilisateur ou mot de passe");
            return false;
        }
    }

    private void register() {
        System.out.println("== Creer un compte ==");
        System.out.print("Entrez un pseudo : ");
        String login = lireString();
        System.out.print("Entrez un mot de passe : ");
        String mdp = lireString();

        // Creation du compte cote serveur
        boolean success = false;
        try {
            success = server.register(login, mdp);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (success) {
            System.out.println("\nInscription complete");
        } else {
            System.out.println("\nErreur lors de l'inscription");
        }
    }

    // Menu principal
    // Choix 1 : S'inscrire
    // Choix 2 : Se connecter
    // Choix 3 : Consulter toutes les enchères
    // Choix 4 : Consulter ses enchères
    // Choix 5 : Consulter une enchère et enchérir
    // Choix 6 : Créer une enchère
    // Choix 7 : Quitter
    private void menu_principal() {
        boolean qOk = true;

        while (qOk) {
            System.out.println("=== Bienvenue sur EbayRMI ===");
            System.out.println("1 - Creer un compte");
            if (currentUser == null)
                System.out.println("2 - Se connecter");
            else
                System.out.println("2 - Se deconnecter");
            System.out.println("3 - Consulter toutes les encheres");
            System.out.println("4 - Consulter ses encheres");
            System.out.println("5 - Consulter une enchere");
            System.out.println("6 - Creer une enchere");
            System.out.println("7 - Quitter");

            int choix = 0;
            do {
                choix = lireInt();
                if (choix < 1 || choix > 7)
                    System.out.println("Entrez un entier entre 1 et 5 !");
            } while (choix < 1 || choix > 7);

            switch (choix) {
                case 1:
                    register();
                    break;
                case 2:
                    if (currentUser == null)
                        authenticate();
                    else
                        deconnecter();
                    break;
                case 3:
                    consulter_toutes_encheres();
                    break;
                case 4:
                    consulter_propres_encheres();
                    break;
                case 5:
                    consulter_enchere();
                    break;
                case 6:
                    creer_enchere();
                    break;
                case 7:
                    qOk = false;
                    deconnecter();
                    break;
            }
        }
    }

    private void consulter_enchere() {
        System.out.print("Veuillez saisir un ID d'enchere : ");
        int choix = lireInt();
        Auction a = null;
        try {
            a = server.getAuctionById(choix);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (a != null) {
            System.out.println(a);
            System.out.println("Voulez-vous enchérir ? Entrez o si oui.");
            String c = lireString();
            if (c.equals("o"))
                encherir(choix);
        } else {
            System.out.println("Cette enchère n'est pas valide");
        }
    }

    private void creer_enchere() {
        System.out.println("== Creer une enchere ==");
        System.out.print("Entrez une duree (en jours) : ");
        int duree = lireInt();
        System.out.print("Entrez une description : ");
        String desc = lireString();

        double montant = -1;
        while (montant < 0) {
            System.out.print("Entrez un montant de depart : ");
            montant = lireDouble();
        }

        boolean success = false;
        try {
            success = server_auth.addAuction(duree, desc, montant, currentUser);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (success) {
            System.out.println("\nVotre enchere a bien ete creee");
        } else {
            System.out.println("\nErreur lors de la creation d'enchere");
        }
    }

    private void consulter_propres_encheres() {
        ArrayList<Auction> auctions = null;
        try {
            auctions = server_auth.getOwnAuctions(currentUser);

            for (Auction a : auctions) {
                System.out.println(a.toString());
                System.out.println("\n-------------------\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void consulter_toutes_encheres() {
        ArrayList<Auction> auctions = null;
        try {
            auctions = server.getAllAuctions();
            for (Auction a : auctions) {
                System.out.println(a.toString());
                System.out.println("\n-------------------\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void encherir(int numero) {
        Auction a = null;
        try {
            a = server.getAuctionById(numero);
            if (!a.getCreator().getLogin().equals(currentUser.getLogin())) {
                double choix = 0;
                do {
                    System.out
                            .print("Choisir le nouveau montant de l'enchere : ");
                    choix = lireDouble();
                    if (choix <= a.getBid())
                        System.out.println("Choisir un montant correct.");
                } while (choix <= a.getBid());

                if (server_auth.placeBid(a, currentUser, choix))
                    System.out.println("L'offre d'enchère a été placée.");
                else
                    System.out.println("Erreur lors du placement de l'offre.");
            } else
                System.out
                        .println("Impossible d'encherir sur sa propre enchere.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deconnecter() {
        currentUser = null;
    }

    // Service de lecture d'entier
    private int lireInt() {
        String ligne = lecteur.nextLine();
        int number = 0;
        try {
            number = Integer.parseInt(ligne);
        } catch (Exception e) {
            return 0;
        }
        return number;
    }

    // Service de lecture de chaîne
    private String lireString() {
        return lecteur.nextLine();
    }

    // Service de lecture de double
    private double lireDouble() {
        String ligne = lecteur.nextLine();
        double number = 0;
        try {
            number = Double.parseDouble(ligne);
        } catch (Exception e) {
            return 0;
        }
        return number;
    }
}
