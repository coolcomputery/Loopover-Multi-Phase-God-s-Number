import java.util.*;
import java.io.*;
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
            out*=n-i;
            out += help[i];
            for (int j=i+1; j<help.length; j++)
                if (help[j]>help[i])
                    help[j]--;
        }
        return out;
    }
    private static int[] perm(long num, int n, int k) {
        //let l[i] be lehmer code
        //num=(l[0]*(n-1)+l[1])*(n-2)+l[2])*...+l[k-1]
        int[] out=new int[k];
        for (int i=k-1; i>-1; i--) {
            out[i]=(int)(num%(n-i));
            num/=n-i;
        }
        for (int i=k-1; i>-1; i--)
            for (int j=i+1; j<k; j++)
                if (out[j]>=out[i])
                    out[j]++;
        return out;
    }
    private static ArrayList<int[]> nextDepth(ArrayList<int[]> locss, Set<Long> perms, int ra, int ca, int rb, int cb, int n) {
        ArrayList<int[]> nlocss=new ArrayList<>();
        int amt=0;
        for (int[] locs:locss) {
            HashSet<Integer> rows=new HashSet<>(), cols=new HashSet<>();
            for (int loc:locs) {
                int r=loc/n, c=loc%n;
                if (!rows.contains(r) && r>=ra) {
                    for (int d=-1; d<=1; d+=2) {
                        int[] mv=locs.clone();
                        rmv(mv,r,d,n);
                        long mvc=code(mv,n*n);
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
                        long mvc=code(mv,n*n);
                        if (!perms.contains(mvc)) {
                            perms.add(mvc);
                            nlocss.add(mv);
                        }
                    }
                    cols.add(c);
                }
            }
            amt++;
            if (amt%500_000==0)
                System.out.println(amt+"/"+locss.size());
        }
        return nlocss;
    }
    //using an int array instead of a HashSet to store non-negative integers
    private static boolean contains(int[] set, long num) {
        return (set[(int)(num/32)]&(1<<(num%32)))!=0;
    }
    private static void add(int[] set, long num) {
        set[(int)(num/32)]|=(1<<(num%32));
    }
    //stores permutations in text files
    private static int maxmovesFolder(int ra, int ca, int rb, int cb, int n) {
        String folder=n+"x"+n+"\\" + ra + "x" + ca + "-" + rb + "x" + cb;
        int[] maxperm=new int[rb*cb-ra*ca];
        for (int i=0; i<maxperm.length; i++)
            maxperm[i]=n*n-i-1;
        long maxcode=code(maxperm,n*n);
        System.out.println("maxcode="+maxcode);
        int[] perms=new int[(int)(maxcode/32)+1];
        int out=0;
        for (int i=0; true; i++) {
            BufferedReader sc;
            try {
                sc=new BufferedReader(new FileReader(folder+"\\"+i+".n6"));
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
                add(perms,(int)num(line));
                amt++;
                if (amt%1_000_000==0)
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
                writer = new PrintWriter(new File(folder+"\\0.n6"));
            }
            catch (Exception e) {
                System.err.println(e);
                return -1;
            }
            writer.println(byte6(code(id,n*n),30));
            writer.close();
            out=0;
        }
        while (true) {
            BufferedReader sc;
            try {
                sc=new BufferedReader(new FileReader(folder+"\\"+out+".n6"));
            }
            catch (Exception e1) {
                throw new RuntimeException(e1.toString());
                //return -1;
            }
            int amt=0;
            boolean empty=true;
            PrintWriter writer;
            try {
                writer = new PrintWriter(new File(folder+"\\"+(out+1)+".n6"));
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
                int[] locs=perm(num(line), n * n, rb * cb - ra * ca);
                HashSet<Integer> rows=new HashSet<>(), cols=new HashSet<>();
                for (int loc:locs) {
                    int r=loc/n, c=loc%n;
                    if (!rows.contains(r) && r>=ra) {
                        for (int d=-1; d<=1; d+=2) {
                            int[] mv=locs.clone();
                            rmv(mv,r,d,n);
                            long mvc=code(mv,n*n);
                            if (!contains(perms,mvc)) {
                                add(perms,mvc);
                                writer.print(byte6(mvc,30)+"\n");
                                empty=false;
                            }
                        }
                        rows.add(r);
                    }
                    if (!cols.contains(c) && c>=ca) {
                        for (int d=-1; d<=1; d+=2) {
                            int[] mv=locs.clone();
                            cmv(mv,c,d,n);
                            long mvc=code(mv,n*n);
                            if (!contains(perms,mvc)) {
                                add(perms,mvc);
                                writer.print(byte6(mvc,30)+"\n");
                                empty=false;
                            }
                        }
                        cols.add(c);
                    }
                }
                amt++;
                if (amt%500_000==0) {
                    System.out.println(amt);
                }
            }
            writer.close();
            if (empty) break;
            out++;
        }
        return out;
    }
    //does not store permutations in text files
    private static int maxmoves(int ra, int ca, int rb, int cb, int n) {
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
        perms.add(code(id,n*n));
        ArrayList<int[]> locss=new ArrayList<>();
        locss.add(id);
        to_n6(locss,"5x5\\" + ra + "x" + ca + "-" + rb + "x" + cb+"\\"+0+".n6",n);
        int out=0;
        while (out<7) {
            locss=nextDepth(locss,perms,ra,ca,rb,cb,n);
            if (locss.size()>0) {
                out++;
                System.out.println(out+": "+locss.size());
                to_n6(locss,"5x5\\" + ra + "x" + ca + "-" + rb + "x" + cb+"\\"+out+".n6",n);
            }
            else break;
        }
        System.out.println(ra+"x"+ca+"->"+rb+"x"+cb+": "+out);
        System.out.println("tot="+perms.size());
        return out;
    }
    private static void to_n6(ArrayList<int[]> locss, String file, int n) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(new File(file));
        }
        catch (Exception e) {
            System.err.println(e);
            return;
        }
        Set<Long> deeps=new HashSet<>();
        for (int[] locs:locss)
            deeps.add(code(locs,n*n));
        StringBuilder txt=new StringBuilder();
        for (long code:deeps)
            txt.append(byte6(code,30)+"\n");
        writer.println(txt);
        writer.close();
    }
    private static String byte6(long n, int bits) {
        bits=(int)(6*Math.ceil(bits/6.0));
        String out="";
        for (int i=0; i<bits; i+=6) {
            int amt=0;
            for (int j=0; j<6 && i+j<bits; j++) {
                if ((n&(1<<(i+j)))>0)
                    amt+=1<<j;
            }
            out=(char)(amt+63)+out;
        }
        return out;
    }
    private static long num(String byte6) {
        long out=0;
        for (int i=0; i<byte6.length(); i++) {
            out<<=6;
            out|=(int)(byte6.charAt(i))-63;
        }
        return out;
    }
    public static int upper(int n) {
        ArrayList<int[]> rs=new ArrayList<>();
        /*for (int i=2; i<n; i++)
            rs.add(new int[] {i,i});*/
        rs.add(new int[] {2,2});
        rs.add(new int[] {3,3});
        rs.add(new int[] {3,4});
        rs.add(new int[] {4,4});
        //rs.add(new int[] {2,3});
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
        System.out.println("FINAL DEPTH="+maxmovesFolder(0,0,3,3,4));
        System.out.println(System.currentTimeMillis()-tst+" ms");
    }
}
