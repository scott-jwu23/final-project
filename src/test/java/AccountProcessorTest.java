import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AccountProcessorTest {

    // ===================================
    // BLACK-BOX TEST
    // ===================================

    @Test
    void normalDepositAndWithdrawal() {
        double result =
                AccountProcessor.processBalance(100.0, 50.0, 25.0);

        assertEquals(125.0, result, 0.001);
    }

    // ===================================
    // WHITE-BOX / CFG PATH / DU TEST
    // ===================================
   
    @Test
    void withdrawalExceedsBalance() {
    // DU chains:
    // D7(updatedBalance) -> U9(updatedBalance)
    // D10(fee) -> U15(fee)
    // D15(finalBalance) -> U17(finalBalance)
    // D18(finalBalance) -> U21(finalBalance)
        double result =
                AccountProcessor.processBalance(20.0, 0.0, 50.0);

        assertEquals(0.0, result, 0.001);
    }

    // ====================================
    // BOUNDARY VALUE / CFG PATH TEST
    // ====================================

    @Test
    void withdrawalEqualsBalance() {
        double result =
                AccountProcessor.processBalance(100.0, 0.0, 100.0);

        assertEquals(0.0, result, 0.001);
    }

    // ====================================
    // EQUIVALENCE PARTITIONING TEST
    // ====================================

    @Test
void validWithdrawalPartition() {
    double result =
            AccountProcessor.processBalance(200.0, 50.0, 100.0);

    assertEquals(150.0, result, 0.001);
}

    @Test
void overdraftPartition() {
    double result =
            AccountProcessor.processBalance(50.0, 0.0, 100.0);

    assertEquals(25.0, result, 0.001);
}
}