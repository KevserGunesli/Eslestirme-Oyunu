import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public char[][] grid; // Oyun tahtası
    private int gridSize; // Izgara boyutu
    private ArrayList<Character> symbols; // Kullanılacak semboller
    private int correctGuesses; // Doğru tahmin sayısı
    private int wrongGuesses; // Yanlış tahmin sayısı
    private long startTime; // Oyunun başladığı zaman

    public Main(int gridSize) {
        this.gridSize = gridSize;
        initializeSymbols();
        initializeGrid();
        correctGuesses = 0;
        wrongGuesses = 0;
        startTime = System.currentTimeMillis(); // Oyunun başladığı zamanı kaydet
    }

    // Izgara oluşturma ve değerleri atamArrayList<Character> tempSymbols = new ArrayList<>(symbols.subList(0, gridSize * gridSize / 2));a
    private void initializeGrid() {
        grid = new char[gridSize][gridSize];
        ArrayList<Character> tempSymbols = new ArrayList<>(symbols.subList(0, gridSize * gridSize / 3));

        // Rastgele sembollerin yerleştirilmesi
        for (char symbol : tempSymbols) {
            for (int i = 0; i < 3; i++) {
                // Boş bir kutu bulana kadar rastgele konum seç
                int row, col;
                do {
                    row = (int) (Math.random() * gridSize);
                    col = (int) (Math.random() * gridSize);
                } while (grid[row][col] != '\u0000'); // Boş kutu kontrolü

                // Sembolu yerleştir
                grid[row][col] = symbol;
            }
        }
    }

    // Kullanılacak sembolleri başlatma
    private void initializeSymbols() {
        symbols = new ArrayList<>(); // Doğru değişkeni kullanıldı

        for (char c = '0'; c <= '9'; c++) {
            symbols.add(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            symbols.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            symbols.add(c);
        }
        Collections.shuffle(symbols); // Sembollerin sırasını karıştır
    }

    // Izgarayı yazdırma
    public void printGrid(boolean[][] revealed) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (revealed[i][j]) {
                    System.out.print(grid[i][j] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
    }


        

    // Kullanıcının tahminini kontrol etme
    public boolean checkGuess(int row1, int col1, int row2, int col2, int row3, int col3, boolean[][] revealed, char[][] grid) {

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if ((row1==i && col1==j) || (row2==i && col2==j) || (row3==i && col3==j)) {
                    System.out.print(grid[i][j] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
        
        if (grid[row1][col1] == grid[row2][col2] && grid[row2][col2] == grid[row3][col3]) {
            revealed[row1][col1] = true;
            revealed[row2][col2] = true;
            revealed[row3][col3] = true;
            correctGuesses += 3;
            return true;
        } else {
            wrongGuesses++;
            //printGrid(revealed); // Yanlış tahminde, doğru tahminlerin olduğu noktaları göster

            // Kullanıcının tahmin ettiği kutucukları göstermek için 3 saniye beklet
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Yanlış tahmin sonrasında tekrar ızgarayı temizleme
            System.out.print("\033[H\033[2J");
            System.out.flush();

            return false;
        }
    }

    // Puanlama fonksiyonu
    public int calculateScore() {
        long endTime = System.currentTimeMillis(); // Oyunun bittiği zaman
        long totalTime = (endTime - startTime) / 1000; // Geçen süreyi saniyeye çevir

        // Her doğru tahmin için 10 puan
        int correctGuessScore = correctGuesses * 10;

        // Her yanlış tahmin için 5 puan
        int wrongGuessScore = wrongGuesses * 5;

        // Geçen süreye göre ek puan (örneğin her 1 saniye için 1 puan)
        int timeScore = Math.max(0, 60 - (int) totalTime); // En fazla 60 saniyede 60 puan alabilir

        // Toplam puanı hesapla
        int totalScore = correctGuessScore - wrongGuessScore + timeScore;

        return totalScore;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int gridSize=1;
        while(gridSize%3!=0 || gridSize<3 || gridSize>12)
        {
            System.out.print("Izgara boyutunu girin: ");
            gridSize = scanner.nextInt();
            scanner.nextLine(); // Satır sonu karakterini oku
        }
        Main game = new Main(gridSize); // Kullanıcının girdiği boyutta bir ızgara oluştur

        boolean[][] revealed = new boolean[gridSize][gridSize]; // Açılan kutuları takip etmek için ızgara boyutunda bir boolean dizisi
        while (game.correctGuesses < gridSize * gridSize) {
            game.printGrid(revealed); // Izgarayı yazdır
            System.out.println("Birinci tahmin için satır ve sütun numaralarını girin (örn. 0 1): ");
            int row1 = scanner.nextInt();
            int col1 = scanner.nextInt();
            System.out.println("İkinci tahmin için satır ve sütun numaralarını girin (örn. 1 2): ");
            int row2 = scanner.nextInt();
            int col2 = scanner.nextInt();

            System.out.println("Üçüncü tahmin için satır ve sütun numaralarını girin (örn. 1 2): ");
            int row3 = scanner.nextInt();
            int col3 = scanner.nextInt();

            if (game.checkGuess(row1, col1, row2, col2, row3, col3, revealed, game.grid)) {
                System.out.println("Tebrikler, doğru tahmin!");
            } else {
                System.out.println("Maalesef, yanlış tahmin. 3 saniye bekleyin...");
                try {
                    Thread.sleep(3000); // Yanlış tahminde 3 saniye bekletme
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Ekranı temizle
            System.out.print("\033[H\033[2J");
           
            System.out.flush();
        }
        scanner.close();

        int totalScore = game.calculateScore(); // Oyun bittiğinde puanı hesapla
        System.out.println("Tüm ızgaralar doğru tahmin edildi!");
        System.out.println("Toplam puanınız: " + totalScore);
    }
}