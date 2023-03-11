import java.util.ArrayList;
import java.util.List;

//Class Containing All Executable Instructions
public class Instructions {
    //Function Responsible for Decoding And Executing Every Instruction
    short check=0;
    public short DecodeExecute(String Opcode, Memory memory,GeneralPurposeRegisters GPR, SpecialPurposeRegisters SPR, short PC, Stack stack,ProcessControlBlock process ){

        //Deciding Which Function To Use Based On Opcode
        switch(Opcode){
            case "16":
                PC++;       //incriment PC
                int R1=memory.Loaddec(PC).value;  //Load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                PC++;       //incriment PC
                int R2 = memory.Loaddec(PC).value;  //Load Adress Of Second Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                short R2value = GPR.GPR[R2].value;  //Load Value Of Second Register
                GPR.GPR[R1].value=R2value;   //Move it to first register
                //ReSet Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                return PC; //return value of pc
            case "17":
                PC++;   //incriment pc
                R1=memory.Loaddec(PC).value;  //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break;  //Error Check
                short R1value = GPR.GPR[R1].value;   //Load Value Of First Register
                PC++;   //incriment pc
                R2 = memory.Loaddec(PC).value; //Load Adress Of Second Register
                if(memory.Loaddec(PC).valid==false) break;  //Error Check
                R2value = GPR.GPR[R2].value;   //Load Value Of Second Register
                int sum = R1value + R2value;   //Add the values
                GPR.GPR[R1].value = (short) sum;  //Store the sum in first register
                //Set flags
                setZeroFlag((short)sum,SPR,sum);
                setSignFlag((short)sum,SPR,sum);
                setCarryFlag((short)sum,SPR,sum);
                setOverflowFlag((short)sum,SPR,sum);
                ResetCarryFlag(SPR);
                return PC;   //return value of pc
            case "18":
                PC++;  //incriment pc
                R1=memory.Loaddec(PC).value;  //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break;   //Error Check
                R1value = GPR.GPR[R1].value;  //Load Value Of First Register
                PC++;  //incriment pc
                R2 = memory.Loaddec(PC).value;  //Load Adress Of Second Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R2value = GPR.GPR[R2].value;   //Load Value Of Second Register
                int sub = R1value - R2value;  //Subtract the values
                GPR.GPR[R1].value = (short) sub; //Store the difference in first register
                //Set flags
                setZeroFlag((short)sub,SPR,sub);
                setSignFlag((short)sub,SPR,sub);
                setCarryFlag((short)sub,SPR,sub);
                setOverflowFlag((short)sub,SPR,sub);
                ResetCarryFlag(SPR);
                return PC;  //return value of pc
            case "19":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value;   //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value;    //load Value Of First Register
                PC++;   //incriment pc
                R2 = memory.Loaddec(PC).value; //Load Adress Of Second Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R2value = GPR.GPR[R2].value;   //Load Value Of Second Register
                int mul = R1value * R2value; //Multiply the values
                GPR.GPR[R1].value = (short) mul;  //Store the product in first register
                //Set flags
                setZeroFlag((short)mul,SPR,mul);
                setSignFlag((short)mul,SPR,mul);
                setCarryFlag((short)mul,SPR,mul);
                setOverflowFlag((short)mul,SPR,mul);
                ResetCarryFlag(SPR);
                return PC; //return value of pc

            case "1a":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break;  //Error Check
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++;  //incriment pc
                R2 = memory.Loaddec(PC).value; //Load Adress Of Second Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R2value = GPR.GPR[R2].value;  //Load Value Of Second Register
                if(R2value==0){
                    System.out.println("Division By Zero"); //Division By Zero Error
                    process.Log="Process terminated due to division By Zero";
                    return -1;
                }
                int div = R1value / R2value;  //Dvide first register value by second register value
                GPR.GPR[R1].value = (short) div;  //Store the Quotient in first register
                //Set flags
                setZeroFlag((short)div,SPR,div);
                setSignFlag((short)div,SPR,div);
                setCarryFlag((short)div,SPR,div);
                setOverflowFlag((short)div,SPR,div);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "1b":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value;  //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value;  //load Value Of First Register
                PC++;  //incriment pc
                R2 = memory.Loaddec(PC).value; //Load Adress Of Second Register
                R2value = GPR.GPR[R2].value;//Load Value Of Second Register
                if(memory.Loaddec(PC).valid==false) break;  //Error Check
                int a = R1value & R2value;  //ANDing the values
                GPR.GPR[R1].value = (short) a; //Store the result in first register
                //Set flags
                setZeroFlag((short)a,SPR,a);
                setSignFlag((short)a,SPR,a);
                setCarryFlag((short)a,SPR,a);
                setOverflowFlag((short)a,SPR,a);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "1c":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value;  //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++; //incriment pc
                R2 = memory.Loaddec(PC).value; //Load Adress Of Second Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R2value = GPR.GPR[R2].value;  //Load Value Of Second Register
                int o = R1value | R2value; //ORing the values
                GPR.GPR[R1].value = (short) o;  //Store the result in first register
                //Set flags
                setZeroFlag((short)o,SPR,o);
                setSignFlag((short)o,SPR,o);
                setCarryFlag((short)o,SPR,o);
                setOverflowFlag((short)o,SPR,o);
                ResetCarryFlag(SPR);
                return PC; //return value of pc

            case "30":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                PC++; //incriment pc
                String Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incriment pc
                String Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                String Immediate=Imm1+Imm2; //Combine both values
                short val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                GPR.GPR[R1].value = val;  ////Store the result in first register
                //Reset the flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                return PC; //return value of pc

            case "31":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value;  //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value; //Load Value Of First Register
                PC++; //incriment pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incriment pc
                Imm2 = memory.Loadhex(PC);//Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16);  //Convert into integer
                sum = R1value + val; //Add it to first register value
                GPR.GPR[R1].value = (short)sum; ////Store the result in first register
                //Set flags
                setZeroFlag((short)sum,SPR,sum);
                setSignFlag((short)sum,SPR,sum);
                setCarryFlag((short)sum,SPR,sum);
                setOverflowFlag((short)sum,SPR,sum);
                ResetCarryFlag(SPR);
                return PC;  //return value of pc

            case "32":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++; //incriment pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incriment pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                sub = R1value - val; //Subtract it from first register value
                GPR.GPR[R1].value = (short)sub; ////Store the result in first register
                //Set flags
                setZeroFlag((short)sub,SPR,sub);
                setSignFlag((short)sub,SPR,sub);
                setCarryFlag((short)sub,SPR,sub);
                setOverflowFlag((short)sub,SPR,sub);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "33":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++; //incriment pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incriment pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                mul = R1value * val; //Multiply it first register value
                GPR.GPR[R1].value = (short)mul; //Store the result in first register
                //Set flags
                setZeroFlag((short)mul,SPR,mul);
                setSignFlag((short)mul,SPR,mul);
                setCarryFlag((short)mul,SPR,mul);
                setOverflowFlag((short)mul,SPR,mul);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "34":
                PC++;  //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++; //incriment pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++;  //incriment pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                if(val==0){
                    System.out.println("Division By Zero");   //Division By Zero Error
                    process.Log="Process terminated due to division By Zero";
                    return -1;
                }
                div = R1value / val; //Divide first register value by it
                GPR.GPR[R1].value = (short)div; //Store the result in first register
                //Set flags
                setZeroFlag((short)div,SPR,div);
                setSignFlag((short)div,SPR,div);
                setCarryFlag((short)div,SPR,div);
                setOverflowFlag((short)div,SPR,div);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "35":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break;
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++; //incriment pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incriment pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                a = R1value & val; //Perform ANDING on both values
                GPR.GPR[R1].value = (short)a; //Store the result in first register
                //Set flags
                setZeroFlag((short)a,SPR,a);
                setSignFlag((short)a,SPR,a);
                setCarryFlag((short)a,SPR,a);
                setOverflowFlag((short)a,SPR,a);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "36":
                PC++; //incriment pc
                R1=memory.Loaddec(PC).value; //load Adress Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check
                R1value = GPR.GPR[R1].value; //load Value Of First Register
                PC++; //incriment pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incriment pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                o = R1value | val; //Perform ORING on both values
                GPR.GPR[R1].value = (short)o; //Store the result in first register
                //Set flags
                setZeroFlag((short)o,SPR,o);
                setSignFlag((short)o,SPR,o);
                setCarryFlag((short)o,SPR,o);
                setOverflowFlag((short)o,SPR,o);
                ResetCarryFlag(SPR);
                return PC; //return value of pc
            case "37":
                //Check if Zero Flag Set
                if(((SPR.SPR[11].value)&2)!=0) { //If zero flag set
                    PC++; //incriment pc
                    PC++; //ignore register value
                    Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                    if(Imm1.equals("")) break; //Error Check
                    PC++; //incriment pc
                    Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                    if(Imm2.equals("")) break; //Error Check
                    Immediate=Imm1+Imm2; //Combine both values
                    val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                    //Check branching is within code limit
                    if(val+SPR.SPR[0].value>=(SPR.SPR[0].value+SPR.SPR[1].value)){
                        process.Log=("Process Ended Because of Out Of Bound Code Access");
                        System.out.println("Out Of Bound Code Access");
                        return -1;
                    }

                    SPR.SPR[9].value=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    //ReSet Flags
                    ResetZeroFlag(SPR);
                    ResetSignFlag(SPR);
                    ResetCarryFlag(SPR);
                    ResetOverflowFlag(SPR);
                    PC=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    if(PC!=0) PC--;//Decriment PC to keep it on the branching adressing on next instruction
                }
                else{ //if zero flag not set
                    PC++;//incriment pc (no branching)
                    PC++;//incriment pc (no branching)
                    PC++;
                }
                return PC; //return value of pc
            case "38":
                //Check if Zero Flag Set
                if(((SPR.SPR[11].value)&2)==0) { //If zero flag not set
                    PC++; //incriment pc
                    PC++; //ignore register value
                    Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                    if(Imm1.equals("")) break; //Error Check
                    PC++; //incriment pc
                    Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                    if(Imm2.equals("")) break; //Error Check
                    Immediate=Imm1+Imm2; //Combine both values
                    val = (short)Integer.parseInt(Immediate,16); //Convert into integer
                    //Check branching is within code limit
                    if(val+SPR.SPR[0].value>=(SPR.SPR[0].value+SPR.SPR[1].value)){
                        process.Log=("Process Ended Because of Out Of Bound Code Access");
                        System.out.println("Out Of Bound Code Access");
                        return -1;
                    }

                    SPR.SPR[9].value=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    //ReSet Flags
                    ResetZeroFlag(SPR);
                    ResetSignFlag(SPR);
                    ResetCarryFlag(SPR);
                    ResetOverflowFlag(SPR);
                    PC=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    if(PC!=0) PC--;//Decriment PC to keep it on the branching adressing on next instruction

                }
                else{ //If zero flag set
                    PC++; //incriment pc (no branching)
                    PC++;
                    PC++;
                }
                return PC; //return value of pc
            case "39":
                //Check if Carry Flag Set
                if(((SPR.SPR[11].value)&1)!=0) { //If carry flag set
                    PC++; //incriment pc
                    PC++; //ignore register value
                    Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                    if(Imm1.equals("")) break;//Error Check
                    PC++;//incriment pc
                    Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                    if(Imm2.equals("")) break; //Error Check
                    Immediate=Imm1+Imm2; //Combine both values
                    val = (short)Integer.parseInt(Immediate,16);  //Convert into integer
                    //Check branching is within code limit
                    if(val+SPR.SPR[0].value>=(SPR.SPR[0].value+SPR.SPR[1].value)){
                        process.Log=("Process Ended Because of Out Of Bound Code Access");
                        System.out.println("Out Of Bound Code Access");
                        return -1;
                    }

                    SPR.SPR[9].value=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    //ReSet Flags
                    ResetZeroFlag(SPR);
                    ResetSignFlag(SPR);
                    ResetCarryFlag(SPR);
                    ResetOverflowFlag(SPR);
                    PC=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    if(PC!=0) PC--;//Decriment PC to keep it on the branching adressing on next instruction
                }
                else{ //If carry flag not set
                    PC++;
                    PC ++; //incriment pc (no branching)
                    PC++;
                }
                return PC; //return value of pc
            case "3a":
                //Check if Sign Flag Set
                if(((SPR.SPR[11].value)&4)!=0) { //If sign flag set
                    PC++; //incrementWaitingTime pc
                    PC++; //ignore register value
                    Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                    if(Imm1.equals("")) break; //Error Check
                    PC++;//incrementWaitingTime pc
                    Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                    if(Imm2.equals("")) break; //Error Check
                    Immediate=Imm1+Imm2; //Combine both values
                    val = (short)Integer.parseInt(Immediate,16);  //Convert into integer
                    //Check branching is within code limit
                    if(val+SPR.SPR[0].value>=(SPR.SPR[0].value+SPR.SPR[1].value)){
                        process.Log=("Process Ended Because of Out Of Bound Code Access");
                        System.out.println("Out Of Bound Code Access");
                        return -1;
                    }

                    SPR.SPR[9].value=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    //ReSet Flags
                    ResetZeroFlag(SPR);
                    ResetSignFlag(SPR);
                    ResetCarryFlag(SPR);
                    ResetOverflowFlag(SPR);
                    PC=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                    if(PC!=0) PC--;//Decriment PC to keep it on the branching adressing on next instruction
                }
                else{ //If carry flag not set
                    PC++;
                    PC ++; //incriment pc (no branching)
                    PC++;
                }
                return PC; //return value of pc
            case "3b":
                PC++; //incrementWaitingTime pc
                PC++; //ignore register value
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++;//incrementWaitingTime pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16);  //Convert into integer
                //Check branching is within code limit
                if(val+SPR.SPR[0].value>=(SPR.SPR[0].value+SPR.SPR[1].value)){
                    process.Log=("Process Ended Because of Out Of Bound Code Access");
                    System.out.println("Out Of Bound Code Access");
                    return -1;
                }

                SPR.SPR[9].value=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)

