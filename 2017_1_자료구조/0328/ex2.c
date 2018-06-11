#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef enum { lparen, rparen, plus, minus, times, divide, mod, eos, operand } precedence;

int isp[] = { 0, 19, 12, 12, 13, 13, 13, 0 }; //8
int icp[] = { 20, 19, 12, 12, 13, 13, 13, 0 };

precedence stack[10];
char expression[] = "(a/(b-c+d))*(e-a)*c"; // abc-d+/ea-*c*

precedence getToken(char *symbol, int *n) {
	*symbol = expression[(*n)++];

	switch (*symbol) {
	case '(': return lparen;
	case ')': return rparen;
	case '+': return plus;
	case '-': return minus;
	case '*': return times;
	case '/': return divide;
	case '%': return mod;
	case '\0': return eos;
	default: return operand;
	}
}

void printToken(precedence token) {

	char *a;

	switch (token) {
	case lparen: a = "("; break;
	case rparen: a = ")"; break;
	case plus:   a = "+"; break;
	case minus:  a = "-"; break;
	case times:  a = "*"; break;
	case divide: a = "/"; break;
	case mod:    a = "%"; break;
	default:     a = "\0";
	}

	printf("%c", *a);
}

void push(int *top, precedence token) {
	stack[++(*top)] = token;
}

precedence pop(int *top) {
	return stack[(*top)--];
}

void postfix() {
	char symbol;
	precedence token;

	int n = 0;
	int top = 0;
	stack[0] = eos;
	
	for (int i = 0; i < strlen(expression); i++)
	{
		token = getToken(&symbol, &n);

		if (token == operand)
		{
			printf("%c", symbol);
		}
		else if (token == rparen)
		{
			while (stack[top] != lparen)
				printToken(pop(&top));

			pop(&top);
		}
		else if (icp[token] > isp[stack[top]]) 
		{
			push(&top, token);
		}
		else if (icp[token] <= isp[stack[top]])
		{
			printToken(pop(&top));
			push(&top, token);
		}
	} 
	
	while ((token = pop(&top)) != eos) {
		printToken(token);
	}
	printf("\n");

}

int main() {
	postfix();
	getchar();

	return 0;
}

