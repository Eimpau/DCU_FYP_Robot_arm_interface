# import the neccessary libraries
from PyQt6 import uic
from PyQt6.QtWidgets import QApplication

# Select the UI file to compile
Form, Window = uic.loadUiType("robot_arm_gui.ui")
# The compiled UI file will be saved the same name of the UI file with the extension changed to .py

# The Application will be created here but its functionality wont work
app = QApplication([])
window = Window()
form = Form()
form.setupUi(window)
window.show()
app.exec()
