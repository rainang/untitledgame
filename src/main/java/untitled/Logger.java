package untitled;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static java.util.logging.Level.*;

public class Logger
{
	private static final Map<String, Logger> loggers = new HashMap<>();
	private static final Formatter formatter = new LogFormatter();
	private static final Handler fileHandler;
	private static final Handler consoleHandler;
	
	static
	{
		consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(ALL);
		consoleHandler.setFormatter(formatter);
		
		String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		File logDir = new File("log/" + formattedDate);
		
		if (!logDir.exists())
		{
			System.out.println(String.format("Creating log directory at %s", logDir.getAbsolutePath()));
			if (!logDir.mkdirs())
				throw new RuntimeException("Unable to make log directory");
		}
		
		try
		{
			String pattern = formattedDate + "_%g.log";
			fileHandler = new FileHandler(new File(logDir, pattern).getPath(), 0, 3);
			fileHandler.setLevel(ALL);
			fileHandler.setFormatter(formatter);
		} catch (IOException e)
		{
			throw new RuntimeException("Failed to open log file", e);
		}
	}
	
	public static Logger get(String name)
	{
		return get(name, ALL);
	}
	
	public static Logger get(String name, Level level)
	{
		if (name == null || "".equals(name))
			throw new IllegalArgumentException("Illegal logger name: \"" + name + "\"");
		
		Logger logger = loggers.get(name);
		if (logger != null)
			return logger;
		
		java.util.logging.Logger javaLogger = java.util.logging.Logger.getLogger(name);
		javaLogger.setLevel(level);
		javaLogger.setUseParentHandlers(false);
		javaLogger.addHandler(fileHandler);
		javaLogger.addHandler(consoleHandler);
		
		logger = new Logger(javaLogger);
		loggers.put(name, logger);
		
		return logger;
	}
	
	private final java.util.logging.Logger javaLogger;
	
	private Logger(java.util.logging.Logger javaLogger)
	{
		this.javaLogger = javaLogger;
	}
	
	public void print(Level level, Throwable thrown, String message)
	{
		javaLogger.log(level, message, thrown);
	}
	
	public void print(Level level, Throwable thrown, String format, Object... args)
	{
		javaLogger.log(level, String.format(format, args), thrown);
	}
	
	public void print(Level level, String message)
	{
		javaLogger.log(level, message);
	}
	
	public void print(Level level, String format, Object... args)
	{
		javaLogger.log(level, String.format(format, args));
	}
	
	public void severe(String message)
	{
		print(SEVERE, message);
	}
	
	public void severe(String format, Object... args)
	{
		print(SEVERE, format, args);
	}
	
	public void severe(Throwable thrown, String message)
	{
		print(SEVERE, thrown, message);
	}
	
	public void severe(Throwable thrown, String format, Object... args)
	{
		print(SEVERE, thrown, format, args);
	}
	
	public void warning(String message)
	{
		print(WARNING, message);
	}
	
	public void warning(String format, Object... args)
	{
		print(WARNING, format, args);
	}
	
	public void warning(Throwable thrown, String message)
	{
		print(WARNING, thrown, message);
	}
	
	public void warning(Throwable thrown, String format, Object... args)
	{
		print(WARNING, thrown, format, args);
	}
	
	public void info(String message)
	{
		print(INFO, message);
	}
	
	public void info(String format, Object... args)
	{
		print(INFO, format, args);
	}
	
	public void config(String message)
	{
		print(CONFIG, message);
	}
	
	public void config(String format, Object... args)
	{
		print(CONFIG, format, args);
	}
	
	public void fine(String message)
	{
		print(FINE, message);
	}
	
	public void fine(String format, Object... args)
	{
		print(FINE, format, args);
	}
	
	public void finer(String message)
	{
		print(FINER, message);
	}
	
	public void finer(String format, Object... args)
	{
		print(FINER, format, args);
	}
	
	public void finest(String message)
	{
		print(FINEST, message);
	}
	
	public void finest(String format, Object... args)
	{
		print(FINEST, format, args);
	}
	
	private static final class LogFormatter extends Formatter
	{
		private static final String LINE_SEPARATOR = System.lineSeparator();
		private static final Format timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		@Override
		public String format(LogRecord record)
		{
			StringBuilder builder = new StringBuilder();
			
			builder.append(timeFormat.format(record.getMillis()));
			builder.append(" [");
			builder.append(Thread.currentThread()
								 .getName());
			builder.append("/");
			builder.append(record.getLoggerName());
			builder.append("/");
			builder.append(record.getLevel()
								 .getLocalizedName());
			builder.append("] ");
			builder.append(formatMessage(record));
			builder.append(LINE_SEPARATOR);
			
			Throwable thrown = record.getThrown();
			if (thrown != null)
			{
				StringWriter writer = new StringWriter();
				thrown.printStackTrace(new PrintWriter(writer));
				builder.append(writer.toString());
			}
			
			return builder.toString();
		}
	}
}
