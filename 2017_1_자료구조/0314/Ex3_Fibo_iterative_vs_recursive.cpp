#include <iostream>
#include <ctime>

using namespace std;

long length = 1;
const long max_length = 61;

long recursive_fibo(unsigned int n)
{
	if (n == 0) return 0;
	if (n == 1) return 1;
	return recursive_fibo(n - 1) + recursive_fibo(n - 2);
}

unsigned long iterative_fibo(unsigned int n)
{
	if (n == 0) return 0;
	
	unsigned long currentMinusOne = 0;
	unsigned long currentMinusTwo = 1;
	unsigned long current = 1;

	for (unsigned int i = 1; i < n; ++i)
	{
		currentMinusTwo = currentMinusOne;
		currentMinusOne = current;
		current = currentMinusOne + currentMinusTwo;
	}
	return current;
}

int main()
{
	double t1, t2;
	unsigned long val;

	for (length = 1; length <= max_length; length += 5)
	{
		cout << "\nLength\t: " << length << '\n';
		/*
		t1 = clock();
		val = recursive_fibo(length);
		t2 = clock();
		cout << "Recursion value : " << val << endl;
		cout << "Recursion\t: " << (t2 - t1) / CLOCKS_PER_SEC << " sec\n";
		*/
		t1 = clock();
		val = iterative_fibo(length);
		t2 = clock();
		cout << "Iteration value : " << val << endl;
		cout << "Iteration\t: " << (t2 - t1) / CLOCKS_PER_SEC << " sec\n";
	}
	return 0;
}
	