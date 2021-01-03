package FibonacciHeap;

public class Tester {

    public static void main(String[] args){

        FibonacciHeap heap = new FibonacciHeap();

        int[] numbers = {10,9,8,7,6,5,4};

        for(int i : numbers){
            heap.insert(i);
        }

        print(heap.min.key);

        heap.deleteMin();
        print(heap.min.key);

        int[] numbers2 = {1,2,33,44};
        for(int i : numbers2){
            heap.insert(i);
        }
        heap.deleteMin();
        print(heap.min.key);
        print(heap.size);
        print(heap.numOfTrees);



    }


    //PRINT FUNCTIONS BECAUSE YOU DESERVE THEM
    public static void print(String str){
        System.out.println(str);
    }
    public static void print(int str){
        System.out.println(Integer.toString(str));
    }
    public static void print(boolean str){
        System.out.println(String.valueOf(str));
    }
}
