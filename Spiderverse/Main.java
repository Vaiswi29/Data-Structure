public class Main {
    public static void main(String[] args) {
        int i = 0;
        int c = 1;
        int n = 5;
        int[] a = new int[n];

        while (i < n) {
            a[i] += c;
            c += 2;
            i += 1;
            System.out.println("i " + i + " c " + c);
            for (int j = 0; j < n; j++) {
                System.out.println("a " + a[j]);
            }
        }
    }
}
