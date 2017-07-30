package com.a3035347861.hku.smartphoneasg1;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by SHI Zhongqi on 2016-10-09.
 * Game Logic Singleton
 */


class gameLogic {
    private static gameLogic instance;
    private static final int player1=1;
    private static final int player2=-1;

    private Stack<Integer> steps;
    private int[][] gameboard=new int[7][6];
    private int[] gameStackHeight = new int[7];
    private int turn=player1;
    private int[][] winCondition1={{0,0},{1,0},{2,0},{3,0}};  //For _
    private int[][] winCondition2={{0,0},{0,1},{0,2},{0,3}};//For |
    private int[][] winCondition3={{0,0},{1,1},{2,2},{3,3}};//for /
    private int[][] winCondition4={{0,3},{1,2},{2,1},{3,0}};// for\
    private int[][] winKeys=new int[4][2];
    private ArrayList<Integer> winKeysList;
    private int winner=0;

    /**
     * @return the winner
     */
    int getWinner(){
        return winner;
    }

    /**
     * @return the keys that let the winner win
     */
    ArrayList<Integer> getWinKeys(){
        return winKeysList;
    }

    /**
     * @param column the column number on the gameboard
     * @return the Position of the Key
     */
    int play(int column){
        if(gameStackHeight[column]>5)
            return -1;
        else{
            gameboard[column][gameStackHeight[column]]=turn;
            steps.push(column+gameStackHeight[column]*7);
            switch(turn){
                case player1:
                    turn=player2;
                    break;
                case player2:
                    turn=player1;
                    break;
            }
            gameStackHeight[column]++;
        }
        judgeWin();
        return gameStackHeight[column]-1;
    }

    int[] retract(){
        int sum,x;
        int[] int_coordinate =new int[2];
        if(steps.size()==0)
            int_coordinate=null;
        else{
            sum=steps.pop();
            x=sum%7;
            gameStackHeight[x]--;
            gameboard[x][gameStackHeight[x]]=0;

            switch(turn){
                case player1:
                    turn=player2;
                    break;
                case player2:
                    turn=player1;
                    break;
            }
            int_coordinate[0]=x;
            int_coordinate[1]=gameStackHeight[x];
        }
        return int_coordinate;
    }

    int getSteps(){
        return steps.size();
    }

    int getTurn(){
        return this.turn;
    }

    private void judgeWin(){
        int result;
        if(steps.size()>6){
            //condition 1
            for(int m=0;m<6;m++) {
                for (int n = 0; n < 4; n++) {
                    result = gameboard[winCondition1[0][0]+n][winCondition1[0][1]+m];
                    result +=gameboard[winCondition1[1][0]+n][winCondition1[1][1]+m];
                    result +=gameboard[winCondition1[2][0]+n][winCondition1[2][1]+m];
                    result +=gameboard[winCondition1[3][0]+n][winCondition1[3][1]+m];
                    if (anyOneWin(result))
                        break;
                }
                if (winner != 0)
                    break;
            }

            //condition 2
            for(int m=0;m<3;m++) {
                for (int n = 0; n < 7; n++) {
                    result = gameboard[winCondition2[0][0]+n][winCondition2[0][1]+m];
                    result +=gameboard[winCondition2[1][0]+n][winCondition2[1][1]+m];
                    result +=gameboard[winCondition2[2][0]+n][winCondition2[2][1]+m];
                    result +=gameboard[winCondition2[3][0]+n][winCondition2[3][1]+m];
                    if (anyOneWin(result))
                        break;
                }
                if (winner != 0)
                    break;
            }

            //condition 3
            for(int m=0;m<3;m++) {
                for (int n = 0; n < 4; n++) {
                    result = gameboard[winCondition3[0][0]+n][winCondition3[0][1]+m];
                    result +=gameboard[winCondition3[1][0]+n][winCondition3[1][1]+m];
                    result +=gameboard[winCondition3[2][0]+n][winCondition3[2][1]+m];
                    result +=gameboard[winCondition3[3][0]+n][winCondition3[3][1]+m];
                    if (anyOneWin(result))
                        break;
                }
                if (winner != 0)
                    break;
            }
            //condition 4
            for(int m=0;m<3;m++) {
                for (int n = 0; n < 4; n++) {
                    result = gameboard[winCondition4[0][0]+n][winCondition4[0][1]+m];
                    result +=gameboard[winCondition4[1][0]+n][winCondition4[1][1]+m];
                    result +=gameboard[winCondition4[2][0]+n][winCondition4[2][1]+m];
                    result +=gameboard[winCondition4[3][0]+n][winCondition4[3][1]+m];
                    if (anyOneWin(result))
                        break;
                }
                if (winner != 0)
                    break;
            }

        }
    }

