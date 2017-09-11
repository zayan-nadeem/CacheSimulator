import java.util.*;

public class bnormal {
	
	public static void transpose(int size, int[][] A,int[][] B) {
		int row=0,col=0,i=0, j=0, temp=0, diag=0, c =0;

    	
     	int bsize = 8;
        for(row = 0 ; row < size; row+=bsize)   {
            for(col = 0; col< size; col+=bsize) {
                for(i=row;i<row+bsize;i++){
                    for(j=col;j<col+bsize;j++) {
                        if(i!=j){
                            B[j][i] = A[i][j];
                        } else { //when i==j
                            temp = A[i][j];
                            diag = i;
                            c = 1;
                        }
                    } if (c == 1) {
                        B[diag][diag] = temp;
                        c = 0;
                    }  
                }
            }
        }
    }

//enter size in cmd
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