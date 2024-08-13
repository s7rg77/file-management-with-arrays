import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class productos {

    private String code;
    private String name;
    private String amount;
    private String description;

    public productos() {
    }

    public productos(String code, String name, String amount,
            String description) {
        this.code = code;
        this.name = name;
        this.amount = amount;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String insert() {
        try {
            File fi = new File("java.txt");
            if (!fi.exists()) {
                fi.createNewFile();
            }
            FileReader fr = new FileReader(fi);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("code: " + code) && line.length()
                        == ("code: " + code).length()) {
                    br.close();
                    return "El código ya ha sido introducido\n"
                            + "anteriormente";
                }
            }

            FileWriter fw = new FileWriter(fi, true);
            fw.write("code: " + code + "\n" + "name: " + name + "\n"
                    + "amount: " + amount + "\n"
                    + "description: " + description + "\n");
            fw.close();
            fr.close();
        } catch (IOException ex) {
            return "Error";
        }
        return "Datos introducidos correctamente";
    }

    public String list() {
        String text = "";
        String line;

        try {
            File fi = new File("java.txt");
            FileReader fr = new FileReader(fi);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();

            while (line != null) {
                text = text + line + "\n";
                line = br.readLine();

            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }

    public String search() {
        String code = JOptionPane.showInputDialog("Introduzca "
                + "código");
        String line;
        String text = "";
        boolean bln = false;

        try {
            File fi = new File("java.txt");
            FileReader fr = new FileReader(fi);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();

            while (line != null) {
                if (line.startsWith("code: " + code)) {
                    for (int i = 0; i &lt; 4; i++) {
                        line = line.substring(line.indexOf(" ") + 1);
                        text = text + line + "\n";
                        line = br.readLine();
                        bln = true;
                    }
                    break;
                } else {
                    line = br.readLine();
                }
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        if (bln) {
            return text;
        } else {
            return "Error";
        }
    }

}

import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class metodos {

    final private String dat = "productos.dat";
    private ArrayList&lt;productos&gt; arraylist = new ArrayList&lt;productos&gt;();

    public metodos() {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(dat));
            productos p = new productos();
            p.setCode(dis.readUTF());
            while (p.getCode() != null) {
                p.setName(dis.readUTF());
                p.setAmount(dis.readUTF());
                p.setDescription(dis.readUTF());
                arraylist.add(p);
                p = new productos();
                p.setCode(dis.readUTF());
            }
            dis.close();
        } catch (Exception ex) {
        }
    }

    public void save() {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(dat));
            for (int i = 0; i &lt; arraylist.size(); i++) {
                dos.writeUTF(arraylist.get(i).getCode());
                dos.writeUTF(arraylist.get(i).getName());
                dos.writeUTF(arraylist.get(i).getAmount());
                dos.writeUTF(arraylist.get(i).getDescription());
            }
            dos.close();
        } catch (Exception ex) {
        }
    }

    public boolean insert(String code, String name, String amount,
            String description) {
        if (code.isEmpty() || name.isEmpty()
                || amount.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debe completar todos los campos",
                    "Mensaje", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean b = false;
        for (productos p : arraylist) {
            if (p.getCode().equals(code)) {
                b = true;
                break;
            }
        }
        if (b) {
            JOptionPane.showMessageDialog(null,
                    "El código ya ha sido introducido anteriormente",
                    "Mensaje", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        productos p = new productos();
        p.setCode(code);
        p.setName(name);
        p.setAmount(amount);
        p.setDescription(description);
        arraylist.add(p);
        save();
        JOptionPane.showMessageDialog(null,
                "El código ha sido introducido correctamente",
                "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public String search() {
        String[] options = {"Código exacto", "Comienza por:", "Contiene:"};
        int option = JOptionPane.showOptionDialog(null,
                "Seleccione una opción de búsqueda",
                "Opciones", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options,
                options);
        String text = "";
        boolean found = false;
        String code = JOptionPane.showInputDialog(null,
                "Introduzca código", "Código",
                JOptionPane.QUESTION_MESSAGE);
        if (code == null) {
            return "";
        }
        try {
            File file = new File("productos.dat");
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            while (dis.available() &gt; 0) {
                String fileCode = dis.readUTF();
                String name = dis.readUTF();
                String amount = dis.readUTF();
                String description = dis.readUTF();
                if (option == 0 && fileCode.equals(code)) {
                    text += "Código: " + fileCode + System.lineSeparator();
                    text += "Nombre: " + name + System.lineSeparator();
                    text += "Cantidad: " + amount + System.lineSeparator();
                    text += "Descripción: " + description
                            + System.lineSeparator();
                    found = true;
                    break;
                } else if (option == 1 && fileCode.startsWith(code)) {
                    text += "Código: " + fileCode + System.lineSeparator();
                    text += "Nombre: " + name + System.lineSeparator();
                    text += "Cantidad: " + amount + System.lineSeparator();
                    text += "Descripción: " + description
                            + System.lineSeparator();
                    found = true;
                } else if (option == 2 && fileCode.contains(code)) {
                    text += "Código: " + fileCode + System.lineSeparator();
                    text += "Nombre: " + name + System.lineSeparator();
                    text += "Cantidad: " + amount + System.lineSeparator();
                    text += "Descripción: " + description
                            + System.lineSeparator();
                    found = true;
                }
            }
            dis.close();
            fis.close();
        } catch (IOException e) {
        }
        if (!found) {
            JOptionPane.showMessageDialog(null,
                    "Código incorrecto",
                    "Mensaje", JOptionPane.ERROR_MESSAGE);
        }
        return text;
    }

    public String list() {
        String text = "";
        text = text + "Lista de productos:" + "\n" + "\n";
        for (int i = 0; i &lt; arraylist.size(); i++) {
            text = text + "código: " + arraylist.get(i).getCode() + "\n";
            text = text + "nombre: " + arraylist.get(i).getName() + "\n";
            text = text + "cantidad: "
                    + arraylist.get(i).getAmount() + "\n";
            text = text + "descripción: "
                    + arraylist.get(i).getDescription() + "\n";
            text = text + "\n";
        }
        return text;
    }

    public boolean modify(String code, String name, String amount,
            String description) {
        int mod = -1;
        for (int i = 0; i &lt; arraylist.size(); i++) {
            if (arraylist.get(i).getCode().equals(code)) {
                mod = i;
                break;
            }
        }
        if (mod &gt;= 0) {
            arraylist.get(mod).setName(name);
            arraylist.get(mod).setAmount(amount);
            arraylist.get(mod).setDescription(description);
            save();
            JOptionPane.showMessageDialog(null,
                    "Datos modificados correctamente",
                    "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                    "El código introducido no existe",
                    "Mensaje", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String delete() {
        Object[] options = {"Borrar un producto",
            "Borrar todos los productos"};
        int d = JOptionPane.showOptionDialog(null,
                "¿Qué desea hacer?", "Borrar productos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        if (d == JOptionPane.CLOSED_OPTION) {
            return "";
        } else if (d == 0) {
            String code = JOptionPane.showInputDialog("Ingrese código:");
            if (code == null) {
                return "";
            }
            int del = -1;
            for (int i = 0; i &lt; arraylist.size(); i++) {
                if (arraylist.get(i).getCode().equals(code)) {
                    del = i;
                    break;
                }
            }
            if (del &gt;= 0) {
                arraylist.remove(del);
                save();
            }
            if (del == -1) {
                JOptionPane.showMessageDialog(null, "El código "
                        + code + " no existe", "Mensaje",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "El producto con el código " + code
                        + " ha sido eliminado correctamente",
                        "Mensaje",
                        JOptionPane.INFORMATION_MESSAGE);
                return "deleted";
            }
        }
        if (d == 1) {
            int yes = JOptionPane.showConfirmDialog(null,
                    "Está seguro de que desea eliminar todo?",
                    "Confirmación", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (yes == JOptionPane.YES_OPTION) {
                arraylist.clear();
                save();
                JOptionPane.showMessageDialog(null,
                        "Todos los productos han sido eliminados",
                        "Mensaje",
                        JOptionPane.INFORMATION_MESSAGE);
                return "deleted";
            }
        }
        return "";
    }

}

import javax.swing.JOptionPane;

public class formulario extends javax.swing.JFrame {

    metodos m;

    public formulario() {
        initComponents();
        m = new metodos();
    }

    @SuppressWarnings("unchecked")
    // &lt;editor-fold defaultstate="collapsed" desc="Generated Code"&gt;//GEN-BEGIN:initComponents

    private void initComponents() {

        btninsert = new javax.swing.JButton();
        btnsearch = new javax.swing.JButton();
        lblcode = new javax.swing.JLabel();
        lblname = new javax.swing.JLabel();
        lblamount = new javax.swing.JLabel();
        lbldescription = new javax.swing.JLabel();
        txtcode = new javax.swing.JTextField();
        txtname = new javax.swing.JTextField();
        txtamount = new javax.swing.JTextField();
        txtdescription = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtlist = new javax.swing.JTextArea();
        btnshow = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        lblproductslist = new javax.swing.JLabel();
        btndelete = new javax.swing.JButton();
        btnmodify = new javax.swing.JButton();
        btnclean = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Formulario de Gestión");

        btninsert.setText("Insertar");
        btninsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btninsertActionPerformed(evt);
            }
        });

        btnsearch.setText("Buscar");
        btnsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsearchActionPerformed(evt);
            }
        });

        lblcode.setText("Código");

        lblname.setText("Nombre");

        lblamount.setText("Cantidad");

        lbldescription.setText("Descripción");

        txtcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcodeActionPerformed(evt);
            }
        });

        txtlist.setColumns(20);
        txtlist.setRows(5);
        jScrollPane1.setViewportView(txtlist);

        btnshow.setText("Mostrar");
        btnshow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnshowActionPerformed(evt);
            }
        });

        btnexit.setText("Salir");
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });

        lblproductslist.setText("Listado de productos");

        btndelete.setText("Borrar");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });

        btnmodify.setText("Modificar");
        btnmodify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodifyActionPerformed(evt);
            }
        });

        btnclean.setText("Limpiar");
        btnclean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncleanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbldescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                        .addComponent(btnmodify, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblamount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btninsert, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(btnsearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                    .addComponent(btndelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtcode, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(txtname)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtamount, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 1, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtdescription)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblproductslist, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnclean, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnshow, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(btnexit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(lblproductslist, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblcode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblname, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblamount, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbldescription, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtdescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btninsert)
                    .addComponent(btnsearch)
                    .addComponent(btnshow)
                    .addComponent(btnexit))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btndelete)
                    .addComponent(btnmodify)
                    .addComponent(btnclean))
                .addGap(30, 30, 30))
        );

        pack();
    }// &lt;/editor-fold&gt;//GEN-END:initComponents

    private void txtcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcodeActionPerformed

    }//GEN-LAST:event_txtcodeActionPerformed

    private void btninsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btninsertActionPerformed

        boolean b = m.insert(txtcode.getText(), txtname.getText(),
                txtamount.getText(), txtdescription.getText());
        if (!b) {
            txtcode.setText("Introduzca código");
            txtname.setText("Introduzca nombre");
            txtamount.setText("Introduzca cantidad");
            txtdescription.setText("Introduzca descripción");
        } else {
            txtlist.setText(m.list());
            txtcode.setText("");
            txtname.setText("");
            txtamount.setText("");
            txtdescription.setText("");
        }
    }//GEN-LAST:event_btninsertActionPerformed

    private void btnshowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnshowActionPerformed
        txtlist.setText(m.list());
        txtcode.setText("");
        txtname.setText("");
        txtamount.setText("");
        txtdescription.setText("");
    }//GEN-LAST:event_btnshowActionPerformed

    private void btnsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsearchActionPerformed
        String sea = m.search();
        txtlist.setText(sea);
    }//GEN-LAST:event_btnsearchActionPerformed

    private void btnexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnexitActionPerformed

    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        String del = m.delete();
        if ("deleted".equals(del)) {
            txtlist.setText(m.list());
        }
    }//GEN-LAST:event_btndeleteActionPerformed

    private void btnmodifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodifyActionPerformed
        boolean mod = m.modify(txtcode.getText(), txtname.getText(),
                txtamount.getText(), txtdescription.getText());
        if (mod) {
            txtcode.setText("");
            txtname.setText("");
            txtamount.setText("");
            txtdescription.setText("");
            txtlist.setText(m.list());
        } else {
            txtcode.setText("Introduzca código");
            txtname.setText("Introduzca nombre nuevo");
            txtamount.setText("Introduzca cantidad nueva");
            txtdescription.setText("Introduzca descripción nueva");
        }
    }//GEN-LAST:event_btnmodifyActionPerformed

    private void btncleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncleanActionPerformed
        txtlist.setText("");
        txtcode.setText("");
        txtname.setText("");
        txtamount.setText("");
        txtdescription.setText("");
    }//GEN-LAST:event_btncleanActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formulario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnclean;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btninsert;
    private javax.swing.JButton btnmodify;
    private javax.swing.JButton btnsearch;
    private javax.swing.JButton btnshow;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblamount;
    private javax.swing.JLabel lblcode;
    private javax.swing.JLabel lbldescription;
    private javax.swing.JLabel lblname;
    private javax.swing.JLabel lblproductslist;
    private javax.swing.JTextField txtamount;
    private javax.swing.JTextField txtcode;
    private javax.swing.JTextField txtdescription;
    private javax.swing.JTextArea txtlist;
    private javax.swing.JTextField txtname;
    // End of variables declaration//GEN-END:variables

}