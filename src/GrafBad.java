import java.util.*;

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
public class GrafBad {
    Kattio io;
    HashMap<Integer, LinkedList<Integer>> test = new HashMap<Integer, LinkedList<Integer>>();
    Boolean[] testV;
    void createReduction() {
        Random r = new Random();

        int nodes = io.getInt(); //nr hörn --> antal roller
        int edges = io.getInt(); //nr kanter --> roller i samma scen
        int colors = io.getInt(); //färger att färglägga --> antal personer
        testV = new Boolean[nodes+1];

        int roles, scenes, actors;

        //cleanup; remove dubbel länkar och isolerade noder
        for(int i = 0; i < testV.length; i++){
            testV[i] = false;
        }
        for(int i = 0; i < edges; i++){
            //check if there is an reverse edge
            int first = io.getInt();
            int second = io.getInt();
            testV[first] = true;
            testV[second] = true;

            //make direction irrelevant
            int tf;
            int ts;
            if (first > second){
                tf=second;
                ts=first;
            }else{
                tf=first;
                ts=second;
            }

            //DEBUG: System.err.println("ts" + ts + " tf " + tf + " get " + test.containsKey(tf));

            if(test.containsKey(tf )){
                if(test.get(tf).contains(ts)){

                    continue;
                }else{
                    //System.out.println("a");
                    test.get(tf).push(ts);
                }
            }else {

                //System.out.println("b");
                LinkedList<Integer> addNew = new LinkedList<Integer>();
                addNew.push(ts);
                test.put(tf, addNew);
            }
        }
        int actualV = 0;
        for(int i = 1; i < testV.length; i++){
            if(testV[i]){
                actualV++;
            }
        }

        //update values; every node has now at leas an edge
        //no same edges is counted twice
        //same number of colors
        //add necessary roles, scenes and people to fulfil the diva criteria.
        roles = actualV + 3; //min 1
        scenes = test.size() + 2; //scenes is at most nr edges OBS: min 0
        actors = colors + 3; //min 1

        //print roles, scenes, actors.
        io.println(roles);
        io.println(scenes);
        io.println(actors);

        //assign each diva plus a random person for both divas to play vs.
        //print divas roles assignment
        io.println(2 + " " + 1); //role 1 can be played by person 1
        io.println(2 + " " + 2); //role 2 can be played by person 2
        io.println(2 + " " + 3); //role 3 can be played by person 3

        //print all other possible combinations divas do not play.
        //todo: check the iteration bounds
        for (int i = 3; i < roles; i++) {
            io.print(actors - 3 + " ");
            //remaining actors start from person nr 3
            for (int j = 4; j <= actors; j++) {
                io.print(j + " ");
            }
            io.println();
        }


        //print diva scenes
        io.println(2 + " " + 1 + " " + 3);
        io.println(2 + " " + 2 + " " + 3);

        //print the remaining scenes
        while(test.size() < (scenes-1)){

            //System.out.println("I am here " + nodes);
            int i = 0;
            for(i = 0; i < testV.length; i++){
                if(!testV[i]){
                    break;
                }
            }
            LinkedList<Integer> addNew = new LinkedList<>();
            if(nodes == 1) {
                addNew.push(0);
            }else {
                addNew.push(r.nextInt(nodes));
            }
            test.put(i, addNew);
        }
        Set<Map.Entry<Integer, LinkedList<Integer>>> set = test.entrySet();
        for (Map.Entry<Integer, LinkedList<Integer>> edge : set){
            for(Integer value : edge.getValue()) {
                int inc = -1;
                while(value == edge.getKey()) {
                    value += inc;
                    if(value == 0){
                        inc = 1;
                    }
                }
                io.println(2 + " " + (edge.getKey() + 3) + " " + (value + 3));
            }
        }
    }

    GrafBad(){
        io = new Kattio(System.in, System.out);
        createReduction();
        io.close();
    }

    public static void main(String[] args){
        new GrafBad();
    }

}
