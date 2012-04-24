package testdata.enums;

public enum EnumWithMethod {

	First(1), Second(2), Third(2);

	private int value;

	EnumWithMethod(int value) {
		this.value = value;
	}

	public int getVal() {
		return value;
	}

}
