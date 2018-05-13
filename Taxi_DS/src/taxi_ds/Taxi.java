/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;
import java.util.*;
import java.io.*;
import taxi_ds.images1.*;
/**
 *
 * @author TheChee
 */
public class Taxi {
    public final int space = 4;//4 or 6
    //final int maxFuel = (int)Math.pow(2, 30);//infinite for now
    private int availableSpace = space;
    private MapArea currentPosition;
    public ArrayList<Passenger> booking;//passenger sources
    public ArrayList<Passenger> contains; //people in taxi
    public ArrayList<MapArea> GPS;
    int time;
    PrintWriter write;//log book
    PrintWriter cusRe;//customerReport
    private Frame[] image;
    boolean envChange = false;
    ArrayList<Passenger> dynamicPassenger;
    
    public Taxi(MapArea currentPosition, Frame[] image, ArrayList<Passenger> dynamicPassenger){
        this.currentPosition = currentPosition;
        this.booking = new ArrayList<>();
        this.contains = new ArrayList<>();
        this.GPS = new ArrayList<>();
        time = 0;
        this.dynamicPassenger = dynamicPassenger;
        this.image = image;
        try{
            write = new PrintWriter(new File("logBook.txt"));
            write.println("Execution Log:\n");
        }catch(IOException io){
            System.out.println("logBook has some error!!!");
        }
        try{
            cusRe = new PrintWriter(new File("Passenger.csv"));
            cusRe.println("Label,WaitingTime,RidingTime,Time leave taxi");
        }catch(IOException io){
            System.out.println("logBook has some error!!!");
        }
    }
    
    public int getAvailableSpace(){
        return this.availableSpace;
    }
    
    public void closeLog(){
        write.close();
        cusRe.close();
    }
    
    public MapArea getCurrentPosition(){
        return this.currentPosition;
    }
    
    public void moveNorth() throws InterruptedException{
        if(this.currentPosition.north != null){
            //
            updateTime(this.currentPosition.getCost());
            image[0].getImage().moveNorth();
            Thread.sleep(this.currentPosition.cost * 1000);
            this.currentPosition = this.currentPosition.north;
            //Taxi move north
            write.printf("[%d] Taxi move north\n", time);
            System.out.printf("[%d] Taxi move north\n", time);
        }
    }
    
    public void moveSouth() throws InterruptedException{
        if(this.currentPosition.south != null){
            updateTime(this.currentPosition.getCost());
            image[0].getImage().moveSouth();
            Thread.sleep(this.currentPosition.cost * 1000);
            this.currentPosition = this.currentPosition.south;
            //Taxi move south
            write.printf("[%d] Taxi move south\n", time);
            System.out.printf("[%d] Taxi move south\n", time);
        }
    }
    public void moveEast() throws InterruptedException{
        if(this.currentPosition.east != null){
            updateTime(this.currentPosition.getCost());
            image[0].getImage().moveEast();
            Thread.sleep(this.currentPosition.cost * 1000);
            this.currentPosition = this.currentPosition.east;
            //Taxi move east
            write.printf("[%d] Taxi move east\n", time);
            System.out.printf("[%d] Taxi move east\n", time);
        }
    }
    public void moveWest() throws InterruptedException{
        if(this.currentPosition.west != null){
            updateTime(this.currentPosition.getCost());
            image[0].getImage().moveWest();
            Thread.sleep(this.currentPosition.cost * 1000);
            this.currentPosition = this.currentPosition.west;
            //Taxi move west
            write.printf("[%d] Taxi move west\n", time);
            System.out.printf("[%d] Taxi move west\n", time);
        }
    }
    
    public void fetch(){
        if(space != 0){  
            boolean exist = false;
            int i = 0;
            for(i = 0; i < booking.size();i++){
                if(booking.get(i).initial == this.currentPosition){
                    exist = true;
                    break;
                }
            }
            if(exist && this.availableSpace != 0){
                Passenger f = this.booking.remove(i);
                f.removeFromMap();
                this.contains.add(f);
                this.availableSpace-=f.people;
                write.printf("[%d] Taxi fetch Passenger %s\n", time, f.label);
                System.out.printf("[%d] Taxi fetch Passenger %s, %d people\n", time, f.label, f.people);
            }
        }
    }
    
    public void drop(){ 
        boolean exist = false;
        int i = 0;
        for(i = 0; i < contains.size();i++){
            if(contains.get(i).destination == this.currentPosition){
                exist = true;
                break;
            }
        }
        if(exist){
            Passenger d = this.contains.remove(i);
            this.availableSpace+=d.people;
            write.printf("[%d] Taxi drop Passenger %s\n", time, d.label);
            cusRe.printf("%s,%d,%d,%d\n", d.label, d.wTime, d.rTime, time);
            System.out.println(d.message());
        }
        
    }
    
