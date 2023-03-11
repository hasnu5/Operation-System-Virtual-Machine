//Class of unsigned byte
public class Ubyte {
    int value; //value of the byte
    boolean valid=true;
    public Ubyte(byte num){ //converts a signed byte into unsigned byte
        value = num & 0xff;
    }
    public String toString(){  //Outputs the Value of An Unsigned Byte
        return ""+this.value;
    }
    public String tohex(){
        String hex = Integer.toHexString(this.value);
        if(hex.length()==1){//Specifying the length of register value
            hex="0"+ hex;
        }
        return hex;
    }

}
