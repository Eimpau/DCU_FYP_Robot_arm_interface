package ArmControl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class ArmGUI extends JFrame implements WindowListener, ActionListener, ChangeListener, KeyListener {

	// Panels
	private JPanel leftPanel; // For values
	private JPanel middlePanel; // For sliders
	private JPanel topPanel; // For connection
	private JPanel rightPanel; // For buttons

	// TextFields
	private JTextField valueText1;
	private JTextField valueText2;
	private JTextField valueText3;
	private JTextField valueText4;
	private JTextField valueText5;

	// Sliders
	private JSlider value1Slider;
	private JSlider value2Slider;
	private JSlider value3Slider;
	private JSlider value4Slider;
	private JSlider value5Slider;

	// Buttons
	private JButton value1P;
	private JButton value1M;
	private JButton value2P;
	private JButton value2M;
	private JButton value3P;
	private JButton value3M;
	private JButton value4P;
	private JButton value4M;
	private JButton value5P;
	private JButton value5M;

	//
	private ArmData arm;
	private ArmSerial armSerial;
	
	//
	private JComboBox<String> comPorts;

	private boolean updating = false;

	public ArmGUI() {
		super("Robot Arm Interface");

		// Window Listener (need to close GUI properly)
		this.addWindowListener(this);
		
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();

		arm = new ArmData();
		armSerial = new ArmSerial();

		createPanels();
		createConnection();
		createLabelsAndTextFields();
		createSliders();
		createButtons();

		updateValues();

		this.pack();
		this.setVisible(true);
	}

	private void createPanels() {
		// Creates Panels for the GUI
		topPanel = new JPanel(new FlowLayout());
		leftPanel = new JPanel(new GridLayout(0, 2));
		middlePanel = new JPanel(new GridLayout(0, 1));
		rightPanel = new JPanel(new GridLayout(0, 2));

		this.add(topPanel, BorderLayout.NORTH);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(middlePanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);
	}

	private void createConnection() {
		comPorts = new JComboBox<String>(armSerial.GetPortList());
		comPorts.addActionListener(this);
		
		topPanel.add(comPorts);
		
		JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		
		topPanel.add(connectButton);
	}
	
	private void createLabelsAndTextFields() {
		// Create Labels and TextFields on the leftside of the GUI

		int size = 3;
		
		JLabel valueLabel1 = new JLabel("Value:");
		JLabel valueLabel2 = new JLabel("Value:");
		JLabel valueLabel3 = new JLabel("Value:");
		JLabel valueLabel4 = new JLabel("Value:");
		JLabel valueLabel5 = new JLabel("Value:");

		valueText1 = new JTextField(size);
		valueText2 = new JTextField(size);
		valueText3 = new JTextField(size);
		valueText4 = new JTextField(size);
		valueText5 = new JTextField(size);

		valueText1.setEditable(false);
		valueText2.setEditable(false);
		valueText3.setEditable(false);
		valueText4.setEditable(false);
		valueText5.setEditable(false);

		leftPanel.add(valueLabel1);
		leftPanel.add(valueText1);
		leftPanel.add(valueLabel2);
		leftPanel.add(valueText2);
		leftPanel.add(valueLabel3);
		leftPanel.add(valueText3);
		leftPanel.add(valueLabel4);
		leftPanel.add(valueText4);
		leftPanel.add(valueLabel5);
		leftPanel.add(valueText5);
	}

	private void createSliders() {
		//create sliders for the GUI to manipulate the servos
		int min = 0;
		int max = 180;

		value1Slider = new JSlider(30, 150, arm.getValue1());
		value2Slider = new JSlider(min, max, arm.getValue2());
		value3Slider = new JSlider(min, max, arm.getValue3());
		value4Slider = new JSlider(min, max, arm.getValue4());
		value5Slider = new JSlider(min, max, arm.getValue5());

		middlePanel.add(value1Slider);
		middlePanel.add(value2Slider);
		middlePanel.add(value3Slider);
		middlePanel.add(value4Slider);
		middlePanel.add(value5Slider);

		value1Slider.addChangeListener(this);
		value2Slider.addChangeListener(this);
		value3Slider.addChangeListener(this);
		value4Slider.addChangeListener(this);
		value5Slider.addChangeListener(this);

	}

	private void createButtons() {
		//create buttons that increment or decrement the slider values
		
		value1P = new JButton("++");
		value1M = new JButton("--");
		value2P = new JButton("++");
		value2M = new JButton("--");
		value3P = new JButton("++");
		value3M = new JButton("--");
		value4P = new JButton("++");
		value4M = new JButton("--");
		value5P = new JButton("++");
		value5M = new JButton("--");

		rightPanel.add(value1M);
		rightPanel.add(value1P);
		rightPanel.add(value2M);
		rightPanel.add(value2P);
		rightPanel.add(value3M);
		rightPanel.add(value3P);
		rightPanel.add(value4M);
		rightPanel.add(value4P);
		rightPanel.add(value5M);
		rightPanel.add(value5P);

		value1M.addActionListener(this);
		value1P.addActionListener(this);
		value2M.addActionListener(this);
		value2P.addActionListener(this);
		value3M.addActionListener(this);
		value3P.addActionListener(this);
		value4M.addActionListener(this);
		value4P.addActionListener(this);
		value5M.addActionListener(this);
		value5P.addActionListener(this);
	}

	private void ButtonAction(int buttonNo, boolean direction) {
		//button listener for the +- buttons
		if (!updating) {
			if (direction) {
				switch (buttonNo) {
				case 1:
					arm.changeValue1(arm.getValue1() + 1);
					break;
				case 2:
					arm.changeValue2(arm.getValue2() + 1);
					break;
				case 3:
					arm.changeValue3(arm.getValue3() + 1);
					break;
				case 4:
					arm.changeValue4(arm.getValue4() + 1);
					break;
				case 5:
					arm.changeValue5(arm.getValue5() + 1);
					break;
				}
			} else {
				switch (buttonNo) {
				case 1:
					arm.changeValue1(arm.getValue1() - 1);
					break;
				case 2:
					arm.changeValue2(arm.getValue2() - 1);
					break;
				case 3:
					arm.changeValue3(arm.getValue3() - 1);
					break;
				case 4:
					arm.changeValue4(arm.getValue4() - 1);
					break;
				case 5:
					arm.changeValue5(arm.getValue5() - 1);
					break;
				}
			}
			updateValues();
			updating = false;
		}
	}

	private void SliderAction(int sliderNo, int sliderValue) {
		if (!updating) {
			//System.out.println("Slider " + sliderNo + "has been moved to " + sliderValue);
			switch (sliderNo) {
				case 1:
					arm.changeValue1(sliderValue);
					armSerial.send(sliderValue + 1000);
					break;
				case 2:
					arm.changeValue2(sliderValue);
					armSerial.send(sliderValue + 2000);
					break;
				case 3:
					arm.changeValue3(sliderValue);
					armSerial.send(sliderValue + 3000);
					break;
				case 4:
					arm.changeValue4(sliderValue);
					armSerial.send(sliderValue + 4000);
					break;
				case 5:
					arm.changeValue5(sliderValue);
					armSerial.send(sliderValue + 5000);
					break;
			}
			updateValues();
			updating = false;
		}

	}

	private void updateValues() {
		if (!updating) {
			valueText1.setText(arm.getValue1() + "");
			value1Slider.setValue(arm.getValue1());
			valueText2.setText(arm.getValue2() + "");
			value2Slider.setValue(arm.getValue2());
			valueText3.setText(arm.getValue3() + "");
			value3Slider.setValue(arm.getValue3());
			valueText4.setText(arm.getValue4() + "");
			value4Slider.setValue(arm.getValue4());
			valueText5.setText(arm.getValue5() + "");
			value5Slider.setValue(arm.getValue5());
			
			updating = false;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(value1P)) {
			ButtonAction(1, true);
		} else if (e.getSource().equals(value1M)) {
			ButtonAction(1, false);
		} else if (e.getSource().equals(value2P)) {
			ButtonAction(2, true);
		} else if (e.getSource().equals(value2M)) {
			ButtonAction(2, false);
		} else if (e.getSource().equals(value3P)) {
			ButtonAction(3, true);
		} else if (e.getSource().equals(value3M)) {
			ButtonAction(3, false);
		} else if (e.getSource().equals(value4P)) {
			ButtonAction(4, true);
		} else if (e.getSource().equals(value4M)) {
			ButtonAction(4, false);
		} else if (e.getSource().equals(value5P)) {
			ButtonAction(5, true);
		} else if (e.getSource().equals(value5M)) {
			ButtonAction(5, false);
		}
		else if(e.getActionCommand().equals("Connect")) {
			armSerial.connectPort(comPorts.getSelectedItem().toString());
		}
	}
	

	public void stateChanged(ChangeEvent e) {
		//if (!((JSlider) e.getSource()).getValueIsAdjusting()) {
			if (e.getSource().equals(value1Slider)) {
				SliderAction(1, value1Slider.getValue());
			} else if (e.getSource().equals(value2Slider)) {
				SliderAction(2, value2Slider.getValue());
			} else if (e.getSource().equals(value3Slider)) {
				SliderAction(3, value3Slider.getValue());
			} else if (e.getSource().equals(value4Slider)) {
				SliderAction(4, value4Slider.getValue());
			} else if (e.getSource().equals(value5Slider)) {
				SliderAction(5, value5Slider.getValue());
			}
		//}
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowClosing(WindowEvent e) {
		System.out.println("Closing GUI...");
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		System.out.println();
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String args[]) {
		new ArmGUI();
	}
}
