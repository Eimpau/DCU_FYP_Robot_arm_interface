package ArmControl;

import java.util.Scanner;
import com.fazecast.jSerialComm.SerialPort;

public class ArmSerial {
	private SerialPort serialPort;

	public ArmSerial() {

	}

	public String[] GetPortList() {
		String[] portsList = new String[SerialPort.getCommPorts().length];
		int i = 0;
		for (SerialPort port : SerialPort.getCommPorts()) {
			portsList[i] = port.getSystemPortName();
			i++;
		}
		return portsList;
	}

	public void connectPort(String selectedPort) {
		if (serialPort != null && serialPort.isOpen()) {
			serialPort.closePort();
		}

		// String selectedPort = (String) portList.getSelectedItem();
		serialPort = SerialPort.getCommPort(selectedPort);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

		if (serialPort.openPort()) {
			System.out.println("Connected to " + selectedPort + "\n");

			// Start listening for incoming messages
			recieve();
		} else {
			System.out.println("Failed to open port.\n");
		}
	}

	private void recieve() {
		new Thread(() -> {
			try (Scanner dataScanner = new Scanner(serialPort.getInputStream())) {
				while (dataScanner.hasNextLine()) {
					String message = dataScanner.nextLine();
					System.out.println("Received: " + message + "\n");

				}
			} catch (Exception ex) {
				System.out.println("Error reading data: " + ex.getMessage() + "\n");
			}
		}).start();
	}

	public void send(int dataValue) {
		if (serialPort != null && serialPort.isOpen()) {
			try {
				serialPort.getOutputStream().write((dataValue + "\n").getBytes());
				serialPort.getOutputStream().flush();
			} catch (NumberFormatException ex) {
				System.out.println("Invalid input, please enter an integer.\n");
			} catch (Exception ex) {
				System.out.println("Error sending data: " + ex.getMessage() + "\n");
			}
		} else {
			System.out.println("Port not open. Connect first.\n");
		}
	}
}
