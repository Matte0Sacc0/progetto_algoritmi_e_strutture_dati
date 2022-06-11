import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Esercizio4 {
   public static String readFile(String filename) throws IOException {
      String content = null;
      File file = new File(filename);
      FileReader reader = null;
      try {
         reader = new FileReader(file);
         char[] chars = new char[(int) file.length()];
         reader.read(chars);
         content = new String(chars);
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
      return content;
   }

   static String[] letters = { "0", "00", "001", "010", "0010", "0100", "0110", "0001" };
   static HashMap<String, Integer> memo = new HashMap<>();

   public static int countValidSequences(String substring, String initialString) {
      int counter = 0;
      if (memo.get(substring) != null)
         return memo.get(substring);

      for (String letter : letters) {
         String newSubstring = substring + letter;
         if (newSubstring.length() <= initialString.length()) {
            if (newSubstring.equals(initialString))
               return (counter + 1);
            else if (initialString.startsWith(newSubstring))
               counter += countValidSequences(newSubstring, initialString);
         }
      }
      memo.put(substring, counter);
      return counter;
   }

   public static void main(String[] args) throws IOException {
      Locale.setDefault(Locale.US);

      Scanner scanner = new Scanner(System.in);
      System.out.println("Indicare il nome del file sorgente compresa l'estensione: ");
      String fileName = scanner.nextLine();
      scanner.close();
      System.out.println("-------------------------------------");

      String s = readFile(fileName);

      int n = 0;
      for (String letter : letters) {
         if (letter.equals(s))
            n++;
         else
            n += countValidSequences(letter, s);
      }

      System.out.println(n);
   }
}
