/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;

/**
 *
 * @author TheChee
 */
public class Passenger {
    public MapArea initial;
    public MapArea destination;
    String label;
    int wTime;//waiting
    int rTime;//riding
    public int people;
    
    public Passenger(String label, MapArea initial, MapArea destination){
        this.label = label;
        this.initial = initial;
        this.destination = destination;
        this.wTime = 0;
        this.rTime = 0;
        this.people = 1;
    }
    
    public Passenger(String label, MapArea initial, MapArea destination, int people){
        this.label = label;
        this.initial = initial;
        this.destination = destination;
        this.wTime = 0;
        this.rTime = 0;
        this.people = people;
    }
    
    public void addToMap(){
        this.initial.list.add(this);
    }
    
    public void removeFromMap(){
        this.initial.list.remove(this);
    }
    
    public String message(){
        return "Passenger("+people+") "+label+" wait for "+wTime+" minutes, ride for "+rTime+" minutes";
    }
}
