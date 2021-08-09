package org.techtown.naver_apitest;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class InGame {
    Context context;
    ArrayList<Integer> correct_board;
    ArrayList<Integer> game_board = new ArrayList<>();

    public InGame(Context context, ArrayList<Integer> correct_board) {
        this.context = context;
        this.correct_board = correct_board;
    }

    public void initialize(){
        ArrayList<Integer> tmp_board = new ArrayList<>();

        for(int i = 0; i<correct_board.size(); i++){
            int num = correct_board.get(i);
            if(num == -2) tmp_board.add(-3);
            else tmp_board.add(num);
        }

        game_board = tmp_board;
    }

    public ArrayList<Integer> getGameBoard(){
        return game_board;
    }

    public ArrayList<Integer> getCorrectBoard(){
        return correct_board;
    }

    public void setBlack(int idx){
        if(game_board.get(idx) == -3) game_board.set(idx, -2);
    }

    public int isOver(){
        for(int i = 0; i<correct_board.size(); i++){
            if(game_board.get(i) != correct_board.get(i)) return 0;
        }

        Toast.makeText(context, "Finish!", Toast.LENGTH_SHORT).show();
        return 1;
    }

    public int isWrong(int idx){
        if(correct_board.get(idx) == -3) return 1;
        else return 0;
    }
}
