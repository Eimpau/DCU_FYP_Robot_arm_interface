import pygame
from PySide6.QtCore import QTimer, QObject, Signal, QTime

class GamepadHandler(QObject):
    # pyQt signals to communicate with the UI 
    connection_status_changed = Signal(bool)  # Emits True if connected, False if disconnected
    joystick_moved = Signal(str, list, int)  # Emits directions & step size
    button_action = Signal(str)  # Emits "Open" or "Close" for the gripper with hold status
    trigger_action = Signal(str, int)  # Emits "Up" or "Down" for the trigger with step size


    # Initialize the gamepad handler and set up polling
    def __init__(self):
        super().__init__()

        # pygame initialization to use the gamepad
        pygame.init()
        pygame.joystick.init()

        self.joystick = None
        self.controller_connected = False  # Track connection status
        self.deadzone = 0.3  # Minimum input threshold to register movement
        self.cooldown = 100  # Cooldown time (ms) to prevent excessive updates
        self.last_move_time = QTime.currentTime()

        # Timer to poll the gamepad state
        self.timer = QTimer()
        self.timer.timeout.connect(self.poll_gamepad)
        self.timer.start(20)  # Poll every 20ms

        # Attempt to auto-connect a controller on startup
        self.reconnect_controller()

    # Checks the gamepad state and emits movement signals if necessary
    def poll_gamepad(self):
        if not self.joystick or not self.controller_connected:
            return  # No active controller, do nothing

        try:
            pygame.event.pump()  # Process input events

            # Read joystick axes
            axis_lx = self.joystick.get_axis(0)  # Left joystick X
            axis_ly = self.joystick.get_axis(1)  # Left joystick Y
            
            axis_rx = self.joystick.get_axis(2)  # Right joystick X
            axis_ry = self.joystick.get_axis(3)  # Right joystick Y
            
            triggerL = self.joystick.get_axis(4)  # Left trigger (up)
            triggerR = self.joystick.get_axis(5)  # Right trigger (down)

            # Determine movement directions and step size
            directionsL, step_sizeL = self.get_joystick_action(axis_lx, axis_ly)
            directionsR, step_sizeR = self.get_joystick_action(axis_rx, axis_ry)

            # Determine trigger actions and step size
            actionL, Tstep_sizeL = self.get_trigger_action(triggerL, "Up")
            actionR, Tstep_sizeR = self.get_trigger_action(triggerR, "Down")

            # Emit movement signal if valid input and cooldown period passed
            if (directionsR or directionsL) and self.can_send_input():
                self.joystick_moved.emit("Base", directionsL, step_sizeL)
                self.joystick_moved.emit("Wrist", directionsR, step_sizeR)
                self.last_move_time = QTime.currentTime()
            elif self.joystick.get_button(0) and self.can_send_input():  # A button (Gripper Open)
                self.button_action.emit("Close")
                self.last_move_time = QTime.currentTime()
            elif self.joystick.get_button(1) and self.can_send_input():  # B button (Gripper Close)
                self.button_action.emit("Open")
                self.last_move_time = QTime.currentTime()
            elif actionL and self.can_send_input():
                self.trigger_action.emit(actionL, Tstep_sizeL)
                self.last_move_time = QTime.currentTime()
            elif actionR and self.can_send_input():
                self.trigger_action.emit(actionR, Tstep_sizeR)
                self.last_move_time = QTime.currentTime()

        except pygame.error:
            # Handle disconnection
            print("Controller disconnected!")
            self.controller_connected = False
            self.connection_status_changed.emit(False)

    # Get movement directions and step size based on joystick input
    def get_joystick_action(self, axis_x, axis_y):
        directions = []

        # Get left/right movement
        if abs(axis_x) > self.deadzone:
            if axis_x < 0:
                directions.append("Left")
            else:
                directions.append("Right")

        # Get up/down movement
        if abs(axis_y) > self.deadzone:
            if axis_y < 0:
                directions.append("Up")
            else:
                directions.append("Down")

        # Scale step size based on joystick intensity (max step size = 10)
        intensity = max(abs(axis_x), abs(axis_y))
        step_size = max(1, int(intensity * 10))

        return directions, step_size

    # Get trigger action and step size based on trigger input
    def get_trigger_action(self, triggerAxis, direction):
        trigger = max(0, (triggerAxis + 1) / 2)  # Normalize from [-1,1] to [0,1]
        
        if trigger > self.deadzone:
            step_size = max(1, int(trigger * 10))  # Scale step size (1-10)
            return direction, step_size
        
        return [], 0  # Return nothing if trigger is not pressed
        
        
    # Checks if enough time has passed since the last input 
    def can_send_input(self):
        return self.last_move_time.msecsTo(QTime.currentTime()) > self.cooldown

    # Manually reconnects the game controller
    def reconnect_controller(self):
        pygame.joystick.quit()  # Reset joystick module
        pygame.joystick.init()

        # Check for available controllers
        if pygame.joystick.get_count() > 0:
            self.joystick = pygame.joystick.Joystick(0)
            self.joystick.init()
            self.controller_connected = True
            print(f"Reconnected: {self.joystick.get_name()}")
            self.connection_status_changed.emit(True)
        else:
            self.joystick = None
            self.controller_connected = False
            print("No controller found.")
            self.connection_status_changed.emit(False)
