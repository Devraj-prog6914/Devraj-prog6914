import java.util.*;
import java.io.*;

class Customer {
    int id;
    String owner;
    double funds;
    boolean active;

    Customer(int id, String owner, double funds) {
        this.id = id;
        this.owner = owner;
        this.funds = funds;
        this.active = true;
    }

    String pack() {
        return id + "|" + owner + "|" + funds + "|" + active;
    }

    static Customer unpack(String row) {
        String[] data = row.split("\\|");
        Customer c = new Customer(
                Integer.parseInt(data[0]),
                data[1],
                Double.parseDouble(data[2]));
        c.active = Boolean.parseBoolean(data[3]);
        return c;
    }
}

// -------------------- Ledger Class --------------------
class Ledger {
    private Map<Integer, Customer> store = new LinkedHashMap<>();
    private final String FILE_NAME = "bank_store.dat";

    Ledger() {
        restore();
    }

    void addCustomer(int id, String name, double amt) {
        if (store.containsKey(id)) {
            System.out.println("Account ID already exists.");
            return;
        }
        if (amt < 500) {
            System.out.println("Minimum opening balance is ₹500.");
            return;
        }
        store.put(id, new Customer(id, name, amt));
        persist();
        System.out.println("New account created.");
    }

    void credit(int id, double amt) {
        Customer c = store.get(id);
        if (c == null || !c.active) {
            System.out.println("Invalid account.");
            return;
        }
        c.funds += amt;
        persist();
        System.out.println("Amount credited.");
    }

    void debit(int id, double amt) {
        Customer c = store.get(id);
        if (c == null || !c.active) {
            System.out.println("Invalid account.");
            return;
        }
        if (c.funds - amt < 500) {
            System.out.println("Balance limit violation.");
            return;
        }
        c.funds -= amt;
        persist();
        System.out.println("Amount debited.");
    }

    void view(int id) {
        Customer c = store.get(id);
        if (c == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("ID: " + c.id);
        System.out.println("Name: " + c.owner);
        System.out.println("Balance: ₹" + c.funds);
        System.out.println("Status: " + (c.active ? "ACTIVE" : "INACTIVE"));
    }

    void showAll() {
        if (store.isEmpty()) {
            System.out.println("No records available.");
            return;
        }
        for (Customer c : store.values()) {
            System.out.println("----------------------");
            view(c.id);
        }
    }

    private void persist() {
        try (PrintWriter pw = new PrintWriter(FILE_NAME)) {
            for (Customer c : store.values()) {
                pw.println(c.pack());
            }
        } catch (Exception e) {
            System.out.println("Save failed.");
        }
    }

    private void restore() {
        File f = new File(FILE_NAME);
        if (!f.exists())
            return;

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                Customer c = Customer.unpack(sc.nextLine());
                store.put(c.id, c);
            }
        } catch (Exception e) {
            System.out.println("Load failed.");
        }
    }
}

public class BankingApplication {
    static Scanner input = new Scanner(System.in);
    static Ledger ldg = new Ledger();

    public static void main(String[] args) {
        int opt;
        do {
            System.out.println("\n--- MINI BANK MENU ---");
            System.out.println("1. New Account");
            System.out.println("2. Add Money");
            System.out.println("3. Remove Money");
            System.out.println("4. View Account");
            System.out.println("5. View All");
            System.out.println("6. Exit");
            System.out.print("Choice: ");
            opt = input.nextInt();

            switch (opt) {
                case 1 -> create();
                case 2 -> deposit();
                case 3 -> withdraw();
                case 4 -> view();
                case 5 -> ldg.showAll();
                case 6 -> System.out.println("Session closed.");
                default -> System.out.println("Invalid option.");
            }
        } while (opt != 6);
    }

    static void create() {
        System.out.print("ID: ");
        int id = input.nextInt();
        input.nextLine();
        System.out.print("Name: ");
        String name = input.nextLine();
        System.out.print("Amount: ");
        double amt = input.nextDouble();
        ldg.addCustomer(id, name, amt);
    }

    static void deposit() {
        System.out.print("ID: ");
        ldg.credit(input.nextInt(), input.nextDouble());
    }

    static void withdraw() {
        System.out.print("ID: ");
        ldg.debit(input.nextInt(), input.nextDouble());
    }

    static void view() {
        System.out.print("ID: ");
        ldg.view(input.nextInt());
    }
}
