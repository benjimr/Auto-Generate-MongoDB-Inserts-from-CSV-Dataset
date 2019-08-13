/*
	This program is designed to read in the dataset from character-deaths.csv and parse the contents
	in to 3 seperate lists of objects representing Characters, Books and Houses. 
	Once all the data is stored, it writes it out to inserts.js. 
	After that is done it writes creates.js and inserts.js in to one
	master file creates-inserts.js. The purpose of this is to make it
	easier to setup or reset the database, having only one file to load.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main 
{
	public static void main(String[] args) 
	{
		List<Character> characters = new ArrayList<Character>();
		List<Book> books = new ArrayList<Book>();
		List<House> houses = new ArrayList<House>();
		
		try
		{
			//setup for reading the csv file line by line
			File data = new File("data/character-deaths.csv");
			FileReader fr = new FileReader(data);
			BufferedReader br = new BufferedReader(fr);
			List<String> headers = new ArrayList<String>();
			String line;
			
			//get the first line and store as headers
			if((line = br.readLine()) != null)
			{
				headers = Arrays.asList(line.split(","));
				
				//turn the book headers in to book objects
				for(int i = 8; i < 13; i++)
				{
					Book b = new Book(headers.get(i));
					books.add(b);
				}
			}
						
			//while there are still lines being read loop
			while((line = br.readLine()) != null)
			{
				//split the line by commas
				List<String> values = new ArrayList<String>(Arrays.asList(line.split(",",-1)));
				//create a new character
				Character c = new Character();
				
				//fill in the basic info
				c.name = values.get(0);
				c.house = values.get(1);
				
				//checking if person has died, if not fill in null in the required sections
				if(!values.get(2).equals(""))
				{
					c.deathYear = values.get(2);
				}
				else
				{
					c.deathYear = "null";
				}
				
				if(!values.get(3).equals(""))
				{
					c.deathBook = values.get(3);				
				}
				else
				{
					c.deathBook = "null";
				}
				
				if(!values.get(4).equals(""))
				{
					c.deathChpt = values.get(4);				
				}
				else
				{
					c.deathChpt = "null";
				}
				
				if(!values.get(5).equals(""))
				{
					c.introChpt = values.get(5);				
				}
				else
				{
					c.introChpt = "null";
				}
				
				
				c.gender = values.get(6);
				c.nobility = values.get(7);
				
				//fill the appears in array
				//if the character appears in a book
				if(Integer.parseInt(values.get(8)) == 1)
				{
					c.appearsIn.add(1);//add it to the appears in array
					books.get(0).characters.add(c.name);//add the person to the characters array of the book
					
					//if they in the book add them to the death array
					if(c.deathBook.equals("1"))
					{
						books.get(0).deaths.add(c.name);
						books.get(0).deathAmt++;
					}
				}
				
				//repeat above for each book
				if(Integer.parseInt(values.get(9)) == 1)
				{
					c.appearsIn.add(2);
					books.get(1).characters.add(c.name);
					
					if(c.deathBook.equals("2"))
					{
						books.get(1).deaths.add(c.name);
						books.get(1).deathAmt++;
					}
				}
				
				if(Integer.parseInt(values.get(10)) == 1)
				{
					c.appearsIn.add(3);
					books.get(2).characters.add(c.name);
					
					if(c.deathBook.equals("3"))
					{
						books.get(2).deaths.add(c.name);
						books.get(2).deathAmt++;
					}
				}
				
				if(Integer.parseInt(values.get(11)) == 1)
				{
					c.appearsIn.add(4);
					books.get(3).characters.add(c.name);
					
					if(c.deathBook.equals("4"))
					{
						books.get(3).deaths.add(c.name);
						books.get(3).deathAmt++;
					}
				}
				
				if(Integer.parseInt(values.get(12)) == 1)
				{
					c.appearsIn.add(5);
					books.get(4).characters.add(c.name);
					
					if(c.deathBook.equals("5"))
					{
						books.get(4).deaths.add(c.name);
						books.get(4).deathAmt++;
					}
				}
				
				characters.add(c);
				
				//if character has a house
				if(!c.house.equals("None"))
				{
					boolean newHouse = true;
					
					//if we find a match, house is already accounted for
					//break and set newHouse to false
					for(int i = 0; i < houses.size(); i++)
					{
						if(houses.get(i).name.equals(c.house))
						{
							newHouse = false;
							break;
						}
					}
					
					//if we have come across a new house create and add it to the houses array
					if(newHouse == true)
					{
						House h = new House(c.house);
						houses.add(h);
					}
					
					//loop through all known houses
					for(int i = 0; i < houses.size(); i++)
					{						
						//if house found add to members
						if(houses.get(i).name.equals(c.house))
						{
							houses.get(i).members.add(c.name);
							
							//if alive add to alive
							if(c.deathYear.equals("null"))
							{
								houses.get(i).AliveAmt++;
							}
							else//otherwise add to dead
							{
								houses.get(i).DeadAmt++;
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
		//at this point the entire data set is represented as object in the program.
		//Next we form inserts using the toString methods as a js file to load in to mongo
		File inserts = new File("data/inserts.js");
		FileOutputStream os;
		
		try
		{
			inserts.createNewFile();
			os = new FileOutputStream(inserts,false);
			
			String commandOpen1 = "printjson(db.Characters.insert([\n";
			String commandOpen2 = "printjson(db.Books.insert([\n";
			String commandOpen3 = "printjson(db.Houses.insert([\n";
			String commandClose = "\n]))\n\n";
			
		
			//CHARACTER INSERTS
			os.write(commandOpen1.getBytes());
			
			for(int i = 0; i < characters.size(); i++)
			{
				String character = characters.get(i).toString();
				
				//if not last character add a comma and 2 line breaks
				if(i != characters.size()-1)
				{
					character += ",\n\n";
				}
				
				os.write(character.getBytes());
			}
			
			os.write(commandClose.getBytes());
			
			
			//BOOK INSERTS
			os.write(("\n\n" + commandOpen2).getBytes());
			
			for(int i = 0; i < books.size(); i++)
			{
				String book = books.get(i).toString();
				
				//if not last book add a comma and 2 line breaks
				if(i != books.size()-1)
				{
					book += ",\n\n";
				}
				
				os.write(book.getBytes());
			}
			
			os.write(commandClose.getBytes());
			
			
			//HOUSE INSERTS
			os.write(("\n\n" + commandOpen3).getBytes());
			
			for(int i = 0; i < houses.size(); i++)
			{
				String house = houses.get(i).toString();
				
				//if not last house add a comma and 2 line breaks
				if(i != houses.size()-1)
				{
					house += ",\n\n";
				}
				
				os.write(house.getBytes());
			}
			
			os.write(commandClose.getBytes());
			os.close();
			
			File creates = new File("data/ColCreates.js");
			FileInputStream is = new FileInputStream(creates);
			
			//this would write it to the exact location of the mongo files, so load("Creates-Inserts.js") could be used easily
			//I have disabled this so the program will still run in the case the exact location was different
			//File comb = new File("D:/Programs/Mongo/bin/Creates-Inserts.js");
			//os = new FileOutputStream(comb);
			
			//this simply writes it to the data folder of the current directory
			File comb2 = new File("data/Creates-Inserts.js");
			FileOutputStream os2 = new FileOutputStream(comb2);

			
			byte[] buff =  new byte[1024];
			int len;
			;
			
			//This is essentially joining the creates that have already been written
			//and the inserts that have just been generated to one master file that
			//can be used to setup or reset the database in one go.
			while((len = is.read(buff)) != -1)
			{
				//os.write(buff, 0, len);
				os2.write(buff, 0, len);
			}
			
			//os.write("\n\n".getBytes());
			os2.write("\n\n".getBytes());

			is.close();

			is = new FileInputStream(inserts);
			
			while((len = is.read(buff)) != -1)
			{
				//os.write(buff, 0, len);
				os2.write(buff, 0, len);
			}
			
			is.close();
			//os.close();
			os2.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}	
	}
}

class Book 
{
	String name;
	List<String> characters = new ArrayList<>();
	List<String> deaths = new ArrayList<>();
	int deathAmt;
	
	public Book(String name)
	{
		this.name = name;
	}
	
	//used to format for js
	public String toString()
	{
		String res;
		
		res ="{\n"+  
				"\tName: \""+ name + "\",\n" +
				"\tCharacters: [";
		
		for(int i = 0; i < characters.size(); i++)
		{
			res += "\"" + characters.get(i) + "\"";
			
			if(i != (characters.size()-1))
			{
				res += ", ";
			}
		}
		
		res += "],\n";
		
		res += "\tDeaths: [";
		
		for(int i = 0; i < deaths.size(); i++)
		{
			res += "\"" + deaths.get(i) + "\"";
			
			if(i != (deaths.size())-1)
			{
				res += ", ";
			}
		}
		
		res +="],\n";
		
		res += "\tDeathAmt: " + deathAmt + "\n}";
	
		return res;
	}
}


class Character
{
	String name;
	String house;
	String deathYear;
	String deathBook;
	String deathChpt;
	String introChpt;
	String gender;
	String nobility;
	List<Integer> appearsIn = new ArrayList<>();
	
	//used to format for js
	public String toString()
	{
		String res;
		
		res ="{\n" +   
				"\tName: \"" + name + "\",\n" +
				"\tHouse: \"" + house + "\",\n" +
				"\tDeathYear: " + deathYear + ",\n" +
				"\tDeathBook: " + deathBook + ",\n" +
				"\tDeathChpt: " + deathChpt + ",\n" +
				"\tIntroChpt: " + introChpt + ",\n" +
				"\tGender: " + gender + ",\n" +
				"\tNobility: " + nobility + ",\n" +
				"\tAppearsIn: " + "[";
		
		for(int i = 0; i < appearsIn.size(); i++)
		{
			res +=  appearsIn.get(i);
			
			if(i != (appearsIn.size() - 1))
			{
				res += ", ";
			}
		}
		
		res += "]\n}";
		
		return res;
	}
}

class House
{
	String name;
	List<String> members = new ArrayList<>();
	int AliveAmt;
	int DeadAmt;
	
	public House(String name)
	{
		this.name = name;
	}

	//use to format for js
	public String toString()
	{
		String res;
		
		res = "{\n"+  
				"\tName: \"" + name + "\",\n" +
				"\tMembers: [";
		
		for(int i = 0; i < members.size(); i++)
		{
			res += "\"" + members.get(i) + "\"";
			
			if(i != (members.size())-1)
			{
				res += ", ";
			}
		}
		
		res += "],\n\tAliveAmt: " + AliveAmt + ",\n"+
				"\tDeadAmt: " + DeadAmt +"\n}";
		return res;
	}
}




