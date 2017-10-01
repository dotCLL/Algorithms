// AI Assignment 3
// K-Means clustering
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;

class Clustering extends JFrame {
  ArrayList<Centroid> res_data = new ArrayList<Centroid>();
  // INPUT
  // java Clustering filename k_value [-v] [-wo filename]
  // Where '-v' creates verbose output
  // and '-wo' creates an output of the centroid final coordinates to a file of the next passed filename.
  // For example: java Clustering ./testdata/testdata1.txt 5 -v -wo testoutput1.txt
  public static void main(String[] args) throws IOException, FileNotFoundException, UnsupportedEncodingException {
    boolean dev = false;
    boolean to_write_out = false;
    String out_file = "";
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-v")) { dev = true; }
      if (args[i].equals("-wo")) { to_write_out = true; out_file = args[i+1]; }
    };
    double minX = Double.MAX_VALUE;
    double minY = Double.MAX_VALUE;
    double maxX = 0.0;
    double maxY = 0.0;
    ArrayList<Node> data_arr = read_in(args[0]);
    for (int i = 0; i < data_arr.size(); i++) {
      double exx = data_arr.get(i).x();
      double why = data_arr.get(i).y();
      if (exx < minX) { minX = exx; };
      if (exx > maxX) { maxX = exx; };
      if (why < minY) { minY = why; };
      if (why > maxY) { maxY = why; };
    }
    ArrayList<Centroid> centroid_arr = create_cents(Integer.parseInt(args[1]), minX, minY, maxX, maxY);
    if (dev) {
      System.out.println("Centroid starting coordinates:");
      print_arr(centroid_arr);
      System.out.println();
    }
    ArrayList<Centroid> resulting_centroids = k_means(data_arr, centroid_arr);
    Clustering clus = new Clustering(resulting_centroids);
    if (to_write_out) { write_out(out_file, resulting_centroids); };
    if (dev) {
      for (int i = 0; i < resulting_centroids.size(); i++) {
        System.out.println("Centroid " + (i+1) + " has " + resulting_centroids.get(i).get_nodes_in_cluster().size() + " nodes in its cluster.");
      }
      System.out.println();
      System.out.println("Centroid final coordinates:");
      print_arr(resulting_centroids);
    }
  }

  // Create the object to make the gui.
  public Clustering(ArrayList<Centroid> res) {
    this.setPreferredSize(new Dimension(900, 900));
    this.pack();
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.res_data = res;
  }

  // Paint the centroid and their connected nodes.
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    for (Centroid c : res_data) {
      Color col;
      int cen_x = ((int)c.x()) * 50;
      int cen_y = ((int)c.y()) * 50;
      g.setColor(Color.RED);
      g.fillRect(cen_x-10, cen_y-10, 20, 20);
      g.setColor(Color.BLACK);
      for (Node n : c.get_nodes_in_cluster()) {
        int node_x = ((int)n.x()) * 50;
        int node_y = ((int)n.y()) * 50;
        g.fillRect(node_x-5, node_y-5, 10, 10);
        g.setColor(Color.BLUE);
        g.drawLine(cen_x, cen_y, node_x, node_y);
      }
    }
  }

  // Print an arraylist of centroids.
  public static void print_arr(ArrayList<Centroid> arr) {
    for (int i = 0; i < arr.size(); i++) {
      System.out.println(i+1 + ": " + arr.get(i).x() + " :: " + arr.get(i).y());
    }
  }

  // Write the nodes out to a file indexed by their respective centroids.
  public static void write_out(String filename, ArrayList<Centroid> cent) throws IOException, FileNotFoundException, UnsupportedEncodingException {
    int i = 0;
    System.out.println(filename);
    Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
    for (Centroid c : cent) {
      for (Node n : c.get_nodes_in_cluster()) {
        writer.write(i + ": " + n.x() + " :: " + n.y() + "\n");
      }
      i++;
    }
    writer.close();
  }

  // Read in a file of coordinates of data points.
  public static ArrayList<Node> read_in(String filename) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
    String holder;
    int ln = 0;
    ArrayList<Node> data = new ArrayList<Node>();
    String[] substr;
    while ((holder = br.readLine()) != null) {
      if (ln != 0) {
        substr = holder.split("\\s+");
        data.add(new Node(Double.parseDouble(substr[1]), Double.parseDouble(substr[2])));
      }
      ln++;
    }
    return data;
  }

  // Create Centroid objects.
  public static ArrayList<Centroid> create_cents(Integer k, double minX, double minY, double maxX, double maxY) {
    ArrayList<Centroid> cent = new ArrayList<Centroid>();
    for (int i = 0; i < k; i++) {
      cent.add(new Centroid(random_double(minX, maxX), random_double(minY, maxY)));
    }
    return cent;
  }

  // Produce a random double within a range.
  public static double random_double(double a, double b) {
      Random rand = new Random();
      return (rand.nextDouble() * (b - a) + a);
  }

  // Calculate the euclidean distance between two coordinates.
  public static double euc_dist_calc(double x1, double y1, double x2, double y2) {
    return Math.sqrt((double)Math.pow((x2 - x1), 2) + (double)Math.pow((y2 - y1), 2));
  }

  // Find the median coordinates between a set of coordinates.
  public static ArrayList<Double> median_loc(Centroid c) {
    ArrayList<Double> res = new ArrayList<Double>();
    ArrayList<Node> nodes_in_cluster = c.get_nodes_in_cluster();
    double x = 0;
    double y = 0;
    for (Node n : nodes_in_cluster) {
      x += n.x();
      y += n.y();
    }
    if (nodes_in_cluster.size() == 0) {
      res.add(y);
      res.add(x);
    } else {
      res.add(x / (double)nodes_in_cluster.size());
      res.add(y / (double)nodes_in_cluster.size());
    }
    return res;
  }

  // Run the k-means algorithm to cluster data points to their respective Centroids.
  public static ArrayList<Centroid> k_means(ArrayList<Node> data, ArrayList<Centroid> cents) {
    for (Node n : data) {
      double euc_dist = Double.MAX_VALUE;
      Centroid closest_centroid = new Centroid((double)0, (double)0);
      for (Centroid c : cents) {
        double cent_euc_dist = euc_dist_calc(n.x(), n.y(), c.x(), c.y());
        if (cent_euc_dist <= euc_dist) {
          closest_centroid = c;
          euc_dist = cent_euc_dist;
        }
        c.remove_from_centroid(n);
      }
      closest_centroid.add_to_centroid(n);
      n.set_dist_to_cent(euc_dist);
    }
    boolean moved = false;
    for (Centroid c : cents) {
      double curr_x = c.x();
      double curr_y = c.y();
      ArrayList<Double> coord = median_loc(c);
      // If != then set centroid location to the new ones and update moved.
      if ((curr_x != coord.get(0)) || (curr_y != coord.get(1))) {
        c.reset_x(coord.get(0));
        c.reset_y(coord.get(1));
        moved = true;
      }
    }
    // If no centroids were moved then return the ArrayList else recur.
    if (moved) {
      return k_means(data, cents);
    };
    return cents;
  }

}
