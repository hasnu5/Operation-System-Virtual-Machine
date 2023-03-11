import java.util.List;

public class FCFS {
    int index = 0;//index for each process
    List<ProcessControlBlock> Queue; //Queue1
    public FCFS(List<ProcessControlBlock> Queue){//constructor for Queue1
        this.Queue = Queue;
    }

    public ProcessControlBlock getProcess(){
        return Queue.get(index);
    }//to retrieve process from Queue1

    public List<ProcessControlBlock> incrementWaitingTime(){//increment waiting time of each process other than the process currently in running state
        for(int i=0;i<=Queue.size()-1;i++){
            if(index!=i) {
                Queue.get(i).WaitingTime += 2;
            }
        }
        return Queue;
    }

    public List<ProcessControlBlock> incrementExecutionTime(){//increment execution time of the process currently running
        Queue.get(index).ExecutionTime +=2;
        return Queue;
    }

    public List<ProcessControlBlock> remove(){//remove the process currently in Queue2
        Queue.remove(index);
        return Queue;
    }


}
