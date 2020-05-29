package cdmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Troy
 */
public class MainScreen extends JFrame implements KeyListener, ActionListener, IChatHost
{

    private JButton btnConnect, btnSearch, btnExit, btnByTitle, btnByAuthor, btnByBarcode, btnProcessLog;
    private JButton btnPreOrder, btnInOrder, btnPostOrder, btnGraphical, btnHashSave, btnHashDisplay;
    private JButton btnCDSave, btnCDNew, btnRetrieveAction, btnRemoveAction, btnReturnAction, btnAddAction;
    private JButton btnRandomSort, btnMostlySort, btnReverseSort;

    private JTextField txtSearch, txtTitle, txtAuthor, txtSection, txtXPos, txtYPos, txtBarcode, txtSort;
    private JTextArea texProcessLog, texDescription;
    private JLabel lblSearch, lblSort, lblProcessLog, lblBinaryTree, lblHashMap, lblTitle, lblAuthor;
    private JLabel lblSection, lblXPos, lblYPos, lblBarcode, lblDescription, lblActionRequest, lblSortSection;
    private JLabel lblConnected, lblConnStatus;
    private JTable dtCDTable;
    private JPanel tablePanel;
    private MyModel tableModel;
    
    TableRowSorter sorter;

    private int currentEntry= 0;
    private boolean newEntry = true;

    private ArrayList<Object[]> dataValues;
    private final DLinkedList dList = new DLinkedList();
    private final BinaryTree BinTree = new BinaryTree();

    private final String fileName = "CDLibraryData2.ser";

    SpringLayout mySpringLayout = new SpringLayout();
    
    private ChatClient myClient;
    private String serverName = "localhost";
    private int serverPort = 4444;
    private boolean isConnected = false;

    public void Run()
    {
        setBounds(20, 20, 1100, 700);
        setTitle("CD Manager Console");
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        dataValues = DataUtilities.ReadSerialisedData(fileName);
        SetupBinaryTree();
        SetupGui();
        setVisible(true);
        DisplayCurrentEntry(currentEntry);
        ConnectToServer(serverName, serverPort);
        
    }

    //<editor-fold defaultstate="collapsed" desc="GUI Setup"> 
    private void SetupGui()
    {
        setLayout(mySpringLayout);
        SetupButtons(mySpringLayout);
        SetupLabels(mySpringLayout);
        SetupTextFields(mySpringLayout);
        SetupTextAreas(mySpringLayout);
        SetupDataTable(mySpringLayout);
    }

    private void SetupButtons(SpringLayout layout)
    {
        btnConnect = LibraryComponents.LocateAJButton(this, this, layout, "CONNECT", 20, 5, 100, 40);
        btnSearch = LibraryComponents.LocateAJButton(this, this, layout, "Search", 640, 20, 80, 20);
        btnExit = LibraryComponents.LocateAJButton(this, this, layout, "Exit", 875, 625, 200, 20);
        btnByTitle = LibraryComponents.LocateAJButton(this, this, layout, "By Title", 80, 323, 90, 20);
        btnByAuthor = LibraryComponents.LocateAJButton(this, this, layout, "By Author", 175, 323, 90, 20);
        btnByBarcode = LibraryComponents.LocateAJButton(this, this, layout, "By Barcode", 300, 323, 100, 20);
        btnProcessLog = LibraryComponents.LocateAJButton(this, this, layout, "Process Log", 605, 360, 110, 20);
        btnPreOrder = LibraryComponents.LocateAJButton(this, this, layout, "Pre-Order", 175, 575, 110, 20);
        btnInOrder = LibraryComponents.LocateAJButton(this, this, layout, "In-Order", 285, 575, 110, 20);
        btnPostOrder = LibraryComponents.LocateAJButton(this, this, layout, "Post-Order", 395, 575, 110, 20);
        btnGraphical = LibraryComponents.LocateAJButton(this, this, layout, "Graphical", 505, 575, 110, 20);
        btnHashSave = LibraryComponents.LocateAJButton(this, this, layout, "Save", 175, 600, 110, 20);
        btnHashDisplay = LibraryComponents.LocateAJButton(this, this, layout, "Display", 285, 600, 110, 20);
        btnCDSave = LibraryComponents.LocateAJButton(this, this, layout, "Save/Update", 935, 280, 110, 20);
        btnCDNew = LibraryComponents.LocateAJButton(this, this, layout, "New Item", 750, 280, 110, 20);
        btnRetrieveAction = LibraryComponents.LocateAJButton(this, this, layout, "Retrieve", 750, 385, 110, 20);
        btnRemoveAction = LibraryComponents.LocateAJButton(this, this, layout, "Remove", 900, 385, 150, 20);
        btnReturnAction = LibraryComponents.LocateAJButton(this, this, layout, "Return", 750, 415, 110, 20);
        btnAddAction = LibraryComponents.LocateAJButton(this, this, layout, "Add To Collection", 900, 415, 150, 20);
        btnRandomSort = LibraryComponents.LocateAJButton(this, this, layout, "Random Collection Sort", 850, 490, 175, 20);
        btnMostlySort = LibraryComponents.LocateAJButton(this, this, layout, "Mostly Sort", 850, 515, 175, 20);
        btnReverseSort = LibraryComponents.LocateAJButton(this, this, layout, "Reverse Sort", 850, 540, 175, 20);

    }

