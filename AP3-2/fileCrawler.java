class fileCrawler {

	void main(String[] args) {
		
		// Print an error message if we don't have a valid input.
		if(args.length == 0) { 
			System.err.println("Usage: 'java –classpath . fileCrawler pattern [directory]'"); 	
		
		// We have valid input! Let's do our thing. 	
		} else {
			
			// Find the pattern, and determine whether it's a word or a BASH pattern.
			String pattern = args[0];
			if (pattern.contains("?")) {
				System.out.println("We found a '?'.");
			} else if(pattern.contains("*")) {
				System.out.println("We found a '*'.");
			} else if (pattern.contains("[")) {
				System.out.println("We found a '['.");
			} else {
				System.out.println("We didn't find a BASH pattern.");
			}


			for (int index = 1; index < args.length; index++) {
			//	
			}

		} 	
	}

}

