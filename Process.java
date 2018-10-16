
import java.util.LinkedList;

public class Process {
	private int processId;

    private int arrivalTime;
    private int burstTime;
    private int executedTime;
    private int waitingTime;
    private int blockedTime;
    private int totalTime;

    private LinkedList<Integer> blockingTimes;
    private LinkedList<Integer> unblockingTimes;

	private ProcessState state;

    private static int createdProcesses = 0;

    public Process(int[] times)
    {
        processId = createdProcesses;
        createdProcesses++;

        blockingTimes = new LinkedList<Integer>();
        unblockingTimes = new LinkedList<Integer>();
        processTimesArray(times);

		this.state = ProcessState.NOTARRIVED;
    }

	public void setState(ProcessState state) {
		this.state = state;
	}
	public ProcessState getState() {
		return state;
	}
	
    public void processTimesArray(int[] times) {
        this.arrivalTime = times[0];
        int lastTime = 0;
        for (int i = 1; i < times.length - 1; i++) {
            if (i % 2 == 1) {
                blockingTimes.add(times[i] + lastTime);
            } else {
                unblockingTimes.add(times[i] + lastTime);
            }
            totalTime += times[i];
            lastTime += times[i];
        }
        totalTime += times[times.length - 1];
    }
    // remove time from list if it is returned true
    // so we can always check only the first element
    public boolean isTimeToBlock() {
        if (!blockingTimes.isEmpty()
                &&(executedTime + blockedTime == blockingTimes.getFirst())) {
            blockingTimes.removeFirst();
            return true;
        } else {
            return false;
        }
    }
    public boolean isTimeToUnblock() {
        if (!unblockingTimes.isEmpty() &&
                (executedTime + blockedTime == unblockingTimes.getFirst())) {
            unblockingTimes.removeFirst();
            return true;
        } else {
            return false;
        }
    }
	public int getTimeUntilUnblock() {
		if (!unblockingTimes.isEmpty()) {
			return unblockingTimes.getFirst() - executedTime - blockedTime + 1;
		}
		return -1;
	}

    public int getProcessId() { return processId;  }
    public int getArrivalTime() { return arrivalTime;  }
    public int getBurstTime() { return burstTime; }
    public int getExecutedTime() { return executedTime; }
    public int getWaitingTime() { return waitingTime; }
    public int getBlockedTime() { return blockedTime; }
    public int getTotalTime() { return totalTime; }
	

    public void incrementWaitingTime() { waitingTime++; }
    public void incrementExecutedTime() { executedTime++; }
    public void incrementBlockedTime() { blockedTime++; }

    public String toString() {
        return "P" + processId + ": " + " B: " + burstTime +
                " W:" + waitingTime + " E:" + executedTime;
    }
	public String getStateString() {
		switch(state) {
			case NOTARRIVED:
				return "NOT ARRIVED";
			case READY:
				return "READY";
			case ACTIVE:
				return "ACTIVE";
			case BLOCKED:
				return "BLOCKED";
			case FINISHED:
				return "FINISHED";
			default:
				return "";
		}
	}
}
