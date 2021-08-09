package org.techtown.naver_apitest;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

public class ImgEditor {
    Bitmap img;
    ArrayList<Bitmap> smallImgList = new ArrayList<>();
    ArrayList<Integer> bitList = new ArrayList<>();

    public ImgEditor(Bitmap img) {
        this.img = img;
    }

    public Bitmap resize(){

        int length = 0;
        if(img.getWidth() < img.getHeight()) length = img.getWidth();
        else length = img.getHeight();

        length -= (length % 20);

        Bitmap resize_bitmap = Bitmap.createScaledBitmap(img, length, length, false);

        img=resize_bitmap;
        return img;
    }

    public Bitmap grayScale(){
        int width = img.getWidth();
        int height = img.getHeight();

        Bitmap gray_bitmap = Bitmap.createBitmap(width, height, img.getConfig());

        int A, R, G, B;
        int pixel;
        for(int x = 0; x < width; x++){
            for(int y = 0; y<height; y++){
                pixel = img.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

                if(gray > 128) gray = 255;
                else gray = 0;
                gray_bitmap.setPixel(x, y, Color.argb(A, gray, gray, gray));
            }
        }

        img = gray_bitmap;
        return gray_bitmap;
    }

    public void cutImg(){
        int length = img.getWidth() / 20;
        Bitmap sub_bitmap;

        for(int h = 0; h<20; h++){
            for(int w = 0; w<20; w++){
                sub_bitmap = Bitmap.createBitmap(img, w*length, h*length, length, length);
                smallImgList.add(sub_bitmap);
            }
        }
    }

    public ArrayList<Bitmap> getImgList(){
        return smallImgList;
    }

    public ArrayList<Integer> getBitList(){

        for(int i = 0; i<smallImgList.size(); i++){
            Bitmap sub_Bitmap = smallImgList.get(i);

            int total_value = 0;
            int width = sub_Bitmap.getWidth();
            int height = sub_Bitmap.getHeight();
            int total_pixel = width*height;

            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    int pixel = sub_Bitmap.getPixel(x, y);
                    total_value += Color.red(pixel);
                }
            }
            if((total_value/total_pixel) > 128) bitList.add(-3);
            else bitList.add(-2);

        }

        return bitList;
    }

}
