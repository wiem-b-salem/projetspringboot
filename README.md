


# Projet Spring - Tour Booking Platform

A Spring Boot application for managing tours, guides, places, and user reservations. This platform allows users to browse places, book tours with guides, and leave reviews.

## Project Structure

### Models
- **User** - Base user entity with email, password, and authentication details
- **Guide** - Extends User, includes bio, languages, tours created, and rating
- **Place** - Tourist destinations with category, coordinates, and description
- **Tour** - Tours created by guides, linking places with dates and pricing
- **Reservation** - User bookings for tours
- **Review** - User reviews for tours and guides
- **Favorite** - User favorite places
- **Admin** - Admin login credentials for system management

### Database Architecture
- **Users Table** - Stores all users with single table inheritance for Guide subclass
- **Places Table** - Tourist locations categorized by type
- **Tours Table** - Tour itineraries created by guides
- **Reservations Table** - Booking records linking users to tours
- **Reviews Table** - Ratings and comments on tours/guides
- **Favorites Table** - User favorite places
- **Admin Table** - Admin authentication

## Features

### CRUD Operations
- ✅ User Management (Create, Read, Update, Delete)
- ✅ Place Management (Create, Read, Update, Delete)
- ✅ Full repositories with custom queries
- ✅ Service layer for business logic

### Web Interface
- Thymeleaf-based templates
- User management forms and listings
- Place management with CRUD interface
- Simple, responsive HTML pages

## Technologies Used

- **Java 17** (or higher)
- **Spring Boot** - Web framework
- **Spring Data JPA** - Database access layer
- **Hibernate** - ORM mapping
- **MySQL** - Database
- **Thymeleaf** - Template engine
- **Lombok** - Boilerplate code reduction
- **Jakarta Persistence** - JPA specification

## Project Setup

### Prerequisites
- JDK 17+
- MySQL 8.0+
- Gradle 7.0+

### Installation

1. Clone the repository
```bash
git clone <your-repo-url>
cd projetspring
```

2. Configure database in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tuniwayspring
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
```

3. Build the project
```bash
./gradlew build
```

4. Run the application
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8082`

## API Endpoints

### Users
- `GET /users/all` - List all users
- `GET /users/add` - Show add user form
- `POST /users/save` - Create new user
- `GET /users/edit/{id}` - Show edit form
- `POST /users/update/{id}` - Update user
- `GET /users/delete/{id}` - Delete user

### Places
- `GET /places/all` - List all places
- `GET /places/add` - Show add place form
- `POST /places/save` - Create new place
- `GET /places/edit/{id}` - Show edit form
- `POST /places/update/{id}` - Update place
- `GET /places/delete/{id}` - Delete place

## File Structure
```
src/
├── main/
│   ├── java/org/tp1ex2/projetspring/
│   │   ├── controller/
│   │   │   ├── PlaceController.java
│   │   │   └── UserController.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   ├── Guide.java
│   │   │   ├── Place.java
│   │   │   ├── Tour.java
│   │   │   ├── Reservation.java
│   │   │   ├── Review.java
│   │   │   ├── Favorite.java
│   │   │   └── Admin.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── GuideRepository.java
│   │   │   ├── PlaceRepository.java
│   │   │   ├── TourRepository.java
│   │   │   ├── ReservationRepository.java
│   │   │   ├── ReviewRepository.java
│   │   │   ├── FavoriteRepository.java
│   │   │   └── AdminRepository.java
│   │   ├── service/
│   │   │   ├── UserService.java
│   │   │   ├── GuideService.java
│   │   │   ├── PlaceService.java
│   │   │   ├── TourService.java
│   │   │   ├── ReservationService.java
│   │   │   ├── ReviewService.java
│   │   │   ├── FavoriteService.java
│   │   │   └── AdminService.java
│   │   ├── implementation/
│   │   │   ├── UserServiceImpl.java
│   │   │   ├── GuideServiceImpl.java
│   │   │   ├── PlaceServiceImpl.java
│   │   │   ├── TourServiceImpl.java
│   │   │   ├── ReservationServiceImpl.java
│   │   │   ├── ReviewServiceImpl.java
│   │   │   ├── FavoriteServiceImpl.java
│   │   │   └── AdminServiceImpl.java
│   │   └── ProjetspringApplication.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           ├── adduser.html
│           ├── listusers.html
│           ├── updateuser.html
│           ├── addplace.html
│           ├── listplaces.html
│           └── updateplace.html
└── test/
    └── java/...
```

## Database Schema

### Users Table
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    type VARCHAR(50) DEFAULT 'CLIENT',
    -- Guide fields
    bio TEXT,
    languages JSON,
    rating DOUBLE DEFAULT 0.0,
    DTYPE VARCHAR(50)
);
```

### Places Table
```sql
CREATE TABLE place (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    picture_url VARCHAR(255),
    category VARCHAR(100) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    city VARCHAR(100) NOT NULL
);
```

## Future Enhancements
- Tours CRUD interface
- Reservations management
- Reviews and ratings system
- Favorites management
- Admin dashboard
- Authentication and authorization
- Email notifications
- Payment integration

## License
This project is open source and available under the MIT License.

## Author
Created for the Tuniway Project - Spring Boot Development


