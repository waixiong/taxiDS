/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;
import java.util.*;
import java.io.*;
import taxi_ds.images1.mapAndTaxi;
/**
 *
 * @author TheChee
 */
public class Taxi {
    final int space = 4;//4 or 6
    //final int maxFuel = (int)Math.pow(2, 30);//infinite for now
    private int availableSpace = space;
    private MapArea currentPosition;
    ArrayList<Passenger> booking;//passenger sources
    ArrayList<Passenger> contains; //people in taxi
    ArrayList<MapArea> GPS;
    int time;
    PrintWriter write;//log book
    private mapAndTaxi image;
    
    public Taxi(MapArea currentPosition, mapAndTaxi image){
        this.currentPosition = currentPosition;
        this.booking = new ArrayList<>();
        this.contains = new ArrayList<>();
        this.GPS = new ArrayList<>();
        time = 0;
        this.image = image;
        try{
            write = new PrintWriter(new File("logBook.txt"));
            write.println("Execution Log:\n");
        }catch(IOException io){
            System.out.println("logBook has some error!!!");
        }
    }
    
    public void closeLog(){
        write.close();
    }
    
    public MapArea getCurrentPosition(){
        return this.currentPosition;
    }
    
    public void moveNorth() throws InterruptedException{
        if(this.currentPosition.north != null){
            //
            updateTime(this.currentPosition.getCost());
            image.moveNorth();
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
            image.moveSouth();
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
            image.moveEast();
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
            image.moveWest();
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
                this.contains.add(f);
                this.availableSpace--;
                write.printf("[%d] Taxi fetch Passenger %s\n", time, f.label);
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
            this.availableSpace++;
            write.printf("[%d] Taxi drop Passenger %s\n", time, d.label);
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
        if(this.availableSpace != 0)
        for(int i = 0; i < booking.size(); i++){
            MapArea g = booking.get(i).initial;
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
        getPathAlgo(goalList.get(0).goal);
    }
    
    public void moveToGoal() throws InterruptedException{
        while(GPS.size() > 0){
            MapArea next = GPS.remove(0);
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
}
