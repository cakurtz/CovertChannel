import java.io.*;
import java.util.*;

public class CovertChannel {
	static File file;
	static File log;
	static File output;
	static FileWriter logWrite;
	static FileWriter outWrite;
	static BufferedWriter logBuff;
	static BufferedWriter outBuff;
	static boolean verbose;
	
	public static void main(String[] args) throws FileNotFoundException {
		FileReader curFile;
		BufferedReader curData;
		String lineData;
		String holder;
		char charTemp;
		long start;
		long end;
		
		init();
		
		// Parse through file and perform various operations
		if(args.length > 0) {
			if(args[0].equals("v")) {
				verbose = true;
				file = new File(args[1]);
				output = new File(args[1] + ".out");
				output.delete();
				output = new File(args[1] + ".out");
			}
			else {
				file = new File(args[0]);
				output = new File(args[0] + ".out");
				output.delete();
				output = new File(args[0] + ".out");
			}
			
			// Allows for overwriting 
			log = new File("log.out");
			log.delete();
			log = new File("log.out");
			
			try {
				curFile = new FileReader(file);
				curData = new BufferedReader(curFile);
				
				start = System.currentTimeMillis();
				
				while((lineData = curData.readLine()) != null){
					Scanner ls = new Scanner(lineData);
					while(ls.hasNext()) {
						holder = ls.next();
						for(int i = 0; i < holder.length(); i++) {
							charTemp = holder.charAt(i);
							runSteps(charTemp);
						}
						if(ls.hasNext()) {
							charTemp = ' ';
							runSteps(charTemp);
						}
						
					}
					ls.close();
					charTemp = '\n';
					runSteps(charTemp);
				}
				end = System.currentTimeMillis();
				System.out.println("Time: " + (end - start));
				
				curData.close();
				curFile.close();
			}
			catch (IOException e){
				System.out.println("Cannot find file.");
			}
		}
		else
			throw new FileNotFoundException("Need a file to parse!");
	}
	
	/* Initializes managers and creates subjects and objects */
	private static void init(){
		ReferenceMonitor.ObjectManager.createObjectManager();
		ReferenceMonitor.createReferenceMonitor();
		SecureSubject.createSecureSubject();
		
		verbose = false;
	}
	
	/* Executes the instructions for the character */
	private static void runSteps(char charTemp) {
		int ascii;
		int bitTemp;
		String printing;
		String byteString;
		
		ascii = (int) charTemp;
		byteString = Integer.toBinaryString(ascii);
		byteString = pad(byteString);
		for(int j = 0; j < byteString.length(); j++) {
			bitTemp = byteString.charAt(j);
			if(bitTemp == 48) {
				if(verbose){
					printing = "create hal current \n";
					logPrint(printing);
				}
				ReferenceMonitor.ObjectManager.create("hal", "current");
			}
			doActions("lyle");
		}
	}
	
	/* Runs the subject's, or lyle's, actions */
	public static void doActions(String lyle) {
		InstructionObject inst;
		String printing;
		String object;
		int readVal;
		
		object = "current";
		if(verbose) {
			printing = "create lyle current \n";
			logPrint(printing);
		}
		ReferenceMonitor.ObjectManager.create("lyle", "current");
		
		printing = "write lyle current 1 \n";
		if(verbose) 
			logPrint(printing);
		inst = new InstructionObject(printing);
		ReferenceMonitor.execute(inst);
		
		printing = "read lyle current \n";
		if(verbose) 
			logPrint(printing);
		inst = new InstructionObject(printing);
		readVal = ReferenceMonitor.execute(inst);
		SecureSubject.newRead = readVal;
		SecureSubject.buffer[SecureSubject.bufferCount - 1] = readVal;
		SecureSubject.bufferCount--;
		
		if(verbose) {
			printing = "destroy lyle current \n";
			logPrint(printing);
		}
		ReferenceMonitor.ObjectManager.destroy(lyle, object);
		
		if(verbose) {
			printing = "run lyle current \n";
			logPrint(printing);
		}
		run(lyle);
	}
	
