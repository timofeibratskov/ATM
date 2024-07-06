import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ATM extends FileWorker {
    User user = new User();
    private List<Card> cards = new ArrayList<>();
    private final String FILENAME = "C:\\Users\\User\\Desktop\\bankomat\\src\\cardsData.txt";

    public void start() {
        System.out.println("Введите номер карты: (XXXX-XXXX-XXXX-XXXX):");
        String cardNumber = makeValidCardNumber(user.responce());
        cards = fileReader();// получение данных всех карт
        Card card = findCard(cardNumber, cards);
        if (isBlocking(card)){
            start();
        }
        while (true) {
            System.out.println("----------------------------------");
            System.out.printf("для карты: %s \n", cardNumber);
            System.out.println("Выберите действие:");
            System.out.println("1. Посмотреть баланс на карте");
            System.out.println("2. Снять деньги с счёта");
            System.out.println("3. Пополнить баланс");
            System.out.println("4. Выход");
            int choice = Integer.parseInt(user.responce());
            System.out.println("----------------------------------\n");
            switch (choice) {
                case 1:
                    if (checkPass(card)) {
                        System.out.println("Текущий баланс на карте: " + card.getCardBalance());
                    } else {
                        start();
                    }
                    break;
                case 2:
                    if (checkPass(card)) {
                        while (true) {
                            try {
                                System.out.println("Введите сумму для снятия наличных:");
                                float amount = Float.parseFloat(user.responce());
                                System.out.println("----------------------------------");
                                card.withdraw(amount);
                                fileWriter();
                                break; // выходим из цикла, так как операция успешно выполнена
                            } catch (NumberFormatException e) {
                                System.out.println("Ошибка: Введенное значение не является числом!");
                            }
                        }
                    } else {
                        start();
                    }
                    break;
                case 3:
                    if (checkPass(card)) {
                        while (true) {
                            try {
                                System.out.println("Введите сумму для выдачи наличных:");
                                float amount = Float.parseFloat(user.responce());
                                System.out.println("----------------------------------");
                                card.deposit(amount);
                                fileWriter();
                                break; // выходим из цикла, так как операция успешно выполнена
                            } catch (NumberFormatException e) {
                                System.out.println("Ошибка: Введенное значение не является числом!");
                            }
                        }
                    } else {
                        start();
                    }
                    break;
                case 4:
                    System.out.println("Завершение работы...");
                    fileWriter();
                    start();
                default:
                    System.out.println("Неверный выбор. Попробуйте еще раз.");

            }
        }
    }

    private Card findCard(String cardNumber, List<Card> cards) {

        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        System.out.println("Такой карты не сущестует");
        start();
        return null;
    }

    //LOAD DATA
    @Override
    public List<Card> fileReader() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String line = reader.readLine();
            while (line != null) {
                String[] mass = line.split(" ");
                cards.add(makeCard(mass));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Произошла ошибка");
        }
        return cards;
    }

    @Override
    public void fileWriter() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME, false))) {
            for (Card card : cards) {
                writer.write(card.getCardNumber() + " " + card.getCardPassword() + " " + card.getCardBalance() + " " + card.getBlockedUntil());
                if (!cards.isEmpty() && card != cards.get(cards.size() - 1)) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private Card makeCard(String[] mass) {
        return new Card(mass[0], Short.parseShort(mass[1]), Float.parseFloat(mass[2]), mass[3]);
    }

    private boolean checkPass(Card card) {
        byte attempt = 0;
        while (attempt < 3) {
            try {
                System.out.println("Введите пароль:");
                short pass = Short.parseShort(user.responce());
                System.out.println("----------------------------------");
                if (Objects.equals(pass, card.getCardPassword())) {
                    return true;
                } else {
                    System.out.println("Неверный пароль. Осталось попыток: " + (2 - attempt));
                    attempt++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введенное значение не является числом!");
                System.out.println("Осталось попыток: " + (2 - attempt));
                attempt++;
            }

        }
        blockTheCard(card);

        fileWriter();
        return false;
    }

    private String makeValidCardNumber(String cardNumber) {
        return (cardNumber.matches("^\\d{4}(-\\d{4}){3}$")) ? cardNumber : cardNumber.replaceAll("(\\d{4})(\\d{4})(\\d{4})(\\d{4})", "$1-$2-$3-$4");
    }

    private void blockTheCard(Card card) {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        card.setBlockedUntil(time.toString());
        fileWriter();
        System.out.println("Карта заблокирована!");
    }

    private boolean isBlocking(Card card) {
        if (Objects.equals(card.getBlockedUntil(), "no")) {
            return false;
        } else {
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(card.getBlockedUntil()))) {
                card.setBlockedUntil("no");
                System.out.println("Блокировка снята");
                return false;
            } else {
                LocalDateTime blickedUntil = LocalDateTime.parse(card.getBlockedUntil());
                Duration time = Duration.between(LocalDateTime.now(),blickedUntil);
                System.out.println("Карта заблокирована! Повторите попытку через  "+time.toDaysPart()+" дня(-ей) "+time.toHoursPart()+" часа(-ов) "+time.toMinutesPart()+" минут(-ы)");
                return true;
            }
        }
    }
}
