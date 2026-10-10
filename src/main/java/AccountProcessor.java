public class AccountProcessor {

    public static double processBalance(
            double balance,
            double deposit,
            double withdrawal) {

        double updatedBalance;
        double fee = 0.0;

        updatedBalance = balance + deposit;

        if (withdrawal > updatedBalance) {
            fee = 25.0;
        } else {
            updatedBalance = updatedBalance - withdrawal;
        }

        double finalBalance = updatedBalance - fee;

        if (finalBalance < 0) {
            finalBalance = 0;
        }

        return finalBalance;
    }
}