package sortpom;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiFile;
import sortpom.logger.IdeaPluginLogger;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;

/**
 * Logic for the Sort menu command
 * 
 * @author bjorn
 * @since 2012-12-21
 */
public class SortPomMenuItem extends AnAction {

    /**
     * Performed when the menu command is chosen
     */
    @Override
    public void actionPerformed(final AnActionEvent e) {
        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (isPomFile(psiFile)) {
            sortFileContentAndSave(psiFile);
        }

    }

    private void sortFileContentAndSave(final PsiFile psiFile) {
        String sortedXml = createSortedXml(psiFile.getText());

        FileContentReplaceAction fileContentReplaceAction = new FileContentReplaceAction(sortedXml, psiFile);
        ApplicationManager.getApplication().runWriteAction(fileContentReplaceAction);
    }

    /**
     * Instantiate SortPom and sort the file content
     */
    private String createSortedXml(String fileContent) {
        SortPomImpl sortPom = new SortPomImpl();
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\n", true, true)
                .setIndent(2, false)
                .setSortEntities("", "", true)
                .setSortOrder("default_0_4_0.xml", "")
                .createPluginParameters();
        sortPom.setup(new IdeaPluginLogger(), pluginParameters);
        return sortPom.sortXml(fileContent);
    }

    /**
     * Determines if the menu command should be shown or not.
     * Only shown if the context indicates that the current file is a pom.xml file
     *
     * @param e Carries information on the invocation place and data available
     */
    @Override
    public void update(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (isPomFile(psiFile)) {
            e.getPresentation().setEnabledAndVisible(true);
        } else {
            e.getPresentation().setVisible(false);
        }

    }

    private boolean isPomFile(PsiFile psiFile) {
        return psiFile != null && "pom.xml".equalsIgnoreCase(psiFile.getName());
    }

}
