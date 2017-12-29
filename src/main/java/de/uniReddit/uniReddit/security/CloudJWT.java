package de.uniReddit.uniReddit.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CloudJWT {

    public static String getGoogleAuthToken() throws IOException {

        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("https://www.googleapis.com/auth/cloud-platform");
        scopes.add("https://www.googleapis.com/auth/datastore");
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("unitalq-eb669dde3312.json")).createScoped(scopes);
        credential.refreshToken();

        return credential.getAccessToken();
    }
}
