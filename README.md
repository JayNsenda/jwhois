# jwhois

This program allows you to see source details of an email
you have received either in your inbox or spam.
You can pass in:
* an email(.eml) file from your local computer:
This would be the actual email file the argument will be: `ef=<path_to_the_eml_file>`
* an IP address: This is the IP address (IPv4) of the email. The argument will be: `ip=<the_ip_address>`
* the source email or orignail email. You can google to know how to get this. The arcgument will be: `-te`, press enter then paste the `<email_source>` and preess `Ctrl+D` on your keyboard
      
### Pre-requisite
In order to use this program, please make sure you are:
1. Running a Linux machine,
2. Have installed the `whois` command.
   
### Examples running the program

* Running the program with email file:
```
java -jar jwhois ef=/home/john/Download/example.eml
```
* Running the program with IP address:
```
java -jar jwhois ip=193.0.0.1
```
* Running the program with source email:
```
java -jar jwhois -te press ENTER then Paste the source and click CTRL+D
```

Enjoy :smile:
