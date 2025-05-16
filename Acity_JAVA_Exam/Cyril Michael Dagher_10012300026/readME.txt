Create a Jframe GPA calculator 
enter student nam, id , courses, credit hours, grades and a combo box to select file or database 
3. click calculate GPA to compute the result 
4. choose where to save the data : 
a. file (gpa_records.txt) OR 
b. Database (gpa.db) 
5. If you choose file, dont write any database code , jsut write "Database: under construction" in the joption at the end of the code 
6. If you choose database, dont write any file code, just write "file : yet to be implemented" 

formula for calculating GPA : 
GPA = sum(grade points * credit hours) / sum(credit hours)




NB : THE FILE TO RUN WHICH IS STYLED GUI IS "CalculatorStyleGPA.java"

- The gui i drew on my question paper to give me an idea on how i want it to look like , the user should input the course, credit hours etc in the jbox below

- the buttons dont look sexy at all but i dont also want to go and look at other designs to do it , lemme do it mysef
    ... well i couldnt do it ill leave it like that , at least my GUI is clear and works well. 

- i am having an issue where when symbols like + is added, it doesnt read the input and my error is thrown 
    fix: ...... lemme try adding more case statements to it to include "A+" and then throw my exception to also include all the gradings possible 

- Gosh my database isnt working , i need a new jar file , its got something to do with my java --version and all , i shouldve checked all of this yesterday 
    fix : .. what do i dooooooooooooooooo, wait lemme just save it to file and make it easy, write down everything you can rememebr on file on the question paper

- well file is done, whats next ....
- i run it and everything seems so smooth , wait let me also manually calculate the same grades on my sheet and calculate to see if the calculator works well 
    it was smart putting the sum functions and then dividing those two.. Gosh my brain broooo

    Jesos i just realized i messed up the for loop and i separated one of them by , and not ; thats why my code wasnt running lmao 
    at least for every iteration when initialized at 0, if i is less than the size of the course in bytes, it increases i by one and then moves to the next course, then my totalpoints is incremeneted by my grades + credits OMGGGGGGGGG THATS ITTTT

    totalPoints += grades.get(i) * credits.get(i); // no way , has this ever been done before, lemme try it 
    wait shouldnt it be , when expanded 
    totalPoints = totalPoints + (grades.get(i) * credits.get(i)) // im saying this because the fomula, for the first part, is sum(gradepoints * credit hours) 






