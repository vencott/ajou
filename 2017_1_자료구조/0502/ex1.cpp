#include <stdio.h>
#include <stdlib.h>
#include <memory.h>

typedef struct treeNode {	// define treeNode
	char data;
	struct treeNode *left;  // left subTree link
	struct treeNode *right; // right subTree link
} treeNode;

// makeRootNode
treeNode* makeNode(char data, treeNode* leftNode, treeNode* rightNode) {
	treeNode* root = (treeNode *)malloc(sizeof(treeNode));
	root->data = data;
	root->left = leftNode;
	root->right = rightNode;
	return root;
}

// preorder
void preorder(treeNode* root) {
	if (root) {
		printf("%c ", root->data);
		preorder(root->left);
		preorder(root->right);
	}
}

// inorder
void inorder(treeNode* root) {
	if (root) {
		inorder(root->left);
		printf("%c ", root->data);
		inorder(root->right);
	}
}

// postorder
void postorder(treeNode* root) {
	if (root) {
		postorder(root->left);
		postorder(root->right);
		printf("%c ", root->data);
	}
}

void main() {
	// making a binary tree that reflects (A*B-C/D) 
	treeNode* n7 = makeNode('D', NULL, NULL);
	treeNode* n6 = makeNode('C', NULL, NULL);
	treeNode* n5 = makeNode('B', NULL, NULL);
	treeNode* n4 = makeNode('A', NULL, NULL);
	treeNode* n3 = makeNode('/', n6, n7);
	treeNode* n2 = makeNode('*', n4, n5);
	treeNode* n1 = makeNode('-', n2, n3);

	printf("\n preorder : ");
	preorder(n1);

	printf("\n inorder : ");
	inorder(n1);

	printf("\n postorder : ");
	postorder(n1);

	getchar();
}