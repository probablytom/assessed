class fileCrawler {

	void main(String[] args) {
		
		// Print an error message if we don't have a valid input.
		if(args.length == 0) { 
			System.err.println("Usage: 'java –classpath . fileCrawler pattern [directory]'"); 	
		
		// We have valid input! Let's do our thing. 	
		} else {
		
			System.out.println("Let's do this.");
		
		} 	
	}

}
