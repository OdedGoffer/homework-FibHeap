import java.util.LinkedList;
import java.util.Stack;

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over integers.
 */
public class FibonacciHeap
{
    public HeapNode forestStart;
    public HeapNode min;
    public int size;
    public int numOfTrees = 0;
    public int marked = 0;
    public static int totalLinks = 0;
    public static int totalCuts = 0;
    public int maxDegree;

   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean isEmpty()
    {
    	return (this.forestStart == null);
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * 
    * Returns the new node created. 
    */
    public HeapNode insert(int key) {
    	HeapNode node = new HeapNode(key);

    	// pointers update ("addFirst")
    	if (isEmpty()) {
    	    node.next = node.prev = node;
    	    numOfTrees++;
    	    forestStart = min = node;
        } else {
            connectSubTree(node);
        }
    	size++;
        return node;
    }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    *
    */
    public void deleteMin()
    {
     	return; // should be replaced by student code
     	
    }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    *
    */
    public HeapNode findMin()
    {
        return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
    * 
    */
    public int[] countersRep() {
        int[] arr = new int[maxDegree+1];
        HeapNode node = forestStart;
        do {
            arr[node.rank]++;
            node = node.next;
        } while (node != forestStart);
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    */
    public void delete(HeapNode x) 
    {    
    	return; // should be replaced by student code
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this chage (for example, the cascading cuts procedure should be applied if needed).
    */
    public void decreaseKey(HeapNode x, int delta) {

        // if x is a root of a tree
        if (x.parent == null) {
            x.key -= delta;
            if (x.key < min.key) {
                min = x;
            }
        } else { // x has a parent
            // x does not need to be cut off
            if (x.key - delta > x.parent.key) {
                x.key = x.key - delta;
            } else { // x needs to be cut off
                disconnectSubTree(x);
                connectSubTree(x);
            }
        }
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
    */
    public int potential() 
    {    
    	return numOfTrees + 2*marked;
    }

   /**
    * public static int totalLinks()
    *
    * This static function returns the total number of link operations made during the run-time of the program.
    * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
    * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
    * in its root.
    */
    public static int totalLinks()
    {    
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return totalCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k*deg(H)). 
    * You are not allowed to change H.
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
        int[] arr = new int[42];
        return arr; // should be replaced by student code
    }

    // HELPER FUNCTIONS

    // connecting from the left
    // @pre - !isEmpty()
    private void connectSubTree(HeapNode root) {
        root.prev = forestStart.prev;
        root.next = forestStart;
        forestStart.prev = root.prev.next = root;
        root.marked = false;

        // tree variables update
    	forestStart = root;
    	if (min.key > root.key) {
    	    min = root;
        }
    	numOfTrees++;
    }

    // @pre - node.parent != null
    private void disconnectSubTree(HeapNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = node.prev = null;

        if (node.parent.child == node) { // node is the first child
            if (node.next != node) { // node is not the only child
                node.parent.child = node.next;
            } else {
                node.parent.child = null;
            }
        }

        rankUpdate(node.parent);
        connectSubTree(node);
        if (!node.parent.marked && node.parent.parent != null) { // node is not marked && not a root
            node.parent.marked = true;
        } else if (node.parent.parent != null) {
            disconnectSubTree(node.parent);
        }

        node.parent = null;
    }

    // PROBLEM - not good enough complexity
    private void rankUpdate(HeapNode node) {
        if (node.child == null) {
            node.rank = 0;
        } else {
            int rank = 0;
            HeapNode child = node.child;
            do {
                if (child.rank + 1 > rank) {
                    rank = child.rank + 1
                }
                child = child.next;
            } while (child.next != node.child);
            node.rank = rank;
        }
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

	public int key;
	public String value;
	public int rank;
	public boolean marked;
	public HeapNode parent;
	public HeapNode child;
	public  HeapNode next;
	public  HeapNode prev;


  	public HeapNode (int key) {
	    this.key = key;
      }

  	public int getKey() {
	    return this.key;
      }

    }
}