    public void getPathAlgo(/*ArrayList<PathMemory> pathList*/MapArea des){
        int[] mag = this.currentPosition.magWith(des);
        int time = 0;
        MapArea location = this.currentPosition;
        if(mag == null)
            time = -1;
        else{
            int ns = 0, we = 0;
            while(ns != Math.abs(mag[0]) || we != Math.abs(mag[1])){
                time += location.cost;
                if(ns == Math.abs(mag[0])){
                    if(mag[1] > 0)
                        location = location.east;
                    else
                        location = location.west;
                    we++;
                }else if(we == Math.abs(mag[1])){
                    if(mag[0] > 0)
                        location = location.south;
                    else
                        location = location.north;
                    ns++;
                }else{//no optimize for this part
                    int nsT,weT;
                    if(mag[0] > 0)
                        nsT = location.south.cost;
                    else
                        nsT = location.north.cost;
                    if(mag[1] > 0)
                        weT = location.east.cost;
                    else
                        weT = location.west.cost;
                    
                    if(nsT <= weT){
                        if(mag[0] > 0)
                            location = location.south;
                        else
                            location = location.north;
                        ns++;
                    }else if(weT < nsT){
                        if(mag[1] > 0)
                            location = location.east;
                        else
                            location = location.west;
                        we++;
                    }else{
                        
                    }
                }
                GPS.add(location);
            }
        }
    }
    
    public void getPath(){
//        PathMemory initial = new PathMemory(this.currentPosition, this.time, this.booking, this.contains);
//        ArrayList<PathMemory> collectionPath = new ArrayList<>();
//        collectionPath.add(initial);
//        getPathAlgo(collectionPath);
        ArrayList<GoalNode> goalList = new ArrayList<>();
        if(this.availableSpace > 0)
        for(int i = 0; i < booking.size(); i++){
            MapArea g = booking.get(i).initial;
            if(this.availableSpace >= booking.get(i).people)
                goalList.add(new GoalNode(g, this.currentPosition.timeWith(g)));
        }//add source goal
        for(int i = 0; i < contains.size(); i++){
            MapArea g = contains.get(i).destination;
            goalList.add(new GoalNode(g, this.currentPosition.timeWith(g)));
        }
        goalList.sort(new Comparator<GoalNode>() {
            @Override
            public int compare(GoalNode t, GoalNode t1) {
                return t.time - t1.time;
            }
            
        });
//        GoalNode min = goalList.get(0);
//        for(int i = 1; i < goalList.size(); i++)
//            if(goalList.get(i).time < min.time)
//                min = goalList.get(i);
        getPathAlgo(goalList.get(0).goal);
    }
    
    public void moveToGoal() throws InterruptedException{
        while(GPS.size() > 0){
            if(image[0].getImage().trafficEvent() || this.dynamicCustomer() || this.envChange){
                this.envChange = true;
                break;
            }
            MapArea next = GPS.get(0);
            String d = this.currentPosition.directionTo(next);
            //Thread.sleep(this.currentPosition.cost * 1000);
            if(d.equals("N"))
                this.moveNorth();
            else if(d.equals("S"))
                this.moveSouth();
            else if(d.equals("E"))
                this.moveEast();
            else if(d.equals("W"))
                this.moveWest();
            GPS.remove(0);
        }
        if(this.envChange){
            GPS.clear();
            this.envChange = false;
            this.getPath();
            this.moveToGoal();
        }
    }
    
    public void updateTime(int time){
        this.time += time;
        for(int i = 0; i < booking.size(); i++){
            booking.get(i).wTime+=time;
        }
        for(int i = 0; i < contains.size(); i++){
            contains.get(i).rTime+=time;
        }
    }
    
    public boolean dynamicCustomer(){
        Random r = new Random();
        boolean change = false;
        if(r.nextInt(100)>70 && !this.dynamicPassenger.isEmpty()){
            change = true;
            Passenger c = this.dynamicPassenger.remove(0);
            this.booking.add(c);
            c.addToMap();
        }
        return change;
    }
    
    public void rest() throws InterruptedException{
        this.updateTime(1);
        write.printf("[%d] Taxi rest and wait for order\n", time);
        System.out.printf("[%d] Taxi rest and wait for order\n", time);
        Thread.sleep(1000);
        this.envChange = image[0].getImage().trafficEvent() || this.dynamicCustomer();
    }
}
