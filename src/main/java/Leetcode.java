import java.util.HashSet;
import java.util.Set;

public class Leetcode {
  static class Node {
    Node parent;
  }

  public int numIslands(char[][] grid) {
    final int n = grid.length;
    final int m = grid[0].length;
    var dp = new Node[m];
    Set<Node> roots = new HashSet<>();
    for (int i = 0; i < n; i++) {
      var dpNew = new Node[m];
      if (grid[i][0] == '1') {
        if (dp[0] == null) {
          roots.add(dpNew[0] = new Node());
        } else {
          dpNew[0] = dp[0];
        }
      } else {
        dpNew[0] = null;
      }
      for (int j = 1; j < m; j++) {
        if (grid[i][j] == '0') {
          continue;
        }
        if (dpNew[j - 1] == null && dp[j] == null) {
          roots.add(dpNew[j] = new Node());
        } else if (dpNew[j - 1] == null) {
          dpNew[j] = dp[j];
        } else if (dp[j] == null) {
          dpNew[j] = dpNew[j - 1];
        } else {
          // Both not null
          if (dpNew[j - 1] != dp[j]) {
            dpNew[j - 1] = getRoot(dpNew[j - 1]);
            dp[j] = getRoot(dp[j]);
            if (dpNew[j - 1] != dp[j]) {
              dpNew[j - 1].parent = dp[j];
              roots.remove(dpNew[j - 1]);
            }
            dpNew[j] = dp[j];
          } else {
            dpNew[j] = dp[j];
          }
        }
      }
      dp = dpNew;
    }
    return roots.size();
  }

  static Node getRoot(Node node) {
    var r = node;
    while (node.parent != null) {
      node = node.parent;
    }
    // Collapse
    while (r.parent != null) {
      var t = r;
      r = r.parent;
      t.parent = node;
    }
    return node;
  }

  public static void main(String[] args) {
      var in = new char[4][];
      in[0] = new char[]{'1','1','0','0','0'};
      in[1] = new char[]{'1','1','0','0','0'};
      in[2] = new char[]{'0','0','1','0','0'};
      in[3] = new char[]{'0','0','0','1','1'};
      System.out.println(new Leetcode().numIslands(in));
  }
}
