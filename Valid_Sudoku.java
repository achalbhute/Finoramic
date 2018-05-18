public class Solution {
    // DO NOT MODIFY THE LIST. IT IS READ ONLY
    public int isValidSudoku(final List<String> A) {
       char[][] board= new char[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
            board[i][j]=A.get(i).charAt(j);
            }
        }
        for(int i=0; i<9; i++){
	        boolean[] b =  new boolean[9];
	        for(int j=0; j<9; j++){
	            char val = board[i][j];
	            if(val != '.')
	            {
	                if(b[(int)(val-'1')])
	                    return 0;
	                    else{
	                        b[(int)(val-'1')] = true;
	                    }
	            }
	        }
	    }
        
        for(int i=0; i<9; i++){
	        boolean[] b =  new boolean[9];
	        for(int j=0; j<9; j++){
	            char val = board[j][i];
	            if(val != '.')
	            {
	                if(b[(int)(val-'1')])
	                    return 0;
	                    else{
	                        b[(int)(val-'1')] = true;
	                    }
	            }
	        }
	    }
	    
	    for(int i=0; i<9; i++){
	        boolean[] b =  new boolean[9];
	        for(int j=(i/3)*3; j<((i/3)*3) + 3 ; j++){
	            for(int k=(i%3)*3; k<((i%3)*3) + 3 ; k++){
    	            char val = board[j][k];
    	            if(val != '.')
    	            {
    	                if(b[(int)(val-'1')])
    	                    return 0;
    	                    else{
    	                        b[(int)(val-'1')] = true;
    	                    }
    	            }
	            }
	        }
	    }
	    return 1;
    }
}