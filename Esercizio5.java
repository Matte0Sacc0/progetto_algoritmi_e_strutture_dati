import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Scanner;

class Node {
   private int id;
   private LinkedList<Arch> arches;
   private Arch parentLink;

   Node(int id) {
      this.id = id;
      this.arches = new LinkedList<>();
   }

   public int getId() {
      return id;
   }

   public void addArch(Arch arch) {
      this.arches.addLast(arch);
   }

   public LinkedList<Arch> getArches() {
      return arches;
   }

   public Arch getParentLink() {
      return parentLink;
   }

   public void setParentLink(Arch parentLink) {
      this.parentLink = parentLink;
   }

   public String toString() {
      String str = "" + this.id;
      for (int i = 0; i < arches.size(); i++) {
         str += "\n" + arches.get(i).toString();
      }
      return str;
   }

}

class Arch {
   private int head;
   private int tail;
   private char type;
   private float length;
   private float cost;

   Arch(int head, int tail, char type, float length, float cost) {
      this.head = head;
      this.tail = tail;
      this.type = type;
      this.length = length;
      this.cost = cost;
   }

   public int getHead() {
      return head;
   }

   public int getTail() {
      return tail;
   }

   public char getType() {
      return type;
   }

   public float getLength() {
      return length;
   }

   public float getCost() {
      return cost;
   }

   public String toString() {
      String str = this.type + " " + this.head + " " + this.tail + " " + this.length;
      if (this.cost != -1) {
         str += " " + this.cost;
      }
      return str;
   }
}

class Graph {
   private Node[] graph;
   private int nodes;
   private int entryCount = 0;

   Graph(int n) {
      this.graph = new Node[n];
      this.nodes = n;
   }

   public void add(Node node) {
      this.graph[this.entryCount] = node;
      this.entryCount++;
   }

   public Node getNode(int id) {
      return this.graph[id];
   }

   public String findDijkstraPath(int source, int tail, char pathType) {
      String str = "non raggiungibile";
      float pathLength[] = new float[this.nodes];
      float pathCost[] = new float[this.nodes];
      boolean[] visitedNodes = new boolean[this.nodes];

      for (int i = 0; i < this.nodes; i++) {
         pathLength[i] = Float.POSITIVE_INFINITY;
         pathCost[i] = 0;
         visitedNodes[i] = false;
      }

      pathLength[source] = 0;
      pathCost[source] = 0;
      visitedNodes[source] = true;
      graph[source].setParentLink(null);

      PriorityQueue<Integer> s = new PriorityQueue<>();
      s.add(source);

      while (!s.isEmpty()) {
         Node u = graph[s.poll()];
         visitedNodes[u.getId()] = false;
         for (Arch arch : u.getArches()) {
            boolean validPath = pathType == 'm' ? true : pathType == arch.getType();
            float estimatedLength = arch.getLength() + pathLength[u.getId()];
            int v = arch.getTail();
            if (validPath && estimatedLength < pathLength[v]) {
               pathLength[v] = estimatedLength;
               pathCost[v] = arch.getCost() + pathCost[u.getId()];
               graph[v].setParentLink(arch);

               if (!visitedNodes[v]) {
                  s.add(v);
                  visitedNodes[v] = true;
               }
            }
         }
      }

      Arch tmp = graph[tail].getParentLink();
      if (tmp != null) {
         str = "";
         float totalLength = 0;
         float totalCost = 0;
         int index = tail;
         while (index != 0) {
            str = "" + tmp.getType() + " " + tmp.getHead() + " "
                  + tmp.getTail()
                  + "\n" + str;
            totalLength += tmp.getLength();
            totalCost += tmp.getCost();
            index = tmp.getHead();
            tmp = graph[index].getParentLink();
         }
         str += totalCost == 0 ? totalLength : totalLength + "\n" + totalCost;
      }
      return str;
   }

   public void printGraph() {
      System.out.println();
      for (int i = 0; i < graph.length; i++) {
         System.out.println(graph[i].toString());
         System.out.println();
      }
      System.out.println();
   }
}

public class Esercizio5 {
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
      Locale.setDefault(Locale.US);

      Scanner scanner = new Scanner(System.in);
      System.out.println("Indicare il nome del file sorgente compresa l'estensione: ");
      String fileName = scanner.nextLine();
      String fileContent = readFile(fileName);
      scanner.close();
      System.out.println("-------------------------------------");

      String[] fileLines = fileContent.split("\n");

      int n = Integer.parseInt(fileLines[0].split(" ")[0].strip());
      int m = Integer.parseInt(fileLines[1].split(" ")[0].strip());

      Graph graph = new Graph(n);

      for (int i = 0; i < n; i++) {
         graph.add(new Node(i));
      }

      for (int i = 0; i < m; i++) {
         String fileLine = fileLines[i + 2].strip();

         char type = fileLine.charAt(0);
         int head = Integer.parseInt(fileLine.split(" ")[1]);
         int tail = Integer.parseInt(fileLine.split(" ")[2]);
         float length = Float.parseFloat(fileLine.split(" ")[3]);
         float cost = type == 'p' ? 0 : Float.parseFloat(fileLine.split(" ")[4]);

         Arch arch = new Arch(head, tail, type, length, cost);

         graph.getNode(head).addArch(arch);
      }

      String pathP = graph.findDijkstraPath(0, 6, 'p');
      System.out.println(pathP);
      String pathT = graph.findDijkstraPath(0, 6, 'm');
      System.out.println(pathT);
   }
}
