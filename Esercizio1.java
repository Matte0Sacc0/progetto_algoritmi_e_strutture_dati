
/**
 * Matteo Sacco
 * mat. 873823
 * matteo.sacco4@studio.unibo.it
 */

/*
 ! Nell'esercizio 1 è richiesto di rispettare le seguenti specifiche:
   ? 1. La struttura S sia ad accesso diretto;
     * La struttura scelta ed implementata è un MaxHeap, alla base del quale è stato utilizzato un vettore per salvare i dati.
     * L'accesso alla struttura può essere effettuato direttamente all'indice desiderato:
     * per semplicità non sono stati implementati metodi non richiesti ai fini dell'esercizio,
     * non ne esiste quindi uno per accedere ai valori salvati in S
  
   ? 2. Il massimo numero di accessi ad S per identificare la coppia avente priorità più alta sia pari ad 1;
     * Avendo utilizzato un MaxHeap, per accedere all'elemento avente priorità maggiore è sufficiente accedere all'elemento in
     * cima all'albero, ovvero il primo elemento del vettore alla base dell'Heap.
     * Dopo l'eliminazione dell'elemento con priorità più alta verrà ripristinata la struttura MaxHeap.
  
   ? 3. L’inserimento di una coppia all’interno di S deve prevedere un numero di confronti tra elementi non superiore a 2*[log(K)];
     * Essendo il MaxHeap ordinato parzialmente, all'inserimento di un elemento il massimo numero di confronti è dato dal numero 
     * di livelli per arrivare alla cima, arrivato in cima un elemento sarà confrontato con l'attuale massimo e scambiato solo se superiore.
     * Non essendo previsto un ordinamento totale, un elemento appena inserito, viene confrontato solamente con i padri che trova salendo di livello. * 
 */

import java.util.Random;
import java.util.Arrays;

class SensorData {
   char info;
   int priority;

   SensorData(char info, int priority) {
      this.info = info;
      this.priority = priority;
   }

   public int getPriority() {
      return priority;
   }

   public String toString() {
      String str = "<" + info + "," + priority + ">";
      return str;
   }
}

class MaxHeap {
   private int capacity = 1;
   private int size = 0;
   private SensorData[] items;

   public MaxHeap() {
      items = new SensorData[capacity];
   }

   // Boilerplate Code contenente le funzioni standard di un Heap
   /**
    * * getRightChildIndex, getLeftChildIndex, getParentIndex
    * * hasRightCHild, hasLeftChild, hasParent
    * * getRightChild, getLeftCHild, getParent
    */
   private int getLeftChildIndex(int parentIndex) {
      return 2 * parentIndex + 1;
   }

   private int getRightChildIndex(int parentIndex) {
      return 2 * parentIndex + 2;
   }

   private int getParentIndex(int childIndex) {
      return (childIndex - 1) / 2;
   }

   private boolean hasLeftChild(int index) {
      return getLeftChildIndex(index) < size;
   }

   private boolean hasRightChild(int index) {
      return getRightChildIndex(index) < size;
   }

   private boolean hasParent(int index) {
      return getParentIndex(index) >= 0;
   }

   private SensorData leftChild(int index) {
      return items[getLeftChildIndex(index)];
   }

   private SensorData rightChild(int index) {
      return items[getRightChildIndex(index)];
   }

   private SensorData parent(int index) {
      return items[getParentIndex(index)];
   }

   /** ****************************************** */

   /**
    * Funzioni utilizzate internamente dall'Heap
    * * swap -> scambia due elementi
    * * checkCapacity -> controlla ci sia sufficiente spazio
    */
   private void swap(int indexOne, int indexTwo) {
      SensorData tmp = items[indexOne];
      items[indexOne] = items[indexTwo];
      items[indexTwo] = tmp;
   }

   private void checkCapacity() {
      if (size == capacity - 1) {
         items = Arrays.copyOf(items, capacity * 2);
         capacity *= 2;
      } else if (size <= 0.4 * capacity) {
         items = Arrays.copyOf(items, capacity / 2);
         capacity /= 2;
      }
   }

   /**
    * Dopo la rimozione di un elemento dalla cima dell'heap,
    * e il seguente inserimento provvisorio dell'ultimo elemento in prima
    * posizione,
    * viene ricostruita la struttura parziale dell'albero col massimo in cima
    */
   private void heapifyDown() {
      int index = 0;
      while (hasLeftChild(index)) {
         int greaterChildIndex = getLeftChildIndex(index);
         if (hasRightChild(index) && rightChild(index).getPriority() > leftChild(index).getPriority()) {
            greaterChildIndex = getRightChildIndex(index);
         }
         if (items[index].getPriority() > items[greaterChildIndex].getPriority()) {
            break;
         } else {
            swap(index, greaterChildIndex);
         }
         index = greaterChildIndex;
      }
   }

   /**
    * Dopo l'inserimento di un elemento in ultima posizione
    * lo trasporta verso la cima dell'heap,
    * fino ad ottenere un ordinamento parziale dello stesso
    */
   private void heapifyUp() {
      int index = size - 1;
      while (hasParent(index) && parent(index).getPriority() < items[index].getPriority()) {
         swap(getParentIndex(index), index);
         index = getParentIndex(index);
      }
   }

   /**
    * Medoti per l'interazione dell'utente con l'Heap
    */
   public void printHeap() {
      for (int i = 0; i < items.length; i++) {
         if (items[i] != null)
            System.out.print(items[i].toString() + " ");
      }
      System.out.println();
   }

   /**
    * restituisce il primo elemento lasciando invariata la struttura dati
    */
   public SensorData peek() {
      if (size == 0) {
         throw new IllegalAccessError();
      }
      return items[0];
   }

   /**
    * restituisce il primo elemento togliendolo dall'Heap
    * e ricostruisce la struttura dati -> Max in testa
    */
   public SensorData poll() {
      if (size == 0) {
         throw new IllegalAccessError();
      }
      SensorData item = items[0];
      items[0] = items[size - 1];
      items[size - 1] = null;
      size--;
      checkCapacity();
      heapifyDown();
      return item;
   }

   public void add(SensorData item) {
      checkCapacity();
      items[size] = item;
      size++;
      heapifyUp();
   }
}

public class Esercizio1 {
   // Inizializza la struttura dati usando la matricola come seme
   static MaxHeap initializeS(int k) {
      MaxHeap s = new MaxHeap();
      int seedMat = 873823;
      Random randomMat = new Random(seedMat);

      for (int i = 0; i < k; i++) {
         char info = (char) ('a' + randomMat.nextInt(26));
         int priority = randomMat.nextInt(100);
         SensorData sns = new SensorData(info, priority);
         s.add(sns);
      }
      return s;
   }

   // Genera un nuovo dato da inserire con il seme 3131123
   static SensorData newSensorData() {
      int seed = 3131123;
      Random randomSeed = new Random(seed);
      char info = (char) ('a' + randomSeed.nextInt(26));
      int priority = randomSeed.nextInt(100);
      SensorData sns = new SensorData(info, priority);
      return sns;
   }

   public static void main(String[] args) {
      int k = 8 + 3;
      MaxHeap s = initializeS(k);
      s.printHeap();

      // Rimozione dell'elemento massimo
      s.poll();
      s.printHeap();

      // Creazione del nuovo elemento da aggiungere ed inserimento nell'Heap
      SensorData newSns = newSensorData();
      System.out.println(newSns);

      s.add(newSns);
      s.printHeap();
   }
}