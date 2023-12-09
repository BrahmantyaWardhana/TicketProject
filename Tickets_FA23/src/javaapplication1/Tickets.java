package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
    String uname;
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
    JMenuItem mnuItemCloseTicket;
	JMenuItem mnuItemOpenTicketAdmin;
    JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewAllTicket;
    JMenuItem mnuItemViewMyTicket;

	public Tickets(Boolean isAdmin, String uname) {

		chkIfAdmin = isAdmin;
        this.uname=uname;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

        // options only available for admin users
        if (chkIfAdmin){
            // initialize first sub menu items for Admin main menu
		    mnuItemUpdate = new JMenuItem("Update Ticket");
		    // add to Admin main menu item
		    mnuAdmin.add(mnuItemUpdate);

		    // initialize second sub menu items for Admin main menu
		    mnuItemDelete = new JMenuItem("Delete Ticket");
		    // add to Admin main menu item
		    mnuAdmin.add(mnuItemDelete);

            // close ticket option
            mnuItemCloseTicket = new JMenuItem("Close Ticket");
            // add to main menu item
            mnuAdmin.add(mnuItemCloseTicket);

            // initialize second sub menu item for Tickets main menu
		    mnuItemViewAllTicket = new JMenuItem("View All Tickets");
		    // add to Ticket Main menu item
		    mnuTickets.add(mnuItemViewAllTicket);

            // initialize first sub menu item for Tickets main menu
		    mnuItemOpenTicketAdmin = new JMenuItem("Open Ticket");
		    // add to Ticket Main menu item
		    mnuTickets.add(mnuItemOpenTicketAdmin);
        }

        else {
            // initialize second sub menu item for Tickets main menu
		    mnuItemViewMyTicket = new JMenuItem("View My Tickets");
	        // add to Ticket Main menu item
	        mnuTickets.add(mnuItemViewMyTicket);

            // initialize first sub menu item for Tickets main menu
		    mnuItemOpenTicket = new JMenuItem("Open Ticket");
		    // add to Ticket Main menu item
		    mnuTickets.add(mnuItemOpenTicket);
        }

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);

        // options only available for admin users
        if (chkIfAdmin) {
            mnuItemUpdate.addActionListener(this);
		    mnuItemDelete.addActionListener(this);
            mnuItemCloseTicket.addActionListener(this);
            mnuItemViewAllTicket.addActionListener(this);
            mnuItemOpenTicketAdmin.addActionListener(this);
        }

        else {
            mnuItemViewMyTicket.addActionListener(this);
            mnuItemOpenTicket.addActionListener(this);
        }

		 /*
		  * continue implementing any other desired sub menu items (like 
		  * for update and delete sub menus for example) with similar 
		  * syntax & logic as shown above
		 */

	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar

        // options only available for admin users
        if (chkIfAdmin) {
            bar.add(mnuAdmin);
        }
		
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} 
        
        else if (e.getSource() == mnuItemOpenTicketAdmin) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter username");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			// insert ticket information to database

			int id = dao.insertRecords(ticketName, ticketDesc);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
		}

        else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = uname;
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter ticket description");

			// insert ticket information to database

			int id = dao.insertRecords(ticketName, ticketDesc);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
		}

		else if (e.getSource() == mnuItemViewAllTicket) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);

                // Allows frame to refresh
				getContentPane().removeAll(); 
                add(sp);
                revalidate(); 
                repaint();    

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

        else if (e.getSource() == mnuItemViewMyTicket) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.userTicket(uname)));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);

                // Allows frame to refresh
				getContentPane().removeAll(); 
                add(sp);
                revalidate(); 
                repaint();    

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

        // admin option to update tickets
        else if (e.getSource() == mnuItemUpdate) {
            // get ticket update information
            String ticketID = JOptionPane.showInputDialog(null, "Enter ticket ID");
			String ticketName = JOptionPane.showInputDialog(null, "Enter new username");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter new ticket description");

            try {
                dao.updateRecords(ticketID, ticketName, ticketDesc);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

        // admin option to delete tickets
        else if (e.getSource() == mnuItemDelete) {
            // get ticket id to delete
            String ticketID = JOptionPane.showInputDialog(null, "Enter ticket ID to delete");
            
            // display the ticket that is going to be deleted
            ResultSet ticketInfo = dao.findRecords(ticketID);
            try {
                if (ticketInfo.next()) {
                    String ticketInfoDetail = "Ticket ID: " + ticketInfo.getInt("ticket_id") + "\nIssuer: " + ticketInfo.getString("ticket_issuer") 
                    + "\nDescription: " + ticketInfo.getString("ticket_description") + "\nTicket status: " + ticketInfo.getString("ticket_status");

                    int deleteConfirm = JOptionPane.showConfirmDialog(null, "Delete the following ticket?\n" 
                    + ticketInfoDetail, "Confirm deletion request", JOptionPane.YES_NO_OPTION);

                    if (deleteConfirm==JOptionPane.YES_OPTION) {
                        dao.deleteRecords(ticketID);
                    }

                    else {
                        JOptionPane.showMessageDialog(null, "Request aborted");
                    }
                }

                else {
                    JOptionPane.showMessageDialog(null, "Ticket ID " + ticketID + " does not exist");
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        // close ticket option
        else if (e.getSource() == mnuItemCloseTicket) {
            // get ticket id to close
            String ticketID = JOptionPane.showInputDialog(null, "Enter ticket ID to close");
            
            // display the ticket that is going to be deleted
            ResultSet ticketInfo = dao.findRecords(ticketID);
            try {
                if (ticketInfo.next()) {
                    String ticketInfoDetail = "Ticket ID: " + ticketInfo.getInt("ticket_id") + "\nIssuer: " + ticketInfo.getString("ticket_issuer") 
                    + "\nDescription: " + ticketInfo.getString("ticket_description") + "\nTicket status: " + ticketInfo.getString("ticket_status");

                    int deleteConfirm = JOptionPane.showConfirmDialog(null, "Close the following ticket?\n" 
                    + ticketInfoDetail, "Confirm ticket closing request", JOptionPane.YES_NO_OPTION);

                    if (deleteConfirm==JOptionPane.YES_OPTION) {
                        dao.closeTicket(ticketID);
                    }

                    else {
                        JOptionPane.showMessageDialog(null, "Request aborted");
                    }
                }

                else {
                    JOptionPane.showMessageDialog(null, "Ticket ID " + ticketID + " does not exist");
                }
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */

	}

}
