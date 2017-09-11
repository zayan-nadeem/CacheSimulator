import java.util.*;

public class normal {
	
	public static void transpose(int size, int[][] A,int[][] B) {
		int i, j, tmp;

    for (i = 0; i < size; i++) {
        for (j = 0; j < size; j++) {
            tmp = A[i][j];
            B[j][i] = tmp;
        }
    }   
	}

	public static void main(String[] args) {

		long startTime = System.nanoTime();
		
		for (int t = 0; t<1000 ; t++) {
		int size = Integer.parseInt(args[0]);
		int[][]	A = new int [size][size];
		int[][]	B = new int [size][size];	
		
		Random rand = new Random();
			for(int p=0; p<size;p+=1) {
				for (int q=0;q<size ;q+=1) {
					int rn = rand.nextInt(10);
					A[p][q] = rn;
				}
			}
		transpose(size, A,B);	
		}
		long endTime = System.nanoTime();

		long duration = (endTime - startTime)/1000000;
		System.out.println(duration);
	}
}