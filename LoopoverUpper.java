import java.util.*;
public class LoopoverUpper {
    //modulo operator but fixed to be constrained in the range [0,k)
    private static int mod(int n, int k) {
        int out=n%k;
        if (out<0)
            out+=k;
        return out;
    }
    /*
    Describing a permutation of a subset of the pieces of an n x n board:
    an array of pieces, where each piece is described by their locations in the board
    if a piece is at row r, column c, it is given the number r*n+c, where r and c are both in the interval [0,n)
    */
    //rotating row r of a set of pieces by d units to the right
    //negative d means shifting to the left
    public static void rmv(int[] locs, int r, int d, int n) {
        for (int i=0; i<locs.length; i++) {
            int rv=locs[i]/n;
            if (rv==r)
                locs[i]=rv*n+mod(locs[i]%n+d,n);
        }
    }
    //rotating column c of a set of pieces by d units down
    //negative d means shifting up
    public static void cmv(int[] locs, int c, int d, int n) {
        for (int i=0; i<locs.length; i++) {
            int cv=locs[i]%n;
            if (cv==c)
                locs[i]=mod(locs[i]/n+d,n)*n+cv;
        }
    }
    //using modified Lehmer code to describe each set of pieces in an n x n board as a single number
    //usually faster than using a String to concatenate each element together with a delimiter character
    public static long code(int[] perm, int n) {
        int[] help=perm.clone();
        long out=0;
        for (int i=0; i<help.length; i++) {
            out*=n*n-i;
            out += help[i];
            for (int j=i+1; j<help.length; j++)
                if (help[j]>help[i])
                    help[j]--;
        }
        return out;
    }
    //the maximum number of moves it takes to extend an ra x ca solved block to a rb x cb block using the most efficient solving method
    //basically a BFS starting from the rb x cb block and allowing all moves using rows ra to rb-1 and columns ca to cb-1
    //does not allow any disturbance of the inner ra x ca block
    public static int maxmoves(int ra, int ca, int rb, int cb, int n) {
        int[] id=new int[rb*cb-ra*ca];
        for (int r=0, i=0; r<rb; r++)
            for (int c=0; c<cb; c++)
                if (r>=ra || c>=ca) {
                    id[i]=r*n+c;
                    i++;
                }
        Set<Long> perms=new HashSet<>();
        perms.add(code(id,n));
        ArrayList<int[]> locss=new ArrayList<>();
        locss.add(id);
        int out=0;
        while (true) {
            ArrayList<int[]> nlocss=new ArrayList<>();
            for (int[] locs:locss) {
                HashSet<Integer> rows=new HashSet<>(), cols=new HashSet<>();
                for (int loc:locs) {
                    int r=loc/n, c=loc%n;
                    if (!rows.contains(r) && r>=ra) {
                        for (int d=-1; d<=1; d+=2) {
                            int[] mv=locs.clone();
                            rmv(mv,r,d,n);
                            long mvc=code(mv,n);
                            if (!perms.contains(mvc)) {
                                perms.add(mvc);
                                nlocss.add(mv);
                            }
                        }
                        rows.add(r);
                    }
                    if (!cols.contains(c) && c>=ca) {
                        for (int d=-1; d<=1; d+=2) {
                            int[] mv=locs.clone();
                            cmv(mv,c,d,n);
                            long mvc=code(mv,n);
                            if (!perms.contains(mvc)) {
                                perms.add(mvc);
                                nlocss.add(mv);
                            }
                        }
                        cols.add(c);
                    }
                }
            }
            locss=nlocss;
            if (locss.size()==0)
                break;
            out++;
            System.out.println(out+": "+locss.size());
        }
        System.out.println(ra+"x"+ca+"->"+rb+"x"+cb+": "+out);
        return out;
    }
    //upper bound of God's Number for n x n Loopover
    //this is where the block extensions are determined
    //currently it is 2x2->2x3->3x3->...->(n-1)x(n-1), which does not make the best possible bounds
    /*
    because at least one row and one column must be left free before extending the solved block to 
    size n x n (solving the entire board), this algorithm is already slow at n=6
    */
    public static int upper(int n) {
        ArrayList<int[]> rs=new ArrayList<>();
        rs.add(new int[] {2,2});
        for (int i=2; i<n-1; i++) {
            rs.add(new int[] {i,i+1});
            rs.add(new int[] {i+1,i+1});
        }
        int out=0;
        for (int i=-1; i<rs.size(); i++) {
            boolean init=i==-1, fin=i+1==rs.size();
            int ra=init?0:rs.get(i)[0], ca=init?0:rs.get(i)[1],
                    rb=fin?n:rs.get(i+1)[0], cb=fin?n:rs.get(i+1)[1];
            long tst=System.currentTimeMillis();
            int cnt=maxmoves(ra,ca,rb,cb,n);
            System.out.println(System.currentTimeMillis()-tst+" ms");
            out+=cnt;
        }
        return out;
    }
    public static void main(String[] args) {
        long tst=System.currentTimeMillis();
        System.out.println(upper(5));
        System.out.println(System.currentTimeMillis()-tst+" ms");
    }
}
