package testdata.enums;

public enum EnumWithAbstractMethod {

	First {

		@Override
		public int getVal() {
			return 1;
		}
	},
	Second {

		@Override
		public int getVal() {
			return 2;
		}
	},
	Third {

		@Override
		public int getVal() {
			return 3;
		}
	};

	public abstract int getVal();

}
