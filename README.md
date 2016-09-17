# CovertChannel

[Description]
I included the entire implementation in one java file called CovertChannel. The implementation beings by reading the file line by line and breaking it up into bytes. It then distinguishes whether it needs to send a 0 or 1 by parsing the byte by bits and if it is a 0, hal creates the object and does so in the main method. Hal never calls run. After, regardless of whether Hal creates or not, Lyle performs the required operations of create, write, read, destroy, and run. Within the run method, Lyle interprets each bit as a 0 or 1 depending on what read returns, which is dependent on whether or not hal created the object. This is stored in a new SecureSubject class object for subjects to be able to store the data internally until the full byte is ready to be translated into an ASCII character. To compile the program, use "javac *.java". To run the program, use "java CovertChannel <file_name>".

[Machine Information]
The machine I used for testing was blow-pops in the 3rd floor computer lab in the GDC. It is a Dell running Linux with a clock speed of 3.50 GHz. I developed this program on a 2.60 GHz HP Omen laptop, and each run took about 4 times longer.

[Source Description]
Pride and Prejudice and Metamorphosis were both downloaded from Project Gutenburg at https://www.gutenberg.org/. With Pride and Prejudice, I deleted all the Project Gutenburg specific text before and after the actual text. With Metamorphosis, I left it all there. I removed it from Pride and Prejudice to change it up from Metamorphosis for easier comparison and distinguishability. This Little Bag is a poem by Jane Austen. I randomly selected one of her poems from the list the website that is linked to this specific poem. It was retrieved from http://www.poemhunter.com/poem/this-little-bag/. The file test1 was a small text file that I typed up myself for easy testing and debugging while I was developing the program. 

[Finish]
I finished all of the requirements for this assignment. My implementation ended up being very slow and likely because of how I handled strings. I could have improved performance by using a StringBuffer or StringBuilder, but ran out of time due to my focus on studying for the exam.

[Results Summary]
[No.]	[DocumentName] 		[Size] 	 	[Bandwidth]
1	Pride and Prejudice	697,796 bytes	10.90 bits/ms
		Run 1 - 512,180 ms
		Run 2 - 512,860 ms
		Run 3 - 511,342 ms
		Average - 512,127 ms
2	Metamorphosis		141,447 bytes	10.92 bits/ms
		Run 1 - 102,850 ms
		Run 2 - 101,364 ms
		Run 3 - 106,702 ms
		Average - 103,639 ms
3	This Little Bag		255 bytes	8.76 bits/ms
		Run 1 - 222 ms
		Run 2 - 226 ms
		Run 3 - 251 ms
		Average - 233 ms
4	test1			53 bytes	4.56 bits/ms
		Run 1 - 92 ms
		Run 2 - 87 ms
		Run 3 - 101 ms
		Average - 93 ms
