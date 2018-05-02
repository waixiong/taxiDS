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
public class GoalNode implements Comparable<GoalNode>{
    MapArea goal;
    int time;
    
    public GoalNode(MapArea goal, int time){
        this.goal = goal;
        this.time = time;
    }

    @Override
    public int compareTo(GoalNode t) {
        return t.time - this.time;
    }
}
