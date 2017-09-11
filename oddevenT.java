import java.util.*;

public class oddevenT implements Runnable {
	/*
	public oddevenT(String name) {
		String Tname = name;	
		
	}
	*/
	static int size;
	static int[][] A; 
	static int[][] B; 
		
		Random rand;			
			
			
	
	public static synchronized void Transpose(int i, int j, int A[][], int B[][]) {
			B[j][i] = A[i][j];	
	}

	public void run(){
		int i = 0;
		int j = 0;
		//System.out.println("HELLO" + Thread.currentThread().getName());
		if(Thread.currentThread().getName() == "Thread-0") {
			for(i=0; i <size; i+=2) {
				for(j=0; j<size; j+=2) {
					Transpose(i,j,A,B);
				}
			}
		} else if (Thread.currentThread().getName() == "Thread-1") {
			for(i=1; i <size; i+=2) {
				for(j=1; j<size; j+=2) {
					Transpose(i,j,A,B);
				}
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
			for (int t =0 ; t<1000; t++) {
				
			int size = Integer.parseInt(args[0]); 
			A = new int [size][size];
			B = new int [size][size]; 
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
			//	System.out.println("Correct");
			} else if (x == 0) {
			//	System.out.println("Incorrect");
			}
			}
			long endTime = System.nanoTime();
			long duration = (endTime - startTime)/1000000; //in ms;
			//float avg = duration/1000;
			System.out.println(duration);


		}		
}
