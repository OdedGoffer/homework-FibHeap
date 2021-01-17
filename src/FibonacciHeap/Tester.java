package FibonacciHeap;

public class Tester {

    public static void main(String[] args){

        FibonacciHeap heap = new FibonacciHeap();

        int[] numbers = {45,36,67,35,76,34,722,344,1,40,333,7,9012,2223,25,26,27,28,29,99,98,97,96,95,94,93,92,91};


        print("inserting numbers from 0 to 1234567...");

        for(int i : numbers){
            heap.insert(i);
        }

        print("Min key(should be 0):");
        print(heap.min.key);
        print("Deleting first 700 numbers:");


        heap.deleteMin();


        print("Min key(should be 700):");
        print(heap.min.key);


        print("The first 7 numbers are:");
        int[] numbers2 = FibonacciHeap.kMin(heap, 7);
        for(int i : numbers2) {
            print(i);
        }
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