                //ReSet Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);

                PC=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                if(PC!=0) PC--;//Decriment PC to keep it on the branching adressing on next instruction
                return PC; //return value of pc


            case "3c":
                //Check for Stack Overflow
                if(SPR.SPR[7].value==999){
                    process.Log=process.Log+("Process Ended Because of Stack Overflow");
                    System.out.println("Stack Overflow");
                    return -1;
                }
                process.SPR.SPR[7].value=(short)(process.SPR.SPR[7].value+2); //Incriment Stack Counter by 2


                PC++; //ignore register value
                PC++; //incrementWaitingTime pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                PC++;//incrementWaitingTime pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16);  //Convert into integer
                //Check branching is within code limit
                if(val+SPR.SPR[0].value>=(SPR.SPR[0].value+SPR.SPR[1].value)){
                    process.Log=("Process Ended Because of Out Of Bound Code Access");
                    System.out.println("Out Of Bound Code Access");
                    return -1;
                }
                PC++;
                check = PC;//Check which value pushed in stack
                stack.push(PC); //Push PC in stack
                SPR.SPR[9].value=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                //ReSet Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                PC=(short)(val+SPR.SPR[0].value); //Set PC to immediate value (Branching)
                if(PC!=0) PC--;//Decriment PC to keep it on the branching adressing on next instruction
                return PC; //return value of pc

            case "3d":
                PC++; //incrementWaitingTime pc
                PC++; //ignore register value
                PC++;
                return PC;

            case "51":
                PC++;       //incrementWaitingTime PC
                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check

                PC++; //incrementWaitingTime pc
                Imm1 = memory.Loadhex(PC); //Load first half of immediate value
                if(Imm1.equals("")) break; //Error Check
                PC++; //incrementWaitingTime pc
                Imm2 = memory.Loadhex(PC); //Load second half of immediate value
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values
                val = (short)Integer.parseInt(Immediate,16); //Convert into integer

                //Check Access Is Within Memory Limit
                if(val+SPR.SPR[3].value>=(SPR.SPR[3].value+SPR.SPR[4].value)){
                    process.Log=process.Log+("Process Ended Because of Out Of Bound Memory Access");
                    System.out.println("Out Of Bound Memory Access");
                    return -1;
                }
                //Check Access is not null
                if(memory.Loaddec((short)(val+SPR.SPR[3].value))==null){
                    System.out.println("Accessing Null Memory");
                    break;
                }
                Immediate=memory.Loadhex((short)(val+SPR.SPR[3].value))+ memory.Loadhex((short)(val+SPR.SPR[3].value+1)); //Takíng a Word From Memory
                val = (short)Integer.parseInt(Immediate,16); //Coverting into decimal


                GPR.GPR[R1].value = val; //Assign Value from Memory at R1

                //ReSet Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                return PC;

            case "52":
                PC++;       //incrementWaitingTime PC
                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check

                PC++; //incrementWaitingTime pc
                Imm1 = memory.Loadhex(PC); //Load first half of Memory Address
                if(Imm1.equals("")) break;//Error Check
                PC++; //incrementWaitingTime pc
                Imm2 = memory.Loadhex(PC); //Load second half of Memory Address
                if(Imm2.equals("")) break; //Error Check
                Immediate=Imm1+Imm2; //Combine both values

                val = (short)Integer.parseInt(Immediate,16); //Convert into integer which is basically the address of the memory

                //Check Access Is Within Memory Limit
                if(val+SPR.SPR[3].value>=(SPR.SPR[3].value+SPR.SPR[4].value)){
                    process.Log=process.Log+("Process Ended Because of Out Of Bound Memory Access");
                    System.out.println("Out Of Bound Memory Access");
                    return -1;
                }

                R1value = GPR.GPR[R1].value;   //Load Value Of Register 1

                String[] Halves = StringtoByte(Integer.toHexString(R1value)); //Split the value at R1 into two parts

                int z =memory.Store((byte)Integer.parseInt(Halves[0],16),(short)(val+SPR.SPR[3].value)); //Store the first part in Memory
                if(z==-1) break;

                z = memory.Store((byte)Integer.parseInt(Halves[1],16),(short)((short)(val+SPR.SPR[3].value)+1));//Store the second part in Memory
                if(z==-1) break;

                //ReSet Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                return PC;

            case "71":
                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check

                int IR1 = GPR.GPR[R1].value;

                IR1 = (IR1 << 1) & (0xFFFF);  //Shifting left the value present at R1

                GPR.GPR[R1].value = (short)IR1; //Assigning the new right bit shifted value to R1

                //Set Flags
                setZeroFlag((short)IR1,SPR,IR1);
                setSignFlag((short)IR1,SPR,IR1);
                setCarryFlag((short)IR1,SPR,IR1);
                ResetOverflowFlag(SPR);

                return PC;

            case "72":
                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break; //Error Check

                IR1 = IR1 = GPR.GPR[R1].value;

                IR1 = (IR1 >> 1) & (0xFFFF);  //Shifting right the value present at R1

                GPR.GPR[R1].value = (short)IR1; //Assigning the new right bit shifted value to R1


                //Set Flags
                setZeroFlag((short)IR1,SPR,IR1);
                setSignFlag((short)IR1,SPR,IR1);
                setCarryFlag((short)IR1,SPR,IR1);
                ResetOverflowFlag(SPR);

                return PC;

            case "73":
                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break;

                IR1 = Integer.valueOf(GPR.GPR[R1].value); //Convert value into Integer

                IR1 = rotateLeft(IR1); //Rotating left the bitwise value at R1

                GPR.GPR[R1].value = (short)IR1;


                //Set Flags
                setZeroFlag((short)IR1,SPR,IR1);
                setSignFlag((short)IR1,SPR,IR1);
                setCarryFlag((short)IR1,SPR,IR1);
                ResetOverflowFlag(SPR);

                return PC;

            case "74":
                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break;

                IR1 = Integer.valueOf(GPR.GPR[R1].value); //Convert value into Integer

                IR1 = rotateRight(IR1); //Rotating right the bitwise value at R1

                GPR.GPR[R1].value = (short)IR1;


                //Set Flags
                setZeroFlag((short)IR1,SPR,IR1);
                setSignFlag((short)IR1,SPR,IR1);
                setCarryFlag((short)IR1,SPR,IR1);
                ResetOverflowFlag(SPR);

                return PC;

            case "75":
                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break;

                GPR.GPR[R1].value =(short)(GPR.GPR[R1].value+1); //Increment the value present at R1 address

                sum = GPR.GPR[R1].value;//Convert into  Integer

                //Set Flags
                setZeroFlag((short)sum,SPR,sum);
                setSignFlag((short)sum,SPR,sum);
                setCarryFlag((short)sum,SPR,sum);
                setOverflowFlag((short)sum,SPR,sum);
                ResetCarryFlag(SPR);

                return PC;

            case "76":
                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register
                if(memory.Loaddec(PC).valid==false) break;

                GPR.GPR[R1].value =(short)(GPR.GPR[R1].value-1); //Decrement the value present at R1 address

                sum = GPR.GPR[R1].value;//Convert into  Integer

                //Set Flags
                setZeroFlag((short)sum,SPR,sum);
                setSignFlag((short)sum,SPR,sum);
                setCarryFlag((short)sum,SPR,sum);
                setOverflowFlag((short)sum,SPR,sum);
                ResetCarryFlag(SPR);

                return PC;


//
            case "77":

                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register

               //Checkíng for Stack Overflow
                if(SPR.SPR[7].value>997){
                    process.Log=("Process Ended Because of Stack Overflow");
                    System.out.println("Stack Overflow");
                    return -1;
                }

                stack.push(GPR.GPR[R1].value); //Push the contents into stack

                process.SPR.SPR[7].value=(short)(process.SPR.SPR[7].value+2); //Incrementing Stack Counter by 2

                //Reset Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                return PC;

//
//
            case "78":

                PC++;       //incrementWaitingTime PC

                R1=memory.Loaddec(PC).value;  //Load Address Of First Register

                //Checkíng for Stack Underflow
                if(SPR.SPR[7].value<1){
                    process.Log=("Process Ended Because of Stack Underflow");
                    System.out.println("Stack Underflow");
                    return -1;
                }

                //Popping value from stack
                Imm1 = stack.pop().tohex();
                Imm2 = stack.pop().tohex();;
                process.SPR.SPR[7].value=(short)(process.SPR.SPR[7].value-2); //Changing Stack Counter

                Immediate = Imm2 + Imm1; //Combining the values

                GPR.GPR[R1].value =  (short)Integer.parseInt(Immediate,16);  //Update the register value

                //Reset Flags
                ResetZeroFlag(SPR);
                ResetSignFlag(SPR);
                ResetCarryFlag(SPR);
                ResetOverflowFlag(SPR);
                return PC;




            case "f1":
                //Checkíng for Stack Underflow
                if(SPR.SPR[7].value<1){
                    process.Log=("Process Ended Because of Stack Underflow");
                    System.out.println("Stack Underflow");
                    return -1;
                }
                process.SPR.SPR[7].value=(short)(process.SPR.SPR[7].value-2); //Changing Stack Counter

                //Popping value from stack
                Imm1 = stack.pop().tohex();
                Imm2 = stack.pop().tohex();;

                List<Short> temp = new ArrayList<Short>(); //Array List to Ensure no stack elements discarded

                Immediate = Imm2 + Imm1; //Combining Popped Values

                //Popping from stack untill PC value found
                while((short)Integer.parseInt(Immediate,16)!=check){
                    temp.add((short)Integer.parseInt(Immediate,16)); //Storing unrequired elements in arraylist
                    process.SPR.SPR[7].value=(short)(process.SPR.SPR[7].value-2); //Updating Stack Counter

                    //Popping value from stack
                    Imm1 = stack.pop().tohex();
                    Imm2 = stack.pop().tohex();

                    Immediate = Imm2 + Imm1; //Combining Popped Values
                }
                PC = (short)Integer.parseInt(Immediate,16);  //Assigning Value To PC
               //Pushing the unused Popped Values back To Stack
                while(!temp.isEmpty()){
                    stack.push(temp.get(temp.size()-1));
                    temp.remove(temp.size()-1);
                    process.SPR.SPR[7].value=(short)(process.SPR.SPR[7].value+2);
                }
                return (short)(PC-1);

            case "f2":
                return PC;