    private void seekAllWinKeys(int gameWinner){
        int result;
        if(steps.size()>6){
            //condition 1
            for(int m=0;m<6;m++) {
                for (int n = 0; n < 4; n++) {
                    result = gameboard[winCondition1[0][0]+n][winCondition1[0][1]+m];
                    result +=gameboard[winCondition1[1][0]+n][winCondition1[1][1]+m];
                    result +=gameboard[winCondition1[2][0]+n][winCondition1[2][1]+m];
                    result +=gameboard[winCondition1[3][0]+n][winCondition1[3][1]+m];
                    if(result==gameWinner)
                        markAllWinKeys(winCondition1,n,m);
                }
            }

            //condition 2
            for(int m=0;m<3;m++) {
                for (int n = 0; n < 7; n++) {
                    result = gameboard[winCondition2[0][0]+n][winCondition2[0][1]+m];
                    result +=gameboard[winCondition2[1][0]+n][winCondition2[1][1]+m];
                    result +=gameboard[winCondition2[2][0]+n][winCondition2[2][1]+m];
                    result +=gameboard[winCondition2[3][0]+n][winCondition2[3][1]+m];
                    if(result==gameWinner)
                        markAllWinKeys(winCondition2,n,m);
                }
            }

            //condition 3
            for(int m=0;m<3;m++) {
                for (int n = 0; n < 4; n++) {
                    result = gameboard[winCondition3[0][0]+n][winCondition3[0][1]+m];
                    result +=gameboard[winCondition3[1][0]+n][winCondition3[1][1]+m];
                    result +=gameboard[winCondition3[2][0]+n][winCondition3[2][1]+m];
                    result +=gameboard[winCondition3[3][0]+n][winCondition3[3][1]+m];
                    if(result==gameWinner)
                        markAllWinKeys(winCondition3,n,m);
                }
            }
            //condition 4
            for(int m=0;m<3;m++) {
                for (int n = 0; n < 4; n++) {
                    result = gameboard[winCondition4[0][0]+n][winCondition4[0][1]+m];
                    result +=gameboard[winCondition4[1][0]+n][winCondition4[1][1]+m];
                    result +=gameboard[winCondition4[2][0]+n][winCondition4[2][1]+m];
                    result +=gameboard[winCondition4[3][0]+n][winCondition4[3][1]+m];
                    if(result==gameWinner)
                        markAllWinKeys(winCondition4,n,m);
                }
            }
        }
    }

    private boolean anyOneWin(int result){
        boolean win =false;
        switch(result){
            case 4:
                winner=player1;
                win=true;
                break;
            case -4:
                winner=player2;
                win=true;
                break;
        }
        if(win)
            seekAllWinKeys(winner*4);
        return win;
    }

    private void markAllWinKeys(int[][] winCondition, int n,int m){
        int cal_temp;
        for(int i=0;i<4;i++){
            winKeys[i][0]=winCondition[i][0]+n;
            winKeys[i][1]=winCondition[i][1]+m;
            cal_temp=winKeys[i][0]*10+winKeys[i][1];
            if(!winKeysList.contains(cal_temp)){
                winKeysList.add(cal_temp);
            }
        }
    }

    private gameLogic(){
        initialGameboard();
        steps=new Stack<>();
        winKeysList=new ArrayList<>();
        turn=player1;
    }

    static gameLogic getInstance(){
        if(null == instance){
            instance = new gameLogic();
        }
        return instance;
    }

    protected boolean clearGameboard(){
        initialGameboard();
        steps.clear();
        turn=player1;
        winKeysList.clear();
        winner=0;
        return true;
    }

    private void initialGameboard(){
        for(int m=0;m<7;m++) {
            gameStackHeight[m]=0;
            for (int n = 0; n < 6; n++) {
                gameboard[m][n] = 0;
            }
        }
        for(int m=0;m<4;m++)
            for(int n=0;n<2;n++)
                winKeys[m][n]=0;
    }

}
