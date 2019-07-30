package com.taksycraft.testapplicatons.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.taksycraft.testapplicatons.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;

public class UploadingFileActivity extends AppCompatActivity {
    String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjU5MTkxYjBhOTg1NGVjNGJlOTJjZWE3ZmJkMTIzM2ZlNjBlN2MxYTg5N2FkMWVhNGQwMWU5M2FlZTYzODVmYjI1NTdmMzc1NWY3NGM4MDljIn0.eyJhdWQiOiIxIiwianRpIjoiNTkxOTFiMGE5ODU0ZWM0YmU5MmNlYTdmYmQxMjMzZmU2MGU3YzFhODk3YWQxZWE0ZDAxZTkzYWVlNjM4NWZiMjU1N2YzNzU1Zjc0YzgwOWMiLCJpYXQiOjE1NjA1MTYwMDQsIm5iZiI6MTU2MDUxNjAwNCwiZXhwIjoxNTkyMTM4NDAzLCJzdWIiOiI0NjYiLCJzY29wZXMiOltdfQ.tBTH5nGK_WIkxlblKquEzXS6RatmlD5soaIe-WNhwVSO6Xa68EPakQekxLoK3iPFb7f4ykkKxbHz4LU-HA2-HoZFDTzDKjrOS5R65z_-MF5E5GvppeFTBihPkGivCT2sPDh-XprKg_1ejDTQ243dQN4nAOJ3cTyeQR4alnlmnaQ_Do6i3qidI0RGhOEu5F7BgwymcfGRFyM2UbEVVH14RayeZHMzoMpQbbUo4d7RsShbYUHMZJk1JgK7yb4nqodKKg1BEPbCSbZNbgsY8hnNNjQbYE-7tT26k5b3G2YrYa7ndsDLARkMuqIpo29aALGhdycb0hQTCGcx2njj6nXaVb3rvXQe6FvvihNDJYh6YE3VpnHy_Xcybrf9yaL-_Z_7hr8HzCW0YCgdTU5x9T6rdJsSrZVwVjrG5wkkmKKUBhShPOlRsJ-R83nX-ONkhHBpFyn0C9y8VFyZZhTdS9gJObiy2a4hviHWHj2BViWbrdcUpDP1fES6q510V__6l_EZcoYBmgxXT52EpHcK7Mwf19XOs2b_jdrOGv9aWNbGCUlnYepU6CLGNJIezc4T3UlIss_EhF7U3m2rYW0Tm1Ka-kuT7Dn7CmMQyeXWwdOQIJkBqW7vzJrVbBK8ehGw8BXOAbJkoVLi8cdWOypcn1UROsiWT3teDLBHAeocUtdJvko";

