/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;

import java.util.*;

/**
 *
 * @author TheChee
 */
public class MapArea {
    int cost = 1;//may change
    public final int X; public final int Y; //coordinate
    MapArea north = null;
    MapArea south = null;
    MapArea east = null;
    MapArea west = null;
    public ArrayList<Passenger> list = new ArrayList<>();
    
    //ArrayList<Passenger> passengers = new ArrayList<>();
    
    public MapArea(int x, int y){
        X = x;
        Y = y;
        Random r = new Random();
        cost = r.nextInt(100) < 75 ? 1 : (r.nextInt(100) < 75 ? 2 : (r.nextInt(100) < 75 ? 3 : 4));
    }
    
    public MapArea(int x, int y, int cost){
        X = x;
        Y = y;
        this.cost = cost;
    }
    
    public int getCost(){
        return cost;
    }
    
    public void printPosition(){
        System.out.println(X + "  " + Y);
        /*if(north != null)
        System.out.println("North = " + this.north.X + "  " + this.north.Y);
        if(south != null)
        System.out.println("South = " + this.south.X + "  " + this.south.Y);
        if(west != null)
        System.out.println("West = " + this.west.X + "  " + this.west.Y);
        if(east != null)
        System.out.println("East = " + this.east.X + "  " + this.east.Y);*///test n bugging
    }
    
    public int[] magWith(MapArea des){//magnitude with destination
        int x = des.X - this.X;
        int y = des.Y - this.Y;
        int[] dis = {x, y};
        MapArea location = this;
        for(int i = 0; i < Math.abs(x); i++){
            if(x > 0)
                location = location.south;
            else if(x < 0)
                location = location.north;
        }
        for(int i = 0; i < Math.abs(y); i++){
            if(y > 0)
                location = location.east;
            else if(y < 0)
                location = location.west;
        }
        if(location == des)
            return dis;
        else
            return null;
    }
    
    public int distanceWith(MapArea des){
        int[] mag = this.magWith(des);
        if(mag == null)
            return -1;
        else
            return Math.abs(mag[0])+ Math.abs(mag[1]);
    }
    
    public int timeWith(MapArea des){
        int[] mag = this.magWith(des);
        int time = 0;
        MapArea location = this;
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
            }
        }
        return time;
    }
    
    public String directionTo(MapArea next){
        if(this.distanceWith(next)==1){
            if(next == this.north) return "N";
            else if(next == this.south) return "S";
            else if(next == this.east) return "E";
            else if(next == this.west) return "W";
        }
        return "Not Connected";
    }
    
    public boolean trafficEvent(){
        boolean change = false;
        Random r = new Random();
        int rand = r.nextInt(100)+1;
        if(rand < 2 + (this.cost*this.cost-1)){
            if(cost != 1)this.cost--;
            change = true;
        }else if(rand > 99){
            if(cost != 4)this.cost++;
            change = true;
        }
        return change;
    }
}
