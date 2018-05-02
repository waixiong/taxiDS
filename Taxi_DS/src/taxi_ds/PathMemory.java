/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;
import java.util.ArrayList;
/**
 *
 * @author TheChee
 */
public class PathMemory {
    ArrayList<MapArea> path;
    int cost;
    int hCost;
    int lastGoal;
    ArrayList<ArrayList<MapArea>> goal;
    MapArea now;
    
    public PathMemory(MapArea location, int cost, ArrayList<Passenger> customerOrder, ArrayList<Passenger> customerOnBoard){
        path = new ArrayList<>();
        path.add(location);
        now = location;
        this.cost = cost;
        for(int i = 0; i < customerOrder.size(); i++){
            ArrayList<MapArea> goalNode = new ArrayList<>();
            Passenger passenger= customerOrder.get(i); 
            goalNode.add(passenger.initial);
            goalNode.add(passenger.destination);
            this.goal.add(goalNode);
        }
        for(int i = 0; i < customerOnBoard.size(); i++){
            ArrayList<MapArea> goalNode = new ArrayList<>();
            Passenger passenger= customerOnBoard.get(i);
            goalNode.add(passenger.destination);
            this.goal.add(goalNode);
        }
        this.lastGoal = 0;
    }
    
    public PathMemory(PathMemory pathB, MapArea now){
        this.path = pathB.path;
        this.path.add(now);
        this.cost = pathB.cost + pathB.path.get(pathB.path.size()-1).getCost();
        this.goal = pathB.goal;
        this.now = now;
        this.lastGoal = pathB.lastGoal + 1;
        checkGoal();
    }
    
    public void checkGoal(){
        boolean reach = false;
        for(int i = 0; i < this.goal.size(); i++){
            if(this.now == this.goal.get(i).get(0)){
                this.goal.get(i).remove(0);
                lastGoal = 0;
                if(this.goal.get(i).size() == 0)
                    this.goal.remove(i);
                break;
            }
        }
    }
}