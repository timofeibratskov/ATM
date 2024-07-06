public class Card {

    private short cardPassword;

    private String cardNumber;

    private String blockedUntil;

    private float cardBalance;

    public Card(String cardNumber, short cardPassword, float cardBalance, String blockedUntil) {
        this.cardBalance = cardBalance;
        this.cardNumber = cardNumber;
        this.cardPassword = cardPassword;
        this.blockedUntil = blockedUntil;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public short getCardPassword() {
        return cardPassword;
    }

    public float getCardBalance() {
        return cardBalance;
    }

    private void setCardBalance(float newBalance) {
        this.cardBalance = newBalance;
    }

    public void deposit(float amount) {
        if (amount <= 1000000 & amount % 5 == 0) {
            setCardBalance(this.cardBalance + amount);
            System.out.println("оперция проведена успешно");
        } else {
            System.out.println("Ошибка: Неверная сумма чтобы поплнить баланс!");
        }
    }

    public void withdraw(float amount) {
        if (this.cardBalance >= amount & amount % 5 == 0) {
            setCardBalance(this.cardBalance - amount);
            System.out.println("операция проведена успешно");
        } else {
            System.out.println("Ошибка: Указана неверная сумма для снятия наличных!");
        }
    }

    public String getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(String blockedUntil) {
        this.blockedUntil = blockedUntil;
    }
}
