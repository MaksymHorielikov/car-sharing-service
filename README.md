# car-sharing-service


| Controller     | End point                       | Method | Description                                                           | MANAGER | CUSTOMER | No-authentication |
|----------------|---------------------------------|--------|-----------------------------------------------------------------------|---------|----------|-------------------|
| Authentication | /register                       | POST   | register a new user                                                   |         |          | x                 |
| Authentication | /login                          | POST   | get JWT token                                                         |         |          | x                 |
| User           | /user/{id}/role?role=..         | PUT    | update role                                                           | x       |          |                   |
| User           | /user/me                        | GET    | get profile info                                                      | x       | x        |                   |
| User           | /user/me                        | PUT    | update profile info                                                   | x       | x        |                   |
| Car            | /cars                           | GET    | get a list of cars                                                    |         |          | x                 |
| Car            | /cars                           | POST   | add a new car                                                         | x       |          |                   |
| Car            | /cars/{id}                      | GET    | get car's detailed information                                        | x       | x        |                   |
| Car            | /cars/{id}                      | PUT    | update car(also manage inventory)                                     | x       |          |                   |
| Car            | /cars/{id}                      | DELETE | delete car                                                            | x       |          |                   |
| Rental         | /rentals                        | POST   | add a new rental                                                      | x       |          |                   |
| Rental         | /rentals/{id}                   | GET    | get rental's detailed information                                     | x       | x        |                   |
| Rental         | /rentals?user_id=..&is_active.. | GET    | get rentals by user ID and whether the rental is still active or not  | x       |          |                   |
| Rental         | /rentals/{id}/return            | POST   | set actual return date                                                | x       |          |                   |
| Payment        | /payments                       | POST   | create a new payment session                                          |         | x        |                   |
| Payment        | /payments                       | GET    | get a list of payments                                                | x       | x        |                   |
| Payment        | /payments/success               | GET    | handle successful payment                                             |         | x        |                   |
| Payment        | /payments/cancel                | GET    | handle cancelled payment                                              |         | x        |                   |
| Payment        | /payments/{paymentId}/renew}    | POST   | renew a payment                                                       | x       | x        |                   |