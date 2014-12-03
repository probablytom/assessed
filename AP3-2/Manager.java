import java.util.concurrent.*;
import java.io.*;
import java.util.regex.*;


class Manager {


	String regex;
	ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();
	ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<String>();

	
	
	public void process(String[] args) {
		
		// Print an error message if we don't have a valid input.
		if(args.length == 0) {
			System.err.println("Usage: 'java -classpath fileCrawler pattern [directory]'");
		
		} else { // On with the show.
			
			this.regex = cvtPattern(args[0]);
			
			// Seperate out files and directories.
			if (args.length == 1) {
				try {
					populateQueue(new File("."));
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			 
			
			for (int index = 1; index < args.length; index++) {
				this.workQueue.add(args[index]);
				while(!this.workQueue.isEmpty()) {
					try {
						populateQueue( new File(this.workQueue.poll()) );
					} catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}
			}
			
			System.out.println(this.regex);
			
			
			// Process all of the files we have.
			String current;
			while (!this.fileQueue.isEmpty()) {
				current = this.fileQueue.poll();
//				Matcher matcher = this.regex.matches(current);
				boolean matched = Pattern.matches(this.regex, current);
				if (matched) System.out.println(current);
			}
		}
		
		
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
	
	public void populateQueue(File toPopulate) throws Exception {
		if (toPopulate.isDirectory()) {
			String entries[] = toPopulate.list();
			for (String entry : entries) {
				// If the entry represents another directory, add it to the workQueue.
				File fEntry = new File(toPopulate.getAbsolutePath() + "/" + entry);
				boolean fEntryDir = fEntry.isDirectory();
				if (fEntry.isDirectory()) {
					this.workQueue.add(fEntry.getAbsolutePath());
				} else {
					this.fileQueue.add(entry);
				}
			}
		} else {
			throw new Exception(); // This should NEVER happen.
		}
	}


}

