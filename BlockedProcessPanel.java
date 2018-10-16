
import java.awt.*;
import javax.swing.*;

public class BlockedProcessPanel extends JPanel {
    static final int WIDTH = 110, HEIGHT = 80;

    public BlockedProcessPanel(Process p) {

        setLayout(new FlowLayout(FlowLayout.CENTER));

        this.setBorder(BorderFactory.createEtchedBorder());
        String waitingTime = "Waiting Time: " + String.valueOf(p.getWaitingTime());
        String serviceTime = "Executed Time: " + String.valueOf(p.getExecutedTime());
		String blockedTime = "Blocked Time: " + String.valueOf(p.getBlockedTime());

		JLabel processName = new JLabel("Process #" + p.getProcessId());
		processName.setForeground(Color.BLUE);
		this.add(processName);
//        this.add(new JLabel(serviceTime));
//        this.add(new JLabel(waitingTime));
//        this.add(new JLabel(blockedTime));
		this.add(new JLabel("Unblock In: " + p.getTimeUntilUnblock()));

        JLabel status = new JLabel(p.getStateString());
        status.setForeground(Color.RED);
        this.add(status);
    }
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    public Dimension getMinimumSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
}
