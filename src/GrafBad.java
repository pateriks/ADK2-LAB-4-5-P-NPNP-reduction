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
    HashMap<Integer, LinkedList<Integer>> testE = new HashMap<Integer, LinkedList<Integer>>();
    Boolean[] testV;

    void createReduction() {

        //param input for graph coloring
        int nodes = io.getInt(); //nr hörn --> antal roller
        int edges = io.getInt(); //nr kanter --> roller i samma scen
        int colors = io.getInt(); //färger att färglägga --> antal personer

        //2check active nodes
        testV = new Boolean[nodes+3];

        //todo: necessary initialization?
        for(int i = 0; i < testV.length; i++){
            testV[i] = false;
        }
        testV[0] = true;
        testV[1] = true;
        testV[2] = true;

        //debug
        System.err.println("testV length: " + testV.length);

        //input data pattern
        int realE = 0;
        int activeV = 0;
        int nactiveV = 0;

        int roles, scenes, actors;

        //cleanup; remove dubbel länkar och check aktiva noder
        for(int i = 0; i < edges; i++){
            //check if there is an reverse edge
            int first = io.getInt();
            int second = io.getInt();
            if(first == second)
                continue;
            testV[first+2] = true;
            testV[second+2] = true;
            //make direction irrelevant, make the node with lowest index key to edges
            int tf;
            int ts;
            if (first > second){
                tf=second;
                ts=first;
            }else{
                tf=first;
                ts=second;
            }
            System.err.println("ts" + ts + " tf " + tf + " get " + testE.containsKey(tf));
            if(testE.containsKey(tf)){
                if(testE.get(tf).contains(ts)){
                    //Same edge as before
                    continue;
                }else{
                    //debug
                    System.err.println("pushing : " + ts);

                    //new edge to add to node tf
                    realE++;
                    testE.get(tf).push(ts);
                }
            }else {
                //debug
                System.err.println("creating : " + ts);

                LinkedList<Integer> addNew = new LinkedList<Integer>();
                addNew.push(ts);
                realE++;
                testE.put(tf, addNew);
            }
        }

        //count active and inactive nodes
        for(int i = 0; i < testV.length; i++){
            if(testV[i]){
                activeV++;
            }else{
                nactiveV++;
            }
        }

        //debug
        System.err.println("S:testE: " + testE.size());
        System.err.println("S:testV: " + testV.length);
        System.err.println("activeV: " + activeV);
        System.err.println("realE: " + realE);
        System.err.println("nactive: " + nactiveV);

        //add necessary roles, scenes and people to fulfil the diva criteria.
        roles = nodes + 3; //min 1
        scenes = realE + nactiveV + 2; //scenes is at most nr edges OBS: min 0
        //todo: this is a very large number
        actors = colors + 3; //min 1
        //todo: verify this, passes kattis though
        if(colors > roles){
            actors = roles;
        }

        //print roles, scenes, actors.
        io.println(roles);
        io.println(scenes);
        io.println(actors);

        //assign each diva plus a random person for both divas to play vs.
        //print divas roles assignment and helper
        io.println(1 + " " + 1); //role 1 can be played by person 1
        io.println(1 + " " + 2); //role 2 can be played by person 2
        io.println(1 + " " + 3); //role 3 can be played by person 3

        //print all other possible combinations divas do not play.
        //todo: check the iteration bounds
        System.err.println("roles: " + roles);
        System.err.println("actors: " + actors);
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
        int connected = activeV;

        //debug
        System.err.println("pre connected: " + connected);

        //do this once, iterate roles-connected times
        //makes isolated nodes active
        int i = 0;
        while(connected < roles){
            System.err.println("roles: " + roles);
            //look for empty nodes
            while (i < testV.length){
                i++;
                if(!testV[i]){
                    System.err.println("break");
                    break;
                }
            }
            LinkedList<Integer> addNew = new LinkedList<>();
            int value = i - 1;
            System.err.println("prevalue: " + value);
            int inc = - 1;
            while(!testV[value] && (value < testV.length-1)) {
                value+= inc;
                if(value < 3){
                    inc = + 1;
                }
            }
            connected++;
            addNew.push(value-2);
            System.err.println("i: " + i + " post value: " + value);
            testE.put(i-2, addNew); //+3 later
        }

        //debug
        System.err.println("post connected " + connected);

        //print scenes
        //todo: make faster
        Set<Map.Entry<Integer, LinkedList<Integer>>> set = testE.entrySet();
        for (Map.Entry<Integer, LinkedList<Integer>> edge : set){
            //io.print((edge.getValue().size()+1) + " " + (edge.getKey() + 3));
            for(Integer value : edge.getValue()) {
                System.err.println("post value: " + value);
                //io.print(" " + (value + 3));
                io.println(2 + " " + (edge.getKey() + 3) + " " + (value + 3));
            }
            //io.println();
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
