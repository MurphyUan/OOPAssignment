package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeImpl implements Callable<ConcurrentHashMap<Integer, Integer>>{
	
	private BlockingQueue<String> queue;

	public AttributeImpl(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
	public ConcurrentHashMap<Integer, Integer> fillMap() throws InterruptedException{
		// Variable Initialisation
		ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<Integer, Integer>();
		String s;
		int key, value;
		// Check that the queue has an entry
		if(queue.peek()==null)Thread.sleep(1);
		// Loop through each string in the queue
		while((s = queue.poll()) != null) {
			// Loop through each character in string
			for(char c : s.toCharArray()) {
				// convert character to integer variant
				key = (int)c;
				// check if the map already has character
				if(map.containsKey(key)) 
					// Increment Value by 1
					value = map.get(key) + 1;	
				// key does not exist
				else 
					// Initialise Value
					value = 1;
				// Append Key Value Pair to ConcurrentHashMap
				map.put(key, value);
			}
		}
		System.out.println("Finished Filling Attribute Map");
		return map;
	}

	@Override
	public ConcurrentHashMap<Integer, Integer> call() throws Exception {
		System.out.println("Starting Attribute Reads");
		return fillMap();
	}
	
}
