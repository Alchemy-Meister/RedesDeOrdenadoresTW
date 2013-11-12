package graphicInterface;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Animate {

	public int runTime;

	private JPanel panel;
	private Rectangle from;
	private Rectangle to;

	private long startTime;
	private double progress;
	private boolean finished;
	
	public Animate(JPanel panel, Rectangle from, Rectangle to, int runTime) {
		this.runTime = runTime;
		this.panel = panel;
		this.from = from;
		this.to = to;
	}

	public void start() {
		Timer timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long duration = System.currentTimeMillis() - startTime;
				progress = (double) duration / (double) runTime;
				if (progress > 1f) {
					progress = 1f;
					((Timer) e.getSource()).stop();
				}
				Rectangle target = calculateProgress(from, to, progress);
				panel.setBounds(target);
				finised();
				
			}
		});
		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.setInitialDelay(0);
		startTime = System.currentTimeMillis();
		timer.start();
	}

	private Rectangle calculateProgress(Rectangle startBounds,
			Rectangle targetBounds, double progress) {

		Rectangle bounds = new Rectangle();

		if (startBounds != null && targetBounds != null) {

			bounds.setLocation(calculateProgress(startBounds.getLocation(),
					targetBounds.getLocation(), progress));
			bounds.setSize(calculateProgress(startBounds.getSize(),
					targetBounds.getSize(), progress));

		}

		return bounds;

	}

	private Point calculateProgress(Point startPoint, Point targetPoint,
			double progress) {

		Point point = new Point();

		if (startPoint != null && targetPoint != null) {

			point.x = calculateProgress(startPoint.x, targetPoint.x, progress);
			point.y = calculateProgress(startPoint.y, targetPoint.y, progress);

		}

		return point;

	}

	private static int calculateProgress(int startValue, int endValue,
			double fraction) {

		int value = 0;
		int distance = endValue - startValue;
		value = (int) Math.round((double) distance * fraction);
		value += startValue;

		return value;

	}

	public static Dimension calculateProgress(Dimension startSize,
			Dimension targetSize, double progress) {

		Dimension size = new Dimension();

		if (startSize != null && targetSize != null) {

			size.width = calculateProgress(startSize.width, targetSize.width,
					progress);
			size.height = calculateProgress(startSize.height,
					targetSize.height, progress);

		}

		return size;

	}
	
	private void finised() {
		finished = to.equals(panel.getBounds());
	}
	public boolean hasFinished() {
		return finished;
	}
}