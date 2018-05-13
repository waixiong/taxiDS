package taxi_ds.images1;


import java.awt.Color;
import static java.awt.Color.white;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import taxi_ds.*;
import taxi_ds.MapArea;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PENG
 */
public class mapAndTaxi extends JComponent{
    private static JPanel panel;
    private static int column=0;
    private static int row=0;
    private static int number=1;
    private static int counter=0;
    private static ImageIcon backgroundImg;
    private static String pngFile;//Add;png for map with its respective size
    private Taxi taxiHolder;//holdTaxiAddress
    public static MapArea taxiLocation =  new MapArea(100, 100, 1);//Add
    private static final int time = 120;//Add just time constant
    private MapArea[][] map;
    
    public mapAndTaxi(JPanel panel, MapArea[][] map, Taxi taxiHolder){
        this.panel=panel;
        int size = map.length-2;
        this.map= map;
        setBounds(0,0,panel.getWidth(),panel.getHeight());
        if(size == 3) pngFile = "images/3.png";
        else if(size == 4) pngFile = "images/4.png";
        else if(size == 5) pngFile = "images/5.png";
        else pngFile = "images/6.png";
        backgroundImg=new ImageIcon(new ImageIcon(getClass().getResource(pngFile)).getImage());
        this.taxiHolder = taxiHolder;
    }
    
    public mapAndTaxi(JPanel panel){
        this.panel=panel;
        setBounds(0,0,panel.getWidth(),panel.getHeight());
        backgroundImg=new ImageIcon(new ImageIcon(getClass().getResource("images/4.png")).getImage());
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        g.drawImage(backgroundImg.getImage(), 100, 0, this);
        for(int i = 1; i < map.length-1; i++)
            for(int j = 1; j < map[i].length-1; j++){
                if(map[i][j].getCost() == 2){
                    g.setColor(new Color(0xCCCC00));
                    g.fillRoundRect((j-1)*64+102, (i-1)*64+2, 60, 60, 2, 2);
                }else if(map[i][j].getCost() == 3){
                    g.setColor(new Color(0xCC6600));
                    g.fillRoundRect((j-1)*64+102, (i-1)*64+2, 60, 60, 2, 2);
                }else if(map[i][j].getCost() == 4){
                    g.setColor(new Color(0xCC0000));
                    g.fillRoundRect((j-1)*64+102, (i-1)*64+2, 60, 60, 2, 2);
                }
            }
        g.setColor(white);
        g.fillRect(10, 0, 50, (int)(210*this.taxiHolder.getAvailableSpace()/this.taxiHolder.space));
        g.setColor(new Color(0xFFFF00));
        g.fillRect(10, (int)(210*this.taxiHolder.getAvailableSpace()/this.taxiHolder.space), 50, (int)(210*(this.taxiHolder.space-this.taxiHolder.getAvailableSpace())/this.taxiHolder.space));
        //people source
        g.setColor(new Color(0X5500ff));
        for(int i = 1; i < map.length-1; i++){
            //Passenger temp = taxiHolder.booking.get(i);
            //g.fillRect(102+(temp.initial.Y-1)*64, (temp.initial.X-1)*64+2, 10, 10*temp.people);
            for(int j = 1; j < map.length-1; j++){
                for(int p = 0; p < map[i][j].list.size(); p++)
                    g.fillRect((102+(j-1)*64)+10*p, (i-1)*64+2, 10, 10*map[i][j].list.get(p).people);
            }
        }
        g.fillRect(10, 400, 10, 10);
        //destination
        g.setColor(new Color(0xff0000));
        for(int i = 0; i < taxiHolder.contains.size(); i++){
            Passenger temp = taxiHolder.contains.get(i);
            g.fillRect(152+(temp.destination.Y-1)*64, (temp.destination.X-1)*64+52, 10, 10);
        }
        g.fillRect(140, 400, 10, 10);
        //show the GPS road
        g.setColor(new Color(0x00ff00));
        for(int i = 0; i < taxiHolder.GPS.size(); i++){
            MapArea temp = taxiHolder.GPS.get(i);
            g.fillRect(127+(temp.Y-1)*64, (temp.X-1)*64+27, 10, 10);
        }
        g.fillRect(270, 400, 10, 10);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString("Source", 22, 411);
        g.drawString("Destination", 152, 411);
        g.drawString("GPS", 282, 411);
        ImageIcon image2=new ImageIcon(new ImageIcon(getClass().getResource("images/1.png")).getImage());
        g.drawImage(image2.getImage(), 100+row,column, 64, 64, null);
    }
    
    
    //north
    private Timer timer1=new Timer(taxiLocation.getCost()*120,new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
                counter++;
                if(counter>=33){
                    timer1.stop();
                    counter=0;
                    return;
                }
                panel.repaint();
                column-=2;
                timer1.setDelay(taxiLocation.getCost()*15);
        }
    });
    
    public void moveNorth(){
        updateLocation();
        if(!timer1.isRunning()){
            timer1.start();
        }
    }
    
    private Timer timer3=new Timer(taxiLocation.getCost()*120, new ActionListener() {
    @Override
        public void actionPerformed(ActionEvent e) {
            counter++;
            if(counter>=33){
                timer3.stop();
                counter=0;
                return;
            }
            panel.repaint();
            column+=2;
            timer3.setDelay(taxiLocation.getCost()*15);
        
        }
    });

    public void moveSouth(){
        updateLocation();
        if(!timer3.isRunning()){
            timer3.start();
        }
    }

    //east
    private Timer timer2=new Timer(taxiLocation.getCost()*120,new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
                counter++;
                if(counter>=33){
                    timer2.stop();
                    counter=0;
                    return;
                }
                panel.repaint();
                row+=2;
                timer2.setDelay(taxiLocation.getCost()*15);
        }
    });
    
    public void moveEast(){
        updateLocation();
        if(!timer2.isRunning()){
            timer2.start();
        }
    }

//west
private Timer timer4=new Timer(taxiLocation.getCost()*120,new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
            counter++;
            if(counter>=33){
                timer4.stop();
                counter=0;
                return;
            }
            panel.repaint();
            row-=2;
            timer4.setDelay(taxiLocation.getCost()*15);
    }
});
        
    public void moveWest(){
        updateLocation();
        if(!timer4.isRunning()){
            timer4.start();
        }
    }
    
    //Add
    private void updateLocation(){
        this.taxiLocation = this.taxiHolder.getCurrentPosition();
    }
    
    public boolean trafficEvent(){
        boolean change = false;
        for(int i = 1; i < map.length-1; i++)
            for(int j = 1; j < map.length-1; j++)
                change = map[i][j].trafficEvent() || change;
        repaint();
        return change;
    }
}
