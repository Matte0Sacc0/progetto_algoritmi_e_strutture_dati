
/**
 * Matteo Sacco
 * mat. 873823
 * matteo.sacco4@studio.unibo.it
 */

/*
 ! Nell'esercizio 2 è richiesto di rispettare le seguenti specifiche:
   ? 1. Numero medio di accessi per a) sia (1 + N/K);
     * La struttura scelta ed implementata è una HashTable, per salvare i dati di tipo Entry(String key, char info, int hs),
     * è stato scelto un vettore di Entry. Perchè fosse possiblie inserire più elementi sotto lo stesso indice in caso di collisioni,
     * ad ogni Entry è stato associato un puntatore ad un'altra Entry. In questo modo è possibile legare due Entry in caso di collisione.
  
   ? 2. Il numero di accessi per b) sia pari a 1
     * Per rispettare questo punto non è stata utilizzata la chiave della Entry, successivamente trasmutata
     * in un intero con una funzione di hashing, bensì è stato direttamente utilizzato il valore di hs.
     * A questo punto basterà accedere all'indice corrispondente al valore cercato e contare il numero di Entry.
 */

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.io.IOException;

class Entry {
   private String key;
   private char info;
   private int hs;
   public Entry next;

   public Entry(String key, char info, int hs) {
      this.key = key;
      this.info = info;
      this.hs = hs;
      this.next = null;
   }

   public Entry() {
      this.next = null;
   }

   public String getKey() {
      return key;
   }

   public char getInfo() {
      return info;
   }

   public int getHs() {
      return hs;
   }

   public String toString() {
      return "<" + key + ", " + info + ", " + hs + ">";
   }
}

class HashTable {
   private Entry[] table;
   private int entryCount = 0;

   public HashTable() {
      this.table = initHashTable(3);
   }

   // Funzione di inizializzazione della HashTable
   private Entry[] initHashTable(int size) {
      Entry[] table = new Entry[size];
      for (int i = 0; i < size; i++) {
         table[i] = new Entry();
      }
      return table;
   }

   // Funzione di Hashing
   private int hashStringToInt(String str, int tableLength) {
      int hash = 0;
      int p = 13;
      for (int i = 0; i < str.length(); i++) {
         hash += str.codePointAt(i) * (p ^ i);
      }
      hash = hash * p;
      hash = Math.abs(hash % tableLength);
      return hash;
   }

   // Funzione per prendere il numero primo successivo, utile nel ridimensionamento
   // della tabella
   private static int getNextPrimeNumber(int num) {
      num++;
      for (int i = 2; i < num; i++) {
         if (num % i == 0) {
            num++;
            i = 2;
         } else {
            continue;
         }
      }
      return num;
   }

   private void resizeHashTable(String direction) {
      int newSize = (direction.equals("expand")) ? getNextPrimeNumber(this.table.length * 2)
            : (direction.equals("shrink")) ? getNextPrimeNumber(this.table.length / 2) : this.table.length;
      Entry[] newTable = initHashTable(newSize);
      int i = 0;
      Entry tableValue = this.table[i];
      while (tableValue != null && i < this.table.length) {
         if (tableValue.getKey() != null) {
            int index = hashStringToInt(tableValue.getKey(), newSize);
            Entry newTableValue = newTable[index];
            Entry tmpEntry = new Entry(tableValue.getKey(), tableValue.getInfo(),
                  tableValue.getHs());
            tmpEntry.next = newTableValue.next;
            newTableValue.next = tmpEntry;
         }
         if (tableValue.next != null) {
            tableValue = tableValue.next;
         } else {
            i++;
            if (i < this.table.length) {
               tableValue = this.table[i];
            }
         }
      }
      this.table = newTable;
   }

   public void add(String key, char info, int hs) {
      entryCount++;
      int loadFactor = entryCount / this.table.length;
      if (loadFactor >= 0.75) {
         resizeHashTable("expand");
      } else if (loadFactor < 0.4) {
         resizeHashTable("shrink");
      }
      int index = hashStringToInt(key, this.table.length);
      Entry tableValue = table[index];
      Entry newEntry = new Entry(key, info, hs);
      newEntry.next = tableValue.next;
      tableValue.next = newEntry;
   }

   public void findValue(Entry entry) {
      String foundItem = "elemento non presente";
      int index = hashStringToInt(entry.getKey(), this.table.length);
      Entry tableValue = table[index];
      while (tableValue != null) {
         if (tableValue.getKey() != null && tableValue.getKey().equals(entry.getKey())) {
            foundItem = "elemento presente";
            break;
         }
         tableValue = tableValue.next;
      }
      System.out.println("input: " + entry.toString() + " output: " + foundItem);
   }

