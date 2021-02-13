package hw6;


import java.util.Arrays;


public class MainMt {
    public static void main(String[] args) {
        try {
            System.out.println(Arrays.toString(lastAfterFour(new int[]{4,1,4,5,6,7,4})));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        System.out.println(isOneAndFour(new int[]{1, 4, 1}));
    }

    public static int[] lastAfterFour (int[] mass)  {
        int liof = -1;
        for (int i = 0; i < mass.length; i++) {
            if (mass[i] == 4) {
                liof = i;
            }
        }
        int[] result = new int[] {};
        if (liof < 0) {
            throw new RuntimeException ("Нету 4");
        } else if (liof == mass.length) {
            return  result;
        } else {
            result = Arrays.copyOfRange(mass, liof+1, mass.length);
        }
        return result;
    }

    public static boolean isOneAndFour (int[] mass) {
        int one = 0;
        int four = 0;
        for (int i = 0; i < mass.length; i++) {
            if (mass[i] != 4 && mass[i] !=1 ) {
                return false;
            } else if (mass[i] == 1) {
                one++;
            } else if (mass[i] == 4) {
                four++;
            }
        }
        if (one == 0 || four == 0) {
            return false;
        }
        return true;
    }
}
