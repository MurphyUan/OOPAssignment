package ie.gmit.sw;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class KeyImpl implements Callable<Double>{
	
	private int key;
	private int numChars;
	private double score = 0;
	private ConcurrentHashMap<Integer, Float> mFreq;
	private ConcurrentHashMap<Integer, Integer> mAttr;

	public KeyImpl(int key, int numChars, ConcurrentHashMap<Integer, Float> mFreq, ConcurrentHashMap<Integer, Integer> mAttr) {
		this.key = key;
		this.numChars = numChars;
		this.mFreq = mFreq;
		this.mAttr = mAttr;
	}
	
	public Double calculateScore() {
		for(int i = 0; i < mAttr.size(); i++) {
			int cypherKey = ((key + i) % 95) + 32;
			int tOccur = mAttr.get(i+32);
			float eOccur = mFreq.get(cypherKey) * numChars;
			score += Math.pow(tOccur - eOccur, 2) / eOccur;
		}
		return score;
	}
	
	public Double decryptCypher() {
		for(Integer i : mAttr.keySet()) {
			int cypherKey = ((key + i) % 95)+ 32;
			int tOccur = mAttr.get(i);
			float eOccur = mFreq.get(cypherKey) * numChars;
			score += Math.pow(tOccur - eOccur, 2) / eOccur;
		}
		return score;
	}

	@Override
	public Double call() throws Exception {
		// TODO Auto-generated method stub
		return decryptCypher();
	}

}
