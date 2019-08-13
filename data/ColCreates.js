db.Characters.drop()
db.createCollection("Characters")

db.runCommand
(
	{
		collMod: "Characters",
		validator: 
		{	
			$or:
			[
				{Name: {$type: "string"}},
				{House: {$type: "string"}},
				{DeathYear: {$type: "number"}},
				{DeathBook: {$in: [1,2,3,4,5]}},
				{DeathChpt: {$type: "number"}},
				{IntroChpt: {$type: "number"}},
				{Gender: {$in: [1,0]}},
				{Nobility: {$in: [1,0]}},
				{AppearsIn: {$type: "array"}}
			]
		},
		validationLevel: "moderate",
		validationAction: "warn"
	}
)


db.Books.drop()
db.createCollection("Books")

db.runCommand
(
	{
		collMod: "Books",
		validator:
		{
			$or:
			[
				{Name: {$type: "string"}},
				{Characters: {$type: "array"}},
				{Deaths: {$type: "array"}},
				{DeathAmt: {$type: "number"}}
			]
		},
		validationLevel: "moderate",
		validationAction: "warn"
	}
)

db.Houses.drop()
db.createCollection("Houses")

db.runCommand
(
	{
		collMod: "Houses",
		validator:
		{
			$or:
			[
				{Name: {$type: "string"}},
				{Members: {$type: "array"}},
				{AliveAmt: {$type: "number"}},
				{DeadAmt: {$type: "number"}}
			]
		},
		validationLevel: "moderate",
		validationAction: "warn"
	}
)