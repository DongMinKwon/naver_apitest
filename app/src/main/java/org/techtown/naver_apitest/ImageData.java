package org.techtown.naver_apitest;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageData {
    Bitmap img;
    int imgHeight;
    int imgWidth;

    String search_word;
    final String ClientID = "olbjaQTrUKUCmk9JaUGs";
    final String ClientSecret = "mMhcq4yzaq";

    public ImageData(String search_word) {
        this.imgHeight = 0;
        this.imgWidth = 0;
        this.search_word = search_word;
    }

    private String getdata() throws IOException {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> getDataTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();

                String query = search_word;

                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://openapi.naver.com/v1/search/image").newBuilder();
                urlBuilder.addQueryParameter("query", query);
                urlBuilder.addQueryParameter("display", "3");

                String url = urlBuilder.build().toString();

                Request req = new Request.Builder()
                        .url(url)
                        .header("X-Naver-Client-Id", ClientID)
                        .addHeader("X-Naver-Client-Secret", ClientSecret)
                        .build();

                String myResponse = "";
                try {
                    myResponse = client.newCall(req).execute().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return myResponse;
            }
        };

        String myreq = "";
        try {
            myreq = getDataTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return myreq;
    }

    public void setimg() throws IOException {

        String total_data = "";
        try {
            total_data = getdata();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        final DataModel data = gson.fromJson(total_data, DataModel.class);

        imgHeight = Integer.parseInt(data.items.get(0).getSizeheight());
        imgWidth = Integer.parseInt(data.items.get(0).getSizewidth());

        final String img_link = data.items.get(0).getLink();

        @SuppressLint("StaticFieldLeak") AsyncTask<Bitmap, Bitmap, Bitmap> imgTask = new AsyncTask<Bitmap, Bitmap, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Bitmap... bitmaps) {

                Bitmap image = null;
                try {
                    URL url = new URL(img_link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    image = BitmapFactory.decodeStream(input);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        };
        Bitmap bit_img = null;
        try {
            bit_img = imgTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        img = bit_img;
    }

    public Bitmap getImg() {
        return img;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }
}
