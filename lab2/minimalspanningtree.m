test1 = [0 4 5 0 0
         4 0 0 1 7
         5 0 0 2 0
         0 1 2 0 1
         0 7 0 1 0];

test2 = [0  6  3  0  9  0  0  0  0  0
         6  0  4  2  0  9  0  0  0  0
         3  4  0  2  9  0  9  0  0  0
         0  2  2  0  0  9  8  0  0  0
         9  0  9  0  0  0  8  0  0  18
         0  9  0  9  0  0  7  4  5  0
         0  0  9  8  8  7  0  0  9  10
         0  0  0  0  0  4  0  0  1  4
         0  0  0  0  0  5  9  1  0  3
         0  0  0  0  18 0  10 4  3  0];

tree = [];
verts_used = [];
mst = [];

function retval = cost_of_edge(v1, v2)
  global tree;
  retval = tree(v1, v2);
endfunction

function retval = include_next_vert()
  global verts_used tree mst;
  lowest_cost = Inf;
  lowest_used_vert_i = -1;
  lowest_unused_vert_i = -1;

  # Find the lowest edge conecting tree to other verts.
  for i = 1:size(tree, 1)     # used verts
    if (!verts_used(i))
      continue;
    endif
    for j = 1:size(tree, 1)   # unused verts
      if (verts_used(j))
        continue;
      endif
      cost = cost_of_edge(i, j);
      if (cost > 0 && cost < lowest_cost)
        lowest_cost = cost_of_edge(i, j);
        lowest_used_vert_i = i;
        lowest_unused_vert_i = j;
      endif
    endfor
  endfor

  if (lowest_cost < Inf)
    mst(size(mst, 1) + 1, :) = [lowest_used_vert_i lowest_unused_vert_i];
    verts_used(lowest_unused_vert_i) = true;
    retval = true;
  else
    retval = false;
  endif
endfunction

function retval = minimalspanningtree(t)
  global tree verts_used mst;
  tree = t;
  verts_used = [];
  mst = [];
  for i = 1:size(tree, 1)
    verts_used(i, 1) = false;
  endfor
  verts_used(1, 1) = true;

  while (include_next_vert())
  endwhile
  retval = mst;
endfunction
