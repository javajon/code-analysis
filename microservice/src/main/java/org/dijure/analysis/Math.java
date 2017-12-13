package org.dijure.analysis;

public class Math
{
    /**
     * A smelly function that any code analysis tool would have a hay day on it.
     * @param a
     * @param c
     * @return
     * @throws RuntimeException Smell exception
     */
    public static int div(int a, final int b) throws RuntimeException
    {
        a = 42 ; // potential bug, smell (how about a misspeling?)

        // int x = 1;  // Comments are not for dead code!

        if (b == 0)
        {
            throw new UnsupportedOperationException("Can't divide by zero!");
        }

        return a / b;
    }
}
