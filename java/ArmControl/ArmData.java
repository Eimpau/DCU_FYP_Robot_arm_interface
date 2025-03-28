package ArmControl;

public class ArmData {
	private int value1;
	private int value2;
	private int value3;
	private int value4;
	private int value5;
		
	public ArmData() {
		this.value1 = 60;
		this.value2 = 180;
		this.value3 = 60;
		this.value4 = 90;
		this.value5 = 90;
		
	}
	
	public void changeValue1(int newValue) {
		this.value1 = newValue;
	}
	
	public void changeValue2(int newValue) {
		this.value2 = newValue;
	}
	
	public void changeValue3(int newValue) {
		this.value3 = newValue;
	}
	
	public void changeValue4(int newValue) {
		this.value4 = newValue;
	}
	
	public void changeValue5(int newValue) {
		this.value5 = newValue;
	}
	
	public int getValue1() {
		return value1;
	}
	
	public int getValue2() {
		return value2;
	}
	
	public int getValue3() {
		return value3;
	}
	
	public int getValue4() {
		return value4;
	}
	
	public int getValue5() {
		return value5;
	}
}
