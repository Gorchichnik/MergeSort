import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Sort {

	public static void main(String[] args) {
		ArrayList<String> inputFileNames = new ArrayList<String>();
		//default values setting
		boolean isItInt = false;   
		boolean isItUp = true;    
		boolean isItSorted = true; 
		
		for(int i = 0; i < args.length; i++) {
			//parameters checking 
			if(args[i].charAt(0) == '-' & args[i].length() == 2) {
				if(args[i].charAt(1) == 'i') {
					isItInt= true;
				}else if(args[i].charAt(1) == 'd') {
					isItUp = false;
				}else if(args[i].charAt(1) == 'n') {
					isItSorted = false;
				}
			//file names adding
			}else if(args[i].length() > 4){
				String end = Character.toString(args[i].charAt( args[i].length() - 4)) + Character.toString(args[i].charAt( args[i].length() - 3)) + Character.toString(args[i].charAt( args[i].length() - 2)) + Character.toString(args[i].charAt( args[i].length() - 1));
				if(end.equals(".txt")) {
					inputFileNames.add(args[i]);
				}
			}
		}
		if(inputFileNames.size() != 0) {
			sortFiles(inputFileNames, isItInt, isItUp, isItSorted);
		}else {
			System.out.println("There is no input or output files!");
		}
	}
	
	public static <T> void sortFiles(ArrayList<String> inputFileNames, boolean isItInt, boolean isItUp, boolean isItSorted) {
			ArrayList<ArrayList<T>> arrListOfList;
			try {
				arrListOfList = readingFiles(inputFileNames, isItInt);
				//sorting of original files if they are not sorted
				if(!isItSorted) {
					arrListOfList = sortOriginalFiles(arrListOfList, isItInt, isItUp);
				}
				String outputFileName = (String) inputFileNames.get(0);
				//sorting of files between themselves
				ArrayList<T> finalList = sort(arrListOfList, isItUp);
				writeFile(finalList, outputFileName);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
	}
	
	public static <T> void writeFile(ArrayList<T> finalList, String outputFileName) throws FileNotFoundException {
		File file = new File(outputFileName);
		PrintWriter pw = new PrintWriter(file);
		for(int i = 0; i < finalList.size(); i++) {
			pw.println(finalList.get(i));	
		}
		pw.close();
		System.out.println("File has been wrote!");
	}
	
	public static <T> ArrayList<ArrayList<T>> sortOriginalFiles(ArrayList<ArrayList<T>> arrListOfList, boolean isItInt, boolean isItUp){
		for(int i = 0; i < arrListOfList.size(); i++) {
			arrListOfList.set(i, sortOriginal( arrListOfList.get(i), 0, arrListOfList.get(i).size()-1 , isItUp));
		}
		return arrListOfList;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<ArrayList<T>> readingFiles(ArrayList<String> inputFileName, boolean isItInt) throws FileNotFoundException {
		ArrayList<ArrayList<T>> returnedArrList = new ArrayList<ArrayList<T>>();
		
		for(int i = 1; i < inputFileName.size(); i++) {
			    File file=new File(inputFileName.get(i) );
				ArrayList<T> arrList = new ArrayList<T>();
				Scanner sc = new Scanner(file);
				
				while(sc.hasNextLine()) {
					if(isItInt) {
						try {
							arrList.add( (T) (Integer.valueOf(sc.nextLine())) );
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("File format error");
						} catch (ClassCastException e) {
					    	e.printStackTrace();
					    }
					}else {
						try {
							arrList.add( (T) sc.nextLine() );
						} catch (ClassCastException e) {
							e.printStackTrace();
						}
					}
				}
				sc.close();
				returnedArrList.add(arrList);
		}
		
		return returnedArrList;
	}
	
	public static <T> ArrayList<T> sort( ArrayList<ArrayList<T>> arrListOfList, boolean isItUp) {
		ArrayList<T> returnedArrList;
		
		int nextInd = arrListOfList.size()/2;
		if(nextInd > 0) {
			ArrayList<ArrayList<T>> devidedArr = new ArrayList<ArrayList<T>>();
			for(int i = 0; i < nextInd; i++) {
				devidedArr.add(arrListOfList.get(i));
			}
			ArrayList<T> firstArr = sort(devidedArr, isItUp);
			devidedArr.clear();
			for(int i = nextInd; i < arrListOfList.size(); i++) {
				devidedArr.add(arrListOfList.get(i));
			}
			ArrayList<T> secondArr = sort(devidedArr, isItUp);
			
			returnedArrList = sortTwoFiles(firstArr, secondArr, isItUp);
		}else {
			returnedArrList = arrListOfList.get(0);
		}
		return returnedArrList;
	}
	
	public static <T> ArrayList<T> sortTwoFiles(ArrayList<T> firstArr, ArrayList<T> secondArr, boolean isItUp) {
		int bufferLength = firstArr.size() + secondArr.size();
		ArrayList<T> arr = new ArrayList<T>();
		int firstInd = 0;
		int secondInd = 0;
		for(int i = 0; i < bufferLength; i++) {
			if(firstInd >= firstArr.size()) {
				arr.add(secondArr.get(secondInd));
				secondInd++;
			}else if(secondInd >= secondArr.size()) {
				arr.add(firstArr.get(firstInd));
				firstInd++;
				
			}else if(compare(firstArr, secondArr, firstInd, secondInd, isItUp) ) {
				arr.add(firstArr.get(firstInd));
				firstInd++;
			}else {
				arr.add(secondArr.get(secondInd));
				secondInd++;
			}
		}
		return arr;
	}
	
	public static <T> void viewArray(ArrayList<T> arr) {
		System.out.println("Array is:");
		for(T t : arr) {
			System.out.println(t);
		}
	}

	public static <T> ArrayList<T> sortOriginal( ArrayList<T> array, int leftInd, int rightInd, boolean isItUp) {
		int nextInd = leftInd + (rightInd - leftInd)/2 + 1;
		
		if(rightInd > leftInd + 1) {
			array = sortOriginal(array, leftInd, nextInd -1, isItUp);
			array = sortOriginal(array, nextInd, rightInd, isItUp);
		}
		ArrayList<T> buffer = new ArrayList<T>();
		int cursor = leftInd;
		int middle = nextInd;
		
		try {
			for(int i = 0; i <= rightInd - leftInd; i++) {
				if(cursor < middle & (nextInd > rightInd || compare(array, array, cursor, nextInd, isItUp) ) ) {
					buffer.add( array.get(cursor));
					cursor++;
				}else {
					buffer.add(array.get(nextInd));
					nextInd++;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		
		array = arrayCopy(buffer, array, leftInd, rightInd);
		return array;
	}
	
	public static <T> ArrayList<T> arrayCopy(ArrayList<T> buffer, ArrayList<T> array, int leftInd, int rightInd){
		int j =0;
		for(int i = leftInd; i <= rightInd; i++) {
			array.set(i, buffer.get(j));
			j++;
		}
		return array;
	}
	
	public static <T> boolean compare(ArrayList<T> string1, ArrayList<T> string2, int firstInd, int secondInd, boolean isItUp) {
		try {
			if(string1.get(0) instanceof String) {
				int i = 0;
				String str1 = (String) string1.get(firstInd);
				String str2 = (String) string2.get(secondInd);
				int length1 = str1.length();
				int length2 = str2.length();
			
				do {
					if( str1.charAt(i) < str2.charAt(i) ) {
						return isItUp;
					}else if(str1.charAt(i) > str2.charAt(i)) {
						return !isItUp;
					}else {
						i++;
					}
				}while(i < length1 & i < length2);
				if(length1 < length2) {
					return isItUp;
				}else {
					return !isItUp;
				}
			}else {
				if((int) string1.get(firstInd) < (int) string2.get(secondInd)) {
					return isItUp;
				}else {
					return !isItUp;
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
