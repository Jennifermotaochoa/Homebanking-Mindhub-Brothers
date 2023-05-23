package com.mindhub.homebanking.utils;

import java.util.Random;

public class AccountUtils {
    public AccountUtils() {
    }

    public static int getNumberRandom(int min, int max){
        Random random = new Random();
        int number = random.nextInt(max) + min;
        return number;
    }
    public static String getStringNumber(){
        int numberRandom = getNumberRandom(000000, 899999);
        return String.valueOf(numberRandom);
    }
}
