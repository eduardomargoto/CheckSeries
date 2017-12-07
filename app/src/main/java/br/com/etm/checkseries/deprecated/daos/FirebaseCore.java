package br.com.etm.checkseries.deprecated.daos;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import br.com.etm.checkseries.deprecated.domains.Profile;

/**
 * Created by EDUARDO_MARGOTO on 13/04/2016.
 */
public class FirebaseCore {
    private static Firebase firebase;

    private static String DOMAIN_APP = "amber-heat-2927";

    public static Firebase getInstance() {
        if (firebase == null)
            firebase = new Firebase("https://" + DOMAIN_APP + ".firebaseio.com");

        return firebase;
    }

    public static void logIn_Google(final Context context) {
        if (getInstance().getAuth() == null) {
            Log.i("FIREBASE", "tOKEN: " + Profile.getInstance().getTokenFB());
            if (!Profile.getInstance().getTokenFB().isEmpty()) {

                getInstance().authWithOAuthToken("google", Profile.getInstance().getTokenFB(), new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                new DAO_Profile(context).edit();
                                Log.d("LOGIN", "OK");
                                FirebaseCore.getInstance().child("users").child(Profile.getInstance().getId()).setValue(Profile.getInstance());
                                logOut();
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                Log.d("LOGIN", "ERROR");
                                Toast.makeText(context, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        }
    }

    public static void logOut() {
        getInstance().unauth();
    }

}
