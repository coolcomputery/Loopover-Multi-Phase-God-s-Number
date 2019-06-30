import java.util.*;
public class LoopoverUpper {
    private static int mod(int n, int k) {
        int out=n%k;
        if (out<0)
            out+=k;
        return out;
    }
    public static void rmv(int[] locs, int r, int d, int n) {
        for (int i=0; i<locs.length; i++) {
            int rv=locs[i]/n;
            if (rv==r)
                locs[i]=rv*n+mod(locs[i]%n+d,n);
        }
    }
    public static void cmv(int[] locs, int c, int d, int n) {
        for (int i=0; i<locs.length; i++) {
            int cv=locs[i]%n;
            if (cv==c)
                locs[i]=mod(locs[i]/n+d,n)*n+cv;
        }
    }
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
    public static int maxmoves(int ra, int ca, int rb, int cb, int n) {
        //calc max possible # moves to extend ra*ca block to rb*cb block under most efficient solve
        //start with rb*cb block and bfs using all moves that do not affect the ra*ca block
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
        System.out.println(upper(6));
        System.out.println(System.currentTimeMillis()-tst+" ms");
    }
}
