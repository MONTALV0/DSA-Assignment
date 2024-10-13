import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The Contact class represents a contact with a name, number, and a reference to the next contact (for linked list)
class Contact {
    String name;  // The name of the contact
    long number;  // The phone number of the contact
    Contact next; // Reference to the next contact in the list

    // Constructor to initialize the contact details
    public Contact(String name, long number) {
        this.name = name;
        this.number = number;
        this.next = null;
    }
}

public class TeleVault2 extends JFrame {
    // Head of the contact linked list
    private static Contact head;
    // Display area to show messages and contact list in the UI
    private static JTextArea displayArea;

    // Constructor to set up the TeleVault GUI
    public TeleVault2() {
        setTitle("TeleVault Phone Book");  // Setting the window title
        setSize(400, 500);  // Setting the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Ensures program closes on exit
        setLayout(new FlowLayout());  // Using a simple flow layout for buttons and text area

        // Initialize and add display area for output
        displayArea = new JTextArea(15, 30);
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea));  // Add scrolling functionality to the text area

        // Create buttons for various actions
        JButton saveButton = new JButton("Save Contact");
        JButton deleteButton = new JButton("Delete Contact");
        JButton updateButton = new JButton("Update Contact");
        JButton displayButton = new JButton("Display All Contacts");
        JButton callButton = new JButton("Call Contact");
        JButton sortButton = new JButton("Sort Contacts");
        JButton searchButton = new JButton("Search Contact");

        // Add the buttons to the window
        add(saveButton);
        add(deleteButton);
        add(updateButton);
        add(displayButton);
        add(callButton);
        add(sortButton);
        add(searchButton);

        // Add action listeners to the buttons
        saveButton.addActionListener(e -> saveContact());  // Save a new contact
        deleteButton.addActionListener(e -> deleteContact());  // Delete a contact
        updateButton.addActionListener(e -> updateContact());  // Update a contact's details
        displayButton.addActionListener(e -> displayAllContacts());  // Display all contacts
        callButton.addActionListener(e -> callContact());  // Simulate calling a contact
        sortButton.addActionListener(e -> sortContacts());  // Sort contacts alphabetically
        searchButton.addActionListener(e -> searchContact());  // Search for a specific contact

        // Make the window visible
        setVisible(true);
    }

    // Simulate calling a contact
    private void callContact() {
        String name = JOptionPane.showInputDialog(this, "Who would you like to call? (Firstname Lastname)");
        if (name != null) {
            displayArea.append("Calling " + name + "\n");  // Show a message in the display area
        }
    }

    // Save a new contact to the phonebook
    private void saveContact() {
        // Get the name and phone number from the user
        String name = JOptionPane.showInputDialog(this, "What is the name of the person you would like to save? (Firstname Lastname)");
        String numberStr = JOptionPane.showInputDialog(this, "What is the phone number of the person you are saving? (+264 815945484)");
        
        // Ensure both inputs are not null
        if (name != null && numberStr != null) {
            long number = Long.parseLong(numberStr);  // Convert the phone number to a long
            Contact newContact = new Contact(name, number);  // Create a new contact
            
            // If the contact list is empty, set the new contact as head
            if (head == null) {
                head = newContact;
            } else {
                // Otherwise, add the new contact to the end of the linked list
                Contact current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newContact;
            }
            displayArea.append("Saving contact " + name + ": " + number + "\n");  // Show confirmation message
        }
    }

    // Delete a contact from the phonebook
    private void deleteContact() {
        String name = JOptionPane.showInputDialog(this, "What is the name of the contact you want to delete? (Firstname Lastname)");
        if (name != null) {
            // Check if the contact list is empty
            if (head == null) {
                displayArea.append("Contact list is empty.\n");
                return;
            }
            // If the contact to delete is the head, update head to the next contact
            if (head.name.equalsIgnoreCase(name)) {
                head = head.next;
                displayArea.append("Deleted contact: " + name + "\n");
                return;
            }
            // Search for the contact to delete
            Contact current = head;
            while (current.next != null) {
                if (current.next.name.equalsIgnoreCase(name)) {
                    current.next = current.next.next;  // Remove the contact from the list
                    displayArea.append("Deleted contact: " + name + "\n");
                    return;
                }
                current = current.next;
            }
            displayArea.append("Could not find " + name + " to delete.\n");  // Contact not found
        }
    }

    // Update the phone number of an existing contact
    private void updateContact() {
        String name = JOptionPane.showInputDialog(this, "What is the name of the contact you want to update? (Firstname Lastname)");
        if (name != null && !name.isEmpty()) {
            Contact current = head;
            boolean found = false;

            // Search through the contact list
            while (current != null) {
                if (current.name.equalsIgnoreCase(name)) {
                    // Contact found, prompt for new phone number
                    String newNumberStr = JOptionPane.showInputDialog(this, "Enter the new phone number for " + current.name + " (+264 815945484)");
                    if (newNumberStr != null && !newNumberStr.isEmpty()) {
                        try {
                            long newNumber = Long.parseLong(newNumberStr);
                            current.number = newNumber;  // Update the phone number
                            displayArea.append("Updated " + current.name + "'s number to: " + newNumber + "\n");
                            found = true;
                        } catch (NumberFormatException e) {
                            displayArea.append("Invalid phone number format. Please enter a valid number.\n");
                        }
                    } else {
                        displayArea.append("No new number provided for " + current.name + ". Update canceled.\n");
                    }
                    break;
                }
                current = current.next;
            }

            if (!found) {
                displayArea.append("Could not find contact: " + name + " to update.\n");  // Contact not found
            }
        } else {
            displayArea.append("No contact name provided. Update canceled.\n");
        }
    }

    // Display all contacts in the phonebook
    private void displayAllContacts() {
        if (head == null) {
            displayArea.append("No contacts available.\n");
            return;
        }
        displayArea.append("Contact List:\n");
        Contact current = head;
        while (current != null) {
            displayArea.append(current.name + ": " + current.number + "\n");
            current = current.next;
        }
    }

    // Sort the contacts alphabetically using Bubble Sort
    private void sortContacts() {
        if (head == null || head.next == null) {
            displayArea.append("No contacts available to sort.\n");
            return;
        }

        // Bubble Sort algorithm to sort contacts by name
        boolean swapped;
        do {
            swapped = false;
            Contact current = head;
            while (current != null && current.next != null) {
                if (current.name.compareToIgnoreCase(current.next.name) > 0) {
                    // Swap the contacts
                    String tempName = current.name;
                    long tempNumber = current.number;
                    current.name = current.next.name;
                    current.number = current.next.number;
                    current.next.name = tempName;
                    current.next.number = tempNumber;
                    swapped = true;  // Set flag to true to continue sorting
                }
                current = current.next;
            }
        } while (swapped);

        displayArea.append("Contacts sorted alphabetically.\n");  // Show confirmation message
    }

    // Search for a contact by name
    private void searchContact() {
        String name = JOptionPane.showInputDialog(this, "What is the name of the contact you want to search for? (Firstname Lastname)");
        if (name != null && !name.isEmpty()) {
            Contact current = head;
            while (current != null) {
                if (current.name.equalsIgnoreCase(name)) {
                    displayArea.append("Found: " + current.name + " - " + current.number + "\n");
                    return;  // Exit after finding the contact
                }
                current = current.next;
            }
            displayArea.append("Could not find " + name + "\n");  // Contact not found
        } else {
            displayArea.append("No contact name provided. Search canceled.\n");
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
       new TeleVault2();
    }
}