   public boolean isDuplicatedEntry(String key) {
      int index = hashStringToInt(key, this.table.length);
      Entry tableValue = table[index];
      while (tableValue != null) {
         if (tableValue.getKey() != null && tableValue.getKey().equals(key)) {
            System.out.println("duplicated");
            return true;
         }
         tableValue = tableValue.next;
      }
      return false;
   }

   public void countHs(int hs) {
      int count = 0;
      int i = 0;
      Entry tableValue = table[i];
      while (tableValue != null && i < table.length) {
         if (tableValue.getKey() != null && tableValue.getHs() == hs) {
            count++;
         }
         if (tableValue.next != null) {
            tableValue = tableValue.next;
         } else {
            i++;
            if (i < table.length) {
               tableValue = table[i];
            }
         }
      }
      System.out.println("input: " + hs + ", output: " + count);
   }

   public void searchByHs(int hs) {
      Entry[] foundItems = new Entry[0];
      int i = 0;
      Entry tableValue = table[i];
      while (tableValue != null && i < table.length) {
         if (tableValue.getKey() != null && tableValue.getHs() == hs) {
            Entry item = new Entry(tableValue.getKey(), tableValue.getInfo(), tableValue.getHs());
            int sz = foundItems.length;
            foundItems = Arrays.copyOf(foundItems, sz + 1);
            foundItems[sz] = item;
         }
         if (tableValue.next != null) {
            tableValue = tableValue.next;
         } else {
            i++;
            if (i < table.length) {
               tableValue = table[i];
            }
         }
      }

      System.out.print("input: " + hs + " output: ");
      for (int j = 0; j < foundItems.length; j++) {
         if ((j == 0 && foundItems.length == 1) || j == foundItems.length - 1) {
            System.out.print(foundItems[j].toString() + "\n");
         } else {
            System.out.print(foundItems[j].toString() + ", ");
         }
      }
   }
}

public class Esercizio2 {
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

   public static void main(String[] args) throws IOException {
      String fileContent = readFile("Esercizio2_input.txt");

      int n = Integer.parseInt(fileContent.split("\n")[0].split(" ")[0].strip());
      int k = Integer.parseInt(fileContent.split("\n")[0].split(" ")[1].strip());

      HashTable d = new HashTable();

      /**
       * Riempimento del dizionario D con i valori Entry <Chiave, info, hs> letti dal
       * file
       */
      for (int i = 1; i <= n; i++) {
         String line = fileContent.split("\n")[i];
         int hs = Integer.parseInt(line.split(" ")[2].strip());
         if (hs >= 0 && hs < k) {
            String key = line.split(" ")[0].strip();
            if (!d.isDuplicatedEntry(key)) {
               char info = line.split(" ")[1].strip().charAt(0);
               d.add(key, info, hs);
            }
         }
      }

      Scanner scanner = new Scanner(System.in);
      System.out.println("Lista dei comandi:");
      System.out.println("\tscrivere anche il carattere che precede il copmando \'a, b, c\'");
      System.out.println("- \'a <Chiave0, a, 0>\' fornire elemento per verificare se presente");
      System.out.println("- \'b 1\' fornire valore hs per contarne le occorrenze");
      System.out.println("- \'c 1\' fornire valore di hs per visualizzare la lista degli elementi aventi quell\'hs");
      System.out.println();

      System.out.println("- \'stop\' per uscire");
      String input;
      do {
         input = scanner.nextLine().stripLeading();
         char action = input.toLowerCase().charAt(0);

         if (action == 'a' || action == 'b' || action == 'c' || input.toLowerCase().equals("stop")) {
            if (action == 'a') {
               input = input.substring(1).stripLeading().replaceAll("<", "").replaceAll(">", "").replaceAll(" ", "");
               String key = input.split(",")[0].strip();
               char info = input.split(",")[1].strip().charAt(0);
               int hs = Integer.parseInt(input.split(",")[2].strip());
               Entry entry = new Entry(key, info, hs);
               d.findValue(entry);
            }
            if (action == 'b') {
               int hs = Integer.parseInt(input.split(" ")[1].strip());
               d.countHs(hs);
            }
            if (action == 'c') {
               int hs = Integer.parseInt(input.split(" ")[1].strip());
               d.searchByHs(hs);
            }
         } else {
            System.out.println("Indicare l'operazione da svolgere correttamente:");
         }
      } while (!input.toLowerCase().equals("stop"));
      scanner.close();
   }
}