    private View btnUpload;
    private String selectedPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploading);
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response = null;
                        try {
                            new Upload().execute();
//                            response = postImageToImaggaNew("/storage/emulated/0/.LocusAlpha/ByBlosss/Recording/1560767329526.mp4");
//                            response = postImageToImagga("/storage/emulated/0/.LocusAlpha/ByBlosss/Recording/1560767329526.mp4");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.i("imagga", response+
                                "");
                    }
                }).start();
            }
        });
    }
    public class PostImageToImaggaAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String response = postImageToImagga("/storage/emulated/0/.LocusAlpha/ByBlosss/Recording/1560767329526.mp4");
                Log.i("imagga", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    public String postImageToImaggaNew(String filepath) throws Exception {
//        String requestURL = "http://staging.orb-in-rooms-tab-api.myryd.com/orbtab/api/v1/file-upload";
        String requestURL = "http://192.168.0.102:8081/fileupload";
//        String requestURL = "https://api.wit.ai/speech?v=20160526";
        URL url = new URL(requestURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);

        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("Authorization", "Bearer "+authToken);;
//        httpConn.setRequestProperty("Content-Type", "audio/wav");;
//        httpConn.setRequestProperty("Content-Type", "video/mp3");;
        File waveFile= new File(filepath);
//        byte[] bytes = Files.readAllBytes(waveFile.toPath());
//        RandomAccessFile f = new RandomAccessFile(waveFile, "r");
//        byte[] bytes = new byte[(int)f.length()];
//        f.readFully(bytes);


//        DataOutputStream request = new DataOutputStream(httpConn.getOutputStream());
        //        request.write(bytes);

        String twoHyphens = "--";
        String boundary =  "*****"+Long.toString(System.currentTimeMillis())+"*****";
        String lineEnd = "\r\n";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        String filefield = "file";

        String[] q = filepath.split("/");
        int idx = q.length - 1;
//



        DataOutputStream request = new DataOutputStream(httpConn.getOutputStream());
//        request.writeBytes(twoHyphens + boundary + lineEnd);
//        request.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] +"\"" + lineEnd);
//        request.writeBytes("Content-Type: Video/mp3" + lineEnd);
//        request.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
//        request.writeBytes(lineEnd);

        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while(bytesRead > 0) {
            request.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }





        request.flush();
        request.close();

        String response = "";
// checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new BufferedInputStream(httpConn.getInputStream());

            BufferedReader responseStreamReader
                    = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return  response;

    }
    public String postImageToImagga(String filepath) throws Exception {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary =  "*****"+Long.toString(System.currentTimeMillis())+"*****";
        String lineEnd = "\r\n";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        String filefield = "image";

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);

        URL url = new URL("https://api.imagga.com/v2/content");
//        URL url = new URL("http://staging.orb-in-rooms-tab-api.myryd.com/orbtab/api/v1/file-upload");
        connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
//        connection.setRequestProperty("Authorization", "Bearer "+authToken);

        outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] +"\"" + lineEnd);
        outputStream.writeBytes("Content-Type: Video/mp3" + lineEnd);
        outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        while(bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        inputStream = connection.getInputStream();

        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            inputStream.close();
            connection.disconnect();
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            return response.toString();
        } else {
            throw new Exception("Non ok response returned");
        }
    }
    class Upload extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
//            new Upload().execute();
            try {
                HashMap<String, String> data = new HashMap<>();
//                selectedPath =FilesUtils.getImageFolderPath()+"f7f2e2fe2d18ab28ba7e45ad8a1cb5ff641b9a85.png";
                selectedPath ="/storage/emulated/0/.LocusAlpha/ByBlosss/Recording/1560775907797.mp3";
                data.put("file", selectedPath);
                data.put("name", "file");
//                data.put("uploadedfile", selectedPath);
//                data.put("name", "uploadedfile");
                new HttpMultipartUpload().upload(new URL("http://staging.orb-in-rooms-tab-api.myryd.com/orbtab/api/v1/file-upload"),
                        new File(selectedPath), "file", data);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
    public class HttpMultipartUpload {

        public   String lineEnd = "\r\n";
        public  String twoHyphens = "--";
        public     String boundary = "AaB03x87yxdkjnxvi7";

        public   String upload(URL url, File file, String fileParameterName, HashMap<String, String> parameters)
                throws IOException {
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            DataInputStream dis = null;
            FileInputStream fileInputStream = null;

            byte[] buffer;
            int maxBufferSize = 20 * 1024;
            try {
                //------------------ CLIENT REQUEST
                fileInputStream = new FileInputStream(file);

                // open a URL connection to the Servlet
                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                // Allow Inputs
                conn.setDoInput(true);
                // Allow Outputs
                conn.setDoOutput(true);
                // Don't use a cached copy.
                conn.setUseCaches(false);
                // Use a post method.
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"" + fileParameterName
                        + "\"; filename=\"" + file.toString() + "\"" + lineEnd);
//                dos.writeBytes("Content-Type: text/xml" + lineEnd);
                dos.writeBytes("Content-Type: audio/mpeg" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                buffer = new byte[Math.min((int) file.length(), maxBufferSize)];
                int length;
                // read file and write it into form...
                while ((length = fileInputStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, length);
                }

                for (String name : parameters.keySet()) {
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(parameters.get(name));
                }

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                dos.flush();
            } finally {
                if (fileInputStream != null) fileInputStream.close();
                if (dos != null) dos.close();
            }

            //------------------ read the SERVER RESPONSE
            try {
                dis = new DataInputStream(conn.getInputStream());
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = dis.readLine()) != null) {
                    response.append(line).append('\n');
                }

                return response.toString();
            } catch (Exception e)
            {
                e.printStackTrace();
            }finally {
                if (dis != null) dis.close();
                return "erer ";
            }
        }

    }
}
