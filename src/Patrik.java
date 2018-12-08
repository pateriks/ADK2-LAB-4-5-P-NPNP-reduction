import java.awt.*;
import java.util.*;

/**
 * Created by Daniel and Patrik on 12/3/2018.
 */
public class Patrik {

    Random r = new Random();

    int roller, scener, actors;

    /*store input in memory*/
    ArrayList<int[]> rollset = new ArrayList<>();
    ArrayList<int[]> scenset = new ArrayList<>();
    HashMap<Integer, LinkedList> actorset = new HashMap<>();

    skådespelare[] skådespelarna;

    /*result*/
    HashMap<Integer, Integer> assignments;
    HashMap<Integer, LinkedList<Integer>> scenes;

    ArrayList<int[]> roleAppearsIn = new ArrayList<>();

    LinkedList<Integer> rolesCheck = new LinkedList<>();

    LinkedList<Integer> superskådisar = new LinkedList<>();

    int totalaSuperskådisar = 0;
    int totalaOrdinarie = 0;

    /*stack variables*/
    int conflicts = 0;

    Patrik(){
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
        scenes = new HashMap<>();
        skådespelarna = new skådespelare[actors];

        readRoles(io, roller);
        readScenes(io, scener);

        r.setSeed(System.currentTimeMillis());
    }

    public void verify (int role){

        if(actorset.get(role) == null){
            return;
        }

        for(Integer i : (LinkedList<Integer>) actorset.get(role)){
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

        if(actors >= 2) {
        }else{
            System.err.println("unsolvable");
            System.exit(0);
        }

        naive();

        return printActors();
    }


    public String printActors(){
        StringBuilder sb = new StringBuilder();

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
        /* Iterate through every role that actor 1 can take and try to find some role for actor 2
        If roles can be find that makes no conflict break, otherwise increment j and try again
         */
        boolean assignedDiva2 = false;
        int j = 0;
        while(true) {
            int roleDiva1 = skådespelarna[0].rolesAvailable.get(j);
            assignments.putIfAbsent(roleDiva1, 0);
            for (int i = 0; i < skådespelarna[1].rolesAvailable.size(); i++) {
                int roleDiva2 = skådespelarna[1].rolesAvailable.get(i);
                Object val = assignments.putIfAbsent(roleDiva2, 1);
                if (val == null) {
                    verify(roleDiva2);
                    if (conflicts > 0) {
                        assignments.remove(roleDiva2);
                        conflicts = 0;
                    }else{
                        assignedDiva2 = true;
                        skådespelarna[1].rolesPlayed.add(roleDiva2);
                        skådespelarna[0].rolesPlayed.add(roleDiva1);
                        break;
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

        long millis = System.currentTimeMillis();
        while (!rolesCheck.isEmpty() && (System.currentTimeMillis()-millis)<1200){
            int i = rolesCheck.poll();
            for (int k = 0; k < rollset.get(i).length; k++) {
                Object val = assignments.putIfAbsent(i, rollset.get(i)[k]);
                if (val == null) {
                    verify(i);
                    if (conflicts > 0) {
                        assignments.remove(i);
                        conflicts = 0;
                    }else{
                        skådespelarna[rollset.get(i)[k]].addRolePlayed(i);
                        /*
                        if(skådespelarna[rollset.get(i)[k]].haveMany || skådespelarna[rollset.get(i)[k]].rolesPlayed.size() > 2) {
                            skådespelarna[rollset.get(i)[k]].haveMany = true;
                            maximize(rollset.get(i)[k], i);
                        }
                        */
                        maximize(rollset.get(i)[k], i);
                        break;
                    }
                }
            }
            if(!assignments.containsKey(i)){
                assignments.put(i, 123);
                superskådisar.add(i);
                Collections.sort(superskådisar);
                totalaSuperskådisar++;
            }
        }
    }

    public static void main(String[] args){
        new Patrik();
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
                    /*
                    if(superskådisar.contains(i)){
                        superskådisar.remove((Integer) i);
                        totalaSuperskådisar--;
                    }
                    */
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
            rolesCheck.add(amount-1);
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
                LinkedList<Integer> toPush = actorset.get(line[i]);
                if(toPush == null){
                    //System.err.println("pushed " +  line[i]);
                    toPush = new LinkedList<>();
                }
                toPush.add(scen);
                actorset.put(line[i], toPush);
            }
            ret.add(line);
            scen++;
            amount--;
        }
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
