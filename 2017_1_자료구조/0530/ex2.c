#include <stdio.h>
#include <limits.h>

#define TRUE  1
#define FALSE 0
#define MAX_VERTICES 5						
#define INF  10000

int weight[MAX_VERTICES][MAX_VERTICES] = {	
	{ 0, 10, 5, INF, INF },
	{ INF, 0, 2, 1, INF },
	{ INF, 3, 0, 9, 2 },
	{ INF, INF, INF, 0, 4 },
	{ 7, INF, INF, 6, 0 },
};
int distance[MAX_VERTICES];			// save the shortest path 
int S[MAX_VERTICES];				// set of vertices

// what is the next vertext with the least cost?
int choose(int n) {
	int i, min, minPos;
	min = INF;
	for (i = 0; i< n; i++)
		if ((distance[i] < min) && !S[i]) {
			min = distance[i];
			minPos = i;
		}
	return minPos;
}

// print the path with the least cost
int printStep(int step) {
	int i;

	printf("\n %3d step : S={", step);
	for (i = 0; i < MAX_VERTICES; i++)
		if (S[i] == TRUE)
			printf("%3c", i + 65);

	if (step<1) printf(" } \t\t\t");
	else if (step<4) printf(" } \t\t");
	else printf(" } \t");

	printf(" distance :[ ");
	for (i = 0; i < MAX_VERTICES; i++)
		if (distance[i] == 10000)
			printf("%4c", '*');
		else printf("%4d", distance[i]);
		printf("%4c", ']');
		return ++step;
}

void Dijkstra(int start, int n) {
	int i, u, w, step = 0;

	for (i = 0; i < n; i++) {	// initialization
		S[i] = 0;
		distance[i] = weight[start][i];
	}

	S[start] = 1;			// add start to S
	distance[start] = 0;

	step = printStep(0);		

	for (i = 0; i < n - 1; i++) {
		u = choose(n);		// find the next u that has the least cost
		S[u] = 1;			// add u to S
		for (w = 0; w < n; w++)
			if (!S[w])			// for the vertex not in S
			if (distance[w] > distance[u] + weight[u][w])
				distance[w] = distance[u] + weight[u][w]; // change the cost of the path

		step = printStep(step);	
	}
}
void main(void) {
	int i, j;
	printf("\n ********** adj matrix **********\n\n");
	for (i = 0; i < MAX_VERTICES; i++) {
		for (j = 0; j < MAX_VERTICES; j++) {
			if (weight[i][j] == 10000)
				printf("%4c", '*');
			else printf("%4d", weight[i][j]);
		}
		printf("\n\n");
	}

	printf("\n ********** Dijkstra shortest path **********\n\n");
	Dijkstra(0, MAX_VERTICES);

	getchar();
}