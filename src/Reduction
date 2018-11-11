import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

class Reduction{


    public static void main(String[] args){


        //Read input;
        LinkedList<int[]> person = new LinkedList<>(); //roller;
        LinkedList<int[]> scen = new LinkedList<>(); //roller;

        Scanner in = new Scanner(System.in);
        //read three first arguments
        int a = in.nextInt(); //antal roller
        int b = in.nextInt(); //antal scener
        int c = in.nextInt(); //antal skådespelare

        //read skådespelares roller
        for (int i = 0; i < c; i++){
            int r = in.nextInt();
            int [] temp = new int[r];
            for (int j = 0; j < r; j++){
                temp[j] = in.nextInt();
            }
            person.push(temp);
        }

        //read roller in scen
        for (int i = 0; i < b; i++){
            int r = in.nextInt();
            int [] temp = new int[r];
            for (int j = 0; j < r; j++){
                temp[j] = in.nextInt();
            }
            scen.push(temp);
        }

        //debug input
        System.err.println("Rollbesättning");
        for(int[] out : person){
            System.err.println(Arrays.toString(out));
        }
        System.err.println("Scenbesättning");

        for(int[] out : scen){
            System.err.println(Arrays.toString(out));
        }


    }
}