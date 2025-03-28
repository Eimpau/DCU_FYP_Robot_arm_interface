package GUITest;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.fazecast.jSerialComm.SerialPort;

import java.awt.Toolkit;
import javax.swing.JSlider;

public class SwingMain extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField ValueField1;
	private JTextField ValueField2;
	private JTextField ValueField3;
	private JTextField ValueField4;
	private JTextField ValueField5;
	private JComboBox<String> portList;
	private JTextArea LogArea;

	private SerialPort serialPort;

	private int value1 = 60;
	private int value2 = 180;
	private int value3 = 60;
	private int value4 = 90;
	private int value5 = 90;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain frame = new SwingMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SwingMain() {
		setTitle("Robot Arm Inteface");
		setResizable(false);
		setIconImage(
				Toolkit.getDefaultToolkit().getImage("C:\\Users\\eiman\\eclipse-workspace\\GUITest\\robotArmIcon.jpg"));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Port Number:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		portList = new JComboBox<String>();
		GridBagConstraints gbc_portList_1 = new GridBagConstraints();
		gbc_portList_1.insets = new Insets(0, 0, 5, 5);
		gbc_portList_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_portList_1.gridx = 2;
		gbc_portList_1.gridy = 0;
		contentPane.add(portList, gbc_portList_1);

		JButton ButtonConnect = new JButton("Connect");
		GridBagConstraints gbc_ButtonConnect = new GridBagConstraints();
		gbc_ButtonConnect.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonConnect.gridx = 4;
		gbc_ButtonConnect.gridy = 0;
		contentPane.add(ButtonConnect, gbc_ButtonConnect);
		ButtonConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectPort();
			}
		});

		createTextFields();

		createButtons();

		createLabels();

		createSliders();

		JLabel lblNewLabel_2 = new JLabel("Log:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 6;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		contentPane.add(scrollPane, gbc_scrollPane);

		LogArea = new JTextArea();
		scrollPane.setViewportView(LogArea);

		populatePortList();
	}

	private void createButtons() {

		JButton ButtonAdd1 = new JButton("++");
		ButtonAdd1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value1, true, 1);
			}
		});

		JButton ButtonAdd2 = new JButton("++");
		ButtonAdd2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value2, true, 2);
			}
		});

		JButton ButtonAdd3 = new JButton("++");
		ButtonAdd3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value3, true, 3);
			}
		});

		JButton ButtonAdd4 = new JButton("++");
		ButtonAdd4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value4, true, 4);
			}
		});

		JButton ButtonAdd5 = new JButton("++");
		ButtonAdd5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value5, true, 5);
			}
		});

		GridBagConstraints gbc_ButtonAdd1 = new GridBagConstraints();
		gbc_ButtonAdd1.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonAdd1.gridx = 4;
		gbc_ButtonAdd1.gridy = 1;
		contentPane.add(ButtonAdd1, gbc_ButtonAdd1);

		GridBagConstraints gbc_ButtonAdd2 = new GridBagConstraints();
		gbc_ButtonAdd2.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonAdd2.gridx = 4;
		gbc_ButtonAdd2.gridy = 2;
		contentPane.add(ButtonAdd2, gbc_ButtonAdd2);

		GridBagConstraints gbc_ButtonAdd3 = new GridBagConstraints();
		gbc_ButtonAdd3.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonAdd3.gridx = 4;
		gbc_ButtonAdd3.gridy = 3;
		contentPane.add(ButtonAdd3, gbc_ButtonAdd3);

		GridBagConstraints gbc_ButtonAdd4 = new GridBagConstraints();
		gbc_ButtonAdd4.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonAdd4.gridx = 4;
		gbc_ButtonAdd4.gridy = 4;
		contentPane.add(ButtonAdd4, gbc_ButtonAdd4);

		ValueField5 = new JTextField();
		ValueField5.setText(value5 + "");
		ValueField5.setHorizontalAlignment(SwingConstants.CENTER);
		ValueField5.setEditable(false);
		ValueField5.setColumns(10);
		GridBagConstraints gbc_valueField5 = new GridBagConstraints();
		gbc_valueField5.anchor = GridBagConstraints.WEST;
		gbc_valueField5.insets = new Insets(0, 0, 5, 5);
		gbc_valueField5.gridx = 1;
		gbc_valueField5.gridy = 5;
		contentPane.add(ValueField5, gbc_valueField5);

		JButton ButtonSub5 = new JButton("--");
		ButtonSub5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value5, false, 5);
			}
		});

		GridBagConstraints gbc_ButtonSub5 = new GridBagConstraints();
		gbc_ButtonSub5.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonSub5.gridx = 3;
		gbc_ButtonSub5.gridy = 5;
		contentPane.add(ButtonSub5, gbc_ButtonSub5);

		GridBagConstraints gbc_ButtonAdd5 = new GridBagConstraints();
		gbc_ButtonAdd5.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonAdd5.gridx = 4;
		gbc_ButtonAdd5.gridy = 5;
		contentPane.add(ButtonAdd5, gbc_ButtonAdd5);
	}

	private void createTextFields() {

		ValueField1 = new JTextField();
		ValueField1.setText(value1 + "");
		ValueField1.setEditable(false);
		ValueField1.setHorizontalAlignment(SwingConstants.CENTER);
		ValueField1.setColumns(10);
		GridBagConstraints gbc_valueField1 = new GridBagConstraints();
		gbc_valueField1.anchor = GridBagConstraints.WEST;
		gbc_valueField1.insets = new Insets(0, 0, 5, 5);
		gbc_valueField1.gridx = 1;
		gbc_valueField1.gridy = 1;
		contentPane.add(ValueField1, gbc_valueField1);
		JButton ButtonSub1 = new JButton("--");
		ButtonSub1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value1, false, 1);
			}
		});

		GridBagConstraints gbc_ButtonSub1 = new GridBagConstraints();
		gbc_ButtonSub1.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonSub1.gridx = 3;
		gbc_ButtonSub1.gridy = 1;
		contentPane.add(ButtonSub1, gbc_ButtonSub1);

		ValueField2 = new JTextField();
		ValueField2.setText(value2 + "");
		ValueField2.setHorizontalAlignment(SwingConstants.CENTER);
		ValueField2.setEditable(false);
		ValueField2.setColumns(10);
		GridBagConstraints gbc_valueField2 = new GridBagConstraints();
		gbc_valueField2.anchor = GridBagConstraints.WEST;
		gbc_valueField2.insets = new Insets(0, 0, 5, 5);
		gbc_valueField2.gridx = 1;
		gbc_valueField2.gridy = 2;
		contentPane.add(ValueField2, gbc_valueField2);

		JButton ButtonSub2 = new JButton("--");
		ButtonSub2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value2, false, 2);
			}
		});

		GridBagConstraints gbc_ButtonSub2 = new GridBagConstraints();
		gbc_ButtonSub2.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonSub2.gridx = 3;
		gbc_ButtonSub2.gridy = 2;
		contentPane.add(ButtonSub2, gbc_ButtonSub2);

		JLabel lblNewLabel_1_2 = new JLabel("Value:");
		GridBagConstraints gbc_lblNewLabel_1_2 = new GridBagConstraints();
		gbc_lblNewLabel_1_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1_2.gridx = 0;
		gbc_lblNewLabel_1_2.gridy = 3;
		contentPane.add(lblNewLabel_1_2, gbc_lblNewLabel_1_2);

		ValueField3 = new JTextField();
		ValueField3.setText(value3 + "");
		ValueField3.setHorizontalAlignment(SwingConstants.CENTER);
		ValueField3.setEditable(false);
		ValueField3.setColumns(10);
		GridBagConstraints gbc_valueField3 = new GridBagConstraints();
		gbc_valueField3.anchor = GridBagConstraints.WEST;
		gbc_valueField3.insets = new Insets(0, 0, 5, 5);
		gbc_valueField3.gridx = 1;
		gbc_valueField3.gridy = 3;
		contentPane.add(ValueField3, gbc_valueField3);

		JButton ButtonSub3 = new JButton("--");
		ButtonSub3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value3, false, 3);
			}
		});

		GridBagConstraints gbc_ButtonSub3 = new GridBagConstraints();
		gbc_ButtonSub3.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonSub3.gridx = 3;
		gbc_ButtonSub3.gridy = 3;
		contentPane.add(ButtonSub3, gbc_ButtonSub3);

		JLabel lblNewLabel_1_3 = new JLabel("Value:");
		GridBagConstraints gbc_lblNewLabel_1_3 = new GridBagConstraints();
		gbc_lblNewLabel_1_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1_3.gridx = 0;
		gbc_lblNewLabel_1_3.gridy = 4;
		contentPane.add(lblNewLabel_1_3, gbc_lblNewLabel_1_3);

		ValueField4 = new JTextField();
		ValueField4.setText(value4 + "");
		ValueField4.setHorizontalAlignment(SwingConstants.CENTER);
		ValueField4.setEditable(false);
		ValueField4.setColumns(10);
		GridBagConstraints gbc_valueField4 = new GridBagConstraints();
		gbc_valueField4.anchor = GridBagConstraints.WEST;
		gbc_valueField4.insets = new Insets(0, 0, 5, 5);
		gbc_valueField4.gridx = 1;
		gbc_valueField4.gridy = 4;
		contentPane.add(ValueField4, gbc_valueField4);

		JButton ButtonSub4 = new JButton("--");
		ButtonSub4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeValue(value4, false, 4);
			}
		});

		GridBagConstraints gbc_ButtonSub4 = new GridBagConstraints();
		gbc_ButtonSub4.insets = new Insets(0, 0, 5, 5);
		gbc_ButtonSub4.gridx = 3;
		gbc_ButtonSub4.gridy = 4;
		contentPane.add(ButtonSub4, gbc_ButtonSub4);

	}

	private void createLabels() {
		JLabel lblNewLabel_1 = new JLabel("Value:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Value:");
		GridBagConstraints gbc_lblNewLabel_1_1 = new GridBagConstraints();
		gbc_lblNewLabel_1_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1_1.gridx = 0;
		gbc_lblNewLabel_1_1.gridy = 2;
		contentPane.add(lblNewLabel_1_1, gbc_lblNewLabel_1_1);

		JLabel lblNewLabel_1_4 = new JLabel("Value:");
		GridBagConstraints gbc_lblNewLabel_1_4 = new GridBagConstraints();
		gbc_lblNewLabel_1_4.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1_4.gridx = 0;
		gbc_lblNewLabel_1_4.gridy = 5;
		contentPane.add(lblNewLabel_1_4, gbc_lblNewLabel_1_4);

	}

	private void createSliders() {
		JSlider slider1 = new JSlider();
		slider1.setMaximum(180);
		GridBagConstraints gbc_slider1 = new GridBagConstraints();
		gbc_slider1.insets = new Insets(0, 0, 5, 5);
		gbc_slider1.gridx = 2;
		gbc_slider1.gridy = 1;
		contentPane.add(slider1, gbc_slider1);

		JSlider slider2 = new JSlider();
		slider2.setMaximum(180);
		GridBagConstraints gbc_slider2 = new GridBagConstraints();
		gbc_slider2.insets = new Insets(0, 0, 5, 5);
		gbc_slider2.gridx = 2;
		gbc_slider2.gridy = 2;
		contentPane.add(slider2, gbc_slider2);

	}

	private void populatePortList() {
		SerialPort[] ports = SerialPort.getCommPorts();
		for (SerialPort port : ports) {
			portList.addItem(port.getSystemPortName());
		}
	}

	private void connectPort() {
		if (serialPort != null && serialPort.isOpen()) {
			serialPort.closePort();
		}

		String selectedPort = (String) portList.getSelectedItem();
		serialPort = SerialPort.getCommPort(selectedPort);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

		if (serialPort.openPort()) {
			LogArea.append("Connected to " + selectedPort + "\n");

			// Start listening for incoming messages
			printLog();
		} else {
			LogArea.append("Failed to open port.\n");
		}
	}

	private void printLog() {
		new Thread(() -> {
			try (Scanner dataScanner = new Scanner(serialPort.getInputStream())) {
				while (dataScanner.hasNextLine()) {
					String message = dataScanner.nextLine();
					LogArea.append("Received: " + message + "\n");

				}
			} catch (Exception ex) {
				LogArea.append("Error reading data: " + ex.getMessage() + "\n");
			}
		}).start();
	}

	private void changeValue(int value, boolean mode, int num) {
		if (mode == true) {
			switch (num) {
			case 1:
				value1 = value1 + 2;
				ValueField1.setText(value1 + "");
				sendAction(value1 + 1000);
				break;
			case 2:
				value2 = value2 + 2;
				ValueField2.setText(value2 + "");
				sendAction(value2 + 2000);
				break;
			case 3:
				value3 = value3 + 2;
				ValueField3.setText(value3 + "");
				sendAction(value3 + 3000);
				break;
			case 4:
				value4 = value4 + 2;
				ValueField4.setText(value4 + "");
				sendAction(value4 + 4000);
				break;
			case 5:
				value5 = value5 + 2;
				ValueField5.setText(value5 + "");
				sendAction(value5 + 5000);
				break;
			default:
				return;
			}
		} else {
			switch (num) {
			case 1:
				value1 = value1 - 2;
				ValueField1.setText(value1 + "");
				sendAction(value1 + 1000);
				break;
			case 2:
				value2 = value2 - 2;
				ValueField2.setText(value2 + "");
				sendAction(value2 + 2000);
				break;
			case 3:
				value3 = value3 - 2;
				ValueField3.setText(value3 + "");
				sendAction(value3 + 3000);
				break;
			case 4:
				value4 = value4 - 2;
				ValueField4.setText(value4 + "");
				sendAction(value4 + 4000);
				break;
			case 5:
				value5 = value5 - 2;
				ValueField5.setText(value5 + "");
				sendAction(value5 + 5000);
				break;
			default:
				return;
			}
		}
	}

	private void sendAction(int dataValue) {
		if (serialPort != null && serialPort.isOpen()) {
			try {
				serialPort.getOutputStream().write((dataValue + "\n").getBytes());
				serialPort.getOutputStream().flush();
			} catch (NumberFormatException ex) {
				LogArea.append("Invalid input, please enter an integer.\n");
			} catch (Exception ex) {
				LogArea.append("Error sending data: " + ex.getMessage() + "\n");
			}
		} else {
			LogArea.append("Port not open. Connect first.\n");
		}
	}
}
