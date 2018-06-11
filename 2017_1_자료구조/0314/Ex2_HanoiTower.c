#include <stdio.h>

void hanoi(int n, int start, int work, int target) {
	if (n==1)
		printf("move object %d from %c to %c\n", 1, start, target);
	else {
		hanoi(n-1, start, target, work);
		printf("move object %d from %c to %c\n", n, start, target);
		hanoi(n-1, work, start, target);
	}
}

void main2() {
	hanoi(3, 'A', 'B', 'C');
	getchar();
}