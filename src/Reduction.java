import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

class Reduction{


    public static void main(String[] args){


        //storage
        LinkedList<int[]> kanter = new LinkedList<>(); //kanter;
        LinkedList<int[]> person = new LinkedList<>(); //roller;
        LinkedList<int[]> scen = new LinkedList<>(); //roller;

        Scanner in = new Scanner(System.in);
        //read three first arguments
        int a = in.nextInt(); //antal hörn
        int b = in.nextInt(); //antal kanter
        int c = in.nextInt(); //antal färger

        //läs kanter
        for (int i = 0; i < b; i++){
            int r = in.nextInt();
            int [] temp = new int[r];
            for (int j = 0; j < r; j++){
                temp[j] = in.nextInt();
            }
            kanter.push(temp);
        }



        //debug input
        System.err.println("Kanter");
        for(int[] out : kanter){
            System.err.println(Arrays.toString(out));
        }

    }
}