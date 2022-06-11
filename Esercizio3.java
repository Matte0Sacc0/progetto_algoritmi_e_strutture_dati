import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

class Point {
   private float x;
   private float y;

   Point(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public String toString() {
      return "[" + x + "; " + y + "]";
   }
}

class Node {
   private Point head;
   private int tail;
   private double minimumDistance = Double.POSITIVE_INFINITY;
   private double[] connectionCost;

   Node(int connections) {
      this.connectionCost = new double[connections];
   }

   public void insertDistance(double distance, int index) {
      this.connectionCost[index] = distance;
      if (distance < this.minimumDistance) {
         setMinimumDistance(distance);
         setTail(index);
      }
   }

   public void setHead(Point head) {
      this.head = head;
   }

   public Point getHead() {
      return this.head;
   }

   public void setTail(int tail) {
      this.tail = tail;
   }

   public int getTail() {
      return this.tail;
   }

   public void setMinimumDistance(double distance) {
      this.minimumDistance = distance;
   }

   public double getMinimumDistance() {
      return this.minimumDistance;
   }

   public String toString() {
      String str = this.head.toString() + " {";
      for (int i = 0; i < connectionCost.length; i++) {
         str += connectionCost[i];
         if (i != connectionCost.length - 1) {
            str += ", ";
         }
      }
      str += "} " + this.tail;
      return str;
   }
}

class GraphMST {
   private Node[] graph;
   private int[][] visited;
   private int entryCount = 0;

   GraphMST(int n) {
      this.graph = initializeGraph(n);
      this.visited = new int[n - 1][2];
   }

   private Node[] initializeGraph(int size) {
      Node[] tmpGraph = new Node[size];
      for (int i = 0; i < size; i++) {
         tmpGraph[i] = new Node(size);
      }
      return tmpGraph;
   }

   private double calculateDistance(Point firstPoint, Point secondPoint) {
      double distance = -1;
      double distanceX = Math.pow((firstPoint.getX() - secondPoint.getX()), 2);
      double distanceY = Math.pow((firstPoint.getY() - secondPoint.getY()), 2);
      distance = Math.sqrt((distanceX + distanceY));
      return distance;
   }

   public void insertPoint(Point point) {
      double min = Double.POSITIVE_INFINITY;
      graph[entryCount].setHead(point);
      graph[entryCount].setTail(entryCount);
      graph[entryCount].insertDistance(min, entryCount);
      for (int i = 0; i < entryCount; i++) {
         double distance = calculateDistance(point, graph[i].getHead());
         graph[entryCount].insertDistance(distance, i);
         graph[i].insertDistance(distance, entryCount);
         if (distance < min) {
            min = distance;
            graph[entryCount].setTail(i);
            graph[entryCount].setMinimumDistance(distance);
            if (distance < graph[i].getMinimumDistance()) {
               graph[i].setMinimumDistance(distance);
               graph[i].setTail(entryCount);
            }
         }
      }
      entryCount++;
   }

   public void getMST() {
      int k = 0;
      for (int i = 0; i < graph.length; i++) {
         boolean newRoute = true;
         for (int j = 0; j < visited.length; j++) {
            if ((visited[j][0] == i && visited[j][1] == graph[i].getTail())
                  || (visited[j][0] == graph[i].getTail() && visited[j][1] == i)) {
               newRoute = false;
            }
         }
         if (newRoute) {
            visited[k][0] = i;
            visited[k][1] = graph[i].getTail();
            k++;
         }
      }

      printMST();
   }

   public void printMST() {
      System.out.println();
      double sum = 0;
      for (int i = 0; i < visited.length; i++) {
         double cost = graph[visited[i][0]].getMinimumDistance();
         sum += cost;
         System.out.println(visited[i][0] + " " + visited[i][1] + " " + cost);
      }
      System.out.println("\n" + sum + "\n");
   }

   public void printGraph() {
      System.out.println();
      for (int i = 0; i < graph.length; i++) {
         System.out.println(graph[i].toString());
      }
      System.out.println();
   }
}

public class Esercizio3 {
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
      String fileContent = readFile("Esercizio3_input.txt");

      int n = Integer.parseInt(fileContent.split("\n")[0].split(" ")[0].strip());

      GraphMST graph = new GraphMST(n);

      for (int i = 1; i <= n; i++) {
         String line = fileContent.split("\n")[i];
         float x = Float.parseFloat(line.split(" ")[0].strip());
         float y = Float.parseFloat(line.split(" ")[1].strip());
         Point point = new Point(x, y);
         graph.insertPoint(point);
      }

      graph.getMST();
   }
}
