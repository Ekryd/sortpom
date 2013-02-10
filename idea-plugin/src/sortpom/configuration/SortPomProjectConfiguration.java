package sortpom.configuration;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author bjorn
 * @since 2013-01-02
 */
public class SortPomProjectConfiguration implements Configurable {
    private static final Logger LOG = Logger.getInstance(SortPomProjectConfiguration.class);
    private Project project;

    private JPanel myPanel;
    private JTextField sortDependenciesField;
    private JTextField sortPluginsField;

    private JComboBox sortOrderField;
    private JComboBox lineSeparatorField;
    private JComboBox indentField;

    private JCheckBox sortPropertiesField;
    private JCheckBox expandEmptyElementsField;
    private JCheckBox keepBlankLinesField;
    private JCheckBox indentBlankLines;


    /**
     * Creates a setting instance
     *
     * @param project reference to current project. This is set automatically (magic!)
     */
    public SortPomProjectConfiguration(Project project) {
        this.project = project;
        addListenersToCheckBoxes();
        addDropDownElements();
        loadValues();
    }

    private void addDropDownElements() {
        DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("default_1_0_0");
        defaultComboBoxModel1.addElement("recommended_2008_06");
        defaultComboBoxModel1.addElement("default_0_4_0");
        defaultComboBoxModel1.addElement("custom_1");
        sortOrderField.setModel(defaultComboBoxModel1);

        lineSeparatorField.setModel(new DefaultComboBoxModel(LineEndingSettingChoice.getDropDownValues()));

        indentField.setModel(new DefaultComboBoxModel(IndentSettingChoice.getDropDownValues()));
    }

    private void addListenersToCheckBoxes() {
        sortPropertiesField.addItemListener(new CheckBoxLabelChangeListener("Yes", "No"));
        expandEmptyElementsField.addItemListener(new CheckBoxLabelChangeListener("Expand", "Do not expand"));
        keepBlankLinesField.addItemListener(new CheckBoxLabelChangeListener("Keep", "Do not keep"));
        indentBlankLines.addItemListener(new CheckBoxLabelChangeListener("Yes", "No"));

        keepBlankLinesField.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                indentBlankLines.setEnabled(keepBlankLinesField.isSelected());
            }
        });

    }

    private void loadValues() {
        LOG.info("Loading SortPom configuration");
        SortPomConfigurationData store = createDefaultConfiguration();

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        boolean success = propertiesComponent.loadFields(store);

        if (success) {
            updateFields(store);
        } else {
            LOG.error("Could not load SortPom configuration");
        }
    }

    private void updateFields(SortPomConfigurationData store) {
        sortDependenciesField.setText(store.sortDependencies);
        sortPluginsField.setText(store.sortPlugins);

        sortOrderField.setSelectedItem(store.predefinedSortOrder);
        lineSeparatorField.setSelectedItem(LineEndingSettingChoice.fromParameter(store.lineSeparator).dropDownValue);
        indentField.setSelectedItem(IndentSettingChoice.fromParameter(store.nrOfIndentSpace).dropDownValue);

        sortPropertiesField.setSelected(store.sortProperties);
        expandEmptyElementsField.setSelected(store.expandEmptyElements);
        keepBlankLinesField.setSelected(store.keepBlankLines);
        indentBlankLines.setSelected(store.indentBlankLines);

    }

    private SortPomConfigurationData createDefaultConfiguration() {
        return new SortPomConfigurationData(
                "",
                "",
                "default_1_0_0",
                "\\n",
                2,
                false,
                true,
                false,
                false);
    }

    public String getDisplayName() {
        return "SortPom settings";
    }

    public boolean isModified() {
        return true;
    }

    public JComponent createComponent() {
        return myPanel;
    }

    public Icon getIcon() {
        return null;
    }

    public void apply() {
        LOG.info("Saving SortPom configuration");
        SortPomConfigurationData store = new SortPomConfigurationData(
                sortDependenciesField.getText(),
                sortPluginsField.getText(),
                sortOrderField.getSelectedItem().toString(),
                LineEndingSettingChoice.fromDropDown(lineSeparatorField.getSelectedItem().toString()).parameterValue,
                IndentSettingChoice.fromDropDown(indentField.getSelectedItem().toString()).parameterValue,
                sortPropertiesField.isSelected(),
                expandEmptyElementsField.isSelected(),
                keepBlankLinesField.isSelected(),
                indentBlankLines.isSelected());

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        boolean success = propertiesComponent.saveFields(store);

        if (!success) {
            LOG.error("Could not save SortPom configuration");
        }
    }

    public void disposeUIResources() {

    }

    public String getHelpTopic() {
        return "preferences.lookFeel";
    }

    public void reset() {
        LOG.info("Resetting fields to default values");
        SortPomConfigurationData store = createDefaultConfiguration();
        updateFields(store);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        myPanel = new JPanel();
        myPanel.setLayout(new GridBagLayout());
        final JLabel label1 = new JLabel();
        label1.setFont(new Font("Segoe UI", label1.getFont().getStyle(), 14));
        label1.setText("Current SortPom settings:");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        myPanel.add(label1, gbc);
        sortOrderField = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        sortOrderField.setModel(defaultComboBoxModel1);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        myPanel.add(sortOrderField, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Predefined sortorder:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label2, gbc);
        sortDependenciesField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        myPanel.add(sortDependenciesField, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Sort depencencies:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label3, gbc);
        sortPluginsField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        myPanel.add(sortPluginsField, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Sort plugins:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label4, gbc);
        final JLabel label5 = new JLabel();
        label5.setText("Sort properties:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label5, gbc);
        sortPropertiesField = new JCheckBox();
        sortPropertiesField.setText("No");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        myPanel.add(sortPropertiesField, gbc);
        lineSeparatorField = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        lineSeparatorField.setModel(defaultComboBoxModel2);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        myPanel.add(lineSeparatorField, gbc);
        final JLabel label6 = new JLabel();
        label6.setText("Line separator:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label6, gbc);
        expandEmptyElementsField = new JCheckBox();
        expandEmptyElementsField.setSelected(true);
        expandEmptyElementsField.setText("Expand");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        myPanel.add(expandEmptyElementsField, gbc);
        final JLabel label7 = new JLabel();
        label7.setText("Empty elements:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label7, gbc);
        keepBlankLinesField = new JCheckBox();
        keepBlankLinesField.setText("Do not keep");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        myPanel.add(keepBlankLinesField, gbc);
        final JLabel label8 = new JLabel();
        label8.setText("Blank lines:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label8, gbc);
        final JLabel label9 = new JLabel();
        label9.setText("Indent:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label9, gbc);
        final JLabel label10 = new JLabel();
        label10.setText("Indent blank lines:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        myPanel.add(label10, gbc);
        indentBlankLines = new JCheckBox();
        indentBlankLines.setEnabled(false);
        indentBlankLines.setText("No");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        myPanel.add(indentBlankLines, gbc);
        indentField = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        indentField.setModel(defaultComboBoxModel3);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        myPanel.add(indentField, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return myPanel;
    }
}
