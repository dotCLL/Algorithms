class Node {
  private double m_x;
  private double m_y;
  private double dist_to_cent = 0;
  Node(double x, double y) {
    this.m_x = x;
    this.m_y = y;
  }
  double x() {
    return m_x;
  }
  double y() {
    return m_y;
  }
  void set_dist_to_cent(double dist) {
    this.dist_to_cent = dist;
  }
}
