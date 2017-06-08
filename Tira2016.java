/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Makonn
 */

public class Tira2016 extends PriorityQueue {

    /**
     * @param args the command line arguments
     */
    
    //Variables
    public final String QUITD = "Ohjelma lopetettu.";
    public final String ADDED = "Lisatty";
    public final String KEYALREADY = " on jo jonossa";
    public final String KEY = "Avain ";
    public final String REMOVED = "Poistettu";
    public final String SMALLESTPRINT = "Pienin alkio on: ";
    public final String NULL = "Jono on tyhjä.";
    public final String ERROR = "Virheellinen syöte.";
    public final String EMPTY = "Tyhja kohta";
    public final String QUIT = "q";
    public final String SIZE = "s";
    public final String ADD = "i";
    public final String SMALLESTREMOVAL = "r";
    public final String SMALLESTSEARCH = "m";
    public final String PRINT = "p";
    public static String input = "";
    public static String output = "";
    
    //Root of the heap
    Item root;
    
    //Array for reading input
    public String[] values;
    
    //Array for data
    public ArrayList <Object> data = new ArrayList <Object> ();
    
    public static void main(String[] args) {

        Tira2016 run = new Tira2016();
        
        input = args[0];
        output = args[1];
               
        run.readInput();
    }
    
    //Method for reading input
    public  void readInput(){
        
        String line;
        
        try {
       
            BufferedReader br = new BufferedReader( new FileReader(input));

            do{
                
                line = br.readLine();
                
               if(line != null){
                  
                  line = commaChecker(line);
                  values = line.split(" ");

                  if(values[0].equals(ADD)){
                       
                     insertItem(values);

                  }
                  else if(values[0].equals(QUIT)|| values[0].equals(SIZE) || 
                        values[0].equals(SMALLESTREMOVAL) || values[0].equals(SMALLESTSEARCH) || 
                        values[0].equals(PRINT)){
                       
                     forfarder(values[0]);
                       
                  }
                       
                  else
                     writeOutput(ERROR);
               
               }
               else
                  writeOutput(EMPTY);
            }
            while(br.ready());
        }
        
        catch(IOException e){
            System.out.println("File not found.");
        }
        
    }
    
    //Method for writing output
    public void writeOutput(String print){
        
        try {
            
            System.out.println(print);
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(output, true)); 		
            bw.write(print);
            bw.newLine();
            bw.close();
        } 
        
        catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    
    }
    
    //Handle the input
    public void forfarder(String a){
        
        if(a.equals(QUIT)){
            writeOutput(QUITD);
        }
        else if(a.equals(SIZE)){
            writeOutput("" + size());
        }
        else if(a.equals(SMALLESTREMOVAL)){
            removeMinElement();
        }
        else if(a.equals(SMALLESTSEARCH)){
            minKey();
        }
        else if(a.equals(PRINT)){
            heapVisualisation();
        }
        
    }
    
    //Method for inserting into data array in its right place
    public void insertItem(String[] values){
        
        Item add = new Item();

        if(alreadyInHeapChecker(add)){
            
            add.setKey(parseInt(values[1]));
            add.setStringPre(values[2]);
            
            data.add(add);
            
            //count left and right child
            int child = data.size() - 1;
            int parent = (child - 1) / 2;

            Item parentObject = (Item)data.get(parent);
            
            //Swap until key is on its right place
            while(parent >= 0 && add.getKey() < parentObject.getKey()){

                Collections.swap(data, parent, child);

                child = parent;
                parent = (child - 1) / 2;

                parentObject = (Item)data.get(parent);

            }

            root = (Item)data.get(0);

            writeOutput("(" + add.getKey() + ", " + add.getStringPre() + ") " + ADDED);
        }
    }
    
    //Method for removing smallest key
    public void removeMinElement(){
        
        if(data != null){
            
            Item printObject = (Item)data.get(0);
            
            //Swap and remove min key
            Collections.swap(data, 0, size() - 1);
            
            data.remove(size() - 1);
            
            int parent = 0;
            boolean cont = true;
            
            //Rearrange array
            while(true){
            
                int leftChild = 2 * parent + 1;
                int rightChild = leftChild + 1;
                int smallerChild;
                
                if(leftChild >= data.size()){
                    root = (Item)data.get(0);
                    writeOutput("(" + printObject.getKey() + ", " + printObject.getStringPre() + ") " + REMOVED);
                    return;
                }
                
                if(leftChild > rightChild)
                    smallerChild = rightChild;
                else
                    smallerChild = leftChild;
                
                Item parentObject = (Item)data.get(parent);
                Item smallerChildObject = (Item)data.get(smallerChild);
                
                if(parentObject.getKey() > smallerChildObject.getKey()){
                    
                    Collections.swap(data, parent, smallerChild);
                    
                    parent = smallerChild;
                }
                else
                    cont = false;
                
            }
            
        }
        else
            writeOutput(NULL);
    }
    
    //Run through heap with preorder
    public void preorder(int index, int level){
        
        if (index >= data.size()) {
            return;
        }
        
        Item object = (Item)data.get(index);
        
        String space = "";
        
        for (int i = 0; i < level; i++) {
            space = space + "   ";
        }
        
        writeOutput(space + object.getKey());
        
        preorder(index * 2 + 1, level + 1 );
        preorder(index * 2 + 2, level + 1 );
        
    }
    
    //Use preorder to visalize heap
    public void heapVisualisation(){
        
       preorder(0, 0);       
       
    }
    
    public void minKey(){
        if(data != null){
            writeOutput(SMALLESTPRINT + root.getKey()); 
        }
        else{
            writeOutput(NULL);
        }
    }
    
    @Override
    public int size(){
        return data.size();
    }
    
    //Check if the key is already in the heap
    public boolean alreadyInHeapChecker(Item add){
        
        for(int i = 0; i < data.size(); i++){
            
            Item comparison = (Item)data.get(i);
            
            if(add.getKey() == comparison.getKey()){
                writeOutput(comparison.getKey() + " " + KEYALREADY);
                return false;
            }
        }
        return true;
    }
    
    //Check for commas and remove
    public String commaChecker(String s){
        
        String newLine = "";
        
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ',')
                newLine = newLine + s.charAt(i);
        }
        
        return newLine;
    }
    
}
