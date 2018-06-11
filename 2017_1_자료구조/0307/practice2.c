#include <stdio.h>

void main2() {
	int i;
	char *ptrArray[4] = { {"Korea"}, {"Suwon"}, {"AjouSoftware"}, {"DataStructures"} };
	for (int i = 0; i < 4; i++)
		printf("\n %s", ptrArray[i]);

	ptrArray[3] = "Human-Computer Interaction";
	printf("\n\n");
	
	for (int i = 0; i < 4; i++)
		printf("\n %s", ptrArray[i]);

	getchar();
}