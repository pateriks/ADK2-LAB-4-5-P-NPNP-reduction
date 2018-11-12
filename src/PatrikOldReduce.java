import java.util.*;

public class PatrikOldReduce {

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
        Kattio io;
        HashMap<Integer, LinkedList<Integer>> test = new HashMap<Integer, LinkedList<Integer>>();
        Boolean[] testV;
        void createReduction() {
            Random r = new Random();

            int nodes = io.getInt(); //nr hörn --> antal roller
            int edges = io.getInt(); //nr kanter --> roller i samma scen
            int colors = io.getInt(); //färger att färglägga --> antal personer
            testV = new Boolean[nodes+1];

            for(int i = 0; i < testV.length; i++){

                testV[i] = false;
            }

            int roles, scenes, actors;

            //if graph is planer following applies:
            //v
            //-
            //e
            //+
            //f
            //=
            //2
            //cleanup; remove dubbel länkar och isolerade noder
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
            nodes = actualV;
            edges = test.size();
            colors = colors;

            //add necessary roles, scenes and people to fulfil the diva criteria.
            if(nodes <= 1){
                roles = 3;
            }else {
                roles = nodes + 2; //min 1
            }
            scenes = edges+2; //scenes is at most nr edges OBS: min 0
            actors = colors+2; //min 1

            //print roles, scenes, actors.
            io.println(roles);
            io.println(scenes);
            io.println(actors);

            //make scen 1 and 2 contain a random role except roles 1 and 2
            int randomV;
            randomV = r.nextInt(roles - 2) + 3; //avoiding nextInt = 0 by adding 1 to 2

            //assign each diva plus a random person for both divas to play vs.
            //print divas roles assignment
            io.println(2 + " " + 1 + " " + randomV); //role 1 can be played by person 1
            io.println(2 + " " + 2 + " " + randomV); //role 2 can be played by person 2

            //print all other possible combinations divas do not play.
            //todo: check the iteration bounds
            for (int i = 2; i < roles; i++) {
                io.print(actors - 2 + " ");
                //remaining actors start from person nr 3
                for (int j = 3; j <= actors; j++) {
                    io.print(j + " ");
                }
                io.println();
            }


            //print diva scenes
            io.println(2 + " " + 1 + " " + randomV);
            io.println(2 + " " + 2 + " " + randomV);

            //print the remaining scenes
            Set<Map.Entry<Integer, LinkedList<Integer>>> set = test.entrySet();
            for (Map.Entry<Integer, LinkedList<Integer>> edge : set){
                for(Integer value : edge.getValue())
                    io.println(2 + " " + (edge.getKey()+2) + " " + (value+2));
            }
        }



        PatrikOldReduce(){
            io = new Kattio(System.in, System.out);
            createReduction();
            io.close();
        }

        public static void main(String[] args){
            new PatrikOldReduce();
        }

    }
