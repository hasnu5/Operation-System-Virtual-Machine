import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static java.lang.Math.ceil;

//Class That Loads A Program From File To Memory And Creates Its PCB
public class ProgramLoader {

    //Loads A Program From File To Memory And Creates Its PCB
    public ProcessControlBlock Load (String filename, Memory memory, List freepages){
        int count = 0; // Initializing a counting variable

        //Initializing the pagetable,GeneralPurposeRegisters,SpecialPurposeRegisters,Stack,ByteArray and ProcessControlBlock for a Process
        PageTable pageTable = new PageTable();
        GeneralPurposeRegisters GPR= new GeneralPurposeRegisters();
        SpecialPurposeRegisters SPR= new SpecialPurposeRegisters();
        ProcessControlBlock process=new ProcessControlBlock();
        Stack stack = new Stack();
        int[] byteRead = new int[65536];

        //Reading the Process From File into a byte Array
        try {
            InputStream inputStream = new FileInputStream(filename);

            int i=1;
            while (inputStream.available() > 0) {
                byteRead[i] = inputStream.read();
                i++;
                count ++;
            }
            inputStream.close();
        } catch (IOException ex) {   //Catching File Exception
            System.out.println("Wrong FileName");
            return(null);
        }

        //Checking For Valid Priority
        if(byteRead[1]>=0 && byteRead[1]<=31) {
            process.Priority = byteRead[1];  //Assigning Priority To process
        }
        else return null; //Terminate if Invalid Priority

        //Combining two byte values to generate ProcessID and assigning it to the Process
        String temp = Integer.toHexString(byteRead[2]);
        if (temp.length()==1){   //Length Fixing
            temp="0"+temp;
        }
        String temp1 = Integer.toHexString(byteRead[3]);
        if (temp1.length()==1){  //Length Fixing
            temp1="0"+temp1;
        }
        String Immediate=temp+temp1;
        process.ID =  Integer.parseInt(Immediate,16); //Assigning Process ID To process


        //Assigning filename to the Process
        process.Filename=filename;


        //Combining two byte values to generate DataSize and assigning it to the Process
        temp = Integer.toHexString(byteRead[4]);
        if (temp.length()==1){  //Length Fixing
            temp="0"+temp;
        }
        temp1 = Integer.toHexString(byteRead[5]);
        if (temp1.length()==1){  //Length Fixing
            temp1="0"+temp1;
        }
        Immediate=temp+temp1;
        //Checking If Data Size Is Valid
        if(Integer.parseInt(Immediate,16)>=0 && Integer.parseInt(Immediate,16)<=65536 ){
              process.DataSize =  Integer.parseInt(Immediate,16); //Assigning Data Size To process
        }
        else{
            return null;    //Terminate if Invalid DataSize
        }

        process.ProcessSize= count;  //Assigning Process Size To process

        //Checking If Code Size Is Valid
        if((process.ProcessSize- process.DataSize-8)>=0 && (process.ProcessSize- process.DataSize-8<=65536) ){
              process.CodeSize= process.ProcessSize- process.DataSize-8; //Assigning Code Size To process
        }
        else{
            return null;  //Terminate if Invalid CodeSize
        }
        process.GPR=GPR;  //Assigning General Purpose Registers To process
        process.SPR=SPR;  //Assigning Special Purpose Registers To process
        process.stack=stack; //Assigning A Stack To process

        process.SPR.SPR[6].value=(short)0;  //Setting Value Of Stack Base Register
        process.SPR.SPR[7].value=(short)-1;  //Setting Value Of Stack Counter Register
        process.SPR.SPR[8].value=(short)999; //Setting Value Of Stack Limit Register

        process.pagetable=pageTable; //Assigning A Page Table To Process

        int counter = (int) ceil(process.ProcessSize/128.0);   //Calculating Number Of Pages Required By The Process

        //Removing the specific pages from FreeFrameList And Adding them to Process Page Table
        for(int i=0;i<counter;i++){
            process.pagetable.list.add((int)freepages.remove(0));
        }

        //Loading the required data from file to memory  pages assigned to the process
        DataLoader(byteRead,memory,process);

        //Loading the required code from file to memory  pages assigned to the process
        int status = CodeLoader(byteRead,memory,process);  //Checking for code size Validity
        if(status==-1) return null; //Terminate Process if code size invalid
        return process; //Return Process Control Block
    }


    //Loading the required data from file to memory  pages assigned to the process
    public void DataLoader(int[] array, Memory m, ProcessControlBlock PCB){
        int[] location = new int[2]; //array to get page and offset  numbers
        PCB.SPR.SPR[3].value=(short)((PCB.pagetable.list.get(0)*128)); //Setting Value Of DataBase Register
        for(int i=0;i<PCB.DataSize;i++){
            location[0] = i/128; //Finding Page Number
            location[1] = i%128; //Finding Page Offset
            m.Store((byte)array[9+i],(short) ((location[0]*128)+location[1]+(PCB.pagetable.list.get(0)*128))); //Storing Data in memory(Specific Page and Offset)
        }
        PCB.SPR.SPR[4].value=(short) PCB.DataSize; //Setting Value Of DataLimit Register
    }

    //Loading the required code from file to memory  pages assigned to the process
    public int CodeLoader(int[] array, Memory m, ProcessControlBlock PCB){
        int[] location = new int[2]; //array to get page and offset  numbers
        short start=(short)((location[0]*128)+location[1]+(PCB.pagetable.list.get(0)*128)+PCB.DataSize); //Calculating Starting Address
        PCB.SPR.SPR[0].value=start; //Setting Value Of CodeBase Register
        PCB.SPR.SPR[9].value=start; //Setting Value Of ProgramCounter Register
        int count=0; //to count iterations
        for(int i=0;i<PCB.CodeSize;i++){
            location[0] = i/128; //Finding Page Number
            location[1] = i%128; //Finding Page Offset
            if((9+PCB.DataSize+i)>array.length-1){     //Checking for Valid Code Size
                System.out.println("Invalid Code Size-Code Size Greater Than Code");
                return -1;  //Terminating Process If Invalid Code Size
            }
            m.Store((byte)array[9+PCB.DataSize+i],start); //Storing code in memory (Specific Page and Offset)
            start=(short)(start+1); //incrementing memory address
            count=i; //counting iterations
        }
        if((9+PCB.DataSize+count)>array.length-1){ //Checking for Valid Code Size
            System.out.println("Invalid Code Size-Code Size Smaller Than Code");
            return -1; //Terminating Process If Invalid Code Size
        }
        PCB.SPR.SPR[1].value=(short)PCB.CodeSize; //Setting Value for Code Limit Register
        return 0; //Returning Status
    }
}
