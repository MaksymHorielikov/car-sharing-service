<img src="https://img.uxwing.com/wp-content/themes/uxwing/download/transportation-automotive/car-sharing-icon.png" width="300" height="300">

# ﻿🚗CAR-SHARING
Introducing the ultimate car-sharing app! Discover a wide range of vehicles, book with ease, enjoy flexible rental durations, and experience top-notch safety features. Whether you're commuting to work, running errands, or planning a road trip, provides a seamless and convenient solution for all your transportation needs. 

## ⚙ Features 
`User registration, login, and role-based authorization:` facilitates a seamless user experience by allowing users to register, log in, and access the app based on their assigned roles. Whether you're a regular user or an administrator, you'll have the appropriate access and capabilities to make the most of the service.

`Exception handling with descriptive messages:` ensures a smooth user journey by implementing robust exception handling. In case of any errors or issues, the app provides descriptive error messages, making it easier to identify and resolve problems efficiently. This enhances the overall user experience and reduces frustration.

`Multiple endpoints with user and admin access:` offers a comprehensive range of endpoints designed to meet the specific needs of both users and administrators. Users can access functionalities tailored to their requirements, while administrators have access to additional features and operations, allowing for efficient and secure management of the system.

`Integration with Stripe payment service:` To ensure secure and reliable transactions for car rentals, integrates with the renowned Stripe payment service. Users can confidently make payments using various payment methods supported by Stripe, enhancing trust, and providing a seamless payment experience.

`Telegram bot notifications:` employs a Telegram bot to provide real-time notifications to users. Stay updated on your rental status, receive payment reminders, and be informed about any changes in the condition of the cars. These proactive notifications ensure that users are always well-informed and can stay connected with their car rental experience.

`Car booking and management:` With, the process of booking and managing rental cars is effortless. Users can easily search for available cars, book them for their desired rental periods, and efficiently return them when they are done. The app accurately tracks all the necessary details, ensuring a streamlined and user-friendly car rental experience.

## 📃 Structure
* `controller` - contains controllers for endpoints with different access depending on the role
* `config` - stores Spring and App configuration
* `repository` - repository with CRUD methods in the database
* `dto` - wrapper for model objects to unify the requests and responses in controllers
* `exception` - custom exception class for DAO's exceptions 
* `lib` - contains email and password validators with its annotations
* `model` - contains models of entity for the database
* `service` - contains services that call repositories and perform business logic
* `mapper` - сonverts model objects into DTO objects and vice versa
* `resources` - contains properties for database
* `telegram bot` - this package contains Telegram bot settings
* `security.jwt` - contains security settings using the JWT authentication

## 💾 Getting Started
-  Clone the project repository to your local machine.
-  You need to get a token to create a Telegram-bot https://t.me/BotFather
-  Configure the database connection parameters in the `resources/db.properties` file.
-  
-  Local build:
-  Build the project: mvn compile
-  Run the application: npm start or yarn start
-  
-  Docker:
-  Ensure Docker is installed and running
-  Set your credentials in `.env`
-  Build the project using the command: `mvn clean package`
-  Run the command: `docker-compose up`

## 🚀 Used technologies
- Java 17
- Apache Maven
- Spring Boot
- Hibernate
- MySQL
- Liquibase
- Stripe 
- Telegram Bots
- Docker
- Swagger

## 👨‍👩‍👧‍👦 Our team
[Maksym Horielikov](https://github.com/MaksymHorielikov)
[Olexandr Leshtaiev](https://github.com/AlexandLes)
[Olena Matus](https://github.com/HelenMatus)
[Volodymyr Prysiazhniuk](https://github.com/copypasterr)
[Kostiantyn Marchenko](https://github.com/KosMarch)
