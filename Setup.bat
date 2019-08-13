::This simply imports the entire dataset as it is in the csv file. For it to run successfully it requires the exact location of mongoimport.exe and character-deaths.csv
D:/Programs/Mongo/bin/mongoimport.exe -d GameOfThrones -c all --type csv "D:\benjm\Documents\Personal\College\Year 3\Databases 2\Assignment 2\character-deaths.csv" --headerline

::Compile the java program used to format the js files for inputting data
javac main.java

::run the java program
java Main

::the java program generates a file called Creates-Inserts.js
::this can be loaded in to the database to create and populate the Characters, Books and Houses collections.
pause