    private void SetupLabels(SpringLayout layout)
    {
        lblConnected = LibraryComponents.LocateAJLabel(this, layout, "", 125, 5);
        lblConnected.setFont(new Font("Courier", Font.BOLD, 15));
        
        lblSearch = LibraryComponents.LocateAJLabel(this, layout, "SEARCH:", 460, 20);
        lblSort = LibraryComponents.LocateAJLabel(this, layout, "SORT:", 25, 325);
        lblProcessLog = LibraryComponents.LocateAJLabel(this, layout, "PROCESS LOG:", 25, 360);
        lblBinaryTree = LibraryComponents.LocateAJLabel(this, layout, "DISPLAY BINARY TREE:", 25, 575);
        lblHashMap = LibraryComponents.LocateAJLabel(this, layout, "HASHMAP / SET:", 60, 600);
        lblTitle = LibraryComponents.LocateAJLabel(this, layout, "TITLE:", 750, 25);
        lblAuthor = LibraryComponents.LocateAJLabel(this, layout, "AUTHOR:", 750, 50);
        lblSection = LibraryComponents.LocateAJLabel(this, layout, "SECTION:", 750, 75);
        lblXPos = LibraryComponents.LocateAJLabel(this, layout, "X:", 750, 100);
        lblYPos = LibraryComponents.LocateAJLabel(this, layout, "Y:", 750, 125);
        lblBarcode = LibraryComponents.LocateAJLabel(this, layout, "BARCODE:", 750, 150);
        lblDescription = LibraryComponents.LocateAJLabel(this, layout, "DESCRIPTION:", 750, 175);
        lblActionRequest = LibraryComponents.LocateAJLabel(this, layout, "AUTOMATION ACTION REQUEST FOR ITEM ABOVE:", 750, 350);
        lblSortSection = LibraryComponents.LocateAJLabel(this, layout, "SORT SECTION:", 750, 465);
    }

