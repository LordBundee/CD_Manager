/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdmanager;
/**
 *
 * @author Troy Vaughn
 */


// A Node is a node in a doubly-linked list.
class Node
{              // class for nodes in a doubly-linked list

    Node prev;              // previous Node in a doubly-linked list
    Node next;              // next Node in a doubly-linked list
    String nodeString;      // string data stored in this Node

    Node()
    {                           // constructor for head Node 
        prev = this;            // of an empty doubly-linked list
        next = this;
        nodeString = "";        //Default String value
        // data = 'H';           // not used except for printing data in list head
    }

    Node(String w)
    {                               // constructor for a Node with data
        prev = null;
        next = null;
        nodeString = w;
        //this.data = data;     // set argument data to instance variable data
    }

    public void append(Node newNode)
    {  // attach newNode after this Node
        newNode.prev = this;
        newNode.next = next;
        if (next != null)
        {
            next.prev = newNode;
        }
        next = newNode;
        System.out.println("Node with data " + newNode.nodeString
                + " appended after Node with data " + this.nodeString);
    }

    public void insert(Node newNode)
    {  // attach newNode before this Node
        newNode.prev = prev;
        newNode.next = this;
        prev.next = newNode;
        prev = newNode;
        System.out.println("Node with data " + newNode.nodeString
                + " inserted before Node with data " + this.nodeString);
    }

    public void remove()
    {              // remove this Node
        next.prev = prev;                 // bypass this Node
        prev.next = next;
        System.out.println("Node with data " + nodeString + " removed");
    }
    public String toString(){
        return this.nodeString + " - " + this.nodeString;
    }
}

class DLinkedList
{

    Node head;

    public DLinkedList()
    {
        head = new Node();
    }

    public DLinkedList(String string)
    {
        head = new Node(string);
    }

    public Node find(String wrd1)
    {          // find Node containing x
        for (Node current = head.next; current != head; current = current.next)
        {
            if (current.nodeString.compareToIgnoreCase(wrd1) == 0)
            {        // is x contained in current Node?
                System.out.println("Data " + wrd1 + " found");
                return current;               // return Node containing x
            }
        }
        return null;
    }

    //This Get method Added by Matt C
    public Node get(int i)
    {
        Node current = this.head;
        if (i < 0 || current == null)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        while (i > 0)
        {
            i--;
            current = current.next;
            if (current == null)
            {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        return current;
    }

    public String toString()
    {
        String str = "";
        if (head.next == head)
        {             // list is empty, only header Node
            return "List Empty";
        }
        str = "list content = ";
        for (Node current = head.next; current != head && current != null; current = current.next)
        {
            str = str + current.nodeString;
        }
        return str;
    }

    public void print()
    {                  // print content of list
        if (head.next == head)
        {             // list is empty, only header Node
            System.out.println("list empty");
            return;
        }
        System.out.print("list content = ");
        for (Node current = head.next; current != head; current = current.next)
        {
            System.out.print(" " + current.nodeString);
        }
        System.out.println("");
    }

  public void TestRun(DLinkedList dList) 
  {
    dList.print();

    dList.head.append(new Node("1"));       // add Node with data '1'
    dList.print();
    dList.head.append(new Node("3"));       // add Node with data '2'
    dList.print();
    dList.head.append(new Node("5"));       // add Node with data '3'
    dList.print();
    dList.head.insert(new Node("A"));       // add Node with data 'A'
    dList.print();
    dList.head.insert(new Node("C"));       // add Node with data 'B'
    dList.print();
    dList.head.insert(new Node("E"));       // add Node with data 'C'
    dList.print();

    Node nodeA = dList.find("A");           // find Node containing 'A'
    nodeA.remove();                         // remove that Node
    dList.print();

    Node node2 = dList.find("3");           // find Node containing '2'
    node2.remove();                           // remove that Node
    dList.print();

    Node nodeB = dList.find("5");            // find Node containing 'B'
    nodeB.append(new Node("Linked-List"));   // add Node with data X
    dList.print();
  }
}
