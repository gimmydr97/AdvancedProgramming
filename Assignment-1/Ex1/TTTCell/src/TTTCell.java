
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import javax.swing.JPanel;

public class TTTCell extends JPanel implements ActionListener, PropertyChangeListener {

    
    public TTTCell() {
        initComponents();
    }
    
    private String State = "Initial";

    public static final String PROP_STATE = "State";

    public String getState() {
        return State;
    }
    
    public void setIndex(int index){
        this.index = index;
    }
    public void setState(String State) {
        if(this.State.equals("Initial")){
            String oldState = this.State;
            try{
                vetoableChangeSupport.fireVetoableChange(PROP_STATE, oldState, State);
                this.State = State;
                
                if(State.equals("X")){
                    X.setBackground(Color.yellow);
                    O.setEnabled(false);
                    O.setText("");
                }
                else{
                    O.setBackground(Color.cyan);
                    X.setEnabled(false);
                    X.setText("");
                }
                propertyChangeSupport.fireIndexedPropertyChange(PROP_STATE,this.index, oldState, State);   
                
            }catch(PropertyVetoException e){ System.out.println(e.getMessage());
            //code to be executed if changed is rejected by somebody
            }
        }
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private transient final VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    @Override
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    @Override
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        X = new javax.swing.JButton();
        O = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255), 3));

        X.setText("X");
        X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XActionPerformed(evt);
            }
        });

        O.setText("O");
        O.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(X, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2))
            .addComponent(O, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(X, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(O, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void OActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OActionPerformed
        this.setState("O");
    }//GEN-LAST:event_OActionPerformed

    private void XActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XActionPerformed
        this.setState("X");
    }//GEN-LAST:event_XActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton O;
    private javax.swing.JButton X;
    // End of variables declaration//GEN-END:variables
    private int index;
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(!State.equals("Initial")){
           if(State.equals("X")){
            X.setBackground(new Color(240,240,240));
            O.setText("O");
           }else{
            O.setBackground(new Color(240,240,240));
            X.setText("X");   
           }
           State = "Initial"; 
        }
        X.setEnabled(true);
        O.setEnabled(true);
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
            if((boolean)evt.getNewValue() == true){
                if(State.equals("X")) {
                    X.setEnabled(false);
                    X.setBackground(Color.green);
                }
                else{
                    O.setEnabled(false);
                    O.setBackground(Color.green);
                } 
            }else{
                    X.setEnabled(false);
                    X.setBackground(new Color(240,240,240));
                    O.setEnabled(false);
                    O.setBackground(new Color(240,240,240));
            
            } 
    }
        
    
}
