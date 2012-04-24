package testdata.enums;

public enum EnumWithField {

	First(1), Second(2), Third(2);

	private int value;

	EnumWithField(int value) {
		this.value = value;
	}

}
