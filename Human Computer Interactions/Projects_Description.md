
#CS522_Fall2015 - World Urban Development Data Visualization.

##Purpose:

Purpose of the project is to provide a user friendly and interactive visualization of world urban development over the past 5 decades. 

##Brief Description:

Based on the client’s data set, application aims to provide visual representations in two ways: Single parameter and Two parameter for comparison in the urban development for multiple countries. In single parameter selection, the application provides visualization in the form of small multiples linked by a map for countries selection for given set of dimension/parameter. In two parameter comparison, the application provides facility to a user similar to that of gapminder visualization, but using google motion chart api. It allows user to select two parameters and provides a timeline to user to brush over it to see trend in urban development for multiple countries. The application takes various HCI design concepts into consideration such as Fitt's Law, Tufte Design principles, Small multiples, linking and brushing and etc. 

##List of features:

Map Based Visualization of Urban Data
Motion Charts

##Disclaimer

This is entirely a class project and not to be used for anything other than class project as it has license constraints.

#Instruction for running application

##Download/Installation:

1. Clone the folder “Folder_Visualization” from github link:
      https://github.com/cs522fall2015/CS522_Fall2015
2. Download Node.js or any equivalent server if not available in user machine.
3. Install Node.js from terminal/command prompt.
4. Start the server by typing http-server and click enter.
5. Open the browser and go to localhost:8080/index.html.

##File Description:

index.html → Project HTML file.
js→ Folder containing all javascript files and json data required by application.
css→ Folder containing all custom style sheets and Bootstrap.css
fonts→ containing fonts used in the application
img→Folder containing internal images required for the application

##External Libraries Used:

Highcharts →A charting library written in pure JavaScript, to develop interactive visual informations.
Google Chart →A Flash based Google API to develop interactive motion charts and custom views.
Angular.js → javascript library to achieve dynamic data binding and manipulate DOM elements.
jquery → javascript library to handle user inputs, animations and combine with css to provide good UI.
Bootstrap CSS → CSS framework to achieve consistency and responsive screen design and layout.
