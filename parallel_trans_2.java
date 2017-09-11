import java.util.concurrent.atomic.AtomicReference;
import java.util.*;


public class parallel_trans_2 implements Runnable {
	
	static private AtomicReference<Integer> row  = new AtomicReference<>(0);
	static private AtomicReference<Integer> col  = new AtomicReference<>(0);
	static private AtomicReference<Integer> cons  = new AtomicReference<>(8);
	static int size;
	static int blocksize;
	static int[][] A; 
	static int[][] B; 
		
		Random rand;			
	/*		
	public static void inc_row() {
			row += 8;
	}
*/
	public static int get_and_set_row() {
			Integer i = new Integer(8);
			int j = i.intValue() + (row.get()).intValue();
			Integer i2 = new Integer(j);
			//AtomicReference<Integer> q = new AtomicReference<>(q); 	
			return row.getAndSet(j);
	}
	
	public static int get_and_set_col() {
			Integer i = new Integer(8);
			int j = i.intValue() + (col.get()).intValue();
			Integer i2 = new Integer(j);
			return col.getAndSet(j);
	}

	public static void Transpose(int i, int j, int A[][], int B[][]) {
			int p;
			int q;
			for (p = i; p < i+size; p++) {
				for (q=j;p< j+size ;q++ ) {
					B[j][i] = A[i][j];
				}
				
			}

	}

	public void run(){
		int m = 0;
		int n = 0;
		//get lock, read it, update it and release lock//
		int crow = row.get();
		for (m = crow; m < size; m = get_and_set_row()) {
			int ccol = col.get();
			for(n = ccol; n < size; n = get_and_set_col()) {
					Transpose(m,n,A,B);
			}		
		}		
	}
	

	public static int isCorrect(int[][] A, int[][] B) {
		int i, j;

    	for (i = 0; i < size; i++) {
        	for (j = 0; j < size; ++j) {
            	if (A[i][j] != B[j][i]) {
                	return 0;
           		}
        	}
    	}
    return 1;
	} 

	public static void main(String[] args) {
			long startTime = System.nanoTime();
			for (int t = 0;t < 1000 ; t++ ) {
				
			
			int size = Integer.parseInt(args[0]);
			//int blocksize = Integer.parseInt(args[1]); 
			A = new int [size][size];
			B = new int [size][size]; 
			row.set(0);
			col.set(0);
			Random rand = new Random();
			for(int p=0; p<size;p+=1) {
				for (int q=0;q<size ;q+=1) {
					int rn = rand.nextInt(10);
					A[p][q] = rn;
				}
			}

			Thread t1 = new Thread(new oddevenT());
			Thread t2 = new Thread(new oddevenT());
			t1.start();
			t2.start();
			int x = isCorrect(A, B);
			if(x == 1) {
		//		System.out.println("Correct");
			} else if (x == 0) {
		//		System.out.println("Incorrect");
			}
		}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000; //in ms;
			//float avg = duration/1000;
			System.out.println(duration);
		}		
}
