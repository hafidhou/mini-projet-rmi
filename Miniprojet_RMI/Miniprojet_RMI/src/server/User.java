package server;

import java.io.Serializable;

public class User implements Serializable {
        
        int idUser;
        String loginUser;
        String passwordUser;
        
        public User(int id, String login, String password) {
            idUser = id;
            loginUser = login;
            passwordUser = password;
        }
        
        public String getLogin() { return loginUser; }
        
        public String getPassword() { return passwordUser; }
}
