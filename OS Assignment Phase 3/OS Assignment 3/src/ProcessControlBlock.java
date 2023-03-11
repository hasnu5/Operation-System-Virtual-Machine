import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
//Class that Defines the Process Control Block Of A Process
public class ProcessControlBlock {

    //Process Attributes of ID,Priority,CodeSize,DataSize,ProcessSize,Filename,General Purpose Registers, Special Purpose Registers, Stack, PageTable, ExecutionTime, Waiting Time and Log
    int ID;
    int Priority;
    int CodeSize;
    int DataSize;
    int ProcessSize;
    String Filename;
    GeneralPurposeRegisters GPR;
    SpecialPurposeRegisters SPR;
    Stack stack;
    PageTable pagetable;
    int ExecutionTime;
    int WaitingTime;
    String Log="Process Terminated Successfully";

    //Printing and Writing Process Control Block Contents to Dump File
    public void dump(){
        try(FileWriter fw = new FileWriter("dump.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            System.out.println("Process Control Block");
            out.println("Process Control Block");
            System.out.println("ID = " +ID);
            out.println("ID = " + ID);
            System.out.println("Priority = " +Priority);
            out.println("Priority = " +Priority);
            System.out.println("Code Size = " + CodeSize);
            out.println("Code Size = " + CodeSize);
            System.out.println("Data Size= " +DataSize);
            out.println("Data Size= " +DataSize);
            System.out.println("ProcessSize = " + ProcessSize);
            out.println("ProcessSize = " + ProcessSize);
            System.out.println("Filename = " +Filename);
            out.println("Filename = " +Filename);
            System.out.println("GeneralPurposeRegisters = " + GPR);
            out.println("GeneralPurposeRegisters = " + GPR);
            System.out.println("SpecialPurposeRegisters = " +SPR);
            out.println("SpecialPurposeRegisters = " +SPR);
            System.out.println("PageTable = " +pagetable);
            out.println("PageTable = " +pagetable);
            System.out.println("Execution Time= " + ExecutionTime);
            out.println("Execution Time= " + ExecutionTime);
            System.out.println("WaitingTime = " +WaitingTime);
            out.println("WaitingTime = " +WaitingTime);
            System.out.println("Log = " +Log);
            out.println("Log = " +Log);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void dump2(){
        try(FileWriter fw = new FileWriter(this.Filename +".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            System.out.println("Process Control Block");
            out.println("Process Control Block");
            System.out.println("ID = " +ID);
            out.println("ID = " + ID);
            System.out.println("Priority = " +Priority);
            out.println("Priority = " +Priority);
            System.out.println("Code Size = " + CodeSize);
            out.println("Code Size = " + CodeSize);
            System.out.println("Data Size= " +DataSize);
            out.println("Data Size= " +DataSize);
            System.out.println("ProcessSize = " + ProcessSize);
            out.println("ProcessSize = " + ProcessSize);
            System.out.println("Filename = " +Filename);
            out.println("Filename = " +Filename);
            System.out.println("GeneralPurposeRegisters = " + GPR);
            out.println("GeneralPurposeRegisters = " + GPR);
            System.out.println("SpecialPurposeRegisters = " +SPR);
            out.println("SpecialPurposeRegisters = " +SPR);
            System.out.println("PageTable = " +pagetable);
            out.println("PageTable = " +pagetable);
            System.out.println("Execution Time= " + ExecutionTime);
            out.println("Execution Time= " + ExecutionTime);
            System.out.println("WaitingTime = " +WaitingTime);
            out.println("WaitingTime = " +WaitingTime);
            System.out.println("Log = " +Log);
            out.println("Log = " +Log);

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public static Comparator<ProcessControlBlock> PPriority = new Comparator<ProcessControlBlock>() {

        public int compare(ProcessControlBlock p1, ProcessControlBlock p2) {

            int prior1 = p1.Priority;
            int prior2 = p2.Priority;

            return prior1-prior2;

        }
    };

    public String toString(){ //to print all the details of the PCB
        String s = "";
        s += "Process Control Block" + "\n";
        s += "ID = " + ID + "\n";
        s += "Priority = " + Priority+ "\n";
        s += "Code Size = " + CodeSize+ "\n";
        s += "Data Size= " + DataSize+ "\n";
        s += "ProcessSize = " + ProcessSize+ "\n";
        s += "Filename = " + Filename + "\n";
        s += "GeneralPurposeRegisters = " + GPR +"\n";
        s += "SpecialPurposeRegisters = " +SPR +"\n";
        s += "PageTable = " +pagetable + "\n";
        s += "Execution Time= " + ExecutionTime +"\n";
        s += "WaitingTime = " +WaitingTime+"\n";
        s += "Log = " + Log +"\n";

        return s;
    }  //Process Control Block to String method


}
