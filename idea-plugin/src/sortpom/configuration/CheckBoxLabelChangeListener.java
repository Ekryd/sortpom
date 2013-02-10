package sortpom.configuration;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Changes the checkbox label depending on the state of the checkbox
 * 
* @author bjorn
* @since 2013-01-02
*/
public class CheckBoxLabelChangeListener implements ItemListener
{
    private final String trueLabel;
    private final String falseLabel;

    public CheckBoxLabelChangeListener(String trueLabel, String falseLabel) {
        this.trueLabel = trueLabel;
        this.falseLabel = falseLabel;
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        JCheckBox source = (JCheckBox) itemEvent.getSource();
        source.setText(source.isSelected() ? trueLabel : falseLabel);
    }
}
