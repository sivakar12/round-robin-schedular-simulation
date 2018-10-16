
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Scheduler {
    static final int MAX_BURST_TIME = 20;
    static final int MAX_ARRIVAL_TIME = 100;

    static final String FILENAME = "processes.csv";

    private LinkedList<Process> readyQueue;
    private ArrayList<Process> blockedList;
    private List<Process> allProcess;
    private ArrayList<Process> finishedProcesses;
    private Process currentProcess;

    private int timeQuantum;
    private int timeUntilPreemption;
    private int currentTime;
	
	private int idleTime;
	private int serviceTime;
	
	private int arrivedCount;
	private int totalWaitingTime;
	private int totalResponseTime;
	private int finishedCount;
	private int minimumWaitingTime;
	private int maximumWaitingTime;
	private int minimumResponseTime;
	private int maximumResponseTime;
	
    public Scheduler()
    {
        timeQuantum = 10;   // use setTimeQuantum to change
        currentTime = 0;
        readyQueue = new LinkedList<Process>();
        blockedList = new ArrayList<Process>();
        allProcess = new ArrayList<Process>();
        finishedProcesses = new ArrayList<Process>();
    }
    public void setTimeQuantum(int time)
    {
        this.timeQuantum = time;
    }
    public void addProcess(Process p)
    {
        allProcess.add(p);
    }

    // increment the executing time of current process
    // increment the waiting time of all ready processes
    // increment the blocked time of all the process in the blocked list
    // if a process is finished, mark finish, dequeue a new process,
    //      and reset the time
    // if current process needs to be blocked, send to the blocked list
    //      and dequeue a new process
    // if it its time to preempt, dequeue a new process,
    //      and enqueue the current one, and reset the preempt time
    // if a new process can come in, add it to the queue
    // if a blocked process can resume, add it to the queue
    public void nextClockCycle()
    {
		
        if (currentProcess != null && processFinished(currentProcess)) {
			currentProcess.setState(ProcessState.FINISHED);
			if (currentProcess.getWaitingTime() < minimumWaitingTime) 
				minimumWaitingTime = currentProcess.getWaitingTime();
			if (currentProcess.getWaitingTime() > maximumWaitingTime) 
				maximumWaitingTime = currentProcess.getWaitingTime();
			finishedCount++;
            finishedProcesses.add(currentProcess);
            currentProcess = null;
        }
        if (currentProcess != null &&
                currentProcess.isTimeToBlock()) {
			currentProcess.setState(ProcessState.BLOCKED);
            blockedList.add(currentProcess);
            currentProcess = null;
        }
        if (currentProcess != null && timeUntilPreemption == 0) {
			currentProcess.setState(ProcessState.READY);
            readyQueue.add(currentProcess);
            currentProcess = null;
        }
        for (Process p : allProcess) {
            if (p.isTimeToUnblock()) {
				p.setState(ProcessState.READY);
                blockedList.remove(p);
                readyQueue.add(p);
            }
			if (p.getState() == ProcessState.BLOCKED && processFinished(p)) {
				p.setState(ProcessState.FINISHED);
				if (p.getWaitingTime() < minimumWaitingTime) 
				minimumWaitingTime = p.getWaitingTime();
			if (p.getWaitingTime() > maximumWaitingTime) 
				maximumWaitingTime = p.getWaitingTime();
				finishedCount++;
				blockedList.remove(p);
				finishedProcesses.add(p);
			}
        }
        currentTime++;
        for(Process p : allProcess)
        {
            if (p.getArrivalTime() == currentTime)
            {
				arrivedCount++;
				p.setState(ProcessState.READY);
                readyQueue.add(p);
            }
        }
        if (currentProcess == null) {
            currentProcess = readyQueue.poll();
			timeUntilPreemption = timeQuantum;
            if (currentProcess != null) {
                currentProcess.setState(ProcessState.ACTIVE);
            } else {
				idleTime++;
			}
        }
        if (currentProcess != null) {
			if (currentProcess.getExecutedTime() == 0) {
				int responseTime = currentProcess.getWaitingTime();
				if (responseTime > maximumResponseTime)
					maximumResponseTime = responseTime;
				if (responseTime < minimumResponseTime)
					minimumResponseTime = responseTime;
				totalResponseTime += responseTime;
			}
            currentProcess.incrementExecutedTime();
            timeUntilPreemption--;
        }
        for(Process p : readyQueue)
        {
            p.incrementWaitingTime();
			totalWaitingTime++;
        }
        for (Process p: blockedList) {
            p.incrementBlockedTime();
        }


    }
    private boolean processFinished(Process process)
    {
        return process.getExecutedTime() + process.getBlockedTime() ==
                process.getTotalTime();
    }
	public boolean allProcessFinished() {
		for (Process p : allProcess) {
			if (p.getState() != ProcessState.FINISHED) {
				return false;
			}
		}
		return true;
	}
    public void loadProcesesDataFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));

            String line;
            Process p;
            while((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                int[] ints = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    ints[i] = Integer.parseInt(tokens[i]);
                }
                p = new Process(ints);
                addProcess(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public void generateRandomProcesses() {
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			int[] data = new int[random.nextInt(5) * 2 + 2];
			data[0] = random.nextInt(100) + 1;
			for (int j = 1; j < data.length; j++) {
				data[j] = random.nextInt(10) + 1;
			}
			addProcess(new Process(data));
		}
	}

    public LinkedList<Process> getReadyQueue() { return readyQueue; }
    public ArrayList<Process> getBlockedItems() { return blockedList; }
    public ArrayList<Process> getFinishedItems() { return finishedProcesses; }
	public List<Process> getAllProcess() { return allProcess; }
    public Process getCurrentProcess() { return currentProcess; }
	
	public int getCurrentTime() { return currentTime; }
	public int getIdleTime() { return idleTime; }
	public int getTimeUntilPreemption() { return timeUntilPreemption; }
	public int getTimeQuantum() { return timeQuantum; }
	public double getAverageWaitingTime() {
		return totalWaitingTime * 1.0 / arrivedCount;
	}
	public double getAverageResponseTime() {
		return totalResponseTime * 1.0 / arrivedCount;
	}
	public double getThroughput() {
		return finishedCount * 1.0 / currentTime;
	}
	public int getMinimumResponseTime() { return minimumResponseTime; }
	public int getMaximumResponseTime() { return maximumResponseTime; }
	public int getMinimumWaitingTime() { return minimumWaitingTime; }
	public int getMaximumWaitingTime() { return maximumWaitingTime; }
	
	public double getUsePercentage() {
		return 100.0 * (currentTime - idleTime) / currentTime;
	}
	
}
