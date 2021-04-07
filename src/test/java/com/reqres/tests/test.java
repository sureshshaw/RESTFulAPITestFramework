package com.reqres.tests;


class Node {
	int data;
	Node nextNode; 
	
	
}
public class test {

	static Node n1, n2, n3, n4;
	public static void main(String[] args) {
		n1 = new Node();
		n2 = new Node();

		n3 = new Node();

		n4 = new Node();
		createLinkedList();
		printLinkedList();
		
		deleteNode(10);
	}
	
	
	public static void deleteNode(int data) {
	//deletion logic
		Node d = n1;
		Node d1 = null;
		Node d2 = null;
		
		while(d!= null) {
			
			if(d.data==data) {
				
				d2 = d.nextNode;
				d1.nextNode = d2;
				d = null;
				break;
			}	
			d1=d;
			d = d.nextNode;
		}
		printLinkedList();
	}
	
	public static void printLinkedList() {
		Node t = n1;
		System.out.println();
		while (t!= null) {
			System.out.print(" "+t.data+" ");
			t = t.nextNode;
		}
	}
		
	//n1->n2->n3->n4->null
	public static void createLinkedList(){

		n1.data = 10;
		n1.nextNode = n2;
		
		n2.data = 20;
		n2.nextNode = n3;
		
		n3.data = 30;
		n3.nextNode = n4;
		
		n4.data = 40;
		n4.nextNode = null;
		
	}
		
		
		
		
}
	



