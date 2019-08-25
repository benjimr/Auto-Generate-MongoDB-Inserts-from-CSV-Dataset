# Auto Generate MongoDB Inserts from CSV Dataset
Java program designed to encapsulate data presented in a CSV file and generate inserts for a MongoDB database.

### Sample Data Entries:
    Name,Allegiances,Death Year,Book of Death,Death Chapter,Book Intro Chapter,Gender,Nobility,GoT,CoK,SoS,FfC,DwD
    Addam Marbrand,Lannister,,,,56,1,1,1,1,1,1,0
    Aegon Frey (Jinglebell),None,299,3,51,49,1,1,0,0,1,0,0
    Aegon Targaryen,House Targaryen,,,,5,1,1,0,0,0,0,1
    Adrack Humble,House Greyjoy,300,5,20,20,1,1,0,0,0,0,1
    Aemon Costayne,Lannister,,,,,1,1,0,0,1,0,0
    Aemon Estermont,Baratheon,,,,,1,1,0,1,1,0,0
    Aemon Targaryen (son of Maekar I),Night's Watch,300,4,35,21,1,1,1,0,1,1,0
    
### Sample Generated Input:

    db.Characters.insert([
    {
      Name: "Addam Marbrand",
      House: "Lannister",
      DeathYear: null,
      DeathBook: null,
      DeathChpt: null,
      IntroChpt: 56,
      Gender: 1,
      Nobility: 1,
      AppearsIn: [1, 2, 3, 4]
    },
