
//Necessary Imports
import jdk.swing.interop.SwingInterOpUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CLICommands { //Class For Executing CLI Commands

    //Initializing ProgramLoader,ProcessControlBlock,FreeFrameList,ReadyQueues,PCB Queues,Blocked Queue,Temporary Queue,Memory and instructions,
    List<Integer> FreeFrameList = new ArrayList<Integer>();
    ProgramLoader programLoader = new ProgramLoader();
    ProcessControlBlock process;

    List<ProcessControlBlock> Queue1 = new ArrayList<ProcessControlBlock>();
    List<ProcessControlBlock> Queue2 = new ArrayList<ProcessControlBlock>();
    List<ProcessControlBlock> blocked = new ArrayList<ProcessControlBlock>();
    List<ProcessControlBlock> list = new ArrayList<ProcessControlBlock>();
    List<ProcessControlBlock> temp = new ArrayList<ProcessControlBlock>();

    Memory memory = new Memory();
    Instructions instructions = new Instructions();

    RoundRobin rr = new RoundRobin(Queue2); //class for Scheduling Criteria for Queue 2
    FCFS fc = new FCFS(Queue1); //class for Scheduling Criteria for Queue 1


    boolean found = false;  //Used for searching

    public CLICommands() { //Constructor
        //Add all memory pages to free page list
        for (int i = 0; i < 512; i++) {
            FreeFrameList.add(i);
        }
    }

    public void execute(String[] Command) {

//        Length Check For Command Validity
        if (Command.length < 2 && !(Command[0].equals("Shutdown") || Command[0].equals("Help") || Command[0].equals("mem") || Command[0].equals("registers") )) {
            System.out.println("Invalid Command");
            return;
        }

        //Switch Case For Commands
        switch (Command[0]) {
            case "load":  //Loading File Into Memory

                //Length Check For Command Validity
                if (Command.length != 2) {
                    System.out.println("Invalid Command");
                    return;
                }

                //Loading Process from files into Memory
                process = programLoader.Load(Command[1], memory, FreeFrameList);
                if (process == null) {
                    System.out.println(Command[1] + " Is An Invalid Process");  //Invalid Process Error
                    return;
                } else { //Assigning Processes To Queues Based On Priority
                    if (process.Priority <= 15) {
                        Queue1.add(process);
                    } else {
                        Queue2.add(process);
                    }
                }
                //Adding Process To PCB List
                list.add(process);

                //Displaying Process ID and Dumping It To Log File
                System.out.println(process.ID);
                DumpOut(Integer.toString(process.ID));

                //Sorting Priority Queue
                Collections.sort(Queue1, ProcessControlBlock.PPriority);

                //Returning To Main Class
                return;

            case "run":

                if (Command[1].equals("-p")) { //Running A Single Process Completely

                    //Length Check For Command Validity
                    if (Command.length != 3) {
                        System.out.println("Invalid Command");
                        return;
                    }
                    //Finding Process In PCB List
                    found = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (Command[2].equals(String.valueOf(list.get(i).ID))) {
                            process = list.get(i);
                            found = true;
                            break;
                        }
                    }
                    //Error Message If PCB Not Found
                    if (found == false) {
                        System.out.println("Wrong Process ID-This process not yet loaded");
                        return;
                    }

                    //Extracting Opcode from memory and converting it to hexadecimal
                    String Opcode = memory.Loadhex(process.SPR.SPR[9].value);

                    //Setting Values For Program Counter,Code Base And Code Limit
                    short PC = process.SPR.SPR[9].value;
                    short CL = process.SPR.SPR[1].value;
                    short CB = process.SPR.SPR[0].value;

                    while ((PC <= (CL + CB - 1)) & (!(Opcode.equals("f3")))) {
                        PC = instructions.DecodeExecute(Opcode, memory, process.GPR, process.SPR, PC, process.stack, process); //Executing the instruction
                        if (PC == -1) break; //Unsuccessful Termination
                        if (PC != 0) PC++;   //icriment PC
                        process.SPR.SPR[9].value = PC; //Setting the value of the PC register
                        process.SPR.SPR[2].value = PC; //Setting the value of the Code Counter Register
                        System.out.println(process.GPR); //Printing the General Purpose Registers
                        System.out.println(process.SPR); //Printing the Special Purpose Registers

                        Opcode = memory.Loadhex(PC); //Loading new instruction from memory and converting it to hexadecimal
                        if (Opcode == null) break;      //Breaking Execution if no opcode available
                    }
                    //Displaying Error if Program Counter Becomes Greater Than Code Limit
                    if (PC >= (CL + CB)) {
                        System.out.println("Accessing out of bound instructions");
                    } else if (Opcode.equals("f3")) {
                        //Dumping Stack,Process Control Block,Code and Data on Output and In File
                        System.out.println("Program executed successfully");
                        System.out.println("Data Dump");
                        memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                        System.out.println("Stack Dump");
                        process.stack.dump();
                        System.out.println("Code Dump");
                        memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                        process.dump();
                        //Return Process Pages To Free Page List
                        int size = process.pagetable.list.size();
                        for (int i = 0; i < size; i++) {
                            FreeFrameList.add(process.pagetable.list.remove(0));

                        }
                    }
                    //Removing Process from Queue On Unsuccessful termination
                    else if (PC == -1) {
                        for (int i = 0; i < Queue1.size(); i++) {
                            if (process.ID == Queue1.get(i).ID) Queue1.remove(i);
                        }
                        for (int i = 0; i < Queue2.size(); i++) {
                            if (process.ID == Queue2.get(i).ID) Queue2.remove(i);
                        }
                        //Dumping Stack,Process Control Block,Code and Data on Output and In File
                        System.out.println(process.Log);
                        System.out.println("Data Dump");
                        memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                        System.out.println("Stack Dump");
                        process.stack.dump();
                        System.out.println("Code Dump");
                        memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                        process.dump();
                        //Return Process Pages To Free Page List
                        int size = process.pagetable.list.size();
                        for (int i = 0; i < size; i++) {
                            FreeFrameList.add(process.pagetable.list.remove(0));

                        }
                    }
                    //Removing Process From Queues On Termination
                    if (Queue1.contains(process)) Queue1.remove(process);
                    else if (Queue2.contains(process)) Queue2.remove(process);
                    list.remove(process);
                    return; //Return
                } else if (Command[1].equals("-a")) {  //Running All Processes Completely
                    //Length Check For Command Validity
                    if (Command.length != 2) {
                        System.out.println("Invalid Command");
                        return;
                    }

                    while (!Queue2.isEmpty() || !Queue1.isEmpty()) { //checking whether both Queues are empty or not

                        boolean RRorPR; //flag to check process from which queue is running

                        int process_time = 0; //as soon as this value goes above 8 in Round Robin Scheduling the scheduling stops

                        if (Queue1.isEmpty()) { // if Queue1 is empty, the processes from Queue2 are loaded one by one
                            process = Queue2.get(0);
                            RRorPR = true;

                        } else {
                            process = Queue1.get(0); //Loading A Process from Ready Queue
                            RRorPR = false;
                        }


                        //Extracting Opcode from memory and converting it to hexadecimal
                        String Opcode = memory.Loadhex(process.SPR.SPR[9].value);

                        //Setting Values For Program Counter,Code Base And Code Limit
                        short PC = process.SPR.SPR[9].value;
                        short CL = process.SPR.SPR[1].value;
                        short CB = process.SPR.SPR[0].value;

                        boolean proc_stop = false; //used to stop the while loop when any process exceeds the limit (>=8)

                        //Executing Program until end instruction or Program Counter becomes greater than Code Base + Code Limit
                        while ((PC <= (CL + CB - 1)) & (!(Opcode.equals("f3"))) & (!proc_stop)) {
                            PC = instructions.DecodeExecute(Opcode, memory, process.GPR, process.SPR, PC, process.stack, process); //Executing the instruction
                            if (PC == -1) break; //Unsuccessful Termination
                            if (PC != 0) PC++;   //icriment PC
                            process.SPR.SPR[9].value = PC; //Setting the value of the PC register
                            process.SPR.SPR[2].value = PC; //Setting the value of the Code Counter Register
                            System.out.println(process.GPR); //Printing the General Purpose Registers
                            System.out.println(process.SPR); //Printing the Special Purpose Registers

                            if (RRorPR) { //if the process is from queue2 which is RoundRobin
                                process_time += 2; //each iteration in while loops adds 2 unit time
                                Queue2 = rr.incrementExecutionTime(); //incrementing execution time for the process currently running
                                Queue2 = rr.incrementWaitingTime();    //incrementing waiting time for the processes currently waiting in queue

                                if (process_time > 8) { //if the process time exceeds it limit assigned to it
                                    Queue2 = rr.reArrange(); //rearrange the queue
                                    proc_stop = true; //stop the loop as next process will be loaded, however the opcode will be loaded so its execution time will be added next time this process gets it turn
                                    break;
                                }

                            } else {
                                Queue2 = rr.incrementWaitingTimeAll(); //incrementing waiting time of all processes in Queue1
                                Queue1 = fc.incrementExecutionTime();//incrementing execution time for the process currently running
                                Queue1 = fc.incrementWaitingTime();//incrementing waiting time for the processes currently waiting in queue
                            }

                            if (!proc_stop) {
                                Opcode = memory.Loadhex(PC); //Loading new instruction from memory and converting it to hexadecimal
                            }
                            if (Opcode == null) break;      //Breaking Execution if no opcode available

                        }

                        //Displaying Error if Program Counter Becomes Greater Than Code Limit
                        if (PC >= (CL + CB)) {
                            System.out.println("Accessing out of bound instructions");
                        } else if (Opcode.equals("f3")) {
                            //Dumping Stack,Process Control Block,Code and Data on Output and In File
                            System.out.println("Program executed successfully");
                            System.out.println("Data Dump");
                            memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                            System.out.println("Stack Dump");
                            process.stack.dump();
                            System.out.println("Code Dump");
                            memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                            process.dump();
                            //Return Process Pages To Free Page List
                            int size = process.pagetable.list.size();
                            for (int i = 0; i < size; i++) {
                                FreeFrameList.add(process.pagetable.list.remove(0));

                            }

                            if (RRorPR) {
                                if (process_time <= 8) { //if any process which is finished before its quantum is finished the Queue will again be rearranged and the particular process will be removed then
                                    Queue2 = rr.reArrange();
                                    Queue2 = rr.removelast();
                                    list.remove(process);
                                }
                            } else {
                                Queue1 = fc.remove(); // Removing a successfully executed process from ready Queue
                                list.remove(process);
                            }

                        }
                        //Removing Process from Queue On Unsuccessful termination
                        else if (PC == -1) {
                            for (int i = 0; i < Queue1.size(); i++) {
                                if (process.ID == Queue1.get(i).ID) Queue1.remove(i);
                            }
                            for (int i = 0; i < Queue2.size(); i++) {
                                if (process.ID == Queue2.get(i).ID) Queue2.remove(i);
                            }
                            //Dumping Stack,Process Control Block,Code and Data on Output and In File
                            System.out.println(process.Log);
                            System.out.println("Data Dump");
                            memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                            System.out.println("Stack Dump");
                            process.stack.dump();
                            System.out.println("Code Dump");
                            memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                            process.dump();
                            //Return Process Pages To Free Page List
                            int size = process.pagetable.list.size();
                            for (int i = 0; i < size; i++) {
                                FreeFrameList.add(process.pagetable.list.remove(0));

                            }
                        }

                    }
                    return; //Return
                }
                //Error Message For Invalid Command
                else {
                    System.out.println("Invalid Command");
                    return;
                }
            case "debug":

                if (Command[1].equals("-p")) { //Running Single Instruction Of Single Process
                    //Length Check For Command Validity
                    if (Command.length != 3) {
                        System.out.println("Invalid Command");
                        return;
                    }

                    //Searching For Process In PCB List
                    found = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (Command[2].equals(String.valueOf(list.get(i).ID))) {
                            process = list.get(i);
                            found = true;
                            break;
                        }
                    }
                    //Displaying Error Message If PCB Not Found
                    if (found == false) {
                        System.out.println("Wrong Process ID-This process not yet loaded");
                        return;
                    }

                    //Extracting Opcode from memory and converting it to hexadecimal
                    String Opcode = memory.Loadhex(process.SPR.SPR[9].value);
                    if (Opcode == null) {
                        System.out.println("No Opcode");       //Breaking Execution if no opcode available
                        return;
                    }
                    //Setting Values For Program Counter,Code Base And Code Limit
                    short PC = process.SPR.SPR[9].value;
                    short CL = process.SPR.SPR[1].value;
                    short CB = process.SPR.SPR[0].value;

                    //Checkíng If PC within range
                    if (PC >= (CL + CB)) {
                        //Displaying Error Message If PC Not In Range
                        System.out.println("Accessing out of bound instructions");
                        //Removing Process From Queues
                        if (Queue1.contains(process)) Queue1.remove(process);
                        else if (Queue2.contains(process)) Queue2.remove(process);
                        list.remove(process);
                        return; //Return
                    }

                    PC = instructions.DecodeExecute(Opcode, memory, process.GPR, process.SPR, PC, process.stack, process); //Executing the instruction
                    if (PC != 0) PC++;   //icriment PC
                    if (PC != -1) {
                        process.SPR.SPR[9].value = PC; //Setting the value of the PC register
                        process.SPR.SPR[2].value = PC; //Setting the value of the Code Counter Register
                        System.out.println(process.GPR); //Printing the General Purpose Registers
                        System.out.println(process.SPR); //Printing the Special Purpose Registers
                    }


                    if (Opcode.equals("f3")) {
                        //Dumping Stack,Process Control Block,Code and Data on Output and In File
                        System.out.println("Program executed successfully");
                        System.out.println("Data Dump");
                        memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                        System.out.println("Stack Dump");
                        process.stack.dump();
                        System.out.println("Code Dump");
                        memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                        process.dump();
                        //Return Process Pages To Free Page List
                        int size = process.pagetable.list.size();
                        for (int i = 0; i < size; i++) {
                            FreeFrameList.add(process.pagetable.list.remove(0));

                        }
                        //Removing Process From Queues
                        if (Queue1.contains(process)) Queue1.remove(process);
                        else if (Queue2.contains(process)) Queue2.remove(process);
                        list.remove(process);
                        return; //return
                    }
                    //Removing Process from Queue On Unsuccessful termination
                    else if (PC == -1) {
                        for (int i = 0; i < Queue1.size(); i++) {
                            if (process.ID == Queue1.get(i).ID) Queue1.remove(i);
                        }
                        for (int i = 0; i < Queue2.size(); i++) {
                            if (process.ID == Queue2.get(i).ID) Queue2.remove(i);
                        }
                        //Dumping Stack,Process Control Block,Code and Data on Output and In File
                        System.out.println(process.Log);
                        System.out.println("Data Dump");
                        memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                        System.out.println("Stack Dump");
                        process.stack.dump();
                        System.out.println("Code Dump");
                        memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                        process.dump();
                        //Return Process Pages To Free Page List
                        int size = process.pagetable.list.size();
                        for (int i = 0; i < size; i++) {
                            FreeFrameList.add(process.pagetable.list.remove(0));

                        }
                        //Removing Process From Queues
                        if (Queue1.contains(process)) Queue1.remove(process);
                        else if (Queue2.contains(process)) Queue2.remove(process);
                        list.remove(process);
                        return; //Return
                    }
                    return; //Return
                } else if (Command[1].equals("-a")) { //Running Single Instruction Of All Processes
                    //Length Check For Command Validity
                    if (Command.length != 2) {
                        System.out.println("Invalid Command");
                        return;
                    }
                    //Temporary Queue For Counter
                    temp = (ArrayList) (((ArrayList) list).clone());
                    int z = -1; //Count Variable
                    int l = list.size(); //Getting List Size For Loop

                    //Loop To Execute Single Instruction Of Each Process
                    while (z < l - 1) {
                        z = z + 1;   //incriment counter
                        process = temp.get(z); //extract new process from list

                        //Extracting Opcode from memory and converting it to hexadecimal
                        String Opcode = memory.Loadhex(process.SPR.SPR[9].value);
                        if (Opcode == null) {
                            System.out.println("No Opcode");       //Breaking Execution if no opcode available
                            if (Queue1.contains(process)) Queue1.remove(process);
                            else if (Queue2.contains(process)) Queue2.remove(process);
                            list.remove(process);
                            continue;
                        }
                        //Setting Values For Program Counter,Code Base And Code Limit
                        short PC = process.SPR.SPR[9].value;
                        short CL = process.SPR.SPR[1].value;
                        short CB = process.SPR.SPR[0].value;

                        //Check If PC within range
                        if (PC >= (CL + CB)) {
                            //Display Error Message If PC not in range
                            System.out.println("Accessing out of bound instructions");
                            //Remove Process From Queues
                            if (Queue1.contains(process)) Queue1.remove(process);
                            else if (Queue2.contains(process)) Queue2.remove(process);
                            list.remove(process);
                            continue; //Move To Next Instruction
                        }

                        PC = instructions.DecodeExecute(Opcode, memory, process.GPR, process.SPR, PC, process.stack, process); //Executing the instruction
                        if (PC != 0) PC++;   //icriment PC
                        if (PC != -1) {
                            process.SPR.SPR[9].value = PC; //Setting the value of the PC register
                            process.SPR.SPR[2].value = PC; //Setting the value of the Code Counter Register
                            System.out.println(process.GPR); //Printing the General Purpose Registers
                            System.out.println(process.SPR); //Printing the Special Purpose Registers
                        }


                        if (Opcode.equals("f3")) {
                            //Dumping Stack,Process Control Block,Code and Data on Output and In File
                            System.out.println("Program executed successfully");
                            System.out.println("Data Dump");
                            memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                            System.out.println("Stack Dump");
                            process.stack.dump();
                            System.out.println("Code Dump");
                            memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                            process.dump();
                            //Return Process Pages To Free Page List
                            int size = process.pagetable.list.size();
                            for (int i = 0; i < size; i++) {
                                FreeFrameList.add(process.pagetable.list.remove(0));

                            }
                            //Removing Process from Queues
                            if (Queue1.contains(process)) Queue1.remove(process);
                            else if (Queue2.contains(process)) Queue2.remove(process);
                            list.remove(process);
                            continue; //Move On To Next Process
                        }
                        //Removing Process from Queue On Unsuccessful termination
                        else if (PC == -1) {
                            for (int i = 0; i < Queue1.size(); i++) {
                                if (process.ID == Queue1.get(i).ID) Queue1.remove(i);
                            }
                            for (int i = 0; i < Queue2.size(); i++) {
                                if (process.ID == Queue2.get(i).ID) Queue2.remove(i);
                            }
                            //Dumping Stack,Process Control Block,Code and Data on Output and In File
                            System.out.println(process.Log);
                            System.out.println("Data Dump");
                            memory.dump((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump");
                            System.out.println("Stack Dump");
                            process.stack.dump();
                            System.out.println("Code Dump");
                            memory.dump((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump");
                            process.dump();
                            //Return Process Pages To Free Page List
                            int size = process.pagetable.list.size();
                            for (int i = 0; i < size; i++) {
                                FreeFrameList.add(process.pagetable.list.remove(0));

                            }
                            //Remove Process From Queues
                            if (Queue1.contains(process)) Queue1.remove(process);
                            else if (Queue2.contains(process)) Queue2.remove(process);
                            list.remove(process);
                            continue; //Move On To Next Process
                        }

                    }
                    return; //Return
                }
                //Check For Invalid Command
                else {
                    System.out.println("Invalid Command");
                    return;
                }
            case "kill": //Killing A Process
                //Length Check For Command Validity
                if (Command.length != 3) {
                    System.out.println("Invalid Command");
                    return;
                }

                //Searching Process In PCB List
                found = false;
                for (int i = 0; i < list.size(); i++) {
                    if (Command[2].equals(String.valueOf(list.get(i).ID))) {
                        process = list.get(i);
                        found = true;
                        break;
                    }
                }
                //Displaying Error if Process Not Found
                if (found == false) {
                    System.out.println("Wrong Process ID-This process not yet loaded");
                    return; //Return
                }
                //Removing Process From Queues
                if (Queue1.contains(process)) Queue1.remove(process);
                else if (Queue2.contains(process)) Queue2.remove(process);
                list.remove(process);
                return;  //return

            case "clone": //Cloning A Process
                //Length Check For Command Validity
                if (Command.length != 2) {
                    System.out.println("Invalid Command");
                    return;
                }
                //Seraching for Process In PCB List
                found = false;
                for (int i = 0; i < list.size(); i++) {
                    if (Command[1].equals(String.valueOf(list.get(i).ID))) {
                        process = list.get(i);
                        found = true;
                        break;
                    }
                }
                //Displaying Error If Process Not Found
                if (found == false) {
                    System.out.println("Wrong Process ID-This process not yet loaded");
                    return; //Return
                }
                //Load The Process Again In Memory
                process = programLoader.Load(process.Filename, memory, FreeFrameList);
                //Changing ProcessID of new Process
                process.ID = process.ID + 1;
                //Assigning Process To Queue Based On Priority
                if (process.Priority <= 15) {
                    Queue1.add(process);
                } else {
                    Queue2.add(process);
                }
                //Add Process To PCB List
                list.add(process);
                //Sort Ready Queue
                Collections.sort(Queue1, ProcessControlBlock.PPriority);
                return;

            case "block": //Send A Process To Blocked Queue
                //Length Check For Command Valdity
                if (Command.length != 2) {
                    System.out.println("Invalid Command");
                    return;
                }
                //Search For Process In PCB List
                found = false;
                for (int i = 0; i < list.size(); i++) {
                    if (Command[1].equals(String.valueOf(list.get(i).ID))) {
                        process = list.get(i);
                        found = true;
                        break;
                    }
                }
                //Display Error Message If Process Not Found
                if (found == false) {
                    System.out.println("Wrong Process ID-This process not yet loaded");
                    return; //Return
                }
                //Remove Process From Other Queues
                if (Queue1.contains(process)) Queue1.remove(process);
                else if (Queue2.contains(process)) Queue2.remove(process);
                list.remove(process);
                //Add Process To Blocked Queue
                blocked.add(process);
                return; //Return

            case "unblock": //Remove A Process From Blocked Queue And Add it to ready queues
                //Length Check For Command Validity
                if (Command.length != 2) {
                    System.out.println("Invalid Command");
                    return;
                }
                //Search For process in PCB List
                found = false;
                for (int i = 0; i < blocked.size(); i++) {

                    if (blocked.get(i).ID == Integer.parseInt(Command[1])) {
                        //Load the Process In Memory If Found
                        process = programLoader.Load(blocked.get(i).Filename, memory, FreeFrameList);
                        //Add Process To Ready Queue Based On Priority
                        if (process.Priority <= 15) {
                            Queue1.add(process);
                        } else {
                            Queue2.add(process);
                        }
                        found = true;
                        //Add Process To Ready Queue
                        list.add(blocked.get(i));
                        //Remove Process From Blockéd Queue
                        blocked.remove(i);
                        break; //Break Loop if PCB Found
                    }
                }
                //Display Error Message if PCB Not Found
                if (found == false) {
                    System.out.println("Wrong Process ID-This process not Blocked");
                }
                return; //Return

            case "Shutdown":  //Shutdown the Operating System
                //Length Check For Command Validity
                if (Command.length != 1) {
                    System.out.println("Invalid Command");
                    return;
                }
                //Temp list for navigation
                temp = (ArrayList) (((ArrayList) list).clone());
                //Killing All Processes one by one
                for (int i = 0; i < temp.size(); i++) {
                    process = temp.get(i);  //get a process from list
                    //Remove Process From Queues
                    if (Queue1.contains(process)) Queue1.remove(process);
                    else if (Queue2.contains(process)) Queue2.remove(process);
                    //Printing Process ID and Dumping It to log file
                    System.out.println(process.ID);
                    DumpOut(Integer.toString(process.ID));
                    //Remove Process From List
                    list.remove(process);
                }
                return; //Return
            case "Help": //A help Menu for checkíng Valid Commands
                //Printing All Valid Commands With their Purpose
                System.out.println("load " + "\033[3mfile\033[0m" + ": Loads the process into memory. Displays the process ID of the loaded process");
                System.out.println("run -p " + "\033[3mprocess_id\033[0m" + ": Completely  execute only the selected process");
                System.out.println("debug -p " + "\033[3mprocess_id\033[0m" + ": Execute only one instruction of the selected process");
                System.out.println("debug -a " + ": Execute only one instruction of all the loaded processes");
                System.out.println("run -a " + ": Completely  execute all the loaded processes ");
                System.out.println("kill -p " + "\033[3mprocess_id\033[0m" + ": Forcefully kill a process ");
                System.out.println("clone " + "\033[3mprocess_id\033[0m" + ": Create a clone of selected process");
                System.out.println("block " + "\033[3mprocess_id\033[0m" + ": Block a process (move it to blocked queue)");
                System.out.println("unblock " + "\033[3mprocess_id\033[0m" + ": Unblock a process (move it from blocked queue to ready queue)");
                System.out.println("Shutdown " + ": Shuts down the operating system, during shutdown process all processes should be killed and their process IDs displayed");
                System.out.println("list -a" + ": Displays all the processes(Process ID) currently loaded");
                System.out.println("list -b" + ": Displays blocked processes(Process ID) currently loaded");
                System.out.println("list -r" + ": Displays ready processes(Process ID) currently loaded");
                System.out.println("list -e" + ": Displays running processes(Process ID) currently loaded");
                System.out.println("display -p " + "\033[3mprocess_id\033[0m" + ": Displays the PCB of of the selected process");
                System.out.println("display -m " + "\033[3mprocess_id\033[0m" + ": Displays the Page Table of of the selected process");
                System.out.println("frames -f" + ": Displays the frame number and location of the free frames");
                System.out.println("frames -a" + ": Displays the Process ID and frame number of allocated frames");
                System.out.println("dump " + "\033[3mprocess_id\033[0m" + ": Displays the memory dump of selected process also writes it into process' filename");
                System.out.println("mem " + ": Displays the memory details of processes");
                System.out.println("registers" + ": Displays the current content of the CPU registers like GPR, SPR and Flag");

                return; //Return

            case "list":
                //Length Check For Command Validity
                if (Command.length != 2) {
                    System.out.println(Command.length);
                    System.out.println("Invalid Command");
                    return;
                }
                if (Command[1].equals("-a")) {  //prints all the processes currently loaded also outputs it into the Log file
                    for (int i = 0; i < list.size(); i++) {
                        process = list.get(i);  //get a process from list
                        //Printing Process ID
                        System.out.println(process.ID);
                        DumpOut(String.valueOf(process.ID));

                    }
                    return;
                } else if (Command[1].equals("-b")) {   //prints all the blocked processes also outputs it into the Log file
                    System.out.println("Blocked Processes");
                    DumpOut("Blocked Processes");
                    for (int i = 0; i < blocked.size(); i++) {
                        process = blocked.get(i);  //get a process from list
                        //Printing Process ID
                        System.out.println(process.ID);
                        DumpOut(String.valueOf(process.ID));
                    }
                    return;
                } else if (Command[1].equals("-r")) {//prints all the ready state processes also outputs it into the Log file
                    System.out.println("Ready Processes");
                    DumpOut("Ready Processes");

                    if(Queue1.isEmpty()) {// if the Queue1 is empty so only outputs Queue2 ready processes excluding running process at index 0 of Queue2
                        temp = Queue2;
                        for (int i = 0; i < temp.size(); i++) {
                            if (i != 0) {
                                process = temp.get(i);  //get a process from list
                                //Printing Process ID
                                System.out.println(process.ID);
                                DumpOut(Integer.toString(process.ID));
                            }
                        }
                    }
                    if(!Queue1.isEmpty())// if the Queue is not empty so outputs ready processes of both processes excluding running process at index 0 of Queue1
                    {
                        temp = Queue1;
                        for (int i = 0; i < temp.size(); i++) {
                            if(i != 0) {
                                process = temp.get(i);  //get a process from list
                                //Printing Process ID
                                System.out.println(process.ID);
                                DumpOut(Integer.toString(process.ID));
                            }
                        }
                        temp = Queue2;
                        for (int i = 0; i < temp.size(); i++) {
                            process = temp.get(i);  //get a process from list
                            //Printing Process ID
                            System.out.println(process.ID);
                            DumpOut(Integer.toString(process.ID));
                        }
                        return;
                    }
                }else if (Command[1].equals("-e")) { //Prints and outputs running process on Log file
                    System.out.println("Running Process");
                    DumpOut("Running Process");
                    if (Queue1.isEmpty()) {//if Queue1 is empty the process at index 0 is printed of Queue2 (running process)
                        System.out.println(Queue2.get(0).ID);
                        DumpOut(Integer.toString(Queue2.get(0).ID));
                    } else {//if Queue1 is not empty the process at index 0 is printed of Queue1 (running process)
                        System.out.println(Queue1.get(0).ID);
                        DumpOut(Integer.toString(Queue2.get(0).ID));
                    }
                    return; //Return
                }
            case "display":
                //Length Check For Command Validity
                if (Command.length != 3) {
                    System.out.println("Invalid Command");
                    return;
                }
                if (Command[1].equals("-p")) { //displays and outputs in Log File the PCB of the process given
                    //Searching Process In PCB List
                    found = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (Command[2].equals(String.valueOf(list.get(i).ID))) {
                            process = list.get(i);
                            System.out.println(process);
                            DumpOut(process.toString());
                            found = true;
                            break;
                        }
                    }
                    if (found == false) {
                        System.out.println("Wrong Process ID-This process not yet loaded");
                        return; //Return
                    }
                    return;
                    //Displaying Error if Process Not Found

                } else if(Command[1].equals("-m")) {//displays and outputs in Log File the PCB pagelist of the process given
                    //Searching Process In PCB List
                    found = false;
                    for (int i = 0; i < list.size(); i++) {
                        if (Command[2].equals(String.valueOf(list.get(i).ID))) {
                            process = list.get(i);
                            System.out.println(process.pagetable);
                            DumpOut(process.pagetable.toString());
                            found = true;
                            break;
                        }
                    }
                    //Displaying Error if Process Not Found
                    if (found == false) {
                        System.out.println("Wrong Process ID-This process not yet loaded");
                        return; //Return
                    }
                }
                return;
            case "dump": //shows the memory dump of the given Process also outputs it into Log file
                //Length Check For Command Validity
                if (Command.length != 2) {
                    System.out.println("Invalid Command");
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    //since in this command we had to dump memory into a "process filename",
                    // since it is also a display command we also have to dump into a Log file we had to make a new method into memory and stack class
                    if (Command[1].equals(String.valueOf(list.get(i).ID))) {
                        process = list.get(i);
                        System.out.println("Data Dump");
                        memory.dump2((short) process.SPR.SPR[3].value, (short) process.SPR.SPR[4].value, "Data Dump",process.Filename);
                        System.out.println("Stack Dump");
                        process.stack.dump2(process.Filename);
                        System.out.println("Code Dump");
                        memory.dump2((short) process.SPR.SPR[0].value, (short) process.SPR.SPR[1].value, "Code Dump", process.Filename);
                        break;
                    }
                }
                return;
            case "frames":
                if (Command.length != 2) {
                    System.out.println("Invalid Command");
                    return;
                }

                if(Command[1].equals("-f"))//shows the free frames with the location of each frames, also writes into Log file
                {
                    for (int i = 0; i < FreeFrameList.size(); i++) {
                        System.out.println("Frame Number: " + FreeFrameList.get(i));
                        DumpOut("Frame Number: " + FreeFrameList.get(i));
                        System.out.println("Frame Location: " + (FreeFrameList.get(i)*128));// the relation between frame number and location is *128
                        DumpOut("Frame Location: " + (FreeFrameList.get(i)*128));

                    }
                }
                else if(Command[1].equals("-a"))//shows the allocated frames , also writes into Log file
                {
                    for (int i = 0; i < list.size(); i++) {
                        process = list.get(i);
                        System.out.println(process.ID);
                        DumpOut(Integer.toString(process.ID));
                        System.out.println(process.pagetable.list);
                        DumpOut(process.pagetable.list.toString());

                    }
                }
                return;
            case "mem": //displays the memory details of processes, also writes them into the log file
                if (Command.length != 1) {
                    System.out.println("Invalid Command");
                    return;
                }

                for (int i = 0; i < list.size(); i++) {
                    process = list.get(i);
                    System.out.println(process.ID);
                    DumpOut(Integer.toString(process.ID));
                    System.out.println("Code Size: " + process.CodeSize + " Data Size: " +process.DataSize + " Stack: " + process.stack + " Process Size: " + process.ProcessSize);
                    DumpOut("Code Size: " + process.CodeSize + " Data Size: " +process.DataSize + " Stack size: " + process.stack.toString() + " Process Size: " + process.ProcessSize);
                }
                return;
            case "registers"://displays the registers information also writes them into the log file
                if (Command.length != 1) {
                    System.out.println("Invalid Command");
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    process = list.get(i);
                    System.out.println(process.ID);
                    DumpOut(Integer.toString(process.ID));
                    System.out.println("GPR: " + process.GPR);
                    DumpOut("GPR: " + process.GPR);
                    System.out.println("SPR: " + process.SPR);
                    DumpOut("SPR: " + process.SPR);
                }
                return;
            default:
                //Displaying Invalid Command Error
                System.out.println("Invalid Command");
                return; //Return

        }

    }


        //Saves A string In Log File
        public void DumpOut(String a){
            //Create File Writer Object
            try (FileWriter fw = new FileWriter("log.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(a); //Write The String To File
            } catch (IOException e) { //Handle Exception Error
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }