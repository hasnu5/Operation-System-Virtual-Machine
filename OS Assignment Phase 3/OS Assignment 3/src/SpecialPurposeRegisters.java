//Class For All Special Purpose Registers
public class SpecialPurposeRegisters {
    Register[] SPR = new Register[16];  //Array of Special Purpose Registers


    public SpecialPurposeRegisters(){ //Initializing Codes and Values Of Special Purpose Registers
        for(int i =0;i<16;i++){
                SPR[i]= new Register();
                SPR[i].value = (short) 0;
        }


        SPR[0].code = "Code Base(CB)";
        SPR[1].code = "Code Limit(CL)";
        SPR[2].code = "Code Counter(CC)";
        SPR[3].code = "Data Base(DB)";
        SPR[4].code = "Data Limit(DL)";
        SPR[5].code = "Data Counter(DC)";
        SPR[6].code = "Stack Base(SB)";
        SPR[7].code = "Stack Counter(SC)";
        SPR[8].code = "Stack Limit(SL)";
        SPR[9].code = "Program Counter(PC)";
        SPR[10].code = "Instruction Register(IR)";
        SPR[11].code = "Flag Register(FR)";
        SPR[12].code = "13th SPR";
        SPR[13].code = "14th SPR";
        SPR[14].code = "15th SPR";
        SPR[15].code = "16th SPR";


    }

    public String toString(){  //Output Fuction For Special Purpose Registers
        String out="";
        out = "Special Purpose Registers" + '\n' ;
        for(int i =0;i<16;i++){
            if(i!=11) {
                String temp = Integer.toHexString((int) SPR[i].value);
                if (temp.length() == 1) temp = "000" + temp;
                else if (temp.length() == 2) temp = "00" + temp;
                else if (temp.length() == 3) temp = "0" + temp;
                else if (temp.length() > 4) temp = temp.substring(4, 8);
                out = out + (SPR[i].code + " : " + temp + '\n');
            }
            else{
                String temp = Integer.toBinaryString((int) SPR[i].value);
                while(temp.length()<16){
                    temp= "0" + temp;
                }
                out = out + (SPR[i].code + " : ");
                for(int j=0;j<16;j++){
                    if(j==12) {
                        out = out + ("Overflow Flag : " + temp. charAt(j)
                        + ' ');
                    }
                    else if(j==13){
                        out = out + ("Sign Flag : " + temp.charAt(j) + ' ');
                    }
                    else if(j==14) {
                        out = out + ("Zero Flag : " + temp.charAt(j) + ' ');
                    }
                    else if(j==15) {
                        out = out + ("Carry Flag: " + temp.charAt(j) + ' ');
                    }
                    else{
                        out = out + ("Unused Flag: " + temp.charAt(j) + ' ');
                    }


                }
                out =out + '\n';
            }

        }
        return out;
    }
}
