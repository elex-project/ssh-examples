package kr.pe.elex.ssh;

import com.elex_project.abraxas.IOz;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class SecureShellTest {

	private static final String host = "192.168.0.1";
	private static final String user = "elex";
	private static final String password = "test";
	private static final int port = 22;

	@Test @Ignore
	void shell() throws JSchException {
		SecureShell ssh = new SecureShell(host, port, user, password);

		ChannelShell shell = ssh.shell(System.in, System.out);

		// ???
	}

	@Test
	void exec() throws JSchException, IOException {
		SecureShell ssh = new SecureShell(host, port, user, password);

		ChannelExec channel = ssh.exec("ls -al");
		String out = IOz.readStringFrom(channel.getInputStream(),"\n");
		System.out.println(out);

		if (channel.isClosed()){
			channel.disconnect();
		}
		ssh.close();
	}

	@Test
	void scpTo() throws JSchException, IOException {
		String filename = "";
		SecureShell ssh = new SecureShell(host, port, user, password);

		ChannelExec channel = ssh.exec("scp -p -t "+ filename);
		InputStream is = channel.getInputStream();
		OutputStream os = channel.getOutputStream();

		// ???
	}
}
