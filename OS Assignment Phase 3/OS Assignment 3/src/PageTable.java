import java.util.ArrayList;
import java.util.List;

//Class Containing The Page Table Of A Process
public class PageTable {
    List<Integer> list=new ArrayList<Integer>(); //List of Pages Assigned to a Process

    //Printing the PageTable
    public String toString(){
          return list.toString();
    }
}

