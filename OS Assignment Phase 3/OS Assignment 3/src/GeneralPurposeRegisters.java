//Class For All General Purpose Registers
public class GeneralPurposeRegisters {
    Register[] GPR = new Register[16];   //Array of General Purpose Registers

    public GeneralPurposeRegisters(){   //Initializing Codes and Values Of General Purpose Registers
          for(int i =0;i<16;i++){
               GPR[i]=new Register();
               GPR[i].code = "0"+ Integer.toHexString(i);
               GPR[i].value = (short) 0;
          }
    }
    public String toString(){     //Output Fuction For General Purpose Registers
        String out="";
        out=("General Purpose Registers" + '\n');
        for(int i =0;i<16;i++){
            String temp = Integer.toHexString((int)GPR[i].value);
            if(temp.length()==1) temp= "000" + temp;
            else if(temp.length()==2) temp= "00" + temp;
            else if(temp.length()==3) temp= "0" + temp;
            else if(temp.length()>4) temp = temp.substring(4,8);
            out=out + (GPR[i].code+" : "+ temp + '\n');
        }
        return out;
    }

}
