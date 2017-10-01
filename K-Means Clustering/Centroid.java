import java.util.*;
class Centroid {
  private double m_x;
  private double m_y;
  private ArrayList<Node> nodes_in_cluster = new ArrayList<Node>();
  Centroid(double x, double y) {
    this.m_x = x;
    this.m_y = y;
  }
  double x() {
    return m_x;
  }
  double y() {
    return m_y;
  }
  void reset_x(double x) {
    this.m_x = x;
  }
  void reset_y(double y) {
    this.m_y = y;
  }
  void add_to_centroid(Node node) {
    if (!nodes_in_cluster.contains(node)) {
      nodes_in_cluster.add(node);
    }
  }
  void remove_from_centroid(Node node) {
    if (nodes_in_cluster.contains(node)) {
      nodes_in_cluster.remove(nodes_in_cluster.indexOf(node));
    }
  }
  ArrayList<Node> get_nodes_in_cluster() {
    return nodes_in_cluster;
  }
}
