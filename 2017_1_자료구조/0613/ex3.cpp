#include <stdio.h>
#define MAX 30
typedef int element;
int size;
element sorted[MAX];				// sorted array

void merge(element a[], int m, int middle, int n) {
	int i, j, k, t;
	i = m;							// 1st half subset starting point
	j = middle + 1;					// 2nf half subset starting point
	k = m;							// set a location for saving newly sorted values

	while (i <= middle && j <= n) {
		if (a[i] <= a[j]) sorted[k++] = a[i++];
		else
		{
			sorted[k++] = a[j++];
		}
	}

	if (i > middle) for (t = j; t <= n; t++, k++) sorted[k] = a[t];
	else for (t = i; t <= middle; t++, k++) sorted[k] = a[t];

	for (t = m; t <= n; t++) a[t] = sorted[t];
	printf("\n Merge Sort >> ");
	for (t = 0; t<size; t++) printf("%4d ", a[t]);
}

void mergeSort(element a[], int m, int n) {
	int middle;
	if (m<n) {
		middle = (m + n) / 2;
		mergeSort(a, m, middle);		// 1st half
		mergeSort(a, middle + 1, n);	// 2nd half
		merge(a, m, middle, n);			// for each subset, execute sorting and merging
	}
}

void main0() {
	int t;
	element list[8] = { 69, 10, 30, 2, 16, 8, 31, 22 };
	size = 8;
	printf("\n Items >> ");
	for (t = 0; t<size; t++) printf("%4d ", list[t]);

	printf("\n\n<<<<<<<<<< Execute Merge Sort >>>>>>>>>>\n");
	mergeSort(list, 0, size - 1);

	getchar();
}
