package FibonacciHeap;

public class Tester {

    public static void main(String[] args){

        FibonacciHeap heap = new FibonacciHeap();

        int[] numbers = new int[1234567];

        for(int i = 0; i < 1234567; i++){
            numbers[i] = i;
        }

        print("inserting numbers from 0 to 1234567...");

        for(int i : numbers){
            heap.insert(i);
        }

        print("Min key(should be 0):");
        print(heap.min.key);
        print("Deleting first 700 numbers:");

        for(int i = 0; i < 700; i++){
            heap.deleteMin();
        }

        print("Min key(should be 700):");
        print(heap.min.key);


        print("The first 100 numbers are:");
        int[] numbers2 = FibonacciHeap.kMin(heap, 49);
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
