import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Class of Computer Memory
public class Memory {
    //Initializing Pages Of Memory and an array to access the location to access the pages
    PageFrame[] pages = new PageFrame[512];
    int[] location = new int[2];

    //Initialize Memory as a sequence of pages
    public Memory(){
        for(int i=0;i<pages.length;i++){
            pages[i]=new PageFrame();
        }
    }


    //Loads Hexvalue from memory Pages
    public String Loadhex(short index){
        if(index<0 || index > 65535 ) {
            System.out.println("Accessing Out of Bound Data");
            return "";
        }
        location=Convert(index);
        return pages[location[0]].hexmemory[location[1]];
    }

    //Loads unsigned byte value from memory Pages
    public Ubyte Loaddec(short index){
        if(index<0 || index > 65535 ) {
            System.out.println("Accessing Out of Bound Data");
            Ubyte a = new Ubyte((byte)0);
            a.valid=false;
            return a;
        }
        location=Convert(index);
        return pages[location[0]].memory[location[1]];
    }

    //Stores a value at a specific adress in both memory Pages
    public int Store(byte content, short adress){
        location=Convert(adress);
        if(adress<0 || adress > 65535 ) {
            System.out.println("Out of Bound Storage");
            return -1;
        }
        else{
            Ubyte value = new Ubyte(content);
            pages[location[0]].memory[location[1]] = value;
            String temp = Integer.toHexString(value.value);
            if (temp.length() == 1) temp = "0" + temp;
            pages[location[0]].hexmemory[location[1]] = temp;
            return 0;
        }
    }

    //Converting Instruction Offsets To Physical Addresses
    public int[] Convert(short index){
        int[] location = new int[2];
        location[0] = index/128;
        location[1] = index%128;
        return location;
    }

    //Printing and Writing Memory Dump To File
    public void dump( short start,short end,String type){
            try(FileWriter fw = new FileWriter("dump.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                out.println(type);
                for(short i=start;i<=end;i++){
                    System.out.println(Loaddec(i));
                    out.println(Loaddec(i));
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
    }

    public void dump2( short start,short end,String type,String Filename){ //to print and dump into the process_file_name file
        dumpLog(start,end,type,Filename);
        try(FileWriter fw = new FileWriter(Filename + ".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(type);
            for(short i=start;i<=end;i++){
                System.out.println(Loaddec(i));
                out.println(Loaddec(i));
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void dumpLog( short start,short end,String type,String Filename){//to print and dump into the log file
        try(FileWriter fw = new FileWriter("log.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(type);
            for(short i=start;i<=end;i++){
                System.out.println(Loaddec(i));
                out.println(Loaddec(i));
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
