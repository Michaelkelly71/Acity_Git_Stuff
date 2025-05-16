#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>

using namespace std;

class Vehicle {
private:
    string id, brand, model;
    double rate;
    bool isRented;

public:
    Vehicle(string id = "", string brand = "", string model = "", double rate = 0.0, bool isRented = false)
        : id(id), brand(brand), model(model), rate(rate), isRented(isRented) {}

    
    void saveToFile(ofstream &outFile) {
        if (outFile.is_open()) {
            outFile << id << "," << brand << "," << model << "," << rate << "," << isRented << endl;
        }
    }

    
    static Vehicle loadFromFile(ifstream &inFile) {
        string id, brand, model;
        double rate;
        bool isRented;
        char delimiter;

        if (getline(inFile, id, ',') && getline(inFile, brand, ',') &&
            getline(inFile, model, ',') && (inFile >> rate >> delimiter >> isRented)) {
            inFile.ignore(); // To handle newline character
            return Vehicle(id, brand, model, rate, isRented);
        }

        return Vehicle();
    }

    
    void display() {
        cout << "ID: " << id << ", Brand: " << brand << ", Model: " << model
             << ", Rate: $" << fixed << setprecision(2) << rate << "/day"
             << (isRented ? " [RENTED]" : "") << endl;
    }

    
    string getId() { return id; }
    bool getIsRented() { return isRented; }
    void setIsRented(bool rented) { isRented = rented; }
};
