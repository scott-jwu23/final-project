
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("AccountProcessor.processBalance")
public class AccountProcessorTest {

    private static final double DELTA = 0.001;

    /**
     * Shared helper so every test states only inputs and expected output.
     */
    private static void assertBalance(double expected,
            double balance, double deposit, double withdrawal) {
        double actual = AccountProcessor.processBalance(balance, deposit, withdrawal);
        assertEquals(expected, actual, DELTA);
    }

    // ===================================
    // BLACK-BOX
    // ===================================
    @Nested
    @DisplayName("Black-box: equivalence partitioning")
    class EquivalencePartitioning {

        @ParameterizedTest(name = "balance={0}, deposit={1}, withdrawal={2} -> {3}")
        @CsvSource({
            "100.0, 50.0, 25.0, 125.0", // withdrawal covered
            "200.0, 50.0, 100.0, 150.0", // withdrawal covered, larger values
            "20.0, 100.0, 110.0, 10.0", // deposit makes withdrawal affordable
            "50.0, 0.0, 100.0, 25.0", // overdraft: fee applied, balance stays positive
            "20.0, 0.0, 50.0, 0.0" // overdraft: fee exceeds balance, floored at 0
        })
        void validPartitions(double balance, double deposit, double withdrawal, double expected) {
            assertBalance(expected, balance, deposit, withdrawal);
        }
    }

    @Nested
    @DisplayName("Black-box: boundary value analysis")
    class BoundaryValues {

        @ParameterizedTest(name = "balance={0}, deposit={1}, withdrawal={2} -> {3}")
        @CsvSource({
            "100.0, 0.0, 99.99, 0.01", // withdrawal just under updated balance
            "100.0, 0.0, 100.0, 0.0", // withdrawal equals updated balance (no fee)
            "100.0, 0.0, 100.01, 75.0", // withdrawal just over updated balance (fee)
            "25.01, 0.0, 30.0, 0.01", // final balance just above 0
            "25.0, 0.0, 30.0, 0.0", // final balance exactly 0 (not clamped)
            "24.99, 0.0, 30.0, 0.0", // final balance just below 0 (clamped)
            "0.0, 0.0, 0.0, 0.0" // all zeros
        })
        void boundaries(double balance, double deposit, double withdrawal, double expected) {
            assertBalance(expected, balance, deposit, withdrawal);
        }
    }

    // ===================================
    // WHITE-BOX: CFG PATHS (V(G) = 3)
    // ===================================
    @Nested
    @DisplayName("White-box: basis paths from CFG")
    class BasisPaths {

        @Test
        @DisplayName("P1: N1-N2-N4-N5-N6-N8 (withdrawal covered)")
        void p1_withdrawalCovered() {
            assertBalance(125.0, 100.0, 50.0, 25.0);
        }

        @Test
        @DisplayName("P2: N1-N2-N3-N5-N6-N8 (fee applied, balance stays positive)")
        void p2_feeApplied() {
            assertBalance(75.0, 100.0, 0.0, 150.0);
        }

        @Test
        @DisplayName("P3: N1-N2-N3-N5-N6-N7-N8 (fee applied, floored at 0)")
        void p3_feeAppliedAndFloored() {
            assertBalance(0.0, 20.0, 0.0, 50.0);
        }

        // Note: N1-N2-N4-N5-N6-N7-N8 is infeasible. If N4 runs, withdrawal <= updatedBalance,
        // so finalBalance cannot be negative and N7 is unreachable on that branch.
    }

    // ===================================
    // WHITE-BOX: DEF-USE PAIRS
    // ===================================
    @Nested
    @DisplayName("White-box: def-use pairs")
    class DefUse {

        @Test
        @DisplayName("updatedBalance N1->N4, N4->N5; fee N1->N5")
        void updatedBalanceRedefinedAndFeeUnchanged() {
            assertBalance(60.0, 10.0, 90.0, 40.0);
        }

        @Test
        @DisplayName("updatedBalance N1->N5; fee N3->N5; finalBalance N5->N8")
        void updatedBalanceKeptAndFeeRedefined() {
            assertBalance(25.0, 40.0, 10.0, 80.0);
        }

        @Test
        @DisplayName("finalBalance N7->N8")
        void finalBalanceRedefinedToZero() {
            assertBalance(0.0, 5.0, 0.0, 10.0);
        }
    }
}
