#include <stdio.h>
#include <string.h>

struct employee {
	char name[10];
	int year;
	int pay;
};

void main4() {
	struct employee em1;
	struct employee* em2 = &em1;

	strcpy(em2->name, "You");
	em2->year = 2013;
	em2->pay = 5900;

	printf("%s ", em2->name);
	printf("%d ", em2->year);
	printf("%d ", em2->pay);
	printf("\n");

	printf("%s ", em1.name);
	printf("%d ", em1.year);
	printf("%d ", em1.pay);
	printf("\n");

	getchar();
}