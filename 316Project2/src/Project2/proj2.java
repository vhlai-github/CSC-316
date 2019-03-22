/**
 * Vincent Lai
 * Project 2
 * Takes user input for a input file and an output file.
 * Reads the Input file for a preorder traversal and a post traversal reading of a tree
 * constructs the tree using a recursive buildtree method
 * Then reads queries pertaining to the tree about family member relationships.
 */

package Project2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.*;
import java.util.Queue;
import java.lang.Math;




public class proj2 {
	
	//public static char[] pretrav = new char[256];
	//public static char[] posttrav = new char[256];
	public static ArrayList<Character> pretrav = new ArrayList<Character>(0);
	public static ArrayList<Character> posttrav = new ArrayList<Character>(0);
	public static int size;
	public static int index;
	public static Scanner reader;
	public static PrintStream writer;
	
	
	public static void main(String[] args) throws IOException {
		
		Scanner scan = new Scanner(System.in);
		String input = scan.nextLine();
		File inFile = new File(input);
		reader = new Scanner(inFile);
		String output = scan.nextLine();
		File outFile = new File(output);
		writer = new PrintStream(outFile);
		
		/**
		 * Read in first 2 lines
		 */
		
		String preScan = reader.nextLine();
		String postScan = reader.nextLine();
		//String preScan = scan.nextLine();
		//String postScan = scan.nextLine();
		boolean reading = true;
		int counter = 1;
		
		while (reading == true) {
			if (preScan.charAt(counter)!= ' ' && preScan.charAt(counter)!= ',' ) {
				pretrav.add(preScan.charAt(counter));
				size++;
				if (preScan.charAt(counter+1) == '.') {
					reading = false;
				}
			}
			counter++;
		}
		//System.out.println(pretrav.toString());
		//System.out.println(pretrav.size());

		reading = true;
		counter = 1;
		while (reading == true) {
			if (postScan.charAt(counter)!= ' ' && postScan.charAt(counter)!= ',' ) {
				posttrav.add(postScan.charAt(counter));
				if (postScan.charAt(counter+1) == '.') {
					reading = false;
				}
			}
			counter++;
		}
		//System.out.println(posttrav.toString());
		//System.out.println(posttrav.size());
		
		FamilyTree family = new FamilyTree(size, pretrav, posttrav);
		
		//System.out.println("Done");
		while (reader.hasNextLine()) {
			//String query = reader.nextLine();
			//System.out.println("Next");
			char A = ' ';
			char B = ' ';
			int posA;
			int posB = 0;
			
			String query = reader.nextLine();
			if (query != "") {
				for (posA = 1; posA < query.length(); posA++) {
					if (query.charAt(posA) != '?' && query.charAt(posA) != ',' && query.charAt(posA) != ' ') {
						A = query.charAt(posA);
						break;
					}
				}
				posB = posA+1;
				
				for (posA = posB; posA < query.length(); posA++) {
					if (query.charAt(posA) != '?' && query.charAt(posA) != ',' && query.charAt(posA) != ' ') {
						B = query.charAt(posA);
						break;
					}
				}
				family.printRelationship(A, B);
			}
		}
		
		family.printLevelOrder(family.root);;
		
		scan.close();
	}
	
	
	
}

class Node{
	public char name;
	public boolean markedA;
	public boolean markedB;
	public int APath;
	public int BPath;
	public ArrayList<Node> children;
	//public Node parent;
	
	public Node(char name){
	    this.name=name;
	    this.children = new ArrayList<Node>(0);
	    this.markedA = false;
	    this.markedB = false;
	    this.APath = -1;
	    this.BPath = -1;
	    //this.parent = parent;
	    //this.parent.children.add(this);
	  }
	  
	  public char getMember(){ 
		    return name;
	  }
	  public Node getChild(int index){ 
		    return children.get(index);
	  }
	  /*
	  public Node getParent() {
		  return parent;
	  }
	  */
}

class FamilyTree{
	public Node root;
	public int APathLength;
	public int BPathLength;
	
	FamilyTree(int size, ArrayList<Character> pre, ArrayList<Character> post){
		root = buildTree(size, 0, 0);
	}
	
	public Node buildTree(int size, int prestart, int poststart) {
		if (size == 1) {
			return new Node(proj2.pretrav.get(prestart));
		}
		else {
			Node root = new Node(proj2.pretrav.get(prestart));
			int subSize = 0;
			prestart++;
			for(int i = 0; i < size-1; i++) {
				subSize++;
				//System.out.print(subSize + " ");
				//System.out.print(proj2.pretrav.get(prestart)+ " ");
				//System.out.println(proj2.posttrav.get(poststart+subSize-1));
				if (proj2.pretrav.get(prestart) == proj2.posttrav.get(poststart+subSize-1)) {
					root.children.add(buildTree(subSize, prestart, poststart));
					if (subSize == 1) {
						prestart = prestart + 1;
						poststart++;
					}
					else {
						prestart = prestart + subSize;
						poststart = poststart + subSize;
					}
					subSize = 0;
				}
				//System.out.println(root.children.size());
			}
			return root;
		}
	}
	
	public void printRelationship(char A, char B) throws IOException{
		this.markAParents(this.root, A);
		this.markBParents(this.root, B);
		checkAncestors(this.root, A, B);
	}
	
