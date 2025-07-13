// Main class
import java.util.*;
import java.io.*;

class add_stock_to_file {
    Scanner sc = new Scanner(System.in);
    Random rand = new Random();
    String stock_name;
    double stock_price;
    double stock_quantity;
    int stock_id;

    public void add_stock() {
        System.out.print("Enter the stock name: ");
        stock_name = sc.nextLine();
        System.out.print("Enter the price of stock: ");
        stock_price = sc.nextDouble();
        while (stock_price < 0) {
            System.out.println("Please enter a valid value!");
            stock_price = sc.nextDouble();
        }
        System.out.print("Enter the quantity of the stock: ");
        stock_quantity = sc.nextDouble();
        while (stock_quantity < 0) {
            System.out.println("Please enter a valid value!");
            stock_quantity = sc.nextDouble();
        }
        stock_id = rand.nextInt(1000);
    }

    public void write_to_file() {
        try {
            File myFile = new File("stock_data.csv");
            FileWriter myFileWriter = new FileWriter(myFile, true);
            myFileWriter.write(stock_id + "," + stock_name + "," + stock_price + "," + stock_quantity + "\n");
            myFileWriter.close();
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}

class read_from_file extends add_stock_to_file {
    public void readFromFile() {
        try {
            File file = new File("stock_data.csv");
            Scanner reader = new Scanner(file);

            System.out.printf("%-10s %-15s %-15s %-15s\n", "Stock ID", "Stock Name", "Price", "Quantity");
            System.out.println("------------------------------------------------------------");

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                double price = Double.parseDouble(data[2].trim());
                double quantity = Double.parseDouble(data[3].trim());
                System.out.printf("%-10d %-15s %-15.2f %-15.2f\n", id, name, price, quantity);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("CSV file not found.");
            e.printStackTrace();
        }
    }
}

class portfolio extends read_from_file {
    Scanner sy = new Scanner(System.in);
    double bill;
    int bought_by_customer;
    boolean stockFound = false;

    public void buy() {
        System.out.print("Enter the Stock ID you want to buy: ");
        stock_id = sy.nextInt();

        File updFile = new File("updated.csv");
        File originalFile = new File("stock_data.csv");

        try {
            FileWriter myFileWriter = new FileWriter(updFile);
            Scanner fileReader = new Scanner(originalFile);

            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                double price = Double.parseDouble(data[2].trim());
                double quantity = Double.parseDouble(data[3].trim());

                if (stock_id == id) {
                    stockFound = true;
                    System.out.println("Enter the amount of stocks you want to buy:");
                    bought_by_customer = sy.nextInt();

                    if (bought_by_customer <= 0 || bought_by_customer > quantity) {
                        System.out.println("Invalid quantity. Transaction skipped.");
                        myFileWriter.write(line + "\n");
                        continue;
                    }

                    stock_name = name;
                    stock_price = price;
                    stock_quantity = quantity;

                    quantity -= bought_by_customer;
                    bill = bought_by_customer * price;
                    System.out.println("Purchase successful. Total bill: " + bill);
                }

                myFileWriter.write(id + "," + name + "," + price + "," + quantity + "\n");
            }

            myFileWriter.close();
            fileReader.close();

            if (!stockFound) {
                System.out.println("Stock ID not found in market. Transaction canceled.");
                return;
            }

            if (originalFile.delete()) {
                updFile.renameTo(originalFile);
            } else {
                System.out.println("Failed to update stock data file.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writing_to_updated_file() {
        if (!stockFound) return;

        File pFile = new File("original_updated_pfile.csv");
        File tempFile = new File("temp_portfolio.csv");

        boolean stockUpdated = false;

        try {
            if (!pFile.exists()) {
                pFile.createNewFile();
            }

            Scanner read = new Scanner(pFile);
            FileWriter writer = new FileWriter(tempFile);

            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                double price = Double.parseDouble(data[2].trim());
                double quantity = Double.parseDouble(data[3].trim());

                if (id == stock_id) {
                    quantity += bought_by_customer;
                    stockUpdated = true;
                    System.out.println("Existing portfolio stock updated.");
                }

                writer.write(id + "," + name + "," + price + "," + quantity + "\n");
            }

            if (!stockUpdated) {
                writer.write(stock_id + "," + stock_name + "," + stock_price + "," + bought_by_customer + "\n");
                System.out.println("New stock added to portfolio.");
            }

            writer.close();
            read.close();

            pFile.delete();
            tempFile.renameTo(pFile);

        } catch (Exception e) {
            System.out.println("Error updating portfolio: " + e.getMessage());
        }
    }

    public void display() {
        File file = new File("original_updated_pfile.csv");
        Random rand = new Random();

        try {
            if (!file.exists() || file.length() == 0) {
                System.out.println("Portfolio is empty.");
                return;
            }

            Scanner read = new Scanner(file);
            System.out.printf("%-10s %-15s %-10s %-10s %-15s\n", "Stock ID", "Name", "Price", "Quantity", "Current Price");
            System.out.println("--------------------------------------------------------------------------");

            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                double price = Double.parseDouble(data[2].trim());
                double quantity = Double.parseDouble(data[3].trim());
                double Current_price = 100 + rand.nextDouble() * 900;

                System.out.printf("%-10d %-15s %-10.2f %-10.2f %-15.2f\n", id, name, price, quantity, Current_price);
            }

            read.close();
        } catch (Exception e) {
            System.out.println("Error displaying portfolio: " + e.getMessage());
        }
    }
    public void sell() {
    System.out.print("Enter the Stock ID you want to sell: ");
    int sell_id = sy.nextInt();
    System.out.print("Enter the quantity you want to sell: ");
    int sell_quantity = sy.nextInt();

    File portfolioFile = new File("original_updated_pfile.csv");
    File tempPortfolio = new File("temp_portfolio.csv");
    File marketFile = new File("stock_data.csv");
    File tempMarket = new File("temp_market.csv");

    boolean stockInPortfolio = false;
    boolean marketUpdated = false;
    String stockName = "";
    double stockPrice = 0;

    try {
        // First update portfolio
        Scanner pfReader = new Scanner(portfolioFile);
        FileWriter pfWriter = new FileWriter(tempPortfolio);

        while (pfReader.hasNextLine()) {
            String line = pfReader.nextLine();
            String[] data = line.split(",");

            int id = Integer.parseInt(data[0].trim());
            String name = data[1].trim();
            double price = Double.parseDouble(data[2].trim());
            double quantity = Double.parseDouble(data[3].trim());

            if (id == sell_id) {
                if (sell_quantity <= 0 || sell_quantity > quantity) {
                    System.out.println("Invalid sell quantity. Transaction aborted.");
                    pfWriter.write(line + "\n");
                    pfReader.close();
                    pfWriter.close();
                    tempPortfolio.delete();
                    return;
                }

                stockInPortfolio = true;
                stockName = name;
                stockPrice = price;

                quantity -= sell_quantity;
                if (quantity > 0)
                    pfWriter.write(id + "," + name + "," + price + "," + quantity + "\n");
                else
                    System.out.println("All shares sold. Stock removed from portfolio.");
            } else {
                pfWriter.write(line + "\n");
            }
        }

        pfReader.close();
        pfWriter.close();

        if (!stockInPortfolio) {
            System.out.println("You don't own this stock.");
            tempPortfolio.delete();
            return;
        }

        // Update portfolio file
        portfolioFile.delete();
        tempPortfolio.renameTo(portfolioFile);

        // Now update market
        Scanner mReader = new Scanner(marketFile);
        FileWriter mWriter = new FileWriter(tempMarket);

        while (mReader.hasNextLine()) {
            String line = mReader.nextLine();
            String[] data = line.split(",");

            int id = Integer.parseInt(data[0].trim());
            String name = data[1].trim();
            double price = Double.parseDouble(data[2].trim());
            double quantity = Double.parseDouble(data[3].trim());

            if (id == sell_id) {
                quantity += sell_quantity;
                marketUpdated = true;
            }

            mWriter.write(id + "," + name + "," + price + "," + quantity + "\n");
        }

        if (!marketUpdated) {
            mWriter.write(sell_id + "," + stockName + "," + stockPrice + "," + sell_quantity + "\n");
        }

        mReader.close();
        mWriter.close();

        marketFile.delete();
        tempMarket.renameTo(marketFile);

        double totalSale = stockPrice * sell_quantity;
        System.out.println("Sell successful! Amount received: " + totalSale);

    } catch (Exception e) {
        System.out.println("Error while selling: " + e.getMessage());
    }
}

}

public class Stock {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        portfolio port = new portfolio();

        int choice;
        do {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. Add Stock to Market");
            System.out.println("2. View Market Stocks");
            System.out.println("3. Buy Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Sell Stock");
System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    port.add_stock();
                    port.write_to_file();
                    System.out.println("Stock added successfully!");
                    break;
                case 2:
                    port.readFromFile();
                    break;
                case 3:
                    port.buy();
                    port.writing_to_updated_file();
                    if (port.stockFound)
                        System.out.println("Stock added to portfolio.");
                    break;
                case 4:
                    port.display();
                    break;
                case 5:
                    port.sell();
                  break;
                  case 6:
                  System.out.println("Exit"); 
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 6);
    }
}
