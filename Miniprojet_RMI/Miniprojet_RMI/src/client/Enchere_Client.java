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
import server.User;

public class Enchere_Client {

    InterfaceServer server;
    Scanner         lecteur;
    User 			currentUser = null;

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
            currentUser = success;
        } else {
            System.out.println("\nMauvais nom d'utilisateur ou mot de passe");
        }
        
        menu_principal();
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
    
    private void menu_principal() {
    	boolean qOk = true;
    	
    	while(qOk) {
	    	System.out.println("=== Menu principal ===");
			System.out.println("1 - Créer une enchère");
			System.out.println("2 - Consulter ses propres enchères");
			System.out.println("3 - Consulter toutes les enchères");
			System.out.println("4 - Se déconnecter");
			
			int choix = 0;
			do {
			    choix = lireInt();
			    if (choix < 1 || choix > 4)
			        System.out.println("Entrez un entier entre 1 et 4 !");
			} while (choix < 1 || choix > 4);
			
			switch (choix) {
			case 1 :
				creer_enchere();
				break;
			case 2 :
				consulter_propres_encheres();
				break;
			case 3 :
				consulter_toutes_encheres();
				break;
			case 4 :
				qOk = false;
				deconnecter();
				break;
			}
    	}
    }
    
    private void creer_enchere() {
    	System.out.println("== Créer une enchère ==");
        System.out.print("Entrez une durée (en jours) : ");
        int duree = lireInt();
        System.out.print("Entrez une description : ");
        String desc = lireString();
        
        double montant = -1;
        while(montant < 0) {
	        System.out.print("Entrez un montant de départ : ");
	        montant = lireDouble();
        }
        
        boolean success = false;
        try {
            success = server.addAuction(duree,desc,montant,currentUser);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (success) {
            System.out.println("\nVotre enchère a bien été créée");
        } else {
            System.out.println("\nErreur lors de la création d'enchère");
        }
    }
    
    private void consulter_propres_encheres() {
    	ArrayList<Auction> auctions = null;
    	try {
    		auctions = server.getOwnAuctions(currentUser);

        	for(Auction a : auctions) {
        		System.out.println(a.toString());
        		System.out.println("\n------------------------------------------------------\n");
        	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    private void consulter_toutes_encheres() {
    	ArrayList<Auction> auctions = null;
    	try {
    		auctions = server.getAllAuctions();

        	for(Auction a : auctions) {
        		System.out.println(a.toString());
        		System.out.println("\n------------------------------------------------------\n");
        	}
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
    private void deconnecter() {
    	currentUser = null;
    }

    // Service de lecture d'entier
    private int lireInt() {
        String ligne = lecteur.nextLine();
        return Integer.parseInt(ligne);
    }

    private String lireString() {
        return lecteur.nextLine();
    }
    
    private double lireDouble() {
    	String ligne = lecteur.nextLine();
        return Double.parseDouble(ligne);
    }
}
