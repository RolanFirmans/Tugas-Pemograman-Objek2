
package transaksi;

import com.toedter.calendar.JDateChooser;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.remote.JMXConnectorFactory.connect;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import master.pelanggan;
//import static transaksi.Transaksi.txt_totalharga2;

//import static transaksi.Transaksi.txt_totalharga2;
//import static transaksi.Barang_Masuk.txt_kembalian;
//import static transaksi.Barang_Masuk.txt_totalharga;

/**
 *
 * @author Rolan Firmansyah
 */
public class Barang_Keluar extends javax.swing.JFrame {
 DefaultTableModel table = new DefaultTableModel();
  


    /**
     * Creates new form formTransaksi
     */
    public Barang_Keluar() {
        initComponents();
        koneksi.getKoneksi();
        totalBiaya();
        tanggal();
        
        
        barang_keluar.setModel(table);
        table.addColumn("No Keluar");
        table.addColumn("Kode Barang");
//        table.addColumn("Nama Distributor");
        table.addColumn("Nama Barang");
        table.addColumn("Harga");
        table.addColumn("Jumlah");
        table.addColumn("Total Harga");
        
        tampilData();
        auto();
    }
   //tanggal
    public void tanggal(){
        Date now = new Date();  
        txt_tanggal.setDate(now);    
    }
    
