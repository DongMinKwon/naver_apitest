package org.techtown.naver_apitest;

import java.util.ArrayList;

public class SetClue {
    ArrayList<Integer> board = new ArrayList<>();

    ArrayList<Integer> bitList;
    ArrayList<Integer>[] row = new ArrayList[20];
    ArrayList<Integer>[] column = new ArrayList[20];

    int width;

    public SetClue(ArrayList<Integer> bitList) {
        this.bitList = bitList;
        this.width = 1;
    }

    public ArrayList<Integer> getClueList(){
        for(int i = 0; i<20; i++){
            row[i] = new ArrayList<>();
            column[i] = new ArrayList<>();
        }

        //row
        for(int x = 0; x < 20; x++){
            int conNum = 0;

            for(int y = 0; y < 20; y++){
                if(bitList.get(20*x+y) == -2){
                    conNum += 1;
                }
                else{
                    if(conNum != 0){
                        row[x].add(conNum);
                        conNum = 0;
                    }
                }
            }
            if(conNum != 0) row[x].add(conNum);
        }

        //column
        for(int y = 0; y < 20; y++){
            int conNum = 0;
            for(int x = 0; x < 20; x++){
                if(bitList.get(20*x+y) == -2){
                    conNum += 1;
                }
                else{
                    if(conNum != 0){
                        column[y].add(conNum);
                        conNum = 0;
                    }
                }
            }
            if(conNum != 0) column[y].add(conNum);
        }

        // set new bitlist
        int max_row = 0;
        for(int x = 0; x<20; x++){
            if(row[x].size() > max_row) max_row = row[x].size();
        }

        int max_column = 0;
        for(int y = 0; y<20; y++){
            if(column[y].size() > max_column) max_column = column[y].size();
        }

        width = max_row + 20;

        for(int x = 0; x < max_column + 20; x++){
            for(int y = 0; y < max_row + 20; y++){
                if(x < max_column && y < max_row) board.add(-1);
                else if(x < max_column && y >= max_row){
                    if(x < (max_column - column[y-max_row].size())) board.add(0);
                    else board.add(column[y-max_row].get(x - (max_column - column[y-max_row].size())));
                }
                else if(x >= max_column && y < max_row){
                    if(y < (max_row - row[x-max_column].size())) board.add(0);
                    else board.add(row[x-max_column].get(y - (max_row - row[x-max_column].size())));
                }
                else{
                    board.add(bitList.get(20*(x-max_column)+(y-max_row)));
                }

            }
        }

        return board;
    }

    public int getWidth() {
        return width;
    }
}
