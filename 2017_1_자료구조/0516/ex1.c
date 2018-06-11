#include<stdio.h> 
#include<stdlib.h> 

typedef char element;
typedef struct treeNode {
	char key;				// data field
	struct treeNode* left;	// left subtree link field
	struct treeNode* right;	// right subtree link field
} treeNode;

// find a node that has a key value = x
treeNode* searchBST(treeNode* root, char x) {
	treeNode* p;
	p = root;
	while (p != NULL) {
		if (x < p->key) p = p->left;
		else if (x == p->key) return p;
		else p = p->right;
	}
	printf("\n Cannot find a key!");
	return p; // 못찾으면 null 반환
}

// insert a node with key = x 
treeNode* insertNode(treeNode *p, char x) {
	treeNode *newNode;
	if (p == NULL) {
		newNode = (treeNode*)malloc(sizeof(treeNode));
		newNode->key = x;
		newNode->left = NULL;
		newNode->right = NULL;
		return newNode;
	}
	else if (x < p->key)  p->left = insertNode(p->left, x);
	else if (x > p->key)  p->right = insertNode(p->right, x);
	else  printf("\n duplicated! \n");

	return p;
}

// delete a node with a key value 
void deleteNode(treeNode *root, element key) {
	treeNode *parent, *p, *succ, *succ_parent; // succ and succ_parent variables are for the case where a deleted node has two child nodes
	treeNode *child;

	parent = NULL;
	p = root;
	while ((p != NULL) && (p->key != key)) {  // find a location of a node with a key value
		// note that we know both the p node and p's parent node
		parent = p;
		if (key < p->key) p = p->left;
		else p = p->right;
	}

	// cannot find a node
	if (p == NULL) {
		printf("\n Cannot find a key in the tree!");
		return;
	}

	// delete node == leaf node
	if ((p->left==NULL) && (p->right==NULL)) {
		if (parent != NULL) {
			if (parent->left == p) parent->left = NULL;
			else parent->right = NULL;
		}
		else root = NULL;
	}

	// delete node == has a one child node
	else if ((p->left == NULL) || (p->right == NULL)) {
		if (p->left != NULL) child = p->left;
		else child = p->right;

		if (parent != NULL) {
			if (parent->left == p) parent->left = child;
			else parent->right = child;
		}
		else root = child;
	}

	// delete node == has two child nodes
	else {
		// there are two options for finding a node to be replaced.
		// we consider replacing the deleted node with the predecessor.
		succ_parent = p;
		succ = p->left;

		while (succ->right != NULL) { // find an inorder predecessor
			succ_parent = succ;
			succ = succ->right;
		}
		// attaching succ->left to succ_parent
		if (succ_parent->left == succ)  succ_parent->left = succ->left;
		else succ_parent->right = succ->left;

		p->key = succ->key;
		p = succ;
	}
	free(p);
}

// inorder
void displayInorder(treeNode* root) {
	if (root) {
		displayInorder(root->left);
		printf("%c_", root->key);
		displayInorder(root->right);
	}
}

void menu() {
	printf("\n*---------------------------*");
	printf("\n\t1 : Print Tree");
	printf("\n\t2 : Insert a node");
	printf("\n\t3 : Delete a node");
	printf("\n\t4 : Search a node");
	printf("\n\t5 : Exit");
	printf("\n*---------------------------*");
	printf("\nChoose an option >> ");
}

int main() {
	treeNode* root = NULL;
	treeNode* foundedNode = NULL;
	char choice, key;

	// Building a binary search tree
	// G is a root node
	root = insertNode(root, 'G');
	insertNode(root, 'I');
	insertNode(root, 'H');
	insertNode(root, 'D');
	insertNode(root, 'B');
	insertNode(root, 'M');
	insertNode(root, 'N');
	insertNode(root, 'A');
	insertNode(root, 'J');
	insertNode(root, 'E');
	insertNode(root, 'Q');

	while (1) {
		menu();
		scanf_s(" %c", &choice);

		switch (choice - '0') {
		case 1:	printf("\t[Print out binary tree]  ");
			displayInorder(root);  printf("\n");
			break;

		case 2:	printf("Type a node to be inserted : ");
			scanf_s(" %c", &key);
			insertNode(root, key);
			break;

		case 3:	printf("Type a node to be deleted : ");
			scanf_s(" %c", &key);
			deleteNode(root, key);
			break;

		case 4: printf("Type a node to be searched : ");
			scanf_s(" %c", &key);
			foundedNode = searchBST(root, key);
			if (foundedNode != NULL)
				printf("\n Found %c! \n", foundedNode->key);
			else  printf("\n Cannot find a char. \n");
			break;

		case 5: return 0;

		default: printf("Try again \n");
			break;
		}
	}
}