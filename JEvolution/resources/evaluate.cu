extern "C"
__global__ void find_reduce_point(char *nodes, int node_count, int count, int threads, int blocks,  int *out)
{
	char TERMINAL = 0;
	char NETERMINAL = 1;
	char NONE = 0xf;
	
	int TYPE_INDEX = 0;
	int MODEL_INDEX = 1;
	int CHILD_INDEX = 2;
	int VALUE_INDEX = 3;
	
	int NODE_SIZE = 11;
	
	int node_offset = count * NODE_SIZE;
	int tid = blockIdx.x * threads + threadIdx.x;
	
	while (tid < count) {
		int start_pos = tid * NODE_SIZE;
		int child = 0;
		int actual = start_pos;
		
		if (nodes[start_pos + TYPE_INDEX] == TERMINAL) {
			out[tid] = 0;
		} 

		for (int i = start_pos + node_offset; i < (node_offset * node_count); i += node_offset) {
			if (nodes[i + TYPE_INDEX] == TERMINAL)
				child++;
			else if (nodes[i + TYPE_INDEX] == NETERMINAL) {
				child = 0;
				actual = i;
			} else {
				continue;
			}	

			if (child == nodes[actual + CHILD_INDEX]) {
				out[tid] = (actual / NODE_SIZE / count);
				break;	
			}
		}
		
		tid += threads * blocks;
	}
	__syncthreads();
}