    private void SetupTextFields(SpringLayout layout)
    {
        txtSearch = LibraryComponents.LocateAJTextField(this, this, layout, 10, 520, 20);
        txtSearch.addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i) *" + txtSearch.getText()+"*"));
                }
            }
        });
        
        txtTitle = LibraryComponents.LocateAJTextField(this, this, layout, 18, 850, 25);
        txtAuthor = LibraryComponents.LocateAJTextField(this, this, layout, 18, 850, 50);
        txtSection = LibraryComponents.LocateAJTextField(this, this, layout, 5, 850, 75);
        txtXPos = LibraryComponents.LocateAJTextField(this, this, layout, 5, 850, 100);
        txtYPos = LibraryComponents.LocateAJTextField(this, this, layout, 5, 850, 125);
        txtBarcode = LibraryComponents.LocateAJTextField(this, this, layout, 18, 850, 150);
        txtSort = LibraryComponents.LocateAJTextField(this, this, layout, 5, 850, 465);

    }

    private void SetupTextAreas(SpringLayout layout)
    {
        texProcessLog = LibraryComponents.LocateAJTextArea(this, layout, 25, 385, 10, 62);
        JScrollPane spProcessText = new JScrollPane(texProcessLog);
        spProcessText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        layout.putConstraint(SpringLayout.WEST, spProcessText, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, spProcessText, 385, SpringLayout.NORTH, this);
        add(spProcessText);

        texDescription = LibraryComponents.LocateAJTextArea(this, layout, 850, 175, 6, 18);
        texDescription.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        texDescription.setLineWrap(true);
        texDescription.setWrapStyleWord(true);

    }

    private void SetupDataTable(SpringLayout layout)
    {
        // Create a panel to hold all other components
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        add(tablePanel);

        // Create column names
        String columnNames[] =
        {
            "Title", "Author", "Section", "X", "Y", "Barcode", "Description", "On Loan"
        };

         //Create some dummy data
//        dataValues = new ArrayList();
//        dataValues.add(new Object[] {"Busn History 2017","Management","B","1","57","43783278","Business History - tax records, events and achievements",false});
//        dataValues.add(new Object[] {"Busn History 2018","Management","B","1","58","43783279","Business History - tax records, events and achievements",false});
//        dataValues.add(new Object[] {"Busn History 2019","Management","B","1","59","43784280","Business History - tax records, events and achievements",false});
//        dataValues.add(new Object[] {"Client A Project","Dev Team","D","4","7","43784760","Client A's project files and copyright material.",false});
//        dataValues.add(new Object[] {"Client A's Testing Records","Testing Team","T","2","18","43784880","Client A's project test plan, test cases and associated emails",false});
//        dataValues.add(new Object[] {"Web Site 2017","Web Team","W","1","27","43784901","Our business' 2017 web site development and graphics files",false});
//        dataValues.add(new Object[] {"HRM Records 2017","HR Team","B","1","60","43785102","Our business' 2017 human resource management records",false}) ;
//        dataValues.add(new Object[] {"Marketing Records 2017","Marketing","B","1","61","43785367","Our business' 2017 marketing and sales records",false});
//        dataValues.add(new Object[] {"Client B's Project","Dev Team","D","4","15","43785444","Client B's project files and copyright material",false});
//        dataValues.add(new Object[] {"Client A's Testing Records","Testing Team","T","2","32","43785445","Client B's project test plan, test cases and associated emails",false});
//        dataValues.add(new Object[] {"Busn History 2018","Management","B","1","64","43785498","Business History - tax records, events and achievements in 2018",false});

        
        // constructor of JTable model
        tableModel = new MyModel(dataValues, columnNames);

        //Create Table
        dtCDTable = new JTable(tableModel);

        // Configure some of JTable's paramters
        dtCDTable.isForegroundSet();
        dtCDTable.setShowHorizontalLines(false);
        dtCDTable.setRowSelectionAllowed(true);
        dtCDTable.setColumnSelectionAllowed(true);
        dtCDTable.setPreferredSize(new Dimension(1000, 500));

        dtCDTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        dtCDTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        dtCDTable.getColumnModel().getColumn(2).setPreferredWidth(35);
        dtCDTable.getColumnModel().getColumn(3).setPreferredWidth(5);
        dtCDTable.getColumnModel().getColumn(4).setPreferredWidth(5);
        dtCDTable.getColumnModel().getColumn(5).setPreferredWidth(45);
        dtCDTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        dtCDTable.getColumnModel().getColumn(7).setPreferredWidth(35);

        //Add Mouse Listener To Select Table Row and Call Mthod To Display
        //Contents in Right Panel
        dtCDTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent me)
            {
                if (me.getClickCount() != 0)
                {
                JTable table = (JTable)me.getSource();
                currentEntry = table.getSelectedRow();
                newEntry = false;
                DisplayCurrentEntry(currentEntry);
                }
            }
        });

        //Add row sorter to table.
        dtCDTable.setRowSorter(new TableRowSorter(tableModel));
        sorter = (TableRowSorter)dtCDTable.getRowSorter();

        
                
        add(dtCDTable);

        // Change the text and background colours
        dtCDTable.setSelectionForeground(Color.white);
        dtCDTable.setSelectionBackground(Color.red);

        // Add the table to a scrolling pane, size and locate
        JScrollPane scrollPane = new JScrollPane(dtCDTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(700, 260));
        layout.putConstraint(SpringLayout.WEST, tablePanel, 20, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tablePanel, 50, SpringLayout.NORTH, this);
    }

    //</editor-fold>
    @Override
    public void keyTyped(KeyEvent ke){}

    @Override
    public void keyPressed(KeyEvent ke){}
 
    @Override
    public void keyReleased(KeyEvent ke){}
 

    //<editor-fold defaultstate="collapsed" desc="Button Events"> 
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == btnConnect)
        {
            ConnectToServer(serverName, serverPort);
        }
        
        
        //SearchButton to Apply row filter to data table
        //If any other button pressed Table reverts to original View.
        if (ae.getSource() == btnSearch)
        {
            sorter.setRowFilter(RowFilter.regexFilter("(?i) *" + txtSearch.getText()+"*")); 
        }
        if (ae.getSource() != btnSearch)
        {
            sorter.setRowFilter(RowFilter.regexFilter("")); 
        }
        
        //Button To exit Program 
        if (ae.getSource() == btnExit)
        {
            System.exit(0);
        }
        
        //Buttons to apply sorts to table view based upon Title/Author or Barcode
        //Uses individual sort algorithms for each search method
        if (ae.getSource() == btnByTitle)
        {
            dataValues = SortingUtilities.bubbleSort(dataValues, 0);
            tableModel.fireTableDataChanged();
        }
        if (ae.getSource() == btnByAuthor)
        {
            dataValues = SortingUtilities.bubbleSort(dataValues, 1);
            tableModel.fireTableDataChanged();
        }
        if (ae.getSource() == btnByBarcode)
        {
            dataValues = SortingUtilities.selectionSort(dataValues, 5);
            tableModel.fireTableDataChanged();
        }

        //Buttons For Adding New Records 
        //
        if (ae.getSource() == btnCDSave)
        {
            if (newEntry)
            {
                SaveNewRecord();
            }
            else
            {
                UpdateCurrentRecord(currentEntry);
            }
            
        }
        if (ae.getSource() == btnCDNew)
        {
            CreateNewRecord();
        }

        //Buttons for Hash Tree
        //
        if (ae.getSource() == btnHashSave)
        {
            SimpleHash.generateHashTable(dataValues);
        }
        
        
        if (ae.getSource() == btnProcessLog)
        {
            ShowProcessLog();
        }

        //Buttons For Binary Tree Traversal and Printing
        //
        if (ae.getSource() == btnPreOrder)
        {
            String binaryString = "";
            binaryString = BinTree.preOrderTraverseTree(binaryString, BinTree.root);
            texProcessLog.setText(binaryString);
        }

        if (ae.getSource() == btnInOrder)
        {
            String binaryString = "";
            binaryString = BinTree.inOrderTraverseTree(binaryString, BinTree.root);
            texProcessLog.setText(binaryString);
        }

        if (ae.getSource() == btnPostOrder)
        {
            String binaryString = "";
            binaryString = BinTree.postOrderTraverseTree(binaryString, BinTree.root);
            texProcessLog.setText(binaryString);
        }

        if (ae.getSource() == btnGraphical)
        {
            texProcessLog.setText("");
        }

        //Buttons For Automation Requests
        //
        if (ae.getSource() == btnAddAction)
        {
            UpdateProcessLog("ADD");
            SendAutomationRequest("ADD");
        }
        if (ae.getSource() == btnRetrieveAction)
        {
            UpdateProcessLog("RETRIEVE");
        }
        if (ae.getSource() == btnReturnAction)
        {
            UpdateProcessLog("RETURN");
        }
        if (ae.getSource() == btnRemoveAction)
        {
            UpdateProcessLog("REMOVE");
        }
         if (ae.getSource() == btnRandomSort)
        {
            
        }
    }

    //</editor-fold> 
    
    //<editor-fold defaultstate="collapsed" desc="Chat Server Methods">
    @Override
    public void ConnectToServer(String server, int port)
    {
        try
        {
            myClient = new ChatClient();
            isConnected = myClient.connect(server, port, this);
            myClient.send("Main");
        }
        catch(Exception e)
        {
            isConnected = false;
        }
        UpdateConnectionStatus();  
    }
    
    @Override
    public void HandleReply(String message)
    {
        texProcessLog.setText(message);
    }
    
    private void SendAutomationRequest(String request)
    {
        String myRequest = "<|>" + request;
        for (int i = 0; i < 8; i++)
        {
            myRequest += "<|>" + dataValues.get(currentEntry)[i].toString();
        }
        myClient.send(myRequest);
    }
    
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
    
    private void UpdateProcessLog(String action)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd  -  HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        dList.head.append(new Node(dtf.format(time) + " - SENT - " + action));
        ShowProcessLog();
    }

    private void ShowProcessLog()
    {
        Node head = dList.head;
        String processString = "";
        for (Node current = head.next; current != head; current = current.next)
        {
            processString += current.nodeString + "\n";
        }
        texProcessLog.setText(processString);
    }

    private void SetupBinaryTree()
    {
        for (int i = 0; i < dataValues.size(); i++)
        {
            BinTree.addNode(Integer.parseInt(dataValues.get(i)[5].toString()), dataValues.get(i)[0].toString());
        }
    }

    private void DisplayCurrentEntry(int row)
    {
        txtTitle.setText(dataValues.get(row)[0].toString());
        txtAuthor.setText(dataValues.get(row)[1].toString());
        txtSection.setText(dataValues.get(row)[2].toString());
        txtXPos.setText(dataValues.get(row)[3].toString());
        txtYPos.setText(dataValues.get(row)[4].toString());
        txtBarcode.setText(dataValues.get(row)[5].toString());
        texDescription.setText(dataValues.get(row)[6].toString());
        
        currentEntry = row;
    }

    private void CreateNewRecord()
    {
        newEntry = true;
        
        txtTitle.setText("");
        txtAuthor.setText("");
        txtSection.setText("");
        txtXPos.setText("");
        txtYPos.setText("");
        txtBarcode.setText("");
        texDescription.setText("");
    }
    
    private void UpdateCurrentRecord(int currentRecord)
    {
        if (ValidateEntryDetails())
        {
            boolean loanStatus = (boolean)dataValues.get(currentRecord)[7];
            dataValues.set(currentRecord,new Object[]
            {
                txtTitle.getText(),
                txtAuthor.getText(),
                txtSection.getText(),
                txtXPos.getText(),
                txtYPos.getText(),
                txtBarcode.getText(),
                texDescription.getText(),
                loanStatus
            });
            tableModel.fireTableDataChanged();
            DataUtilities.SaveSerialisedData(dataValues, fileName);
        }  
    }
    
    private void SaveNewRecord()
    {
        if(ValidateEntryDetails())
        {
            dataValues.add(new Object[]
            {
                txtTitle.getText(),
                txtAuthor.getText(),
                txtSection.getText(),
                txtXPos.getText(),
                txtYPos.getText(),
                txtBarcode.getText(),
                texDescription.getText(),
                false
            });
            tableModel.fireTableDataChanged();
            DataUtilities.SaveSerialisedData(dataValues, fileName);
        }
        else
        {
            System.out.print("Invalid Entry");
        }
    }
    
    private boolean ValidateEntryDetails()
    {
        boolean IsValid = true;
        
        if (txtTitle.getText().isEmpty()) {IsValid = false;}
        if (txtAuthor.getText().isEmpty()){IsValid = false;}
        if (txtSection.getText().isEmpty()) {IsValid = false;}
        if (txtXPos.getText().isEmpty()){IsValid = false;}
        if (txtYPos.getText().isEmpty()) {IsValid = false;}
        if (txtBarcode.getText().isEmpty()){IsValid = false;}
        if (texDescription.getText().isEmpty()) {IsValid = false;}
        if (!CheckForInteger(txtXPos.getText())) {IsValid = false;}
        if (!CheckForInteger(txtYPos.getText())) {IsValid = false;}
        if (!CheckForInteger(txtBarcode.getText())) {IsValid = false;}
        
        return IsValid;
    }
    
    private boolean CheckForInteger(String strNum)
    {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //</editor-fold> 
    
    //<editor-fold defaultstate="collapsed" desc="Table Model Class"> 
    //---------------------------------------------------------------------------------------------------
    // Source: http://www.dreamincode.net/forums/topic/231112-from-basic-jtable-to-a-jtable-with-a-tablemodel/
    // class that extends the AbstractTableModel
    //---------------------------------------------------------------------------------------------------
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
            if (column == 7)
            {
                return Boolean.class;
            } else
            {
                return String.class;
            }
        }
    }
    //</editor-fold> 

}
