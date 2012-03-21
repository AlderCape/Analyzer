package com.aldercape.internal.classparser;

import java.io.DataInputStream;
import java.io.IOException;

enum ConstantPoolType {
	Class(7) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort());
		}
	},
	FieldRef(9) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort(), in.readUnsignedShort());
		}
	},
	MethodRef(10) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort(), in.readUnsignedShort());
		}
	},
	InterfaceMethodRef(11) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort(), in.readUnsignedShort());
		}
	},
	String(8) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort());
		}
	},
	Integer(3) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readInt());
		}
	},
	Float(4) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readFloat());
		}
	},
	Long(5) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readLong());
		}
	},
	Double(6) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readDouble());
		}
	},
	NameAndType(12) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort(), in.readUnsignedShort());
		}

	},
	Utf8(1) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUTF());
		}
	},
	MethodHandle(15) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedByte(), in.readUnsignedShort());
		}
	},
	MethodType(16) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort());
		}
	},
	InvokeDynamic(18) {
		@Override
		public Constant consume(DataInputStream in) throws IOException {
			return new Constant(this, in.readUnsignedShort(), in.readUnsignedShort());
		}
	};

	private int value;

	ConstantPoolType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static ConstantPoolType getFor(int tag) {
		ConstantPoolType[] values = values();
		for (ConstantPoolType type : values) {
			if (type.getValue() == tag) {
				return type;
			}
		}
		throw new RuntimeException("Unknown constant pool type, " + tag);
	}

	public abstract Constant consume(DataInputStream in) throws IOException;
}