package server;

import java.io.Serializable;

public class User implements Serializable {
        
        private int idUser;
        private String loginUser;
        private String passwordUser;
        
        public User(int id, String login, String password) {
            idUser = id;
            loginUser = login;
            passwordUser = password;
        }
        
        public String getLogin() { return loginUser; }
        
        public String getPassword() { return passwordUser; }
}
