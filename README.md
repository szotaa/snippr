# Snippr

Snippr is a "pastebin like" REST api. This is my first ever published Spring Boot application which was written solely to practice my coding skils. Every code review and constructive criticism is much welcomed.


# Features

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

  - **GET** `/user/{id}` - returns snippet with specified id

  - **PUT** `/user/{id}` - updates snippet with specified id (secured)

  - **DELETE** `/user/{id}` - deletes snippet with specified id (secured)

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


----------

		
	user {
	    "id": 1,
	    "username": "admin",
	    "pasword": "hehe",
	    "dateAdded": 1519233691,
	    "lastModified": 1519233691
    }
    
    id - read-only, user id
    username - read-write, username (not null)
    password - write-only, pasword (not null)
	dateAdded - read-only, epoch timestamp, date at whch user registred
	lastModified - read-only, epoch timestamp, date at which user was last modified
	
			