	/* Runs the subject's actions */
	public static void run(String lyle) {
		String binary;
		char toPrint;
		String printing;
		int asciiVal;
		
		SecureSubject.buffer[SecureSubject.bufferCount] = 
				SecureSubject.newRead;
		if(SecureSubject.bufferCount == 0) {
			binary = "";
			for(int i = 0; i < SecureSubject.buffer.length; i++) {
				binary += SecureSubject.buffer[i];
			}
			asciiVal = binaryToAscii(binary);
			toPrint = (char) asciiVal;
			printing = Character.toString(toPrint);
			try {
				outWrite = new FileWriter(output, true);
			} 
			catch (IOException e) {
				System.out.println("Writer failure.");
			}
			outBuff = new BufferedWriter(outWrite);
			try {
				outBuff.write(printing);
				outBuff.close();
			} 
			catch (IOException e) {
				System.out.println("Print to output file failed");
			}
			
			SecureSubject.buffer = new int[8];
			SecureSubject.bufferCount = 8;
		}
	}
	
	/* Returns the ASCII value of the character transmitted */
	private static int binaryToAscii(String binary) {
		int binaryAscii;
		int temp;
		int val;
		
		binaryAscii = 0;
		for(int i = 0; i < binary.length(); i++) {
			temp = binary.charAt(i);
			if(temp == 49) {
				val = (int) Math.pow(2, i);
				binaryAscii += val;
			}
		}
		
		return binaryAscii;
	}
	
	/* Prints to the log if verbose is a parameter to the program */
	public static void logPrint(String printing) {
		try {
			logWrite = new FileWriter(log, true);
		} catch (IOException e1) {
			System.out.println("Writer failure.");
		}
		logBuff = new BufferedWriter(logWrite);
		try {
			logBuff.write(printing);
			logBuff.close();
		} catch (IOException e) {
			System.out.println("Print to log failed.");
		}
	}
	
	/* Pads the byte string with 0's if necessary */
	public static String pad(String byteString) {
		String holder = byteString;
		String temp;
		int length = holder.length();
		while(length < 8) {
			temp = "0" + holder;
			holder = temp;
			length++;
		}
		return holder;
	}
	
	/* Class to manage subjects and stored data */
	private static class SecureSubject {
		/* Subject to value mapping */
		static Map<String, Integer> subjToVal;
		static int[] buffer;
		static int bufferCount;
		static int newRead;
		
		private static void createSecureSubject() {
			SecureSubject.subjToVal = new HashMap<String, Integer>();
			buffer = new int[8];
			bufferCount = 8;
			
			ReferenceMonitor.SecurityLevel low = 
					ReferenceMonitor.SecurityLevel.LOW;
			ReferenceMonitor.SecurityLevel high = 
					ReferenceMonitor.SecurityLevel.HIGH;
			
			// Creation of subjects
			SecureSubject.subjToVal.put("lyle", 0);
			ReferenceMonitor.subjToLabel.put("lyle", low);
			SecureSubject.subjToVal.put("hal", 0);
			ReferenceMonitor.subjToLabel.put("hal", high);
		}
	}
	
	/* Manages the security levels and allows or denies operations from
	 * subjects to objects */
	private static class ReferenceMonitor {
		/* Subject to label mapping */
		static Map<String, SecurityLevel> subjToLabel;
		
		/* Object to label mapping */
		static Map<String, SecurityLevel> objToLabel; 
		
		/* Initializes the security level mappings */
		private static void createReferenceMonitor() {
			subjToLabel = new HashMap<String, SecurityLevel>();
			objToLabel = new HashMap<String, SecurityLevel>();
		}
		
		/* Possible security levels */
		private enum SecurityLevel {
			HIGH, LOW
		}
		
		/* Determines whether to execute requested instruction */
		private static int execute(InstructionObject current) {
			String curSub = current.subject;
			String curObj = current.object;
			
			boolean execute = compareLabels(current);
			if(execute && current.instrType.equals("read"))
				return ObjectManager.read(curSub, curObj);
			else if(execute && current.instrType.equals("write"))
				return ObjectManager.write(curSub, curObj, current.value);
			return 0;
		}
		
