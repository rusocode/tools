package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class HTTPClient {

	public static final String GET_REQUEST = "GET / HTTP/1.1\n";

	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "www.kth.se";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 80;

		String hostHeader = "Host: " + host + "\n\n";

		WritableByteChannel out = Channels.newChannel(System.out);

		try {

			SocketChannel channel = SocketChannel.open(new InetSocketAddress(host, port));

			ByteBuffer buffer = ByteBuffer.wrap(GET_REQUEST.getBytes());
			channel.write(buffer);
			buffer = ByteBuffer.wrap(hostHeader.getBytes());
			channel.write(buffer);
			buffer = ByteBuffer.allocate(1024);

			while (buffer.hasRemaining() && channel.read(buffer) != -1) {
				buffer.flip();
				out.write(buffer);
				buffer.clear();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}
}
