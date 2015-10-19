package launchserver.command.handler;

import java.io.IOException;

import jline.console.ConsoleReader;
import launcher.helper.LogHelper;
import launchserver.LaunchServer;
import org.fusesource.jansi.Ansi;

public final class JLineCommandHandler extends CommandHandler {
	private final ConsoleReader reader;

	public JLineCommandHandler(LaunchServer server) throws IOException {
		super(server);

		// Set reader
		reader = new ConsoleReader();
		reader.setExpandEvents(false);

		// Replace writer
		LogHelper.removeStdOutput();
		LogHelper.addOutput(new JLineOutput());
	}

	@Override
	public void bell() throws IOException {
		reader.beep();
	}

	@Override
	public void clear() throws IOException {
		reader.clearScreen();
	}

	@Override
	public String readLine() throws IOException {
		return reader.readLine();
	}

	private final class JLineOutput implements LogHelper.Output {
		private final String ANSI_RESET = LogHelper.JANSI ?
			Ansi.ansi().reset().toString() : "\u0027[m";

		@Override
		public void println(String message) {
			try {
				reader.println(ConsoleReader.RESET_LINE + message + ANSI_RESET);
				reader.drawLine();
				reader.flush();
			} catch (IOException ignored) {
				// Ignored
			}
		}
	}
}