package ie.gmit.sw;
import java.awt.FileDialog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

public class Runner {

	public static void displayMenu() {
		System.out.println("\n***** Cypher Cracker System *****\n"
				+ "1) Enter Cyphertext\n"
				+ "2) Specify a text file\n"
				+ "3) Customisations\n"
				+ "4) Quit\n"
				+ "Select Option [1-4]>\n");
	}
	
	public static String getFileGUI() {
		JFrame jf = new JFrame();
		FileDialog fd = new FileDialog(jf);
		jf.setVisible(true);
		fd.setVisible(true);
		String filePath = fd.getFiles()[0].getAbsolutePath();
		jf.dispose();
		return filePath;
	}
	
	public static void go() {
		AttributeImpl attribute;
		FrequencyImpl frequency;
		Scanner scan = new Scanner(System.in);
		String loadFile = null, saveFile = null, frequencyFile = null;
		int choice = -1;
		
		while(choice != 4) {
			displayMenu();
			choice = scan.nextInt();
			switch(choice) {
				case 1:{
					System.out.println("Please input file path for CypherText:\n");
					
					System.out.println("Please input file path for Frequency Table:\n");
					//frequency = new FrequencyImpl(frequencyFile = scan.next());
					
					break;
				}
				case 2:{
					//Enter a text file from folder
					System.out.println("Please input file path for save file:\n");
					saveFile = scan.nextLine();
					break;
				}
				case 3:{
					//Customization Menu
					break;
				}
				case 4:{
					System.out.println("End of Application");
					break;
				}
				default:{
					//All Other Values read in
					System.out.println("Illegal Expression Encountered\n");
					break;
				}
			}
		}
		scan.close();
	}
	
	public static void test() throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newFixedThreadPool(15);
		
		BlockingQueue<String> q1 = new LinkedBlockingQueue<>();
		
		ConcurrentHashMap<Integer, Float> mFreq = new ConcurrentHashMap<Integer, Float>();
		ConcurrentHashMap<Integer, Integer> mAttr = new ConcurrentHashMap<Integer, Integer>();
		
		executor.submit(new FileReaderImpl(q1, getFileGUI()));
		Future<ConcurrentHashMap<Integer, Float>> fFreq = executor.submit(new FrequencyImpl(q1));
		
		mFreq = fFreq.get();
		
		executor.submit(new FileReaderImpl(q1, getFileGUI()));
		Future<ConcurrentHashMap<Integer, Integer>> fAttr = executor.submit(new AttributeImpl(q1));
		
		mAttr = fAttr.get();
		
		int sumValues = mAttr.values().stream().mapToInt(i -> i).sum();
		
		Collection<Future<Double>> fCol = new ArrayList<Future<Double>>();
		List<Double> col = new ArrayList<Double>();
		
		for(int i = 0; i < mFreq.size(); i++) {
			Future<Double> dub = executor.submit(new KeyImpl(i, sumValues, mFreq, mAttr));
			fCol.add(dub);
		}
		
		double lowest = -1;
		for(Future<Double> f : fCol) {
			double temp = f.get();
			if(lowest == -1)lowest = temp;
			else if(temp < lowest)lowest = temp;
			col.add(temp);
			//System.out.println(temp);
		}
		
		System.out.println(lowest);
		
		System.out.println(col.indexOf(lowest));
		
		executor.shutdown();
	}
	
	public static void main(String[] args){
		try {
			test();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
