import java.util.*;
import java.util.Scanner;

//Main Execution Class
public class Main {
    public static void main(String args[]) {

        CLICommands CLI =  new CLICommands();    //Create a class for CLI Command Execution
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object

        System.out.println("Type Help TO GET COMMANDS INFORMATION"); //Displaying Help Prompt

        while(true){   //Loop Till Shut Down Instruction

            System.out.println("Enter Commands For The Processes"); //Prompt User To Enter Command
            String Command = myObj.nextLine();  // Read user input
            String[] div = Command.split(" "); //Split input String
            CLI.execute(div); //Execute CLI Command
            if(div[0].equals("Shutdown")) System.exit(0); //Exit Program On Shut Down Command
        }

    }
}
