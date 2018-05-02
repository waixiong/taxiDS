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
    MapArea initial;
    MapArea destination;
    String label;
    int wTime;//waiting
    int rTime;//riding
    
    public Passenger(String label, MapArea initial, MapArea destination){
        this.label = label;
        this.initial = initial;
        this.destination = destination;
        this.wTime = 0;
        this.rTime = 0;
    }
    
    public String message(){
        return "Passenger "+label+" wait for "+wTime+" minutes, ride for "+rTime+" minutes";
    }
}