    // autonamber
    public void auto (){
        
     String query = "SELECT MAX(kode_barang) FROM barang_keluar";

     try {
         Connection connect = koneksi.getKoneksi(); // memanggil koneksi
         Statement sttmnt = connect.createStatement(); // membuat statement
         ResultSet rslt = sttmnt.executeQuery(query); // menjalankan query

         if (rslt.next()) {
             String kodeBarang = rslt.getString(1);

             if (kodeBarang == null) {
                 txt_kodebarang.setText("KD001");
             } else {
                 int set_kd = Integer.parseInt(kodeBarang.substring(2)) + 1;
                 String no = String.format("%03d", set_kd);
                 txt_kodebarang.setText("KD" + no);
             }
         }
     } catch (SQLException ex) {
         Logger.getLogger(Barang_Keluar.class.getName()).log(Level.SEVERE, null, ex);
     }
}
    
    
     private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = barang_keluar.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
         String query = "SELECT *, (harga * jumlah) AS total_harga FROM `barang_keluar`";

        
        try{
            Connection connect = koneksi.getKoneksi();//memanggil koneksi
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
           
            while (rslt.next()){
               Locale locale = new Locale("id", "ID");
               DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
               symbols.setGroupingSeparator('.');
               DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
               String formattedValue = decimalFormat.format(rslt.getInt(6));
               
                //menampung data sementara
                    String noMasuk = rslt.getString("no_keluar");
                    String kode_barang = rslt.getString("kode_barang");
//                  String distributor = rslt.getString("nama_distributor");
                    String nama_barang = rslt.getString("nama_barang");
                    String harga = rslt.getString("harga");
                    String jumlah = rslt.getString("jumlah");
                    String total = rslt.getString("total_harga");
                    
                Object[] ob = new Object[7];
                    ob[0] = rslt.getString(1);
                    ob[1] = rslt.getString(2);
                    ob[2] = rslt.getString(3);
                    ob[3] = decimalFormat.format(rslt.getInt(4));
                    //String biaya = 
                    ob[4] = rslt.getString(5);
                    ob[5] = decimalFormat.format(rslt.getInt(8));
                    
                //masukan semua data kedalam array
                String[] data = {noMasuk,kode_barang,nama_barang,harga,jumlah,total};
                
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(ob);
            }
                //mengeset nilai yang ditampung agar muncul di table
                barang_keluar.setModel(table);
                
                
        }catch(Exception e){
            System.out.println(e);
        }
       auto();
     
    }
    private void clear(){
        
        txt_kodebarang.setText(null);
//        txt_Distributor.setText(null);
        txt_nama_barang.setText(null);
        txt_harga.setText(null);
        txt_jumlah.setText(null);
        txt_totalharga.setText(null);
    }
  private void keranjang() {
      

    String kode = txt_kodebarang.getText();
    String nama_barang = txt_nama_barang.getText();
    String harga = txt_harga.getText();
    String jumlah = txt_jumlah.getText();
    String total = txt_totalharga.getText();
    
    
   // Mendapatkan tanggal terpilih dari JDateChooser
    Date tanggalTerpilih = txt_tanggal.getDate();

    
    if (tanggalTerpilih == null) {
        JOptionPane.showMessageDialog(null, "Tanggal belum dipilih!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String tanggal = dateFormat.format(tanggalTerpilih);
    
    try {
        // Query untuk mendapatkan ID terakhir
        Connection connect = koneksi.getKoneksi();
        String queryGetLastId = "SELECT MAX(no_keluar) FROM barang_keluar";
        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(queryGetLastId);
        
        int lastId = 0;
        if (rs.next()) {
            lastId = rs.getInt(1);
        }
        int newId = lastId + 1;
        
        // Query untuk memasukkan data
        String query = "INSERT INTO `barang_keluar` (`no_keluar`, `tgl_keluar`, `kode_barang`, `nama_barang`, `harga`, `jumlah`, `total_keluar`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, newId);
        ps.setString(2, tanggal);
        ps.setString(3, kode);
        ps.setString(4, nama_barang);
        ps.setString(5, harga);
        ps.setString(6, jumlah);
        ps.setString(7, total);
        
        ps.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data Masuk Ke-Keranjang");
    } catch (SQLException e) {
        System.out.println(e);
        JOptionPane.showMessageDialog(null, "Data Gagal Disimpan");
    } finally {
        tampilData();
        clear();
    }
    
    totalBiaya();
    auto();
 
}

                

    
    private void hapusData(){
        int i = barang_keluar.getSelectedRow();
        String noKeluar = table.getValueAt(i, 0).toString();
        Connection connect = koneksi.getKoneksi();

        String deleteQuery = "DELETE FROM `barang_keluar` WHERE `barang_keluar`.`no_keluar` = '"+noKeluar+"' ";
        String resetQuery = "UPDATE `barang_keluar` SET `no_keluar` = `no_keluar` - 1 WHERE `no_keluar` > '"+noKeluar+"' ";

        try {
            PreparedStatement deletePS = connect.prepareStatement(deleteQuery);
            deletePS.execute();

            PreparedStatement resetPS = connect.prepareStatement(resetQuery);
            resetPS.executeUpdate();

            tampilData();
            clear();
            totalBiaya();
            auto();
        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
        }

    }
    
    private void editData(){
        int i = barang_keluar.getSelectedRow();
        
        String noKeluar = table.getValueAt(i, 0).toString();
        String kode = txt_kodebarang.getText();
        String nama_barang = txt_nama_barang.getText();
        String harga = txt_harga.getText();
        String jumlah = txt_jumlah.getText();
        String total = txt_totalharga.getText();
        
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = String.valueOf(date.format(txt_tanggal.getDate()));
        
        Connection connect = koneksi.getKoneksi();
        
        String query = "UPDATE `barang_keluar` SET `kode_barang` = '"+kode+"',`nama_barang` = '"+nama_barang+"',`harga` = '"+harga+"', `jumlah` = '"+jumlah+"', `total_keluar` = '"+total+"' "
                + "WHERE `barang_keluar`.`no_keluar` = '"+noKeluar+"';";

        try{
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null , "Data Update");
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Gagal Update");
        }finally{
            tampilData();
            clear();
        }
        auto();
    }
    
    private void totalBiaya() {
    try {
    String hargaStr = txt_harga.getText();
    String jumlahStr = txt_jumlah.getText();

    if (hargaStr.isEmpty() || jumlahStr.isEmpty()) {
        // Tangani jika salah satu input kosong
        txt_totalharga.setText("0");
    } else {
        int harga = Integer.parseInt(hargaStr);
        int jumlah = Integer.parseInt(jumlahStr);

        int totalBiaya = harga * jumlah;
        txt_totalharga.setText(Integer.toString(totalBiaya));
    }
    } catch (NumberFormatException e) {
    System.out.println(e);
}

}

    
private void total() {
    String harga = txt_harga.getText();
    String jumlah = txt_jumlah.getText();

    try {
        int hargaa = Integer.parseInt(harga);
        int jumlahh = Integer.parseInt(jumlah);

        int total = hargaa * jumlahh;
        String total_harga = Integer.toString(total);

        txt_totalharga.setText(total_harga);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Only Numbers allowed");
        txt_harga.setText("");
        txt_jumlah.setText(null);
        txt_totalharga.setText("");
    }
}


    
    private void reset(){
//        txt_uang.setText(null);
    }
    
//    private void kembalian(){
//        String total = txt_totalharga2.getText();
//        String uang = txt_uang.getText();
//        
//        int totals = Integer.parseInt(total);
//        try{
//            int uangs = Integer.parseInt(uang);     
//            int kembali = (uangs - totals);
//            String fix = Integer.toString(kembali);
//            txt_kembalian.setText(fix);
//            JOptionPane.showMessageDialog(null, "Transaksi Berhasil!");
//        }catch(NumberFormatException | HeadlessException e){
//            JOptionPane.showMessageDialog(null, "Invalid Payment");
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txt_harga = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        barang_keluar = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_tanggal = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_nama_barang = new javax.swing.JTextField();
        txt_jumlah = new javax.swing.JTextField();
        txt_kodebarang = new javax.swing.JTextField();
        txt_totalharga = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(139, 203, 152));

        txt_harga.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hargaActionPerformed(evt);
            }
        });
        txt_harga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_hargaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_hargaKeyTyped(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nama Barang");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Harga");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Jumlah");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Total Harga");

        jButton4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_rewind_30px.png"))); // NOI18N
        jButton4.setText("  BACK");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        barang_keluar.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        barang_keluar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        barang_keluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barang_keluarMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(barang_keluar);

        jButton6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_delete_30px.png"))); // NOI18N
        jButton6.setText("  DELETE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_reset_30px.png"))); // NOI18N
        jButton7.setText("  RESET");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(192, 192, 192));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("BARANG KELUAR");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        txt_tanggal.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Kode Barang");

        jLabel12.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Rp.");

        txt_nama_barang.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_nama_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_nama_barangActionPerformed(evt);
            }
        });
        txt_nama_barang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_nama_barangKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_nama_barangKeyTyped(evt);
            }
        });

        txt_jumlah.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_jumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jumlahActionPerformed(evt);
            }
        });
        txt_jumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_jumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jumlahKeyTyped(evt);
            }
        });

        txt_kodebarang.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_kodebarang.setEnabled(false);
        txt_kodebarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodebarangActionPerformed(evt);
            }
        });
        txt_kodebarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_kodebarangKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_kodebarangKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_kodebarangKeyTyped(evt);
            }
        });

        txt_totalharga.setEditable(false);
        txt_totalharga.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_totalharga.setAutoscrolls(false);
        txt_totalharga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txt_totalhargaMouseReleased(evt);
            }
        });
        txt_totalharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalhargaActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_shopping_cart_30px_2.png"))); // NOI18N
        jButton8.setText("  ADD");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_edit_30px.png"))); // NOI18N
        jButton9.setText("  EDIT");
        jButton9.setPreferredSize(new java.awt.Dimension(117, 40));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_business_report_30px.png"))); // NOI18N
        jButton10.setText("LAPORAN");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_nama_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_kodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(txt_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(78, 78, 78)
                                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(jButton6)
                                        .addGap(27, 27, 27)
                                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(26, 26, 26)
                                        .addComponent(jButton10))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(690, 690, 690)
                                        .addComponent(jLabel6)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_tanggal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(106, 106, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_kodebarang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_nama_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_jumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10))))
                .addContainerGap(117, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_hargaActionPerformed
        // TODO add your handling code here:
       
      //
    }//GEN-LAST:event_txt_hargaActionPerformed

    private void txt_hargaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_hargaKeyReleased
        
    }//GEN-LAST:event_txt_hargaKeyReleased

    private void txt_hargaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_hargaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_hargaKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new user.menu_user().setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void barang_keluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barang_keluarMouseClicked
        // TODO add your handling code here:
         int baris = barang_keluar.getSelectedRow();

        String kode_barang = table.getValueAt(baris,1).toString();
        txt_kodebarang.setText(kode_barang);

