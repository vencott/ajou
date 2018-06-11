#include <stdio.h>
#include <stdlib.h>

// define a list node using struct
float coef;
typedef struct ListNode {
	float coef;
	int expo;
	struct ListNode* link;
} ListNode;

// define a head node
typedef struct ListHead {
	ListNode* head;
} ListHead;

// createLinkedList
ListHead* createLinkedList(void) {
	ListHead* L;
	L = (ListHead *)malloc(sizeof(ListHead));
	L->head = NULL;
	return L;
}

// appendTerm
void appendTerm(ListHead* L, float coef, int expo) {
	ListNode* newNode;
	ListNode* p;
	newNode = (ListNode *)malloc(sizeof(ListNode));
	newNode->coef = coef;
	newNode->expo = expo;
	newNode->link = NULL;

	if (L->head == NULL)
	{
		L->head = newNode;
		return;
	}
	else
	{
		p = L->head;
		while (p->link != NULL)
			p = p->link;

		p->link = newNode;
	}
}

// add polynomials
void addPoly(ListHead* A, ListHead* B, ListHead* C) {
	ListNode* pA = A->head;
	ListNode* pB = B->head;
	float sum;

	// run until a node exists for any polynomials
	while (pA && pB) {
		// if exponentials are the same
		if (pA->expo == pB->expo) {
			sum = pA->coef + pB->coef;
			appendTerm(C, sum, pA->expo);
			pA = pA->link;
			pB = pB->link;
		}
		// pA exponential > pB exponential
		else if (pA->expo > pB->expo) {
			appendTerm(C, pA->coef, pA->expo);
			pA = pA->link;
		}
		// pA exponential < pB exponential
		else {
			appendTerm(C, pB->coef, pB->expo);
			pB = pB->link;
		}
	}
	// append the rest of A's node
	for (; pA != NULL; pA = pA->link)
		appendTerm(C, pA->coef, pA->expo);

		// append the rest of B's node
	for (; pB != NULL; pB = pB->link)
		appendTerm(C, pB->coef, pB->expo);
}

// print out polynomials
void printPoly(ListHead* L) {
	ListNode* p = L->head;
	for (; p; p = p->link) {
		printf("%3.0fx^%d", p->coef, p->expo);
		if (p->link != NULL) printf(" +");
	}
}

void main(void) {
	ListHead *A, *B, *C;

	A = createLinkedList();
	B = createLinkedList();
	C = createLinkedList();

	appendTerm(A, 4, 3);
	appendTerm(A, 3, 2);
	appendTerm(A, 5, 1);
	printf("\n A(x) =");
	printPoly(A);

	appendTerm(B, 3, 4);
	appendTerm(B, 1, 3);
	appendTerm(B, 2, 1);
	appendTerm(B, 1, 0);
	printf("\n B(x) =");
	printPoly(B);

	addPoly(A, B, C);
	printf("\n C(x) =");
	printPoly(C);

	getchar();
}