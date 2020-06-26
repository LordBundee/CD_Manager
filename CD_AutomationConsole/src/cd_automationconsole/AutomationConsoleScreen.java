/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cd_automationconsole; 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Troy
 */
public class AutomationConsoleScreen extends JFrame implements ActionListener, KeyListener, IChatHost
{
    private JButton btnConnect, btnClose, btnProcess, btnAdd;
    private JLabel lblAction, lblBarcode, lblSection;
    private JLabel lblConnected, lblConnStatus;
    private JTextField txtAction, txtBarcode, txtSection;
    
    private JTable dtCDTable;
    private MyModel tableModel;
    private int targetEntry = 0;
    
    private ArrayList<Object[]> tableData;
    
    SpringLayout mySpringLayout = new SpringLayout();
    
    private ChatClient myClient;
    private Boolean isConnected;
    private String serverName = "localHost";
    private int serverPort = 4444;
    
    public void run()
    {
        setBounds(20, 20, 750, 425);
        setTitle("CD Automation Console");
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        SetupGUI();
        setVisible(true);
        ConnectToServer(serverName, serverPort);
    }
    
    //<editor-fold defaultstate="collapsed" desc="GUI Setup">
    
     /**This method calls all the appropriate methods for generating the UI screen for the application
     * sequentially to ensure that any element requiring an existing element's creation is called 
     * at the appropriate time*/
    public void SetupGUI()
    {
        setLayout(mySpringLayout);
        SetupButtons(mySpringLayout);
        SetupLabels(mySpringLayout);
        SetupTextFields(mySpringLayout);
        SetupTable(mySpringLayout);
    }
    
     /**This method uses the LocateAJButton method in the LibraryComponents class to setup and position
     each required button based upon the provided parameters.*/
    private void SetupButtons(SpringLayout layout)
    {
        btnConnect = LibraryComponents.LocateAJButton(this, this, layout, "CONNECT", 20, 5, 100, 40);
        btnClose = LibraryComponents.LocateAJButton(this, this, layout, "Close", 645, 350, 75, 20);
        btnProcess = LibraryComponents.LocateAJButton(this, this, layout, "Process", 615, 20, 100, 20);
        btnAdd = LibraryComponents.LocateAJButton(this, this, layout, "Add Item", 615, 50, 100, 20);
    }
    
    /**This method uses the LocateAJLabel method in the LibraryComponents class to setup and position
     each required label based upon the provided parameters.*/
    private void SetupLabels(SpringLayout layout)
    {
        lblConnected = LibraryComponents.LocateAJLabel(this, layout, "", 125, 5);
        lblConnected.setFont(new Font("Courier", Font.BOLD, 15));
        
        lblAction = LibraryComponents.LocateAJLabel(this, layout, "  Current Requested Action:", 265, 20);
        lblBarcode = LibraryComponents.LocateAJLabel(this, layout, "Barcode of Requested Item:", 265, 50);
        lblSection = lblBarcode = LibraryComponents.LocateAJLabel(this, layout, "Section:", 515, 50);
    }
    
    /**This method uses the LocateAJTextField method in the LibraryComponents class to setup and position
     *each required text field based upon the provided parameters.*/
    private void SetupTextFields(SpringLayout layout)
    {
        txtAction = LibraryComponents.LocateAJTextField(this, this, layout, 15, 425, 20);
        txtBarcode = LibraryComponents.LocateAJTextField(this, this, layout, 7, 425, 50);
        txtSection = LibraryComponents.LocateAJTextField(this, this, layout, 2, 565, 50);
    }
    
