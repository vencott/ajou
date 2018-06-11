
#include <stdio.h>
typedef int element;
int size;

// selection sort
void SelectionSort(int a[], int size) {
	int i, j, t, min;
	element temp;
	printf("\nInit Array : ");
	for (t = 0; t<size; t++)  printf("%d ", a[t]);	// before sorting
	printf("\n\n<<<<<<<<<< Selection Sort >>>>>>>>>>\n");
	for (i = 0; i<size - 1; i++) {
		min = i;
		for (j = i + 1; j < size; j++) {
			if (a[j] < a[min]) min = j;
		}
		temp = a[i]; a[i] = a[min]; a[min] = temp;
			printf("\nStep %d: ", i + 1);
		for (t = 0; t<size; t++)  printf("%3d ", a[t]);
	}
}

void main() {
	element list[8] = { 69, 10, 30, 2, 16, 8, 31, 22 };
	size = 8;
	SelectionSort(list, size);

	getchar();
}
