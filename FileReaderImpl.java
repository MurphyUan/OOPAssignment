package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class FileReaderImpl implements Runnable{
	
	BlockingQueue<String> queue;
	String filePath;

	public FileReaderImpl(BlockingQueue<String> queue, String filePath) {
		this.queue = queue;
		this.filePath = filePath;
	}
	
	public boolean readFile() {
		// Variable initialisation
		boolean bool = false;
		String next;
		// Read file specified
			try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.filePath), "UTF-8"))){
				// Loop through the file read
				while((next = br.readLine()) != null) {
					// Offer the last string read in from file
					while(queue.offer(next) == false)
						// Wait until there is space in the queue for more strings
						if(queue.offer(next) == true)
							// Exit the loop as we successfully offered the latest string
							break;
				}
				// Successfully read through the file
				bool = true;
			}catch(Exception e) {
				// Unsuccessfully read through the file
				e.printStackTrace();
			}
			// Return the success of the operation
			return bool;
	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		System.out.println("Started Read File");
		readFile();
		System.out.println("Successfully Read File");
	}
}
