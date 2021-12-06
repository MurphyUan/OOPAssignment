package ie.gmit.sw;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class FrequencyImpl implements Callable<ConcurrentHashMap<Integer, Float>>{
	
	private BlockingQueue<String> queue;
	
	public FrequencyImpl(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
	public ConcurrentHashMap<Integer, Float> fillMap() throws InterruptedException {
		ConcurrentHashMap<Integer, Float> map = new ConcurrentHashMap<Integer, Float>();
		String s;
		if(queue.peek() == null)Thread.sleep(1);
		// Get the first element in the queue until the queue is empty
		while((s = queue.poll())!=null) {
			// Parse string to string array
			String[] temp = {s.substring(0, 1),s.substring(2)};
			//Input variables into the array
			map.put((int)temp[0].charAt(0), Float.parseFloat(temp[1]) * 0.01f);
		}
		System.out.println("Finished Filling Frequency Map");
		return map;
	}

	@Override
	public ConcurrentHashMap<Integer, Float> call() throws Exception {
		System.out.println("Starting Frequency Reads");
		return fillMap();
	}
	
}
