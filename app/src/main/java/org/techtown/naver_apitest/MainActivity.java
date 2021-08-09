package org.techtown.naver_apitest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imgview;
    TextView response_text;
    Button naverBtn;
    Button galleryBtn;
    GridView board;
    EditText search_query;

    Bitmap image;
    ArrayList<Bitmap> imgList = new ArrayList<>();
    ArrayList<Integer> answerList = new ArrayList<>();
    ArrayList<Integer> board_bit = new ArrayList<>();

    GridAdapter adapter;
    InGame game;
    SetClue clue;
    int gameOver = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgview = findViewById(R.id.imageView);
        response_text = findViewById(R.id.textView);
        naverBtn = findViewById(R.id.button2);
        board = findViewById(R.id.gridview);
        search_query = findViewById(R.id.edittext);
        galleryBtn = findViewById(R.id.button3);

        naverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String query = search_query.getText().toString();

                if(query.length() == 0){
                    Toast.makeText(getApplicationContext(), "please input search query", Toast.LENGTH_SHORT).show();
                    return;
                }

                //get image data from naver image searching
                ImageData imagedata = new ImageData(query);
                try {
                    imagedata.setimg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image=imagedata.getImg();

                editImage();
                startGame();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

    }

    public void startGame(){
        // set Clue numbers

        gameOver = 0;

        clue = new SetClue(answerList);
        board_bit = clue.getClueList();

        board.setNumColumns(clue.getWidth());

        System.out.println("boardbit size: "+String.valueOf(board_bit.size()));
        System.out.println("width: "+String.valueOf(clue.getWidth()));

        game = new InGame(getApplicationContext() ,board_bit);
        game.initialize();

        adapter = new GridAdapter(game.getGameBoard(), clue.getWidth());
        board.setAdapter(adapter);

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(gameOver == 0){
                    if(game.isWrong(position) == 1){
                        Toast.makeText(getApplicationContext(), "Wrong.", Toast.LENGTH_SHORT).show();
                        game.initialize();
                        adapter = new GridAdapter(game.getGameBoard(), clue.getWidth());
                        board.setAdapter(adapter);
                    }
                    else{
                        game.setBlack(position);
                        adapter.notifyDataSetChanged();
                        gameOver = game.isOver();
                    }
                }
            }
        });

        //test code
//        imgview.setImageBitmap(image);
//        response_text.setText(image.getHeight()+"  "+image.getWidth());


    }

    public void editImage(){

        //resize img data
        ImgEditor editor = new ImgEditor(image);
        image = editor.resize();

        //to black and white image
        image = editor.grayScale();

        //cut image
        editor.cutImg();
        imgList = editor.getImgList();

        //make 20x20 black-white list
        answerList = editor.getBitList();
        //System.out.println(answerList.get(258));



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 2){
                InputStream input = null;
                try {
                    input = getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bit_img = BitmapFactory.decodeStream(input);
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                image = bit_img;
                editImage();
                startGame();
            }

        }
    }

}
