import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Stack {
    Ubyte[] stack = new Ubyte[1000]; //size of stack for each process
    int index = 0;

    public Stack(){ //constructor for Stack and to initialize it to 0
        for(int i=0;i<999;i++){
            stack[i]=new Ubyte((byte)0);
        }
    }

    public void push(short S){ //to add short value to stack by breaking it into two
        String hex = Integer.toHexString(S & 0xffff);
        if(hex.length()==3){ //Specifying the length of register value
            hex="0"+ hex;
        }
        else if(hex.length()==2){//Specifying the length of register value
            hex="00"+ hex;
        }
        else if(hex.length()==1){//Specifying the length of register value
            hex="000"+ hex;
        }


        // breaking the short type into two parts to store into Ubyte
        String part1 = hex.substring(0,2);
        String part2 = hex.substring(2,4);
        stack[index].value = Integer.parseInt(part1,16);
        index++;
        stack[index].value = Integer.parseInt(part2,16);
        index++;

    }

    public Ubyte pop(){ //removing the last input to stack
        index--;
        int index2 = index;
        return stack[index2];

    }

    public String toString(){
        return "1000";
    }

    //Printing and Writing Stack To File
    public void dump(){
        try(FileWriter fw = new FileWriter("dump.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("Stack Dump");
            for(short i=0;i<1000;i++){
                System.out.println(stack[i]);
                out.println(stack[i]);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void dump2(String Filename){
        dumpLog();
        try(FileWriter fw = new FileWriter(Filename +".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("Stack Dump");
            for(short i=0;i<1000;i++){
                System.out.println(stack[i]);
                out.println(stack[i]);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void dumpLog(){
        try(FileWriter fw = new FileWriter("log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("Stack Dump");
            for(short i=0;i<50;i++){
                System.out.println(stack[i]);
                out.println(stack[i]);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



}