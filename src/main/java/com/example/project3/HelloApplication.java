package com.example.project3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class HelloApplication extends Application implements Bill {

    private Button createBackButton(Stage stage, Scene previousScene) {
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            stage.setScene(previousScene);
        });
        return backButton;
    }

    GridPane p = new GridPane();
    Scene scene1;
    Button backButton = new Button("Back");
    private static final String ORDERS_FILE = "orders.txt";
    TextField customerNameField = new TextField();
    TextField phoneNumberField = new TextField();
    private Location selectedLocation;
    ComboBox<String> resturantCombo = new ComboBox<>();
    ComboBox<Location> cityCombo = new ComboBox<>();
    ComboBox<String> townCombo = new ComboBox<>();

    @Override
    public void start(Stage stage) throws NullPointerException {


        stage.setWidth(1000);
        stage.setHeight(700);
        stage.setResizable(false);


        Label welcomeLabel = new Label("Welcome to the Flavourfull Fusion");
        welcomeLabel.setStyle(
                "-fx-font-size: 40px;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI Black';" +
                        "-fx-effect: dropshadow(one-pass-box, black, 8, 0, 3, 3);"
        );

        Button button1 = new Button("Get Started");
        button1.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-font-size: 24px;" +
                        "-fx-padding: 12 30 12 30;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-font-family: 'Segoe UI Semibold';"
        );


        Image backGroundImage = new Image(
                getClass().getResourceAsStream("/images/main.jpeg")
        );

        BackgroundImage backGround = new BackgroundImage(
                backGroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        );

        VBox frontLayoutVBox = new VBox(30);
        frontLayoutVBox.getChildren().addAll(welcomeLabel, button1);
        frontLayoutVBox.setAlignment(Pos.CENTER);
        frontLayoutVBox.setBackground(new Background(backGround));
        frontLayoutVBox.setStyle("-fx-padding: 60;");

        Scene frontScene = new Scene(frontLayoutVBox, 1500, 1200);
        stage.setScene(frontScene);
        stage.setResizable(false);
        stage.centerOnScreen();


        ArrayList<String> cities = new ArrayList<>(Arrays.asList("Lahore", "Karachi"));
        Location.setCities(cities);



        // Create an instance of Location
        Location lahoreLocation = new Location();
        lahoreLocation.setCity("Lahore");
        lahoreLocation.setTowns(new ArrayList<>(Arrays.asList("Johar Town", "Wapda Town")));
        lahoreLocation.setRestaurants("Johar Town", getRestaurants(new ArrayList<>(Arrays.asList("Lahore Fast Food", "Sushi House"))));
        lahoreLocation.setRestaurants("Wapda Town", getRestaurants(new ArrayList<>(Arrays.asList("Pizza Junction", "Dagwood"))));

        // Create an instance of Location
        Location karachiLocation = new Location();
        karachiLocation.setCity("Karachi");
        karachiLocation.setTowns(new ArrayList<>(Arrays.asList("Clifton", "Defence")));
        karachiLocation.setRestaurants("Clifton", getRestaurants(new ArrayList<>(Arrays.asList("Cafe Aylanto", "Mango Tango"))));
        karachiLocation.setRestaurants("Defence", getRestaurants(new ArrayList<>(Arrays.asList("Italian Cuisine", "Subway"))));
        ArrayList<Location> locations = new ArrayList<>(Arrays.asList(lahoreLocation, karachiLocation));
        cityCombo.setItems(FXCollections.observableArrayList(locations));



        resturantCombo.setPromptText("Restaurants");
        resturantCombo.setStyle("-fx-font-size:30; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Arial Black;");

        cityCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                selectedLocation = cityCombo.getSelectionModel().getSelectedItem();
                if (selectedLocation != null) {
                    townCombo.setItems(FXCollections.observableArrayList(selectedLocation.getTowns()));
                }
            }
        });

        townCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedTown = townCombo.getSelectionModel().getSelectedItem();
                if (selectedLocation != null) {
                    // Convert Restaurant objects to String names
                    ArrayList<String> restaurantNames = new ArrayList<>();
                    for (Restaurant r : selectedLocation.getSelectedTownRestaurants(selectedTown)) {
                        restaurantNames.add(r.getName());
                    }
                    resturantCombo.setItems(FXCollections.observableArrayList(restaurantNames));
                }
            }
        });



        //ArrayList of restaurants for each Town
        ArrayList<String> joharRestaurants = new ArrayList<>();
        joharRestaurants.add("Lahore Fast Food");
        joharRestaurants.add("Sushi House");

        ArrayList<String> wapdaRestaurants = new ArrayList<>();
        wapdaRestaurants.add("Pizza Junction");
        wapdaRestaurants.add("Dagwood");

        ArrayList<String> cliftonRestaurant = new ArrayList<>();
        cliftonRestaurant.add("Cafe Aylanto");
        cliftonRestaurant.add("Mango Tango");

        ArrayList<String> defenceRestaurant = new ArrayList<>();
        defenceRestaurant.add("Italian Cuisine");
        defenceRestaurant.add("Subway");

        //Towns will be selected according to the cities

        townCombo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedTown = townCombo.getSelectionModel().getSelectedItem();
                if (selectedTown.equalsIgnoreCase("Johar Town")) {
                    resturantCombo.setItems(FXCollections.observableArrayList(joharRestaurants));
                } else if (selectedTown.equalsIgnoreCase("Wapda Town")) {
                    resturantCombo.setItems(FXCollections.observableArrayList(wapdaRestaurants));
                } else if (selectedTown.equalsIgnoreCase("Clifton")) {
                    resturantCombo.setItems(FXCollections.observableArrayList(cliftonRestaurant));
                } else if (selectedTown.equalsIgnoreCase("Defence")) {
                    resturantCombo.setItems(FXCollections.observableArrayList(defenceRestaurant));
                }

            }
        });

        // Storing Items and their Prices

        ArrayList<Item> lahoreFastFoodMenuItems = new ArrayList<>();
        lahoreFastFoodMenuItems.add(new Item("Zinger Burger", 299));
        lahoreFastFoodMenuItems.add(new Item("Cheese Burger", 399));
        lahoreFastFoodMenuItems.add(new Item("Grilled Burger", 449));
        lahoreFastFoodMenuItems.add(new Item("Chicken Burger", 249));
        lahoreFastFoodMenuItems.add(new Item("Mozzarella Burger", 599));
        lahoreFastFoodMenuItems.add(new Item("Tower Burger", 699));

        ArrayList<Item> sushiHouseMenuItems = new ArrayList<>();
        sushiHouseMenuItems.add(new Item("Sushi Platter", 599));
        sushiHouseMenuItems.add(new Item("SALMON Sushi", 899));
        sushiHouseMenuItems.add(new Item("YellowTail Sushi", 999));
        sushiHouseMenuItems.add(new Item("Sashimi Sushi", 1149));
        sushiHouseMenuItems.add(new Item("Simple Sushi with Rice", 799));
        sushiHouseMenuItems.add(new Item("Tuna Sushi", 849));

        ArrayList<Item> PizzaMenuItems = new ArrayList<>();

        PizzaMenuItems.add(new Item("Chicken Tikka Pizza", 1199));
        PizzaMenuItems.add(new Item("Chicken Fajita Pizza", 1249));
        PizzaMenuItems.add(new Item("Chicken Supreme Pizza", 1299));
        PizzaMenuItems.add(new Item("Malai Boti Pizza", 1399));
        PizzaMenuItems.add(new Item("Behari Kebab Pizza", 1499));
        PizzaMenuItems.add(new Item("Pizza Junction Special Pizza", 1599));

        ArrayList<Item> DagwoodMenuItems = new ArrayList<>();

        DagwoodMenuItems.add(new Item("Chicken Strips", 399));
        DagwoodMenuItems.add(new Item("Chicken Fillet Sandwich", 499));
        DagwoodMenuItems.add(new Item("Cocktail Sandwich", 599));
        DagwoodMenuItems.add(new Item("Cheese Lover Sandwich", 699));
        DagwoodMenuItems.add(new Item("Chocolate Explosion", 449));
        DagwoodMenuItems.add(new Item("Chocolate Brownie", 799));

        ArrayList<Item> AylantoMenuItems = new ArrayList<>();
        AylantoMenuItems.add(new Item("Decked Beef", 999));
        AylantoMenuItems.add(new Item("Grilled Fillet of Chicken", 899));
        AylantoMenuItems.add(new Item("Margarita Pizza", 1499));
        AylantoMenuItems.add(new Item("Penne Arrabiata with Grill Chicken", 1549));
        AylantoMenuItems.add(new Item("Spaghetti Bolognese", 1199));
        AylantoMenuItems.add(new Item("Club Sandwich", 449));

        ArrayList<Item> MangoTangoMenuItems = new ArrayList<>();
        MangoTangoMenuItems.add(new Item("Mango Ticky Rice", 599));
        MangoTangoMenuItems.add(new Item("Nutella Mini Pancake", 499));
        MangoTangoMenuItems.add(new Item("Mango Kakigori", 699));
        MangoTangoMenuItems.add(new Item("Chocolate Mochi", 549));
        MangoTangoMenuItems.add(new Item("Strawberry Japanese Cheesecake Layers", 799));
        MangoTangoMenuItems.add(new Item("Mixed Berry And Coconut Chia Pudding", 899));

        ArrayList<Item> ItalianCuisineMenuItems = new ArrayList<>();
        ItalianCuisineMenuItems.add(new Item("Chicken Parmesan", 599));
        ItalianCuisineMenuItems.add(new Item("Creamy Tortellini Soup with Sausage", 699));
        ItalianCuisineMenuItems.add(new Item("Skillet Neapolitan Margherita Pizza", 899));
        ItalianCuisineMenuItems.add(new Item("Salami and Mushroom Skillet Pizza", 949));
        ItalianCuisineMenuItems.add(new Item("Creamy Garlic Salmon", 799));
        ItalianCuisineMenuItems.add(new Item("Tomato Basil Soup", 549));

        ArrayList<Item> SubwayMenuItems = new ArrayList<>();

        SubwayMenuItems.add(new Item("BBQ Chicken", 599));
        SubwayMenuItems.add(new Item("Peri Peri Chicken", 699));
        SubwayMenuItems.add(new Item("Roasted Chicken Breast", 799));
        SubwayMenuItems.add(new Item("Chicken Mughlai Sub", 849));
        SubwayMenuItems.add(new Item("Chicken Chapli Sub", 999));
        SubwayMenuItems.add(new Item("Italian B.M.T", 899));


        resturantCombo.setOnAction(e -> {
            String selectedRestaurant = resturantCombo.getSelectionModel().getSelectedItem();
            if (selectedRestaurant != null) {
                if (selectedRestaurant.equals("Lahore Fast Food")) {
                    GridPane p1 = createMenuGridPane(stage, "Lahore Fast Food", lahoreFastFoodMenuItems);
                    p1.setBackground(setLocalImage("/images/fastfood.jpeg"));


                    Scene scene = new Scene(p1, 500, 500);
                    p1.setHgap(5);
                    p1.setVgap(5);
                    stage.setScene(scene);

                    stage.show();
                } else if (selectedRestaurant.equals("Sushi House")) {
                    GridPane p2 = createMenuGridPane(stage, "Sushi House", sushiHouseMenuItems);
                    p2.setBackground(setLocalImage("/images/sushi.jpeg"));


                    Scene scene = new Scene(p2, 1000, 700);
                    p2.setHgap(5);
                    p2.setVgap(5);
                    stage.setScene(scene);

                    stage.show();
                } else if (selectedRestaurant.equals("Pizza Junction")) {
                    GridPane p3 = createMenuGridPane(stage, "Pizza Junction", PizzaMenuItems);
                    p3.setBackground(setLocalImage("/images/pizza.jpeg"));


                    Scene scene = new Scene(p3, 100, 1000);
                    p3.setHgap(5);
                    p3.setVgap(5);
                    stage.setScene(scene);
                    stage.show();
                } else if (selectedRestaurant.equals("Dagwood")) {
                    GridPane p4 = createMenuGridPane(stage, "Dagwood", DagwoodMenuItems);
                    p4.setBackground(setLocalImage("/images/burger.jpeg"));

                    Scene scene = new Scene(p4, 1000, 700);
                    p4.setHgap(5);
                    p4.setVgap(5);
                    stage.setScene(scene);
                    stage.setWidth(900);
                    stage.setHeight(600);
                    stage.centerOnScreen();
                    stage.show();
                } else if (selectedRestaurant.equals("Cafe Aylanto")) {
                    GridPane p5 = createMenuGridPane(stage, "Cafe Aylanto", AylantoMenuItems);
                    p5.setBackground(setLocalImage("/images/aylanto.jpeg"));

                    Scene scene = new Scene(p5, 1000, 700);
                    p5.setHgap(5);
                    p5.setVgap(5);
                    stage.setScene(scene);
                    stage.show();
                } else if (selectedRestaurant.equals("Mango Tango")) {
                    GridPane p6 = createMenuGridPane(stage, "Mango Tango", MangoTangoMenuItems);
                    p6.setBackground(setLocalImage("/images/mango.jpeg"));

                    Scene scene = new Scene(p6, 1000, 700);
                    p6.setHgap(5);
                    p6.setVgap(5);
                    stage.setScene(scene);
                    stage.show();
                } else if (selectedRestaurant.equals("Italian Cuisine")) {
                    GridPane p7 = createMenuGridPane(stage, "Italian Cuisine", ItalianCuisineMenuItems);
                    p7.setBackground(setLocalImage("/images/italian.jpeg"));

                    Scene scene = new Scene(p7, 1000, 700);
                    p7.setHgap(5);
                    p7.setVgap(5);
                    stage.setScene(scene);
                    stage.show();
                } else if (selectedRestaurant.equals("Subway")) {
                    GridPane p8 = createMenuGridPane(stage, "Subway", SubwayMenuItems);
                    p8.setBackground(setLocalImage("/images/subway.jpeg"));

                    Scene scene = new Scene(p8, 1000, 700);
                    p8.setHgap(5);
                    p8.setVgap(5);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        });


        GridPane p = new GridPane();
        p.setAlignment(Pos.CENTER);
        p.setHgap(20);
        p.setVgap(20);
        p.setStyle("-fx-padding: 40;");

// Title
        Label title = new Label("Choose Your Location");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
        p.add(title, 0, 0, 2, 1);

// Labels
        Label l1 = new Label("City:");
        Label l2 = new Label("Town:");
        Label l3 = new Label("Restaurant:");

        String labelStyle = "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;";
        l1.setStyle(labelStyle);
        l2.setStyle(labelStyle);
        l3.setStyle(labelStyle);

        String comboStyle =
                "-fx-font-size: 18px; -fx-background-radius: 10; -fx-padding: 5;";

        cityCombo.setStyle(comboStyle);
        townCombo.setStyle(comboStyle);
        resturantCombo.setStyle(comboStyle);

        p.add(l1, 0, 1);
        p.add(cityCombo, 1, 1);

        p.add(l2, 0, 2);
        p.add(townCombo, 1, 2);

        p.add(l3, 0, 3);
        p.add(resturantCombo, 1, 3);

        p.setBackground(new Background(new BackgroundImage(
                new Image(getClass().getResource("/images/location.jpeg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, false)
        )));



        p.setHgap(10);
        p.setVgap(10);
        scene1 = new Scene(p, 1000, 700);
        button1.setOnAction(e -> {
            stage.setScene(scene1);
        });

        stage.setResizable(true);
        stage.setTitle("Food Ordering System");
        stage.show();



    }

    private ArrayList<Restaurant> getRestaurants(ArrayList<String> restaurantNames) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        return restaurants;
    }

    private GridPane createMenuGridPane(Stage stage, String restaurantName, ArrayList<Item> menuItems) {

        GridPane menuGridPane = new GridPane();

        ArrayList<CheckBox> menuCheckBoxes = new ArrayList<>();
        for (int i = 1; i < menuItems.size(); i++) {
            CheckBox checkBox = new CheckBox(menuItems.get(i).getItemName() + " Rs. " + menuItems.get(i).getPrice());
            menuCheckBoxes.add(checkBox);
            menuGridPane.add(checkBox, 130, 25 + i);
        }

        // Add drink checkboxes
        Label drinkLabel = new Label("Select Drinks: ");
        drinkLabel.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Arial Black;");
        ArrayList<CheckBox> drinkCheckBoxes = new ArrayList<>();
        String[] drinkNames = {"Coke", "Pepsi", "Sprite", "Water"};
        for (String drink : drinkNames) {
            CheckBox checkBox = new CheckBox(drink + " Rs.190");
            drinkCheckBoxes.add(checkBox);
        }

        // Add drink checkboxes to the menuGridPane
        int drinkRowIndex = menuItems.size();
        menuGridPane.add(drinkLabel, 130, 25 + drinkRowIndex);
        for (CheckBox drinkCheckBox : drinkCheckBoxes) {
            drinkRowIndex++;
            menuGridPane.add(drinkCheckBox, 130, 25 + drinkRowIndex);
        }

        Label l1 = new Label();
        Label l2 = new Label();

        // Add the submitOrder button
        Button submitOrderButton = new Button("Place Order");
        submitOrderButton.setOnAction(e -> {

            // Check if the text fields are empty
            if (customerNameField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
                l1.setText("Oops!you forget to enter your name or your phone number");

                l1.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Times New Roman';");

            } else {
                submitOrder(stage, restaurantName, menuCheckBoxes, drinkCheckBoxes, customerNameField, phoneNumberField);
            }

        });

        // Add the button to the menuGridPane
        menuGridPane.add(submitOrderButton, 130, 47);


        Label l4 = new Label("Select Menu Item: ");
        l4.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Arial Black;");
        Label l5 = new Label("Enter Your Name: ");
        l5.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Arial Black;");
        Label l6 = new Label("Enter Your Phone Number: ");
        l6.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Arial Black;");
        Label l7 = new Label("Enter Your Details: ");
        l7.setStyle("-fx-font-size:20; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Arial Black;");

        Button backButtonMenu = createBackButton(stage, scene1);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        menuGridPane.add(l4, 130, 23);
        menuGridPane.add(l1, 130, 48);
        menuGridPane.add(l2, 130, 50);
        menuGridPane.add(l7, 130, 38);
        menuGridPane.add(l5, 130, 41);
        menuGridPane.add(l6, 130, 43);

        menuGridPane.add(customerNameField, 130, 42);
        menuGridPane.add(phoneNumberField, 130, 44);
        menuGridPane.add(backButtonMenu, 130, 48);
        menuGridPane.add(exitButton, 131, 47);

        return menuGridPane;
    }

    private void submitOrder(Stage stage, String restaurantName, ArrayList<CheckBox> menuCheckBoxes, ArrayList<CheckBox> drinkCheckBoxes, TextField customerNameField, TextField phoneNumberField) {

        // Collect selected menu items
        ArrayList<String> selectedMenuItems = new ArrayList<>();
        for (CheckBox checkBox : menuCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedMenuItems.add(checkBox.getText());
            }
        }

        // Collect selected drinks
        ArrayList<String> selectedDrinks = new ArrayList<>();
        for (CheckBox checkBox : drinkCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedDrinks.add(checkBox.getText());
            }
        }

        // Calculate the bill
        double totalBill = calculateTotalBill(selectedMenuItems, selectedDrinks);

        String customerName = customerNameField.getText();
        String phoneNumber = phoneNumberField.getText();

        // Display a confirmation
        String confirmationMessage = String.format("Order placed at %s%nTotal Bill: Rs.%.2f%nCustomer: %s, Phone: %s",
                restaurantName, totalBill, customerName, phoneNumber);

        // Display the confirmation message
        Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
        confirmationDialog.setHeaderText("Order Confirmation");
        confirmationDialog.setContentText(confirmationMessage);
        confirmationDialog.showAndWait();

    }


    @Override
    public double calculateTotalBill(ArrayList<String> selectedMenuItems, ArrayList<String> selectedDrinks) {
        double totalBill = 0.0;

        // Calculate total for menu items
        for (String menuItem : selectedMenuItems) {
            String[] parts = menuItem.split("Rs.");
            if (parts.length > 1) {
                double itemPrice = Double.parseDouble(parts[1].trim());
                totalBill += itemPrice;
            }
        }

        // Calculate total for drinks
        for (String drink : selectedDrinks) {
            String[] parts = drink.split("Rs.");
            if (parts.length > 1) {
                double drinkPrice = Double.parseDouble(parts[1].trim());
                totalBill += drinkPrice;
            }
        }

        // Get customer details
        String customerName = customerNameField.getText();
        Location selectedCity = cityCombo.getSelectionModel().getSelectedItem();
        String city = selectedCity != null ? selectedCity.getCity() : "";
        String town = townCombo.getSelectionModel().getSelectedItem();
        String restaurant = resturantCombo.getSelectionModel().getSelectedItem();
        String phone = phoneNumberField.getText();

        // File in project directory
        String path = "ORDERS_FILE.txt"; // relative path

        try {
            // Ensure file exists
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read current number of orders
            int orderIndex = 1;
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Order #")) {
                    orderIndex++;
                }
            }
            scanner.close();

            // Write the new order
            try (Formatter output = new Formatter(new FileWriter(file, true))) {
                String formattedTotalBill = String.format(Locale.US, "%.2f", totalBill);
                output.format("Order #%d\n", orderIndex);
                output.format("Customer Name: %s\n", customerName);
                output.format("City: %s\n", city);
                output.format("Town: %s\n", town);
                output.format("Restaurant Name: %s\n", restaurant);
                output.format("Total Bill: Rs.%s\n", formattedTotalBill);
                output.format("Phone Number: %s\n", phone);
                output.format("-------------------------------\n"); // separator
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return totalBill;
    }



    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application
    }

    private Background setLocalImage(String imagePath) {
        Image img = new Image(getClass().getResourceAsStream(imagePath));
        BackgroundImage bgImg = new BackgroundImage(
                img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        1.0, 1.0,
                        true, true,
                        false, false
                )
        );
        return new Background(bgImg);
    }

}