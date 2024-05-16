import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public char[][] tahta; // Oyun tahtası
    private int tahtaBoyu; // Izgara boyutu
    private ArrayList<Character> elemanlar; // Kullanılacak semboller
    private int dogruTahmin; // Doğru tahmin sayısı
    private int yanlisTahmin; // Yanlış tahmin sayısı
    private long baslangic; // Oyunun başladığı zaman

    public Main(int gridSize) {
        this.tahtaBoyu = gridSize;
        ElemanYerlestirme();
        matrisOlusturma();
        dogruTahmin = 0;
        yanlisTahmin = 0;
        baslangic = System.currentTimeMillis(); // Oyunun başladığı zamanı kaydet
    }
    
    // Izgara oluşturma ve değerleri atama
    private void matrisOlusturma() {
        tahta = new char[tahtaBoyu][tahtaBoyu];
        ArrayList<Character> tempSymbols = new ArrayList<>(elemanlar.subList(0, tahtaBoyu * tahtaBoyu / 3));

        // Rastgele sembollerin yerleştirilmesi
        for (char symbol : tempSymbols) {
            for (int i = 0; i < 3; i++) {
                // Boş bir kutu bulana kadar rastgele konum seç
                int row, col;
                do {
                    row = (int) (Math.random() * tahtaBoyu);
                    col = (int) (Math.random() * tahtaBoyu);
                } while (tahta[row][col] != '\u0000' || !SutunBosMu(col, symbol)); // Boş kutu ve sütun kontrolü

                // Sembolu yerleştir
                tahta[row][col] = symbol;
            }
        }
    }

    // Aynı sembolün farklı sütunlara yerleştirildiğinden emin olmak için sütun kontrolü
    private boolean SutunBosMu(int col, char symbol) {
        for (int row = 0; row < tahtaBoyu; row++) {
            if (tahta[row][col] == symbol) {
                return false;
            }
        }
        return true;
    }

    // Kullanılacak sembolleri başlatma
    private void ElemanYerlestirme() {
        elemanlar = new ArrayList<>(); // Doğru değişkeni kullanıldı

        for (char c = '0'; c <= '9'; c++) {
            elemanlar.add(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            elemanlar.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            elemanlar.add(c);
        }
        Collections.shuffle(elemanlar); // Sembollerin sırasını karıştır
    }

    // Izgarayı yazdırma
    public void matrisYazdirma(boolean[][] revealed) {
        for (int i = 0; i < tahtaBoyu; i++) {
            for (int j = 0; j < tahtaBoyu; j++) {
                if (revealed[i][j]) {
                    System.out.print(tahta[i][j] + " ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }
    }

    // Kullanıcının tahminini kontrol etme
    public boolean TahminKontrol(int row1, int col1, int row2, int col2, int row3, int col3, boolean[][] revealed, char[][] grid) {
        for (int i = 0; i < tahtaBoyu; i++) {
            for (int j = 0; j < tahtaBoyu; j++) {
                if ((row1 == i && col1 == j) || (row2 == i && col2 == j) || (row3 == i && col3 == j)) {
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
            dogruTahmin += 3;
            return true;
        } else {
            yanlisTahmin++;

            // Kullanıcının tahmin ettiği kutucukları göstermek için 3 saniye beklet
            return false;
        }
    }

    // Puanlama fonksiyonu
    public int Skor() {
        long endTime = System.currentTimeMillis(); // Oyunun bittiği zaman
        long totalTime = (endTime - baslangic) / 1000; // Geçen süreyi saniyeye çevir

        // Her doğru tahmin için 10 puan
        int correctGuessScore = dogruTahmin * 10;

        // Her yanlış tahmin için 5 puan
        int wrongGuessScore = yanlisTahmin * 5;

        // Geçen süreye göre ek puan (örneğin her 1 saniye için 1 puan)
        int timeScore = Math.max(0, 60 - (int) totalTime); // En fazla 60 saniyede 60 puan alabilir

        // Toplam puanı hesapla
        int totalScore = correctGuessScore - wrongGuessScore + timeScore;

        return totalScore;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int gridSize = 1;
        while (gridSize % 3 != 0 || gridSize < 3 || gridSize > 12) {
            System.out.print("Izgara boyutunu girin: ");
            gridSize = scanner.nextInt();
            scanner.nextLine(); // Satır sonu karakterini oku
        }
        Main game = new Main(gridSize); // Kullanıcının girdiği boyutta bir ızgara oluştur

        boolean[][] revealed = new boolean[gridSize][gridSize]; // Açılan kutuları takip etmek için ızgara boyutunda bir boolean dizisi
        while (game.dogruTahmin < gridSize * gridSize) {
            game.matrisYazdirma(revealed); // Izgarayı yazdır

            System.out.println("Birinci tahmin icin satir ve sutun numaralarini girin 0(dahil) ile " + gridSize + " arasinda degerler secmelisin. Orn: 0 0");
            int row1 = scanner.nextInt();
            int col1 = scanner.nextInt();
            System.out.println("Ikinci tahmin icin satir ve sutun numaralarini girin 0(dahil) ile " + gridSize + " arasinda degerler secmelisin. Orn: 1 1");
            int row2 = scanner.nextInt();
            int col2 = scanner.nextInt();

            System.out.println("Ucuncu tahmin icin sati ve sutun numaralarini girin 0(dahil) ile " + gridSize + " arasinda degerler secmelisin. Orn: 1 2");
            int row3 = scanner.nextInt();
            int col3 = scanner.nextInt();

            if (game.TahminKontrol(row1, col1, row2, col2, row3, col3, revealed, game.tahta)) {
                System.out.println("Tebrikler, dogru tahmin!");
            } else {
                System.out.println("Maalesef, yanlis tahmin. 3 saniye bekleyin...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                Temizlik.temizle();
            }
        }
        scanner.close();

        int totalScore = game.Skor(); // Oyun bittiğinde puanı hesapla
        System.out.println("Tum izgaralar dogru tahmin edildi!");
        System.out.println("Toplam puaniniz: " + totalScore);
    }
}
