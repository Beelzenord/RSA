test1 = [0  7  9  0  0 14
         7  0 10 15  0  0
         9 10  0 11  0  2
         0 15 11  0  6  0
         0  0  0  6  0  9
         14 0  2  0  9  0];

tree = [];
distances = [];
visited = [];
shortest_from = [];

function retval = cost_of_edge(v1, v2)
  global tree;
  retval = tree(v1, v2);
endfunction

function retval = smallest_unvisited()
  global distances visited;
  smallest_dist = Inf;
  smallest_node = -1;
  for i = 1:size(distances)
    if !visited(i) && distances(i) < smallest_dist
      smallest_node = i;
      smallest_dist = distances(i);
    endif
  endfor
  retval = smallest_node;
endfunction

function retval = shortestpath(t, v1, v2)
  global tree distances visited shortest_from;
  tree = t;
  shortest_from = ones(size(t, 1), 1) * -1;
  distances = ones(size(t, 1), 1) * Inf;
  visited = [];
  for i = 1:size(tree, 1)
    visited(i) = false;
  endfor
  visited(v1) = true;
  distances(v1) = 0;
  current = v1;
  path = [];
  while (current < Inf && !visited(v2))
    for neighbor = 1:size(tree, 1)
      if (visited(neighbor) || cost_of_edge(current, neighbor) == 0)
        continue;
      endif
      dist_to_neighbor = distances(current) + cost_of_edge(current, neighbor);
      if (dist_to_neighbor < distances(neighbor))
        distances(neighbor) = dist_to_neighbor;
        shortest_from(neighbor) = current;
      endif
    endfor
    visited(current) = true;
    current = smallest_unvisited();
  endwhile
  n = v2;
  while (n != -1)
    path = [n path];
    n = shortest_from(n);
  endwhile
  retval = path;
endfunction

#{

Let the node at which we are starting be called the initial node. Let
the distance of node Y be the distance from the initial node to
Y. Dijkstra's algorithm will assign some initial distance values and
will try to improve them step by step.

  1. Assign to every node a tentative distance value: set it to zero
     for our initial node and to infinity for all other nodes.

  2. Set the initial node as current. Mark all other nodes
     unvisited. Create a set of all the unvisited nodes called the
     unvisited set.

  3. For the current node, consider all of its unvisited neighbors and
     calculate their tentative distances. Compare the newly calculated
     tentative distance to the current assigned value and assign the
     smaller one. For example, if the current node A is marked with a
     distance of 6, and the edge connecting it with a neighbor B has
     length 2, then the distance to B (through A) will be 6 + 2 =
     8. If B was previously marked with a distance greater than 8 then
     change it to 8. Otherwise, keep the current value.

  4. When we are done considering all of the neighbors of the current
     node, mark the current node as visited and remove it from the
     unvisited set. A visited node will never be checked again.

  5. If the destination node has been marked visited (when planning a
     route between two specific nodes) or if the smallest tentative
     distance among the nodes in the unvisited set is infinity (when
     planning a complete traversal; occurs when there is no connection
     between the initial node and remaining unvisited nodes), then
     stop. The algorithm has finished.

  6. Otherwise, select the unvisited node that is marked with the
     smallest tentative distance, set it as the new "current node",
     and go back to step 3.

#}