    /**This method first generates and sets up a Data table for the application UI. It sets properties such 
     * column sizes, dimensions and adds a mouse listener for table interaction. Additionally it assigns the 
     * table model and data to the table before presenting it on screen */
     private void SetupTable(SpringLayout layout)
    {
        // Create a panel to hold all other components
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        add(tablePanel);
        
         // Create column names
        String columnNames[] =
        {
            "REQUEST", "Title", "Author", "Section", "X", "Y", "Barcode", "Description", "On Loan"
        };
        
        //Create some dummy data
        tableData = new ArrayList();
        tableData.add(new Object[] {"ADD","Busn History 2017","Management","B","1","57","43783278","Business History - tax records, events and achievements",false});
        tableData.add(new Object[] {"RETRIEVE","Busn History 2018","Management","B","1","58","43783279","Business History - tax records, events and achievements",false});
        
        // constructor of JTable model
        tableModel = new MyModel(tableData, columnNames);
        
        dtCDTable = new JTable(tableModel);
        
           // Configure some of JTable's paramters
        dtCDTable.isForegroundSet();
        dtCDTable.setShowHorizontalLines(false);
        dtCDTable.setRowSelectionAllowed(true);
        dtCDTable.setColumnSelectionAllowed(true);
        dtCDTable.setPreferredSize(new Dimension(1000, 500));

        //Configure column widths
        dtCDTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        dtCDTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        dtCDTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        dtCDTable.getColumnModel().getColumn(3).setPreferredWidth(35);
        dtCDTable.getColumnModel().getColumn(4).setPreferredWidth(5);
        dtCDTable.getColumnModel().getColumn(5).setPreferredWidth(5);
        dtCDTable.getColumnModel().getColumn(6).setPreferredWidth(45);
        dtCDTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        dtCDTable.getColumnModel().getColumn(8).setPreferredWidth(35);
        
        dtCDTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent me)
            {
                if (me.getClickCount() != 0)
                {
                JTable table = (JTable)me.getSource();
                targetEntry = table.getSelectedRow();
                if (table.getRowSorter()!=null) {
                    targetEntry = table.getRowSorter().convertRowIndexToModel(targetEntry);
                }
                DisplayCurrentEntry(targetEntry);
                }
            }
        });
        
         add(dtCDTable);

        // Change the text and background colours
        dtCDTable.setSelectionForeground(Color.white);
        dtCDTable.setSelectionBackground(Color.red);

        // Add the table to a scrolling pane, size and locate
        JScrollPane scrollPane = new JScrollPane(dtCDTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(700, 260));
        layout.putConstraint(SpringLayout.WEST, tablePanel, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tablePanel, 75, SpringLayout.NORTH, this);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Button Events">
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == btnClose)
        {
            System.exit(0);
        }
        
        if (ae.getSource() == btnConnect)
        {
            ConnectToServer(serverName, serverPort);
        }
        
        if (ae.getSource() == btnProcess)
        {
            //Check reuest tag and call appropriate responce method
            if (tableData.get(targetEntry)[0].toString().contains("ADD"))
            {
                JOptionPane.showMessageDialog(this, "Please use the 'ADD ITEM' Button\n For requests of this type!");
                return;
            }
            else if(tableData.get(targetEntry)[0].toString().contains("SORT"))
            {
                JOptionPane.showMessageDialog(this, "Automation System \nSorting Section: " + tableData.get(targetEntry)[3].toString() + "now!");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Automation System \nProcessing " + tableData.get(targetEntry)[0].toString() + " request now!");
            }    
            
            if (tableData.get(targetEntry)[0].toString().contains("RETRIEVE") || tableData.get(targetEntry)[0].toString().contains("RETURN"))
            {
                UpdateLoanStatusToApplication(tableData.get(targetEntry)[6].toString(), tableData.get(targetEntry)[8].toString());
            }
            
            tableData.remove(targetEntry);
            tableModel.fireTableDataChanged();
        }
        
        if (ae.getSource() == btnAdd)
        {
            if (tableData.get(targetEntry)[0].toString().contains("ADD"))
            {
                JOptionPane.showMessageDialog(this, "ADDING selected item to collection!");
                tableData.remove(targetEntry);
                tableModel.fireTableDataChanged();
            }
            else
            {
            JOptionPane.showMessageDialog(this, "Cannot ADD this item! \nIt is already in the collection!");
            }    
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Chat Server Methods">
    
    /**Attempts to Connect to local chat server and setup open connection to it. It will then update the
     * isConnected boolean variable accordingly with the correct status. Once process is completed
     * it will run the UpdateConnectionStatus() method to display connection status on screen*/
    @Override
    public void ConnectToServer(String server, int port)
    {
        try
        {
            myClient = new ChatClient();
            isConnected = myClient.connect(server, port, this);
            myClient.send("Console");
        }
        catch(Exception e)
        {
            isConnected = false;
        }
        UpdateConnectionStatus();
    }
    
    /**Catches all replies from chat server once it has broken down the message it
     * diverts response information to the appropriagte method for further processing based upon
     * specified keyphrases*/
    @Override
    public void HandleReply(String msg)
    {
        String[] temp = msg.split(":");
        
        if (temp[1].trim().equals("ADD") || temp[1].trim().equals("RETURN") || temp[1].trim().equals("RETRIEVE") || temp[1].trim().equals("REMOVE"))
        {
            AddNewRequest(temp);
            myClient.send("<<RCVD>>" + "--" + temp[1].trim() + "--" + temp[2] + "--" + temp[7] );
        }
        else if(temp[1].trim().equals("SORT"))
        {
            AddSortRequest(temp);
            myClient.send("<<RCVD>>" + "--SORT----" + temp[2] + "--Section--" + temp[3] );
        }
    }
    
    /**Once called, this methos will check the connection status variable isConnected and update the
     * display next to the connection button appropriately to indicate the current status.*/
    private void UpdateConnectionStatus()
    {
        if (isConnected)
        {
            lblConnected.setText("#CONNECTED#");
            lblConnected.setForeground(Color.GREEN);
        }
        else
        {
            lblConnected.setText("#OFFLINE#");
            lblConnected.setForeground(Color.RED);
        }
    }
    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Helper Methods">
    
    /**Sends message to Main Console to update loan status of given entry*/
    private void UpdateLoanStatusToApplication(String barcode, String status)
    {
        //Check current onLoan status of item and send message to change to
        //opposing status.
        if (status.contains("true"))
        {
            myClient.send("UPDATE:" + barcode + ":" + "false");
        }
        else
        {
            myClient.send("UPDATE:" + barcode + ":" + "true");
        }
    }
    
    /**Once a new processing request has been this method creates a new entry in the data set based upon the 
     values given by the decoded message values. Once the new entry is created the table is called 
     * to update the display */
    private void AddNewRequest(String[] values)
    {
        //Check on loan status of new entry
        Boolean onLoan = false;
        if (values[9].trim().equals("true"))
        {
            onLoan = true;
        }
        
        //Create new entry and insert correct data
        tableData.add(new Object[] {
            values[1].trim(),   //Request
            values[2],          //Title
            values[3],          //Author
            values[4],          //Section
            values[5],          //XPos
            values[6],          //YPos
            values[7],          //Barcode
            values[8],          //Description
            onLoan});           //Onloan?
        
        //Update on screen data display
        tableModel.fireTableDataChanged();
    }
    
    /**Once a new sorting request has been this method creates a new entry in the data set based upon the 
     values given by the decoded message values. Once the new entry is created the table is called 
     * to update the display */
    private void AddSortRequest(String[] values)
    {
        //Generate data entry indicating sort request 
        tableData.add(new Object[] {
            values[1].trim(),       //Sort
            values[2],              //Sort type
            values[2].trim(),       //Sort type
            values[3],              //Section
            values[3],              //Section
            values[3],              //Section
            values[3],              //Barcode
            values[2],              //Sort type
            false});                //invalid - set to default
        
        //Update on screen table display
        tableModel.fireTableDataChanged();
    }
    
    /**gets the appropriate details from the selected request to update the barcode, action 
     * section text fields in the display */
    private void DisplayCurrentEntry(int entry)
    {
        //Determines whether request is a sort type or not
        if(tableData.get(entry)[0].toString().contains("SORT"))
        {
            txtBarcode.setText(tableData.get(entry)[0].toString());
        }
        else
        {
            txtBarcode.setText(tableData.get(entry)[6].toString());
        }
        txtAction.setText(tableData.get(entry)[0].toString());
        txtSection.setText(tableData.get(entry)[3].toString());
    }
    
    //</editor-fold> 
    
    //<editor-fold defaultstate="collapsed" desc="Table Model Class">
    class MyModel extends AbstractTableModel
    {
        
        ArrayList<Object[]> al;
        
        // the headers
        String[] header;
        
        // constructor
        MyModel(ArrayList<Object[]> obj, String[] header)
        {
            // save the header
            this.header = header;
            // and the data
            al = obj;
        }
        
        // method that needs to be overload. The row count is the size of the ArrayList
        public int getRowCount()
        {
            return al.size();
        }
        
        // method that needs to be overload. The column count is the size of our header
        public int getColumnCount()
        {
            return header.length;
        }
        
        // method that needs to be overload. The object is in the arrayList at rowIndex
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return al.get(rowIndex)[columnIndex];
        }
        
        // a method to return the column name
        public String getColumnName(int index)
        {
            return header[index];
        }
        
        // a method to add a new line to the table
        public void add(String word1, String word2)
        {
            // make it an array[2] as this is the way it is stored in the ArrayList
            // (not best design but we want simplicity)
            String[] str = new String[2];
            str[0] = word1;
            str[1] = word2;
            al.add(str);
            // inform the GUI that I have change
            fireTableDataChanged();
        }
        
        @Override
        public Class getColumnClass(int column)
        {
            if (column == 8)
            {
                return Boolean.class;
            } else
            {
                return String.class;
            }
        }
    }
//</editor-fold>
            
    //<editor-fold defaultstate="collapsed" desc="Unused Keylistener Overrides">
    
    @Override
    public void keyTyped(KeyEvent ke){}
    
    @Override
    public void keyPressed(KeyEvent ke){}
    
    @Override
    public void keyReleased(KeyEvent ke){}
    
//</editor-fold>
}
