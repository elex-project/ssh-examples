package kr.pe.elex.ssh;

import com.jcraft.jsch.*;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

public class SecureShell implements Closeable {

	private final Session session;

	public SecureShell(String host, int port, String user, String password) throws JSchException {
		JSch jSch = new JSch();
		session = jSch.getSession(user, host, port);
		session.setPassword(password);
		session.setUserInfo(new UserInfo() {
			@Override
			public String getPassphrase() {
				return null;
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public boolean promptPassword(String message) {
				return false;
			}

			@Override
			public boolean promptPassphrase(String message) {
				return false;
			}

			@Override
			public boolean promptYesNo(String message) {
				System.out.println(message);
				// user type 'yes'
				return true;
			}

			@Override
			public void showMessage(String message) {
				System.out.println(message);
			}
		});
		session.connect();
	}

	@Override
	public void close() {
		session.disconnect();
	}

	public ChannelShell shell(InputStream is, OutputStream os) throws JSchException {
		ChannelShell channel = (ChannelShell) session.openChannel("shell");
		channel.setInputStream(is);
		channel.setOutputStream(os);
		channel.setAgentForwarding(true);
		channel.setPtyType("vt100");
		channel.connect();

		return channel;
	}

	public ChannelExec exec(String command) throws JSchException {
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(command);
		channel.setInputStream(null);
		channel.setErrStream(System.err);
		channel.connect();
		return channel;
	}

	public void portForwardingL(int portL, String hostR, int portR) throws JSchException {
		session.setPortForwardingL(portL, hostR, portR);
	}

}
