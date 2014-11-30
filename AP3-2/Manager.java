import java.util.concurrent.*;
import java.io.*;

class Manager {


	String regex = "";
	ConcurrentLinkedQueue<String> workQueue = new ConcurrentLinkedQueue<String>();

	public void process(String[] args) {
		
		DirectoryTree dTree = new DirectoryTree();	
		
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
			this.regex = Regex.cvtPattern(pattern);


			// Populate each directory we were passed. 
			for (int index = 1; index < args.length; index++) {
			 	this.workQueue.add(args[index]);
				while(!this.workQueue.isEmpty()) {
					processDirectory(workQueue.poll());
				}
			}

		} 	
	}


   public void processDirectory( String name ) {
    try {
         File file = new File(name);	// create a File object
	 if (file.isDirectory()) {	// a directory - could be symlink
    String entries[] = file.list();
	    if (entries != null) {	// not a symlink
               System.out.println(name);// print out the name
               for (String entry : entries ) {

                	if (entry.compareTo(".") == 0)
                 		continue;
               		if (entry.compareTo("..") == 0)
                 		continue;
			   		this.workQueue.add(name + "/" + entry);
            		processDirectory(name+"/"+entry);
				}
        }
    } else {   // We found a file, deal with it using the inputted regex.
		//System.out.printf("%s: %s ", file.getName(), this.regex);
		//System.out.print(file.getName().matches(this.regex));
		//System.out.println();
		if (file.getName().matches(this.regex)) {System.out.println(file.getName());}

	}
    } catch (Exception e) {
    	System.err.println("Error processing "+name+": "+e);
    }
	}

}
