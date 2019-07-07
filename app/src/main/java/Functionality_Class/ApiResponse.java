package Functionality_Class;

import android.content.Context;
import android.util.Log;

import com.StudentFaclity.Stulity.R;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Controllers.Config;


// All Api Request Method class
public class ApiResponse {
    String result;

    HttpURLConnection urlConnection;
    String response;
    URL url1;
    private InputStream inputStream;


    //Get request method
    public String ResponseGet(Context context, String str) {
        try {
            Log.e("URL","URL"+str);
            url1 = new URL(str);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.connect();

            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputStream ins = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(ins));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
                br.close();
                urlConnection.disconnect();
            } else if (HttpResult == HttpURLConnection.HTTP_NOT_FOUND) {
                response = context.getString(R.string.error_Http_not_found);
            } else if (HttpResult == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                response = context.getString(R.string.error_Http_internal);
            } else {
                response = context.getString(R.string.error_Http_other);
            }
        } catch (MalformedURLException e) {
            urlConnection.disconnect();
            response = "Error";
        } catch (IOException e) {
            urlConnection.disconnect();
            response = "Error";
        }
        return response;
    }




    public String Response_Post(Context context, String url, JSONObject data) {
        StringBuilder sb = new StringBuilder();
        try {
            url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.connect();

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(String.valueOf(data));
            out.close();

            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                response = sb.toString();
            } else if (HttpResult == HttpURLConnection.HTTP_NOT_FOUND) {
                response = context.getString(R.string.error_Http_not_found);
            } else if (HttpResult == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                response = context.getString(R.string.error_Http_internal);
            } else {
                response = context.getString(R.string.error_Http_other);
            }

            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            urlConnection.disconnect();
            response = context.getString(R.string.error);
        } catch (IOException e) {
            urlConnection.disconnect();
            response = context.getString(R.string.error);
        }

        return response;
    }

}