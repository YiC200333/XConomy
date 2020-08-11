package me.Yi.XConomy.Task;

import java.io.ByteArrayOutputStream;
import net.md_5.bungee.api.config.ServerInfo;

public class SendMessTaskB implements Runnable {
	private final ByteArrayOutputStream stream;
	private final ServerInfo s;

	public SendMessTaskB(ServerInfo s, ByteArrayOutputStream stream) {
		this.stream = stream;
		this.s = s;
	}

	@Override
	public void run() {
		s.sendData("xconomy:aca", stream.toByteArray());
	}
}