	public void printLevelOrder(Node p) {
		ArrayList<Character> levelOrderList = new ArrayList<Character>(0);
		Queue<Node> levelOrder = new LinkedList<>();
		if (p == null) {
			return;
		}
		levelOrder.add(this.root);
		while (levelOrder.isEmpty() == false) {
			Node q = levelOrder.remove();
			levelOrderList.add(q.name);
			//System.out.print(q.name+ ", ");
			for(int i = 0; i < q.children.size(); i++) {
				//System.out.print(q.children.size()); //
				Node r = q.children.get(i); 
				levelOrder.add(r);
			}
		}
		int i;
		for(i = 0; i<levelOrderList.size() - 1 ; i++) {
			proj2.writer.print(levelOrderList.get(i) + ", ");
		}
		proj2.writer.println(levelOrderList.get(i)+".");
	}
	
	public void checkAncestors(Node p, char A, char B) throws IOException { //using level order Traversal
		Queue<Node> levelOrder = new LinkedList<>();
		if (p == null) {
			return;
		}
		levelOrder.add(this.root);
		while (levelOrder.isEmpty() == false) {
			Node q = levelOrder.remove();
			if (q.markedA && q.markedB) {
				if (q.APath == 0 && q.BPath == 0) {
					proj2.writer.println(A + " is " + B + ".");
				}
				else if (q.APath == 0 && q.BPath == 1) {
					proj2.writer.println(A + " is "+ B +"'s parent.");
				}
				else if (q.APath == 0 && q.BPath == 2) {
					proj2.writer.println(A + " is " + B +"'s grandparent.");
				}
				else if (q.APath == 0 && q.BPath == 3) {
					proj2.writer.println(A + " is " + B +"'s great grandparent.");
				}
				else if (q.APath == 0 && q.BPath > 3) {
					proj2.writer.print(A + " is " + B +"'s");
					for (int i = 0; i < q.BPath - 2; i++) {
						proj2.writer.print(" great");
					}
					proj2.writer.println("-grandparent.");
				}
				else if (q.APath == 1 && q.BPath == 0) {
					proj2.writer.println(A + " is " + B +"'s child.");
				}
				else if (q.APath == 2 && q.BPath == 0) {
					proj2.writer.println(A + " is " + B +"'s grandchild.");
				}
				else if (q.APath >= 3 && q.BPath == 0) {
					proj2.writer.print(A + " is " + B +"'s");
					for (int i = 0; i < q.APath - 2; i++) {
						proj2.writer.print(" great.");
					}
					proj2.writer.println("-grandchild.");
				}
				else if (q.APath == 1 && q.BPath == 1) {
					proj2.writer.println(A + " is " + B +"'s sibling.");
				}
				else if (q.APath == 1 && q.BPath == 2) {
					proj2.writer.println(A + " is " + B +"'s aunt/uncle.");
				}
				else if (q.APath == 1 && q.BPath >= 3) {
					proj2.writer.print(A + " is " + B +"'s");
					for (int i = 0; i < q.BPath - 2; i++) {
						proj2.writer.print(" great");
					}
					proj2.writer.println("-aunt/uncle.");
				}
				else if (q.APath == 2 && q.BPath == 1) {
					proj2.writer.println(A + " is " + B +"'s niece/nephew.");
				}
				else if (q.APath >= 3 && q.BPath == 1) {
					proj2.writer.print(A + " is " + B +"'s");
					for (int i = 0; i < q.APath -2; i++) {
						proj2.writer.print(" great");
					}
					proj2.writer.println("-niece/nephew.");
				}
				else if(q.APath >= 2 && q.BPath >= 2){
					proj2.writer.println(A + " is " + B +"'s " + (Math.min(q.APath, q.BPath)-1) + "th cousin " + (Math.abs(q.APath-q.BPath)) + " times removed."); //
				}
			}
			q.markedA = q.markedB = false;
			q.APath = q.BPath = -1;
			for(int i = 0; i < q.children.size(); i++) {
				//System.out.print(q.children.size()); //
				Node r = q.children.get(i); 
				levelOrder.add(r);
			}
		}
	}
	
	public boolean markAParents(Node p, char c) {
		if (p == null) {
			return false;
		}
		if (p.name == c) {
			this.APathLength = 0;
			p.markedA = true;
			p.APath = this.APathLength;
			return true;
		}

		for (int i = 0; i < p.children.size(); i++) {
			if (markAParents(p.children.get(i), c)) {
				this.APathLength++;
				p.markedA = true;
				p.APath = this.APathLength;
				return true;
			}
		}
		return false;
	}
		
	public boolean markBParents(Node p, char c) {
		if (p == null) {
			return false;
		}
		if (p.name == c) {
			this.BPathLength = 0;
			p.markedB = true;
			p.BPath = this.BPathLength;
			if (p.markedB && p.markedA) {
				return false;
			}
			else {
				return true;
			}
		}

		for (int i = 0; i < p.children.size(); i++) {
			if (markBParents(p.children.get(i), c)) {
				this.BPathLength++;
				p.markedB = true;
				p.BPath = this.BPathLength;
				if (p.markedB && p.markedA) {
					return false;
				}
				else {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean clearMarked;
	
}