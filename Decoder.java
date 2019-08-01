import java.util.*;
public class LoopoverUpper {
    private static int[] perm(long num, int n, int ra, int ca, int k) {
        int max=locid(n*n-1,n,ra,ca)+1;
        int[] out=new int[k];
        for (int i=k-1; i>-1; i--) {
            out[i]=(int)(num%(max-i));
            num/=max-i;
        }
        for (int i=k-1; i>-1; i--)
            for (int j=i+1; j<k; j++)
                if (out[j]>=out[i])
                    out[j]++;
        for (int i=0; i<out.length; i++)
            out[i]=idloc(out[i],n,ra,ca);
        return out;
    }
    private static int locid(int loc, int n, int ra, int ca) {
        int r=loc/n, c=loc%n;
        assert(r>=ra || c>=ca);
        int smallr=n-ca;
        if (r<ra)
            return r*smallr+(c-ca);
        else
            return (r-ra)*n+c+ra*smallr;
    }
    private static int idloc(int id, int n, int ra, int ca) {
        int smallr=n-ca;
        if (id<ra*smallr)
            return (id/smallr)*n+(id%smallr+ca);
        else {
            id-=ra*smallr;
            return (id/n+ra)*n+(id%n);
        }
    }
    private static long num(String b95) {
        long out=0;
        for (int i=0; i<b95.length(); i++) {
            out*=95;
            out+=b95.charAt(i)-32;
        }
        return out;
    }
    public static void main(String[] args) {
        String code="&@q8`";
        int N=5;
        int ra=3, ca=4, rb=5, cb=5;
        int[] locs=perm(num(code),N,ra,ca,rb*cb-ra*ca);
        int[] tiles=new int[rb*cb-ra*ca];
        for (int i=0; i<tiles.length; i++)
            tiles[i]=idloc(i,N,ra,ca);
        System.out.println(Arrays.toString(locs)+"\n"+Arrays.toString(tiles));
        int[][] board=new int[N][N];
        for (int i=0; i<tiles.length; i++) {
            int loc=locs[i];
            int tile=tiles[i];
            board[loc/N][loc%N]=tile;
        }
        for (int r=0; r<N; r++) {
            for (int c=0; c<N; c++) {
                System.out.print((r<ra&&c<ca)?"*":(char)(65+board[r][c]));
            }
            System.out.println();
        }
    }
}
