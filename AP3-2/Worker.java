import java.io.File;
import java.util.regex.Pattern;


public class Worker implements Runnable{

	File fileToWork;

	/**
	 * @param args
	 */
	public Worker(File fileToWork) {
		this.fileToWork = fileToWork;
	}

	public Worker() {
		this.fileToWork = null;
	}

	// Main function for each Worker thread. Processes a directory.
	@Override
	public void run() {
		if (this.fileToWork == null) {
			// We're just catching dumy threads to simplify lower logic down below. 
			// This means we can populate Manager.threads with dead threads, because this will exit as soon as it begins.
		}
		if (this.fileToWork.isDirectory()) {
			String entries[] = this.fileToWork.list();
			for (String entry : entries) {

				// If the entry represents another directory, add it to the workQueue.
				File fEntry = new File(this.fileToWork.getAbsolutePath() + "/" + entry);
				boolean fEntryDir = fEntry.isDirectory();
				if (fEntry.isDirectory()) {
					Manager.addToWorkQueue(fEntry.getAbsolutePath());
				} else {
					Manager.addToFileQueue(fEntry.getAbsolutePath());
				}

			}

		} else if (!this.fileToWork.isDirectory()) {
			//He's a file, add him to the file queue.
			Manager.addToFileQueue(this.fileToWork.getAbsolutePath());
		}
	}


}
