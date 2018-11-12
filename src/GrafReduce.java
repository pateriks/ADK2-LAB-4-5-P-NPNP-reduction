import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Daniel and Patrik on 11/11/2018.
 */
/*
6
5
4
3 1 3 4
2 2 3
2 1 3
1 2
4 1 2 3 4
2 1 4
3 1 2 6
3 2 3 5
3 2 4 6
3 2 3 6
2 1 6
 */
public class GrafReduce {
    Kattio io;
    HashMap<Integer, Integer> test = new HashMap<Integer, Integer>();
    void createReduction() {
        int nodes = io.getInt();
        int edges = io.getInt();
        int colors = io.getInt();

        int roles, scenes, actors;

        //add neccessary roles, scenes and people to fulfil the diva criteria.
        roles = nodes + 2;
        scenes = edges + 2;
        actors = colors + 2;

        //print roles, scenes, actors.
        io.println(roles);
        io.println(scenes);
        io.println(actors);

        //assign each diva plus a random person for both divas to play vs.
        io.println(1 + " " + 1);
        io.println(1 + " " + 2);
        //io.println(1 + " " + 3);

        //print all other possible combinations divas do not play.
        for (int i = 2; i < roles; i++) {
            io.print(actors - 2 + " ");
            for (int j = 3; j <= actors; j++) {
                io.print(j + " ");
            }
            io.println();
        }

        //print diva scenes
        io.println(2 + " " + 1 + " " + 3);
        io.println(2 + " " + 2 + " " + 3);

        //print scenes depending on edges, divas do not play
        for (int i = 0; i < scenes - 2; i++) {
            int first = io.getInt() + 2;
            int second = io.getInt() + 2;
            int tf;
            int ts;
            if (first > second){
                tf=second;
                ts=first;
            }else{
                tf=first;
                ts=second;
            }
            //System.err.println("ts" + ts + " tf " + tf + " get " + test.containsKey(tf));
            if(test.containsKey(tf )){
                if(ts == test.get(tf)){

                    continue;
                }
            }else{
                test.put(tf, ts);
            }
            io.println(2 + " " + first + " " + second);
        }
    }

    GrafReduce(){
        io = new Kattio(System.in, System.out);
        createReduction();
        //io.println();
        io.close();
    }

    public static void main(String[] args){
        new GrafReduce();
    }

}
