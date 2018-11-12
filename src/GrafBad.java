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
        testV = new Boolean[nodes];

        //System.err.println(testV.length);
        int roles, scenes, actors;

        //cleanup; remove dubbel länkar och isolerade noder
        for(int i = 0; i < testV.length; i++){
            testV[i] = false;
        }
        for(int i = 0; i < edges; i++){
            //check if there is an reverse edge
            int first = io.getInt();
            int second = io.getInt();
            testV[first-1] = true;
            testV[second-1] = true;

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

            if(test.containsKey(tf)){
                if(test.get(tf).contains(ts)){
                    //Same edge as before
                    continue;
                }else{
                    //System.out.println("a");
                    //vertex containing several edged
                    //push edge
                    //System.err.println("pushing : " + ts);
                    test.get(tf).push(ts);
                }
            }else {

                //System.err.println("creating : " + ts);
                //System.out.println("b");
                LinkedList<Integer> addNew = new LinkedList<Integer>();
                addNew.push(ts);
                test.put(tf, addNew);
            }
        }
        int actualV = 0;
        for(int i = 0; i < testV.length; i++){
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
        int connected = actualV;
        while(connected < nodes){
            //System.out.println("I am here " + nodes);
            //look for empty nodes
            int i;
            for(i = 0; i < testV.length; i++){
                if(!testV[i]){
                    //System.err.println("break");
                    testV[i] = true;
                    break;
                }
            }
            connected++;
            //make identical for now, check that they are identical later and fix
            LinkedList<Integer> addNew = new LinkedList<>();
            addNew.push(i+1);
            //System.err.println("i " + i);
            test.put(i+1, addNew); //+3 later
        }

        Set<Map.Entry<Integer, LinkedList<Integer>>> set = test.entrySet();
        for (Map.Entry<Integer, LinkedList<Integer>> edge : set){
            for(Integer value : edge.getValue()) {
                int inc = -1;
                //System.err.println("prevalue: " + value);
                while(value == edge.getKey() && testV[value-1]) {
                    //System.err.println("value: " + value);
                    value += inc;
                    if(value < 3){
                        inc = 1;
                    }
                }
                //System.err.println("post value: " + value);
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
