#include <stdio.h>
#include <memory.h>
#include <stdlib.h>
#define MAX_VERTEX 10
#define FALSE 0
#define TRUE 1

typedef struct graphNode {
	int vertex;
	struct graphNode* link;
} graphNode;

typedef struct graphType {
	int n;
	graphNode* adjList_H[MAX_VERTEX];	// list 
	int visited[MAX_VERTEX];			// visited
} graphType;

// STACK
// we are using stack
typedef int element;

typedef struct stackNode {
	int data;
	struct stackNode *link;
} stackNode;

stackNode* top;

// check if stack is empty
int isEmpty() {
	if (top == NULL) return 1;
	else return 0;
}

void push(int item) {
	stackNode* temp = (stackNode *)malloc(sizeof(stackNode));
	temp->data = item;
	temp->link = top;
	top = temp;
}

int pop() {
	int item;
	stackNode* temp = top;

	if (isEmpty()) {
		printf("\n\n Stack is empty !\n");
		return 0;
	}
	else {
		item = temp->data;
		top = temp->link;
		free(temp);
		return item;
	}
} // end of STACK


// create Graph
void createGraph(graphType* g) {
	int v;
	g->n = 0;							// 
	for (v = 0; v<MAX_VERTEX; v++) {
		g->visited[v] = FALSE;			// 
		g->adjList_H[v] = NULL;			// 
	}
}

// add vertex
// this is just having the total number of nodes
void insertVertex(graphType* g) {
	if (((g->n) + 1)>MAX_VERTEX) {
		printf("\nFull!");
		return;
	}
	g->n++;
}

// add edge
void insertEdge(graphType* g, int u, int v) {
	graphNode* node;
	if (u >= g->n || v >= g->n) {
		printf("\n No vertex in the graph!");
		return;
	}
	node = (graphNode *)malloc(sizeof(graphNode));
	node->vertex = v;
	node->link = g->adjList_H[u];
	g->adjList_H[u] = node;
}

// print adjustList
void print_adjList(graphType* g) {
	int i;
	graphNode* p;
	for (i = 0; i<g->n; i++) {
		printf("\n\t\tVertex %c\s adjList items", i + 65);
		p = g->adjList_H[i];
		while (p) {
			printf(" -> %c", p->vertex + 65);	
			p = p->link;
		}
	}
}

// DFS
void DFS_adjList(graphType* g, int v) {  //starting from v 
	graphNode* w;
	top = NULL;
	push(v);
	g->visited[v] = 1;
	printf(" %c", v+65);

	while (!isEmpty()) // go DFS
	{
		w = g->adjList_H[v];
		while (w) // if there is a adj vertex
		{
			if (!g->visited[w->vertex]) // if not visited yet, proceed
			{
				push(w->vertex);
				g->visited[w->vertex] = 1;
				printf("%c", w->vertex + 65);
				v = w->vertex;
				w = g->adjList_H[v];
			}
			else // if already visited, see the next vertex
				w = w->link;
		}
		v = pop();
	}
}

void main() {
	int i;
	graphType *G9;
	G9 = (graphType *)malloc(sizeof(graphType));
	createGraph(G9);

	// Create G9
	for (i = 0; i<7; i++)
		insertVertex(G9);

	insertEdge(G9, 0, 2);
	insertEdge(G9, 0, 1);
	insertEdge(G9, 1, 4);
	insertEdge(G9, 1, 3);
	insertEdge(G9, 1, 0);
	insertEdge(G9, 2, 4);
	insertEdge(G9, 2, 0);
	insertEdge(G9, 3, 6);
	insertEdge(G9, 3, 1);
	insertEdge(G9, 4, 6);
	insertEdge(G9, 4, 2);
	insertEdge(G9, 4, 1);
	insertEdge(G9, 5, 6);
	insertEdge(G9, 6, 5);
	insertEdge(G9, 6, 4);
	insertEdge(G9, 6, 3);

	printf("\n G9\s adj List ");
	print_adjList(G9);

	printf("\n\n///////////////\n\nDFS >> ");
	DFS_adjList(G9, 0);	// starting from 0

	getchar();
}