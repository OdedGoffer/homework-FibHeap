import java.util.Arrays;

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
    public static int TOTALLINKS = 0;
    public static int TOTALCUTS = 0;

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

    	// pointers update ("addFirst")
    	if (isEmpty()) {
    	    HeapNode node = new HeapNode(key);
    	    node.next = node.prev = forestStart = min = node;
    	    numOfTrees++;
    	    size++;
        } else {
    	    FibonacciHeap heap = new FibonacciHeap();
    	    heap.insert(key);
            meld(heap);
        }
        return forestStart;
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
    public void meld (FibonacciHeap heap2) // heap2 is added at first
    {
        internalMeld(heap2);
        // tree variables update
    	this.numOfTrees += heap2.numOfTrees;
    	this.size += heap2.size();
    	this.marked += heap2.marked;
    }

    private void internalMeld (FibonacciHeap heap2) {
        HeapNode tmp = this.forestStart.prev;
        heap2.forestStart.prev.next  = this.forestStart;
        this.forestStart.prev = heap2.forestStart.prev;
        heap2.forestStart.prev = tmp;
        tmp.next = heap2.forestStart;

        // tree variables update
    	forestStart = heap2.forestStart;
    	if (this.min.key > heap2.min.key) {
    	    this.min = heap2.min;
        }
    	numOfTrees++;
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
        int[] arr = new int[(int) (Math.log(this.size))+1];
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
        int minimalKey = this.min.key;
        decreaseKey(x, x.key - minimalKey - 1);
        deleteMin();
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
            HeapNode parent = x.parent;
            // x does not need to be cut off
            if (x.key - delta > x.parent.key) {
                x.key = x.key - delta;
            } else do { // cascading-cuts
                parent = disconnectSubTree(x);
                FibonacciHeap heap = nodeToHeap(x);
                internalMeld(heap);
                x = parent;
                TOTALCUTS++;
            } while (parent.marked); // @inv a root (root.parent == null) is never marked
            if (parent.parent != null) { // parent is not a root
                parent.marked = true;
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
    	return TOTALLINKS;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the run-time of the program.
    * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
    */
    public static int totalCuts()
    {    
    	return TOTALCUTS;
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

    // @pre - node.parent != null
    // @ret - node.parent (for cascading-cuts)
    private HeapNode disconnectSubTree(HeapNode node) {
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
        HeapNode parent = node.parent;
        node.parent = null;
        return parent;
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
                    rank = child.rank + 1;
                }
                child = child.next;
            } while (child.next != node.child);
            node.rank = rank;
        }
    }

    // Node pointer ---> Heap pointer (for melding)
    // ONLY USE WITH INTERNAL MELD - variables not updated
    private FibonacciHeap nodeToHeap(HeapNode node) {
        FibonacciHeap heap = new FibonacciHeap();
        heap.forestStart = heap.min = node.next = node.prev = node;
        node.marked = false;
        return heap;
    }
    
   /*
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
	public HeapNode next;
	public HeapNode prev;

  	public HeapNode (int key) {
	    this.key = key;
      }

  	public int getKey() {
	    return this.key;
      }

    }
}