//        String distributor = table.getValueAt(baris, 2).toString();
//        txt_Distributor.setText(distributor);

        String barang = table.getValueAt(baris, 2).toString();
        txt_nama_barang.setText(barang);
        
        String harga = table.getValueAt(baris, 3).toString();
        txt_harga.setText(harga);
        
        String jumlah = table.getValueAt(baris, 4).toString();
        txt_jumlah.setText(jumlah);
        
        String total = table.getValueAt(baris, 5).toString();
        txt_totalharga.setText(total);

//        String tanggal = table.getValueAt(baris, 7).toString();
//
//        Date convert = null;
//        try{
//            convert = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
//        }catch(Exception e){
//            System.out.println(e);
//        }
//        txt_tanggal.setDate(convert);
    }//GEN-LAST:event_barang_keluarMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        hapusData();
//        txt_uang.setText(null);
//        txt_kembalian.setText(null);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txt_nama_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_nama_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama_barangActionPerformed

    private void txt_nama_barangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nama_barangKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama_barangKeyReleased

    private void txt_nama_barangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_nama_barangKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_nama_barangKeyTyped

    private void txt_jumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jumlahActionPerformed
        // TODO add your handling code here:
        totalBiaya();
    }//GEN-LAST:event_txt_jumlahActionPerformed

    private void txt_jumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jumlahKeyReleased
        // TODO add your handling code here:
        total();
    }//GEN-LAST:event_txt_jumlahKeyReleased

    private void txt_jumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jumlahKeyTyped
        // TODO add your handling code here:
//        total()
    }//GEN-LAST:event_txt_jumlahKeyTyped

    private void txt_kodebarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodebarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kodebarangActionPerformed

    private void txt_kodebarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodebarangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kodebarangKeyPressed

    private void txt_kodebarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodebarangKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kodebarangKeyReleased

    private void txt_kodebarangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodebarangKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kodebarangKeyTyped

    private void txt_totalhargaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_totalhargaMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalhargaMouseReleased

    private void txt_totalhargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalhargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalhargaActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        keranjang();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        editData();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
    txt_kodebarang.setText("");
//    txt_Distributor.setText("");
    txt_nama_barang.setText("");
    txt_harga.setText("");
    txt_jumlah.setText("");
    txt_totalharga.setText("");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        transaksi.Laporan_barang_keluar bk = new transaksi.Laporan_barang_keluar();
        bk.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Barang_Keluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Barang_Keluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Barang_Keluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Barang_Keluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Barang_Keluar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable barang_keluar;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField txt_harga;
    public javax.swing.JTextField txt_jumlah;
    public javax.swing.JTextField txt_kodebarang;
    public javax.swing.JTextField txt_nama_barang;
    private com.toedter.calendar.JDateChooser txt_tanggal;
    public javax.swing.JTextField txt_totalharga;
    // End of variables declaration//GEN-END:variables

    
}
