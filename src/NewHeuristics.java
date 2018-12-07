import java.util.*;

/**
 * Created by Daniel and Patrik on 12/3/2018.
 */
public class NewHeuristics {

    Random r = new Random();

    int roller, scener, actors;

    /*store input in memory*/
    ArrayList<int[]> rollset = new ArrayList<>();
    ArrayList<int[]> scenset = new ArrayList<>();
    skådespelare[] skådespelarna;

    /*keep track of assigned roles*/
    boolean[] assignmentCheck;

    /*result*/
    HashMap<Integer, Integer> assignments;
    HashMap<Integer, LinkedList<Integer>> scenes;


    ArrayList<int[]> roleAppearsIn = new ArrayList<>();


    /*stack variables*/
    int conflicts = 0;

    public void init(Kattio io){
        this.roller = io.getInt();
        this.scener = io.getInt();
        this.actors = io.getInt();

        assignmentCheck = new boolean[roller];

        assignments = new HashMap<>();
        scenes = new HashMap<>();
        skådespelarna = new skådespelare[actors];
        readRoles(io, roller);
        readScenes(io, scener);
    }


    public void verify (){
        for(int i = 0; i < scener; i++){
            HashMap<Integer, Integer> checker = new HashMap<>();
            //iterate roles in the scene
            for(int j = 0; j < scenset.get(i).length; j++) {
                if (assignments.get(scenset.get(i)[j]) != null) {
                    int actor = assignments.get(scenset.get(i)[j]);
                    if (checker.containsKey(actor)) {
                        conflicts++;
                    } else if (((checker.containsKey(0) && actor == 1) || (checker.containsKey(1) && actor == 0))) {
                        conflicts++;
                    } else {
                        checker.put(assignments.get(scenset.get(i)[j]), 123456789);
                    }
                }
            }
        }
    }

    public String solve(){

        /* Assign a actor to a role and count conflicts */
        /* If there is less conflicts than maximum conflicts it is valid assignment otherwise assign another */

        //todo: make sure actor 1 and 2 does not play in the same role
        //todo: make sure everyone plays a role in a scen
        if(actors >= 2) {
        }else{
            System.err.println("unsolvable");
            System.exit(0);
        }


        /*
        long time = System.currentTimeMillis();

        while (((System.currentTimeMillis() - time)/1000) < 5) {
            naive();
            break;
        }
        */
        naive();

        return printActors();
    }

