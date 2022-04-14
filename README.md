# jwhois

This command line program allows you to see source details of an email you have received either in your inbox or spam.
You can pass in:
1. an email(.eml) file from your local computer:
   This would be the actual email file the argument will be: `ef=<path_to_the_eml_file>`
2. an IP address: This is the IP address (IPv4) of the email. The argument will be: `ip=<the_ip_address>`
3. the source email or original email. You can google to know how to get this. The argument will be: `-te`, press enter then paste the `<email_source>` and press `Ctrl+D` on your keyboard

### Pre-requisite
In order to use this program, please make sure you are:
1. Running a Linux machine,
2. Have installed the `whois` command.

### Building the program
* Run the below command to build the program
```
mvn clean install
```
This will generate a .jar file in the `target` directory. You can use this file
to run the program.

### Examples running the program

* Running the program with email file:
```
java -jar jwhois.jar ef=<PATH_TO_EMAIL_FILE>/example.eml
```
* Running the program with IP address:
```
java -jar jwhois.jar ip=<IP_ADDRESS>
```
* Running the program with source email:
```
java -jar jwhois.jar -te 
```
Press ENTER after typing the above command, then Paste the source and click CTRL+D.

Enjoy 
