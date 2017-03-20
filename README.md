# deepak
this repository contains the implementation for generating the JSON objects from https stream

# Dependency(s)
1. Use Java8 for compiling/ executing this code
2. Main class for executing the excercise is com.cof.deepak.Controller 
3. Dependency on JOpt package for command line handling https://pholser.github.io/jopt-simple/
4. Following command line options are supported 
5. Import the project into eclipse (neon) and set the JRE as 1.8

Option                                 Description                
------                                 -----------                
--crystal-ball 							Include projected txns                                              
 --year 									year for projected txns (default: 2017)
--month                                 month for projected txns (default: 3)
                                                        
--ignore-cc-payments 					Ignore reversed(24hrs) CreditCard txns       
--dups 									Output filename for duplicates  (default: duplicates.txt)  
                
--ignore-donuts 						Ignore donut for merchant name (default: Donuts|DUNKIN)                                                           
                                                 
--out 									Outputfile for txns    (default: activity_out.txt)

# Example(s)

1. java com.cof.deepak.Controller --out default.txt // generates the output in the default.txt file
2. java com.cof.deepak.Controller --ignore-donuts --out ignore_donuts.txt // ignores the donut txns, and generates the output in the ignore_donuts.txt file
3. java com.cof.deepak.Controller --ignore-cc-payments --out ignore_cc.txt --dup duplicates.txt // ignores the cc reversed txns, and generates the output in the ignore_cc.txt file, and duplicates int the duplicates.txt
4. java com.cof.deepak.Controller --crystal-ball --out projected.txt // includes the projected txns, and generates the output in the projected.txt file


