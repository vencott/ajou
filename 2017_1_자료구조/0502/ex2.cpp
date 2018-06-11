#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

// Node struct to support threaded trees
typedef struct treeNode {
	char data;
	struct treeNode *left;
	struct treeNode *right;
	int isThreadRight;
} treeNode;

// makeNode
treeNode* makeNode(char data, treeNode* leftNode, treeNode* rightNode, int isThreadRight) {
	treeNode* node = (treeNode *)malloc(sizeof(treeNode));
	node->data = data;
	node->left = leftNode;
	node->right = rightNode;
	node->isThreadRight = isThreadRight;
	return node;
}

// return successor
treeNode *findThreadSuccessor(treeNode* p) {
	treeNode *q = p->right;
	if (!q) return q;
	if (p->isThreadRight) return q;
	while (q->left) q = q->left;
	return q;
}

// inorder traversal
void threadInorder(treeNode* root) {
	treeNode * q;
	q = root;
	while (q->left) q = q->left;
	do {
		printf("%3c", q->data);
		q = findThreadSuccessor(q);
	} while (q);
}

void main() {
	// (A*B-C/D) 
	treeNode* n7 = makeNode('D', NULL, NULL, 0);
	treeNode* n6 = makeNode('C', NULL, NULL, 1);
	treeNode* n5 = makeNode('B', NULL, NULL, 1);
	treeNode* n4 = makeNode('A', NULL, NULL, 1);
	treeNode* n3 = makeNode('/', n6, n7, 0);
	treeNode* n2 = makeNode('*', n4, n5, 0);
	treeNode* n1 = makeNode('-', n2, n3, 0);

	n4->right = n2;
	n5->right = n1;
	n6->right = n3;

	printf("\n Inorder of a threaded binary tree : ");
	threadInorder(n1);

	getchar();
}