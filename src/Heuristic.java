import java.util.*;

public class Heuristic {

    Random r = new Random();

    int roller, scener, actors;

    /*store input in memory*/
    ArrayList<int[]> rollset = new ArrayList<>();
    ArrayList<int[]> scenset = new ArrayList<>();

    /*keep track of assigned roles*/
    boolean[] assignmentCheck;
    boolean allAssigned = false;

    /*result*/
    HashMap<Integer, Integer> assignments;
    HashMap<Integer, LinkedList<Integer>> scenes;

    /*stack variables*/
    int conflicts = 0;

    public Heuristic(){


    }

    public void init(int roller, int actors, int scener){
        this.roller = roller;
        this.actors = scener;
        this.scener = actors;

        System.err.println("ROLLER: " + this.roller);
        System.err.println("ACTORS: " + this.actors);
        System.err.println("SCENER: " + this.scener);

        assignmentCheck = new boolean[roller];

        assignments = new HashMap<>();
        scenes = new HashMap<>();
    }

    public void init2(ArrayList<int[]> rollset, ArrayList<int[]> scenset){

        this.scenset = scenset;
        this.rollset = rollset;

    }

    public void verify (){
        for(int i = 0; i < scener; i++){
            HashMap<Integer, Integer> checker = new HashMap<>();
            //iterate roles in the scen
            for(int j = 0; j < scenset.get(i).length; j++){
                if(checker.containsKey(assignments.get(scenset.get(i)[j])) && assignments.get(scenset.get(i)[j]) != null){
                    //System.err.println("VERIFY: CONFLICT FOR ROLE " + assignments.get(scenset.get(i)[j]));
                    conflicts++;
                }else {
                    checker.put(assignments.get(scenset.get(i)[j]), 123456789);
                }
            }
        }
    }

    public String solve(){

        /* Assign a actor to a role and count conflicts */
        /* If there is less conflicts than maximum conflicts it is valid assignment otherwise assign another */
        LinkedList<Integer> insert;

        //todo: make sure actor 1 and 2 does not play in the same role
        if(actors >= 2) {
             //scen 0;
             //assignments.put(0, 0);

             //scen 1
             //assignments.put(1, 1);
         }else{
            System.err.println("unsolvable");
            System.exit(0);
        }

        long time = System.currentTimeMillis();
        while (((System.currentTimeMillis() - time)/1000) < 4) {

            naive();

            if(assignments.size() == roller){
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < roller; i++){
                    if(assignments.containsKey(i)) {
                        sb.append("ROLE " + i + " played by: " + assignments.get(i) + "\n");
                    }else {
                        sb.append("ERROR: NO ACTOR FOUND\n");
                    }
                }
                return sb.toString();
            }else{
                optimize();

            }

        }
        return "no solution found in " + ((System.currentTimeMillis() - time)/1000) + " secs";
    }

    public void naive (){

        for (int i = 0; i < roller; i++) {
            //includes actor 0 and 1 todo: make sure they dont play in the same scene
            for (int k = 0; k < rollset.get(i).length; k++) {
                Object val = assignments.putIfAbsent(i, rollset.get(i)[k]);
                if (val == null) {
                    verify();
                    if (conflicts > 0) {
                        //System.err.println("CONFLICT " + conflicts);
                        assignments.remove(i);
                        conflicts = 0;
                    }
                }
            }
        }
    }

    public void optimize(){

        for(int i = 0; i < roller; i++){
            if(assignments.containsKey(i)){
                //fine
            }else{
                //search for someone that can play the role
                int[] candidate = rollset.get(i);
                //if candidate j plays in a role that is in a scen that has role i, remove candidate from that role;
                //System.err.println("CANDIDATE LENGTH: " + candidate.length);
                int rand;
                //bound cannot be 0
                if(candidate.length <= 1){
                    rand = 0;
                }else {
                    rand = r.nextInt(candidate.length - 1);
                }
                //System.err.println("RAND: " + rand);
                int cand = candidate[rand];
                for(int k = 0; k < scener; k++){
                    //roles is the roles in scen k
                    int [] roles = scenset.get(k);
                    for(int l = 0; l < roles.length; l++){
                        if(assignments.containsKey(l) && assignments.get(l) == l){
                            assignments.remove(l);
                        }
                    }
                }
            }
        }
    }

    private static ArrayList<int[]> read (Scanner in, int amount){
        ArrayList<int[]> ret = new ArrayList<>();
        while(amount>0) {
            int nrLine = in.nextInt();
            int[] line = new int[nrLine];
            for (int i = 0; i < nrLine; i++) {
                line[i] = in.nextInt()-1;
            }
            ret.add(line);
            amount--;
        }
        for(int[] pr : ret){
            for(int i : pr){
                System.err.print(i);
            }
        }
        System.err.println();
        return ret;
    }

    public static void main(String [] args){
        /* Read stdin */
        Scanner in = new Scanner(System.in);

        /* Choose heuristic */
        Heuristic heuristic = new Heuristic();

        /* Let heuristic find a solution */
        heuristic.init(in.nextInt(), in.nextInt(), in.nextInt());
        heuristic.init2(read(in, heuristic.roller), read(in, heuristic.scener));

        /* Hejhej */
        System.out.println(heuristic.solve());
    }
}
