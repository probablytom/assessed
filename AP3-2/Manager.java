import java.util.concurrent.*;
import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;


class Manager {


	String regex;
	static ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();
	static ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();
	static int noThreads;
	static ArrayList<Thread> threads = new ArrayList<Thread>();



	public void process(String args[]) {

		// Print an error message if we don't have a valid input.
		if(args.length == 0) {
			System.err.println("Usage: 'java -classpath fileCrawler pattern [directory]'");

		} else { // On with the show.
			

			this.regex = cvtPattern(args[0]);

			// Populate the work queue with the directories to process given.
			if (args.length == 1) {
				// Only this directory to search.
				addToWorkQueue(".");
			} else {
				for (int i = 1; i < args.length; i++) {
					addToWorkQueue(args[i]);
				}
			}

			// Get the maximum number of threads available.
			String noThreadsString = System.getenv("CRAWLER_THREADS");
			if (noThreadsString == null) {
				noThreads = 1;
			} else {
				try {
					noThreads = Integer.parseInt(noThreadsString);
				} catch (Exception e) {
					System.err.println("Invalid number of threads given; cannot convert to integer.");
					System.err.println("Assuming minimum (1) threads.");
					noThreads = 1;
				}
			}

			// Populate our list of threads with dead threads. 
			for (int i = 0; i < noThreads; i++) {
				threads.add(new Thread(new Worker()));
			}

			// Some flags to manage program flow.
			boolean stillToAdd = true;
			boolean firstRun = true;
			
			// Process the work queue.
			while (!workQueue.isEmpty() || !allThreadsDead() || firstRun ) {
				firstRun = false;
				Worker toAdd = new Worker( new File(workQueue.poll()) );
				
				// Wait until a thread is dead to run it with the code from this workQueue.poll().
				while (!anyAvailableThreads()) {
					continue;		
				}

				// Find the right thread and execute it with the new workQueue.poll(). So we don't do anythin
				for (int index = 0; index < noThreads && stillToAdd; index++) {
					if (!threads.get(index).isAlive()) {
						threads.remove(index);
						// Add a new thread to run a worker with the most recent workQueue.poll()
						Thread current = new Thread( toAdd );
						current.start();
						stillToAdd = false;
						try {
							current.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						threads.add( current );
					}
				}
				
				// Reset the adding variable so we know we're working with a new item from the queue next time around.
				stillToAdd = true;
			}

			// We've processed all of the directories and found each file, and stored in fileQueue.
			// Now, search through all of the files found and print if the file is a match.
			String current;
			while (!fileQueue.isEmpty()) {
				current = fileQueue.poll();
				boolean matched = Pattern.matches(this.regex, getFilename(current));
				if (matched) System.out.println(current);
			}

		}

	}

	public static void addToWorkQueue(String toAdd) {
		workQueue.add(toAdd);
	}

	public static void addToFileQueue(String toAdd) {
		fileQueue.add(toAdd);
	}

	// Returns whether ALL threads are dead.
	public static boolean allThreadsDead() {
		boolean allThreadsDead = true;
		// See if we can find a dead thread, update allThreadsDead if so. 
		for (int i = 0; i < noThreads; i++) {
			if ( threads.get(i).isAlive() ) {
				allThreadsDead = false;
			}
		}
		return allThreadsDead;
	}
	
	// Returns whether any threads are not alive.
	public static boolean anyAvailableThreads() {
		boolean anyAvailableThreads = false;
		for (int i = 0; i < noThreads; i++) {
			if (!threads.get(i).isAlive()) {
				anyAvailableThreads = true;
			}
		}
		return anyAvailableThreads;
	}
	
	
	
	// Returns the filename from a path.
	public String getFilename(String path) {
		return (new File(path)).getName();
	}

	/**
	 * Note: Code borrowed heavily from https://gist.github.com/yangls06/5464683. Modifications were then made to make this work as intended.
	 * If time permits, this will be entirely rewritten by Tom Wallis, as opposed to partially. 
	 * For as long as this note persists, some credit for this code is handed to the original authour.
	 */
	private String cvtPattern(String line) {
		line = line.trim();
		int strLen = line.length();
		StringBuilder sb = new StringBuilder(strLen);

		if (line.charAt(0) == '\'') {	// double quoting on Windows
			strLen--;
			line = line.substring(1);
		}
		if (line.endsWith("\'")) {
			strLen--;
			line = line.substring(0, line.length()-1);
		}

		boolean escaping = false;
		int inCurlies = 0;
		sb.append("^");
		for (char currentChar : line.toCharArray())
		{
			switch (currentChar)
			{
			case '*':
				if (escaping)
					sb.append("\\*");
				else
					sb.append(".*");
				escaping = false;
				break;
			case '?':
				if (escaping)
					sb.append("\\?");
				else
					sb.append('.');
				escaping = false;
				break;
			case '.':
			case '(':
			case ')':
			case '+':
			case '|':
			case '^':
			case '$':
			case '@':
			case '%':
				sb.append('\\');
				sb.append(currentChar);
				escaping = false;
				break;
			case '\\':
				if (escaping)
				{
					sb.append("\\\\");
					escaping = false;
				}
				else
					escaping = true;
				break;
			case '{':
				if (escaping)
				{
					sb.append("\\{");
				}
				else
				{
					sb.append('(');
					inCurlies++;
				}
				escaping = false;
				break;
			case '}':
				if (inCurlies > 0 && !escaping)
				{
					sb.append(')');
					inCurlies--;
				}
				else if (escaping)
					sb.append("\\}");
				else
					sb.append("}");
				escaping = false;
				break;
			case ',':
				if (inCurlies > 0 && !escaping)
				{
					sb.append('|');
				}
				else if (escaping)
					sb.append("\\,");
				else
					sb.append(",");
				break;
			default:
				escaping = false;
				sb.append(currentChar);
			}
		}
		sb.append("$");
		return sb.toString();
	}


}

