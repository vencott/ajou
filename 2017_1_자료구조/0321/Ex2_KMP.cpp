#include<stdio.h>
#include<string.h>

int failure[100] = { 0, };

void fail(char* pat)
{
	int lenp = strlen(pat);
	int beforeValue;
	failure[0] = -1;

	for (int i = 1; i < lenp; i++)
	{
		beforeValue = failure[i - 1];

		while (pat[i] != pat[beforeValue+1] && beforeValue >= 0)
		{
			beforeValue = failure[beforeValue];
		}
		
		if (pat[i] == pat[beforeValue + 1])
			failure[i] = beforeValue + 1;
		else
			failure[i] = -1;
	}
}

int pmatch(char *string, char *pat) {
	int i = 0;
	int j = 0;
	int lens = strlen(string);
	int lenp = strlen(pat);

	while (i < lens && j < lenp) {
		if (string[i] == pat[j]) {
			i++;
			j++;
		}
		else if (j == 0)
			i++;
		else
			j = failure[j - 1] + 1;
	}

	return j == lenp ? i - lenp : -1;
}

void main() {
	char string[] = "ABABBABABCABB";
	char pat[] = "ABABC";

	fail(pat);

	for (int i = 0; i<strlen(pat); i++)
	{
		printf("%d\n", failure[i] + 1);
	}

	int matched_count = pmatch(string, pat);

	if (matched_count == -1) {
		printf("No pattern found");
	}
	else {
		printf("Index of the first pattern found: %d", matched_count);
	}

	getchar();
}