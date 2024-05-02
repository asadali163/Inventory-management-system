# Welcome to Inventory Management System!

Hey there! 👋 Welcome to the Inventory Management System, a handy tool built in Java to help you keep track of your inventory hassle-free! Whether you're running a small business, managing a warehouse, or just need to keep tabs on your stuff, this software has got you covered.

## Who's It For?
This software is designed with small to medium-sized businesses, retail stores, and warehouses in mind. If you're someone who needs to juggle inventory, manage stock levels, and stay organized, you're in the right place!

## What's It All About?
Ever felt overwhelmed by inventory management? Say goodbye to manual tracking and hello to efficiency! Our system is here to streamline your inventory tasks, reduce errors, and make your life a whole lot easier.

## When and Where Can You Use It?
Anytime, anywhere! Whether you're at your desk, on the shop floor, or chilling at home, you can access our system on compatible devices. Need to check stock levels in the middle of the night? No problem!

## How Does It Work?
It's pretty straightforward:
- We've built the system in Java, with a sleek graphical interface (thanks to JavaFX) to make navigation a breeze.
- Your product data is stored securely in a database (like MySQL), so you can access it whenever you need.
- Want to add, update, delete, or search for items? Just a few clicks and you're done!
- We've also added some cool features to handle exceptions and provide feedback, so you're always in the loop.

## Why Choose Us?
- **User-friendly Interface**: No tech wizardry required! Our GUI is easy to use, even for beginners.
- **Efficiency Boost**: Say goodbye to manual grunt work. Our system automates tasks, so you can focus on what matters.
- **Real-time Insights**: Need info on the fly? We've got your back with up-to-date inventory data.
- **Centralized Data**: No more hunting for spreadsheets or paper records. Everything you need is right here.
- **Customizable and Scalable**: Whether you're a one-person operation or a growing business, our system can adapt to your needs.

## Getting Started
Ready to give it a spin? Here's how to get started:
1. Clone this repository to your local machine.
2. Make sure you have Java and MySQL set up.
3. Compile and run the Java files to launch the application.
4. Start managing your inventory like a pro!

## UML Class Diagrams
We've created UML class diagrams to illustrate the relationships between the classes in our software. Check them out below:

### InventoryUI Class Diagram
![InventoryUI and Inventory Relationship](https://github.com/asadali163/Inventory-management-system/assets/87898113/0e5d6c44-ccbe-4541-9198-0a3f6fb9fe6d>)

*Description:  The InventoryUI class serves as the graphical user interface (GUI) for our Inventory Management System. It encapsulates the interface elements, such as buttons, input fields, and tables, facilitating interaction with the inventory data. Through its methods and attributes, InventoryUI orchestrates the display of inventory items, addition, deletion, and modification operations, ensuring a seamless user experience.*

### Inventory Class Diagram
![InventoryUI and Inventory Relationship](https://github.com/asadali163/Inventory-management-system/assets/87898113/4e87b738-e8e6-4908-9dc0-0870f3a712d7)

*The Inventory class lies at the heart of our system, functioning as the repository for managing inventory-related data. It maintains collections of items, such as itemList, itemDeque, and itemSet, offering various data structures for storage and retrieval. Through methods like addItem, updateItem, and deleteItem, Inventory provides functionalities for manipulating inventory items. Additionally, it interfaces with the database via initializeDatabase and fetchDataFromDatabase methods to ensure persistent storage and retrieval of inventory data.*

### Item Class Diagram
![Item Class Diagram](https://github.com/asadali163/Inventory-management-system/assets/87898113/428c888c-4cf8-4fff-bacb-10056ce793ee)

*Description: The Item class represents individual items within our inventory system. It encapsulates attributes such as ID, name, quantity, and price, defining the core properties of each inventory item. Through getter and setter methods, Item enables access to and modification of its attributes, ensuring data integrity and encapsulation principles. With its constructor and initialization methods, Item facilitates the creation and initialization of item objects with specific properties.*

### FileManager Class Diagram
![FileManager Class Diagram](https://github.com/asadali163/Inventory-management-system/assets/87898113/21f60eea-c767-430f-a50f-6c3afb44df92)

*The FileManager class plays a crucial role in our system, providing functionalities for managing data persistence. It offers methods like saveToFile and loadFromFile, facilitating the storage and retrieval of inventory data from external files. By encapsulating file operations, FileManager abstracts away the complexities of file handling, ensuring seamless integration with the rest of the system. Through its robust error handling and feedback mechanisms, FileManager ensures data integrity and reliability in data storage and retrieval operations.*




## Contributing
Got ideas to make our system even better? We'd love to hear them! Feel free to open an issue or pull request.

## License
This project is licensed under the [MIT License](LICENSE).
