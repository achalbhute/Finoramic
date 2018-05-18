public class Solution {
	public int pow(int x, int n, int d) {
    	    long ans = 1, xn;  
    	    if(n==0 && x==0)
    	        return 0;
    	    xn = x%d;
            while (0 < n)
            {
                if ((n & 1)==1)
                    ans = (ans*xn)%d;
                n = n/2; 
                xn = (xn*xn)%d;  
            }
            if(ans<0)
                return (d + x) % d;
    	   return (int)ans;
	}
}
