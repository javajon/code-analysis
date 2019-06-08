package org.dijure.analysis;

public class Math
{
    /**
     * A smelly function that any code analysis tool would have a heyday with it.
     * @param a
     * @param c
     * @return
     * @throws Exception Smell exception
     */
    public static int div(int a, final int B) throws RuntimeException
    {
        a = 42 ; // smell and a potential bug

        // int x = 1;  // Comments are not for dead code!

        if (B == 0)
        {
            throw new UnsupportedOperationException("Can't divide by zero!.");
        }

        String PASSWORD = "Come find me security scanner.";
        System.out.printf("Time for a new career. Here, take my password %s", PASSWORD);

        return a / B;

        // Your's truly, Mrs. Spellng
    }
}
