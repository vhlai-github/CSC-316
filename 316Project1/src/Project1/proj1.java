/*
 * Vincent Lai
 * Project 1: 
 * 
 * class: proj1 main program. Contains the decompress and compression algorithms
 * 	Compression and decompression is done by reading character by character. 
 * 	First instances of words are put into a linked list. 
 *  Further instances are replaced by the position of which it appears in the list
 *  Any numbers will serach for the word in the list at that index. 
 * class: Node: Node base used for LinkedList Implementation. Only takes strings. 
 * class: LinkedList: Uses node to create linked lists. Designed to insert at front. 
 * 	Moving elements is done by iterating remove and move to front. 
 */



package Project1;

import java.io.*; 
import java.util.*;


public class proj1 { 
	/*
	 * Reads the input, creates a link list for each new word. Does the actual compression.
	 */
	public static void main (String[] args){
		Scanner scan = new Scanner(System.in);
		String line = scan.nextLine();
		LinkedList wordList = new LinkedList();
		boolean contRead = true;
		String word;

		
		if (line.charAt(0) == '0') { //if 0: decompress
			char[] tokenArray;
			int tokenPos;
			int listPos;
			int i = 0;
			if (line.charAt(0) == '0') {
				line = line.substring(2, line.length());
			}
			
			while (contRead == true) {
				tokenArray = new char[line.length()];
				tokenPos = -1;
				for (i = 0; i <= line.length(); i++) {
					
					if (i == line.length()) {
						if (tokenPos != -1) {
							if(Character.isLetter(tokenArray[0])) {
								word = new String(tokenArray, 0, tokenPos+1);
								wordList.insertFront(word);
								System.out.print(wordList.getWord(0));
							}
							else if(Character.isDigit(tokenArray[0])) {
								word = new String(tokenArray, 0, tokenPos+1);
								listPos = Integer.valueOf(word);
								//System.out.print(listPos);
								wordList.moveToFront(listPos);
								System.out.print(wordList.getWord(0));
							}
							tokenPos = -1;
							tokenArray = new char[line.length()];
						}
					}
					
					else if (Character.isLetter(line.charAt(i)) == true) {
						tokenPos++;
						tokenArray[tokenPos] = line.charAt(i);
					}
					else if(Character.isDigit(line.charAt(i)) == true) {
						tokenPos++;
						tokenArray[tokenPos] = line.charAt(i);
					}
					else {
						if (tokenPos != -1) {
							if(Character.isLetter(tokenArray[0])) {
								word = new String(tokenArray, 0, tokenPos+1);
								wordList.insertFront(word);
								System.out.print(wordList.getWord(0));
							}
							else if(Character.isDigit(tokenArray[0])) {
								word = new String(tokenArray, 0, tokenPos+1);
								listPos = Integer.valueOf(word);
								wordList.moveToFront(listPos);
								System.out.print(wordList.getWord(0));
							}
							tokenPos = -1;
							tokenArray = new char[line.length()];
						}

						System.out.print(line.charAt(i));
					}
				}
				System.out.print("\n");
				line = scan.nextLine();
				if (line.equals("") == false) {
					if (line.charAt(0) == '0'){
						contRead = false;
					}
				}
			}
		}
		
		else {  //if not 0, compress
			int inCount = 0;
			int outCount = 0;
			char[] wordArray;
			System.out.print("0 ");
			while(contRead == true) {
				wordArray = new char[line.length()];
				int wordPos = -1;
				
				for (int i = 0; i <= line.length(); i++) {
					
					if (i == line.length()) {
						if (wordPos != -1) {
							word = new String(wordArray, 0, wordPos+1);
							int wordIndex = wordList.find(word);
							//I System.out.print(wordIndex);
							if (wordIndex == -1) {
								wordList.insertFront(word);
								//System.out.print(word);
								outCount = outCount + word.length();
								System.out.print(wordList.getWord(0));
							}
							else {
								System.out.print(wordIndex);
								outCount++;
								wordList.moveToFront(wordIndex);
							}
							wordPos = -1;
							wordArray = new char[line.length()];
						}
					}
					//if it's a letter, start storing it as a word
					else if (Character.isLetter(line.charAt(i)) == true) {
						inCount++;
						wordPos++;
						wordArray[wordPos] = line.charAt(i);
					}
					
					else if ((Character.isLetter(line.charAt(i)) == false)){
						if (wordPos != -1) {
							word = new String(wordArray, 0, wordPos+1);
							int wordIndex = wordList.find(word);
							//System.out.print(wordIndex);
							if (wordIndex == -1) {
								wordList.insertFront(word);
								System.out.print(wordList.getWord(0));
								//System.out.print(word);
								outCount = outCount + word.length();
							}
							else {
								System.out.print(wordIndex);
								outCount++;
								wordList.moveToFront(wordIndex);
							}
							wordPos = -1;
							wordArray = new char[line.length()];		
						}
						inCount++;
						System.out.print(line.charAt(i));
						outCount++;
					}
					
				}
				System.out.print("\n");
				if (scan.hasNextLine() == false){
					contRead = false;
				}
				else {
					line = scan.nextLine();
				}
			}
			System.out.print("0 Uncompressed: " + inCount + " bytes;  Compressed: " + outCount + " bytes");	
		}
		scan.close();
	}	
	
}

class Node{
	public String data;
	public Node next; //links this node to the next Node in the List
	public Node prev; //links this node to the preceeding Node in the List (ie this Node is the prev Node's next node)
	public Node(String data){
	    this.data=data;
	    this.next=null;
	  }
	  
	  public String getData(){ 
		    return data;
	  }
	  public Node getNext(){ 
		    return next;
	  }
	  
	  
}

class LinkedList{
	/*
	 * LinkedList Implementation. 
	 * Has methods to add and remove words and move words to the head of the list.
	 */
	LinkedList(){
		head = new Node("0");
		head.next = null;
		size = 0;
	}
	Node head; //start of the list
	
	int size = 0; //the size of the list
	
	int counter;
	
	String tempWord;
	
	
	public boolean insertFront(String word){ 
		Node newNode = new Node(word);
		newNode.next = head.next;
		head.next = newNode;
		size++;
		return true;
	}
	
	public int find(String word) {
		Node currNode = head;
		counter = 0;
		while(counter <= size) {
			if (currNode.data.equals(word)) {
				return counter;
			}
			else {
				counter++;
				currNode = currNode.next;
			}
		}
		return -1;
	}
	
	public String getWord(int index) {
		counter = 0;
		Node currNode = head.next;
		while (counter < index) {
			currNode = currNode.next;
		}
		return currNode.data;
	}
	
	public boolean remove(int index){
		  Node currNode = head;
		  counter = 0;
			while(counter < index) {
				if (counter == index - 1) {
					tempWord = currNode.next.data;
					currNode.next = currNode.next.next;
					size--;
					return true;
				}
				else {
					counter++;
					currNode = currNode.next;
				}
			}
			return false;
	}	
	
	public boolean moveToFront(int wordIndex) {
		this.remove(wordIndex);
		this.insertFront(this.tempWord);
		return true;
	}
	
	public boolean moveToPos(int wordPos) {
		
		return true;
	}
}






