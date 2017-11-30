import java.awt.*;
import java.awt.event.*;//重量级
import javax.swing.*;
import javax.swing.event.*;//轻量级
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import javax.swing.table.DefaultTableCellRenderer;
class WindowCalendar extends JFrame implements ItemListener,TreeSelectionListener
{                 //总结：代码有一定的复用率，这种默认布局很受影响，5位置5组件，默认值都不好，要调整
    JTable table;
    JTree tree = null;
    DefaultMutableTreeNode root;
    Object[] name1 = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};//国外  //可以做个地区选择下拉列表
    Object[] name2 = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};//国内
    JComboBox yearList;
    JComboBox areaList;
    int year,month;
    CalendarBean calendar;
    String[][] rili;
    String[] item = {"2010","2011","2012","2013","2014","2015","2016","2017","2018"};
    String[] area = {"国外","国内"};
    JScrollPane scrollTable,scrollTree;//表和树的滚动面板
    JSplitPane split;
    //end definition

    WindowCalendar(){
        init();
        setSize(980,400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //start init
    void init(){
        calendar = new CalendarBean();
        yearList = new JComboBox();//可以在下面添加一个地区列表
        for(int k = 0;k < item.length;k++)
            yearList.addItem(item[k]);
        yearList.addItemListener(this);
        areaList = new JComboBox();
        for(int k = 0;k < area.length;k++)
            areaList.addItem(area[k]);
        areaList.addItemListener(this);
        root = new DefaultMutableTreeNode(item[0]);//2010年为默认
        year = Integer.parseInt(item[0]);
        month = 1;
        DefaultMutableTreeNode[] mth = new DefaultMutableTreeNode[13];//子节点数组
        for(int i = 1;i <= 12;i++ ){
            mth[i] = new DefaultMutableTreeNode(""+i);//转换字符串，方便处理
            root.add(mth[i]);//在根节点上添加子节点
        }
        tree = new JTree(root);
        add(new JScrollPane(tree),BorderLayout.WEST);
        tree.addTreeSelectionListener(this);
        calendar.setYear(year);
        calendar.setMonth(month);
        rili = calendar.getCalendar();
        //table = new JTable(rili,name1); //设置不可编辑
         DefaultTableModel dtm = new DefaultTableModel(rili,name1){
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
         };  //匿名类重写方法使表格不可被编辑
        table = new JTable(dtm);
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
        // tcr.setHorizontalAlignment(JLabel.CENTER);
        tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
        table.setDefaultRenderer(Object.class, tcr);
        table.setRowHeight(30);//px为单位 设置行高
        scrollTree = new JScrollPane(tree);
        scrollTable = new JScrollPane(table);
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,scrollTree,scrollTable);//水平支撑做两个面板间隔
        split.setDividerLocation(0.5);
        add(yearList,BorderLayout.NORTH);
        add(areaList,BorderLayout.SOUTH);
        add(split,BorderLayout.CENTER);
    }//end init

    //start deal with event
    public void itemStateChanged(ItemEvent e){//下拉列表事件
        String yar = yearList.getSelectedItem().toString().trim();//获取年份并处理
        year = Integer.parseInt(yar); //两次转换保险起见
        calendar.setYear(year);
        root = new DefaultMutableTreeNode(yar);//节点放字符串，日历放整数，一旦点击下拉列表就重新获取年份添加节点，显示新日历
        DefaultMutableTreeNode[] mth = new DefaultMutableTreeNode[13];
        for(int i = 1;i <= 12;i++ ){
            mth[i] = new DefaultMutableTreeNode(""+i);
            root.add(mth[i]);
        }
        split.remove(scrollTree);//先移除面板，再新建树，再根据树新建面板
        tree = new JTree(root);
        tree.addTreeSelectionListener(this);
        scrollTree = new JScrollPane(tree);
        split.add(scrollTree,JSplitPane.LEFT);

        String area1 = areaList.getSelectedItem().toString().trim();//获取地区
        if(area1.equals(area[1])){
            rili = calendar.getCalendar(); //根据当前月份获取新日历 //二维表全部前移一位
            String[][] a = new String[6][7];
            for(int i = 0; i < 6;i++){
                for(int j = 0;j < 7;j++){
                    if(rili[i][j].isEmpty() == false && rili[0][0].equals( "1") == false) {
                        if(j != 0)
                            a[i][j - 1] = rili[i][j];
                        else
                            a[i-1][6] = rili[i][0];
                    }
                    else if(rili[i][j].isEmpty() == false && rili[0][0].equals( "1") == true){//转换时第一天为星期天 //集体下移一行
                        if(j != 0)
                            a[i+1][j - 1] = rili[i][j];
                        else
                            a[i][6] = rili[i][0];
                    }
                }
            }
            split.remove(scrollTable);
            //table = new JTable(a,name2);
            DefaultTableModel dtm = new DefaultTableModel(a,name2){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };  //匿名类重写方法使表格不可被编辑
            table = new JTable(dtm);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
            // tcr.setHorizontalAlignment(JLabel.CENTER);
            tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
            table.setDefaultRenderer(Object.class, tcr);
            table.setRowHeight(30);
            scrollTable = new JScrollPane(table);
            split.add(scrollTable,JSplitPane.RIGHT);

        }
        else{
            rili = calendar.getCalendar(); //国外
            split.remove(scrollTable);
            //table = new JTable(rili,name1);
            DefaultTableModel dtm = new DefaultTableModel(rili,name1){
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };  //匿名类重写方法使表格不可被编辑
            table = new JTable(dtm);
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
            // tcr.setHorizontalAlignment(JLabel.CENTER);
            tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
            table.setDefaultRenderer(Object.class, tcr);
            table.setRowHeight(30);
            scrollTable = new JScrollPane(table);
            split.add(scrollTable,JSplitPane.RIGHT);
        }
    }
    public void valueChanged(TreeSelectionEvent e){//树节点选中事件
        DefaultMutableTreeNode monthNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();//获取上次点击节点路径 //因为只有具体到月份才能转变当前面板（叶子节点）
        if(monthNode.isLeaf()){
            month = Integer.parseInt(monthNode.toString().trim());
            calendar.setMonth(month); //获取到叶子节点即月份，就移除之前面板，然后新建table，table所在面板再用split添加
            rili = calendar.getCalendar(); //根据当前月份获取新日历
            split.remove(scrollTable);
            String area1 = areaList.getSelectedItem().toString().trim();//获取地区
            if(area1.equals(area[0])) {
                //table = new JTable(rili, name1);// 内容默认
                DefaultTableModel dtm = new DefaultTableModel(rili,name1){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };  //匿名类重写方法使表格不可被编辑
                table = new JTable(dtm);
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
                // tcr.setHorizontalAlignment(JLabel.CENTER);
                tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
                table.setDefaultRenderer(Object.class, tcr);
                table.setRowHeight(30);
            }
            else {
               // table = new JTable(rili, name2);
                //如果非默认地区当前的rili与name要一同修改
                String[][] a = new String[6][7];
                for(int i = 0; i < 6;i++){
                    for(int j = 0;j < 7;j++){
                        if(rili[i][j].isEmpty() == false && rili[0][0].equals( "1") == false) {
                            if(j != 0)
                                a[i][j - 1] = rili[i][j];
                            else
                                a[i-1][6] = rili[i][0];
                        }
                        else if(rili[i][j].isEmpty() == false && rili[0][0].equals( "1") == true){//转换时第一天为星期天 //集体下移一行
                            if(j != 0)
                                a[i+1][j - 1] = rili[i][j];
                            else
                                a[i][6] = rili[i][0];
                        }
                    }
                }
                //table = new JTable(a,name2); //复用15行代码 同时修改了rili和name
                DefaultTableModel dtm = new DefaultTableModel(a,name2){
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };  //匿名类重写方法使表格不可被编辑
                table = new JTable(dtm);
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
                // tcr.setHorizontalAlignment(JLabel.CENTER);
                tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
                table.setDefaultRenderer(Object.class, tcr);
                table.setRowHeight(30);
            }
            scrollTable = new JScrollPane(table);
            split.add(scrollTable,JSplitPane.RIGHT);
        }
    }
}

