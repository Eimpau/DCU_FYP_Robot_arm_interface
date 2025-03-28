import sys
import threading
from PySide6.QtWidgets import QApplication, QMainWindow, QMessageBox

# Import the custom modules
from robot_arm_gui_ui import Ui_MainWindow
from serialcomm import SerialConnection
from controller import GamepadHandler

class MyApp(QMainWindow, Ui_MainWindow):
    def __init__(self):
        super().__init__()
        self.setupUi(self) # load the gui
        self.serial_conn = SerialConnection()
        
        # Connecting a button click to a function
        self.connectButton.clicked.connect(self.connect_to_port)
        self.refreshButton.clicked.connect(self.refresh_ports)
        
        # Connect the spinbox values to a function
        self.axisSpinBox.valueChanged.connect(self.axis_changed)
        self.axisSpinBox_2.valueChanged.connect(self.axis_changed)
        self.axisSpinBox_3.valueChanged.connect(self.axis_changed)
        self.axisSpinBox_4.valueChanged.connect(self.axis_changed)
        self.axisSpinBox_5.valueChanged.connect(self.axis_changed)
        self.axisSpinBox_6.valueChanged.connect(self.axis_changed)
        
        # Connect the menu bar actions to functions
        self.exitAction.triggered.connect(self.close) # Close the application
        self.aboutAction.triggered.connect(self.show_about_dialog) # Show some info about the application
        
        # Populate ports list
        self.refresh_ports()
        
        # Button listeners for simple control
        # Base Group
        self.baseUp.clicked.connect(lambda: self.on_button_click("Base", "Up"))
        self.baseDown.clicked.connect(lambda: self.on_button_click("Base", "Down"))
        self.baseLeft.clicked.connect(lambda: self.on_button_click("Base", "Left"))
        self.baseRight.clicked.connect(lambda: self.on_button_click("Base", "Right"))
        
        # Elbow Group
        self.elbowUp.clicked.connect(lambda: self.on_button_click("Elbow", "Up"))
        self.elbowDown.clicked.connect(lambda: self.on_button_click("Elbow", "Down"))

        # Wrist Group
        self.wristUp.clicked.connect(lambda: self.on_button_click("Wrist", "Up"))
        self.wristDown.clicked.connect(lambda: self.on_button_click("Wrist", "Down"))
        self.rotClk.clicked.connect(lambda: self.on_button_click("Wrist", "Clockwise"))
        self.rotAntiClk.clicked.connect(lambda: self.on_button_click("Wrist", "Anti-Clockwise"))
        self.gripOpen.clicked.connect(lambda: self.on_button_click("Gripper", "Open"))
        self.gripClose.clicked.connect(lambda: self.on_button_click("Gripper", "Close"))
        
        # GamePad Handler
        self.gamepad = GamepadHandler()
        self.gamepad.connection_status_changed.connect(self.on_connection_status_changed)
        self.gamepad.joystick_moved.connect(self.handle_joystick_input)  # Connect signal
        self.reconnectControllerButton.clicked.connect(self.on_reconnect_button_clicked)
        self.gamepad.button_action.connect(self.handle_button_input)
        self.gamepad.trigger_action.connect(self.handle_trigger_input)

        # Program is ready
        self.statusBar().showMessage("Ready")
 
    # Create a simple dialog with the about text   
    def show_about_dialog(self):
        about_text = "This is a simple application to control a 5-axis robot arm."
        QMessageBox.information(self, "About", about_text)       
    
    # Refresh and list available serial ports
    def refresh_ports(self):
        self.comboBox.clear()
        ports = self.serial_conn.list_ports()
        if ports:
            self.comboBox.addItems(ports)
        else:
            self.comboBox.addItem("No ports found")
            
    # Connect to the selected serial port.
    def connect_to_port(self):
        selected_port = self.comboBox.currentText()
        if "No ports found" in selected_port:
            self.statusLabel.setText(f"Status: Connection Error")
            self.statusBar().showMessage("Connection Error: No available ports")
            QMessageBox.warning(self, "Connection Error", "No available ports to connect.")
            return

        if self.serial_conn.connect(selected_port):
            self.statusLabel.setText(f"Status: {selected_port} Connected")
            self.statusBar().showMessage(f"Connected to {selected_port}")
            # Start reading in a separate thread, doesnt seem to work properly
            threading.Thread(target=self.serial_conn.read_from_port, args=(self.status_update,), daemon=True).start()
        else:
            self.statusLabel.setText(f"Status: Connection Failed")
            self.statusBar().showMessage("Connection Failed")
            QMessageBox.critical(self, "Connection Failed", "Could not connect to the selected port.")
        
    # Display received data from the serial port.
    def status_update(self, message):
        print(f"Received: {message}")
        
    # Send the command to the serial port when slides are changed
    def axis_changed(self, value):
        sender = self.sender()  # Get the spin box that sent the signal
        axis_map = {
            self.axisSpinBox: "6", # Base
            self.axisSpinBox_2: "1", # Shoulder
            self.axisSpinBox_3: "2", # Elbow
            self.axisSpinBox_4: "3", # Lower Arm
            self.axisSpinBox_5: "4", # Wrist
            self.axisSpinBox_6: "5", # Gripper
        }
        axis = axis_map.get(sender, "Unknown")  # Get the axis name
        command = f"{axis}{value}" # Get the command 
        
        # Send the command to console and serial port
        print(f"Sending: {command}")
        self.serial_conn.send_command(command)

    # Ensure serial connection is closed when exiting.
    def closeEvent(self, event):
        self.serial_conn.close()
        event.accept()
        
    # Get the values of all sliders
    def getSliderValues(self):
        return self.axisSpinBox.value(), self.axisSpinBox_2.value(), self.axisSpinBox_3.value(), self.axisSpinBox_4.value(), self.axisSpinBox_5.value(), self.axisSpinBox_6.value()
        
    def updateSliderValue(self, slider, value):    
        match slider:
            case 1:
                self.axisSpinBox.setValue(value)
            case 2:
                self.axisSpinBox_2.setValue(value)
            case 3:
                self.axisSpinBox_3.setValue(value)
            case 4:
                self.axisSpinBox_4.setValue(value)
            case 5:
                self.axisSpinBox_5.setValue(value)
            case 6:
                self.axisSpinBox_6.setValue(value)
            case _:
                print("Invalid slider number")
    
    # Handle button presses, allowing step size customization.
    def on_button_click(self, group, buttonType, step_size=None):
        if step_size is None:
            step_size = self.stepSize.value()  # Default to UI step size if none provided
        
        SliderValues = self.getSliderValues()
        axis = None
        value = None
        spinbox = None  # Track which spinbox corresponds to the axis

        if group == "Base":
            if buttonType == "Up":
                axis = "1"
                value = SliderValues[1] - step_size
                spinbox = self.axisSpinBox_2
            elif buttonType == "Down":
                axis = "1"
                value = SliderValues[1] + step_size
                spinbox = self.axisSpinBox_2
            elif buttonType == "Left":
                axis = "6"
                value = SliderValues[0] + step_size
                spinbox = self.axisSpinBox
            elif buttonType == "Right":
                axis = "6"
                value = SliderValues[0] - step_size
                spinbox = self.axisSpinBox
        elif group == "Elbow":
            if buttonType == "Up":
                axis = "2"
                value = SliderValues[2] - step_size
                spinbox = self.axisSpinBox_3
            elif buttonType == "Down":
                axis = "2"
                value = SliderValues[2] + step_size
                spinbox = self.axisSpinBox_3
        elif group == "Wrist":
            if buttonType == "Clockwise":
                axis = "4"
                value = SliderValues[4] - step_size
                spinbox = self.axisSpinBox_5
            elif buttonType == "Anti-Clockwise":
                axis = "4"
                value = SliderValues[4] + step_size
                spinbox = self.axisSpinBox_5
            elif buttonType == "Up":
                axis = "3"
                value = SliderValues[3] - step_size
                spinbox = self.axisSpinBox_4
            elif buttonType == "Down":
                axis = "3"
                value = SliderValues[3] + step_size
                spinbox = self.axisSpinBox_4
        elif group == "Gripper":
            if buttonType == "Open":
                axis = "5"
                value = SliderValues[5] + step_size
                spinbox = self.axisSpinBox_6
            elif buttonType == "Close":
                axis = "5"
                value = SliderValues[5] - step_size
                spinbox = self.axisSpinBox_6

        if spinbox is not None:
            spinbox.blockSignals(True)
            spinbox.setValue(value)
            spinbox.blockSignals(False)

        if axis is not None and value is not None:
            print(f"Sending: {axis}{value}")
            self.serial_conn.send_command(f"{axis}{value}")

    # Joystick input, handles joystick movement by triggering UI buttons.
    def handle_joystick_input(self, type, directions, step_size):
        for direction in directions:
            if type == "Wrist":
                if direction == "Left":
                    self.on_button_click("Wrist", "Clockwise", step_size)
                elif direction == "Right":
                    self.on_button_click("Wrist", "Anti-Clockwise", step_size)
                else:
                    self.on_button_click("Wrist", direction, step_size)
            elif type == "Base":
                self.on_button_click("Base", direction, step_size)
    
    # specific function to handle gripper mechanism         
    def handle_button_input(self, type):
        self.on_button_click("Gripper", type, 5)
    
    # Handles trigger presses for elbow movement.
    def handle_trigger_input(self, direction, step_size):
        if direction == "Up":
            self.on_button_click("Elbow", "Up", step_size)
        elif direction == "Down":
            self.on_button_click("Elbow", "Down", step_size)
    
    # Reconnect button for the gamepad
    def on_reconnect_button_clicked(self):
        self.gamepad.reconnect_controller()

    # check gamepad connection status
    def on_connection_status_changed(self, connected):
        if connected:
            self.controllerStatus.setText("Connected")
        else:
            self.controllerStatus.setText("Disconnected")

# Run the application
if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MyApp()
    window.show()
    sys.exit(app.exec())