            default:
                //Check for Out of Bound Opcode
                process.Log=("Process Ended Because of Out Of Bound Opcode");
                System.out.println("Out Of Bound Opcode");
                return -1;
        }
        return -1; // Unsuccessful Return Value
    }

    public short rotateLeft(int number)
    {
        if(number<0){ //For negative number

            number = Integer.rotateLeft(number,1);//rotate left the bits of the given value
            return (short) number;
        }
        else {
            int t = 16; //Since the Register space has 16 bits only

            int i = number << 1; //Shifting one bit left of the value given
            i = (i | number >> 15); //Shifting 15 bits right so that the 16 bit comes at the right most bit
            i = i & 0xFFFF; //since integer has 32 bits we need to make sure the upper bits are set to zero as we are working on 16 bits only
            //We use AND logical operator to take care of upper 16 bits

            return (short) i; //The value of integer "i" won't be bigger than 16 bits value
        }
    }

    public short rotateRight(int number)
    {
        if (number<0)
        {
            int check = number & 1; //check the right most bit if its 1 or not
            int i;
            if (check != 0 ){ //  If the right most bit is set 1

                i = number >> 1; //Shifting one bit right of the value given

                i = i | 32768; //OR the 16 bit as 1

                i = i & 0xFFFF;//since integer has 32 bits we need to make sure the upper bits are set to zero as we are working on 16 bits only
                //We use AND logical operator to take care of upper 16 bits

            }
            else { //If the right most bit is not 1

                i = number >> 1; //Shift the bits to right

                i = i & 32767; // set the 17 left most bits of the value as 1

                i = i & 0xFFFF;//since integer has 32 bits we need to make sure the upper bits are set to zero as we are working on 16 bits only
                //We use AND logical operator to take care of upper 16 bits

            }

            return (short) i; //The value of integer "i" won't be bigger than 16 bits value
        }
        else {

            int t = 16; //Since the Register space has 16 bits only

            int i = number >> 1; //Shifting one bit right of the value given
            i = (i | number << 15); //Shifting 15 bits left so that the first bit comes at the left most bit
            i = i & 0xFFFF; //since integer has 32 bits we need to make sure the upper bits are set to zero as we are working on 16 bits only
            //We use AND logical operator to take care of upper 16 bits

            return (short) i; //The value of integer "i" won't be bigger than 16 bits value
        }
    }


    public String[] StringtoByte(String RegisterValue){
        String[] Halves = new String[2];

        if(RegisterValue.length()>4){ //Specifying the length of register value
            RegisterValue=RegisterValue.substring(4);
        }

        while(RegisterValue.length()<4){//Specifying the length of register value
            RegisterValue="0"+ RegisterValue;
        }

        //Splitting the Register value
        Halves[0] = RegisterValue.substring(0, 2);
        Halves[1] = RegisterValue.substring(2, 4);
        return Halves;
    }

    public void setZeroFlag(short result, SpecialPurposeRegisters SPR, int actual){
        if(result==(short)0){ //IF result zero set Zero Flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)|2);
        }
        else { //Else Reset Zero Flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65533);
        }
    }
    public void setSignFlag(short result, SpecialPurposeRegisters SPR, int actual){
        if(result<(short)0){ //IF result negative set Sign Flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)|4);
        }
        else { //Else Reset Sign Flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65531);
        }
    }
    public void setCarryFlag(short result, SpecialPurposeRegisters SPR, int actual){
        if(actual>65535 || actual<0){ //IF result generates carry set Carry Flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)|1);
        }
        else{ //Else Reset Carry Flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65534);
        }
    }

    public void setOverflowFlag(short result, SpecialPurposeRegisters SPR, int actual){
        //If overflow generated set overflow flag
        if((((((SPR.SPR[11].value)&1)!=0))&((SPR.SPR[11].value)&4)==0)){
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)|8);
        }
        else if(((((SPR.SPR[11].value)&1)==0)&((SPR.SPR[11].value)&4)!=0)){
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)|8);
        }

        else{ //Else reset overflow flag
            (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65527);
        }
    }

    public void ResetZeroFlag(SpecialPurposeRegisters SPR){
        //Reset Zero Flag
        (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65533);

    }

    public void ResetSignFlag(SpecialPurposeRegisters SPR){
        //Reset Sign Flag
        (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65531);
    }

    public void ResetCarryFlag(SpecialPurposeRegisters SPR){
        //Reset Carry Flag
        (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65534);

    }
    public void ResetOverflowFlag(SpecialPurposeRegisters SPR){
        //reset overflow flag
        (SPR.SPR[11].value)=(short)((SPR.SPR[11].value)&65527);

    }


}
