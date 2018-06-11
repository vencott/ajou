#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// doubled linked list
typedef struct ListNode {
	struct ListNode* llink;   // left link
	char data[4];
	struct ListNode* rlink;   // right link
} listNode;

// define a node that indicates a head of the linked list
typedef struct {
	listNode* head;
} linkedList_h;

// create empty linked list
linkedList_h* createLinkedList_h(void) {
	linkedList_h* DL;
	DL = (linkedList_h*)malloc(sizeof(linkedList_h));	// assign a space to the head node
	DL->head = NULL;									// put null as the linked list is empty
	return DL;
}

// print all data in the linked list
void printList(linkedList_h* DL) {
	listNode* p;
	printf(" DL = (");
	p = DL->head;
	while (p != NULL) {
		printf("%s", p->data);
		p = p->rlink;
		if (p != NULL) printf(", ");
	}
	printf(") \n");
}

// insert a node after pre
void insertNode(linkedList_h* DL, listNode *pre, char*x)
{
	listNode* newNode;
	newNode = (listNode*)malloc(sizeof(listNode));
	strcpy(newNode->data, x);

	if (DL->head == NULL) { // empty linked list
		newNode->llink = NULL;
		newNode->rlink = NULL;
		DL->head = newNode;
	}
	else { // if not empty
		newNode->rlink = pre->rlink;
		pre->rlink = newNode;
		newNode->llink = pre;
		if (newNode->rlink != NULL)
			newNode->rlink->llink = newNode;
	}
}

// delete old node
void deleteNode(linkedList_h* DL, listNode* old) {
	if (DL->head == NULL) return;	// if empty, just return
	else if (old == NULL) return;	// if old is null, just return
	else {
		old->llink->rlink = old->rlink;
		old->rlink->llink = old->llink;
		
		free(old);
	}
}

// search x node on the list
listNode* searchNode(linkedList_h* DL, char* x) {
	listNode *temp;
	temp = DL->head;
	while (temp != NULL) 
	{
		if (strcmp(temp->data, x) == 0)
			return temp;
		else
			temp = temp->rlink;
	}
	return temp;
}

int main() {
	linkedList_h* DL;
	listNode *p;
	DL = createLinkedList_h();  // create an empty node
	printf("(1) create a doubly linked list. \n");
	printList(DL); getchar();

	printf("(2) Insert A. \n");
	insertNode(DL, NULL, "A");
	printList(DL); getchar();

	printf("(3) Insert B after A. \n");
	p = searchNode(DL, "A"); insertNode(DL, p, "B");
	printList(DL); getchar();

	printf("(4) Insert C after B. \n");
	p = searchNode(DL, "B"); insertNode(DL, p, "C");
	printList(DL); getchar();

	printf("(5) Delete B. \n");
	
	p = searchNode(DL, "B");
	 deleteNode(DL, p);
	printList(DL); getchar();

	return 0;
}