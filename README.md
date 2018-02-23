# Snippr

Snippr is a "pastebin like" REST api. This is my first ever published Spring Boot application which was written solely to practice my coding skils. Every code review and constructive criticism is much welcomed.


## Features

- Adding, creating, modifying and deleting snippets
- Account management
- JWT authentication and authorization

## Used technologies

- Java 8
- Spring Boot 1.5.10
- Spring Security
- Hibernate 5.2.12
- MySQL
- Lombok 
- Maven
- JJWT 0.9.0
- JUnit 4.12
- SpringFox 2.8.0

## Endpoints

- User resource

  - **POST** `/user` - registers new user

  - **POST** `/user/login` - returns jwt token when correct creditentials provided

  - **GET** `/user/{id}` - returns user with specified id (secured)

  - **PUT** `/user/{id}` - updates user with specified id (secured)

  - **DELETE** `/user/{id}` - deletes existing user with specified id (secured)


- Snippet resource

  - **POST** `/snippet` - creates new snippet

  - **GET** `/snippet/{id}` - returns snippet with specified id

  - **PUT** `/snippet/{id}` - updates snippet with specified id (secured)

  - **DELETE** `/snippet/{id}` - deletes snippet with specified id (secured)

## Resources
		
	
	snippet {
	    "id": 2,
	    "title": "example title",
	    "content": "example content",
	    "syntaxHighlighting": "JAVA",
	    "expiryDate": 1577841071,
	    "owner": null,
	    "dateAdded": 1519295029,
	    "lastModified": 1519295029
	}

	id - read-only, snippet id
	title - read-write, snippet title (not null)
	content - read-write, snippet content (not nul)
	syntaxHighlighting - read-write, programming language used in this snippet (null = plaintext)
	expiryDate - read-write, epoch timestamp, after this date snippet won't be viewable (null = never)
	owner - read-only, snippet owner id (null = posted by anonymous)
	dateAdded - read-only, epoch timestamp, date at whch snippet was added
	lastModified - read-only, epoch timestamp, date at which snippet was last modified
	
	supported syntaxHighlighting values can be found in ProgrammingLanguages enum in 
	src/main/java/pl/szotaa/snippr/snippet/validation/ProgrammingLanguageValidator.java


----------

		
	user {
	    "id": 1,
	    "username": "admin",
	    "pasword": "hehe",
	    "dateAdded": 1519233691,
	    "lastModified": 1519233691
    }
    
    id - read-only, user id
    username - read-write, username (not null, min 5, max 50)
    password - write-only, pasword (not null, min 8, max 60)
	dateAdded - read-only, epoch timestamp, date at whch user registred
	lastModified - read-only, epoch timestamp, date at which user was last modified
	
			
## How to launch

Snippr needs 3 env variables to launch properly:

- `DB_URL` - database connection url (for example: jdbc:mysql://127.0.0.1:3306/snippr )
- `DB_USERNAME` - database conection username
- `DB_PASSWORD` - database connection password


Launching Snippr with `swagger` profile will enable `/api-docs` endpoint containing swagger2 json.

## Example usage

**Register new account:**	

> **POST** `/user`
> {
> "username": "exampleUsername",
	"password": "examplePassword"
> }

**Log in:**

>**POST** `/user/login`
> {
> "username": "exampleUsername",
	"password": "examplePassword"
> }

	This wil return JWT string in response Authorization header

**Add new Snippet:**

>**POST** `/snippet`
> {
  "title": "example title",
  "content": "example content"
  "expiryDate": "1577841071",
  "syntaxHighlighting": "JAVA"
}

	Adding Authorization header containing your JWT token from /user/login will 
	result in adding this snippet as yours (you wil be able to delete and edit it)
	Fields exipryDate and syntaxHighlighting are optional.

## Default admin creditentials

To log in as admin use:
>**POST** `/user/login`
> {
> "username": "admin",
	"password": "hehe"
> }

Admin account is inserted on database creation from resources/import.sql file.

## Bugs

- Signing JWT claims throws an exception

## TODO

- Improve documentation

- More unit tests

- Integration tests

- Improve error handling
