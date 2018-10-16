
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SchedulerGUI extends JFrame implements ActionListener, ChangeListener {
    private static final int WIDTH = 800, HEIGHT = 160;
    Scheduler scheduler;
    
	JPanel currentProcess;
	JPanel queue;
    JPanel blockedItems;
    JPanel finishedItems;
	ProgressPanel progress;
	
	JPanel controls;
	JPanel details;
	
	JSpinner timeQuantum;
	JButton setQuantum;
	JTextField inputField;
	JButton addProcess;
	JButton loadFromFile;
	JButton generateRandom;
	JButton play;
	JButton pause;
    JButton next;
	
	Timer timer;

    public SchedulerGUI() {
        super("Round Robin Scheduler Simulation");
        scheduler = new Scheduler();

        queue = new JPanel();
        queue.setLayout(new FlowLayout(FlowLayout.LEFT));

        blockedItems = new JPanel();
        blockedItems.setLayout(new FlowLayout(FlowLayout.LEFT));

        finishedItems = new JPanel();
        finishedItems.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		progress = new ProgressPanel(scheduler.getAllProcess());
		
		createControls();
		
		details = new JPanel();
		
        refreshComponents();

        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(controls);
		this.getContentPane().add(details);
		
		this.getContentPane().add(
				createScrollablePanel(queue, "The Queue", WIDTH, HEIGHT));
		this.getContentPane().add(
				createScrollablePanel(blockedItems, "Blocked Processes", WIDTH,
						120));
		
        this.getContentPane().add(createScrollablePanel(
				finishedItems, "Finished Processes", WIDTH, 150));
		
		
		String progressPaneTitle = " Progress (A: Active R: Ready B: Blocked " +
				"N: Not Arrived  F: Finished)";
		this.getContentPane().add(
				createScrollablePanel(progress, progressPaneTitle, WIDTH, HEIGHT));
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
		
		timer = new Timer(200, null);
			timer.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					scheduler.nextClockCycle();
					refreshComponents();
				}
			});
    }
	
	private void createControls() {
		controls = new JPanel();
		
		timeQuantum = new JSpinner();
		timeQuantum.setValue(10);
		timeQuantum.addChangeListener(this);
		
		inputField = new JTextField(10);
		addProcess = new JButton("Add Process");
		addProcess.addActionListener(this);
		
		loadFromFile = new JButton("Load From File");
		loadFromFile.addActionListener(this);
		
		generateRandom = new JButton("Generate Random");
		generateRandom.addActionListener(this);
		
		play = new JButton("Play");
		play.addActionListener(this);
		
		pause = new JButton("Pause");
		pause.addActionListener(this);
		
        next = new JButton("Next");
        next.addActionListener(this);;
		
		controls.setLayout(new FlowLayout());
		controls.add(new JLabel("Time Quantum:"));
		controls.add(timeQuantum);
		controls.add(new JLabel("Enter Process Details:"));
		controls.add(inputField);
		controls.add(addProcess);
		controls.add(loadFromFile);
		controls.add(generateRandom);
		controls.add(play);
		controls.add(pause);
		controls.add(next);
	}
	private JScrollPane createScrollablePanel(JPanel panel, String title,
			int width, int height) {
		JScrollPane scrollPane = new JScrollPane(panel, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(BorderFactory.createTitledBorder(title));
		scrollPane.setMinimumSize(new Dimension(width, height));
		scrollPane.setPreferredSize(new Dimension(width, height));
		return scrollPane;
	}
	
    private void refreshComponents() {
        queue.removeAll();
        queue.repaint();
        queue.revalidate();
        blockedItems.removeAll();
        blockedItems.repaint();
        blockedItems.revalidate();
        finishedItems.removeAll();
        finishedItems.repaint();
        finishedItems.revalidate();

        
        if (scheduler.getCurrentProcess() != null) {
            ReadyProcessPanel p = new ReadyProcessPanel(scheduler.getCurrentProcess());
            queue.add(p);
        }
        for (Process process: scheduler.getReadyQueue()) {
            ReadyProcessPanel p = new ReadyProcessPanel(process);
            queue.add(p);
        }
        for (Process process: scheduler.getBlockedItems()) {
            BlockedProcessPanel b = new BlockedProcessPanel(process);
            blockedItems.add(b);
        }
        for (Process process: scheduler.getFinishedItems()) {
            FinishedProcessPanel p = new FinishedProcessPanel(process);
            finishedItems.add(p);
        }
		updateDetails();
		
		progress.next(scheduler.getCurrentTime());
    }
	private void updateDetails() {
//		details.setText("Current Time: " + scheduler.getCurrentTime()
//			+ " Time Quantum: " + scheduler.getTimeQuantum()
//			+ " Time Until Preemption: " + scheduler.getTimeUntilPreemption()
//			+ " Idle Time: " + scheduler.getIdleTime()
//			+ " Average Waiting Time: " + scheduler.getAverageWaitingTime()
//			+ " Average Response Time: " + scheduler.getAverageResponseTime()
//			+ " Throughput: " + scheduler.getThroughput()
//		);
		details.removeAll();
		details.setLayout(new GridLayout(0,6));
		details.add(new JLabel("Current Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getCurrentTime())));
		details.add(new JLabel("Time Quantum:"));
		details.add(new JLabel(String.valueOf(scheduler.getTimeQuantum())));
		details.add(new JLabel("Time Until Preemption:"));
		details.add(new JLabel(String.valueOf(
				scheduler.getTimeUntilPreemption())));
		details.add(new JLabel("Idle Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getIdleTime())));
		details.add(new JLabel("CPU Usage"));
		details.add(new JLabel(scheduler.getUsePercentage() + "%"));
		details.add(new JLabel("Average Waiting Time:"));
		details.add(new JLabel(String.valueOf(
				scheduler.getAverageWaitingTime())));
		details.add(new JLabel("Average Response Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getAverageResponseTime())));
		
		details.add(new JLabel("Minimum Waiting Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getMinimumWaitingTime())));
		details.add(new JLabel("Maximum Waiting Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getMaximumWaitingTime())));
		details.add(new JLabel("Minimum Response Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getMinimumResponseTime())));
		details.add(new JLabel("Maximum Response Time:"));
		details.add(new JLabel(String.valueOf(scheduler.getMaximumResponseTime())));
		details.add(new JLabel("Processes finished:"));
		details.add(new JLabel(scheduler.getFinishedItems().size() +
				"/" + scheduler.getAllProcess().size()));
		
		details.repaint();
		details.revalidate();
	}

    @Override
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == next) {
			scheduler.nextClockCycle();
			refreshComponents();
		} else if (e.getSource() == play) {
			timer.start();
		} else if (e.getSource() == pause) {
			timer.stop();
		} else if (e.getSource() == addProcess) {
			String[] tokens = inputField.getText().split(",");
			int[] ints = new int[tokens.length];
			for (int i = 0; i < tokens.length; i++) {
				ints[i] = Integer.parseInt(tokens[i]);
			}
			scheduler.addProcess(new Process(ints));
			progress.removeAll();
			progress.startFresh();
			progress.repaint();
			progress.revalidate();
			inputField.setText("");
		} else if (e.getSource() == loadFromFile) {
	        scheduler.loadProcesesDataFromFile();
			progress.removeAll();
			progress.startFresh();
			progress.repaint();
			progress.revalidate();
		} else if (e.getSource() == generateRandom) {
			scheduler.generateRandomProcesses();
			progress.removeAll();
			progress.startFresh();
			progress.repaint();
			progress.revalidate();
		}
    }
	@Override
	public void stateChanged(ChangeEvent e) {
		scheduler.setTimeQuantum((int)timeQuantum.getValue());
		updateDetails();
	}
    public static void main(String[] args) {
        new SchedulerGUI();
	}

}

