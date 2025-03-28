import serial
import serial.tools.list_ports

class SerialConnection:
    def __init__(self):
        # create variables to hold the connection and reading status
        self.connection = None
        self.is_reading = False

    # Return a list of available COM ports
    def list_ports(self):
        ports = serial.tools.list_ports.comports()
        return [port.device for port in ports]

    # Establish a connection to the specified port
    def connect(self, port):
        try:
            self.connection = serial.Serial(port, 9600, timeout=1) # connect to selected port at 9600 baud rate
            self.is_reading = True # set reading status to True
            return True
        except serial.SerialException as e:
            print(f"Failed to connect: {e}")
            return False

    # Continuously read from the serial port in a separate thread
    def read_from_port(self, status_callback):
        if not self.connection or not self.connection.is_open:
            return
        
        # Check if the connection is already reading
        self.is_reading = True
        while self.is_reading and self.connection.is_open:
            try:
                # Read a line from the serial port
                if self.connection.in_waiting > 0:
                    line = self.connection.readline().decode('utf-8').strip()
                    status_callback(line)
            except serial.SerialException as e:
                status_callback(f"Error reading from port: {e}")
                break

    # Send a command to the serial port
    def send_command(self, command):
        # Check if the connection is open before sending a command
        if self.connection and self.connection.is_open:
            try:
                self.connection.write((command + "\n").encode('utf-8')) # send command with newline
            except serial.SerialException as e:
                print(f"Error sending data: {e}")

    # Close the serial connection
    def close(self):
        if self.connection and self.connection.is_open:
            self.is_reading = False
            self.connection.close()
