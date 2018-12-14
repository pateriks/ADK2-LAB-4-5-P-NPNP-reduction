import java.util.*;

/**
 * Created by Daniel and Patrik on 12/3/2018.
 */
public class Labb5 {

    Random r = new Random();
    long millis;
    int roller, scener, actors;

    /*rollset is a map of [x, y] where x point out a role and y is an actor. It means that role x can be played by actor x */
    ArrayList<int[]> rollset;
    /*scenset is a map of [x, y] where x scene and y role in scene*/
    ArrayList<int[]> scenset;
    /*rolesScenes is a map from x to a list of scenes where that role is present*/
    HashMap<Integer, LinkedList> rolesScenes = new HashMap<>();
    /*every actor has a list of roles played and what roles that the actor can play */
    skådespelare[] skådespelarna;

    /* points out a actor (value) for a (key) given role */
    HashMap<Integer, Integer> assignments;

    /* check whether all roles has a assignment (cannot be done with hashmap)*/
    LinkedList<Integer> rolesCheck = new LinkedList<>();

    /* stores roles given to super actors */
    LinkedList<Integer> superskådisar = new LinkedList<>();

    /* count number of actors under assignment */
    int totalaSuperskådisar = 0;
    int totalaOrdinarie = 0;

    int conflicts = 0;

    Labb5(){
        Kattio io = new Kattio(System.in);
        init(io);
        io.println(solve());
        io.close();
    }


    public void init(Kattio io){
        this.roller = io.getInt();
        this.scener = io.getInt();
        this.actors = io.getInt();

        assignments = new HashMap<>();
        skådespelarna = new skådespelare[actors];

        readRoles(io, roller);
        readScenes(io, scener);

        millis = System.currentTimeMillis();
        r.setSeed(System.currentTimeMillis());
    }

    public void verify (int role){
        //get interesting scenes (where role is present)
        if(rolesScenes.get(role) == null){
            //hey what are you doing here, A role without any scene, why??
            //System.exit(1);
            return;
        }
        for(Integer i : (LinkedList<Integer>) rolesScenes.get(role)){
            //checker is used to dynamically see what keys are used (value is ignored)
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
        //System.err.println(actors);
        if(actors < 2){
            System.exit(1);
        }
/*
        for(int i = 0; i < roller; i++){
            if(rolesScenes.get(i) == null){
                if(rollset.get(i) == null){
                    System.exit(1);
                }else if(rollset.get(i).length == 0){
                    System.exit(1);
                }
                assignments.put(i, rollset.get(i)[0]);
                skådespelarna[rollset.get(i)[0]].rolesPlayed.add(i);
            }
        }
*/
        naive();
        return printActors();
    }


    public String printActors(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < roller; i++){
            if(assignments.get(i) == null){
                superskådisar.add(i);
            }
        }

        for(int i = 0; i < actors; i++){
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
            totalaSuperskådisar++;
        }

        sb.insert(0, (totalaOrdinarie+totalaSuperskådisar));

        return sb.toString();
    }

    public void naive (){
        /* Iterate through every role that actor 1 can take and try to find some role for actor 2
        If roles can be find that makes no conflict break, otherwise increment j and try again
         */
        boolean assignedDiva2 = false;
        int j = 0;

        while(j < skådespelarna[0].rolesAvailable.size()) {
            int roleDiva1 = skådespelarna[0].rolesAvailable.get(j);
            Object check = assignments.putIfAbsent(roleDiva1, 0);
            if(check == null) {
                for (int i = 0; i < skådespelarna[1].rolesAvailable.size(); i++) {
                    int roleDiva2 = skådespelarna[1].rolesAvailable.get(i);
                    Object val = assignments.putIfAbsent(roleDiva2, 1);
                    if (val == null) {
                        verify(roleDiva2);
                        if (conflicts > 0) {
                            assignments.remove(roleDiva2);
                            conflicts = 0;
                        } else {
                            assignedDiva2 = true;
                            skådespelarna[1].rolesPlayed.add(roleDiva2);
                            skådespelarna[0].rolesPlayed.add(roleDiva1);
                            break;
                        }
                    }
                }
                if(assignedDiva2){
                    break;
                }else{
                    assignments.remove(roleDiva1);
                }
            }
            j++;
        }


        int iterations = 0;
        //(iterations < roller && (System.currentTimeMillis()-millis)<1250){//
        while (!rolesCheck.isEmpty() && (System.currentTimeMillis()-millis)<1300){
            //poll: retrive and delete

            int i = rolesCheck.get(r.nextInt(rolesCheck.size()));
            rolesCheck.remove((Integer) i);
            //int i = iterations;

            //iterations++;
            for (int k = rollset.get(i).length-1; k >= 0; k--) {
                Object val = assignments.putIfAbsent(i, rollset.get(i)[k]);
                if (val == null) {
                    verify(i);
                    if (conflicts > 0) {
                        //System.err.println("ROLE: " + i + " not added to actor: " + rollset.get(i)[k] + " conflicts: " + conflicts);
                        //System.err.println(assignments);
                        assignments.remove(i);
                        conflicts = 0;
                    }else{
                        //System.err.println("ROLE: " + i + " added to actor: " + rollset.get(i)[k]);
                        skådespelarna[rollset.get(i)[k]].addRolePlayed(i);
                        maximize(rollset.get(i)[k], i);
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        new Labb5();
    }

    void maximize (int skadespelare, int role){
        for (int i : skådespelarna[skadespelare].rolesAvailable){
            Object val = assignments.putIfAbsent(i, skadespelare);
            if(val == null) {
                verify(i);
                if (conflicts > 0) {
                    assignments.remove(i);
                    conflicts = 0;
                }else{
                    skådespelarna[skadespelare].addRolePlayed(i);
                    rolesCheck.remove((Integer) i);
                    // Kan det hör inträffa?
                    //superskådisar.remove((Integer) i);
                }
            }
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
            rolesCheck.push(roleNumber);
            amount--;
            roleNumber++;
        }
        this.rollset = ret;
        this.skådespelarna = skådisar;
    }

    private void readScenes (Kattio in, int amount) {
        int scen = 0;
        ArrayList<int[]> ret = new ArrayList<>();
        while (amount > 0) {
            int nrLine = in.getInt();
            int[] line = new int[nrLine];
            for (int i = 0; i < nrLine; i++) {
                line[i] = in.getInt() - 1;
                LinkedList<Integer> toPush = rolesScenes.get(line[i]);
                if(toPush == null){
                    //System.err.println("pushed " +  line[i]);
                    toPush = new LinkedList<>();
                }
                toPush.add(scen);
                rolesScenes.put(line[i], toPush);
            }
            ret.add(line);
            scen++;
            amount--;
        }
        //System.err.println(rolesScenes);
        this.scenset = ret;
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
}
