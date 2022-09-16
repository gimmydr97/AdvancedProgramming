
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import javax.swing.JLabel;


public class TTTController  extends JLabel implements VetoableChangeListener, ActionListener, PropertyChangeListener{

    public TTTController() {
        initComponents();
        this.setOpaque(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 204, 51));
        setText("     START GAME");
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void vetoableChange(PropertyChangeEvent evt)throws PropertyVetoException{
        //throw new UnsupportedOperationException("Not supported yet."); 
         
        if(previous.equals("start")){   
            if( evt.getNewValue().equals("X"))
                this.setText("     NEXT MOVE: O" );
            else
                this.setText("     NEXT MOVE: X");
            
            previous = (String)evt.getNewValue();
        }
        else if(evt.getNewValue().equals(previous)){
            throw new PropertyVetoException("Invalid Operation",evt);
        }
        else{
            this.setText("     NEXT MOVE: " + previous);
            previous = (String)evt.getNewValue();

        }

    }

    private String previous = "start";

    @Override
    public void actionPerformed(ActionEvent e) {
        previous = "start";
        this.setBackground(new Color(255,204,51));
        setText("     START GAME");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.setBackground(Color.green);
        this.setOpaque(true);
        this.setText("     THE WINNER");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
