#include <stdio.h>
#include <string.h>

struct employee {
	char name[10];
	int year;
	int pay;
};

void main3() {
	int i;
	struct employee em[4] = {
		{"Jack", 2014, 4200},
		{"John", 2015, 3300},
		{"Mary", 2015, 3500},
		{"Beth", 2016, 2900}
	};

	for (int i = 0; i < 4; i++) {
		printf("\n");
		printf("%s ", em[i].name);
		printf("%d ", em[i].year);
		printf("%d ", em[i].pay);
		printf("\n");
	}

	getchar();
}