    public boolean doLocalModification(){
        boolean didAswapWork = false;
        for(int i = 0; i < skådespelarna.length; i++){
            if(skådespelarna[i].rolesPlayed.size() > 0) {
                if (!skådespelarna[i].haveMany) {
                    for (int j = 0; j < skådespelarna[i].rolesPlayed.size(); j++) {
                        if(!(i == 0 && skådespelarna[0].rolesPlayed.size() < 2) && !(i == 1 && skådespelarna[1].rolesPlayed.size() < 2)) {
                            int roleToSwap = skådespelarna[i].rolesPlayed.get(j);
                            boolean swapped = false;
                            for (int k = 0; k < rollset.get(roleToSwap).length; k++) {
                                if(!swapped) {
                                    int actorToTake = rollset.get(roleToSwap)[k];
                                    if (skådespelarna[actorToTake].rolesPlayed.size() > 0 && actorToTake != i) {
                                        assignments.remove(roleToSwap);
                                        Object val = assignments.putIfAbsent(roleToSwap, actorToTake);
                                        if (val == null) {
                                            verify();
                                            if (conflicts > 0) {
                                                //System.err.println("CONFLICT " + conflicts);
                                                assignments.remove(roleToSwap);
                                                assignments.putIfAbsent(roleToSwap, i);
                                                conflicts = 0;
                                            } else if (skådespelarna[i].rolesPlayed.size() != 0) {
                                                skådespelarna[i].rolesPlayed.remove(j);
                                                skådespelarna[actorToTake].addRolePlayed(roleToSwap);
                                                System.err.println("i removed " + roleToSwap + " and gave to " + actorToTake);
                                                System.err.println("skådis = " + i + " rollplats " + j);
                                                swapped = true;
                                                didAswapWork = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        checkIfSuperCanbeRemoved();
                    }
                }
            }
        }
        return didAswapWork;
    }

    public void checkIfSuperCanbeRemoved(){
        for (int i = 0; i < roller; i++) {
            if(!assignments.containsKey(i)) {
                //includes actor 0 and 1 todo: make sure they dont play in the same scene WHERE?
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
    }


    public String printActors(){
        StringBuilder sb = new StringBuilder();
        LinkedList<Integer> superskådisar = new LinkedList<>();

        int totalaSuperskådisar = 0;
        int totalaOrdinarie = 0;

        for(int i = 0; i < roller; i++){
            if(assignments.containsKey(i)){
                skådespelarna[assignments.get(i)].addRolePlayed(i);
                if(skådespelarna[assignments.get(i)].rolesPlayed.size() > 3){
                    skådespelarna[assignments.get(i)].setLotsOfRoles();
                }
            }
        }
        /*
        int iterations = 0;
        while (doLocalModification() || iterations < 10){
            iterations++;
            System.err.println("Iteration: " + iterations);
        }
        if(!verification()){
            System.err.println("we have conflicts");
        }
        */
        for(int i = 0; i < roller; i++){
            if(!assignments.containsKey(i)){
                totalaSuperskådisar++;
                superskådisar.add(i);
            }
        }
        for(int i = 0; i < skådespelarna.length; i++){
            if(skådespelarna[i] != null && skådespelarna[i].rolesPlayed.size() != 0){
                totalaOrdinarie++;
                sb.append("\n");
                sb.append((i+1) + " " + skådespelarna[i].rolesPlayed.size());
                for(int role : skådespelarna[i].rolesPlayed) {
                    sb.append(" " + (role+1));
                }

            }
        }
        for(int i = 0; i < superskådisar.size(); i++){
            sb.append("\n");
            sb.append((superskådisar.get(i)+actors+1) + " " + 1 + " " + (superskådisar.get(i)+1));
        }
        sb.insert(0, (totalaOrdinarie+totalaSuperskådisar));

        return sb.toString();
    }

    public void naive (){

        boolean assignedDiva2 = false;
        int j = 0;
        while(true) {
            int roleDiva1 = skådespelarna[0].rolesAvailable.get(j);
            assignments.putIfAbsent(roleDiva1, 0);
            for (int i = 0; i < skådespelarna[1].rolesAvailable.size(); i++) {
                int roleDiva2 = skådespelarna[1].rolesAvailable.get(i);
                Object val = assignments.putIfAbsent(roleDiva2, 1);
                if (val == null) {
                    verify();
                    if (conflicts > 0) {
                        //System.err.println("CONFLICT " + conflicts);
                        assignments.remove(roleDiva2);
                        conflicts = 0;
                    }else{
                        assignedDiva2 = true;
                    }
                }
            }
            if(!assignedDiva2){
                assignments.remove(roleDiva1);
                j++;
            }else{
                break;
            }
        }
        for (int i = 0; i < roller; i++) {
            //includes actor 0 and 1 todo: make sure they dont play in the same scene WHERE?
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

    NewHeuristics(){
        Kattio io = new Kattio(System.in);
        init(io);
        io.println(solve());
        io.close();
    }
    public static void main(String[] args){
        new NewHeuristics();
    }

    /*------------------------Just some struct of each skådespelare--------------------- */
    public class skådespelare{
        LinkedList<Integer> rolesAvailable = new LinkedList<>();
        LinkedList<Integer> rolesPlayed = new LinkedList<>();
        boolean haveMany = false;
        public void skådespelare(){

        }
        public void addAvailableRole(int roleNumber){
            this.rolesAvailable.add(roleNumber);
        }
        public void addRolePlayed(int roleNumber){
            this.rolesPlayed.add(roleNumber);
        }
        public void setLotsOfRoles(){
            this.haveMany = true;
        }
    }

    /*------------------------Read inputs--------------------- */
    private void readRoles (Kattio in, int amount){
        ArrayList<int[]> ret = new ArrayList<>();
        skådespelare[] skådisar = new skådespelare[actors];
        int roleNumber = 0;
        while(amount>0) {
            int nrLine = in.getInt();
            int[] line = new int[nrLine];
            for (int i = 0; i < nrLine; i++) {
                line[i] = in.getInt() - 1;
                if (skådisar[line[i]] == null) {
                    skådisar[line[i]] = new skådespelare();
                }
                skådisar[line[i]].addAvailableRole(roleNumber);
            }
            ret.add(line);
            amount--;
            roleNumber++;
        }
        this.rollset = ret;
        this.skådespelarna = skådisar;
    }

    private void readScenes (Kattio in, int amount) {
        ArrayList<int[]> ret = new ArrayList<>();
        while (amount > 0) {
            int nrLine = in.getInt();
            int[] line = new int[nrLine];
            for (int i = 0; i < nrLine; i++) {
                line[i] = in.getInt() - 1;
            }
            ret.add(line);
            amount--;
        }
        this.scenset = ret;
    }
}
