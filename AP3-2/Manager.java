import java.util.concurrent.*;
import java.io.*;
import java.util.regex.*;


class Manager {


	Pattern regex;
	Matcher matcher;
	ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();
	ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();

	/*public void process(String[] args) {


		// Print an error message if we don't have a valid input.
		if(args.length == 0) { 
			System.err.println("Usage: 'java -classpath fileCrawler pattern [directory]'"); 	

			// We have valid input! Let's do our thing. 	
		} else {

			String noOfThreadsEnv = System.getenv("CRAWLER_THREADS");
			if (noOfThreadsEnv != null) {
				Integer noOfThreads = Integer.parseInt( noOfThreadsEnv );
			} else {
				int noOfThreads = 1; // Default single thread.
			}


			// Find the pattern, and determine whether it's a word or a BASH pattern.
			String pattern = args[0];
			this.regex = cvtPattern(pattern);

			// Populate each directory we were passed. 
			for (int index = 1; index < args.length; index++) {
				this.workQueue.add(args[index]);
				while(!this.workQueue.isEmpty()) {
					processDirectory(workQueue.poll());
				}
			}

		} 	
	}*/

	
	public void process(String[] args) {
		
		// Print an error message if we don't have a valid input.
		if(args.length == 0) {
			System.err.println("Usage: 'java -classpath fileCrawler pattern [directory]'");
		
		} else { // On with the show.
			
			this.regex = Pattern.compile( cvtPattern(args[0]) );
			
			// Seperate out files and directories.
			
			if (args.length == 1) {
				populateQueue(new File("."));
			}
			
			System.out.println(this.regex.pattern());
			
			for (int index = 1; index < args.length; index++) {
				this.workQueue.add(args[index]);
				while(!this.workQueue.isEmpty()) {
					populateQueue( new File(this.workQueue.poll()) );
				}
			}
			
			
			// Process all of the files we have.
			String current;
			while (!this.fileQueue.isEmpty()) {
				current = this.fileQueue.poll();
//				Matcher matcher = this.regex.matches(current);
				boolean matched = Pattern.matches(cvtPattern(args[0]), current);
				if (matched) System.out.println(current);
			}
		}
		
		
	}

	/*
	public void processDirectory( String name ) {
		try {
			File file = new File(name);	// create a File object
			if (file.isDirectory()) {	// a directory - could be symlink
				String entries[] = file.list();
				if (entries != null) {	// not a symlink
					for (String entry : entries ) {
						if (entry.compareTo(".") == 0)
							continue;
						if (entry.compareTo("..") == 0)
							continue;
						this.workQueue.add(name + "/" + entry);
						//processDirectory(name+"/"+entry);
					}
				}
			} else {   // We found a file, deal with it using the inputted regex.

				if (file.getName().matches(this.regex)) {System.out.println(file.getName());}

			}
		} catch (Exception e) {
			System.err.println("Error processing "+name+": "+e);
		}
	}*/
	
	


	/**
	 * Note: Code copied from https://gist.github.com/yangls06/5464683.
	 * If time permits, this will be written by Tom Wallis. 
	 * For as long as this note persists, credit for this code is handed to the original authour.
	 */
	private String cvtPattern(String line) {
		line = line.trim();
		int strLen = line.length();
		StringBuilder sb = new StringBuilder(strLen);
		// Remove beginning and ending * globs because they're useless
		if (line.startsWith("*") || line.startsWith("'"))
		{
			line = line.substring(1);
			strLen--;
		}
		if (line.endsWith("*") || line.endsWith("'"))
		{
			line = line.substring(0, strLen-1);
			strLen--;
		}
		boolean escaping = false;
		int inCurlies = 0;
		sb.append('/');
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
		sb.append('/');
		return sb.toString();
	}
	
	public void populateQueue(File toPopulate) {
		if (toPopulate.isDirectory()) {
			String entries[] = toPopulate.list();
			for (String entry : entries) {
				// If the entry represents another directory, add it to the workQueue.
				File fEntry = new File(entry);
				if (fEntry.isDirectory()) {
					this.workQueue.add(entry);
				} else {
					this.fileQueue.add(entry);
				}
			}
		} else {
			System.out.println("Things are worse than you feared.");
		}
	}


}
