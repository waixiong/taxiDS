/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Side;
import javafx.scene.chart.*;

import taxi_ds.images1.*;
/**
 *
 * @author TheChee
 */
public class Taxi_DS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        Scanner in = new Scanner(System.in);
        int size = getSize();
        int person = getPSize();
        MapArea[][] map = new MapArea[size+2][size+2];//size+=2
        Random r = new Random();
        
        /*map initialise*/
        for(int i = 1; i < map.length - 1; i++)
            for(int j = 1; j < map.length - 1; j++)
                map[i][j] = new MapArea(i, j);
        
        for(int i = 1; i < map.length - 1; i++)
            for(int j = 1; j < map.length - 1; j++){
                if(map[i-1][j] != null) map[i][j].north = map[i-1][j];
                if(map[i+1][j] != null) map[i][j].south = map[i+1][j];
                if(map[i][j-1] != null) map[i][j].west = map[i][j-1];
                if(map[i][j+1] != null) map[i][j].east = map[i][j+1];
            }
        //map[2][1].cost = 2;map[3][1].cost = 2; map[1][3].cost = 2;map[2][3].cost = 2;
        /*map initialise*/
        
        //Taxi AND Frame(Graphics) Initailize
        ArrayList<Passenger> dynamicC = new ArrayList<>();
        Frame[] holder = new Frame[1];//hold address
        Taxi taxi = new Taxi(map[1][1], holder, dynamicC);
        Frame f = new Frame(map, taxi);
        holder[0] = f;
        
        //Enter passenger
        for(int i = 0; i < person/5; i++){
            //System.out.printf("Enter passenger #%d label: ", i+1);
            String label = Character.toString((char)(i + 65));//in.nextLine();
            //System.out.printf("Enter passenger #%d source location: ", i+1);
            //String[] ini = in.nextLine().split(",");
            ///*up is manual, down is random auto*///
            int[] initial = new int[2];
            for(int j = 0; j < initial.length; j++)
                initial[j] = /*Integer.parseInt(ini[j])*/r.nextInt(size)+1;
            //System.out.printf("Enter passenger #%d source location: ", i+1);
            //String[] des = in.nextLine().split(",");
            ///*up is manual, down is random auto*///
            int[] destination = new int[2];
            for(int j = 0; j < initial.length; j++)
                destination[j] = /*Integer.parseInt(des[j])*/r.nextInt(size)+1;
            //determine how many people
            int people = r.nextInt(100) < 94 ? 1 : (r.nextInt(100) < 94 ? 2 : 3);
            if(initial[0] != destination[0] || initial[1] != destination[1]){
                //map[initial[0]][initial[1]].passengers.add(new Passenger(map[destination[0]][destination[1]]));
                Passenger customer = new Passenger(label,map[initial[0]][initial[1]], map[destination[0]][destination[1]], people);
                taxi.booking.add(customer);
                customer.addToMap();
            }
            else{
                i--;
                System.out.println("Same source and destination, re-enter");
            }
        }
        //dynamic customer
        for(int i = taxi.booking.size(); i < person; i++){
            //System.out.printf("Enter passenger #%d label: ", i+1);
            String label = Character.toString((char)(i + 65));//in.nextLine();
            //System.out.printf("Enter passenger #%d source location: ", i+1);
            //String[] ini = in.nextLine().split(",");
            ///*up is manual, down is random auto*///
            int[] initial = new int[2];
            for(int j = 0; j < initial.length; j++)
                initial[j] = /*Integer.parseInt(ini[j])*/r.nextInt(size)+1;
            //System.out.printf("Enter passenger #%d source location: ", i+1);
            //String[] des = in.nextLine().split(",");
            ///*up is manual, down is random auto*///
            int[] destination = new int[2];
            for(int j = 0; j < initial.length; j++)
                destination[j] = /*Integer.parseInt(des[j])*/r.nextInt(size)+1;
            //determine how many people
            int people = r.nextInt(100) < 94 ? 1 : (r.nextInt(100) < 94 ? 2 : 3);
            if(initial[0] != destination[0] || initial[1] != destination[1]){
                //map[initial[0]][initial[1]].passengers.add(new Passenger(map[destination[0]][destination[1]]));
                Passenger customer = new Passenger(label,map[initial[0]][initial[1]], map[destination[0]][destination[1]], people);
                dynamicC.add(customer);
            }
            else{
                i--;
                System.out.println("Same source and destination, re-enter");
            }
        }
        
        for(int i = 1; i < map.length-1; i++){
            for(int j = 1; j < map[i].length-1; j++){
                System.out.print(map[i][j].cost + " ");
            }
            System.out.println("");
        }
        
        f.setAlwaysOnTop(true);
        f.setVisible(true);//show Frame
        
        
        System.out.println("");
        System.out.println("RESULT");
        while(taxi.booking.size() != 0 || taxi.contains.size() != 0 || !dynamicC.isEmpty()){
            boolean taskExist = false;
            while(taxi.booking.size() != 0 || taxi.contains.size() != 0){
                taxi.getPath();
//              for(int i = 0; i < taxi.GPS.size(); i++){
//                  taxi.GPS.get(i).printPosition();
//              }
                taxi.moveToGoal();
                taxi.drop();
                taxi.fetch();
                //trafficEvent
//              boolean temp=false;
//              for(int i = 1; i < map.length-1; i++)
//                  for(int j = 1; j < map.length-1; j++)
//                      temp = map[i][j].trafficEvent();
                //dynamic passenger
                f.getImage().repaint();
                taskExist=true;
            }
            if(!taskExist){
                taxi.rest();
                //taxi.dynamicCustomer();
            }
        }
        
        taxi.getCurrentPosition().printPosition();
        System.out.println(taxi.time);
        
        System.out.println(taxi);
        taxi.closeLog();
//        try{
//            Process p = Runtime.getRuntime().exec("python3 graph.py");
//            
//        }catch(Exception e){
//            System.out.println("Python compiler has some error!!!");
//        }
        //SChart c = new SChart();
    }
    
    public static int getSize(){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter map size: ");
        int size;
        try{
            size = in.nextInt();
            if(size > 6){
                System.out.println("Max size is 6 so the size is set to 6");
                return 6;
            }else if(size<3){
                System.out.println("Min size is 3 so the size is set to 3");
                return 3;
            }
            return size;
        }catch(Exception e){
            System.out.println("Is Integer, Please reenter: ");
            return getSize();
        }
    }
    
    public static int getPSize(){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter number of passenger: ");
        int size;
        try{
            size = in.nextInt();
            if(size > 26){
                System.out.println("Max is 26 so the number of passenger label is 26");
                return 26;
            }
            return size;
        }catch(Exception e){
            System.out.println("Is Integer, Please reenter: ");
            return getSize();
        }
    }
    
//    public void dynamicCustomer(ArrayList<Passenger> list, Taxi taxi){
//        Random r = new Random();
//        if(r.nextInt(100)>70 && !list.isEmpty()){
//            Passenger c = list.remove(0);
//            taxi.booking.add(c);
//            c.addToMap();
//        }
//    }
    
    
}
