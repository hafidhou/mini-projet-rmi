package server;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class EnchereLoginModule implements LoginModule {
    private Subject         subject;
    private CallbackHandler callbackHandler;
    private Map             sharedState;
    private Map             options;
    private boolean         success;

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean commit() throws LoginException {
        return success;
    }

    @Override
    public void initialize(Subject arg0, CallbackHandler arg1,
            Map<String, ?> arg2, Map<String, ?> arg3) {
        subject = arg0;
        callbackHandler = arg1;
        sharedState = arg2;
        options = arg3;

    }

    @Override
    public boolean login() throws LoginException {
        if (callbackHandler == null) {
            throw new LoginException("Pas de callbackHandler");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Login :");
        callbacks[1] = new PasswordCallback("Mot de passe :", false);

        try {
            callbackHandler.handle(callbacks);
        } catch (IOException e) {
            throw new LoginException(
                    "IOException");
        } catch (UnsupportedCallbackException e) {
            throw new LoginException(
                    "UnsupportedCallbackException");
        }

        NameCallback nameCallback = (NameCallback) callbacks[0];
        PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];

        String name = nameCallback.getName();
        String password = new String(passwordCallback.getPassword());
        
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return false;
    }

}
