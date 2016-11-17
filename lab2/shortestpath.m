tree = [];
distances = [];
visited = [];

function retval = cost_of_edge(v1, v2)
  global tree;
  retval = tree(v1, v2);
endfunction

function retval = smallest_unvisited()
  global distances visited;
  smallest = Inf;
  for i = 1:size(distances)
    if !visited(i) && distances(i) < smallest
      smallest = distances(i);
    endif
  endfor
  retval = smallest;
endfunction

function retval = shortestpath(t, v1, v2)
  global tree distances visited;
  tree = t;
  distances = ones(size(t, 1), 1) * Inf;
  visited = [];
  for i = 1:size(tree, 1)
    visited(i) = false;
  endfor
  visited(v1) = true;
  distances(v1) = 0;
  current = v1;
  path = [v1];
  while (current < Inf && !visited(v2))
    for neighbor = 1:size(tree, 1)
      if (visited(neighbor))
        continue;
      endif
      dist_to_neighbor = distances(current) + cost_of_edge(current, neighbor);
      if (dist_to_neighbor < distances(neighbor))
        distances(neighbor) = dist_to_neighbor;
      endif
    endfor
    visited(current) = true;
    current = smallest_unvisited();
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
