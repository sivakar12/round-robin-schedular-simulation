
import java.awt.Dimension;
import java.util.List;
import javax.swing.*;

public class ProgressPanel extends JPanel {
	private List<Process> allProcess;
	public ProgressPanel(List<Process> allProcess) {
		this.allProcess = allProcess;
		//startFresh();
	}
	public void startFresh() {
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel names = new JPanel();
		names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
		names.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		names.add(new JLabel("Time"));
		for (Process p: allProcess) {
			names.add(new JLabel("P" + p.getProcessId()));
		}
		this.add(names);
	}
	public void next(int time) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(new JLabel(Integer.toString(time)));
		for (Process p: allProcess) {
			panel.add(new JLabel(p.getStateString().substring(0, 1)));
		}
		this.add(panel);
	}
//	public Dimension getPreferredSize() {
//		return new Dimension(500, 100);
//	}
//	public Dimension getMinimumSize() {
//		return new Dimension(500, 400);
//	}
}
