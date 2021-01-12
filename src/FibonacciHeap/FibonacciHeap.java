package FibonacciHeap;

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
    private static HeapNode[] buckets;

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
            this.meld(heap);
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
        buckets = new HeapNode[(int)(Math.log(size)/Math.log(2))*2]; //create empty array of buckets
        HeapNode children = remove(min); //pointer to start of children
        numOfTrees += concat(children); //add all children of min to forest
        successiveLink();
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
        if (heap2.isEmpty()){
            return;
        }
        internalMeld(heap2);
        // tree variables update
    	this.numOfTrees += heap2.numOfTrees;
    	this.size += heap2.size();
    	this.marked += heap2.marked;
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
                numOfTrees++;
                TOTALCUTS++;
            } while (parent.marked); // @inv a root (root.parent == null) is never marked
            if (parent.parent != null) { // parent is not a root
                parent.marked = true;
                this.marked++;
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
        treeList[] treeListArr = new treeList[(int)(Math.log(H.size)/Math.log(2)) + 1];
        int[] minArr = new int[k];
        HeapNode node = H.forestStart;
        do{
            assert treeListArr[node.rank] == null;
            treeListArr[node.rank] = new treeList(node);
            node = node.next;
        }while (node != H.forestStart);
        for (int i = 0; i<k; i++){
            minArr[i] = getMin(treeListArr);
        }

        return minArr;
    }

    private static int getMin(treeList[] treeListArr){
        treeList min = null;
        for(treeList tree: treeListArr){
            if(tree == null) continue;
            if(min == null) min = tree;
            if(tree.getMinKey() < min.getMinKey()){
                min = tree;
            }
        }
        treeListArr[min.rank] = null;
        LinkArray(treeListArr, min);
        return min.getMinKey();
    }

    private static void LinkArray(treeList[] treeListArr, treeList min){
        HeapNode node = min.getChildren();
        if(node == null) return;
        do{
            treeList tree = new treeList(node);
            pushToArray(treeListArr, tree);
            node = node.next;
        }while (node != min.getChildren());
        while(min.hasNext()){
            treeList tree = min.getNext();
            pushToArray(treeListArr, tree);
        }
    }

    private static void pushToArray(treeList[] treeListArr, treeList tree){

        int i = tree.rank;

        if(treeListArr[i] == null){
            treeListArr[i] = tree;
            return;
        }
        tree.insert(treeListArr[i]);
        treeListArr[i] = null;
        pushToArray(treeListArr, tree);
    }

    // HELPER FUNCTIONS

    private void successiveLink(){ //successively link heap
        HeapNode nextRoot = forestStart;
        do{
            HeapNode prevRoot = nextRoot; //iterate to next tree before changing pointers
            nextRoot = nextRoot.next;
            link(prevRoot);
        }while(nextRoot != forestStart);
        resetPointers(); //make forrest right again
    }

    private void link(HeapNode node){//recursively link trees
        int bucket = node.rank;

        if (buckets[bucket] == null) {
            buckets[bucket] = node;
            return;
        }

        HeapNode father = buckets[bucket];
        HeapNode son = node;

        if(father.key > node.key){
            father = node;
            son = buckets[bucket];
        }
        //join em up
        father.makeSon(son);
        //fix the stuff
        buckets[bucket] = null;
        TOTALLINKS++;
        numOfTrees--;
        link(father);
    }

    private void resetPointers(){ //create new linked list from buckets
        forestStart = null;
        min = null;
        HeapNode last = null;
        for(HeapNode node: buckets){
           if( node != null){
               if(forestStart == null){
                   forestStart = node;
                   min = node;
                   last = node;
                   continue;
               }
               if(node.key < min.key){
                   min = node;
               }
               last.next = node;
               node.prev = last;
               last = node;
           }
        }
        if (forestStart != null) forestStart.prev = last;
        if (last != null) last.next = forestStart;
    }

    public HeapNode remove(HeapNode node){ // remove tree from forrest and return pointer to child node

        if(node == null){ //do nothing if heap is empty
            return null;
        }
        size--;
        HeapNode prev = node.prev;
        if(node == prev){ //if tree is only node
            forestStart = null;
        } else {
            if (node == forestStart){
                forestStart = forestStart.next;
            }
            prev.next = node.next;
            prev.next.prev = prev;
            node.next = node.prev = node;
        }
        numOfTrees--;
        return node.child;
    }

    public int concat(HeapNode nodeList){   //accepts pointer to start of list, adds list to forest and returns number
                                            // of nodes added
        if (nodeList == null){
            return 0;
        }
        //fix pointers
        HeapNode node = nodeList;
        int numOfNodes = 0;
        do {
            node.parent = null; // set all parents to null
            if(node.marked){ //un-mark
                node.marked = false;
                marked--;
            }
            numOfNodes++; //count
            HeapNode tmp = node.next;
            FibonacciHeap heap = nodeToHeap(node);
            internalMeld(heap);
            numOfTrees++;
            node = tmp;
        } while (node != nodeList);

        return numOfNodes;
    }

    private void internalMeld (FibonacciHeap heap2) {
        if (this.isEmpty()){
            forestStart = heap2.forestStart;
            this.min = heap2.min;
        }

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
    }

    // @pre - node.parent != null
    // @ret - node.parent (for cascading-cuts)
    private HeapNode disconnectSubTree(HeapNode node) {
        if (node.parent.child == node) { // node is the first child
            if (node.next != node) { // node is not the only child
                node.parent.child = node.next;
            } else {
                node.parent.child = null;
            }
        }

        //fix pointers
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = node.prev = node;

        node.parent.rank--;
        HeapNode parent = node.parent;
        node.parent = null;
        return parent;
    }

    // Node pointer ---> Heap pointer (for melding)
    // ONLY USE WITH INTERNAL MELD - variables not updated
    private FibonacciHeap nodeToHeap(HeapNode node) {
        FibonacciHeap heap = new FibonacciHeap();
        heap.forestStart = heap.min = node.next = node.prev = node;
        node.marked = false;
        this.marked--;
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
	    this.value = null;
    }

  	public HeapNode (int key, String value) {
	    this.key = key;
	    this.value = value;
      }

    public void makeSon(HeapNode other){
  	    if (child == null){
            child = other.next = other.prev = other;
        } else {
            other.next = child;
            other.prev = child.prev;
            child.prev.next = child.prev = other;
            child = other;
        }
  	    other.parent = this;
  	    rank++;
    }

  	public int getKey() {
	    return this.key;
      }

    }

    private static class treeList {
        public int rank = 0;
        public treeListNode min;
        public treeListNode last;

        public treeList(HeapNode root){
            this.min = new treeListNode(root);
            this.last = this.min;
            this.rank = root.rank;
        }

        public void insert(treeList tree){
            if(tree.getMinKey() >= min.node.key){
                last.setNext(tree.min);
                last = tree.last;
             } else {
                tree.last.setNext(min);
                min = tree.min;
            }
            rank++;
        }

        public int getMinKey() {
            return min.node.key;
        }

        public HeapNode getChildren(){
            return min.node.child;
        }

        public boolean hasNext(){
            return (min.next != null);
        }

        public treeList getNext(){
            treeListNode next = min.next;
            treeList nextTree = new treeList(next.node);
            return nextTree;
        }
    }

    private static class treeListNode{

        HeapNode node;
        treeListNode next = null;

        public treeListNode(HeapNode node){
            this.node = node;
        }

        public void setNext(treeListNode node){
            this.next = node;
        }

    }

}
