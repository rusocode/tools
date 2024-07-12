package _lab;

public class Args {

	public static void main(String[] args) {
		if (args.length == 1 && "throw".equals(args[0])) throw new IllegalStateException("Exception thrown from Args.");
	}

}
