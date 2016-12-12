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
