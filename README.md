# Introduction  
- The trading Application is an online stock trading simulation REST API which can allow user to `CRUD` create, read, update and delete operations on database. This application provide functionality such as  create an account, deposit/withdraw money, buy/sell stock and view quote which is being retrieved from `IEX Cloud` market data. This REST API can be used by investor who are new to stock market and learn the basic operation to help them get started in real stock exchange. 
-  The technology used to implement this Micro Service is `SpringBoot`. `PSQL` database is used to persist information regarding quote, account, etc. Swagger UI was used to test your endpoints. IEX Cloud is used to get data for stock and persist them into the database. 

# Quick Start  
- Prequiresites: Java, Docker, CentOS 7  
- PSQL init - Database initialization is done by running the following script `/scripts/start_trading.sh`. This script will start the docker initialize database and populate it with necessary tables.
- git clone and mvn build  
- Start Springboot app using a following shell script  `/scripts/start_trading.sh` and once the application started it can be accessible using Swagger-UI and through Postman app. To access it via Postman you would need to download and import it to Postman through this link `http://localhost:8080/v2/api-docs`. To use Swagger-UI use this following link `http://localhost:5000/swagger-ui.html`
  - This trading application uses few environment variable that has been initialized by user in the OS. To run this application first user must setup their own Enviornment variable as shown below.
  ```add enviornment variable to ~/.bash_profile
  #run `idea` in terminal to propagate the enviornment vaiable
  PSQL_URL jdbc:postgresql://localhost:5432/jrvstrading
  PSQL_USER postgres
  PSQL_PASSWORD password (Can be anything)
  SPRING_PROFILES_ACTIVE dev
  IEX_PUB_TOKEN your_iex_token
  ```
  Swagger UI page
![Swagger](https://user-images.githubusercontent.com/51926543/62734931-1c56f680-b9f8-11e9-8d36-680522b6ec6c.jpg)

- How to consume REST API? 
- You can also execute `Http` request by importing the api specification using the link to `POSTMAN`
- [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs)  
-
  
# REST API Usage  
## Swagger  
Swagger is an open-source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services. API design is prone to errors, and it’s extremely difficult and time-consuming to spot and rectify mistakes when modeling APIs thus using swagger simplify the job of developer who can test their endpoints using swagger graphical user interface (GUI).
 
## Quote Controller  
- Quote Controller is designed to `create` ,`Read` and `Update` quote  related information which is being retrieved from IEX cloud using HTTP request and is cached into `quote` table in the database `jrvstrading`  using JDBC connection.

- There are five endpoints for this controller which are listed below with brief description:
  - GET `/quote/dailyList`: list all securities that are available to trading in this trading system
  - GET `/quote/iex/ticker/{ticker}`: Display the IEX market data for user defined ticker
  - POST `/quote/tickerId/{tickerId}`: Add a new ticker to the dailyList (quote table)
  - PUT `/quote/`: Update a given quote in the quote table
  - PUT `/quote/iexMarketData`: Update all quotes from IEX which is an external market data source  
## Trader Controller  
- Trader Controller is designed to provide functionality  of `Create`,`Update` and `DELETE` a trader and an account associated with specific user. It can also deposit and withdraw money from user account.
- This controller also have five endpoints which are listed below with brief description:
	- DELETE `/trader/traderID/{traderId}`: Delete a trader if the balance in an account is zero and no position is open. Also, this will also delete associated account and sezurityOrders
  - POST `/trader/}`: Create a trader and an account with DTO
  -   POST `/trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}`: Create a trader and an account
  - PUT `/trader/deposit/traderId/{traderId}/amount/{amount}`: Deposit a fund to user account
  - PUT `/trader/withdraw/traderId/{traderId}/amount/{amount}`: Withdraw a fund to user account
## Order Controller  
Order Controller is used to buy/sell stock from user defined company such as `AAPL`, `MSFT` etc.  
- There is only one endpoint which   
  - POST `/order/marketOrder`:  MarketOrder is a `JSON POGO` that contains `accountId, size and ticker`. AccountId is from the user buying/selling the stock, Size is how much he/she wants to buy and ticker is company code such as for Apple `AAPL` to buy the stock from.
## Dashboard Controller
This controller is designed to check if the Spring Booth is running or not and has only one endpoints.
- GET `/app/health` to make sure SpringBoot app is up and running  

  
# Architecture  
- Trading Application Workflow
![Trading](https://user-images.githubusercontent.com/51926543/62731680-b6b33c00-b9f0-11e9-8a47-78a3489c8411.png)

  - `Controller` : Parse user inputs and then calls the corresponding service method.
  - `Service` : This component is knows as Business logic. In computer software a business logic is the part of program that encodes the real-world` business rules` that determines how data can be created, stored and changed.`Business Rules` describe the operation, definitions and constraints that apply to an organization.
  - `Dao` : Data Access Object which handles various object. DAO is used to map the retrieved information from `IEX Cloud` and then latter use it to persist data into `PSQL` database. 
  - `SpringBoot: webservlet/TomCat and IoC` : Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
  - `PSQL` - PostgrSQL, also know as postgres, is a free and open-source relational database management system `RDBMS`. It takes the information from DAO and store it into `PSQL` database using DataSouce (JDBC Connection). 
  - `IEX`  - Investors  Exchange is a stock exchange based in United States. For this Application we use `IEX Cloud` is a flexible financial data platform connecting a wide array of developers with curated financial data. It also offers an API service, allowing developers to query US and Canadian Stock Data. 
  
# Improvements  
1.
2.
3.
4.
5.
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTY2OTg2MTU5OSwxNzY4NTYzNTc1LC05Nj
E0Nzc2MTQsNDM2MTQ1NjQwLDYxOTMwNTU5NywtMTk1MzE5MTQ5
NiwtNzg1NTI4MTE4LC0xMDg5Mjg4MTc0LC0xOTU4ODkzNjMxLD
E1MTQwMTkyNDgsMTY5NDgxMTI0Miw5MzU5MzE1NjUsMTQ5NjU3
MDMyOCwtMTgzMTc2NDcyMSwtODU4OTI1MTg2LC0xMTYxOTExNz
YsNjAxMDM3NjE4LC0xOTk2NDAzMTkwLC0xODk0NjY5NzY0LC0x
MDM4OTY1Mjk2XX0=
-->