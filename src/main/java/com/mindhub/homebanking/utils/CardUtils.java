package com.mindhub.homebanking.utils;

import java.util.Random;

public class CardUtils {
    private CardUtils(){
    }

    public static int getNumberRandom(int min, int max){
        Random random = new Random();
        int number = random.nextInt(max) + min;
        return number;
    }
    public static String getStringNumber(){
        String numberRandom = "";
        for(int i = 0; i < 4; i++){
            numberRandom += String.valueOf(getNumberRandom(1000, 8999)) + " ";
        }
        return numberRandom;
    }
    public static int getRandomCVV(){
        return getNumberRandom(100, 899);
    }
}
