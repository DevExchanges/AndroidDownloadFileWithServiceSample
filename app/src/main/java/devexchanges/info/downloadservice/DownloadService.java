package devexchanges.info.downloadservice;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

public class DownloadService extends IntentService {

    public static final String URL = "url";
    public static final String FILENAME = "name";
    public static final String FILEPATH = "path";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "notification";

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        int result = Activity.RESULT_CANCELED;

        try {
            URL url = new URL(urlPath);
            InputStream input = url.openStream();
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
            File storagePath = new File(Environment.getExternalStorageDirectory() + "/Pictures");
            OutputStream output = new FileOutputStream(new File(storagePath, fileName));
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
                result = Activity.RESULT_OK;
            } finally {
                output.close();
                input.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        publishResults(result);
    }

    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
