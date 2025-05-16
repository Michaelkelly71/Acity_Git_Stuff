#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>
#include <ctime>

#include "Vehicle.cpp"
#include "Rental.cpp"

using namespace std;

class RentalSystem {
private:
    static const int MAX_VEHICLES = 100;
    static const int MAX_RENTALS = 100;  

    Vehicle vehicles[MAX_VEHICLES]; 
    Rental rentals[MAX_RENTALS];   

    int vehicleCount;              
    int rentalCount;               

    const string vehicleFile = "vehicles.txt";
    const string rentalFile = "rentals.txt";

public:
    
    RentalSystem() : vehicleCount(0), rentalCount(0) {}

    // Load data from files
    void loadFromFile() {
        ifstream inVehicle(vehicleFile);
        if (inVehicle.is_open()) {
            while (vehicleCount < MAX_VEHICLES && inVehicle.peek() != EOF) {
                vehicles[vehicleCount] = Vehicle::loadFromFile(inVehicle);
                vehicleCount++;
            }
            inVehicle.close();
        }

        ifstream inRental(rentalFile);
        if (inRental.is_open()) {
            while (rentalCount < MAX_RENTALS && inRental.peek() != EOF) {
                rentals[rentalCount] = Rental::loadFromFile(inRental);
                rentalCount++;
            }
            inRental.close();
        }
    }

    
    void saveToFile() {
        ofstream outVehicle(vehicleFile);
        if (outVehicle.is_open()) {
            for (int i = 0; i < vehicleCount; i++) {
                vehicles[i].saveToFile(outVehicle);
            }
            outVehicle.close();
        }

        ofstream outRental(rentalFile);
        if (outRental.is_open()) {
            for (int i = 0; i < rentalCount; i++) {
                rentals[i].saveToFile(outRental);
            }
            outRental.close();
        }
    }

    
    void addVehicle() {
        if (vehicleCount >= MAX_VEHICLES) {
            cout << "Error: Maximum vehicle limit reached." << endl;
            return;
        }

        string id, brand, model;
        double rate;

        cout << "Enter Vehicle ID: ";
        cin >> id;
        cout << "Enter Brand: ";
        cin >> brand;
        cout << "Enter Model: ";
        cin >> model;
        cout << "Enter Daily Rate: ";
        cin >> rate;

        vehicles[vehicleCount] = Vehicle(id, brand, model, rate);
        vehicleCount++;
        cout << "Vehicle added successfully!" << endl;
    }

    // Rent a vehicle
    void rentVehicle() {
        if (rentalCount >= MAX_RENTALS) {
            cout << "Error: Maximum rental limit reached." << endl;
            return;
        }

        string id, customerName;
        cout << "Enter Vehicle ID to Rent: ";
        cin >> id;
        cout << "Enter Customer Name: ";
        cin.ignore();
        getline(cin, customerName);

        for (int i = 0; i < vehicleCount; i++) {
            if (vehicles[i].getId() == id) {
                if (!vehicles[i].getIsRented()) {
                    vehicles[i].setIsRented(true);
                    rentals[rentalCount] = Rental(customerName, id, time(0));
                    rentalCount++;
                    cout << "Vehicle rented successfully!" << endl;
                    return;
                }
                cout << "Error: Vehicle is already rented!" << endl;
                return;
            }
        }
        cout << "Error: Vehicle ID not found!" << endl;
    }

    
    void displayVehicles() {
        if (vehicleCount == 0) {
            cout << "No vehicles available in the system." << endl;
            return;
        }

        for (int i = 0; i < vehicleCount; i++) {
            vehicles[i].display();
        }
    }

    
    void displayRentals() {
        if (rentalCount == 0) {
            cout << "No rental records available." << endl;
            return;
        }

        for (int i = 0; i < rentalCount; i++) {
            rentals[i].display();
        }
    }
};
