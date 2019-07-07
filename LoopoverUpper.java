import java.util.*;
import java.io.*;
public class LoopoverUpper {
    //mod operator within interval [0,k)
    private static int mod(int n, int k) {
        int out=n%k;
        if (out<0)
            out+=k;
        return out;
    }
    //moving  a specific row or column of a loopover permutation
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
    //TODO: map locations to smaller numbers and do simple base n encoding for now
    //encoding and decoding Lehmer code
    public static long code(int[] perm, int n, int ra, int ca) {
        int[] help=new int[perm.length];
        for (int i=0; i<perm.length; i++)
            help[i]=locid(perm[i],n,ra,ca);
        long out=0;
        int max=n*n;
        for (int i=0; i<help.length; i++) {
            out*=max-i;
            out += help[i];
            for (int j=i+1; j<help.length; j++)
                if (help[j]>help[i])
                    help[j]--;
        }
        return out;
    }
    private static int[] perm(long num, int n, int ra, int ca, int k) {
        //let l[i] be lehmer code
        //num=(l[0]*(n-1)+l[1])*(n-2)+l[2])*...+l[k-1]
        int[] out=new int[k];
        int max=n*n;
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
        /*
        ex. n=4, ra=2, ca=2
        **01
        **23
        4567
        89ab (a=10, b=11)
        */
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
    //implementing integer set with int array and bitmasking
    private static boolean contains(int[] set, long num) {
        return (set[(int)(num/32)]&(1<<(num%32)))!=0;
    }
    private static void add(int[] set, long num) {
        set[(int)(num/32)]|=(1<<(num%32));
    }
    //encoding and decoding base 95
    private static String b95(long n) {
        if (n==0) return " ";
        String out="";
        while (n>0) {
            out=(char)(n%95+32)+out;
            n/=95;
        }
        return out;
    }
    private static long num(String b95) {
        long out=0;
        for (int i=0; i<b95.length(); i++) {
            out*=95;
            out+=b95.charAt(i)-32;
        }
        return out;
    }
    private static int maxmovesFolder(int ra, int ca, int rb, int cb, int n) {
        String folder=n+"x"+n+"\\" + ra + "x" + ca + "-" + rb + "x" + cb;
        int[] maxperm=new int[rb*cb-ra*ca];
        for (int i=0; i<maxperm.length; i++)
            maxperm[i]=n*n-i-1;
        long maxcode=code(maxperm,n,ra,ca);
        System.out.println("maxcode="+maxcode);
        int[] perms=new int[(int)(maxcode/32)+1];
        int out=0;
        for (int i=0; true; i++) {
            BufferedReader sc;
            try {
                sc=new BufferedReader(new FileReader(folder+"\\"+i+".txt"));
            }
            catch (Exception e) {
                out=i-1;
                break;
            }
            System.out.println("depth "+i);
            int amt=0;
            while (true) {
                String line;
                try {
                    line = sc.readLine();
                }
                catch (Exception e) {
                    System.out.println("EXCEPTION OCCURED");
                    break;
                }
                if (line==null) break;
                if (line.length()==0) continue;
                add(perms,num(line));
                amt++;
                if (amt%10_000_000==0)
                    System.out.println(amt);
            }
            System.out.println("fin="+amt);
        }
        if (out==-1) {
            int[] id=new int[rb*cb-ra*ca];
            for (int r=0, i=0; r<rb; r++)
                for (int c=0; c<cb; c++)
                    if (r>=ra || c>=ca) {
                        id[i]=r*n+c;
                        i++;
                    }
            PrintWriter writer;
            try {
                writer = new PrintWriter(new File(folder+"\\0.txt"));
            }
            catch (Exception e) {
                System.err.println(e);
                return -1;
            }
            long code=code(id,n,ra,ca);
            add(perms,code);
            writer.println(b95(code));
            writer.close();
            out=0;
        }
        while (true) {
            BufferedReader sc;
            try {
                sc=new BufferedReader(new FileReader(folder+"\\"+out+".txt"));
            }
            catch (Exception e1) {
                throw new RuntimeException(e1.toString());
                //return -1;
            }
            int amt=0;
            boolean empty=true;
            PrintWriter writer;
            try {
                writer = new PrintWriter(new File(folder+"\\"+(out+1)+".txt"));
            }
            catch (Exception e) {
                System.err.println(e);
                return -1;
            }
            System.out.println("depth="+out);
            while (true) {
                String line;
                try {
                    line = sc.readLine();
                }
                catch (Exception e) {
                    System.out.println("EXCEPTION OCCURED");
                    break;
                }
                if (line==null) break;
                if (line.length()==0) continue;
                int[] locs=perm(num(line), n, ra,ca,rb * cb - ra * ca);
                HashSet<Integer> rows=new HashSet<>(), cols=new HashSet<>();
                for (int loc:locs) {
                    int r=loc/n, c=loc%n;
                    if (!rows.contains(r) && r>=ra) {
                        for (int d=-1; d<=1; d+=2) {
                            int[] mv=locs.clone();
                            rmv(mv,r,d,n);
                            long mvc=code(mv,n,ra,ca);
                            if (!contains(perms,mvc)) {
                                add(perms,mvc);
                                writer.print(b95(mvc)+"\n");
                                empty=false;
                            }
                        }
                        rows.add(r);
                    }
                    if (!cols.contains(c) && c>=ca) {
                        for (int d=-1; d<=1; d+=2) {
                            int[] mv=locs.clone();
                            cmv(mv,c,d,n);
                            long mvc=code(mv,n,ra,ca);
                            if (!contains(perms,mvc)) {
                                add(perms,mvc);
                                writer.print(b95(mvc)+"\n");
                                empty=false;
                            }
                        }
                        cols.add(c);
                    }
                }
                amt++;
                if (amt%10_000_000==0) {
                    System.out.println(amt);
                }
            }
            writer.close();
            if (empty) break;
            out++;
        }
        check(folder);
        return out;
    }
    private static void check(String folder) {
        long tot=0;
        for (int i=0; true; i++) {
            BufferedReader sc;
            try {
                sc=new BufferedReader(new FileReader(folder+"\\"+i+".txt"));
            }
            catch (Exception e) {
                break;
            }
            long amt=0;
            while (true) {
                String line;
                try {
                    line = sc.readLine();
                }
                catch (Exception e) {
                    System.out.println("EXCEPTION OCCURED");
                    break;
                }
                if (line==null) break;
                if (line.length()==0) continue;
                amt++;
                if (amt%10_000_000==0)
                    System.out.println(amt);
            }
            System.out.println("depth "+i+": "+amt);
            tot+=amt;
        }
        System.out.println("tot="+tot);
    }
    public static void main(String[] args) {
        long tst=System.currentTimeMillis();
        System.out.println("FINAL DEPTH="+maxmovesFolder(0,0,2,3,5));
        System.out.println(System.currentTimeMillis()-tst+" ms");
    }
}
