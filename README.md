# ATM
консольное приложение для банкомата./n
Пользователь вводит данные карты вида ХХХХ-ХХХХ-ХХХХ-ХХХХ и пароль.
далее пользователю открыто несколько команд банкомата 1.посмотреть баланс 2.Пополнить счет 3.Снять деньги(кратно 5 т.к. банкомат выдает минимальную купюру '5' 4.Выйти из этой карточки)
При вводе неверного номера карты выводит ошибку(такой карты не существует). при вводе верного номера карты ,но неверного пароля пользователю дается 3 попытки. Если все попытки неудачные карочка блокирется на 24 часа.
в отдельном файле находятся данные карт и их содержимое: номер карты, пароль, баланс, блокировка(если есть)
Данные карточки перезаписываются.

