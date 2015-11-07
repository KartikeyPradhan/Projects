#Project Description

##Abstract
The aim of these simulations is to compare and contrast different algorithms, with different levels of information access to find
parking spots in real time. The setup is done for the city of San Francisco, CA. We use the data released by the parking 
authorities, ranging over a month, given with timestamps accurate to second granularity.
We simulated the experiments on a web-based interface, running on Tomcat 8 server and SQL Server 2012 used as the database server.
The simulations assume the given time-stamped availability data as the real-time data, with no information to the future data.
So we had to perform the following tasks
 Task 1: To evaluate different algorithms to find a parking spot for the user with high level of information on a real time dataset, like parking block, parking availability, date and time, user destination and start location.
 Task 2: To evaluate different algorithms to find a parking spot for a user with limited level of information, like parking block, start location, destination location and time, and the dataset is probabilistic rather than real time.
 Task 3: To find a parking spot for a user in an uninformed search with only information of user destination, starting location and parking block.
The results are presented from 86 simulations in each implementation of each task, and each congestion level. The graphs are also
presented depicting the performance of the algorithms in each of the mentioned scenarios.
The results are reproducible with the code provided (instructions for rerunning the simulations are included).
The results of the experiments indicate that having the access to real time parking availability information increases the 
efficiency of the algorithms in all variations. The less information we have, the worse the results are (time taken to find a 
parking spot increases relatively).
Also, the results indicate for lesser congestions, the greedy algorithm performs comparable to the other algorithms, but as the
congestion levels increase, the efficiency falls. This indicates that using more sophisticated algorithms like Gravitational 
Force algorithms is a better design for overall robustness of the system.

Also see the project report for detail information on this project.
