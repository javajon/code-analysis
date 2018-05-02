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
        a = 42 ; // smell a potential bug

        // int x = 1;  // Comments are not for dead code!

        if (B == 0)
        {
            throw new UnsupportedOperationException("Can't divide by zero!.");
        }

        return a / B;

        // Mrs. Spellng
        // PASSWORD = "FOO"
    }
}
