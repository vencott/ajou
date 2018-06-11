#include <stdio.h>
#include <stdlib.h>
#define Q_SIZE 4

typedef char element;
typedef struct {
	element queue[Q_SIZE];
	int front, rear;
} QueueType;

QueueType *createQueue() {
	QueueType *Q;
	Q = (QueueType *)malloc(sizeof(QueueType));
	Q->front = -1;
	Q->rear = -1;
	return Q;
}

// isEmpty
int isEmpty(QueueType *Q) {
	if (Q->front == Q->rear) {
		printf(" Queue is empty! ");
		return 1;
	}
	else return 0;
}

// isFull
int isFull(QueueType *Q) {
	if (Q->rear == Q_SIZE - 1) {
		printf(" Queue is full! ");
		return 1;
	}
	else return 0;
}

// add item to rear
void enQueue(QueueType *Q, element item) {
	if (isFull(Q))
	{
		printf("Queue is full!\n");
		return;
	}

	Q->queue[++(Q->rear)] = item;
}

// remove item at front
element deQueue(QueueType *Q) {
	if (isEmpty(Q))
	{
		printf("Queue is empty!\n");
		return NULL;
	}

	return Q->queue[++(Q->front)];
}

// print all items in queue
void printQ(QueueType *Q) {
	int i;
	printf(" Queue : [");
	for (i = Q->front + 1; i <= Q->rear; i++)
		printf("%3c", Q->queue[i]);
	printf(" ]");
}

void main(void) {
	QueueType *Q1 = createQueue();
	element data;
	printf("\n ***** Queue ***** \n");
	printf("\n enQueue A>>");  enQueue(Q1, 'A'); printQ(Q1);
	printf("\n enQueue B>>");  enQueue(Q1, 'B'); printQ(Q1);
	printf("\n enQueue C>>");  enQueue(Q1, 'C'); printQ(Q1);

	printf("\n deQueue  >>");  data = deQueue(Q1); printQ(Q1);
	printf("\t deQueued data: %c", data);
	printf("\n deQueue  >>");  data = deQueue(Q1); printQ(Q1);
	printf("\t deQueued data: %c", data);
	printf("\n deQueue  >>");  data = deQueue(Q1); printQ(Q1);
	printf("\t deQueued data: %c", data);

	getchar();
}
