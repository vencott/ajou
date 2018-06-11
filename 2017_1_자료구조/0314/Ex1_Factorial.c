#include <stdio.h>

long int fact(int);

void main1() {
	int n, result;
	printf("\n add a number: ");
	scanf_s("%d", &n);
	result = fact(n);
	printf("\n\n factorial(%d) = %ld.\n", n, result);
	getchar();
}

long int fact(int n) {
	int value;
	if (n <= 1) {
		printf("\n fact(1) called!");
		printf("\n fact(1) returned!!");
		return 1;
	}
	else {
		printf("\n fact(%d) called!", n);
		value = n * fact(n - 1);
		printf("\n fact(%d) returned value: %ld!!", n, value);
		return value;
	}
}