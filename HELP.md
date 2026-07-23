# Getting Started
run command ./mvnw spring-boot:run or if you using Intellij IDEA, run the main class `com.demo.recipe.RecipeApplication` to start the application.

DB: The H2 DB is running as an in-memory database. You can access the H2 console at `http://localhost:8080/h2-console` with the following credentials: SA

Follow the steps below to ensure a smooth installation and configuration process.

if you need to insert a recipe by http client, by Intellij IDEA:
POST http://localhost:8080/api/recipes
Content-Type: application/json

{
"name": "Vegetarian Pasta",
"vegetarian": true,
"servings": 4,
"ingredients": [
"Pasta",
"Tomato Sauce",
"Basil"
],
"instructions": "Boil pasta. Mix with sauce and basil. Serve hot."
}

or execute the next command on your terminal
curl -X POST http://localhost:8080/api/recipes \
-H "Content-Type: application/json" \
-d '{
"name": "Vegetarian Pasta",
"vegetarian": true,
"servings": 4,
"ingredients": ["Pasta", "Tomato Sauce", "Basil"],
"instructions": "Boil pasta. Mix with sauce and basil. Serve hot."
}'

For swagger documentation please run application http://localhost:8080/swagger-ui/index.html
I also added a screenshot from database (H2) 
