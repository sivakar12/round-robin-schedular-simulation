# round-robin-schedular-simulation

To run the demo, first run 'javac *.java' and follow it with 'java SchedularGUI'

A process is described by a sequence of integers, seperated by comma only.
The first integer is the arrival time (This has to be greater than zero)
Then the next integer values give CPU burst and IO bursts alternatively.

For example, if a process arrives at 5 and have 4 units of CPU burst, 2 units of IO burst and then 6 units of CPU burst, the string is 5,4,2,6

These values can be stored in the file processes.csv with one line for a process and then loaded by clicking the "Load from File" button.
Or processes can be added one by one using the textbox in the UI.
Or processes can be generated randomly by clicking "Generate Random" button.