		/* Executes a BadInstruction by letting the user know it was an illegal
		 * or incorrect instruction */
		private static void doBad(BadInstruction badInst) {
			System.out.println(badInst.value);
		}
		
		/* Compares subject security level to object security level */
		private static boolean compareLabels(InstructionObject current) {
			SecurityLevel cur = subjToLabel.get(current.subject);
			SecurityLevel other = objToLabel.get(current.object);
			
			if(current.instrType.equals("read") && 
					(cur.equals(SecurityLevel.HIGH) && 
							other.equals(SecurityLevel.HIGH) || 
					 cur.equals(SecurityLevel.HIGH) && 
			   		   		other.equals(SecurityLevel.LOW) || 
			   		 cur.equals(SecurityLevel.LOW) && 
			   		   		other.equals(SecurityLevel.LOW)))
				return true;
			else if(current.instrType.equals("write") && 
					(cur.equals(SecurityLevel.HIGH) && 
							other.equals(SecurityLevel.HIGH) || 
					 cur.equals(SecurityLevel.LOW) && 
					 		other.equals(SecurityLevel.HIGH) || 
					 cur.equals(SecurityLevel.LOW) && 
					 		other.equals(SecurityLevel.LOW)))
				return true;
			else
				return false;
		}
		
		/* Object manager nested for protection by ReferenceMonitor */
		private static class ObjectManager {
			/* Object to value mapping */
			static Map<String, Integer> objToVal;
			
			/* Initializes object mapping to values */
			private static void createObjectManager() {
				objToVal = new HashMap<String, Integer>();
			}
			
			/* Reads the current value of the desired object */
			public static int read(String curSub, String curObj) {
				return objToVal.get(curObj);
			}
			
			/* Writes val to the desired object */
			public static int write(String curSub, String curObj, int val) {
				objToVal.replace(curObj, val);
				return 1;
			}
			
			/* Creates an object with the specified name if object of that name
			 * does not exist. Otherwise, does nothing. */
			public static void create (String curSub, String curObj) {
				if(!objToVal.containsKey(curObj)) {
					SecurityLevel ofObj = subjToLabel.get(curSub);
					int initial = 0;
					
					objToVal.put(curObj, initial);
					objToLabel.put(curObj, ofObj);
				}
			}
			
			/* Destroys an object of the specified name */
			public static void destroy (String curSub, String curObj) {
				if(objToVal.containsKey(curObj) && 
				  (subjToLabel.get(curSub).equals(objToLabel.get(curObj)) || 
				  (subjToLabel.get(curSub).equals(SecurityLevel.LOW) && 
				   objToLabel.get(curObj).equals(SecurityLevel.HIGH)))) {
					
					objToVal.remove(curObj);
					objToLabel.remove(curObj);
				}
			}
		}
	}
	
	/* InstructionObject that holds parsed line data */
	private static class InstructionObject {
		String instrType;
		String subject;
		String object;
		int value;
		
		/* Creates a InstructionObject object */
		private InstructionObject(String data) {
			Scanner lineScanner = new Scanner(data);
			instrType = lineScanner.next().toLowerCase();
			if(instrType.equals("write") || instrType.equals("read")){
				if(lineScanner.hasNext()){
					subject = lineScanner.next().toLowerCase();
					if(!SecureSubject.subjToVal.containsKey(subject)) 
						instrType = "bad";
					if(lineScanner.hasNext()){
						object = lineScanner.next().toLowerCase();
						if(!lineScanner.hasNext() && instrType.equals("write")
						   || !ReferenceMonitor.ObjectManager.
						   		objToVal.containsKey(object))
							instrType = "bad";
						if(lineScanner.hasNext() && instrType.equals("write")){
							value = Integer.parseInt(lineScanner.next());
							if(lineScanner.hasNext())
								instrType = "bad";
						}
					}
					else
						instrType = "bad";
				}
				else
					instrType = "bad";
			}
			else
				instrType = "bad";
			lineScanner.close();
		}
	}
	
	/* Object used when an illegal or incorrect instruction is made */
	private static class BadInstruction {
		String value; 
		
		/* Creates a BadInstruction object */
		private BadInstruction() {
			value = "Bad Instruction";
		}
	}
	
}
