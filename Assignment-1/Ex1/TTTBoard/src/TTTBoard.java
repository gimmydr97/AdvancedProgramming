/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author gianm
 */
import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TTTBoard extends javax.swing.JFrame implements PropertyChangeListener {

    
    
    private boolean win = false;
    public static final String PROP_WIN = "win";

    public void setWin(boolean win) {
        boolean oldWin = this.win;
        this.win = win;
        propertyChangeSupport.firePropertyChange(PROP_WIN, oldWin, win);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    



    public TTTBoard() {
        
        initComponents();
        
        for(int i = 0; i < SIZE; i++){
            TTTCell cell = (TTTCell)this.jPanel1.getComponent(i);
            
            cell.setIndex(i);
            cell.addPropertyChangeListener(this);
            cell.addVetoableChangeListener(this.tTTController1);
            
            this.restart.addActionListener(cell);
            matrix[i/3][i%3] = "I";
        }
        this.restart.addActionListener(this.tTTController1);
        this.addPropertyChangeListener(this.tTTController1);
        
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tTTCell1 = new TTTCell();
        tTTCell2 = new TTTCell();
        tTTCell3 = new TTTCell();
        tTTCell4 = new TTTCell();
        tTTCell5 = new TTTCell();
        tTTCell6 = new TTTCell();
        tTTCell7 = new TTTCell();
        tTTCell8 = new TTTCell();
        tTTCell9 = new TTTCell();
        tTTController1 = new TTTController();
        restart = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 255));

        jPanel1.setLayout(new java.awt.GridLayout(3, 3, 2, 2));
        jPanel1.add(tTTCell1);
        jPanel1.add(tTTCell2);
        jPanel1.add(tTTCell3);
        jPanel1.add(tTTCell4);
        jPanel1.add(tTTCell5);
        jPanel1.add(tTTCell6);
        jPanel1.add(tTTCell7);
        jPanel1.add(tTTCell8);
        jPanel1.add(tTTCell9);

        restart.setBackground(new java.awt.Color(255, 0, 0));
        restart.setText("RESTART");
        restart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tTTController1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125)
                        .addComponent(restart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(restart, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                    .addComponent(tTTController1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void restartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartActionPerformed
        for(int i = 0; i < SIZE; i++){
            matrix[i/3][i%3] = "I";
        }
        for(int i = 0; i < SIZE; i++){
                this.removePropertyChangeListener((TTTCell)this.jPanel1.getComponent(i));
        }
        count = 0;
        winners = new ArrayList(3);
    }//GEN-LAST:event_restartActionPerformed

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public static void main(String args[]) {
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TTTBoard().setVisible(true); 
                
            }
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton restart;
    private TTTCell tTTCell1;
    private TTTCell tTTCell2;
    private TTTCell tTTCell3;
    private TTTCell tTTCell4;
    private TTTCell tTTCell5;
    private TTTCell tTTCell6;
    private TTTCell tTTCell7;
    private TTTCell tTTCell8;
    private TTTCell tTTCell9;
    private TTTController tTTController1;
    // End of variables declaration//GEN-END:variables
    int SIZE = 9;
    int count = 0;
    ArrayList<Integer> winners = new ArrayList(3);
    String matrix[][] = new String[3][3];
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        IndexedPropertyChangeEvent ievt = (IndexedPropertyChangeEvent)evt;
        int index = ievt.getIndex();
        
        matrix[index/3][index%3] = ((TTTCell)this.jPanel1.getComponent(index)).getState();
        
        if(count >= 4 ){
            
            Collections.addAll(winners, victory(matrix,index));
                if(!winners.isEmpty()){
                    Iterator<Integer> iter = winners.iterator();
                    while (iter.hasNext()) {
                        this.addPropertyChangeListener((TTTCell)this.jPanel1.getComponent(iter.next()));
                        
                    }
                    this.setWin(true);
                    for(int i = 0; i < SIZE; i++){
                        if(winners.contains(i)){
                           this.removePropertyChangeListener((TTTCell)this.jPanel1.getComponent(i));
                        }else{
                           this.addPropertyChangeListener((TTTCell)this.jPanel1.getComponent(i));
                        }
                        
                    }
                    this.setWin(false);
                }
        }
        count++;
        
        /*for(int i = 0; i < 3; i++){
            for(int j=0; j < 3; j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
        
        System.out.println("////////");
        for(int j = 0; j < winners.size(); j++){
            System.out.print(winners.get(j)+",");
        }
        System.out.println("");*/
    }
    
    public Integer[] victory(String[][] matrix, int index){
                
            if(index == 4){
                
                //row
                if( (matrix[index/3][index%3].equals(matrix[index/3][(index%3)-1])) &&
                    (matrix[index/3][index%3].equals(matrix[index/3][(index%3)+1]))
                  )
                   return new Integer[]{(index/3)*3 +(index%3)-1,(index/3)*3 +(index%3),(index/3)*3 +(index%3)+1};//X
                //column
                else if( (matrix[index/3][index%3].equals(matrix[(index/3)-1][(index%3)])) &&
                    (matrix[index/3][index%3].equals(matrix[(index/3)+1][(index%3)]))
                  )
                
                   return new Integer[]{((index/3)-1)*3 +(index%3),(index/3)*3 +(index%3),((index/3)+1)*3 +(index%3)};//X
                //diagonal
                else if( (matrix[index/3][index%3].equals(matrix[(index/3)-1][(index-1)%3])) &&
                    (matrix[index/3][index%3].equals(matrix[(index/3)+1][(index+1)%3]))
                  ){
                   
                   return new Integer[]{((index/3)-1)*3+(index%3)-1,(index/3)*3 +(index%3),((index/3)+1)*3+(index%3)+1};}//NO
                //controdiagonal
                else if( (matrix[index/3][index%3].equals(matrix[(index/3)-1][(index%3)+1])) &&
                    (matrix[index/3][index%3].equals(matrix[(index/3)+1][(index%3)-1]))
                  )
                  
                   return new Integer[]{((index/3)-1)*3+(index%3)+1,(index/3)*3 +(index%3),((index/3)+1)*3+(index%3)-1};//NO
                
            }
            else if(index%2 == 0){//if is even
                //row
                if( (matrix[index/3][index%3].equals(matrix[(index/3)][(index+1) %3])) &&
                    (matrix[index/3][index%3].equals(matrix[(index/3)][(index+2) %3]))
                  )
                   return new Integer[]{(index/3)*3 +(index+1)%3, (index/3)*3+(index%3),(index/3)*3 +(index+2)%3};//X
                //column
                if( (matrix[index/3][index%3].equals(matrix[((index/3)+1)%3][(index%3)])) &&
                    (matrix[index/3][index%3].equals(matrix[((index/3)+2)%3][(index%3)]))
                  ) 
                   return new Integer[]{(((index/3)+1)%3)*3 +(index%3),(index/3)*3+(index%3),(((index/3)+2)%3)*3+(index%3)};//X
                
                //diagonal
               if( (matrix[index/3][index%3].equals(matrix[((index/3)+1)%3][((index+1)%3)])) &&
                    (matrix[index/3][index%3].equals(matrix[((index/3)+2)%3][((index+2)%3)]))
                  ){
                    
                    return new Integer[]{(((index/3)+1)%3)*3 +(index+1)%3,
                                           (index/3)*3+(index%3),
                                           (((index/3)+2)%3)*3+(index+2)%3
                                        };//X
                }
               //coontrodiagonal
               if( (matrix[index/3][index%3].equals(matrix[((index/3)+1)%3][((index+2)%3)])) &&
                    (matrix[index/3][index%3].equals(matrix[((index/3)+2)%3][((index+1)%3)]))
                  ){
                    
                    return new Integer[]{(((index/3)+1)%3)*3 +(index+2)%3,
                                           (index/3)*3+(index%3),
                                           (((index/3)+2)%3)*3+((index+1)%3)
                                        };//X
                }
               
            }
            else if(index%2 == 1){//if is odd
                //row
                if( (matrix[index/3][index%3].equals(matrix[(index/3)][(index+1) %3])) &&
                    (matrix[index/3][index%3].equals(matrix[(index/3)][(index+2) %3]))
                  )
                   return new Integer[]{(index/3)*3 +(index+1)%3, (index/3)*3 +(index%3),(index/3)*3 +(index+2)%3};//X  
                //column
                if( (matrix[index/3][index%3].equals(matrix[((index/3)+1)%3][(index%3)])) &&
                    (matrix[index/3][index%3].equals(matrix[((index/3)+2)%3][(index%3)]))
                  ) 
                   return new Integer[]{(((index/3)+1)%3)*3 +(index%3),(index/3)*3+(index%3),(((index/3)+2)%3)*3+(index%3)};//X         
            } 
        
        return new Integer[]{};
    }
}
