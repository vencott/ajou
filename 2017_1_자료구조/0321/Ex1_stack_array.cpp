#include <stdio.h>
#include <stdlib.h>
#define STACK_SIZE 100

typedef int element;

element stack[STACK_SIZE];  // stack (one dimensional array)
int top = -1;               // initialize top

// isEmpty
int isEmpty() {
	if (top == -1) return 1;
	else return 0;
}

// isFull
int isFull() {
	if (top == STACK_SIZE - 1) return 1;
	else return 0;
}

// push
void push(element item)
{
	if (!isFull())
		stack[++top] = item;
}

// pop
element pop()
{
	if (!isEmpty())
		return stack[top--];
	else
		return 1000;
}

// check the top item in stack
element peek()
{
	if (!isEmpty())
		return stack[top];
	else
		return 1000;
}

// print stack
void printStack() {
	int i;
	printf("\n STACK [ ");
	for (i = 0; i <= top; i++)
		printf("%d ", stack[i]);
	printf("] ");
}

void main(void) {
	element item;
	printf("\n** stack **\n");
	printStack();
	push(1);	printStack();
	push(2);    printStack();
	push(3);	printStack();

	item = peek(); printStack();
	printf("peek => %d", item);

	item = pop();  printStack();
	printf("\t pop  => %d", item);

	item = pop();  printStack();
	printf("\t pop  => %d", item);

	item = pop();  printStack();
	printf("\t pop  => %d", item);

	getchar();
}