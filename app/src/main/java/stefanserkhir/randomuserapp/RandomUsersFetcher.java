package stefanserkhir.randomuserapp;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RandomUsersFetcher {
    private static final String TAG = "RandomUsersFetcher";

    public byte[] getURLBytes(String stringURL) throws IOException {
        URL url = new URL(stringURL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with" + stringURL);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getURLContent(String stringURL) throws IOException {
        return new String(getURLBytes(stringURL));
    }

    public void fetchRandomUsers() {
        try {
            String stringURL = Uri.parse("https://randomuser.me/api/")
                    .buildUpon()
                    .appendQueryParameter("results", "40")
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("noinfo", "1")
                    .build().toString();
            String jsonString = getURLContent(stringURL);
            Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch users", e);
        }
    }
}
