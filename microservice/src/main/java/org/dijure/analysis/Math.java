package org.dijure.analysis;

import com.qualinsight.plugins.sonarqube.smell.api.annotation.Smell;
import com.qualinsight.plugins.sonarqube.smell.api.annotation.Smells;
import com.qualinsight.plugins.sonarqube.smell.api.model.SmellType;

@Smells({
    @Smell(minutes=1000, reason="Math class only has one method ...", type=SmellType.BAD_DESIGN),
    @Smell(minutes=10, reason="Consider a 3rd party jar instead of recreating a wheel.", type=SmellType.UNCOMMUNICATIVE_NAME),
    @Smell(minutes=10, reason="Math as a name is not clear.", type=SmellType.UNCOMMUNICATIVE_NAME)
})
public class Math
{
    @Smell(minutes=10, reason="Div? Divide, diverse, divorce, divagate?", type=SmellType.MEANINGLESS_COMMENT)
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
