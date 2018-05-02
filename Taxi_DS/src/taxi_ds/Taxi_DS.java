/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi_ds;
import java.util.*;
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
        Taxi[] taxiHolder = new Taxi[1];//hold address
        Frame f = new Frame(map, taxiHolder);
        Taxi taxi = new Taxi(map[1][1], f.getImage());
        taxiHolder[0] = taxi;
        
        //Enter passenger
        for(int i = 0; i < person; i++){
            System.out.printf("Enter passenger #%d label: ", i+1);
            String label = in.nextLine();
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
            if(initial[0] != destination[0] || initial[1] != destination[1]){
                //map[initial[0]][initial[1]].passengers.add(new Passenger(map[destination[0]][destination[1]]));
                taxi.booking.add(new Passenger(label,map[initial[0]][initial[1]], map[destination[0]][destination[1]]));
            }
            else{
                i--;
                System.out.println("Same source and destination, re-enter");
            }
        }
        System.out.println("");
        System.out.println("");
        System.out.println("Map");
        for(int i = 1; i < map.length-1; i++){
            for(int j = 1; j < map[i].length-1; j++){
                System.out.print(map[i][j].cost + " ");
                taxi.write.print(map[i][j].cost + " ");
            }
            System.out.println("");
            taxi.write.println("");
        }
        
        f.setVisible(true);//show Frame
        
        taxi.write.println("\ncheck map");
        //checking start//
        for(int i = 0; i < taxi.booking.size(); i++){
            MapArea ini = taxi.booking.get(i).initial;
            MapArea des = taxi.booking.get(i).destination;
            taxi.write.println(taxi.booking.get(i).label+": "+ini.X+" "+ini.Y+"   "+des.X+" "+des.Y);
        }
        //checking finish//
        
        System.out.println("");
        System.out.println("RESULT");
        taxi.write.println("\nExecution Log:\n");
        while(taxi.booking.size() != 0 || taxi.contains.size() != 0){
            taxi.getPath();
//            for(int i = 0; i < taxi.GPS.size(); i++){
//                taxi.GPS.get(i).printPosition();
//            }
            taxi.moveToGoal();
            taxi.drop();
            taxi.fetch();
        }
        taxi.getCurrentPosition().printPosition();
        System.out.println(taxi.time);
        
        System.out.println(taxi);
        taxi.closeLog();
    }
    
    public static int getSize(){
        Scanner in = new Scanner(System.in);
        System.out.print("Enter map size: ");
        int size;
        try{
            size = in.nextInt();
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
            return size;
        }catch(Exception e){
            System.out.println("Is Integer, Please reenter: ");
            return getSize();
        }
    }
    
}
