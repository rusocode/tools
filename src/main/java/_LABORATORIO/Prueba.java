package _LABORATORIO;

import java.nio.ByteBuffer;

public class Prueba {

	public static void main(String[] args) {

		ByteBuffer buf = ByteBuffer.allocate(10);

		buf.put("abcdefhijk".getBytes());

		buf.flip();
		
		while (buf.hasRemaining()) {
			System.out.print(buf.get());
		}

	}

}