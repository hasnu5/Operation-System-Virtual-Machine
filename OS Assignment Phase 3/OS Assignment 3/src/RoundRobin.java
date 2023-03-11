import java.util.Collections;
import java.util.List;

public class RoundRobin {
    int index = 0; //index for each process
    List<ProcessControlBlock> Queue;//Queue2
    public RoundRobin(List<ProcessControlBlock> Queue){
        this.Queue = Queue;
    } //constructor for Queue2

    public ProcessControlBlock getProcess(){
        return Queue.get(index);
    } //to retrieve process from Queue2

    public List<ProcessControlBlock> incrementWaitingTime(){ //increment waiting time of each process other than the process currently in running state
        for(int i=0;i<=Queue.size()-1;i++){
            if(index!=i) {
                Queue.get(i).WaitingTime += 2;
            }
        }
        return Queue;
    }

    public List<ProcessControlBlock> incrementWaitingTimeAll(){ //increment waiting time of all processes while Queue1 is running
        for(int i=0;i<=Queue.size()-1;i++){
            Queue.get(i).WaitingTime += 2;
        }
        return Queue;
    }

    public List<ProcessControlBlock> incrementExecutionTime(){ //increment execution time of the process currently running
        Queue.get(index).ExecutionTime +=2;
        return Queue;
    }

    public List<ProcessControlBlock> reArrange(){ //send back the current process at the end as its quantum time is up

        Collections.rotate(Queue,-1);

        return Queue;
    }



    public List<ProcessControlBlock> remove(){ //remove the process currently in Queue2
        Queue.remove(index);
        return Queue;
    }

    public List<ProcessControlBlock> removelast(){//remove the last process in Queue2

        Queue.get(Queue.size()-1).ExecutionTime +=2;

        Queue.remove(Queue.size()-1);
        return Queue;
    }





}
